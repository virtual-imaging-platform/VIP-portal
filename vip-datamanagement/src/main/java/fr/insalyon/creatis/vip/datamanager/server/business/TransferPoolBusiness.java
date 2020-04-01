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
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation.Type;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Silva
 */
public class TransferPoolBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Server serverConfiguration = Server.getInstance();
    private LFCBusiness lfcBusiness;

    public TransferPoolBusiness() {
        lfcBusiness = new LFCBusiness();
    }

    /**
     *
     * @param email
     * @param date
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public List<PoolOperation> getOperations(
        String email, Date date, String currentUserFolder, Connection connection)
        throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            List<PoolOperation> poolOperations = new ArrayList<PoolOperation>();

            for (Operation operation : client.getOperationsLimitedListByUserAndDate(
                    email, DataManagerConstants.MAX_OPERATIONS_LIMIT, date)) {

                if (operation.getType() != Operation.Type.Delete) {
                    poolOperations.add(processOperation(operation, currentUserFolder, connection));
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

    /**
     *
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public List<PoolOperation> getOperations(
        String currentUserFolder, Connection connection)
        throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            List<PoolOperation> poolOperations = new ArrayList<PoolOperation>();

            for (Operation operation : client.getAllOperations()) {
                poolOperations.add(
                    processOperation(operation, currentUserFolder, connection));
            }

            return poolOperations;
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting all operations", ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param operationId
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public PoolOperation getOperationById(
        String operationId, String currentUserFolder, Connection connection)
        throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            return processOperation(
                client.getOperationById(operationId),
                currentUserFolder,
                connection);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting operation {}", operationId, ex);
            throw new BusinessException(ex);
        }
    }

    public PoolOperation getDownloadPoolOperation(String operationId) throws BusinessException {
        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            Operation operation = client.getOperationById(operationId);
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

    /**
     *
     * @param operation
     * @param currentUserFolder
     * @return
     * @throws DataManagerException
     */
    private PoolOperation processOperation(
        Operation operation, String currentUserFolder, Connection connection)
        throws DataManagerException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm");
        String source = "";
        String dest = "";
        PoolOperation.Type type = null;
        PoolOperation.Status status = PoolOperation.Status.valueOf(operation.getStatus().name());

        if (operation.getType() == Operation.Type.Upload) {
            type = PoolOperation.Type.Upload;
            source = FilenameUtils.getName(operation.getSource());
            dest = DataManagerUtil.parseRealDir(
                operation.getDest(), currentUserFolder, connection);
        } else if (operation.getType() == Operation.Type.Delete) {
            type = PoolOperation.Type.Delete;
            source = DataManagerUtil.parseRealDir(
                operation.getSource(), currentUserFolder, connection);
        } else {
            type = PoolOperation.Type.Download;
            dest = "Platform";
            source = operation.getType() == Operation.Type.Download
                    ? DataManagerUtil.parseRealDir(
                        operation.getSource(), currentUserFolder, connection)
                    : FilenameUtils.getBaseName(operation.getDest());
        }

        return new PoolOperation(operation.getId(), operation.getRegistration(),
                dateFormat.format(operation.getRegistration()), source, dest,
                type, status, operation.getUser(), operation.getProgress());
    }

    /**
     *
     * @param ids
     * @throws BusinessException
     */
    public void removeOperations(List<String> ids) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();

            for (String id : ids) {
                client.removeOperationById(id);
            }

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations {}", ids, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param email
     * @throws BusinessException
     */
    public void removeUserOperations(String email) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            client.removeOperationsByUser(email);

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations for {}", email, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param id
     * @throws BusinessException
     */
    public void removeOperationById(String id) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            client.removeOperationById(id);

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations {}", id, ex);
            throw new BusinessException(ex);
        }
    }

    public String downloadFile(
        User user, String remoteFile, Connection connection)
        throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFile, connection);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            String remotePath = DataManagerUtil.parseBaseDir(
                user, remoteFile, connection);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + FilenameUtils.getFullPath(remotePath);

            return poolClient.downloadFile(remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading file {} for {}", remoteFile, user, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param remoteFiles
     * @param packName
     * @return Operation ID
     * @throws BusinessException
     */
    public String downloadFiles(User user, List<String> remoteFiles,
                                String packName, Connection connection)
        throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFiles, connection);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            List<String> remotePaths = new ArrayList<String>();
            for (String remoteFile : remoteFiles) {
                remotePaths.add(
                    DataManagerUtil.parseBaseDir(user, remoteFile, connection));
            }
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads/" + packName;

            return poolClient.downloadFiles(remotePaths.toArray(new String[]{}),
                    localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading files {} for {}", remoteFiles, user, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param remoteFolder
     * @return Operation ID
     * @throws BusinessException
     */
    public String downloadFolder(
        User user, String remoteFolder, Connection connection)
        throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFolder, connection);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            String remotePath = DataManagerUtil.parseBaseDir(
                user, remoteFolder, connection);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + remotePath;
            return poolClient.downloadFolder(remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading folder {} for {}", remoteFolder, user, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param localFilePath
     * @param remoteFile
     * @return Operation ID
     * @throws BusinessException
     */
    public String uploadFile(
        User user, String localFilePath, String remoteFile,
        Connection connection)
        throws BusinessException {

        try {
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();
            String remotePath = DataManagerUtil.parseBaseDir(
                user, remoteFile, connection);
            return poolClient.uploadFile(localFilePath, remotePath, user.getEmail());
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error uploading file {} to {} for {}",
                    localFilePath, remoteFile, user, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param paths
     * @throws BusinessException
     */
    public void delete(User user, Connection connection, String... paths)
        throws BusinessException {

        try {
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            for (String path : paths) {
                String remotePath =
                    DataManagerUtil.parseBaseDir(user, path, connection);
                poolClient.delete(remotePath, user.getEmail());
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
