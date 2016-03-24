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
package fr.insalyon.creatis.vip.datamanager.server.rpc;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMZombieFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.Image;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.bean.TransferType;
import fr.insalyon.creatis.vip.datamanager.client.bean.VisualizationItem;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerServiceImpl extends AbstractRemoteServiceServlet implements DataManagerService {

    private static final Logger logger = Logger.getLogger(DataManagerServiceImpl.class);
    private final DataManagerBusiness dataManagerBusiness;
    private final LFCBusiness lfcBusiness;
    private final TransferPoolBusiness transferPoolBusiness;

    public DataManagerServiceImpl() {

        dataManagerBusiness = new DataManagerBusiness();
        lfcBusiness = new LFCBusiness();
        transferPoolBusiness = new TransferPoolBusiness();
    }

    @Override
    public List<Data> listDir(String baseDir, boolean refresh) throws DataManagerException {

        try {
            List<SSH> sshs = dataManagerBusiness.getSSHConnections();
            List<String> LfcDirSSHSynchronization = new ArrayList<String>();
            for (SSH ssh : sshs) {
                if (ssh.getTransferType().equals(TransferType.Synchronization)) {
                    LfcDirSSHSynchronization.add(ssh.getLfcDir());
                }
            }
            List<Data> data = lfcBusiness.listDir(getSessionUser(), baseDir, refresh);

            String lfcBaseDir = DataManagerUtil.parseBaseDir(getSessionUser(), baseDir);
            for (Data d : data) {
                String dataPath = lfcBaseDir + "/" + d.getName();
                for (String s : LfcDirSSHSynchronization) {
                    if (s.equals(dataPath)) {
                        d.setType(Data.Type.folderSync);
                    } else if (dataPath.contains(s+"/") && d.getType().equals(Data.Type.file)) {
                        d.setType(Data.Type.fileSync);
                    } else if (dataPath.contains(s+"/") && d.getType().equals(Data.Type.folder)) {
                        d.setType(Data.Type.folderSync);
                    }
                }
            }
            return data;

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void delete(String path) throws DataManagerException {

        try {
            trace(logger, "Deleting: " + path);
            User user = getSessionUser();
            transferPoolBusiness.delete(user, path);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void delete(List<String> paths) throws DataManagerException {

        try {
            trace(logger, "Deleting: " + paths);
            User user = getSessionUser();
            transferPoolBusiness.delete(user, paths.toArray(new String[]{}));

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void createDir(String baseDir, String name) throws DataManagerException {

        try {
            trace(logger, "Creating folder: " + baseDir + "/" + name);
            lfcBusiness.createDir(getSessionUser(), baseDir, name);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void rename(String oldPath, String newPath, boolean extendPath)
            throws DataManagerException {

        try {
            trace(logger, "Renaming '" + oldPath + "' to '" + newPath + "'");
            lfcBusiness.rename(getSessionUser(), oldPath, newPath, extendPath);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void rename(String baseDir, List<String> paths, String newBaseDir,
            boolean extendPath) throws DataManagerException {

        try {
            lfcBusiness.rename(getSessionUser(), baseDir, paths,
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

    /**
     *
     * @param cachedFiles
     * @throws DataManagerException
     */
    public void deleteCachedFiles(List<String> cachedFiles) throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing chached files: " + cachedFiles);
            dataManagerBusiness.deleteCachedFiles(cachedFiles);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public List<PoolOperation> getPoolOperationsByUser() throws DataManagerException {

        try {
            User user = getSessionUser();
            return transferPoolBusiness.getOperations(user.getEmail(), new Date(), user.getFolder());

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public List<PoolOperation> getPoolOperationsByUserAndDate(Date startDate) throws DataManagerException {

        try {
            User user = getSessionUser();
            return transferPoolBusiness.getOperations(user.getEmail(), startDate, user.getFolder());

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public List<PoolOperation> getPoolOperations() throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            return transferPoolBusiness.getOperations(getSessionUser().getFolder());

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public PoolOperation getPoolOperationById(String operationId) throws DataManagerException {

        try {
            return transferPoolBusiness.getOperationById(operationId, getSessionUser().getFolder());

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
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

    @Override
    public String downloadFile(String remoteFile) throws DataManagerException {

        try {
            trace(logger, "Adding file to transfer queue: " + remoteFile);
            User user = getSessionUser();
            return transferPoolBusiness.downloadFile(user, remoteFile);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
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

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public String downloadFolder(String remoteFolder) throws DataManagerException {

        try {
            trace(logger, "Adding folder to transfer queue: " + remoteFolder);
            User user = getSessionUser();
            return transferPoolBusiness.downloadFolder(user, remoteFolder);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    /**
     *
     * @param localFile
     * @param remoteName
     * @param remoteDir
     * @throws DataManagerException
     */
    @Override
    public void uploadFile(String localFile, String remoteName,
            String remoteDir) throws DataManagerException {

        File f = new File(Server.getInstance().getDataManagerPath()
                + "/uploads/" + localFile);
        f.renameTo(new File(Server.getInstance().getDataManagerPath()
                + "/uploads/" + remoteName));
        uploadFile(remoteName, remoteDir);
    }

    @Override
    public void uploadFile(String localFile, String remoteFile) throws DataManagerException {

        try {
            trace(logger, "Uploading file '" + localFile + "' to '" + remoteFile + "'.");
            User user = getSessionUser();
            transferPoolBusiness.uploadFile(user, localFile, remoteFile);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public boolean exists(String remoteFile) throws DataManagerException {
        try {
            trace(logger, "Test if file '" + remoteFile + " exists");
            User user = getSessionUser();
            return lfcBusiness.exists(user, remoteFile);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }

    }

    /**
     *
     * @return @throws DataManagerException
     */
    public List<DMZombieFile> getZombieFiles() throws DataManagerException {

        try {
            return dataManagerBusiness.getZombieFiles();
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    /**
     *
     * @param surls
     * @throws DataManagerException
     */
    public void deleteZombieFiles(List<String> surls) throws DataManagerException {

        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Removing zombie files: " + surls);
            dataManagerBusiness.deleteZombieFiles(surls);

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public Image getImageSlicesURL(String imageFileName, String direction) throws DataManagerException {

        try {
            trace(logger, "Slicing image: " + imageFileName);
            User user = getSessionUser();
            return dataManagerBusiness.getImageSlicesURL(imageFileName, direction);
        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }

    }

    @Override
    public List<SSH> getSSHConnections() throws DataManagerException {
        try {
            trace(logger, "Getting ssh connections");

            return dataManagerBusiness.getSSHConnections();

        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }

    }

    @Override
    public void addSSH(SSH ssh) throws DataManagerException {
        try {
            trace(logger, "Adding ssh connection " + ssh.getEmail() + " ; " + ssh.getHost());
            dataManagerBusiness.addSSH(ssh);
        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void updateSSH(SSH ssh) throws DataManagerException {
        try {
            trace(logger, "Updating ssh connection " + ssh.getEmail() + " ; " + ssh.getHost());
            dataManagerBusiness.updateSSH(ssh);
        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void removeSSH(String email, String name) throws DataManagerException {
        try {
            trace(logger, "Removing ssh connection " + email + " ; " + name);
            dataManagerBusiness.removeSSH(email, name);
        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public void resetSSHConnections(List<List<String>> sshConnections) throws DataManagerException {
        try {
            for (List<String> sshC : sshConnections) {
                trace(logger, "Removing ssh connection " + sshC.get(0) + " ; " + sshC.get(1));
            }
            dataManagerBusiness.resetSSHs(sshConnections);
        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }

    @Override
    public String getSSHPublicKey() {
        return Server.getInstance().getSshPublicKey();
    }

    @Override
    public VisualizationItem getVisualizationItemFromLFN(String lfn) throws DataManagerException {
        try {
            trace(logger, "Getting URL for file: " + lfn);
            User user = getSessionUser();
            return dataManagerBusiness.getVisualizationItemFromLFN(lfn, this.getServletContext().getRealPath("."), user);
        } catch (CoreException ex) {
            throw new DataManagerException(ex);
        } catch (BusinessException ex) {
            throw new DataManagerException(ex);
        }
    }
}
