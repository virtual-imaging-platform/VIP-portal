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
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.core.server.CarminProperties;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.model.PathProperties;
import fr.insalyon.creatis.vip.api.model.UploadData;
import fr.insalyon.creatis.vip.api.model.UploadDataType;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness.LFCAccessType;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.io.*;
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
import java.util.concurrent.*;
import java.util.function.Supplier;

import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.*;


/**
 * Created by abonnet on 1/18/17.
 *
 */
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

    public boolean doesFileExist(String path) throws ApiException {
        checkReadPermission(path);
        return path.equals(ROOT) || baseDoesFileExist(path);
    }

    public void deletePath(String path) throws ApiException {
        checkPermission(path, LFCAccessType.DELETE);
        if (!baseDoesFileExist(path)) {
            logger.error("trying to delete a non-existing file : {}", path);
            throw new ApiException("trying to delete a non-existing dile");
        }
        baseDeletePath(path);
    }

    public PathProperties getPathProperties(String path ) throws ApiException {
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
            pathProperties.setMimeType(server.getEnvProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        }
        return pathProperties;
    }

    public List<PathProperties> listDirectory(String path) throws ApiException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            return getRootSubDirectoriesPathProps();
        }
        Optional<Data.Type> type = baseGetPathInfo(path);
        if (!type.isPresent()) { // path doesn't exist
            logger.error("Trying to list a non-existing path ({})", path);
            throw new ApiException("Error listing a directory");
        }
        if (!type.get().equals(Data.Type.folder)) {
            logger.error("Trying to list {} , but is a file :", path);
            throw new ApiException("Error listing a directory");
        }
        List<Data> directoryData = baseGetFileData(path);
        List<PathProperties> res = new ArrayList<>();
        for (Data fileData : directoryData) {
            res.add(buildPathFromLfcData(path, fileData));
        }
        return res;
    }

    public File getFile(String path) throws ApiException {
        checkDownloadPermission(path);
        String downloadOperationId =
            downloadFileToLocalStorage(path);
        return getDownloadFile(downloadOperationId);
    }

    public void uploadRawFileFromInputStream(String lfcPath, InputStream is)
            throws ApiException {
        // TODO : check upload size ?
        checkPermission(lfcPath, LFCAccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of upload {} does not exist :", lfcPath);
            throw new ApiException("Upload Directory doest not exist");
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
            throws ApiException {
        // TODO : check upload size ?
        // TODO : factorize with previous method
        checkPermission(lfcPath, LFCAccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of {} does not exist :", lfcPath);
            throw new ApiException("Upload Directory doest not exist");
        }
        if (uploadData.getType().equals(UploadDataType.ARCHIVE)) {
            logger.error("archive upload not supported yet for ({})", lfcPath);
            throw new ApiException("archive upload not supported yet");
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

    private void checkReadPermission(String path) throws ApiException {
        checkPermission(path, LFCAccessType.READ);
    }

    private void checkDownloadPermission(String path) throws ApiException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            logger.error("cannot download root ({})", path);
            throw new ApiException("Illegal data API access");
        }
        Optional<Data.Type> type = baseGetPathInfo(path);
        if (!type.isPresent()) { // path doesn't exist
            logger.error("Trying to download a non-existing file ({})", path);
            throw new ApiException("Illegal data API access");
        }
        if (!type.get().equals(Data.Type.file)) {
            // it works on a directory and return a zip, but we cant check the download size
            logger.error("Trying to download a directory ({})", path);
            throw new ApiException("Illegal data API access");
        }
        // path exists and is a file: check its size
        List<Data> fileData = baseGetFileData(path);
        Long maxSize = server.getEnvProperty(CarminProperties.API_DATA_TRANSFERT_MAX_SIZE, Long.class);
        if (fileData.get(0).getLength() > maxSize) {
            logger.error("Trying to download a file too big ({})", path);
            throw new ApiException("Illegal data API access");
        }
    }

    private void checkPermission(String path, LFCAccessType accessType)
            throws ApiException {
        try {
            if ( ! lfcPermissionBusiness.isLFCPathAllowed(
                currentUserProvider.get(), path, accessType, true)) {
                throw new ApiException(ApiError.UNAUTHORIZED_DATA_ACCESS, path);
            }
        } catch (BusinessException e) {
            throw new ApiException("Error when checking permissions", e);
        }
    }

    // #### DOWNLOAD STUFF

    private String downloadFileToLocalStorage(String path) throws ApiException {
        String downloadOperationId = baseDownloadFile(path);
        waitForOperationOrTimeout(downloadOperationId);
        return downloadOperationId;
    }

    private File getDownloadFile(String operationId) throws ApiException {
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
            throws ApiException {
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
            Future<Boolean> completionFuture, int timeoutInSeconds) throws ApiException {
        try {
            completionFuture.get(timeoutInSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Waiting for operation completion interrupted : {}", operationId ,e);
            throw new ApiException("Waiting for operation completion interrupted", e);
        } catch (ExecutionException e) {
            logger.error("Error waiting for operation completion : {}", operationId ,e);
            throw new ApiException("Error waiting for operation completion", e);
        } catch (TimeoutException e) {
            completionFuture.cancel(true);
            logger.error("Timeout operation completion : {}", operationId, e);
            throw new ApiException("Aborting operation : too long", e);
        }
    }

    private Integer getRetryDelay() {
        return server.getEnvProperty(CarminProperties.API_DOWNLOAD_RETRY_IN_SECONDS, Integer.class);
    }

    private Integer getTimeout() {
        return server.getEnvProperty(CarminProperties.API_DOWNLOAD_TIMEOUT_IN_SECONDS, Integer.class);
    }

    private boolean isOperationOver(String operationId, User user)
            throws ApiException {
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
                throw new ApiException("IO LFC Operation operation failed");
        }
    }

    // #### UPLOAD STUFF

    private void writeFileFromBase64(String base64Content, String localFilePath) throws ApiException {
        Base64.Decoder decoder = Base64.getDecoder();
        StringReader stringReader = new StringReader(base64Content);
        try {
            InputStream inputStream = ReaderInputStream.builder()
                    .setReader(new StringReader(base64Content))
                    .setCharset(StandardCharsets.UTF_8)
                    .get();
            InputStream base64InputStream = decoder.wrap(inputStream);
            Files.copy(base64InputStream, Paths.get(localFilePath));
        } catch (IOException e) {
            logger.error("Error writing base64 file in {}", localFilePath, e);
            throw new ApiException("Error writing base64 file", e);
        }
    }

    private boolean saveInputStreamToFile(InputStream is, String path) throws ApiException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            boolean isFileEmpty = true;
            while ((bytesRead = is.read(buffer)) != -1) {
                isFileEmpty = false;
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
            return isFileEmpty;
        } catch (FileNotFoundException e) {
            logger.error("Error creating new file {}", path ,e);
            throw new ApiException("Upload error", e);
        } catch (IOException e) {
            logger.error("IO Error storing file {}", path, e);
            throw new ApiException("Upload error", e);
        }
    }

    // #### ROOT folder STUFF

    private PathProperties getRootPathProperties() {
        PathProperties rootPathProperties = new PathProperties();
        rootPathProperties.setExists(true);
        rootPathProperties.setMimeType(server.getEnvProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
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
        rootPathProperties.setMimeType(server.getEnvProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
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
            pathProperties.setMimeType(server.getEnvProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
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
            return contentType == null ?
                    server.getEnvProperty(CarminProperties.API_DEFAULT_MIME_TYPE) :
                    contentType;
        } catch (IOException e) {
            logger.warn("Cant detect mime type of {}. Ignoring and returning application/octet-stream",
                    path, e);
            return "application/octet-stream";
        }
    }

    // #### LOWER LEVELS CALLS, all prefixed with "base"

    private boolean baseDoesFileExist(String path) throws ApiException {
        try {
            return lfcBusiness.exists(currentUserProvider.get(), path);
        } catch (BusinessException e) {
            throw new ApiException("Error testing file existence", e);
        }
    }

    private Optional<Data.Type> baseGetPathInfo(String path) throws ApiException {
        try {
            return lfcBusiness.getPathInfo(currentUserProvider.get(), path);
        } catch (BusinessException e) {
            throw new ApiException("Error getting path info", e);
        }
    }

    private List<Data> baseGetFileData(String path) throws ApiException {
        try {
            return lfcBusiness.listDir(
                currentUserProvider.get(), path, true);
        } catch (BusinessException e) {
            throw new ApiException("Error getting lfc information", e);
        }
    }

    /* return the operation id */
    private String baseDownloadFile(String path) throws ApiException {
        try {
            return transferPoolBusiness.downloadFile(
                currentUserProvider.get(), path);
        } catch (BusinessException e) {
            throw new ApiException("Error download LFC file", e);
        }
    }

    private String baseUploadFile(String localPath, String lfcPath)
            throws ApiException {
        try {
            return transferPoolBusiness.uploadFile(
                currentUserProvider.get(), localPath, lfcPath);
        } catch (BusinessException e) {
            throw new ApiException("Error uploading a lfc file", e);
        }
    }

    private PoolOperation baseGetPoolOperation(String operationId, User user)
            throws ApiException {
        // need to specify the user to avoid accessing apiContext from another thread
        try {
            return transferPoolBusiness.getOperationById(
                    operationId, user.getFolder());
        } catch (BusinessException e) {
            throw new ApiException("Error getting download operation", e);
        }
    }

    private PoolOperation baseGetDownloadOperation(String operationId) throws ApiException {
        try {
            return transferPoolBusiness.getDownloadPoolOperation(operationId);
        } catch (BusinessException e) {
            throw new ApiException("Error getting download operation", e);
        }
    }

    private Long baseGetFileModificationDate(String path) throws ApiException {
        try {
            return lfcBusiness.getModificationDate(
                currentUserProvider.get(), path);
        } catch (BusinessException e) {
            throw new ApiException("Error getting lfc modification", e);
        }
    }

    private void baseDeletePath(String path) throws ApiException {
        try {
            transferPoolBusiness.delete(currentUserProvider.get(), path);
        } catch (BusinessException e) {
            throw new ApiException("Error deleting lfc file", e);
        }
    }

    private void baseMkdir(String path, String dirName) throws ApiException {
        try {
            lfcBusiness.createDir(currentUserProvider.get(), path, dirName);
        } catch (BusinessException e) {
            throw new ApiException("Error creating LFC directory", e);
        }
    }

}
