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
package fr.insalyon.creatis.vip.datamanager.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerServiceImpl extends RemoteServiceServlet implements DataManagerService {

    public void configureDataManager(String user, String proxyFileName) {

        try {
            DataManagerBusiness business = new DataManagerBusiness();
            business.configureDataManager(user, proxyFileName);

        } catch (BusinessException ex) {
        }
    }

    public List<Data> listDir(String user, String proxyFileName, String baseDir, 
            boolean refresh) {
        try {
            LFCBusiness business = new LFCBusiness();
            return business.listDir(user, proxyFileName, baseDir, refresh);
            
        } catch (BusinessException ex) {
            return null;
        }
    }

    public void delete(String user, String proxyFileName, String path) {
        
        try {
            LFCBusiness business = new LFCBusiness();
            business.delete(user, proxyFileName, path);
            
        } catch (BusinessException ex) {
        }
    }

    public void deleteFiles(String user, String proxyFileName, List<String> paths) {
        
        try {
            LFCBusiness business = new LFCBusiness();
            business.deleteFiles(user, proxyFileName, paths);
            
        } catch (BusinessException ex) {
        }
    }

    public void createDir(String user, String proxyFileName, String baseDir, String name) {
        
        try {
            LFCBusiness business = new LFCBusiness();
            business.createDir(user, proxyFileName, baseDir, name);
            
        } catch (BusinessException ex) {
        }
    }

    public void rename(String user, String proxyFileName, String oldPath, String newPath) {
        
        try {
            LFCBusiness business = new LFCBusiness();
            business.rename(user, proxyFileName, oldPath, newPath);
            
        } catch (BusinessException ex) {
        }
    }

    public void renameFiles(String user, String proxyFileName, Map<String, String> paths) {

        try {
            LFCBusiness business = new LFCBusiness();
            business.renameFiles(user, proxyFileName, paths);
            
        } catch (BusinessException ex) {
        }
    }
    
    public List<DMCachedFile> getCachedFiles(String proxy) {
        
        try {
            DataManagerBusiness business = new DataManagerBusiness();
            return business.getCachedFiles(proxy);
            
        } catch (BusinessException ex) {
            return null;
        }
    }
    
    public void deleteCachedFiles(List<String> cachedFiles, String proxy) {
        
        try {
            DataManagerBusiness business = new DataManagerBusiness();
            business.deleteCachedFiles(proxy, cachedFiles);
            
        } catch (BusinessException ex) {
        }
    }
}
