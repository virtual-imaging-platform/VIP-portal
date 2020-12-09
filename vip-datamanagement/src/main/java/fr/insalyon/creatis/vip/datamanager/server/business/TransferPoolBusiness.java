/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation.Type;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
@Service
@Transactional
public class TransferPoolBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server serverConfiguration;
    private LFCBusiness lfcBusiness;
    private GRIDAPoolClient gridaPoolClient;
    private LfcPathsBusiness lfcPathsBusiness;

    @Autowired
    public TransferPoolBusiness(Server serverConfiguration, LFCBusiness lfcBusiness, GRIDAPoolClient gridaPoolClient, LfcPathsBusiness lfcPathsBusiness) {
        this.serverConfiguration = serverConfiguration;
        this.lfcBusiness = lfcBusiness;
        this.gridaPoolClient = gridaPoolClient;
        this.lfcPathsBusiness = lfcPathsBusiness;
    }

    public List<PoolOperation> getOperations(
            String email, Date date, String currentUserFolder)
            throws BusinessException {

        try {
            List<PoolOperation> poolOperations = new ArrayList<>();

            for (Operation operation : gridaPoolClient.getOperationsLimitedListByUserAndDate(
                    email, DataManagerConstants.MAX_OPERATIONS_LIMIT, date)) {

                if (operation.getType() != Operation.Type.Delete) {
                    poolOperations.add(processOperation(operation, currentUserFolder));
                }
            }
            return poolOperations;

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting operations for {} since {}", email, date, ex);
            throw new BusinessException(ex);
        }
    }

    public List<PoolOperation> getOperations(String currentUserFolder)
            throws BusinessException {

        try {
            List<PoolOperation> poolOperations = new ArrayList<>();

            for (Operation operation : gridaPoolClient.getAllOperations()) {
                poolOperations.add(
                    processOperation(operation, currentUserFolder));
            }

            return poolOperations;
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting all operations", ex);
            throw new BusinessException(ex);
        }
    }

    public PoolOperation getOperationById(
            String operationId, String currentUserFolder)
            throws BusinessException {

        try {
            return processOperation(
                    gridaPoolClient.getOperationById(operationId),
                    currentUserFolder);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting operation {}", operationId, ex);
            throw new BusinessException(ex);
        }
    }

    public PoolOperation getDownloadPoolOperation(String operationId)
            throws BusinessException {
        try {
            Operation operation = gridaPoolClient.getOperationById(operationId);
            if (operation.getType() != Operation.Type.Download) {
                logger.error("Not a download operation {}", operationId);
                throw new BusinessException("Wrong operation type for download");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm");
            PoolOperation.Status status = PoolOperation.Status.valueOf(operation.getStatus().name());
            return new PoolOperation(operation.getId(), operation.getRegistration(),
                    dateFormat.format(operation.getRegistration()), operation.getSource(), operation.getDest(),
                    Type.Download, status, operation.getUser(), operation.getProgress());
        } catch (GRIDAClientException ex) {
            logger.error("Error getting download operation {}", operationId, ex);
            throw new BusinessException(ex);
        }
    }

    private PoolOperation processOperation(
            Operation operation, String currentUserFolder)
            throws DataManagerException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm");
        String source = "";
        String dest = "";
        PoolOperation.Type type = null;
        PoolOperation.Status status = PoolOperation.Status.valueOf(operation.getStatus().name());

        if (operation.getType() == Operation.Type.Upload) {
            type = PoolOperation.Type.Upload;
            source = FilenameUtils.getName(operation.getSource());
            dest = lfcPathsBusiness.parseRealDir(
                    operation.getDest(), currentUserFolder);
        } else if (operation.getType() == Operation.Type.Delete) {
            type = PoolOperation.Type.Delete;
            source = lfcPathsBusiness.parseRealDir(
                    operation.getSource(), currentUserFolder);
        } else {
            type = PoolOperation.Type.Download;
            dest = "Platform";
            source = operation.getType() == Operation.Type.Download
                    ? lfcPathsBusiness.parseRealDir(
                        operation.getSource(), currentUserFolder)
                    : FilenameUtils.getBaseName(operation.getDest());
        }

        return new PoolOperation(operation.getId(), operation.getRegistration(),
                dateFormat.format(operation.getRegistration()), source, dest,
                type, status, operation.getUser(), operation.getProgress());
    }

    public void removeOperations(List<String> ids) throws BusinessException {

        try {

            for (String id : ids) {
                gridaPoolClient.removeOperationById(id);
            }

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations {}", ids, ex);
            throw new BusinessException(ex);
        }
    }

    public void removeUserOperations(String email) throws BusinessException {

        try {
            gridaPoolClient.removeOperationsByUser(email);

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations for {}", email, ex);
            throw new BusinessException(ex);
        }
    }

    public void removeOperationById(String id) throws BusinessException {

        try {
            gridaPoolClient.removeOperationById(id);

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations {}", id, ex);
            throw new BusinessException(ex);
        }
    }

    public String downloadFile(User user, String remoteFile) throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFile);

            String remotePath = lfcPathsBusiness.parseBaseDir(user, remoteFile);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + FilenameUtils.getFullPath(remotePath);

            return gridaPoolClient.downloadFile(remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading file {} for {}", remoteFile, user, ex);
            throw new BusinessException(ex);
        }
    }

    public String downloadFiles(
            User user, List<String> remoteFiles, String packName)
            throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFiles);

            List<String> remotePaths = new ArrayList<>();
            for (String remoteFile : remoteFiles) {
                remotePaths.add(
                        lfcPathsBusiness.parseBaseDir(user, remoteFile));
            }
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads/" + packName;

            return gridaPoolClient.downloadFiles(remotePaths.toArray(new String[]{}),
                    localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading files {} for {}", remoteFiles, user, ex);
            throw new BusinessException(ex);
        }
    }

    public String downloadFolder(User user, String remoteFolder)
            throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFolder);

            String remotePath = lfcPathsBusiness.parseBaseDir(
                user, remoteFolder);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + remotePath;
            return gridaPoolClient.downloadFolder(
                    remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading folder {} for {}", remoteFolder, user, ex);
            throw new BusinessException(ex);
        }
    }

    public String uploadFile(User user, String localFilePath, String remoteFile)
            throws BusinessException {

        try {
            String remotePath = lfcPathsBusiness.parseBaseDir(user, remoteFile);
            return gridaPoolClient.uploadFile(localFilePath, remotePath, user.getEmail());
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error uploading file {} to {} for {}",
                    localFilePath, remoteFile, user, ex);
            throw new BusinessException(ex);
        }
    }

    public void delete(User user, String... paths)
        throws BusinessException {

        try {

            for (String path : paths) {
                String remotePath = lfcPathsBusiness.parseBaseDir(user, path);
                gridaPoolClient.delete(remotePath, user.getEmail());
            }
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error deleting files {} for {}",
                    Arrays.toString(paths), user, ex);
            throw new BusinessException(ex);
        }
    }
}
