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
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMZombieFile;
import fr.insalyon.creatis.vip.datamanager.client.bean.Image;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
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

    public Image getImageSlicesURL(String imageLFN, String localDir, User user) throws BusinessException {
        String relativeDirString = "/images/viewer" + System.getProperty("file.separator") + (new File(imageLFN)).getParent().replaceAll(" ", "_").replaceAll("\\([^\\(]*\\)", "");
        String imageDirString = localDir +relativeDirString;
        File imageDir = new File(imageDirString);
        String imageFileName = imageDir.getAbsolutePath() + System.getProperty("file.separator") + imageLFN.substring(imageLFN.lastIndexOf('/') + 1);

        if (!imageDir.exists()) { //if it exists, assume that the file is being downloaded and don't do it again
            imageDir.mkdirs();
            if (!imageDir.exists()) {
                throw new BusinessException("Cannot create viewer dir: " + imageDir.getAbsolutePath());
            }
            try {
                CoreUtil.getGRIDAClient().getRemoteFile(DataManagerUtil.parseBaseDir(user, imageLFN), imageDir.getAbsolutePath());
            } catch (GRIDAClientException ex) {
                imageDir.delete();
                throw new BusinessException(ex);
            } catch (DataManagerException ex) {
                throw new BusinessException(ex);
            }

            //split slices
            ProcessBuilder builder = new ProcessBuilder("slice.sh", imageFileName, imageDir.getAbsolutePath());
            builder.redirectErrorStream(true);
            try {

                builder.start();
                try {
                    //wait for the first slice to be produced but not for all slices ;)
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException ex) {
                    imageDir.delete();
                    throw new BusinessException(ex);
                }

            } catch (IOException ex) {
                imageDir.delete();
                throw new BusinessException(ex);
            }
        }
        //get z value
        ProcessBuilder builderZ = new ProcessBuilder("getz.sh", imageFileName);
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
                imageDir.delete();
                throw new BusinessException(ex);
            }


        } catch (IOException ex) {
            imageDir.delete();
            throw new BusinessException(ex);
        }
        System.out.println(relativeDirString);
        return new Image(relativeDirString, Integer.parseInt(number.trim()));//tempDir.listFiles().length -1);

    }
}
