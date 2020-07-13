package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Service
@Transactional
public class LfcPathsBusiness {

    private Server server;
    private GroupDAO groupDAO;

    @Autowired
    public LfcPathsBusiness(Server server, GroupDAO groupDAO) {
        this.server = server;
        this.groupDAO = groupDAO;
    }

    public String parseBaseDir(User user, String baseDir)
            throws DataManagerException {
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
            for (Group group : groupDAO.getGroups()) {
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

    private String parsePath(String baseDir, String pattern, String toReplace) {
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

    public String parseRealDir(String baseDir, String currentUserFolder)
            throws DataManagerException {

        if (baseDir.startsWith("lfn://")) {
            baseDir = URI.create(baseDir).getPath();
        }

        baseDir = replaceLfnUserPrefix(
                baseDir, currentUserFolder, server.getDataManagerUsersHome(),
                server.getAltDataManagerUsersHome());

        try {
            for (Group group : groupDAO.getGroups()) {
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

    private String replaceLfnUserPrefix(String path, String currentUserFolder, String... prefixesToReplace) {
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

    private String replaceLfnGroupPrefix(String path, String groupName, String... prefixesToReplace) {
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
}
