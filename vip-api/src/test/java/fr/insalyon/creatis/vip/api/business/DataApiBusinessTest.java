package fr.insalyon.creatis.vip.api.business;

import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.api.model.UploadData;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;

public class DataApiBusinessTest {

    @Test
    public void testBase64Decoder(@TempDir Path tempDir) throws IOException, VipException {

        // Prepare
        TransferPoolBusiness transferPoolBusiness = Mockito.mock(TransferPoolBusiness.class);
        LFCPermissionBusiness lfcPermissionBusiness = Mockito.mock(LFCPermissionBusiness.class);
        LFCBusiness lfcBusiness = Mockito.mock(LFCBusiness.class);
        DataManagerBusiness dataManagerBusiness = Mockito.mock(DataManagerBusiness.class);
        Server server = Mockito.mock(Server.class);

        String lfcParentPath = "/vip/Home";
        String lfcPath = lfcParentPath + "/test_uploaded.txt";
        File uploadDataFile = new ClassPathResource("jsonObjects/uploadData_1.json").getFile();
        UploadData uploadData = new ObjectMapper().readValue(uploadDataFile, UploadData.class);
        String operationId = "testOpId";

        PoolOperation donePoolOperation = new PoolOperation(operationId,
                null, null, null, null, PoolOperation.Type.Upload, PoolOperation.Status.Done, baseUser2.getEmail(), 100);
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, PoolOperation.Type.Upload, PoolOperation.Status.Running, baseUser2.getEmail(), 0);

        String expectedUploadedPath = tempDir.resolve("test_uploaded.txt").toAbsolutePath().toString();

        // Configure
        when(lfcPermissionBusiness.isLFCPathAllowed(baseUser2, lfcPath, LFCPermissionBusiness.LFCAccessType.UPLOAD, true)).thenReturn(true);
        when(lfcBusiness.exists(baseUser2, lfcParentPath)).thenReturn(true);
        when(dataManagerBusiness.getUploadRootDirectory(false)).thenReturn(tempDir.toAbsolutePath() + "/");
        when(server.getApiParallelDownloadNb()).thenReturn(2);
        when(server.getCarminApiDownloadTimeoutInSeconds()).thenReturn(10);
        when(server.getCarminApiDownloadRetryInSeconds()).thenReturn(1);

        when (transferPoolBusiness.uploadFile(
                baseUser2, expectedUploadedPath, lfcParentPath))
                .thenReturn(operationId);
        when (transferPoolBusiness.getOperationById(
                eq(operationId), eq(baseUser2.getFolder())))
                .thenReturn(runningPoolOperation, runningPoolOperation, donePoolOperation);

        // Doing it
        DataApiBusiness sut = new DataApiBusiness(server, () -> baseUser2, lfcBusiness, transferPoolBusiness, lfcPermissionBusiness, dataManagerBusiness);
        sut.uploadCustomData(lfcPath, uploadData);

        // Verify
        File expectedFile = new ClassPathResource("b64decoded/uploadData_1.txt").getFile();
        assertThat(
                FileUtils.contentEquals(expectedFile, new File(expectedUploadedPath)),
                Matchers.is(true));
    }
}
