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
package fr.insalyon.creatis.vip.datamanager.server;

import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentPoolClient;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerUtil {

    /**
     * 
     * @param user
     * @param baseDir
     * @return
     * @throws DataManagerException 
     */
    public static String parseBaseDir(String user, String baseDir) throws DataManagerException {

        baseDir = parsePath(baseDir, DataManagerConstants.USERS_HOME,
                ServerConfiguration.getInstance().getDataManagerUsersHome()
                + "/" + user.replaceAll(" ", "_").toLowerCase());

        baseDir = parsePath(baseDir, DataManagerConstants.TRASH_HOME,
                ServerConfiguration.getInstance().getDataManagerUsersHome()
                + "/" + user.replaceAll(" ", "_").toLowerCase()
                + "_" + DataManagerConstants.TRASH_HOME);

        baseDir = parsePath(baseDir, DataManagerConstants.BIOMED_HOME,
                "/grid/biomed");

        try {
            for (String groupName : DAOFactory.getDAOFactory().getGroupDAO().getGroups()) {
                String folderName = groupName.replaceAll(" ", "_");

                baseDir = parsePath(baseDir, groupName + DataManagerConstants.GROUP_APPEND,
                        ServerConfiguration.getInstance().getDataManagerGroupsHome()
                        + "/" + folderName);

                baseDir = parsePath(baseDir, groupName,
                        ServerConfiguration.getInstance().getDataManagerGroupsHome()
                        + "/" + folderName);
            }
        } catch (DAOException ex) {
            throw new DataManagerException(ex);
        }

        return baseDir;
    }

    /**
     * 
     * @param baseDir
     * @param pattern
     * @param toReplace
     * @return 
     */
    private static String parsePath(String baseDir, String pattern, String toReplace) {
        pattern = DataManagerConstants.ROOT + "/" + pattern;
        if (baseDir.contains(pattern)) {
            return baseDir.replace(pattern, toReplace);
        }
        return baseDir;
    }

    /**
     * 
     * @param baseDir
     * @return
     * @throws DataManagerException 
     */
    public static String parseRealDir(String baseDir) throws DataManagerException {
        if (baseDir.startsWith("lfn://")) {
            try {
                baseDir = new URI(baseDir).getPath();
            } catch (URISyntaxException ex) {
                throw new DataManagerException(ex);
            }
        }

        if (baseDir.contains(ServerConfiguration.getInstance().getDataManagerUsersHome())) {
            baseDir = baseDir.replace(ServerConfiguration.getInstance().getDataManagerUsersHome() + "/", "");
            if (baseDir.indexOf("/") != -1) {
                baseDir = baseDir.substring(baseDir.indexOf("/"), baseDir.length());
            } else {
                baseDir = "";
            }
            baseDir = DataManagerConstants.ROOT + "/"
                    + DataManagerConstants.USERS_HOME + baseDir;
        }

        try {
            for (String groupName : DAOFactory.getDAOFactory().getGroupDAO().getGroups()) {
                baseDir = baseDir.replace(
                        ServerConfiguration.getInstance().getDataManagerGroupsHome()
                        + "/" + groupName.replaceAll(" ", "_"),
                        DataManagerConstants.ROOT + "/" + groupName
                        + DataManagerConstants.GROUP_APPEND);
            }
        } catch (DAOException ex) {
            throw new DataManagerException(ex);
        }

        baseDir = baseDir.replace("/grid/biomed",
                DataManagerConstants.ROOT + "/" + DataManagerConstants.BIOMED_HOME);

        return baseDir;
    }

    /**
     * 
     * @param local
     * @return 
     */
    public static String getUploadRootDirectory(boolean local) {

        String rootDirectory = ServerConfiguration.getInstance().getDataManagerPath()
                + "/uploads/";

        if (!local) {
            rootDirectory += System.nanoTime() + "/";
        }

        File dir = new File(rootDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return rootDirectory;
    }

    /**
     * 
     * @param proxyFileName
     * @return 
     */
    public static VletAgentClient getVletAgentClient(String proxyFileName) {

        return new VletAgentClient(
                ServerConfiguration.getInstance().getVletagentHost(),
                ServerConfiguration.getInstance().getVletagentPort(),
                proxyFileName);
    }
    
    /**
     * 
     * @param proxyFileName
     * @return 
     */
    public static VletAgentPoolClient getVletAgentPoolClient(String proxyFileName) {

        return new VletAgentPoolClient(
                ServerConfiguration.getInstance().getVletagentHost(),
                ServerConfiguration.getInstance().getVletagentPort(),
                proxyFileName);
    }
}
