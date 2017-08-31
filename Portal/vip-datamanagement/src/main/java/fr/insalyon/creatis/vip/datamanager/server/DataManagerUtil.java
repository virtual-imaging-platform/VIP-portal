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
package fr.insalyon.creatis.vip.datamanager.server;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
    public static String parseBaseDir(User user, String baseDir) throws DataManagerException {

        baseDir = parsePath(baseDir, DataManagerConstants.USERS_HOME,
                Server.getInstance().getDataManagerUsersHome()
                + "/" + user.getFolder());

        baseDir = parsePath(baseDir, DataManagerConstants.TRASH_HOME,
                Server.getInstance().getDataManagerUsersHome()
                + "/" + user.getFolder()
                + "_" + DataManagerConstants.TRASH_HOME);

        baseDir = parsePath(baseDir, DataManagerConstants.USERS_FOLDER,
                Server.getInstance().getDataManagerUsersHome());

        baseDir = parsePath(baseDir, DataManagerConstants.BIOMED_HOME,
                "/grid/biomed");

        try {
            for (Group group : CoreDAOFactory.getDAOFactory().getGroupDAO().getGroups()) {
                String folderName = group.getName().replaceAll(" ", "_");

                baseDir = parsePath(baseDir, group.getName() + DataManagerConstants.GROUP_APPEND,
                        Server.getInstance().getDataManagerGroupsHome()
                        + "/" + folderName);

                baseDir = parsePath(baseDir, group.getName(),
                        Server.getInstance().getDataManagerGroupsHome()
                        + "/" + folderName);
            }
        } catch (DAOException ex) {
            throw new DataManagerException(ex);
        }

        return baseDir;
    }

    /**
     *
     * @param groups
     * @return
     */
     public static List<String> getPaths(List<String> groups){
        ArrayList<String> paths = new ArrayList();
        for(String s : groups)
            paths.add(s.replaceAll(" ", "_"));
        return paths;
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
     * @param currentUserFolder
     * @return
     * @throws DataManagerException
     */
    public static String parseRealDir(String baseDir, String currentUserFolder)
            throws DataManagerException {


        if (baseDir.startsWith("lfn://")) {
            baseDir = URI.create(baseDir).getPath();
        }

        if (baseDir.contains(Server.getInstance().getDataManagerUsersHome())) {
            baseDir = baseDir.replace(Server.getInstance().getDataManagerUsersHome() + "/", "");

            // sometimes there's still a leading "/" left
            while (baseDir.startsWith("/")) {
                baseDir = baseDir.substring(1);
            }

            int index = baseDir.indexOf("/");

            if (index != -1) {
                if (baseDir.substring(0, index).equals(currentUserFolder)) {
                    return DataManagerConstants.ROOT + "/"
                            + DataManagerConstants.USERS_HOME
                            + baseDir.substring(index, baseDir.length());
                } else {
                    return DataManagerConstants.ROOT + "/"
                            + DataManagerConstants.USERS_FOLDER + "/"
                            + baseDir;
                }
            } else {
                return DataManagerConstants.ROOT + "/"
                        + DataManagerConstants.USERS_HOME;
            }
        }

        try {
            for (Group group : CoreDAOFactory.getDAOFactory().getGroupDAO().getGroups()) {
                baseDir = baseDir.replace(
                        Server.getInstance().getDataManagerGroupsHome()
                        + "/" + group.getName().replaceAll(" ", "_"),
                        DataManagerConstants.ROOT + "/" + group.getName()
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

        String rootDirectory = Server.getInstance().getDataManagerPath()
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
}
