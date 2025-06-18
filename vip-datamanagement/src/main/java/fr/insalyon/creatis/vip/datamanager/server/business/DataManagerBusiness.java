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

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.common.bean.CachedFile;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DataManagerBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GRIDAClient gridaClient;
    private GRIDACacheClient gridaCacheClient;
    private ConfigurationBusiness configurationBusiness;
    private LfcPathsBusiness lfcPathsBusiness;
    private Server server;

    @Autowired
    public DataManagerBusiness(
            GRIDAClient gridaClient, GRIDACacheClient gridaCacheClient,
            ConfigurationBusiness configurationBusiness, LfcPathsBusiness lfcPathsBusiness, Server server) {
        this.gridaClient = gridaClient;
        this.gridaCacheClient = gridaCacheClient;
        this.configurationBusiness = configurationBusiness;
        this.lfcPathsBusiness = lfcPathsBusiness;
        this.server = server;
    }

    public void deleteLocalFile(String fileName) {

        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        } else {
            logger.error("File '" + fileName + "' does not exist.");
        }
    }

    public List<DMCachedFile> getCachedFiles() throws BusinessException {

        try {

            List<CachedFile> cachedFilesList = gridaCacheClient.getCachedFiles();
            List<DMCachedFile> dmCachedFiles = new ArrayList<>();

            for (CachedFile cf : cachedFilesList) {
                dmCachedFiles.add(new DMCachedFile(cf.getPath(),
                        cf.getName(), FileUtils.parseFileSize((long) cf.getSize()),
                        cf.getFrequency(), cf.getLastUsage()));
            }

            return dmCachedFiles;

        } catch (GRIDAClientException ex) {
            logger.error("Error getting cached files", ex);
            throw new BusinessException(ex);
        }
    }

    public void deleteCachedFiles(List<String> cachedFiles) throws BusinessException {

        try {
            for (String path : cachedFiles) {
                gridaCacheClient.deleteCachedFile(path);
            }
        } catch (GRIDAClientException ex) {
            logger.error("Error deleting cached files {}", cachedFiles, ex);
            throw new BusinessException(ex);
        }
    }

    public String getRemoteFile(User user, String remoteFile)
            throws BusinessException {

        String remotePath;
        String localDir;
        try {
            remotePath = lfcPathsBusiness.parseBaseDir(user, remoteFile);
            localDir = lfcPathsBusiness.getLocalDirForGridaFileDownload(remotePath);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
        try {
            return gridaClient.getRemoteFile(
                    remotePath,
                    localDir);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting file {} to {} by {}",
                    remoteFile, localDir, user, ex);
            throw new BusinessException(ex);
        }
    }

    public String getLFCDir(String email, String lfcName) throws BusinessException {

        User user = configurationBusiness.getUser(email);
        String homeDir = server.getDataManagerUsersHome() + "/" + user.getFolder();

        return (homeDir + "/" + lfcName);
    }

    public String getUploadRootDirectory(boolean local) {

        String rootDirectory = server.getDataManagerPath()
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
