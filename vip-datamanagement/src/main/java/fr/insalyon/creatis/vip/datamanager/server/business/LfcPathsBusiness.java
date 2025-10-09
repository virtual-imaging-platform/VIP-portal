package fr.insalyon.creatis.vip.datamanager.server.business;

import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.DOWNLOAD_FOLDER;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;

/*
    Utility service to convert user-readable virtual vip paths to real lfn paths
    back and forth. For instance /vip/Home/file.txt to
    /prefix/../user_folder/user_name/file.txt
 */
@Service
@Transactional
public class LfcPathsBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
            for (Group group : groupDAO.get()) {
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

        if (baseDir.startsWith("lfn:/")) {
            baseDir = baseDir.substring(4);
        } else if (baseDir.startsWith("file:")) {
            baseDir = baseDir.substring(5);
        }
        // only keep 1 slash
        if  (baseDir.startsWith("/")) {
            while (baseDir.startsWith("/")) {
                baseDir = baseDir.substring(1);
            }
            baseDir = "/" + baseDir;
        }

        baseDir = replaceLfnUserPrefix(
                baseDir, currentUserFolder, server.getDataManagerUsersHome(),
                server.getAltDataManagerUsersHome());

        try {
            for (Group group : groupDAO.get()) {
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
            if (prefixToTest != null && !prefixToTest.isEmpty() && path.contains(prefixToTest)) {
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

    public String getLocalDirForGridaMultiFilesDownload(String packageName) {
        // give a temp folder path. Grida will delete it and create a "*.zip"
        // archive with the folder name
        return Paths.get(
                server.getDataManagerPath(),
                DOWNLOAD_FOLDER,
                "file-packages",
                packageName).toString();
    }

    public String getLocalDirForGridaFolderDownload(String remoteFolderPath) throws VipException {
        // give folder itself, grida will delete it and create a "*.zip" archive
        // with the folder name
        return getLocalDownloadPath(remoteFolderPath);
    }

    public String getLocalDirForGridaFileDownload(String remoteFilePath) throws VipException {
        // give parent folder, grida will download the file into it
        return Paths.get(getLocalDownloadPath(remoteFilePath)).getParent().toString();
    }
    /*
     Transform a full remote path (generally a LFN path) to the local directory
      path on the machine where it will be downloaded.
     */

    public String getLocalDownloadPath(String remotePathString) throws VipException {
        remotePathString = remotePathString.replaceAll(" ", "");
        Path remotePath;
        try {
            remotePath = Paths.get(remotePathString);
        } catch (InvalidPathException e) {
            logger.error("Invalid download path : {}", remotePathString, e);
            throw new VipException("Invalid download path", e);
        }
        // verify it is an absolute path
        if ( ! remotePath.isAbsolute()) {
            logger.error("Cannot download a relative path : {}", remotePathString);
            throw new VipException("Cannot download a relative path");
        }
        // remove ".." and "." and avoid security issues
        remotePath = remotePath.normalize();
        // return full local path, in local root download path
        return Paths.get(
                server.getDataManagerPath(),
                DOWNLOAD_FOLDER,
                remotePath.toString()).toString();
    }


}
