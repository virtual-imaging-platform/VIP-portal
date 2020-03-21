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
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAZombieClient;
import fr.insalyon.creatis.grida.common.bean.CachedFile;
import fr.insalyon.creatis.grida.common.bean.ZombieFile;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMZombieFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import fr.insalyon.creatis.vip.datamanager.server.dao.DataManagerDAOFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerBusiness {

    private final static Logger logger = LoggerFactory.getLogger(DataManagerBusiness.class);

    public void deleteLocalFile(String fileName) throws BusinessException {

        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        } else {
            logger.error("File '" + fileName + "' does not exist.");
        }
    }

    public List<DMCachedFile> getCachedFiles() throws BusinessException {

        try {
            GRIDACacheClient client = CoreUtil.getGRIDACacheClient();

            List<CachedFile> cachedFilesList = client.getCachedFiles();
            List<DMCachedFile> dmCachedFiles = new ArrayList<DMCachedFile>();

            for (CachedFile cf : cachedFilesList) {
                dmCachedFiles.add(new DMCachedFile(cf.getPath(),
                        cf.getName(), FileUtils.parseFileSize((long) cf.getSize()),
                        cf.getFrequency(), cf.getLastUsage()));
            }

            return dmCachedFiles;

        } catch (GRIDAClientException ex) {
            logger.error(ex.toString());
            throw new BusinessException(ex);
        }
    }

    public void deleteCachedFiles(List<String> cachedFiles)
            throws BusinessException {

        try {
            GRIDACacheClient client = CoreUtil.getGRIDACacheClient();

            for (String path : cachedFiles) {
                client.deleteCachedFile(path);
            }
        } catch (GRIDAClientException ex) {
            logger.error(ex.toString());
            throw new BusinessException(ex);
        }
    }

    public String getRemoteFile(
        User user, String remoteFile, String localDir, Connection connection)
        throws BusinessException {

        try {
            return CoreUtil.getGRIDAClient().getRemoteFile(
                DataManagerUtil.parseBaseDir(user, remoteFile, connection),
                localDir);
        } catch (DataManagerException | GRIDAClientException ex) {
            logger.error(ex.toString());
            throw new BusinessException(ex);
        }
    }

    /**
     * Gets the list of zombie files.
     */
    public List<DMZombieFile> getZombieFiles() throws BusinessException {

        try {
            List<DMZombieFile> list = new ArrayList<DMZombieFile>();
            for (ZombieFile zf : CoreUtil.getGRIDAZombieClient().getList()) {
                list.add(new DMZombieFile(zf.getSurl(), zf.getRegistration()));
            }
            return list;

        } catch (GRIDAClientException ex) {
            logger.error(ex.toString());
            throw new BusinessException(ex);
        }
    }

    /**
     * Deletes a list of zombie files.
     *
     * @param surls List of zombie SURLs
     * @throws BusinessException
     */
    public void deleteZombieFiles(List<String> surls) throws BusinessException {

        try {
            GRIDAZombieClient client = CoreUtil.getGRIDAZombieClient();

            for (String surl : surls) {
                client.delete(surl);
            }
        } catch (GRIDAClientException ex) {
            logger.error(ex.toString());
            throw new BusinessException(ex);
        }
    }

    public List<SSH> getSSHConnections(Connection connection)
        throws BusinessException {
        try {
            return DataManagerDAOFactory.getInstance()
                .getSSHDAO(connection).getSSHConnections();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void addSSH(SSH ssh, Connection connection) throws BusinessException {
        try {
            //create LFC dir
            ConfigurationBusiness conf = new ConfigurationBusiness();
            User user = conf.getUser(ssh.getEmail(), connection);
            ssh.setLfcDir(
                DataManagerUtil.parseBaseDir(user, ssh.getLfcDir(), connection));
            DataManagerDAOFactory.getInstance().getSSHDAO(connection).addSSH(ssh);
        } catch (DAOException | DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeSSH(String email, String name, Connection connection)
        throws BusinessException {
        try {
            DataManagerDAOFactory.getInstance()
                .getSSHDAO(connection).removeSSH(email, name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void resetSSHs(
        List<List<String>> sshConnections, Connection connection)
        throws BusinessException {
        try {

            DataManagerDAOFactory.getInstance()
                .getSSHDAO(connection).resetSSHConnections(sshConnections);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateSSH(SSH ssh, Connection connection)
        throws BusinessException {
        try {
            DataManagerDAOFactory.getInstance().getSSHDAO(connection)
                    .updateSSH(ssh);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public static String extractName(String lfcDir) {
        return lfcDir.substring(lfcDir.lastIndexOf("/") + 1);
    }

    public static String generateLFCDir(
        String name, String email, Connection connection)
        throws DataManagerException, BusinessException {

        ConfigurationBusiness conf = new ConfigurationBusiness();
        User user = conf.getUser(email, connection);
        String homeDir = Server.getInstance()
                .getDataManagerUsersHome() + "/" + user.getFolder();

        return (homeDir + "/" + name);
    }
}
