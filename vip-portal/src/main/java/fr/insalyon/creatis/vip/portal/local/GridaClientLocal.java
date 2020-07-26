package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.common.bean.GridData;
import fr.insalyon.creatis.grida.common.bean.GridData.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("local")
public class GridaClientLocal extends GRIDAClient {

    @Value("${local.grida.root")
    private String localRoot;

    public GridaClientLocal() {
        super(null, 0, null);
    }

    @Override
    public String getRemoteFile(String remoteFile, String localDir) throws GRIDAClientException {
        Path from = Paths.get(localRoot).resolve(remoteFile);
        Path to = Paths.get(localDir).resolve(from.getFileName());
        try {
            Files.copy(from, to);
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
        Path dirPath = Paths.get(localRoot).resolve(dir);
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
        return Paths.get(localRoot).resolve(fileName).toFile().lastModified();
    }

    @Override
    public List<Long> getModificationDate(List<String> filesList) throws GRIDAClientException {
        return filesList.stream()
                .map(s -> Paths.get(localRoot).resolve(s).toFile())
                .map(file -> file.lastModified())
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFile(String localFile, String remoteDir) throws GRIDAClientException {
        Path from = Paths.get(localFile);
        Path to = Paths.get(localRoot).resolve(remoteDir).resolve(from.getFileName());
        try {
            Files.copy(from, to);
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
        try {
            Files.delete(Paths.get(localRoot).resolve(path));
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
        try {
            Files.createDirectory(Paths.get(localRoot).resolve(path).resolve(folderName));
        } catch (IOException e) {
            throw new GRIDAClientException(e);
        }
    }

    @Override
    public void rename(String oldPath, String newPath) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public boolean exist(String remotePath) throws GRIDAClientException {
        return Paths.get(localRoot).resolve(remotePath).toFile().exists();
    }
}
