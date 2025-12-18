package fr.insalyon.creatis.vip.datamanager.server.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation.Type;

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
            throws VipException {

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
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting operations for {} since {}", email, date, ex);
            throw new VipException(ex);
        }
    }

    public List<PoolOperation> getOperations(String currentUserFolder)
            throws VipException {

        try {
            List<PoolOperation> poolOperations = new ArrayList<>();

            for (Operation operation : gridaPoolClient.getAllOperations()) {
                poolOperations.add(
                    processOperation(operation, currentUserFolder));
            }

            return poolOperations;
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting all operations", ex);
            throw new VipException(ex);
        }
    }

    public PoolOperation getOperationById(
            String operationId, String currentUserFolder)
            throws VipException {

        try {
            return processOperation(
                    gridaPoolClient.getOperationById(operationId),
                    currentUserFolder);
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting operation {}", operationId, ex);
            throw new VipException(ex);
        }
    }

    public PoolOperation getDownloadPoolOperation(String operationId)
            throws VipException {
        try {
            Operation operation = gridaPoolClient.getOperationById(operationId);
            if (operation.getType() != Operation.Type.Download) {
                logger.error("Not a download operation {}", operationId);
                throw new VipException("Wrong operation type for download");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm");
            PoolOperation.Status status = PoolOperation.Status.valueOf(operation.getStatus().name());
            return new PoolOperation(operation.getId(), operation.getRegistration(),
                    dateFormat.format(operation.getRegistration()), operation.getSource(), operation.getDest(),
                    Type.Download, status, operation.getUser(), operation.getProgress());
        } catch (GRIDAClientException ex) {
            logger.error("Error getting download operation {}", operationId, ex);
            throw new VipException(ex);
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

    public void removeOperations(List<String> ids) throws VipException {

        try {

            for (String id : ids) {
                gridaPoolClient.removeOperationById(id);
            }

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations {}", ids, ex);
            throw new VipException(ex);
        }
    }

    public void removeUserOperations(String email) throws VipException {

        try {
            gridaPoolClient.removeOperationsByUser(email);

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations for {}", email, ex);
            throw new VipException(ex);
        }
    }

    public void removeOperationById(String id) throws VipException {

        try {
            gridaPoolClient.removeOperationById(id);

        } catch (GRIDAClientException ex) {
            logger.error("Error removing operations {}", id, ex);
            throw new VipException(ex);
        }
    }

    public String downloadFile(User user, String remoteFile) throws VipException {

        try {
            lfcBusiness.getModificationDate(user, remoteFile);

            String remotePath = lfcPathsBusiness.parseBaseDir(user, remoteFile);
            String localDirPath = lfcPathsBusiness.getLocalDirForGridaFileDownload(remotePath);

            return gridaPoolClient.downloadFile(remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading file {} for {}", remoteFile, user, ex);
            throw new VipException(ex);
        }
    }

    public String downloadFiles(
            User user, List<String> remoteFiles, String packName)
            throws VipException {

        try {
            lfcBusiness.getModificationDate(user, remoteFiles);

            List<String> remotePaths = new ArrayList<>();
            for (String remoteFile : remoteFiles) {
                remotePaths.add(
                        lfcPathsBusiness.parseBaseDir(user, remoteFile));
            }
            String localDirPath =
                    lfcPathsBusiness.getLocalDirForGridaMultiFilesDownload(packName);

            return gridaPoolClient.downloadFiles(remotePaths.toArray(new String[]{}),
                    localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading files {} for {}", remoteFiles, user, ex);
            throw new VipException(ex);
        }
    }

    public String downloadFolder(User user, String remoteFolder)
            throws VipException {

        try {
            lfcBusiness.getModificationDate(user, remoteFolder);

            String remotePath = lfcPathsBusiness.parseBaseDir(user, remoteFolder);
            String localDirPath = lfcPathsBusiness.getLocalDirForGridaFolderDownload(remotePath);
            return gridaPoolClient.downloadFolder(
                    remotePath, localDirPath, user.getEmail());

        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error downloading folder {} for {}", remoteFolder, user, ex);
            throw new VipException(ex);
        }
    }

    public String uploadFile(User user, String localFilePath, String remoteFile)
            throws VipException {

        try {
            String remotePath = lfcPathsBusiness.parseBaseDir(user, remoteFile);
            return gridaPoolClient.uploadFile(localFilePath, remotePath, user.getEmail());
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error uploading file {} to {} for {}",
                    localFilePath, remoteFile, user, ex);
            throw new VipException(ex);
        }
    }

    public void delete(User user, String... paths)
        throws VipException {

        try {

            for (String path : paths) {
                String remotePath = lfcPathsBusiness.parseBaseDir(user, path);
                gridaPoolClient.delete(remotePath, user.getEmail());
            }
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error deleting files {} for {}",
                    Arrays.toString(paths), user, ex);
            throw new VipException(ex);
        }
    }
}
