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
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
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
     * @return
     * @throws BusinessException 
     */
    public List<PoolOperation> getOperations(String email, Date date) throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();

            List<Operation> operationsList = client.getOperationsLimitedListByUserAndDate(email, operationsLimit, date);
            List<PoolOperation> poolOperations = new ArrayList<PoolOperation>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm");

            for (Operation op : operationsList) {
                if (op.getType() != Operation.Type.Delete) {
                    String source = "";
                    String dest = "";
                    PoolOperation.Type type = null;
                    PoolOperation.Status status = PoolOperation.Status.valueOf(op.getStatus().name());

                    if (op.getType() == Operation.Type.Upload) {
                        type = PoolOperation.Type.Upload;
                        source = FilenameUtils.getName(op.getSource());
                        dest = DataManagerUtil.parseRealDir(op.getDest());

                    } else {
                        type = PoolOperation.Type.Download;
                        dest = "Platform";
                        source = op.getType() == Operation.Type.Download
                                ? DataManagerUtil.parseRealDir(op.getSource())
                                : FilenameUtils.getBaseName(op.getDest());
                    }

                    poolOperations.add(new PoolOperation(op.getId(),
                            op.getRegistration(), dateFormat.format(op.getRegistration()),
                            source, dest, type, status, op.getUser()));
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
     * @return
     * @throws BusinessException 
     */
    public List<PoolOperation> getOperations() throws BusinessException {

        try {
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();

            List<Operation> operationsList = client.getAllOperations();
            List<PoolOperation> poolOperations = new ArrayList<PoolOperation>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm");

            for (Operation op : operationsList) {
                String source = "";
                String dest = "";
                PoolOperation.Type type = null;
                PoolOperation.Status status = PoolOperation.Status.valueOf(op.getStatus().name());

                if (op.getType() == Operation.Type.Upload) {
                    type = PoolOperation.Type.Upload;
                    source = FilenameUtils.getName(op.getSource());
                    dest = DataManagerUtil.parseRealDir(op.getDest());

                } else if (op.getType() == Operation.Type.Delete) {
                    type = PoolOperation.Type.Delete;
                    source = DataManagerUtil.parseRealDir(op.getSource());

                } else {
                    type = PoolOperation.Type.Download;
                    dest = "Platform";
                    source = op.getType() == Operation.Type.Download
                            ? DataManagerUtil.parseRealDir(op.getSource())
                            : FilenameUtils.getBaseName(op.getDest());
                }

                poolOperations.add(new PoolOperation(op.getId(),
                        op.getRegistration(), dateFormat.format(op.getRegistration()),
                        source, dest, type, status, op.getUser()));
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
     * @param userName
     * @param email
     * @param remoteFile
     * @throws BusinessException 
     */
    public void downloadFile(String userName, String email, String remoteFile) throws BusinessException {

        try {
            lfcBusiness.getModificationDate(userName, remoteFile);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            String remotePath = DataManagerUtil.parseBaseDir(userName, remoteFile);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + FilenameUtils.getFullPath(remotePath);

            poolClient.downloadFile(remotePath, localDirPath, email);

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
     * @param userName
     * @param email
     * @param remoteFiles
     * @param packName
     * @throws BusinessException 
     */
    public void downloadFiles(String userName, String email, List<String> remoteFiles,
            String packName) throws BusinessException {

        try {
            lfcBusiness.getModificationDate(userName, remoteFiles);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            List<String> remotePaths = new ArrayList<String>();
            for (String remoteFile : remoteFiles) {
                remotePaths.add(DataManagerUtil.parseBaseDir(userName, remoteFile));
            }
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads/" + packName;

            poolClient.downloadFiles(remotePaths.toArray(new String[]{}), localDirPath, email);

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
     * @param userName
     * @param email
     * @param remoteFolder
     * @throws BusinessException 
     */
    public void downloadFolder(String userName, String email,
            String remoteFolder) throws BusinessException {

        try {
            lfcBusiness.getModificationDate(userName, remoteFolder);
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            String remotePath = DataManagerUtil.parseBaseDir(userName, remoteFolder);
            String localDirPath = serverConfiguration.getDataManagerPath()
                    + "/downloads" + remotePath;
            poolClient.downloadFolder(remotePath, localDirPath, email);

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
     * @param userName
     * @param email
     * @param localFile
     * @param remoteFile
     * @throws BusinessException 
     */
    public void uploadFile(String userName, String email, String localFile,
            String remoteFile) throws BusinessException {

        try {
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();
            String localPath = serverConfiguration.getDataManagerPath()
                    + "/uploads/" + localFile;
            String remotePath = DataManagerUtil.parseBaseDir(userName, remoteFile);
            poolClient.uploadFile(localPath, remotePath, email);

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
     * @param userName
     * @param email
     * @param paths
     * @throws BusinessException 
     */
    public void delete(String userName, String email, String... paths) throws BusinessException {

        try {
            GRIDAPoolClient poolClient = CoreUtil.getGRIDAPoolClient();

            for (String path : paths) {
                String remotePath = DataManagerUtil.parseBaseDir(userName, path);
                poolClient.delete(remotePath, email);
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
