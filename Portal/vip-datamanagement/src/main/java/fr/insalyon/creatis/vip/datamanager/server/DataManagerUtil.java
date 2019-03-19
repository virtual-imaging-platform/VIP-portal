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

import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.*;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;

import java.io.File;
import java.net.URI;
import java.util.*;

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
        Server server = Server.getInstance();
        baseDir = parsePath(baseDir, DataManagerConstants.USERS_HOME,
                server.getDataManagerUsersHome()
                + "/" + user.getFolder());

        baseDir = parsePath(baseDir, DataManagerConstants.TRASH_HOME,
                server.getDataManagerUsersHome()
                + "/" + user.getFolder()
                + "_" + DataManagerConstants.TRASH_HOME);

        baseDir = parsePath(baseDir, DataManagerConstants.USERS_FOLDER,
                server.getDataManagerUsersHome());

        baseDir = parsePath(baseDir, DataManagerConstants.VO_ROOT_FOLDER,
                            server.getVoRoot());

        try {
            for (Group group : CoreDAOFactory.getDAOFactory().getGroupDAO().getGroups()) {
                String folderName = group.getName().replaceAll(" ", "_");

                baseDir = parsePath(baseDir, group.getName() + DataManagerConstants.GROUP_APPEND,
                        server.getDataManagerGroupsHome()
                        + "/" + folderName);

                baseDir = parsePath(baseDir, group.getName(),
                        server.getDataManagerGroupsHome()
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
            /* the directory should correspond perfectly, not partially
            * So either the path ends after the root subdirectory, either there is a slash
            * and another path element after.
            * */
            int nextCharIndex = baseDir.indexOf(pattern) + pattern.length();
            if (baseDir.length() == nextCharIndex || baseDir.charAt(nextCharIndex) == '/') {
                return baseDir.replace(pattern, toReplace);
            } else {
                return baseDir;
            }
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

        Server server = Server.getInstance();

        if (baseDir.startsWith("lfn://")) {
            baseDir = URI.create(baseDir).getPath();
        }

        baseDir = replaceLfnUserPrefix(baseDir, currentUserFolder,
                Server.getInstance().getDataManagerUsersHome(),
                Server.getInstance().getAltDataManagerUsersHome());

        try {
            for (Group group : CoreDAOFactory.getDAOFactory().getGroupDAO().getGroups()) {
                baseDir = replaceLfnGroupPrefix(baseDir, group.getName(),
                        server.getDataManagerGroupsHome(),
                        server.getAltDataManagerGroupsHome());
            }
        } catch (DAOException ex) {
            throw new DataManagerException(ex);
        }

        baseDir = baseDir.replace(
            server.getVoRoot(),
            DataManagerConstants.ROOT + "/" + DataManagerConstants.VO_ROOT_FOLDER);

        return baseDir;
    }

    private static String replaceLfnUserPrefix(String path, String currentUserFolder, String... prefixesToReplace) {
        String prefixToReplace = null;
        for (String prefixToTest : prefixesToReplace) {
            if (prefixToTest != null && !prefixToTest.isEmpty()
                    && path.contains(prefixToTest)) {
                prefixToReplace = prefixToTest;
                break;
            }
        }
        if (prefixToReplace != null) {
            path = path.replace(prefixToReplace + "/", "");

            // sometimes there's still a leading "/" left
            while (path.startsWith("/")) {
                path = path.substring(1);
            }

            int index = path.indexOf("/");

            if (index != -1) {
                if (path.substring(0, index).equals(currentUserFolder)) {
                    return DataManagerConstants.ROOT + "/"
                            + DataManagerConstants.USERS_HOME
                            + path.substring(index);
                } else {
                    return DataManagerConstants.ROOT + "/"
                            + DataManagerConstants.USERS_FOLDER + "/"
                            + path;
                }
            } else {
                return DataManagerConstants.ROOT + "/"
                        + DataManagerConstants.USERS_HOME;
            }
        } else {
            return path;
        }
    }

    private static String replaceLfnGroupPrefix(String path, String groupName, String... prefixesToReplace) {
        for (String prefixToTest : prefixesToReplace) {
            if (prefixToTest != null && !prefixToTest.isEmpty()) {
                // the prefix used in the catalog
                String realPrefix = prefixToTest + "/" + groupName.replaceAll(" ", "_");
                // the prefix shown to the user
                String userPrefix = DataManagerConstants.ROOT
                        + "/" + groupName
                        + DataManagerConstants.GROUP_APPEND;

                path = path.replace(realPrefix, userPrefix);
            }
        }
        return path;
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
