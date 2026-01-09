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
package fr.insalyon.creatis.vip.api.rest.itest.data;

import fr.insalyon.creatis.vip.api.model.PathProperties;
import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation.Status;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation.Type;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fr.insalyon.creatis.vip.api.data.PathTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 1/23/17.
 */
@Disabled
public class DataControllerIT extends BaseRestApiSpringIT {

    @Test
    public void shouldReturnFilePath() throws Exception {
        configureDataFS();
        String testLfcPath = getAbsolutePath(testFile1);
        mockMvc.perform(
                get("/rest/path" + testLfcPath).param("action", "properties").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", jsonCorrespondsToPath(testFile1PathProperties)));
    }

    @Test
    public void shouldReturnDirectoryPath() throws Exception {
        configureDataFS();
        String testLfcPath = getAbsolutePath(testDir1);
        mockMvc.perform(
                get("/rest/path" + testLfcPath).param("action", "properties").with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", jsonCorrespondsToPath(getPathWithTS(testDir1PathProperties))));
    }

    @Test
    public void shouldReturnNonExistingPath() throws Exception {
        String testLfcPath = "/vip/Home/WRONG/PATH";
        when(lfcBusiness.exists(eq(baseUser1), eq(testLfcPath)))
            .thenReturn(false);
        PathProperties expectedPathProperties = new PathProperties();
        expectedPathProperties.setExists(false);
        expectedPathProperties.setPath(testLfcPath);
        mockMvc.perform(
                get("/rest/path" + testLfcPath).param("action", "exists").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.exists").value(false));
    }

    @Test
    public void shouldListuser2Dir() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(user2Dir);
        mockMvc.perform(
                get("/rest/path" + lfcPath).param("action", "list").with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", Matchers.containsInAnyOrder(
                        jsonCorrespondsToPath(testDir1PathProperties),
                        jsonCorrespondsToPath(testFile2PathProperties)
                ))
        );
    }

