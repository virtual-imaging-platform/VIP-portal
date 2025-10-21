package fr.insalyon.creatis.vip.datamanager.server.rpc;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.models.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.models.Data;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import jakarta.servlet.ServletException;

public class DataManagerServiceImpl extends AbstractRemoteServiceServlet implements DataManagerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private DataManagerBusiness dataManagerBusiness;
    private LFCBusiness lfcBusiness;
    private TransferPoolBusiness transferPoolBusiness;
    private Server server;

    @Override
    public void init() throws ServletException {
        super.init();
        transferPoolBusiness = getBean(TransferPoolBusiness.class);
        lfcBusiness = getBean(LFCBusiness.class);
        dataManagerBusiness = getBean(DataManagerBusiness.class);
        server = getBean(Server.class);
    }

    @Override
    public List<Data> listDir(String baseDir, boolean refresh) throws DataManagerException {
        try {
            List<Data> data = lfcBusiness.listDir(getSessionUser(), baseDir, refresh);

            return data;
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void delete(String path) throws DataManagerException {
        try {
            trace(logger, "Deleting: " + path);
            User user = getSessionUser();
            transferPoolBusiness.delete(user, path);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void delete(List<String> paths) throws DataManagerException {
        try {
            trace(logger, "Deleting: " + paths);
            User user = getSessionUser();
            transferPoolBusiness.delete(user, paths.toArray(new String[]{}));
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void createDir(String baseDir, String name) throws DataManagerException {
        try {
            trace(logger, "Creating folder: " + baseDir + "/" + name);
            lfcBusiness.createDir(getSessionUser(), baseDir, name);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void rename(String oldPath, String newPath, boolean extendPath)
            throws DataManagerException {
        try {
            trace(logger, "Renaming '" + oldPath + "' to '" + newPath + "'");
            lfcBusiness.rename(getSessionUser(), oldPath, newPath, extendPath);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void rename(String baseDir, List<String> paths, String newBaseDir,
            boolean extendPath) throws DataManagerException {
        try {
            lfcBusiness.rename(
                getSessionUser(), baseDir, paths, newBaseDir, extendPath);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public List<DMCachedFile> getCachedFiles() throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            return dataManagerBusiness.getCachedFiles();

        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void deleteCachedFiles(List<String> cachedFiles) throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing chached files: " + cachedFiles);
            dataManagerBusiness.deleteCachedFiles(cachedFiles);

        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public List<PoolOperation> getPoolOperationsByUser() throws DataManagerException {
        try {
            User user = getSessionUser();
            return transferPoolBusiness.getOperations(
                    user.getEmail(), new Date(), user.getFolder());
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public List<PoolOperation> getPoolOperationsByUserAndDate(Date startDate)
            throws DataManagerException {
        try {
            User user = getSessionUser();
            return transferPoolBusiness.getOperations(
                user.getEmail(), startDate, user.getFolder());
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public List<PoolOperation> getPoolOperations() throws DataManagerException {
        try {
            authenticateSystemAdministrator(logger);
            return transferPoolBusiness.getOperations(getSessionUser().getFolder());
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public PoolOperation getPoolOperationById(String operationId)
            throws DataManagerException {
        try {
            return transferPoolBusiness.getOperationById(
                operationId, getSessionUser().getFolder());
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void removeOperations(List<String> ids) throws DataManagerException {

        try {
            trace(logger, "Removing operations: " + ids);
            transferPoolBusiness.removeOperations(ids);

        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void removeUserOperations() throws DataManagerException {

        try {
            trace(logger, "Removing all operations.");
            transferPoolBusiness.removeUserOperations(getSessionUser().getEmail());

        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void removeOperationById(String id) throws DataManagerException {

        try {
            trace(logger, "Removing operation: " + id);
            transferPoolBusiness.removeOperationById(id);

        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public String downloadFile(String remoteFile) throws DataManagerException {
        try {
            trace(logger, "Adding file to transfer queue: " + remoteFile);
            User user = getSessionUser();
            return transferPoolBusiness.downloadFile(user, remoteFile);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public String downloadFiles(List<String> remoteFiles, String packName)
            throws DataManagerException {
        try {
            trace(logger, "Adding files to transfer queue: " + remoteFiles);
            User user = getSessionUser();
            return transferPoolBusiness.downloadFiles(user, remoteFiles, packName);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public String downloadFolder(String remoteFolder) throws DataManagerException {
        try {
            trace(logger, "Adding folder to transfer queue: " + remoteFolder);
            User user = getSessionUser();
            return transferPoolBusiness.downloadFolder(user, remoteFolder);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void uploadFile(String localFile, String remoteName, String remoteDir)
            throws DataManagerException {

        File f = new File(
                server.getDataManagerPath() + "/uploads/" + localFile);
        f.renameTo(new File(
                server.getDataManagerPath() + "/uploads/" + remoteName));
        uploadFile(f.getAbsolutePath(), remoteDir);
    }

    @Override
    public void uploadFile(String localFilePath, String remoteFile)
            throws DataManagerException {
        try {
            trace(logger, "Uploading file '" + localFilePath + "' to '" + remoteFile + "'.");
            User user = getSessionUser();
            transferPoolBusiness.uploadFile(user, localFilePath, remoteFile);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public boolean exists(String remoteFile) throws DataManagerException {
        try {
            trace(logger, "Test if file '" + remoteFile + " exists");
            User user = getSessionUser();
            return lfcBusiness.exists(user, remoteFile);
        } catch (VipException ex) {
            throw new DataManagerException(ex);
        }
    }
}
