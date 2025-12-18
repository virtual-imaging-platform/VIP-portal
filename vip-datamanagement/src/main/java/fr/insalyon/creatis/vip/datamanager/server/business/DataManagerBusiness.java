package fr.insalyon.creatis.vip.datamanager.server.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.grida.client.GRIDACacheClient;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.common.bean.CachedFile;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.models.DMCachedFile;

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

    public List<DMCachedFile> getCachedFiles() throws VipException {

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
            throw new VipException(ex);
        }
    }

    public void deleteCachedFiles(List<String> cachedFiles) throws VipException {

        try {
            for (String path : cachedFiles) {
                gridaCacheClient.deleteCachedFile(path);
            }
        } catch (GRIDAClientException ex) {
            logger.error("Error deleting cached files {}", cachedFiles, ex);
            throw new VipException(ex);
        }
    }

    public String getRemoteFile(User user, String remoteFile)
            throws VipException {

        String remotePath;
        String localDir;
        try {
            remotePath = lfcPathsBusiness.parseBaseDir(user, remoteFile);
            localDir = lfcPathsBusiness.getLocalDirForGridaFileDownload(remotePath);
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        }
        try {
            return gridaClient.getRemoteFile(
                    remotePath,
                    localDir);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting file {} to {} by {}",
                    remoteFile, localDir, user, ex);
            throw new VipException(ex);
        }
    }

    public String getLFCDir(String email, String lfcName) throws VipException {

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
