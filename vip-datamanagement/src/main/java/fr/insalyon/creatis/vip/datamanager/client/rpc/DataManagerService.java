package fr.insalyon.creatis.vip.datamanager.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.models.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.models.Data;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;

import java.util.Date;
import java.util.List;

public interface DataManagerService extends RemoteService {

    public static final String SERVICE_URI = "/datamanagerservice";

    public static class Util {

        public static DataManagerServiceAsync getInstance() {

            DataManagerServiceAsync instance = (DataManagerServiceAsync) GWT.create(DataManagerService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public List<Data> listDir(String baseDir, boolean refresh) throws DataManagerException;

    public void delete(String path) throws DataManagerException;

    public void delete(List<String> paths) throws DataManagerException;

    public void createDir(String baseDir, String name) throws DataManagerException;

    public void rename(String oldPath, String newPath, boolean extendPath) throws DataManagerException;

    public void rename(String baseDir, List<String> paths, String newBaseDir, boolean extendPath) throws DataManagerException;

    public List<DMCachedFile> getCachedFiles() throws DataManagerException;

    public void deleteCachedFiles(List<String> cachedFiles) throws DataManagerException;

    public List<PoolOperation> getPoolOperationsByUser() throws DataManagerException;

    public List<PoolOperation> getPoolOperationsByUserAndDate(Date startDate) throws DataManagerException;

    public List<PoolOperation> getPoolOperations() throws DataManagerException;

    public PoolOperation getPoolOperationById(String operationId) throws DataManagerException;

    public void removeOperations(List<String> ids) throws DataManagerException;

    public void removeUserOperations() throws DataManagerException;

    public void removeOperationById(String id) throws DataManagerException;

    public String downloadFile(String remoteFile) throws DataManagerException;

    public String downloadFiles(List<String> remoteFiles, String packName) throws DataManagerException;

    public String downloadFolder(String remoteFolder) throws DataManagerException;

    public void uploadFile(String localFile, String remoteName, String remoteDir) throws DataManagerException;

    public void uploadFile(String localFile, String remoteFile) throws DataManagerException;

    public boolean exists(String remoteFile) throws DataManagerException;
}
