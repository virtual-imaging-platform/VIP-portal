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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
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

    public void rename(Map<String, String> paths, boolean extendPath) throws DataManagerException;

    public List<DMCachedFile> getCachedFiles() throws DataManagerException;

    public void deleteCachedFiles(List<String> cachedFiles) throws DataManagerException;

    public List<PoolOperation> getPoolOperationsByUser() throws DataManagerException;

    public List<PoolOperation> getPoolOperations() throws DataManagerException;

    public void removeOperations(List<String> ids) throws DataManagerException;

    public void removeOperationById(String id) throws DataManagerException;

    public void downloadFile(String remoteFile) throws DataManagerException;

    public void downloadFolder(String remoteFolder) throws DataManagerException;
    
    public void uploadFile(String localFile, String remoteFile) throws DataManagerException;
}
