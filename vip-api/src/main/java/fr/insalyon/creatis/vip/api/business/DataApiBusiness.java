package fr.insalyon.creatis.vip.api.business;

import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.GROUP_APPEND;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.ROOT;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.TRASH_HOME;
import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.USERS_HOME;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.model.PathProperties;
import fr.insalyon.creatis.vip.api.model.UploadData;
import fr.insalyon.creatis.vip.api.model.UploadDataType;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.models.Data;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness.LFCAccessType;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import jakarta.annotation.PreDestroy;

@Service
public class DataApiBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Server server;
    private final Supplier<User> currentUserProvider;

    private final LFCBusiness lfcBusiness;
    private final TransferPoolBusiness transferPoolBusiness;
    private final LFCPermissionBusiness lfcPermissionBusiness;
    private final DataManagerBusiness dataManagerBusiness;

    private final ScheduledExecutorService scheduler;

    @Autowired
    public DataApiBusiness(
            Server server, Supplier<User> currentUserProvider,
            LFCBusiness lfcBusiness, TransferPoolBusiness transferPoolBusiness,
            LFCPermissionBusiness lfcPermissionBusiness,
            DataManagerBusiness dataManagerBusiness) {
        this.server = server;
        this.currentUserProvider = currentUserProvider;
        this.lfcBusiness = lfcBusiness;
        this.transferPoolBusiness = transferPoolBusiness;
        this.lfcPermissionBusiness = lfcPermissionBusiness;
        this.dataManagerBusiness = dataManagerBusiness;
        int parallelDownloadsNb = server.getApiParallelDownloadNb();
        logger.info("Starting threads for {} parallel downloads", parallelDownloadsNb);
        // 2 threads are needed for every download
        this.scheduler = Executors.newScheduledThreadPool(2 * parallelDownloadsNb);
    }

    @PreDestroy
    public void close() {
        logger.info("shutting down download threads");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                logger.info("Forcing download threads shutdown");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
        logger.info("download threads succesfully shutdown");
    }

    public boolean doesFileExist(String path) throws VipException {
        checkReadPermission(path);
        return path.equals(ROOT) || baseDoesFileExist(path);
    }

    public void deletePath(String path) throws VipException {
        checkPermission(path, LFCAccessType.DELETE);
        if (!baseDoesFileExist(path)) {
            logger.error("trying to delete a non-existing file : {}", path);
            throw new VipException("trying to delete a non-existing dile");
        }
        baseDeletePath(path);
    }

    public PathProperties getPathProperties(String path ) throws VipException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            return getRootPathProperties();
        }
        PathProperties pathProperties = new PathProperties();
        pathProperties.setPath(path);
        Optional<Data.Type> type = baseGetPathInfo(path);
        if (type.isEmpty()) { // path doesn't exist
            pathProperties.setExists(false);
            return pathProperties;
        }
        pathProperties.setExists(true);
        List<Data> fileData = baseGetFileData(path);
        if (type.get().equals(Data.Type.file)) {
            // this is a file, not a directory
            Data fileInfo = fileData.get(0);
            pathProperties.setIsDirectory(false);
            pathProperties.setSize(fileInfo.getLength());
            pathProperties.setLastModificationDate(
                    getTimeStampFromGridaFormatDate(fileInfo.getModificationDate()));
            pathProperties.setMimeType(getMimeType(path));
        } else {
            // its a directory
            pathProperties.setIsDirectory(true);
            pathProperties.setSize((long) fileData.size());
            pathProperties.setLastModificationDate(
                baseGetFileModificationDate(path) / 1000);
            pathProperties.setMimeType(server.getCarminApiDirectoryMimeType());
        }
        return pathProperties;
    }

    public List<PathProperties> listDirectory(String path) throws VipException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            return getRootSubDirectoriesPathProps();
        }
        Optional<Data.Type> type = baseGetPathInfo(path);
        if (!type.isPresent()) { // path doesn't exist
            logger.error("Trying to list a non-existing path ({})", path);
            throw new VipException("Error listing a directory");
        }
        if (!type.get().equals(Data.Type.folder)) {
            logger.error("Trying to list {} , but is a file :", path);
            throw new VipException("Error listing a directory");
        }
        List<Data> directoryData = baseGetFileData(path);
        List<PathProperties> res = new ArrayList<>();
        for (Data fileData : directoryData) {
            res.add(buildPathFromLfcData(path, fileData));
        }
        return res;
    }

    public File getFile(String path) throws VipException {
        checkDownloadPermission(path);
        String downloadOperationId =
            downloadFileToLocalStorage(path);
        return getDownloadFile(downloadOperationId);
    }

    public void uploadRawFileFromInputStream(String lfcPath, InputStream is)
            throws VipException {
        // TODO : check upload size ?
        checkPermission(lfcPath, LFCAccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of upload {} does not exist :", lfcPath);
            throw new VipException("Upload Directory doest not exist");
        }
        // TODO : check if it already exists
        // TODO : support archive upload
        String uploadDirectory = dataManagerBusiness.getUploadRootDirectory(false);
        // get file name and clean it as in an upload
        String fileName = DataManagerUtil.getCleanFilename(
                Paths.get(lfcPath).getFileName().toString() );
        String localPath = uploadDirectory + fileName;
        logger.debug("storing upload file in :" + localPath);
        boolean isFileEmpty = saveInputStreamToFile(is, localPath);
        if (isFileEmpty) {
            logger.info("no content in upload, creating dir : " + parentLfcPath + "/" + fileName);
            baseMkdir(parentLfcPath, fileName);
        } else {
            String opId = baseUploadFile(localPath, parentLfcPath);
            // wait for it to be over
            waitForOperationOrTimeout(opId);
        }
    }

    public void uploadCustomData(String lfcPath, UploadData uploadData)
            throws VipException {
        // TODO : check upload size ?
        // TODO : factorize with previous method
        checkPermission(lfcPath, LFCAccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of {} does not exist :", lfcPath);
            throw new VipException("Upload Directory doest not exist");
        }
        if (uploadData.getType().equals(UploadDataType.ARCHIVE)) {
            logger.error("archive upload not supported yet for ({})", lfcPath);
            throw new VipException("archive upload not supported yet");
        }
        // TODO : check if it already exists
        // TODO : support archive upload
        String uploadDirectory = dataManagerBusiness.getUploadRootDirectory(false);
        // get file name and clean it as in an upload
        String fileName = DataManagerUtil.getCleanFilename(
                Paths.get(lfcPath).getFileName().toString() );
        String localPath = uploadDirectory + fileName;
        logger.debug("storing upload file in :" + localPath);
        writeFileFromBase64(uploadData.getBase64Content(), localPath);
        String opId = baseUploadFile(localPath, parentLfcPath);
        // wait for it to be over
        waitForOperationOrTimeout(opId);
    }

    // #### PERMISSION STUFF

    private void checkReadPermission(String path) throws VipException {
        checkPermission(path, LFCAccessType.READ);
    }

    private void checkDownloadPermission(String path) throws VipException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            logger.error("cannot download root ({})", path);
            throw new VipException("Illegal data API access");
        }
        Optional<Data.Type> type = baseGetPathInfo(path);
        if (!type.isPresent()) { // path doesn't exist
            logger.error("Trying to download a non-existing file ({})", path);
            throw new VipException("Illegal data API access");
        }
        if (!type.get().equals(Data.Type.file)) {
            // it works on a directory and return a zip, but we cant check the download size
            logger.error("Trying to download a directory ({})", path);
            throw new VipException("Illegal data API access");
        }
        // path exists and is a file: check its size
        List<Data> fileData = baseGetFileData(path);
        Long maxSize = server.getCarminApiDataTransfertMaxSize();
        if (fileData.get(0).getLength() > maxSize) {
            logger.error("Trying to download a file too big ({})", path);
            throw new VipException("Illegal data API access");
        }
    }

    private void checkPermission(String path, LFCAccessType accessType)
            throws VipException {
        if ( ! lfcPermissionBusiness.isLFCPathAllowed(
            currentUserProvider.get(), path, accessType, true)) {
            throw new VipException(ApiError.UNAUTHORIZED_DATA_ACCESS, path);
        }
    }

    // #### DOWNLOAD STUFF

    private String downloadFileToLocalStorage(String path) throws VipException {
        String downloadOperationId = baseDownloadFile(path);
        waitForOperationOrTimeout(downloadOperationId);
        return downloadOperationId;
    }

    private File getDownloadFile(String operationId) throws VipException {
        PoolOperation operation = baseGetDownloadOperation(operationId);
        File file = new File(operation.getDest());
        if (file.isDirectory()) {
            file = new File(operation.getDest() + "/"
                    + FilenameUtils.getName(operation.getSource()));
        }
        return file;
    }

    // #### Operation stuff

    private void waitForOperationOrTimeout(String operationId)
            throws VipException {
        // get user in main thread because spring store auth/user information in
        // thread bound structure and it wont be available in the
        // 'isDownloadOverCall' thread
        User user = currentUserProvider.get();
        Callable<Boolean> isDownloadOverCall =
            () -> isOperationOver(operationId, user);

        // task that check every x seconds if the operation is over.
        // return true when OK or goes on indefinitly
        Callable<Boolean> waitForDownloadCall = () -> {
            while (true) {
                Future<Boolean> isDownloadOverFuture =
                        scheduler.schedule(isDownloadOverCall, getRetryDelay(), TimeUnit.SECONDS);
                if (isDownloadOverFuture.get()) {
                    return true;
                }
            }
        };
        // launch checking task
        Future<Boolean> completionFuture =
                scheduler.submit(waitForDownloadCall);
        timeoutOperationCompletionFuture(operationId, completionFuture, getTimeout());
    }

    private void timeoutOperationCompletionFuture (
            String operationId,
            Future<Boolean> completionFuture, int timeoutInSeconds) throws VipException {
        try {
            completionFuture.get(timeoutInSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Waiting for operation completion interrupted : {}", operationId ,e);
            throw new VipException("Waiting for operation completion interrupted", e);
        } catch (ExecutionException e) {
            logger.error("Error waiting for operation completion : {}", operationId ,e);
            throw new VipException("Error waiting for operation completion", e);
        } catch (TimeoutException e) {
            completionFuture.cancel(true);
            logger.error("Timeout operation completion : {}", operationId, e);
            throw new VipException("Aborting operation : too long", e);
        }
    }

    private Integer getRetryDelay() {
        return server.getCarminApiDownloadRetryInSeconds();
    }

    private Integer getTimeout() {
        return server.getCarminApiDownloadTimeoutInSeconds();
    }

    private boolean isOperationOver(String operationId, User user)
            throws VipException {
        PoolOperation operation = baseGetPoolOperation(operationId, user);

        switch (operation.getStatus()) {
            case Queued:
            case Running:
                logger.debug("status of operation {" + operationId + "} : "  + operation.getStatus());
                return false;
            case Done:
                return true;
            case Failed:
            case Rescheduled:
            default:
                logger.error("IO LFC Operation failed : {} : {}", operationId, operation.getStatus());
                throw new VipException("IO LFC Operation operation failed");
        }
    }

    // #### UPLOAD STUFF

    private void writeFileFromBase64(String base64Content, String localFilePath) throws VipException {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            InputStream inputStream = ReaderInputStream.builder()
                    .setReader(new StringReader(base64Content))
                    .setCharset(StandardCharsets.UTF_8)
                    .get();
            InputStream base64InputStream = decoder.wrap(inputStream);
            Files.copy(base64InputStream, Paths.get(localFilePath));
        } catch (IOException e) {
            logger.error("Error writing base64 file in {}", localFilePath, e);
            throw new VipException("Error writing base64 file", e);
        }
    }

    private boolean saveInputStreamToFile(InputStream is, String path) throws VipException {
        try (OutputStream fos = Files.newOutputStream(Paths.get(path))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            boolean isFileEmpty = true;
            while ((bytesRead = is.read(buffer)) != -1) {
                isFileEmpty = false;
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
            return isFileEmpty;
        } catch (IOException e) {
            logger.error("IO Error storing file {}", path, e);
            throw new VipException("Upload error", e);
        }
    }

    // #### ROOT folder STUFF

    private PathProperties getRootPathProperties() {
        PathProperties rootPathProperties = new PathProperties();
        rootPathProperties.setExists(true);
        rootPathProperties.setMimeType(server.getCarminApiDirectoryMimeType());
        rootPathProperties.setIsDirectory(true);
        rootPathProperties.setSize((long) getRootDirectoriesName().size());
        rootPathProperties.setPath(ROOT);
        return rootPathProperties;
    }

    private List<PathProperties> getRootSubDirectoriesPathProps() {
        List<PathProperties> directories = new ArrayList<>();
        for (String dirName : getRootDirectoriesName()) {
            directories.add(getRootSubDirPathProperties(dirName));
        }
        return directories;
    }

    private List<String> getRootDirectoriesName() {
        // Home + Trash + users groups
        List<String> rootDir = new ArrayList<>();
        rootDir.add(USERS_HOME);
        rootDir.add(TRASH_HOME);
        for (Group group : currentUserProvider.get().getGroups()) {
            rootDir.add(group.getName() + GROUP_APPEND);
        }
        return rootDir;
    }

    private PathProperties getRootSubDirPathProperties(String name) {
        PathProperties rootPathProperties = new PathProperties();
        rootPathProperties.setExists(true);
        rootPathProperties.setMimeType(server.getCarminApiDirectoryMimeType());
        rootPathProperties.setIsDirectory(true);
        // TODO : size ?
        rootPathProperties.setPath(ROOT + "/" + name);
        return rootPathProperties;
    }

    // #### DATA UTILS

    private PathProperties buildPathFromLfcData(String path, Data lfcData) {
        PathProperties pathProperties = new PathProperties();
        pathProperties.setExists(true);
        pathProperties.setSize(lfcData.getLength());
        pathProperties.setLastModificationDate(
                getTimeStampFromGridaFormatDate(lfcData.getModificationDate()));
        boolean isDirectory = lfcData.getType().equals(Data.Type.folder)
                || lfcData.getType().equals(Data.Type.folderSync);
        pathProperties.setIsDirectory(isDirectory);
        if (isDirectory) {
            pathProperties.setMimeType(server.getCarminApiDirectoryMimeType());
        } else {
            pathProperties.setMimeType(getMimeType(lfcData.getName()));
        }
        pathProperties.setPath(path + "/" + lfcData.getName());
        return pathProperties;
    }

    /* returns timestamp in seconds from format "Jan 12 2016" */
    private Long getTimeStampFromGridaFormatDate(String gridaFormatDate) {
        if (gridaFormatDate == null || gridaFormatDate.isEmpty()) return null;
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.US);
        try {
            return dateFormat.parse(gridaFormatDate).getTime() / 1000;
        } catch (ParseException e) {
            logger.warn("Error with grida date format : {}. Ignoring it", gridaFormatDate, e);
            return null;
        }
    }

    private String getMimeType(String path) {
        try {
            String contentType = Files.probeContentType(Paths.get(path));
            return contentType == null ? server.getCarminApiDefaultMimeType() : contentType;
        } catch (IOException e) {
            logger.warn("Cant detect mime type of {}. Ignoring and returning application/octet-stream",
                    path, e);
            return "application/octet-stream";
        }
    }

    // #### LOWER LEVELS CALLS, all prefixed with "base"

    private boolean baseDoesFileExist(String path) throws VipException {
        return lfcBusiness.exists(currentUserProvider.get(), path);
    }

    private Optional<Data.Type> baseGetPathInfo(String path) throws VipException {
        return lfcBusiness.getPathInfo(currentUserProvider.get(), path);
    }

    private List<Data> baseGetFileData(String path) throws VipException {
        return lfcBusiness.listDir(currentUserProvider.get(), path, true);
    }

    /* return the operation id */
    private String baseDownloadFile(String path) throws VipException {
        return transferPoolBusiness.downloadFile(currentUserProvider.get(), path);
    }

    private String baseUploadFile(String localPath, String lfcPath)
            throws VipException {
        return transferPoolBusiness.uploadFile(
                currentUserProvider.get(), localPath, lfcPath);
    }

    private PoolOperation baseGetPoolOperation(String operationId, User user)
            throws VipException {
        // need to specify the user to avoid accessing apiContext from another thread
        return transferPoolBusiness.getOperationById(operationId, user.getFolder());
    }

    private PoolOperation baseGetDownloadOperation(String operationId) throws VipException {
        return transferPoolBusiness.getDownloadPoolOperation(operationId);
    }

    private Long baseGetFileModificationDate(String path) throws VipException {
        return lfcBusiness.getModificationDate(currentUserProvider.get(), path);
    }

    private void baseDeletePath(String path) throws VipException {
        transferPoolBusiness.delete(currentUserProvider.get(), path);
    }

    private void baseMkdir(String path, String dirName) throws VipException {
        lfcBusiness.createDir(currentUserProvider.get(), path, dirName);
    }
}
