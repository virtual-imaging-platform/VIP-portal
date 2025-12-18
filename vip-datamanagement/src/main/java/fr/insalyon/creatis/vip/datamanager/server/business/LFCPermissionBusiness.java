package fr.insalyon.creatis.vip.datamanager.server.business;

import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.GROUP_APPEND;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.ROOT;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.TRASH_HOME;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.USERS_FOLDER;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.USERS_HOME;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.VO_ROOT_FOLDER;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.models.User;

@Service
@Transactional
public class LFCPermissionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    public LFCPermissionBusiness() {
    }

    public enum LFCAccessType {
        READ, UPLOAD, DELETE
    }

    public Boolean isLFCPathAllowed(User user, String path, LFCAccessType LFCAccessType, Boolean enableAdminArea)
            throws VipException {
        // normalize to remove "..".
        path = Paths.get(path).normalize().toString();

        // verify the root ("/vip") is present and that it is not written on
        if ( ! verifyRoot(user, path, LFCAccessType)) return false;

        // Root is always filtered by user so always permitted
        if (path.equals(ROOT)) return true;

        // else it all depends of the first directory
        String firstDir = getFirstDirectoryName(path);
        // always can access its home and its trash
        if (firstDir.equals(USERS_HOME)) return true;
        if (firstDir.equals(TRASH_HOME)) return true;
        if (firstDir.equals(USERS_FOLDER) || firstDir.equals(VO_ROOT_FOLDER)) {
            // restricted to admin
            return verifyAdminArea(user, path, enableAdminArea);
        }
        // else it should be a group folder
        if (!firstDir.endsWith(GROUP_APPEND)) {
            logger.error("({}) Wrong lfc access to '{}'",
                    user.getEmail(), path);
            return false;
        }
        String groupName = firstDir.substring(0,firstDir.length()-GROUP_APPEND.length());
        return isGroupAllowed(user, groupName, LFCAccessType, enableAdminArea);
    }

    private String getFirstDirectoryName(String path) {
        path = path.substring(ROOT.length() + 1); // remove trailing slash
        int nextSlashIndex = path.indexOf('/');
        if (nextSlashIndex > 0) {
            return path.substring(0, nextSlashIndex);
        } else {
            return path;
        }
    }

    private Boolean verifyRoot(
            User user, String path, LFCAccessType LFCAccessType)
            throws VipException {
        // verify path begins with the root
        if (!path.startsWith(ROOT)) {
            logger.error("({}) Access to a lfc not starting with the root '{}'",
                    user.getEmail(), path);
            return false;
        }
        // the root or a direct subdirectory of root cannot be written or deleted
        boolean unwritable = path.equals(ROOT) ||
                path.lastIndexOf('/') == ROOT.length();
        if (unwritable &&
                LFCAccessType != LFCPermissionBusiness.LFCAccessType.READ) {
            logger.error("({}) Unexpected write lfc access to '{}'",
                    user.getEmail(), path);
            return false;
        }
        return true;
    }

    private boolean verifyAdminArea(User user, String path, Boolean enableAdminArea) {
        if ( ! user.isSystemAdministrator()) {
            logger.error("({}) Non admin trying to access an admin folder : {}",
                    user.getEmail(), path);
            return false;
        }
        if ( ! enableAdminArea) {
            logger.error("({}) LFC access not enabled to admins : {}",
                    user.getEmail(), path);
            return false;
        }
        return true;
    }

    private boolean isGroupAllowed(
            User user, String groupName, LFCAccessType LFCAccessType,
            Boolean enableAdminArea) {
        if (user.isSystemAdministrator() && enableAdminArea) {
            return true;
        }
        // user must have access to this group
        if (!user.hasGroupAccess(groupName)) {
            logger.error("({}) Trying to access an unauthorized goup '{}'",
                    user.getEmail(), groupName);
            return false;
        }
        // beginner cant write/delete in groups folder
        if (LFCAccessType != LFCPermissionBusiness.LFCAccessType.READ && user.getLevel() == UserLevel.Beginner) {
            logger.error("({}) Beginner user try to upload/delete in a group '{}'",
                    user.getEmail(), groupName);
            return false;
        }
        return true;
    }
}