    @Test
    public void shouldListDirectory1() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(testDir1);
        mockMvc.perform(
                get("/rest/path" + lfcPath).param("action", "list").with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", Matchers.containsInAnyOrder(
                        jsonCorrespondsToPath(testFile3PathProperties),
                        jsonCorrespondsToPath(testFile4PathProperties),
                        jsonCorrespondsToPath(testFile5PathProperties)
                        ))
                );
    }

    @Test
    public void shouldDownload() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(testFile1);
        String operationId = "testOpId";
        String testFile = Paths.get(ClassLoader.getSystemResource("testFile.txt").toURI())
                .toAbsolutePath().toString();
        PoolOperation donePoolOperation = new PoolOperation(operationId,
                null, null, null, testFile, Type.Download, Status.Done, baseUser1.getEmail(), 100);
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Download, Status.Running, baseUser1.getEmail(), 0);
        when (transferPoolBusiness.downloadFile(eq(baseUser1), eq(lfcPath)))
            .thenReturn(operationId);
        when (transferPoolBusiness.getOperationById(
                  eq(operationId), eq(baseUser1.getFolder())))
            .thenReturn(runningPoolOperation, runningPoolOperation, donePoolOperation);
        when (transferPoolBusiness.getDownloadPoolOperation(operationId))
                .thenReturn(donePoolOperation);
        mockMvc.perform(
                get("/rest/path" + lfcPath).param("action", "content").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk());
        // todo test file content
    }

    @Test
    public void shouldHaveDownloadTimeout() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(testFile1);
        String operationId = "testOpId";
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Download, Status.Running, baseUser1.getEmail(), 0);
        when (transferPoolBusiness.downloadFile(eq(baseUser1), eq(lfcPath)))
            .thenReturn(operationId);
        when (transferPoolBusiness.getOperationById(
                  eq(operationId), eq(baseUser1.getFolder())))
            .thenReturn(runningPoolOperation, runningPoolOperation);
        mockMvc.perform(
                get("/rest/path" + lfcPath).param("action", "content").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDownloadAfterFirstTimeout() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(testFile1);
        String operationId = "testOpId";
        String testFile = Paths.get(ClassLoader.getSystemResource("testFile.txt").toURI())
                .toAbsolutePath().toString();
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Download, Status.Running, baseUser1.getEmail(), 0);
        PoolOperation donePoolOperation = new PoolOperation(operationId,
                null, null, null, testFile, Type.Download, Status.Done, baseUser1.getEmail(), 100);
        when (transferPoolBusiness.downloadFile(eq(baseUser1), eq(lfcPath)))
                .thenReturn(operationId);
        when (transferPoolBusiness.getOperationById(
                eq(operationId), eq(baseUser1.getFolder())))
                .thenReturn(runningPoolOperation);
        when (transferPoolBusiness.getDownloadPoolOperation(operationId))
              .thenReturn(donePoolOperation);
        mockMvc.perform(
                get("/rest/path" + lfcPath).param("action", "content").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        // now do an OK download
        Thread.sleep(3*1000);
          when (transferPoolBusiness.getOperationById(
                  eq(operationId), eq(baseUser1.getFolder())))
                  .thenReturn(runningPoolOperation, runningPoolOperation, donePoolOperation);
        mockMvc.perform(
                get("/rest/path" + lfcPath).param("action", "content").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUploadFile() throws Exception {
        configureDataFS();
        String path =  getAbsolutePath(testDir1) + "/uploaded.txt";
        byte fileContent[] = Files.readAllBytes(Paths.get(
                ClassLoader.getSystemResource("testFile.txt").toURI()));
        String operationId = "testOpId";
        PoolOperation donePoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Upload, Status.Done, baseUser2.getEmail(), 100);
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Upload, Status.Running, baseUser2.getEmail(), 0);
        when (transferPoolBusiness.uploadFile(
                  eq(baseUser2),
                  anyString(),
                  eq(getAbsolutePath(testDir1))))
                .thenReturn(operationId);
        when (transferPoolBusiness.getOperationById(
                  eq(operationId), eq(baseUser2.getFolder())))
            .thenReturn(runningPoolOperation, runningPoolOperation, donePoolOperation);
        mockMvc.perform(
                put("/rest/path" + path)
                        .content(fileContent).contentType(MediaType.TEXT_PLAIN)
                        .with(baseUser2()))
                .andDo(print())
                .andExpect(status().isCreated());
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(transferPoolBusiness).uploadFile(
            eq(baseUser2),
            captor.capture(),
            eq(getAbsolutePath(testDir1)));
        String copiedFile = captor.getValue();
        File expectedFile = getResourceFromClasspath("testFile.txt").getFile();
        assertThat(
                FileUtils.contentEquals(expectedFile, new File(copiedFile)),
                Matchers.is(true));
        assertThat(copiedFile, Matchers.startsWith("/tmp"));
    }

    @Test
    public void shouldUploadBase64Data() throws Exception {
        configureDataFS();
        String path =  getAbsolutePath(testDir1) + "/uploaded.txt";
        String operationId = "testOpId";
        PoolOperation donePoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Upload, Status.Done, baseUser2.getEmail(), 100);
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Upload, Status.Running, baseUser2.getEmail(), 0);
        when (transferPoolBusiness.uploadFile(
                  eq(baseUser2),
                  anyString(),
                  eq(getAbsolutePath(testDir1))))
            .thenReturn(operationId);
        when (transferPoolBusiness.getOperationById(
                  eq(operationId), eq(baseUser2.getFolder())))
            .thenReturn(runningPoolOperation, runningPoolOperation, donePoolOperation);
        mockMvc.perform(
                put("/rest/path" + path)
                        .content(getResourceAsString("jsonObjects/uploadData_1.json"))
                        .contentType("application/carmin+json")
                        .with(baseUser2()))
                .andDo(print())
                .andExpect(status().isCreated());
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(transferPoolBusiness).uploadFile(
            eq(baseUser2),
            captor.capture(),
            eq(getAbsolutePath(testDir1)));
        String copiedFile = captor.getValue();
        File expectedFile = getResourceFromClasspath("b64decoded/uploadData_1.txt").getFile();
        assertThat(
                FileUtils.contentEquals(expectedFile, new File(copiedFile)),
                Matchers.is(true));
        assertThat(copiedFile, Matchers.startsWith("/tmp"));
    }
}
