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
package fr.insalyon.creatis.vip.datamanager.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import java.util.List;
import java.util.Map;

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

    public void rename(Map<String, String> paths, boolean extendPath, AsyncCallback<Void> asyncCallback);

    public void getCachedFiles(AsyncCallback<List<DMCachedFile>> asyncCallback);

    public void deleteCachedFiles(List<String> cachedFiles, AsyncCallback<Void> asyncCallback);

    public void getPoolOperationsByUser(AsyncCallback<List<PoolOperation>> asyncCallback);

    public void getPoolOperations(AsyncCallback<List<PoolOperation>> asyncCallback);

    public void removeOperations(List<String> ids, AsyncCallback<Void> asyncCallback);

    public void removeOperationById(String id, AsyncCallback<Void> asyncCallback);

    public void downloadFile(String remoteFile, AsyncCallback<Void> asyncCallback);

    public void downloadFolder(String remoteFolder, AsyncCallback<Void> asyncCallback);
    
    public void uploadFile(String localFile, String remoteFile, AsyncCallback<Void> asyncCallback);
}
