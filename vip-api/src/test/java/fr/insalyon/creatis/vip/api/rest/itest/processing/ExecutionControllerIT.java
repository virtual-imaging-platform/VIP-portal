package fr.insalyon.creatis.vip.api.rest.itest.processing;

import static fr.insalyon.creatis.vip.api.data.ExecutionTestUtils.jsonCorrespondsToExecution;
import static fr.insalyon.creatis.vip.api.data.ExecutionTestUtils.WORKFLOW_1;
import static fr.insalyon.creatis.vip.api.data.ExecutionTestUtils.WORKFLOW_2;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser2;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import fr.insalyon.creatis.grida.common.bean.GridData;
import fr.insalyon.creatis.grida.common.bean.GridPathInfo;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.DataType;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Output;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.OutputID;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.api.data.UserTestUtils;
import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.model.Execution;
import fr.insalyon.creatis.vip.api.model.ExecutionStatus;
import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.api.rest.config.RestTestUtils;
import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.models.ResourceType;
import fr.insalyon.creatis.vip.application.models.Task;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.application.server.dao.SimulationDAO;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;

/**
 * Test method on platform path
 */
public class ExecutionControllerIT extends BaseRestApiSpringIT {

    private Workflow w1;
    private Workflow w2;

    @Autowired ResourceBusiness resourceBusiness;
    @Autowired AppVersionBusiness appVersionBusiness;
    @Autowired SimulationDAO mockSimulationDao;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        UserTestUtils.reset();

