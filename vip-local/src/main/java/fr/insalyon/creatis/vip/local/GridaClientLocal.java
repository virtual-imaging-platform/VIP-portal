package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.common.bean.GridData;
import fr.insalyon.creatis.grida.common.bean.GridData.Type;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * overides original GRIDAClient by a simpler version that do local file
 * transfers in a configured "localRoot" directory.
 *
 * replication methods are not implemented
 */
@Component
@Profile("local")
@Primary
@DependsOn("localConfiguration") // to populate rootDirName @Value
public class GridaClientLocal extends GRIDAClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;

    private Path localRoot; // local folder simulating remote LFN hierarchy

    @Autowired
    public GridaClientLocal(
            Server server,
            Resource vipConfigFolder,
            @Value("${local.grida.root.dirname}") String rootDirName) throws IOException {
        super(null, 0, null);
        this.server = server;
        logger.info("Creating local GRIDAClient with rootDirName: {}", rootDirName);
        localRoot = Paths.get(vipConfigFolder.getURI()).resolve(rootDirName);

    }

    @PostConstruct
    public void init() {
        // creating root if needed
        logger.info("Local GRIDAClient root: {}", localRoot);
        if (localRoot.toFile().exists() && ! localRoot.toFile().isDirectory()) {
            throw new IllegalStateException("grida local root must be a directory");
        } else if (localRoot.toFile().exists()) {
            return;
        }
        // create folder test root folder
        createDirectory(localRoot, "grida local root");
        // create users and groups lfn folders
        String usersFolderLFN = server.getDataManagerUsersHome();
        String groupsFolderLFN = server.getDataManagerGroupsHome();
        Path usersFolder = localRoot.resolve(usersFolderLFN.substring(1)); // remove first slash to make usersFolderLFN relative
        Path groupsFolder = localRoot.resolve(groupsFolderLFN.substring(1)); // remove first slash to make groupsFolderLFN relative
        createDirectory(usersFolder, "users lfn root folder");
        createDirectory(groupsFolder, "groups lfn root folder");
    }

    private void createDirectory(Path dir, String description) {
        logger.info("Creating {} directory: {}", description, dir);
        boolean mkdirOK = dir.toFile().mkdirs();
        if ( ! mkdirOK) {
            throw new IllegalStateException("Error creating " + description + " directory");
        }
    }

    @Override
    public String getRemoteFile(String remoteFile, String localDir) throws GRIDAClientException {
        while (remoteFile.startsWith("/")) {
            remoteFile = remoteFile.substring(1);
        }
        Path from = localRoot.resolve(remoteFile);
        Path to = Paths.get(localDir).resolve(from.getFileName());
        try {
            Files.createDirectories(Paths.get(localDir));
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new GRIDAClientException(e);
        }
        return to.toString();
    }

    @Override
    public String getRemoteFolder(String remoteDir, String localDir) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public List<GridData> getFolderData(String dir, boolean refresh) throws GRIDAClientException {
        while (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        Path dirPath = localRoot.resolve(dir);
        try {
            return Files.list(dirPath).map( path -> path.toFile()).map(file ->
                new GridData(
                        file.getName(),
                        file.isDirectory() ? Type.Folder : Type.File,
                        file.length(),
                        String.valueOf(file.lastModified()),
                        "", "")
            ).collect(Collectors.toList());
        } catch (IOException e) {
            throw new GRIDAClientException(e);
        }
    }

    @Override
    public Long getModificationDate(String fileName) throws GRIDAClientException {
        while (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        return localRoot.resolve(fileName).toFile().lastModified();
    }

    @Override
    public List<Long> getModificationDate(List<String> filesList) throws GRIDAClientException {
        return filesList.stream()
                .map(s -> {
                    while (s.startsWith("/")) {
                        s = s.substring(1);
                    }
                    return s;
                })
                .map(s -> localRoot.resolve(s).toFile())
                .map(file -> file.lastModified())
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(String localFile, String remoteDir) throws GRIDAClientException {
        while (remoteDir.startsWith("/")) {
            remoteDir = remoteDir.substring(1);
        }
        Path from = Paths.get(localFile);
        Path to = localRoot.resolve(remoteDir).resolve(from.getFileName());
        if ( ! to.getParent().toFile().exists()) {
            createDirectory(to.getParent(), "");
        }
        try {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new GRIDAClientException(e);
        }
        return Paths.get(remoteDir).resolve(from.getFileName()).toString();
    }

    @Override
    public String uploadFileToSE(String localFile, String remoteDir, String storageElement) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public String uploadFileToSE(String localFile, String remoteDir, List<String> storageElementsList) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public void replicateToPreferredSEs(String remoteFile) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public void delete(String path) throws GRIDAClientException {
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        try {
            Files.delete(localRoot.resolve(path));
        } catch (IOException e) {
            throw new GRIDAClientException(e);
        }
    }

    @Override
    public void delete(List<String> paths) throws GRIDAClientException {
        for (String path : paths) {
            delete(path);
        }
    }

    @Override
    public void createFolder(String path, String folderName) throws GRIDAClientException {
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        try {
            Files.createDirectory(localRoot.resolve(path).resolve(folderName));
        } catch (IOException e) {
            logger.error("Error creating folder {}", folderName, e);
            throw new GRIDAClientException(e);
        }
    }

    @Override
    public void rename(String oldPath, String newPath) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public boolean exist(String remotePath) throws GRIDAClientException {
        while (remotePath.startsWith("\\") || remotePath.startsWith("/")) { // TODO : / ou \
            remotePath = remotePath.substring(1);
        }
        return localRoot.resolve(remotePath).toFile().exists();
    }
}
