package fr.insalyon.creatis.vip.api.rest.itest.processing;


import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.fileParam;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.flagParam;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.getFullPipeline;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.getPipeline;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.jsonCorrespondsToPipeline;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.optionalTextParam;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.textParam;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser2;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser3;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser4;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.core.models.GroupType;

public class PipelineControllerIT extends BaseRestApiSpringIT {

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
    public void shouldReturnPublicApps() throws Exception {
        setAdminContext();
        createGroup("group1");
        createAnApplication("app1", "group1");
        AppVersion app11 = createAVersion("app1", "v1", true);
        AppVersion app12 = createAVersion("app1", "v2", true);
        createAnApplication("app2", "group1");
        AppVersion app21 = createAVersion("app2", "v1", true);
        createGroup("group2", GroupType.APPLICATION, false);
        createAnApplication("app3", "group2");
        AppVersion app31 = createAVersion("app3", "v1", true);
        //To test deduplication
        createGroup("group3");
        putApplicationInGroup(app12.getApplicationName(), "group3");

        //To test popularity score
        createUser(baseUser1);
        Workflow w1 = new Workflow("w1", baseUser1.getFullName(), fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Completed, new Date(), new Date(), "description", app11.getApplicationName(), app11.getVersion(), "applicationClass", "engine", null);
        Workflow w2 = new Workflow("w2", baseUser1.getFullName(), fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Completed, new Date(), new Date(), "description", app11.getApplicationName(), app11.getVersion(), "applicationClass", "engine", null);
        clearContext();
        // public URL, not authenticated
        mockMvc.perform(get("/rest/pipelines?public"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[0].identifier", equalTo(app11.getApplicationName() + "/" + app11.getVersion())))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        jsonCorrespondsToPipeline(getPipeline(app11)),
                        jsonCorrespondsToPipeline(getPipeline(app12)),
                        jsonCorrespondsToPipeline(getPipeline(app21)))));
    }

    @Test
    public void shouldReturnPublicAppsDescriptors() throws Exception {
        setAdminContext();
        createGroup("group1public", GroupType.APPLICATION, true);
        createAnApplication("app1visible", "group1public");
        AppVersion app1g1 = applicationTestConfigurer.createAVersion("app1visible", "v67",
                getResourceAsString("boutiques/test-boutiques2.json"), true);
        createGroup("group2public", GroupType.APPLICATION, true);
        createAnApplication("app2hidden", "group2public");
        AppVersion app2g2 = applicationTestConfigurer.createAVersion("app2hidden", "v42",
                getResourceAsString("boutiques/test-boutiques.json"), false);
        createAnApplication("app3visible", "group2public");
        AppVersion app3g2 = applicationTestConfigurer.createAVersion("app3visible", "v42",
                getResourceAsString("boutiques/test-boutiques.json"), true);
        createGroup("group3private", GroupType.APPLICATION, false);
        createAnApplication("app4visible", "group3private");
        AppVersion app4g3 = applicationTestConfigurer.createAVersion("app4visible", "v67",
                getResourceAsString("boutiques/test-boutiques2.json"), true);
        clearContext();

        // public URL, not authenticated
        mockMvc.perform(get("/rest/pipelines?public&format=boutiques"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(
                        equalTo(boutiquesBusiness.parseBoutiquesString(app1g1.getDescriptor()).getDescription()),
                        equalTo(boutiquesBusiness.parseBoutiquesString(app3g2.getDescriptor()).getDescription())
                )));
    }

    @Test
    public void userGetAPipelineWithBoutiques() throws Exception {
        String appName = "testBoutiquesApp", groupName = "testGroup", versionName = "v42";
        AppVersion appVersion = configureBoutiquesTestApp(appName, groupName, versionName);
        String pipelineId = appName + "/" + versionName;

        baseUser1 = createUserInGroup(baseUser1.getEmail(), groupName);

        mockMvc.perform(get("/rest/pipelines/" + pipelineId).with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // res-dir should be absent from the description
                .andExpect(jsonPath("$.name", equalTo(appName)))
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(
                        getFullPipeline(appVersion, "Test app from axel", flagParam, textParam, fileParam, optionalTextParam))));

    }

    @Test
    public void userGetBoutiquesDescriptor() throws Exception {
        setAdminContext();
        String appName = "testBoutiquesApp", groupName = "testGroup", versionName = "v42";
        configureBoutiquesTestApp(appName, groupName, versionName);
        String pipelineId = appName + "/" + versionName;

        baseUser1 = createUserInGroup(baseUser1.getEmail(), groupName);
        clearContext();

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
        setAdminContext();
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

        baseUser1 = createUserInGroup(baseUser1.getEmail(), "test1", "group1");
        baseUser2 = createUserInGroup(baseUser2.getEmail(), "test2", "group2");
        baseUser3 = createUserInGroup(baseUser3.getEmail(), "test3", "group3");
        baseUser4 = createUserInGroups(baseUser4.getEmail(), "test4", "group1", "group2");
        clearContext();

        // temp trailing slash for shanoir, see fr.insalyon.creatis.vip.api.SpringRestApiConfig::configurePathMatch
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
