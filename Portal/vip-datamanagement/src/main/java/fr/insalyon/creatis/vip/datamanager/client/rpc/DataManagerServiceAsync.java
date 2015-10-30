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
package fr.insalyon.creatis.vip.datamanager.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMZombieFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.Image;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.bean.VisualizationItem;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
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

    public void getZombieFiles(AsyncCallback<List<DMZombieFile>> asyncCallback);

    public void deleteZombieFiles(List<String> surls, AsyncCallback<Void> asyncCallback);

    public void getImageSlicesURL(String imageLocalPath, String direction, AsyncCallback<Image> asyncCallback);

    public void getVisualizationItemFromLFN(String lfn, AsyncCallback<VisualizationItem> asyncCallback);

    public void getSSHConnections(AsyncCallback<List<SSH>> asyncCallback);

    public void addSSH(SSH ssh, AsyncCallback<Void> asyncCallback);

    public void updateSSH(SSH ssh, AsyncCallback<Void> asyncCallback);

    public void removeSSH(String email, String name, AsyncCallback<Void> asyncCallback);

    public void resetSSHs(List<List<String>> sshConnections, AsyncCallback<Void> asyncCallback);

    public void getSSHPublicKey(AsyncCallback<String> asycCallback);
}
