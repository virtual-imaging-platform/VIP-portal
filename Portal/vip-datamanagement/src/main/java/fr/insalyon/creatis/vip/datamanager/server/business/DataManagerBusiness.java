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
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAZombieClient;
import fr.insalyon.creatis.grida.common.bean.CachedFile;
import fr.insalyon.creatis.grida.common.bean.ZombieFile;
import java.util.ArrayList;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMZombieFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Image;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.bean.VisualizationItem;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.dao.SSHDAOFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerBusiness {

    private final static Logger logger = Logger.getLogger(DataManagerBusiness.class);

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
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void deleteCachedFiles(List<String> cachedFiles) throws BusinessException {

        try {
            GRIDACacheClient client = CoreUtil.getGRIDACacheClient();

            for (String path : cachedFiles) {
                client.deleteCachedFile(path);
            }
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param remoteFile
     * @param localDir
     * @return
     * @throws BusinessException
     */
    public String getRemoteFile(User user, String remoteFile, String localDir)
            throws BusinessException {

        try {
            return CoreUtil.getGRIDAClient().getRemoteFile(
                    DataManagerUtil.parseBaseDir(user, remoteFile), localDir);

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * Gets the list of zombie files.
     *
     * @return
     * @throws BusinessException
     */
    public List<DMZombieFile> getZombieFiles() throws BusinessException {

        try {
            List<DMZombieFile> list = new ArrayList<DMZombieFile>();
            for (ZombieFile zf : CoreUtil.getGRIDAZombieClient().getList()) {
                list.add(new DMZombieFile(zf.getSurl(), zf.getRegistration()));
            }
            return list;

        } catch (GRIDAClientException ex) {
            logger.error(ex);
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
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

     
    public Image getImageSlicesURL(String imageFileName, String dir) throws BusinessException {

        File imageFile = new File(imageFileName);
        String imageDirName = imageFile.getParent() + "/" + imageFile.getName() + "-" + dir+ "-slices";
        File imageDir = new File(imageDirName);

        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        String sliceZeroFileName = imageDirName + "/slice0.png";
        File sliceZero = new File(sliceZeroFileName);
        if (!sliceZero.exists()) {
            //split slices
            ProcessBuilder builder = new ProcessBuilder("slice.sh", imageFileName, imageDirName,dir);
            builder.redirectErrorStream(true);
            try {

                builder.start();
                try {
                    //wait for the first slice to be produced but not for all slices ;)
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException ex) {
                    new File(imageDirName).delete();
                    throw new BusinessException(ex);
                }
            } catch (IOException ex) {
                new File(imageDirName).delete();
                throw new BusinessException(ex);
            }
        }
        //get z value
        ProcessBuilder builderZ = new ProcessBuilder("getz.sh", imageFileName, dir);
        builderZ.redirectErrorStream(true);
        String number = "";
        try {
            try {
                Process process = builderZ.start();
                process.waitFor();

                InputStream stdout = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));

                String line;

                while ((line = reader.readLine()) != null) {
                    System.out.println("Stdout: " + line);
                    number += line;
                }
            } catch (InterruptedException ex) {
                new File(imageDirName).delete();
                throw new BusinessException(ex);
            }

        } catch (IOException ex) {
            new File(imageDirName).delete();
            throw new BusinessException(ex);
        }

        logger.info("IMAGE DIR NAME IS " + imageDirName);
        return new Image(imageDirName, Integer.parseInt(number.trim()), imageDirName.substring(imageDirName.indexOf("/files/viewer")) + "/");

    }
    
    
    
    
    public List<SSH> getSSHConnections() throws BusinessException {
        try {
            return SSHDAOFactory.getDAOFactory().getSSHDAO().getSSHConnections();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void addSSH(SSH ssh) throws BusinessException {
        try {
            //create LFC dir
            ConfigurationBusiness conf = new ConfigurationBusiness();
            User user = conf.getUser(ssh.getEmail());

            String lfcDir = generateLFCDir(ssh.getName(), ssh.getEmail());
            String name = lfcDir.substring(lfcDir.lastIndexOf("/") + 1);
            String dir = Server.getInstance().getDataManagerUsersHome() + "/" + user.getFolder();
            logger.info("Creating directory " + name + " in folder " + dir);

            CoreUtil.getGRIDAClient().createFolder(dir, name);

//            CoreUtil.getGRIDAClient().createFolder(dir, name);
            SSHDAOFactory.getDAOFactory().getSSHDAO().addSSH(ssh);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            throw new BusinessException(ex);
        }

    }

    public void removeSSH(String email, String name) throws BusinessException {
        try {
            SSHDAOFactory.getDAOFactory().getSSHDAO().removeSSH(email, name);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateSSH(SSH ssh) throws BusinessException {
        try {
            SSHDAOFactory.getDAOFactory().getSSHDAO().updateSSH(ssh);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public static String extractName(String lfcDir) {
        String bruteName = lfcDir.substring(lfcDir.lastIndexOf("/") + 1);
        String shortName = bruteName.replace(DataManagerConstants.SSH_APPEND, "");
        return shortName;
    }

    public static String generateLFCDir(String name, String email) throws DataManagerException, BusinessException {

        ConfigurationBusiness conf = new ConfigurationBusiness();
        User user = conf.getUser(email);
        String homeDir = Server.getInstance().getDataManagerUsersHome() + "/" + user.getFolder();

        return (homeDir + "/" + name + DataManagerConstants.SSH_APPEND);
    }

    public VisualizationItem getVisualizationItemFromLFN(String lfn, String localDir, User user) throws BusinessException {

        String relativeDirString;
        try {
            relativeDirString = "files/viewer" + System.getProperty("file.separator") + (new File(DataManagerUtil.parseBaseDir(user, lfn))).getParent().replaceAll(" ", "_").replaceAll("\\([^\\(]*\\)", "");
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
        String fileDirString = localDir + System.getProperty("file.separator") + relativeDirString;
        File fileDir = new File(fileDirString);
        String fileName = fileDir.getAbsolutePath() + System.getProperty("file.separator") + lfn.substring(lfn.lastIndexOf('/') + 1);

        if (!fileDir.exists()) {
            fileDir.mkdirs();
            if (!fileDir.exists()) {
                throw new BusinessException("Cannot create viewer dir: " + fileDir.getAbsolutePath());
            }
        }
        fileDir.setWritable(true, false);
        if (!(new File(fileName)).exists()) {
            try {
                CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(user, lfn), fileDir.getAbsolutePath());
            } catch (GRIDAClientException ex) {
                fileDir.delete();
                throw new BusinessException(ex);
            } catch (DataManagerException ex) {
                throw new BusinessException(ex);
            }

        }
        String url = relativeDirString + System.getProperty("file.separator") + lfn.substring(lfn.lastIndexOf('/') + 1);
        return new VisualizationItem(url, fileName);
    }

}