        createUser(baseUser1);
        w1 = new Workflow(WORKFLOW_1.getID(), baseUser1.getFullName(), fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Completed, new Date(), new Date(), "description", "application", "applicationVersion", "applicationClass", "engine", null);
        w2 = new Workflow(WORKFLOW_2.getID(), baseUser1.getFullName(), fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Completed, new Date(), new Date(), "description", "application", "applicationVersion", "applicationClass", "engine", null);

    }

    @AfterEach
    public void cleanUp() {
        UserTestUtils.reset();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldListExecutions() throws Exception {
        when(workflowDAO.get(baseUser1.getFullName(), null))
                .thenReturn(Arrays.asList(w1, w2), (List<Workflow>) null);

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(2)))
                // Check that the returned executions are the good ones
                .andExpect(jsonPath("$[0].status").value("Finished"))
                .andExpect(jsonPath("$[0].identifier").value("execId1"))
                .andExpect(jsonPath("$[1].status").value("Finished"))
                .andExpect(jsonPath("$[1].identifier").value("execId2"));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCountExecutions() throws Exception {
        when(workflowDAO.get(baseUser1.getFullName(), null))
                .thenReturn(Arrays.asList(w1, w2), (List<Workflow>) null);

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions/count").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.TEXT_CONTENT_TYPE_UTF8))
                .andExpect(content().string("2"));
    }

    @Test
    public void shouldGetExecution1() throws Exception {
        when(workflowDAO.get(eq(WORKFLOW_1.getID()))).thenReturn(w1, w1, null);

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions/" + WORKFLOW_1.getID()).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Check that the returned execution is the good one
                .andExpect(jsonPath("$.status").value("Finished"))
                .andExpect(jsonPath("$.identifier").value("execId1"));
    }

    @Test
    public void shouldGetExecution2() throws Exception {
        when(workflowDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(w2, w2, null);

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions/" + WORKFLOW_2.getID()).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Check that the returned execution is the good one
                .andExpect(jsonPath("$.status").value("Finished"))
                .andExpect(jsonPath("$.identifier").value("execId2"));
    }

    @Test
    public void shouldGetErrorOnUnknownExecution() throws Exception {
        mockMvc.perform(
                        get("/rest/executions/WrongExecId").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.NOT_FOUND.getCode()));
    }

    @Test
    public void shouldGetErrorOnExpectedException() throws Exception {
        when(workflowDAO.get(anyString())).thenThrow(new WorkflowsDBDAOException("test exception"));

        mockMvc.perform(
                        get("/rest/executions/WrongExecId").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.GENERIC_ERROR_WITH_MESSAGE.getCode()));
    }

    @Test
    public void shouldReturnErrorOnUnexpectedException() throws Exception {
        when(workflowDAO.get(anyString())).thenThrow(new RuntimeException("TEST RUNTIME EXCEPTION"));

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions/WrongExecId").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.GENERIC_ERROR_WITH_MESSAGE.getCode()));
    }

    @Test
    public void shouldUpdateExecution1() throws Exception {
        String newName = "Exec test 1 - modified";

        when(workflowDAO.get(eq(WORKFLOW_1.getID()))).thenReturn(w1, w1, w1, w1, null);

        workflowDAO.get(w1.getId()).setId(newName);

        // perform a getWorkflows()
        mockMvc.perform(
                put("/rest/executions/" + WORKFLOW_1.getID())
                        .contentType("application/json")
                        .content(getResourceAsString("jsonObjects/execution1-name-updated.json"))
                        .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Check that the returned execution is the good one and has been modified correctly
                .andExpect(jsonPath("$.name").value("Exec test 1 - modified"));
    }

    @Test
    public void testNotInitExecutionMissingField() throws Exception {
        mockMvc.perform(
                        post("/rest/executions").contentType("application/json")
                                .content("{}")
                                .with(baseUser1())
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(DefaultError.BAD_INPUT_FIELD.getCode())
                );
    }

    @Test
    public void shouldDeleteWithFilesExecution2() throws Exception {
        when(workflowDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(w2, w2, w2, w2, null);

        // perform a getWorkflows() and then delete
        mockMvc.perform(
                        put("/rest/executions/" + WORKFLOW_2.getID() + "/delete")
                                .contentType("application/json")
                                .content("{\"deleteFiles\":true}")
                                .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldReturn500() throws Exception {
        when(workflowDAO.get(baseUser1.getFullName(), null))
            .thenThrow(new RuntimeException("test exception"));

        // perform a getWorkflows() with an undetermined error
        mockMvc.perform(
                        get("/rest/executions").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.GENERIC_ERROR_WITH_MESSAGE.getCode()));
    }

    @Test
    public void shouldReturn400() throws Exception {
        // perform a getWorkflows() with a client error
        mockMvc.perform(
                        put("/rest/executions/whynotthisid").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(DefaultError.BAD_INPUT.getCode()));
    }


    @Test
    public void shouldGetExecution2Stderr() throws Exception {
        when(workflowDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(w2, w2, null);
        when(server.getWorkflowsPath()).thenReturn("src/test/resources/testFolder");
        Task testTask = new Task(42, TaskStatus.COMPLETED, "testCommand");
        testTask.setFileName("testjobfilename");
        when(mockSimulationDao.getJobs()).thenReturn(List.of(testTask));
        // normally simulationDao is a prototype-scope bean specific to a job, here as it is a singleton mock

        String testOutput = "blablablastderr\n";

        mockMvc.perform(get("/rest/executions/" + WORKFLOW_2.getID() + "/stderr").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.TEXT_CONTENT_TYPE_UTF8))
                .andExpect(content().string(testOutput));
    }

    @Test
    public void shouldGetExecution2Stdout() throws Exception {
        when(workflowDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(w2);
        when(server.getWorkflowsPath()).thenReturn("src/test/resources/testFolder");
        Task testTask = new Task(42, TaskStatus.COMPLETED, "testCommand");
        testTask.setFileName("testjobfilename");
        when(mockSimulationDao.getJobs()).thenReturn(List.of(testTask));
        // normally simulationDao is a prototype-scope bean specific to a job, here as it is a singleton mock

        String testOutput = "blablablastdout\n";

        mockMvc.perform(
                        get("/rest/executions/" + WORKFLOW_2.getID() + "/stdout").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.TEXT_CONTENT_TYPE_UTF8))
                .andExpect(content().string(testOutput));
    }

    @Test
    public void shouldDeleteWithoutExecution2() throws Exception {
        when(workflowDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(w2, w2, w2, w2, null);

        mockMvc.perform(
                        put("/rest/executions/" + WORKFLOW_2.getID() + "/delete")
                                .contentType("application/json")
                                .content("{\"deleteFiles\":false}")
                                .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }


    @Test
    public void testPlayExecutionIsNotImplemented() throws Exception {
        mockMvc.perform(
                        put("/rest/executions/" + WORKFLOW_1.getID() + "/play")
                                .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiError.NOT_IMPLEMENTED.getCode()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetExecution2Results() throws Exception {
        String resultPath = String.format("/root/user/%s/path/to/result.res", baseUser1.getFolder());

        when(workflowDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(w2, (Workflow) null);
        Output output = new Output(new OutputID("workflowID", resultPath, "processor"), DataType.URI, "port");
        when(outputDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(Arrays.asList(output), Arrays.asList(output), (List<Output>) null);

        Mockito.when(server.getDataManagerUsersHome()).thenReturn("/root/user");
        Mockito.when(gridaClient.getPathInfo(resultPath)).thenReturn(new GridPathInfo(true, GridData.Type.File));
        Mockito.when(gridaClient.getFolderData(resultPath, true)).thenReturn(Arrays.asList(
                new GridData("result.res", GridData.Type.File, 42, "modifData", "", "", "")));

        mockMvc.perform(
                        get("/rest/executions/" + WORKFLOW_2.getID() + "/results").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(1)))
                .andExpect(jsonPath("$[0].path", equalTo("/vip/Home/path/to/result.res")))
                .andExpect(jsonPath("$[0].size", equalTo(42)));
    }


    @Test
    public void shouldKillExecution2() throws Exception
    {
        when(workflowDAO.get(eq(WORKFLOW_2.getID()))).thenReturn(w2, w2, null);

        mockMvc.perform(
                        put("/rest/executions/" + WORKFLOW_2.getID() + "/kill").with(baseUser1()))
                .andDo(print());

        verify(webServiceEngine).kill(w2.getEngine(), WORKFLOW_2.getID());
    }

    // the difference (at the moment) is that with moteurLite the optional and absent parameters are not included
    @Test
    @SuppressWarnings("unchecked")
    public void testInitBoutiquesExecution() throws Exception
    {
        String appName = "test application", groupName = "testGroup", versionName = "4.2";
        String engineEndpoint = "endpoint", workflowId = "test-workflow-id";
        Date startDate = new Date();

        configureBoutiquesTestApp(appName, groupName, versionName);

        createGroup("testResources", GroupType.RESOURCE);
        baseUser2 = createUserInGroups(baseUser2.getEmail(), "", groupName, "testResources");

        ArgumentCaptor<String> inputsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> workflowContentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Workflow> workflowCaptor = ArgumentCaptor.forClass(Workflow.class);

        Mockito.when(server.getVoName()).thenReturn("test-vo-name");
        Mockito.when(server.getServerProxy("test-vo-name")).thenReturn("/path/to/proxy");
        Mockito.when(webServiceEngine.launch(eq(engineEndpoint), workflowContentCaptor.capture(), inputsCaptor.capture(), eq("{\"default.executor\":\"LOCAL\"}"), eq(""), eq("/path/to/proxy"))).thenReturn(workflowId, (String) null);
        Mockito.when(webServiceEngine.getStatus(engineEndpoint, workflowId)).thenReturn(WorkflowStatus.Running, (WorkflowStatus) null);

        Workflow w = new Workflow(workflowId, baseUser2.getFullName(), fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Running, startDate, null, "Exec test 1", appName, versionName, "", engineEndpoint, null);
        when(workflowDAO.get(workflowId)).thenReturn(w, (Workflow) null);

        Execution expectedExecution = new Execution(workflowId, "Exec test 1", appName + "/" + versionName, 0, ExecutionStatus.RUNNING, null, null, startDate.getTime(), null, null);
        expectedExecution.getJobs().put(0, new HashMap<>() {{
            put("exitCode", 0);
            put("exitMessage", "Successfully executed");
            put("status", "COMPLETED");
            put("inputs", new ArrayList<>());
            put("outputs", new ArrayList<>());
        }});

        setUpResourceAndEngine(appName, versionName, engineEndpoint);

        mockMvc.perform(
                        post("/rest/executions").contentType("application/json")
                                .content(getResourceAsString("jsonObjects/execution1.json"))
                                .with(baseUser2()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$",
                        jsonCorrespondsToExecution(expectedExecution)
                ));

        // verify workflow path
        Assertions.assertEquals(fileUtil.read(getBoutiquesTestFile()), workflowContentCaptor.getValue());

        // verify inputs / same as gwendia without optional one
        String inputs = inputsCaptor.getValue();
        Map<String, List<String>> expectedParams = new HashMap<>();
        expectedParams.put("testFileInput", List.of("lfn:" + ServerMockConfig.TEST_USERS_ROOT + "/" +  baseUser2.getFolder() + "/path/to/input.in"));
        expectedParams.put("testTextInput", List.of("best test text value"));
        expectedParams.put("testFlagInput", List.of("false"));
        expectedParams.put("results-directory", List.of("lfn:" + ServerMockConfig.TEST_USERS_ROOT + "/" +  baseUser2.getFolder()));
        List<Map<String, List<String>>> paramsList = new ArrayList<>();
        paramsList.add(expectedParams);
        String expectedInputs = workflowExecutionBusiness.getParametersAsJSONInput(paramsList);
        Assertions.assertEquals(expectedInputs, inputs);

        // verify created workflow
        Mockito.verify(workflowDAO).add(workflowCaptor.capture());
        Workflow workflow = workflowCaptor.getValue();
        Assertions.assertEquals(appName, workflow.getApplication());
        Assertions.assertEquals(versionName, workflow.getApplicationVersion());
        Assertions.assertEquals(workflowId, workflow.getId());
        Assertions.assertEquals(fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Running, workflow.getStatus());
        Assertions.assertEquals("Exec test 1", workflow.getDescription());
        Assertions.assertEquals(engineEndpoint, workflow.getEngine());
        Assertions.assertEquals(baseUser2.getFullName(), workflow.getUsername());
        Assertions.assertNull(workflow.getFinishedTime());
        MatcherAssert.assertThat(workflow.getStartedTime().getTime(),
                is(both(greaterThan(startDate.getTime())).and(lessThan(new Date().getTime()))));

    }

    public void setUpResourceAndEngine(String appName, String version, String endpoint) throws Exception {
        Engine engine = new Engine("testEngine", endpoint, "enabled");
        Resource resource = new Resource(
            "testResource", 
            true, 
            ResourceType.LOCAL, 
            "", 
            Arrays.asList(engine.getName()),
            Set.of(new Group("testResources", true, GroupType.APPLICATION)));

        engineBusiness.add(engine);
        resourceBusiness.add(resource);
        resourceBusiness.associate(resource, appVersionBusiness.getVersion(appName, version));
    }
}
