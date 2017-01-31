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

import fr.insalyon.creatis.grida.client.*;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.vip.api.CarminProperties;
import fr.insalyon.creatis.vip.api.rest.model.Path;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.*;
import fr.insalyon.creatis.vip.datamanager.client.bean.*;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data.Type;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation.Status;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;

import static java.util.Base64.getEncoder;
import static sun.java2d.cmm.ColorTransform.Out;


/**
 * Created by abonnet on 1/18/17.
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

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public DataApiBusiness() {
    }

    public DataApiBusiness(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public boolean doesFileExist(String apiUri) throws ApiException {
        String lfcPath = getLFCPathFromApiUri(apiUri);
        try {
            return lfcBusiness.exists(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error testing lfc file existence", e);
            throw new ApiException("Error testing file existence");
        }
    }

    public void deletePath(String apiUri) throws ApiException {
        String lfcPath = getLFCPathFromApiUri(apiUri);
        if (!doesFileExist(apiUri)) {
            logger.error("trying to delete a non-existing file : " + apiUri);
            throw new ApiException("trying to delete a non-existing dile");
        }
        try {
            transferPoolBusiness.delete(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error deleting lfc file", e);
            throw new ApiException("Error deleting lfc file");
        }
    }

    public Path getFileApiPath(String apiUri) throws ApiException {
        Path path = new Path();
        path.setPlatformURI(apiUri);
        if (!doesFileExist(apiUri)) {
            path.setExists(false);
            return path;
        }
        path.setExists(true);
        List<Data> fileData = getFileData(apiUri);
        buildApiPathFromFileData(path, fileData);
        return path;
    }

    public List<Path> listDirectory(String apiUri) throws ApiException {
        List<Data> directoryData = getFileData(apiUri);
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

    public Long getFileModificationDate(String apiUri) throws ApiException {
        String lfcPath = getLFCPathFromApiUri(apiUri);
        try {
            return lfcBusiness.getModificationDate(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error getting lfc file modification date", e);
            throw new ApiException("Error getting lfc modification");
        }
    }

    public String getFileContent(String apiUri) throws ApiException {
        String downloadOperationId = downloadFile(apiUri);
        Callable<Boolean> isDownloadOverCall = () -> isDownloadOver(downloadOperationId);

        Callable<Boolean> waitForDownloadCall = () -> {
            while (true) {
                Future<Boolean> isDownloadOverFuture =
                        scheduler.schedule(isDownloadOverCall, 2, TimeUnit.SECONDS);
                if (isDownloadOverFuture.get()) {
                    return true;
                }
            }
        };
        Future<Boolean> waitForDownloadFuture =
                scheduler.submit(waitForDownloadCall);
        try {
            // wait for 45 seconds or abort
            waitForDownloadFuture.get(45, TimeUnit.SECONDS);
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

    private String downloadFile(String apiUri) throws ApiException {
        // it return the operation id
        String lfcPath = getLFCPathFromApiUri(apiUri);
        try {
            return transferPoolBusiness.downloadFile(apiContext.getUser(), lfcPath);
        } catch (BusinessException e) {
            logger.error("Error downloading lfc file :" + lfcPath);
            throw new ApiException("Error download LFC file");
        }
    }
    private Path buildPathFromLfcData(String rootApiUri, Data lfcData) {
        Path path = new Path();
        path.setExists(true);
        path.setSize(lfcData.getLength());
        path.setMimeType(lfcData.getModificationDate());
        // TODO : set modification date and mime type
        boolean isDirectory = lfcData.getType().equals(Type.folder)
                || lfcData.getType().equals(Type.folderSync);
        path.setIsDirectory(isDirectory);
        path.setPlatformURI(rootApiUri + "/" + lfcData.getName());
        return path;
    }

    private boolean isDownloadOver(String operationId) throws ApiException {
        PoolOperation operation;
        try {
            operation = transferPoolBusiness.getDownloadPoolOperation(operationId);
        } catch (BusinessException e) {
            logger.error("Error getting download operation", e);
            throw new ApiException("Error getting download operation");
        }
        switch (operation.getStatus()) {
            case Queued:
            case Running:
                logger.info("status of operation {" + operationId + "} : "  + operation.getStatus());
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
        PoolOperation operation = null;
        try {
            operation = transferPoolBusiness.getDownloadPoolOperation(operationId);
        } catch (BusinessException e) {
            logger.error("Error getting download operation", e);
            throw new ApiException("Error getting download operation");
        }

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
            e.printStackTrace();
        }
        return baos.toString();
    }

    private List<Data> getFileData(String apiUri) throws ApiException {
        String lfcPath = getLFCPathFromApiUri(apiUri);
        try {
            return lfcBusiness.listDir(apiContext.getUser(), lfcPath, true); // TODO always refresh ??
        } catch (BusinessException e) {
            logger.error("Error getting lfc file information", e);
            throw new ApiException("Error getting lfc information");
        }
    }

    private String getLFCPathFromApiUri(String apiUri) throws ApiException {
        String uriPrefix = env.getRequiredProperty(CarminProperties.API_URI_PREFIX);
        if (!apiUri.startsWith(uriPrefix)) {
            logger.error("Wrong uri prefix. Should start with :" + uriPrefix);
            throw new ApiException("Wrong URI scheme");
        }
        return apiUri.substring(uriPrefix.length());
    }

    private void buildApiPathFromFileData(Path path, List<Data> fileData) {
        if (fileData.size() == 1 && fileData.get(0).getName().startsWith("/")) {
            // this is a file, not a directory
            Data fileInfo = fileData.get(0);
            // it's a file
            path.setIsDirectory(false);
            path.setSize(fileInfo.getLength());
            path.setMimeType(fileInfo.getModificationDate());
            // TODO set modification date and mime type
        } else {
            // its a directory
            path.setIsDirectory(true);
            path.setSize(Long.valueOf(fileData.size()));
            // TODO set modification date and mime type
        }
    }

}
