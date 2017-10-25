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
import fr.insalyon.creatis.vip.api.rest.model.PathProperties;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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

    private final static Logger logger = Logger.getLogger(DataApiBusiness.class);

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

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public DataApiBusiness() {
    }

    public boolean doesFileExist(String path) throws ApiException {
        checkReadPermission(path);
        return path.equals(ROOT) || baseDoesFileExist(path);
    }

    public void deletePath(String path) throws ApiException {
        checkPermission(path, LFCAccessType.DELETE);
        if (!baseDoesFileExist(path)) {
            logger.error("trying to delete a non-existing file : " + path);
            throw new ApiException("trying to delete a non-existing dile");
        }
        baseDeletePath(path);
    }

    public PathProperties getPathProperties(String path) throws ApiException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            return getRootPathProperties();
        }
        PathProperties pathProperties = new PathProperties();
        pathProperties.setPath(path);
        if (!baseDoesFileExist(path)) {
            pathProperties.setExists(false);
            return pathProperties;
        }
        pathProperties.setExists(true);
        List<Data> fileData = baseGetFileData(path);
        if (doesLFCDataCorrespondToAFile(fileData)) {
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
            pathProperties.setLastModificationDate(baseGetFileModificationDate(path) / 1000);
            pathProperties.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        }
        return pathProperties;
    }

    public List<PathProperties> listDirectory(String path) throws ApiException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            return getRootSubDirectoriesPathProps();
        }
        List<Data> directoryData = baseGetFileData(path);
        if (doesLFCDataCorrespondToAFile(directoryData)) {
            logger.error("Trying to list a directory, but is a file :" + path);
            throw new ApiException("Error listing a directory");
        }
        List<PathProperties> res = new ArrayList<>();
        for (Data fileData : directoryData) {
            res.add(buildPathFromLfcData(path, fileData));
        }
        return res;
    }

    public String getFileContentInBase64(String path) throws ApiException {
        checkDownloadPermission(path);
        String downloadOperationId = downloadFileToLocalStorage(path);
        return getDownloadContentInBase64(downloadOperationId);
    }

    public File getFile(String path) throws ApiException {
        checkDownloadPermission(path);
        String downloadOperationId = downloadFileToLocalStorage(path);
        return getDownloadFile(downloadOperationId);
    }

    private void checkDownloadPermission(String path) throws ApiException {
        checkReadPermission(path);
        if (path.equals(ROOT)) {
            logger.error("cannot download root");
            throw new ApiException("Illegal data API access");
        }
        List<Data> fileData = baseGetFileData(path);
        if (!doesLFCDataCorrespondToAFile(fileData)) {
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

    public void uploadRawFileFromInputStream(String path, InputStream is) throws ApiException {
        // TODO : check upload size ?
        checkPermission(path, LFCAccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(path);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of upload does not exist :" + path);
            throw new ApiException("Upload Directory doest not exist");
        }
        // TODO : support archive upload
        String uploadDirectory = DataManagerUtil.getUploadRootDirectory(false);
        logger.debug("LFC path of new upload :" + path);
        // TODO : normalize file name to avoid weird characters
        String fileName = Paths.get(path).getFileName().toString();
        String localPath = uploadDirectory + fileName;
        logger.debug("storing upload file in :" + localPath);
        try (FileOutputStream fos = new FileOutputStream(localPath);) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            logger.error("Error creating new file " + localPath);
            throw new ApiException("Upload error");
        } catch (IOException e) {
            logger.error("IO Error storing file " + localPath);
            throw new ApiException("Upload error");
        }
    }

    public PathProperties mkdir(String path) throws ApiException {
        checkPermission(path, LFCAccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(path);
        String parentLfcPath = javaPath.getParent().toString();

        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of upload does not exist :" + path);
            throw new ApiException("Upload Directory doest not exist");
        }
        if (baseDoesFileExist(path)) {
            logger.error("Trying do create an existing directory :" + path);
            throw new ApiException("Upload error");
        }
        baseMkdir(parentLfcPath, javaPath.getFileName().toString());
        PathProperties newPathProperties = new PathProperties();
        newPathProperties.setPath(path);
        newPathProperties.setIsDirectory(true);
        newPathProperties.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        newPathProperties.setSize(0L);
        newPathProperties.setExists(true);
        return newPathProperties;
    }

    // #### PERMISSION STUFF

    private String checkReadPermission(String path) throws ApiException {
        return checkPermission(path, LFCAccessType.READ);
    }

    private String checkPermission(String path, LFCAccessType accessType) throws ApiException {
        try {
            lfcPermissionBusiness.checkPermission(apiContext.getUser(), path, accessType);
        } catch (BusinessException e) {
            logger.error("API Permission error");
            throw new ApiException(e);
        }
        // all check passed : all good !
        return path;
    }

    // #### DOWNLOAD STUFF

    private String downloadFileToLocalStorage(String path) throws ApiException {
        String downloadOperationId = baseDownloadFile(path);
        Callable<Boolean> isDownloadOverCall = () -> isDownloadOver(downloadOperationId);

        Integer retryDelay =
                env.getProperty(CarminProperties.API_DOWNLOAD_RETRY_IN_SECONDS, Integer.class);
        Callable<Boolean> waitForDownloadCall = () -> {
            while (true) {
                Future<Boolean> isDownloadOverFuture =
                        scheduler.schedule(isDownloadOverCall, retryDelay, TimeUnit.SECONDS);
                if (isDownloadOverFuture.get()) {
                    return true;
                }
            }
        };
        Future<Boolean> waitForDownloadFuture =
                scheduler.submit(waitForDownloadCall);
        try {
            // wait for n seconds or abort
            Integer timeout =
                    env.getProperty(CarminProperties.API_DOWNLOAD_TIMEOUT_IN_SECONDS, Integer.class);
            waitForDownloadFuture.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Waiting for download interrupted :" + path, e);
            throw new ApiException("Waiting for download interrupted");
        } catch (ExecutionException e) {
            logger.error("Error waiting for download :" + path, e);
            throw new ApiException("Error waiting for download");
        } catch (TimeoutException e) {
            logger.error("Aborting download too long :" + path, e);
            throw new ApiException("Aborting dowload : too long");
        }
        return downloadOperationId;
    }

    private boolean isDownloadOver(String operationId) throws ApiException {
        PoolOperation operation = baseGetPoolOperation(operationId);

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
                logger.error("Download operation failed : " + operation.getStatus());
                throw new ApiException("Download operation failed");
        }
    }

    private String getDownloadContentInBase64(String operationId) throws ApiException {
        File file = getDownloadFile(operationId);
        Base64.Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStream outputStream = encoder.wrap(baos)) {
            Files.copy(file.toPath(), outputStream);
        } catch (IOException e) {
            logger.error("Error encoding download file for operation :" + operationId , e);
            throw new ApiException("Download operation failed");
        }
        return baos.toString();
    }

    private File getDownloadFile(String operationId) throws ApiException {
        PoolOperation operation = baseGetPoolOperation(operationId);
        File file = new File(operation.getDest());
        if (file.isDirectory()) {
            file = new File(operation.getDest() + "/"
                    + FilenameUtils.getName(operation.getSource()));
        }
        return file;
    }

    // #### UPLOAD STUFF

    private void writeFileFromBase64(String base64Content, String localFilePath) throws ApiException {
        Base64.Decoder decoder = Base64.getDecoder();
        StringReader stringReader = new StringReader(base64Content);
        InputStream inputStream = new ReaderInputStream(stringReader, StandardCharsets.UTF_8);
        try (InputStream base64InputStream = decoder.wrap(inputStream)) {
            Files.copy(base64InputStream, Paths.get(localFilePath));
        } catch (IOException e) {
            e.printStackTrace();
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

    private boolean doesLFCDataCorrespondToAFile(List<Data> lfcData) {
        return lfcData.size() == 1 && lfcData.get(0).getName().startsWith("/");
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
            logger.warn("Error with grida date format :" + gridaFormatDate);
            logger.warn("Ignoring it");
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
            logger.warn("Cant detect mime type of " + path);
            logger.warn("Ignoring and returning application/octet-stream");
            return "application/octet-stream";
        }
    }

    // #### LOWER LEVELS CALLS, all prefixed with "base"

    private boolean baseDoesFileExist(String path) throws ApiException {
        try {
            return lfcBusiness.exists(apiContext.getUser(), path);
        } catch (BusinessException e) {
            logger.error("Error testing lfc file existence", e);
            throw new ApiException("Error testing file existence");
        }
    }

    private List<Data> baseGetFileData(String path) throws ApiException {
        try {
            return lfcBusiness.listDir(apiContext.getUser(), path, true);
        } catch (BusinessException e) {
            logger.error("Error getting lfc file information", e);
            throw new ApiException("Error getting lfc information");
        }
    }

    /* return the operation id */
    private String baseDownloadFile(String path) throws ApiException {
        try {
            return transferPoolBusiness.downloadFile(apiContext.getUser(), path);
        } catch (BusinessException e) {
            logger.error("Error downloading lfc file :" + path);
            throw new ApiException("Error download LFC file");
        }
    }

    private String baseUploadFile(String localPath, String lfcPath) throws ApiException {
        try {
            return transferPoolBusiness.uploadFile(apiContext.getUser(), localPath, lfcPath);
        } catch (BusinessException e) {
            logger.error("Error uploading lfc file : " + lfcPath);
            throw new ApiException("Error uploading a flc fiel");
        }
    }

    private PoolOperation baseGetPoolOperation(String operationId) throws ApiException {
        try {
            return transferPoolBusiness.getDownloadPoolOperation(operationId);
        } catch (BusinessException e) {
            logger.error("Error getting download operation", e);
            throw new ApiException("Error getting download operation");
        }
    }

    private Long baseGetFileModificationDate(String path) throws ApiException {
        try {
            return lfcBusiness.getModificationDate(apiContext.getUser(), path);
        } catch (BusinessException e) {
            logger.error("Error getting lfc file modification date", e);
            throw new ApiException("Error getting lfc modification");
        }
    }

    private void baseDeletePath(String path) throws ApiException {
        try {
            transferPoolBusiness.delete(apiContext.getUser(), path);
        } catch (BusinessException e) {
            logger.error("Error deleting lfc file", e);
            throw new ApiException("Error deleting lfc file");
        }
    }

    private void baseMkdir(String path, String dirName) throws ApiException {
        try {
            lfcBusiness.createDir(apiContext.getUser(), path, dirName);
        } catch (BusinessException e) {
            logger.error("Error creating directory :" + path, e);
            throw new ApiException("Error creating LFC directory");
        }
    }

}
