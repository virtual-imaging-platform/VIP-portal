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

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerServiceImpl extends AbstractRemoteServiceServlet implements DataManagerService {

    private static final Logger logger = Logger.getLogger(DataManagerServiceImpl.class);
    private DataManagerBusiness dataManagerBusiness;
    private LFCBusiness lfcBusiness;
    private TransferPoolBusiness transferPoolBusiness;

    public DataManagerServiceImpl() {

        dataManagerBusiness = new DataManagerBusiness();
        lfcBusiness = new LFCBusiness();
        transferPoolBusiness = new TransferPoolBusiness();
    }

    public List<Data> listDir(String baseDir, boolean refresh) throws DataManagerException {

        try {
            return lfcBusiness.listDir(getSessionUser().getFullName(), baseDir, refresh);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void delete(String path) throws DataManagerException {

        try {
            trace(logger, "Deleting: " + path);
            User user = getSessionUser();
            transferPoolBusiness.delete(user.getFullName(), user.getEmail(), path);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void delete(List<String> paths) throws DataManagerException {

        try {
            trace(logger, "Deleting: " + paths);
            User user = getSessionUser();
            transferPoolBusiness.delete(user.getFullName(), user.getEmail(), 
                    paths.toArray(new String[]{}));

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void createDir(String baseDir, String name) throws DataManagerException {

        try {
            trace(logger, "Creating folder: " + baseDir + "/" + name);
            lfcBusiness.createDir(getSessionUser().getFullName(), baseDir, name);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void rename(String oldPath, String newPath, boolean extendPath)
            throws DataManagerException {

        try {
            trace(logger, "Renaming '" + oldPath + "' to '" + newPath + "'");
            lfcBusiness.rename(getSessionUser().getFullName(), oldPath, newPath, extendPath);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void rename(String baseDir, List<String> paths, String newBaseDir,
            boolean extendPath) throws DataManagerException {

        try {
            lfcBusiness.rename(getSessionUser().getFullName(), baseDir, paths,
                    newBaseDir, extendPath);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public List<DMCachedFile> getCachedFiles() throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            return dataManagerBusiness.getCachedFiles();

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void deleteCachedFiles(List<String> cachedFiles) throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing files: " + cachedFiles);
            dataManagerBusiness.deleteCachedFiles(cachedFiles);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public List<PoolOperation> getPoolOperationsByUser() throws DataManagerException {

        try {
            return transferPoolBusiness.getOperations(getSessionUser().getEmail(), new Date());

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }
    
    public List<PoolOperation> getPoolOperationsByUserAndDate(Date startDate) throws DataManagerException {

        try {
            return transferPoolBusiness.getOperations(getSessionUser().getEmail(), startDate);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public List<PoolOperation> getPoolOperations() throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            return transferPoolBusiness.getOperations();

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }
    
    public PoolOperation getPoolOperationById(String operationId) throws DataManagerException {
        
        try {
            return transferPoolBusiness.getOperationById(operationId);
            
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void removeOperations(List<String> ids) throws DataManagerException {

        try {
            trace(logger, "Removing operations: " + ids);
            transferPoolBusiness.removeOperations(ids);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void removeUserOperations() throws DataManagerException {

        try {
            trace(logger, "Removing all operations.");
            transferPoolBusiness.removeUserOperations(getSessionUser().getEmail());

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void removeOperationById(String id) throws DataManagerException {

        try {
            trace(logger, "Removing operation: " + id);
            transferPoolBusiness.removeOperationById(id);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void downloadFile(String remoteFile) throws DataManagerException {

        try {
            trace(logger, "Adding file to transfer queue: " + remoteFile);
            User user = getSessionUser();
            transferPoolBusiness.downloadFile(user.getFullName(),
                    user.getEmail(), remoteFile);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void downloadFiles(List<String> remoteFiles, String packName)
            throws DataManagerException {

        try {
            trace(logger, "Adding files to transfer queue: " + remoteFiles);
            User user = getSessionUser();
            transferPoolBusiness.downloadFiles(user.getFullName(),
                    user.getEmail(), remoteFiles, packName);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void downloadFolder(String remoteFolder) throws DataManagerException {

        try {
            trace(logger, "Adding folder to transfer queue: " + remoteFolder);
            User user = getSessionUser();
            transferPoolBusiness.downloadFolder(user.getFullName(),
                    user.getEmail(), remoteFolder);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public void uploadFile(String localFile, String remoteName, String remoteDir) throws DataManagerException {
        File f = new File(Server.getInstance().getDataManagerPath()
                + "/uploads/" + localFile);
        f.renameTo(new File(Server.getInstance().getDataManagerPath()
                + "/uploads/" + remoteName));
        uploadFile(remoteName, remoteDir);
    }

    public void uploadFile(String localFile, String remoteFile) throws DataManagerException {

        try {
            trace(logger, "Uploading file '" + localFile + "' to '" + remoteFile + "'.");
            User user = getSessionUser();
            transferPoolBusiness.uploadFile(user.getFullName(),
                    user.getEmail(), localFile, remoteFile);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    public boolean exists(String remoteFile) throws DataManagerException {
        try {
            trace(logger, "Test if file '" + remoteFile + " exists");
            User user = getSessionUser();
            return lfcBusiness.exists(user.getFullName(), remoteFile);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }

    }
}
