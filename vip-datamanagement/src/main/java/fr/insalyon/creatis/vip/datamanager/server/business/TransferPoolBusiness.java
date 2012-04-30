/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class TransferPoolBusiness {

    private static Logger logger = Logger.getLogger(TransferPoolBusiness.class);
    private Server serverConfiguration = Server.getInstance();
    private LFCBusiness lfcBusiness;
    private int operationsLimit = 10;

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
    public List<PoolOperation> getOperations(String email, Date date, 
            String currentUserFolder) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            List<PoolOperation> poolOperations = new ArrayList<PoolOperation>();

            for (Operation operation : client.getOperationsLimitedListByUserAndDate(email, operationsLimit, date)) {
                
                if (operation.getType() != Operation.Type.Delete) {
                    poolOperations.add(processOperation(operation, currentUserFolder));
                }
            }
            return poolOperations;

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param currentUserFolder
     * @return
     * @throws BusinessException 
     */
    public List<PoolOperation> getOperations(String currentUserFolder) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            List<PoolOperation> poolOperations = new ArrayList<PoolOperation>();

            for (Operation operation : client.getAllOperations()) {
                poolOperations.add(processOperation(operation, currentUserFolder));
            }

            return poolOperations;

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
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
    public PoolOperation getOperationById(String operationId, 
            String currentUserFolder) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            return processOperation(client.getOperationById(operationId), currentUserFolder);

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
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
    private PoolOperation processOperation(Operation operation, 
            String currentUserFolder) throws DataManagerException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm");
        String source = "";
        String dest = "";
        PoolOperation.Type type = null;
        PoolOperation.Status status = PoolOperation.Status.valueOf(operation.getStatus().name());

        if (operation.getType() == Operation.Type.Upload) {
            type = PoolOperation.Type.Upload;
            source = FilenameUtils.getName(operation.getSource());
            dest = DataManagerUtil.parseRealDir(operation.getDest(), currentUserFolder);

        } else if (operation.getType() == Operation.Type.Delete) {
            type = PoolOperation.Type.Delete;
            source = DataManagerUtil.parseRealDir(operation.getSource(), currentUserFolder);

        } else {
            type = PoolOperation.Type.Download;
            dest = "Platform";
            source = operation.getType() == Operation.Type.Download
                    ? DataManagerUtil.parseRealDir(operation.getSource(), currentUserFolder)
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
            logger.error(ex);
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
            logger.error(ex);
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
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param email
     * @param remoteFile
     * @throws BusinessException 
     */
    public void downloadFile(User user, String remoteFile) throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFile);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            String remotePath = DataManagerUtil.parseBaseDir(user, remoteFile);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + FilenameUtils.getFullPath(remotePath);

            poolClient.downloadFile(remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param remoteFiles
     * @param packName
     * @throws BusinessException 
     */
    public void downloadFiles(User user, List<String> remoteFiles,
            String packName) throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFiles);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            List<String> remotePaths = new ArrayList<String>();
            for (String remoteFile : remoteFiles) {
                remotePaths.add(DataManagerUtil.parseBaseDir(user, remoteFile));
            }
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads/" + packName;

            poolClient.downloadFiles(remotePaths.toArray(new String[]{}), localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param remoteFolder
     * @throws BusinessException 
     */
    public void downloadFolder(User user, String remoteFolder) throws BusinessException {

        try {
            lfcBusiness.getModificationDate(user, remoteFolder);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            String remotePath = DataManagerUtil.parseBaseDir(user, remoteFolder);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + remotePath;
            poolClient.downloadFolder(remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param localFile
     * @param remoteFile
     * @throws BusinessException 
     */
    public void uploadFile(User user, String localFile, String remoteFile) 
            throws BusinessException {

        try {
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();
            String localPath = serverConfiguration.getDataManagerPath()
                    + "/uploads/" + localFile;
            String remotePath = DataManagerUtil.parseBaseDir(user, remoteFile);
            poolClient.uploadFile(localPath, remotePath, user.getEmail());

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param paths
     * @throws BusinessException 
     */
    public void delete(User user, String... paths) throws BusinessException {

        try {
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            for (String path : paths) {
                String remotePath = DataManagerUtil.parseBaseDir(user, path);
                poolClient.delete(remotePath, user.getEmail());
            }

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
}
