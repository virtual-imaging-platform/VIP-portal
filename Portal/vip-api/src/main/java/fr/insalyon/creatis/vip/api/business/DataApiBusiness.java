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
import fr.insalyon.creatis.vip.api.rest.model.Path;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.*;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data.Type;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
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
    private DataManagerBusiness dataManagerBusiness;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public DataApiBusiness() {
    }

    public boolean doesFileExist(String apiUri) throws ApiException {
        String lfcPath = checkReadPermission(apiUri);
        return lfcPath.equals(ROOT) || baseDoesFileExist(lfcPath);
    }

    public void deletePath(String apiUri) throws ApiException {
        String lfcPath = checkPermission(apiUri, AccessType.DELETE);
        if (!baseDoesFileExist(lfcPath)) {
            logger.error("trying to delete a non-existing file : " + apiUri);
            throw new ApiException("trying to delete a non-existing dile");
        }
        baseDeletePath(lfcPath);
    }

    public Path getFileApiPath(String apiUri) throws ApiException {
        String lfcPath = checkReadPermission(apiUri);
        if (lfcPath.equals(ROOT)) {
            return getRootPath();
        }
        Path path = new Path();
        path.setPlatformURI(apiUri);
        if (!baseDoesFileExist(lfcPath)) {
            path.setExists(false);
            return path;
        }
        path.setExists(true);
        List<Data> fileData = baseGetFileData(lfcPath);
        if (doesLFCDataCorrespondToAFile(fileData)) {
            // this is a file, not a directory
            Data fileInfo = fileData.get(0);
            path.setIsDirectory(false);
            path.setSize(fileInfo.getLength());
            path.setLastModificationDate(
                    getTimeStampFromGridaDateFormat(fileInfo.getModificationDate()));
            path.setMimeType(getMimeType(lfcPath));
        } else {
            // its a directory
            path.setIsDirectory(true);
            path.setSize(Long.valueOf(fileData.size()));
            path.setLastModificationDate(baseGetFileModificationDate(lfcPath) / 1000);
            path.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        }
        return path;
    }

    public List<Path> listDirectory(String apiUri) throws ApiException {
        String lfcPath = checkReadPermission(apiUri);
        if (lfcPath.equals(ROOT)) {
            return getRootDirectories();
        }
        List<Data> directoryData = baseGetFileData(lfcPath);
        if (directoryData.size() == 1 && directoryData.get(0).getName().startsWith("/")) {
            logger.error("Trying to list a directory, but is a file :" + apiUri);
            throw new ApiException("Error listing a directory");
        }
        List<Path> res = new ArrayList<>();
        for (Data fileData : directoryData) {
            res.add(buildPathFromLfcData(apiUri, fileData));
        }
        return res;
    }

    public String getFileContent(String apiUri) throws ApiException {
        String lfcPath = checkReadPermission(apiUri);
        if (lfcPath.equals(ROOT)) {
            logger.error("cannot download root");
            throw new ApiException("Illegal data API access");
        }
        List<Data> fileData = baseGetFileData(lfcPath);
        if (!doesLFCDataCorrespondToAFile(fileData)) {
            // it works on a directory and return a zip, but we cant check the download size
            logger.error("Trying to download a directory : " + lfcPath);
            throw new ApiException("Illegal data API access");
        }
        Long maxSize = env.getProperty(CarminProperties.API_DATA_TRANSFERT_MAX_SIZE, Long.class);
        if (fileData.get(0).getLength() > maxSize) {
            logger.error("Trying to download a file too big : " + lfcPath);
            throw new ApiException("Illegal data API access");
        }
        String downloadOperationId = baseDownloadFile(lfcPath);
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
            logger.error("Waiting for download interrupted :" + apiUri, e);
            throw new ApiException("Waiting for download interrupted");
        } catch (ExecutionException e) {
            logger.error("Error waiting for download :" + apiUri, e);
            throw new ApiException("Error waiting for download");
        } catch (TimeoutException e) {
            logger.error("Aborting download too long :" + apiUri, e);
            throw new ApiException("Aborting dowload : too long");
        }
        return getDownloadContent(downloadOperationId);
    }

    public Path uploadData(UploadData uploadData) throws ApiException {
        // TODO : check upload size ?
        String lfcPath = checkPermission(uploadData.getUri(), AccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();
        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of upload does not exist :" + lfcPath);
            throw new ApiException("Upload Directory doest not exist");
        }
        // TODO : support archive upload
        String uploadDirectory = DataManagerUtil.getUploadRootDirectory(false);
        logger.debug("LFC path of new upload :" + lfcPath);
        // TODO : normalize file name to avoid weird characters
        String fileName = Paths.get(lfcPath).getFileName().toString();
        String localPath = uploadDirectory + fileName;
        logger.debug("storing upload file in :" + localPath);
        // transform base64 string to a new file
        writeFileFromBase64(uploadData.getPathContent(), localPath);
        // transfer it
        String operationId = baseUploadFile(localPath, parentLfcPath);
        logger.info("File upload asked. Operation id : " + operationId);
        // get new path
        Path newPath = new Path();
        newPath.setPlatformURI(uploadData.getUri());
        newPath.setIsDirectory(false);
        newPath.setMimeType(getMimeType(lfcPath));
        newPath.setExists(false); // not created yet because asynchronous
        return newPath;
    }

    public Path mkdir(String apiUri) throws ApiException {
        String lfcPath = checkPermission(apiUri, AccessType.UPLOAD);
        java.nio.file.Path javaPath = Paths.get(lfcPath);
        String parentLfcPath = javaPath.getParent().toString();

        // check if parent dir exists
        if (!baseDoesFileExist(parentLfcPath)) {
            logger.error("parent directory of upload does not exist :" + lfcPath);
            throw new ApiException("Upload Directory doest not exist");
        }
        if (baseDoesFileExist(lfcPath)) {
            logger.error("Trying do create an existing directory :" + lfcPath);
            throw new ApiException("Upload error");
        }
        baseMkdir(parentLfcPath, javaPath.getFileName().toString());
        Path newPath = new Path();
        newPath.setPlatformURI(apiUri);
        newPath.setIsDirectory(true);
        newPath.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        newPath.setSize(0L);
        newPath.setExists(true);
        return newPath;
    }

    // #### PERMISSION STUFF

    private enum AccessType {READ, UPLOAD, DELETE}

    private String checkReadPermission(String apiUri) throws ApiException {
        return checkPermission(apiUri, AccessType.READ);
    }

    private String checkPermission(String apiUri, AccessType accessType) throws ApiException {
        String lfcPath = getLFCPathFromApiUri(apiUri);
        checkRootPermission(lfcPath, accessType);
        // Root is always filtered so always permited
        if (lfcPath.equals(ROOT)) return lfcPath;
        // else it all depends of the first directory
        String firstDir = getFirstDirectoryName(lfcPath);
        // always can access its home and its trash
        if (firstDir.equals(USERS_HOME)) return lfcPath;
        if (firstDir.equals(TRASH_HOME)) return lfcPath;
        // currently no admin access is possible via the api for security reasons
        if (firstDir.equals(USERS_FOLDER) || firstDir.equals(BIOMED_HOME)) {
            logger.error("Trying to access admin folders from api");
            throw new ApiException("Unauthorized API access");
        }
        // else it should be a group folder
        if (!firstDir.endsWith(GROUP_APPEND)) {
            logger.error("Unexpected api access to: " + firstDir);
            throw new ApiException("Unexpected API access");
        }
        String groupname = firstDir.substring(0,firstDir.length()-GROUP_APPEND.length());
        checkGroupPermission(groupname, accessType);
        if (accessType == AccessType.DELETE) {
            checkAdditionalDeletePermission(lfcPath);
        }
        // all check passed : all good !
        return lfcPath;
    }

    private void checkGroupPermission(String groupname, AccessType accessType) throws ApiException {
        User user = apiContext.getUser();
        // beginner cant write/delete in groups folder
        if (accessType != AccessType.READ && user.getLevel() == UserLevel.Beginner) {
            logger.error("beginner user try to upload/delete in a group:" + user.getEmail() +"/" + groupname);
            throw new ApiException("Unauthorized data API access");
        }
        // otherwise it must have access to this group
        if (!apiContext.getUser().hasGroupAccess(groupname)) {
            logger.error("Trying to access an unauthorized goup");
            throw new ApiException("Unauthorized API access");
        }
    }

    private void checkRootPermission(String lfcPath, AccessType accessType) throws ApiException {
        // verify path begins with the root
        if (!lfcPath.startsWith(ROOT)) {
            logger.error("Access to a lfc not starting with the root:" + lfcPath);
            throw new ApiException("Illegal data API access");
        }
        // read always possible
        if (accessType == AccessType.READ) return;
        // else it cannot be THE root nor a direct subdirectory of root
        if (lfcPath.equals(ROOT) ||
                lfcPath.lastIndexOf('/') == ROOT.length()) {
            logger.error("Illegal upload/delete data API access");
            throw new ApiException("Illegal data API access");
        }
    }

    private void checkAdditionalDeletePermission(String lfcPath) throws ApiException {
        checkSynchronizedDirectories(lfcPath);
        if(lfcPath.endsWith("Dropbox")){
            logger.error("Trying to delete a dropbox directory :" + lfcPath);
            throw new ApiException("Illegal data API access");
        }
    }

    private void checkSynchronizedDirectories(String lfcPath) throws ApiException {
        List<SSH> sshs;
        try {
            sshs = dataManagerBusiness.getSSHConnections();
        } catch (BusinessException e) {
            logger.error("Error listing synchronized directories");
            throw new ApiException("Error listing synchronized directories");
        }
        List<String> lfcDirSSHSynchronization = new ArrayList<String>();
        for (SSH ssh : sshs) {
            if (ssh.getTransferType().equals(TransferType.Synchronization)) {
                lfcDirSSHSynchronization.add(ssh.getLfcDir());
            }
        }

        String lfcBaseDir;
        try {
            lfcBaseDir = DataManagerUtil.parseBaseDir(apiContext.getUser(), lfcPath);
        } catch (DataManagerException e) {
            logger.error("Error parsing api path :" + lfcPath);
            throw new ApiException("Internal error in data API");
        }
        for (String s : lfcDirSSHSynchronization) {
            if (lfcBaseDir.startsWith(s)) {
                logger.error("Try to delete  synchronized file :" + lfcPath);
                throw new ApiException("Illegal data API access");
            }
        }
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

    // #### DOWNLOAD STUFF

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

    private String getDownloadContent(String operationId) throws ApiException {
        PoolOperation operation = baseGetPoolOperation(operationId);

        File file = new File(operation.getDest());
        if (file.isDirectory()) {
            file = new File(operation.getDest() + "/"
                    + FilenameUtils.getName(operation.getSource()));
        }
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

    private Path getRootPath() {
        Path rootPath = new Path();
        rootPath.setExists(true);
        rootPath.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        rootPath.setIsDirectory(true);
        rootPath.setSize(Long.valueOf(getRootDirectoriesName().size()));
        rootPath.setPlatformURI(env.getProperty(CarminProperties.API_URI_PREFIX) + ROOT);
        return rootPath;
    }

    private List<Path> getRootDirectories() {
        List<Path> directories = new ArrayList<>();
        for (String dirName : getRootDirectoriesName()) {
            directories.add(getRootDirPath(dirName));
        }
        return directories;
    }

    private Path getRootDirPath(String name) {
        Path rootPath = new Path();
        rootPath.setExists(true);
        rootPath.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        rootPath.setIsDirectory(true);
        rootPath.setPlatformURI(
                env.getProperty(CarminProperties.API_URI_PREFIX)
                    + ROOT + "/" + name);
        return rootPath;
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

    // #### DATA UTILS

    private boolean doesLFCDataCorrespondToAFile(List<Data> lfcData) {
        return lfcData.size() == 1 && lfcData.get(0).getName().startsWith("/");
    }

    private String getLFCPathFromApiUri(String apiUri) throws ApiException {
        String uriPrefix = env.getRequiredProperty(CarminProperties.API_URI_PREFIX);
        if (!apiUri.startsWith(uriPrefix)) {
            logger.error("Wrong uri prefix. Should start with :" + uriPrefix);
            throw new ApiException("Wrong URI scheme");
        }
        String notNormalizedPath = apiUri.substring(uriPrefix.length());
        // needed to remove ".." and potential security leeks
        return Paths.get(notNormalizedPath).normalize().toString();
    }

    private Path buildPathFromLfcData(String rootApiUri, Data lfcData) {
        Path path = new Path();
        path.setExists(true);
        path.setSize(lfcData.getLength());
        if (lfcData.getModificationDate() != null) {
            path.setLastModificationDate(
                    getTimeStampFromGridaDateFormat(lfcData.getModificationDate()));
        }
        boolean isDirectory = lfcData.getType().equals(Type.folder)
                || lfcData.getType().equals(Type.folderSync);
        path.setIsDirectory(isDirectory);
        if (isDirectory) {
            path.setMimeType(env.getProperty(CarminProperties.API_DIRECTORY_MIME_TYPE));
        } else {
            path.setMimeType(getMimeType(lfcData.getName()));
        }
        path.setPlatformURI(rootApiUri + "/" + lfcData.getName());
        return path;
    }

    /* returns timestamp in seconds from format "Jan 12 2016" */
    private Long getTimeStampFromGridaDateFormat(String gridaDateFormat) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.US);
        try {
            return dateFormat.parse(gridaDateFormat).getTime() / 1000;
        } catch (ParseException e) {
            logger.warn("Error with grida date format :" + gridaDateFormat);
            logger.warn("Ignoring it");
            return null;
        }
    }

    private String getMimeType(String lfcPath) {
        try {
            String contentType = Files.probeContentType(Paths.get(lfcPath));
            return contentType == null ?
                    env.getProperty(CarminProperties.API_DEFAULT_MIME_TYPE) :
                    contentType;
        } catch (IOException e) {
            logger.warn("Cant detect mime type of " + lfcPath);
            logger.warn("Ignoring and returning application/octet-stream");
            return "application/octet-stream";
        }
    }

    // #### LOWER LEVELS CALLS, all prefixed with "base"

    private boolean baseDoesFileExist(String lfcPath) throws ApiException {
        try {
            return lfcBusiness.exists(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error testing lfc file existence", e);
            throw new ApiException("Error testing file existence");
        }
    }

    private List<Data> baseGetFileData(String lfcPath) throws ApiException {
        try {
            return lfcBusiness.listDir(apiContext.getUser(), lfcPath, true);
        } catch (BusinessException e) {
            logger.error("Error getting lfc file information", e);
            throw new ApiException("Error getting lfc information");
        }
    }

    /* return the operation id */
    private String baseDownloadFile(String lfcPath) throws ApiException {
        try {
            return transferPoolBusiness.downloadFile(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error downloading lfc file :" + lfcPath);
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

    private Long baseGetFileModificationDate(String lfcPath) throws ApiException {
        try {
            return lfcBusiness.getModificationDate(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error getting lfc file modification date", e);
            throw new ApiException("Error getting lfc modification");
        }
    }

    private void baseDeletePath(String lfcPath) throws ApiException {
        try {
            transferPoolBusiness.delete(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error deleting lfc file", e);
            throw new ApiException("Error deleting lfc file");
        }
    }

    private void baseMkdir(String lfcPath, String dirName) throws ApiException {
        try {
            lfcBusiness.createDir(apiContext.getUser(), lfcPath, dirName);
        } catch (BusinessException e) {
            logger.error("Error creating directory :" + lfcPath, e);
            throw new ApiException("Error creating LFC directory");
        }
    }

}
