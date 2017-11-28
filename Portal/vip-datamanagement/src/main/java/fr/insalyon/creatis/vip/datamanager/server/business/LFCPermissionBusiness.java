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
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import org.apache.log4j.Logger;

import java.util.*;

import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.*;

/**
 * Created by abonnet on 5/24/17.
 */
public class LFCPermissionBusiness {

    private static final Logger logger = Logger.getLogger(LFCPermissionBusiness.class);

    public enum LFCAccessType {
        READ, UPLOAD, DELETE
    }

    public void checkPermission(User user, String lfcPath, LFCAccessType LFCAccessType) throws BusinessException {
        checkRootPermission(lfcPath, LFCAccessType);
        // Root is always filtered so always permited
        if (lfcPath.equals(ROOT)) return;
        // else it all depends of the first directory
        String firstDir = getFirstDirectoryName(lfcPath);
        // always can access its home and its trash
        if (firstDir.equals(USERS_HOME)) return;
        if (firstDir.equals(TRASH_HOME)) return;
        // currently no admin access is possible via the api for security reasons
        if (firstDir.equals(USERS_FOLDER) || firstDir.equals(VO_ROOT_FOLDER)) {
            logger.error("Trying to access admin folders from api");
            throw new BusinessException("Unauthorized LFC access");
        }
        // else it should be a group folder
        if (!firstDir.endsWith(GROUP_APPEND)) {
            logger.error("Unexpected lfc access to: " + firstDir);
            throw new BusinessException("Unauthorized LFC access");
        }
        String groupName = firstDir.substring(0,firstDir.length()-GROUP_APPEND.length());
        checkGroupPermission(user, groupName, LFCAccessType);
        if (LFCAccessType == LFCPermissionBusiness.LFCAccessType.DELETE) {
            checkAdditionalDeletePermission(user, lfcPath);
        }
        // all check passed : all good !
    }

    private String getFirstDirectoryName(String lfcPath) {
        lfcPath = lfcPath.substring(ROOT.length() + 1); // remove trailing slash
        int nextSlashIndex = lfcPath.indexOf('/');
        if (nextSlashIndex > 0) {
            return lfcPath.substring(0, nextSlashIndex);
        } else {
            return lfcPath;
        }
    }

    private void checkRootPermission(String lfcPath, LFCAccessType LFCAccessType) throws BusinessException {
        // verify path begins with the root
        if (!lfcPath.startsWith(ROOT)) {
            logger.error("Access to a lfc not starting with the root:" + lfcPath);
            throw new BusinessException("Unauthorized LFC access");
        }
        // read always possible
        if (LFCAccessType == LFCPermissionBusiness.LFCAccessType.READ) return;
        // else it cannot be THE root nor a direct subdirectory of root
        if (lfcPath.equals(ROOT) ||
                lfcPath.lastIndexOf('/') == ROOT.length()) {
            logger.error("Unexpected lfc access to: " + lfcPath);
            throw new BusinessException("Unauthorized LFC access");
        }
    }

    private void checkGroupPermission(User user, String groupname, LFCAccessType LFCAccessType) throws BusinessException {
        // beginner cant write/delete in groups folder
        if (LFCAccessType != LFCAccessType.READ && user.getLevel() == UserLevel.Beginner) {
            logger.error("beginner user try to upload/delete in a group:" + user.getEmail() +"/" + groupname);
            throw new BusinessException("Unauthorized LFC access");
        }
        // otherwise it must have access to this group
        if (!user.hasGroupAccess(groupname)) {
            logger.error("Trying to access an unauthorized goup");
            throw new BusinessException("Unauthorized LFC access");
        }
    }

    private void checkAdditionalDeletePermission(User user, String lfcPath) throws BusinessException {
        checkSynchronizedDirectories(user, lfcPath);
        if(lfcPath.endsWith("Dropbox")){
            logger.error("Trying to delete a dropbox directory :" + lfcPath);
            throw new BusinessException("Unauthorized LFC access");
        }
    }

    private void checkSynchronizedDirectories(User user, String lfcPath) throws BusinessException {
        List<SSH> sshs;
        try {
            sshs = new DataManagerBusiness().getSSHConnections();
        } catch (BusinessException e) {
            logger.error("Error listing synchronized directories");
            throw new BusinessException("Error listing synchronized directories");
        }
        List<String> lfcDirSSHSynchronization = new ArrayList<String>();
        for (SSH ssh : sshs) {
            if (ssh.getTransferType().equals(TransferType.Synchronization)) {
                lfcDirSSHSynchronization.add(ssh.getLfcDir());
            }
        }

        String lfcBaseDir;
        try {
            lfcBaseDir = DataManagerUtil.parseBaseDir(user, lfcPath);
        } catch (DataManagerException e) {
            logger.error("Error parsing api path :" + lfcPath);
            throw new BusinessException("Internal error in data API");
        }
        for (String s : lfcDirSSHSynchronization) {
            if (lfcBaseDir.startsWith(s)) {
                logger.error("Try to delete  synchronized file :" + lfcPath);
                throw new BusinessException("Illegal data API access");
            }
        }
    }

}
