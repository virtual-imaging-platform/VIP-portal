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
package fr.insalyon.creatis.vip.api.rest.itest.processing;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.server.business.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.*;
import static fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError.WRONG_APPLICATION_DESCRIPTOR;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PipelineControllerIT extends BaseWebSpringIT {

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void pipelineMethodShouldBeSecured() throws Exception {
        mockMvc.perform(get("/rest/pipelines"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnErrorOnAPIException() throws Exception {
        mockMvc.perform(get("/rest/pipelines/WRONG_APP").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiError.INVALID_PIPELINE_IDENTIFIER.getCode()));
    }

    @Test
    public void shouldReturnErrorOnAPIExceptionInvalidId() throws Exception {
        mockMvc.perform(get("/rest/pipelines").param("pipelineId", "incorrect pipeline id").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiError.INVALID_PIPELINE_IDENTIFIER.getCode()));
    }

    @Test
    public void shouldReturnErrorOnConfiguredVipException() throws Exception {
        String appName = "testApp", groupName = "testGroup";
        String versionName = "42-test";
        AppVersion appVersion = configureAnApplication(appName, versionName, groupName);
        configureVersion(appVersion, FileUtil.read(getBoutiquesTestFile()));

        createUserInGroup(baseUser1.getEmail(), groupName);

        Mockito.when(server.getDataManagerPath()).thenReturn("/test/folder");
        Mockito.when(server.getDataManagerGroupsHome()).thenReturn("/root/group");
        // localDir is datamanagerpath + "downloads" + groupRoot + dir(path)
        Mockito.when(gridaClient.getRemoteFile(
                "/root/group/testGroup/path/to/test.gwendia",
                "/test/folder/downloads/root/group/testGroup/path/to")).thenThrow(new GRIDAClientException("test exception"));

        String pipelineId = appName + "/" + versionName;
        mockMvc.perform(get("/rest/pipelines/" + pipelineId).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(WRONG_APPLICATION_DESCRIPTOR.getCode()));
    }

    @Test
    public void userGetAPipelineWithPathParameterNonEncoded() throws Exception {

        String appName = "testGwendiaApp", groupName = "testGroup", versionName = "42-test";
        AppVersion appVersion = configureGwendiaTestApp(appName, groupName, versionName);
        String pipelineId = appName + "/" + versionName;

        createUserInGroup(baseUser1.getEmail(), groupName);

        mockMvc.perform(get("/rest/pipelines/" + pipelineId).with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // res-dir should be removed from the description
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(
                        getFullPipeline(appVersion, "Test tool description. Must be similar to the boutiques one", flagParam, textParam, fileParam, optionalTextParamNoValueProvided))));
    }

    @Test
    public void userGetAPipelineWithQueryParameter() throws Exception {
        String appName = "testGwendiaApp", groupName = "testGroup", versionName = "42-test";
        AppVersion appVersion = configureGwendiaTestApp(appName, groupName, versionName);
        String pipelineId = appName + "/" + versionName;

        createUserInGroup(baseUser1.getEmail(), groupName);

        mockMvc.perform(get("/rest/pipelines").param("pipelineId", pipelineId).with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // res-dir should be removed from the description
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(
                        getFullPipeline(appVersion, "Test tool description. Must be similar to the boutiques one", flagParam, textParam, fileParam, optionalTextParamNoValueProvided))));
    }

    @Test
    public void userGetAPipelineWithBoutiques() throws Exception {

        String appName = "testBoutiquesApp", groupName = "testGroup", versionName = "v42";
        AppVersion appVersion = configureBoutiquesTestApp(appName, groupName, versionName);
        String pipelineId = appName + "/" + versionName;

        Mockito.when(server.useMoteurlite()).thenReturn(true);

        createUserInGroup(baseUser1.getEmail(), groupName);

        mockMvc.perform(get("/rest/pipelines/" + pipelineId).with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // res-dir should be absent from the description
                .andExpect(jsonPath("$.name", equalTo(appName)))
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(
                        getFullPipeline(appVersion, "Test app from axel. Must be similar to the gwendia test app", flagParam, textParam, fileParam, optionalTextParam))));

    }

    @Test
    public void userGetBoutiquesDescriptor() throws Exception {

        String appName = "testBoutiquesApp", groupName = "testGroup", versionName = "v42";
        configureBoutiquesTestApp(appName, groupName, versionName);
        String pipelineId = appName + "/" + versionName;

        createUserInGroup(baseUser1.getEmail(), groupName);

        mockMvc.perform(get("/rest/pipelines/" + pipelineId).param("format", "boutiques").with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // res-dir should be removed from the description
                .andExpect(jsonPath("$.name", equalTo(appName)))
                .andExpect(jsonPath("$.tool-version", equalTo(versionName)))
                .andExpect(jsonPath("$.schema-version", equalTo("0.5")))
                .andExpect(jsonPath("$.inputs[*]", hasSize(4)));

    }

    @Test
    public void shouldReturnPipelines() throws Exception {
        createGroup("group1");
        createGroup("group2");
        createGroup("group3");

        createAnApplication("app1", "group1");
        createAnApplication("app2", "group2");
        createAnApplication("app3", "group3");

        AppVersion app11 = createAVersion("app1", "v1", true);
        AppVersion app12 = createAVersion("app1", "v2", false);
        AppVersion app13 = createAVersion("app1", "v3", true);
        AppVersion app2 = createAVersion("app2", "v1.1", true);
        AppVersion app34 = createAVersion("app3", "v4", false);

        createAnApplication("appAB", "group1");
        createAnApplication("appBC", "group2");
        createAnApplication("appC", "group3");
        putApplicationInGroup("appAB", "group2");
        putApplicationInGroup("appBC", "group3");

        AppVersion appAB = createAVersion("appAB", "v1", true);
        AppVersion appBC = createAVersion("appBC", "v1", true);
        AppVersion appC = createAVersion("appC", "v1", true);

        createUserInGroup(baseUser1.getEmail(), "test1", "group1");
        createUserInGroup(baseUser2.getEmail(), "test2", "group2");
        createUserInGroup(baseUser3.getEmail(), "test3", "group3");
        createUserInGroups(baseUser4.getEmail(), "test4", "group1", "group2");

        // temp trailing slash for shanoir, see fr.insalyon.creatis.vip.api.SpringWebConfig::configurePathMatch
        mockMvc.perform(get("/rest/pipelines/").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        jsonCorrespondsToPipeline(getPipeline(app11)),
                        jsonCorrespondsToPipeline(getPipeline(app13)),
                        jsonCorrespondsToPipeline(getPipeline(appAB)))));

        mockMvc.perform(get("/rest/pipelines").with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        jsonCorrespondsToPipeline(getPipeline(app2)),
                        jsonCorrespondsToPipeline(getPipeline(appAB)),
                        jsonCorrespondsToPipeline(getPipeline(appBC)))));

        mockMvc.perform(get("/rest/pipelines").with(baseUser3()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        jsonCorrespondsToPipeline(getPipeline(appBC)),
                        jsonCorrespondsToPipeline(getPipeline(appC)))));

        mockMvc.perform(get("/rest/pipelines").with(baseUser4()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        jsonCorrespondsToPipeline(getPipeline(app11)),
                        jsonCorrespondsToPipeline(getPipeline(app13)),
                        jsonCorrespondsToPipeline(getPipeline(app2)),
                        jsonCorrespondsToPipeline(getPipeline(appAB)),
                        jsonCorrespondsToPipeline(getPipeline(appBC)))));
    }
}
