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

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.*;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.util.*;

import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.*;

/**
 * Created by abonnet on 5/24/17.
 */
@Service
@Transactional
public class LFCPermissionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DataManagerBusiness dataManagerBusiness;
    private LfcPathsBusiness lfcPathsBusiness;

    @Autowired
    public LFCPermissionBusiness(DataManagerBusiness dataManagerBusiness, LfcPathsBusiness lfcPathsBusiness) {
        this.dataManagerBusiness = dataManagerBusiness;
        this.lfcPathsBusiness = lfcPathsBusiness;
    }

    public enum LFCAccessType {
        READ, UPLOAD, DELETE
    }

    public Boolean isLFCPathAllowed(User user, String path, LFCAccessType LFCAccessType, Boolean enableAdminArea)
            throws BusinessException {
        // normalize to remove "..".
        path = Paths.get(path).normalize().toString();

        // verify the root ("/vip") is present and that it is not written on
        if ( ! verifyRoot(user, path, LFCAccessType)) return false;

        // Root is always filtered by user so always permitted
        if (path.equals(ROOT)) return true;

        // do not delete synchronized stuff
        if (LFCAccessType == LFCPermissionBusiness.LFCAccessType.DELETE
            && isPathSynchronized(user, path)) {
            return false;
        }
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
            throws BusinessException {
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

    private boolean isPathSynchronized(User user, String path)
            throws BusinessException {
        List<SSH> sshs = dataManagerBusiness.getSSHConnections();
        List<String> lfcDirSSHSynchronization = new ArrayList<>();
        for (SSH ssh : sshs) {
            if (ssh.getTransferType().equals(TransferType.Synchronization)) {
                lfcDirSSHSynchronization.add(ssh.getLfcDir());
            }
        }

        String lfcBaseDir;
        try {
            lfcBaseDir = lfcPathsBusiness.parseBaseDir(user, path);
        } catch (DataManagerException e) {
            throw new BusinessException("Internal error in data API");
        }
        for (String s : lfcDirSSHSynchronization) {
            if (lfcBaseDir.startsWith(s)) {
                logger.error("({}) Try to delete  synchronized file '{}'", user.getEmail(), path);
                return false;
            }
        }
        return true;
    }

}
