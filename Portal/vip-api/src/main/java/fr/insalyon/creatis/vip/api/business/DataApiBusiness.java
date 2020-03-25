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

import fr.insalyon.creatis.vip.api.CarminProperties;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.*;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data.Type;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness.LFCAccessType;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Connection;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

import static fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants.*;


/**
 * Created by abonnet on 1/18/17.
 *
 * // TODO : should we move files to the Trash Folder before deleting ?
 */
@Service
public class DataApiBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;
    @Autowired
    private ApiContext apiContext;

    @Autowired
    private LFCBusiness lfcBusiness;
    @Autowired
    private TransferPoolBusiness transferPoolBusiness;
    @Autowired
    private LFCPermissionBusiness lfcPermissionBusiness;

    // 2 threads are needed for every download
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2 * 5);

    public DataApiBusiness() {
    }

    public boolean doesFileExist(String path, Connection connection)
        throws ApiException {
        checkReadPermission(path, connection);
        return path.equals(ROOT) || baseDoesFileExist(path, connection);
    }

    public void deletePath(String path, Connection connection)
        throws ApiException {
        checkPermission(path, LFCAccessType.DELETE, connection);
        if (!baseDoesFileExist(path, connection)) {
            logger.error("trying to delete a non-existing file : " + path);
            throw new ApiException("trying to delete a non-existing dile");
        }
        baseDeletePath(path, connection);
    }

    public PathProperties getPathProperties(String path, Connection connection)
        throws ApiException {
        checkReadPermission(path, connection);
        if (path.equals(ROOT)) {
            return getRootPathProperties();
        }
        PathProperties pathProperties = new PathProperties();
        pathProperties.setPath(path);
        if (!baseDoesFileExist(path, connection)) {
            pathProperties.setExists(false);
            return pathProperties;
        }
        pathProperties.setExists(true);
        List<Data> fileData = baseGetFileData(path, connection);
        if (doesPathCorrespondsToAFile(path, fileData)) {
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
            pathProperties.setSize(Long.valueOf(fileData.size()));
            pathProperties.setLastModificationDate(
                baseGetFileModificationDate(path, connection) / 1000);
            pathProperties.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        }
        return pathProperties;
    }

    public List<PathProperties> listDirectory(String path, Connection connection)
        throws ApiException {
        checkReadPermission(path, connection);
        if (path.equals(ROOT)) {
            return getRootSubDirectoriesPathProps();
        }
        List<Data> directoryData = baseGetFileData(path, connection);
        if (doesPathCorrespondsToAFile(path, directoryData)) {
            logger.error("Trying to list a directory, but is a file :" + path);
            throw new ApiException("Error listing a directory");
        }
        List<PathProperties> res = new ArrayList<>();
        for (Data fileData : directoryData) {
            res.add(buildPathFromLfcData(path, fileData));
        }
        return res;
    }

    public File getFile(String path, Connection connection) throws ApiException {
        checkDownloadPermission(path, connection);
        String downloadOperationId =
            downloadFileToLocalStorage(path, connection);
        return getDownloadFile(downloadOperationId);
    }

    public void uploadRawFileFromInputStream(
        String lfcPath, InputStream is, Connection connection)
        throws ApiException {
        // TODO : check upload size ?
        checkPermission(lfcPath, LFCAccessType.UPLOAD, connection);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath, connection)) {
            logger.error("parent directory of upload does not exist :" + lfcPath);
            throw new ApiException("Upload Directory doest not exist");
        }
        // TODO : check if it already exists
        // TODO : support archive upload
        String uploadDirectory = DataManagerUtil.getUploadRootDirectory(false);
        // TODO : normalize file name to avoid weird characters
        String fileName = Paths.get(lfcPath).getFileName().toString();
        String localPath = uploadDirectory + fileName;
        logger.debug("storing upload file in :" + localPath);
        boolean isFileEmpty = saveInputStreamToFile(is, localPath);
        if (isFileEmpty) {
            logger.info("no content in upload, creating dir : " + lfcPath);
            baseMkdir(parentLfcPath, fileName, connection);
        } else {
            String opId = baseUploadFile(localPath, parentLfcPath, connection);
            // wait for it to be over
            waitForOperationOrTimeout(opId, connection);
        }
    }

    public void uploadCustomData(
        String lfcPath, UploadData uploadData, Connection connection)
        throws ApiException {
        // TODO : check upload size ?
        // TODO : factorize with previous method
        checkPermission(lfcPath, LFCAccessType.UPLOAD, connection);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath, connection)) {
            logger.error("parent directory of upload does not exist :" + lfcPath);
            throw new ApiException("Upload Directory doest not exist");
        }
        if (uploadData.getType().equals(UploadDataType.ARCHIVE)) {
            logger.error("archive upload not supported yet");
            throw new ApiException("archive upload not supported yet");
        }
        // TODO : check if it already exists
        // TODO : support archive upload
        String uploadDirectory = DataManagerUtil.getUploadRootDirectory(false);
        // TODO : normalize file name to avoid weird characters
        String fileName = Paths.get(lfcPath).getFileName().toString();
        String localPath = uploadDirectory + fileName;
        logger.debug("storing upload file in :" + localPath);
        writeFileFromBase64(uploadData.getBase64Content(), localPath);
        String opId = baseUploadFile(localPath, parentLfcPath, connection);
        // wait for it to be over
        waitForOperationOrTimeout(opId, connection);
    }

    public PathProperties mkdir(String path, Connection connection)
        throws ApiException {
        checkPermission(path, LFCAccessType.UPLOAD, connection);
        java.nio.file.Path javaPath = Paths.get(path);
        String parentLfcPath = javaPath.getParent().toString();

        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath, connection)) {
            logger.error("parent directory of upload does not exist :" + path);
            throw new ApiException("Upload Directory doest not exist");
        }
        if (baseDoesFileExist(path, connection)) {
            logger.error("Trying do create an existing directory :" + path);
            throw new ApiException("Upload error");
        }
        baseMkdir(parentLfcPath, javaPath.getFileName().toString(), connection);
        PathProperties newPathProperties = new PathProperties();
        newPathProperties.setPath(path);
        newPathProperties.setIsDirectory(true);
        newPathProperties.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        newPathProperties.setSize(0L);
        newPathProperties.setExists(true);
        return newPathProperties;
    }

    // #### PERMISSION STUFF

    private String checkReadPermission(String path, Connection connection)
        throws ApiException {
        return checkPermission(path, LFCAccessType.READ, connection);
    }

    private void checkDownloadPermission(String path, Connection connection)
        throws ApiException {
        checkReadPermission(path, connection);
        if (path.equals(ROOT)) {
            logger.error("cannot download root");
            throw new ApiException("Illegal data API access");
        }
        List<Data> fileData = baseGetFileData(path, connection);
        if (!doesPathCorrespondsToAFile(path, fileData)) {
            // it works on a directory and return a zip, but we cant check the download size
            logger.error("Trying to download a directory : " + path);
            throw new ApiException("Illegal data API access");
        }
        Long maxSize = env.getProperty(CarminProperties.API_DATA_TRANSFERT_MAX_SIZE, Long.class);
        if (fileData.get(0).getLength() > maxSize) {
            logger.error("Trying to download a file too big : " + path);
            throw new ApiException("Illegal data API access");
        }
    }

    private String checkPermission(
        String path, LFCAccessType accessType, Connection connection)
        throws ApiException {
        try {
            lfcPermissionBusiness.checkPermission(
                apiContext.getUser(), path, accessType, connection);
        } catch (BusinessException e) {
            throw new ApiException("API Permission error", e);
        }
        // all check passed : all good !
        return path;
    }

    // #### DOWNLOAD STUFF

    private String downloadFileToLocalStorage(
        String path, Connection connection) throws ApiException {
        String downloadOperationId = baseDownloadFile(path, connection);
        waitForOperationOrTimeout(downloadOperationId, connection);
        return downloadOperationId;
    }

    private String getDownloadContentInBase64(String operationId) throws ApiException {
        File file = getDownloadFile(operationId);
        Base64.Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStream outputStream = encoder.wrap(baos)) {
            Files.copy(file.toPath(), outputStream);
        } catch (IOException e) {
            logger.error("Error encoding download file for operation : {}",
                    operationId, e);
            throw new ApiException("Download operation failed", e);
        }
        return baos.toString();
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

    private void waitForOperationOrTimeout(
        String operationId, Connection connection) throws ApiException {
        // get user before launching thread : apiContext is not available from threads
        User user = apiContext.getUser();
        Callable<Boolean> isDownloadOverCall =
            () -> isOperationOver(operationId, user, connection);

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
        return env.getProperty(CarminProperties.API_DOWNLOAD_RETRY_IN_SECONDS, Integer.class);
    }

    private Integer getTimeout() {
        return env.getProperty(CarminProperties.API_DOWNLOAD_TIMEOUT_IN_SECONDS, Integer.class);
    }

    private boolean isOperationOver(
        String operationId, User user, Connection connection)
        throws ApiException {
        PoolOperation operation = baseGetPoolOperation(
            operationId, user, connection);

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
        InputStream inputStream = new ReaderInputStream(stringReader, StandardCharsets.UTF_8);
        try (InputStream base64InputStream = decoder.wrap(inputStream)) {
            Files.copy(base64InputStream, Paths.get(localFilePath));
        } catch (IOException e) {
            logger.error("Error writing base64 file in {}", localFilePath, e);
            throw new ApiException("Error writing base64 file", e);
        }
    }

    private boolean saveInputStreamToFile(InputStream is, String path) throws ApiException {
        try (FileOutputStream fos = new FileOutputStream(path);) {
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
        rootPathProperties.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        rootPathProperties.setIsDirectory(true);
        rootPathProperties.setSize(Long.valueOf(getRootDirectoriesName().size()));
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
        for (Group group : apiContext.getUser().getGroups()) {
            rootDir.add(group.getName() + GROUP_APPEND);
        }
        return rootDir;
    }

    private PathProperties getRootSubDirPathProperties(String name) {
        PathProperties rootPathProperties = new PathProperties();
        rootPathProperties.setExists(true);
        rootPathProperties.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        rootPathProperties.setIsDirectory(true);
        // TODO : size ?
        rootPathProperties.setPath(ROOT + "/" + name);
        return rootPathProperties;
    }

    // #### DATA UTILS

    private boolean doesPathCorrespondsToAFile(String path, List<Data> pathDataList) {
        // Currently, there is no perfect way to determine that
        // TODO : add a isDirectory method in grida
        if (pathDataList.size() != 1) {
            return false;
        }
        String fileName = Paths.get(path).getFileName().toString();
        return fileName.equals(pathDataList.get(0).getName());
    }

    private PathProperties buildPathFromLfcData(String path, Data lfcData) {
        PathProperties pathProperties = new PathProperties();
        pathProperties.setExists(true);
        pathProperties.setSize(lfcData.getLength());
        pathProperties.setLastModificationDate(
                getTimeStampFromGridaFormatDate(lfcData.getModificationDate()));
        boolean isDirectory = lfcData.getType().equals(Type.folder)
                || lfcData.getType().equals(Type.folderSync);
        pathProperties.setIsDirectory(isDirectory);
        if (isDirectory) {
            pathProperties.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
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
                    env.getProperty(CarminProperties.API_DEFAULT_MIME_TYPE) :
                    contentType;
        } catch (IOException e) {
            logger.warn("Cant detect mime type of {}. Ignoring and returning application/octet-stream",
                    path, e);
            return "application/octet-stream";
        }
    }

    // #### LOWER LEVELS CALLS, all prefixed with "base"

    private boolean baseDoesFileExist(String path, Connection connection)
        throws ApiException {
        try {
            return lfcBusiness.exists(apiContext.getUser(), path, connection);
        } catch (BusinessException e) {
            throw new ApiException("Error testing file existence", e);
        }
    }

    private List<Data> baseGetFileData(String path, Connection connection)
        throws ApiException {
        try {
            return lfcBusiness.listDir(
                apiContext.getUser(), path, true, connection);
        } catch (BusinessException e) {
            throw new ApiException("Error getting lfc information", e);
        }
    }

    /* return the operation id */
    private String baseDownloadFile(String path, Connection connection)
        throws ApiException {
        try {
            return transferPoolBusiness.downloadFile(
                apiContext.getUser(), path, connection);
        } catch (BusinessException e) {
            throw new ApiException("Error download LFC file", e);
        }
    }

    private String baseUploadFile(
        String localPath, String lfcPath, Connection connection)
        throws ApiException {
        try {
            return transferPoolBusiness.uploadFile(
                apiContext.getUser(), localPath, lfcPath, connection);
        } catch (BusinessException e) {
            throw new ApiException("Error uploading a lfc file", e);
        }
    }

    private PoolOperation baseGetPoolOperation(
        String operationId, User user, Connection connection)
        throws ApiException {
        // need to specify the user to avoid accessing apiContext from another thread
        try {
            return transferPoolBusiness.getOperationById(
                operationId, user.getFolder(), connection);
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

    private Long baseGetFileModificationDate(String path, Connection connection)
        throws ApiException {
        try {
            return lfcBusiness.getModificationDate(
                apiContext.getUser(), path, connection);
        } catch (BusinessException e) {
            throw new ApiException("Error getting lfc modification", e);
        }
    }

    private void baseDeletePath(String path, Connection connection)
        throws ApiException {
        try {
            transferPoolBusiness.delete(apiContext.getUser(), connection, path);
        } catch (BusinessException e) {
            throw new ApiException("Error deleting lfc file", e);
        }
    }

    private void baseMkdir(String path, String dirName, Connection connection)
        throws ApiException {
        try {
            lfcBusiness.createDir(
                apiContext.getUser(), path, dirName, connection);
        } catch (BusinessException e) {
            throw new ApiException("Error creating LFC directory", e);
        }
    }

}
