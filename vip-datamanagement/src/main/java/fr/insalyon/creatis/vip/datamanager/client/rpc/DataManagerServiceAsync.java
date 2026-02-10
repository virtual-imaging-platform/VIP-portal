package fr.insalyon.creatis.vip.datamanager.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.datamanager.models.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.models.Data;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;

import java.util.Date;
import java.util.List;

public interface DataManagerServiceAsync {

    public void listDir(String baseDir, boolean refresh, AsyncCallback<List<Data>> asyncCallback);

    public void delete(String path, AsyncCallback<Void> asyncCallback);

    public void delete(List<String> paths, AsyncCallback<Void> asyncCallback);

    public void createDir(String baseDir, String name, AsyncCallback<Void> asyncCallback);

    public void rename(String oldPath, String newPath, boolean extendPath, AsyncCallback<Void> asyncCallback);

    public void rename(String baseDir, List<String> paths, String newBaseDir, boolean extendPath, AsyncCallback<Void> asyncCallback);

    public void getCachedFiles(AsyncCallback<List<DMCachedFile>> asyncCallback);

    public void deleteCachedFiles(List<String> cachedFiles, AsyncCallback<Void> asyncCallback);

    public void getPoolOperationsByUser(AsyncCallback<List<PoolOperation>> asyncCallback);

    public void getPoolOperationsByUserAndDate(Date startDate, AsyncCallback<List<PoolOperation>> asyncCallback);

    public void getPoolOperations(AsyncCallback<List<PoolOperation>> asyncCallback);

    public void getPoolOperationById(String operationId, AsyncCallback<PoolOperation> asyncCallback);

    public void removeOperations(List<String> ids, AsyncCallback<Void> asyncCallback);

    public void removeUserOperations(AsyncCallback<Void> asyncCallback);

    public void removeOperationById(String id, AsyncCallback<Void> asyncCallback);

    public void downloadFile(String remoteFile, AsyncCallback<String> asyncCallback);

    public void downloadFiles(List<String> remoteFiles, String packName, AsyncCallback<String> asyncCallback);

    public void downloadFolder(String remoteFolder, AsyncCallback<String> asyncCallback);

    public void uploadFile(String localFile, String remoteFile, AsyncCallback<Void> asyncCallback);

    public void uploadFile(String localFile, String remoteName, String remoteDir, AsyncCallback<Void> asyncCallback);

    public void exists(String remoteFile, AsyncCallback<Boolean> asyncCallback);
}
