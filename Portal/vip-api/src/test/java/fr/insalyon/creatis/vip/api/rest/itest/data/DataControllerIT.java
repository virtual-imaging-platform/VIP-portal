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

import fr.insalyon.creatis.vip.api.data.PathTestUtils;
import fr.insalyon.creatis.vip.api.rest.config.*;
import fr.insalyon.creatis.vip.api.rest.model.Path;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation.*;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.nio.file.Paths;
import java.text.*;
import java.util.*;

import static fr.insalyon.creatis.vip.api.VipConfigurer.logger;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_API_URI_PREFIX;
import static fr.insalyon.creatis.vip.api.data.PathTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 1/23/17.
 */
public class DataControllerIT extends BaseVIPSpringIT {

    @Test
    public void shouldReturnFilePath() throws Exception {
        configureDataFS();
        String testLfcPath = getAbsolutePath(testFile1);
        mockMvc.perform(
                get("/rest/path").param("uri", TEST_API_URI_PREFIX + testLfcPath).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$", jsonCorrespondsToPath(testFile1Path)));
    }

    @Test
    public void shouldReturnDirectoryPath() throws Exception {
        configureDataFS();
        String testLfcPath = getAbsolutePath(testDir1);
        String uri = TEST_API_URI_PREFIX + testLfcPath;
        mockMvc.perform(
                get("/rest/path").param("uri", uri).with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$", jsonCorrespondsToPath(getPathWithTS(testDir1Path))));
    }

    @Test
    public void shouldReturnNonExistingPath() throws Exception {
        String testLfcPath = "/vip/Home/WRONG/PATH";
        String uri = TEST_API_URI_PREFIX + testLfcPath;
        when(lfcBusiness.exists(baseUser1, testLfcPath))
                .thenReturn(false);
        Path expectedPath = new Path();
        expectedPath.setExists(false);
        expectedPath.setPlatformURI(uri);
        mockMvc.perform(
                get("/rest/path").param("uri", uri).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$", jsonCorrespondsToPath(expectedPath)));
    }

    @Test
    public void shouldListuser2Dir() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(user2Dir);
        String uri = TEST_API_URI_PREFIX + lfcPath;
        mockMvc.perform(
                get("/rest/path/directory").param("uri", uri).with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.containsInAnyOrder(
                        jsonCorrespondsToPath(testDir1Path),
                        jsonCorrespondsToPath(testFile2Path)
                ))
        );
    }

    @Test
    public void shouldListDirectory1() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(testDir1);
        String uri = TEST_API_URI_PREFIX + lfcPath;
        mockMvc.perform(
                get("/rest/path/directory").param("uri", uri).with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.containsInAnyOrder(
                        jsonCorrespondsToPath(testFile3Path),
                        jsonCorrespondsToPath(testFile4Path),
                        jsonCorrespondsToPath(testFile5Path)
                        ))
                );
    }

    @Test
    public void shouldDownload() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(testFile1);
        String uri = TEST_API_URI_PREFIX + lfcPath;
        String operationId = "testOpId";
        String testFile = Paths.get(ClassLoader.getSystemResource("testFile.txt").toURI())
                .toAbsolutePath().toString();
        PoolOperation donePoolOperation = new PoolOperation(operationId,
                null, null, null, testFile, Type.Download, Status.Done, baseUser1.getEmail(), 100);
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Download, Status.Running, baseUser1.getEmail(), 0);
        when (transferPoolBusiness.downloadFile(baseUser1, lfcPath))
                .thenReturn(operationId);
        when (transferPoolBusiness.getDownloadPoolOperation(operationId))
                .thenReturn(runningPoolOperation, runningPoolOperation, donePoolOperation);
        mockMvc.perform(
                get("/rest/path/content").param("uri", uri).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHaveDownloadTimeout() throws Exception {
        configureDataFS();
        String lfcPath = getAbsolutePath(testFile1);
        String uri = TEST_API_URI_PREFIX + lfcPath;
        String operationId = "testOpId";
        String testFile = Paths.get(ClassLoader.getSystemResource("testFile.txt").toURI())
                .toAbsolutePath().toString();
        PoolOperation runningPoolOperation = new PoolOperation(operationId,
                null, null, null, null, Type.Download, Status.Running, baseUser1.getEmail(), 0);
        when (transferPoolBusiness.downloadFile(baseUser1, lfcPath))
                .thenReturn(operationId);
        when (transferPoolBusiness.getDownloadPoolOperation(operationId))
                .thenReturn(runningPoolOperation, runningPoolOperation);
        mockMvc.perform(
                get("/rest/path/content").param("uri", uri).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
