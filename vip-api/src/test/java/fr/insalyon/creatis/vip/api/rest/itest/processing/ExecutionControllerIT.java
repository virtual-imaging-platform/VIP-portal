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

import fr.insalyon.creatis.grida.common.bean.GridData;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.Execution;
import fr.insalyon.creatis.vip.api.model.ExecutionStatus;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.api.rest.config.RestTestUtils;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.ResourceType;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.*;

import static fr.insalyon.creatis.vip.api.data.ExecutionTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/20/16.
 * <p>
 * Test method on platform path
 */

public class ExecutionControllerIT extends BaseWebSpringIT {

    private Workflow w1;
    private Workflow w2;

    @Autowired ResourceBusiness resourceBusiness;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        w1 = new Workflow(simulation1.getID(), baseUser1.getFullName(), WorkflowStatus.Completed, new Date(), new Date(), "description", "application", "applicationVersion", "applicationClass", "engine", null);
        w2 = new Workflow(simulation2.getID(), baseUser1.getFullName(), WorkflowStatus.Completed, new Date(), new Date(), "description", "application", "applicationVersion", "applicationClass", "engine", null);
    }

    @Test
    public void shouldListExecutions() throws Exception {
        when(workflowDAO.get(eq(simulation1.getID()))).thenReturn(w1, null);
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, null);
        when(workflowDAO.get(Collections.singletonList(baseUser1.getFullName()), null, null, null, null, null, null))
                .thenReturn(Arrays.asList(w1, w2), null);

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
    public void shouldCountExecutions() throws Exception {
        when(workflowDAO.get(Collections.singletonList(baseUser1.getFullName()), null, null, null, null, null, null))
                .thenReturn(Arrays.asList(w1, w2), null);

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
        when(workflowDAO.get(eq(simulation1.getID()))).thenReturn(w1, w1, null);

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions/" + simulation1.getID()).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Check that the returned execution is the good one
                .andExpect(jsonPath("$.status").value("Finished"))
                .andExpect(jsonPath("$.identifier").value("execId1"));
    }

    @Test
    public void shouldGetExecution2() throws Exception {
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, w2, null);

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions/" + simulation2.getID()).with(baseUser1()))
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
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.GENERIC_API_ERROR.getCode()));
    }

    @Test
    public void shouldGetErrorOnExpectedException() throws Exception {
        when(workflowDAO.get(anyString())).thenThrow(new WorkflowsDBDAOException("test exception"));

        mockMvc.perform(
                        get("/rest/executions/WrongExecId").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.GENERIC_API_ERROR.getCode()));
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
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.GENERIC_API_ERROR.getCode()));
    }

    @Test
    public void shouldUpdateExecution1() throws Exception {
        String newName = "Exec test 1 - modified";

        when(workflowDAO.get(eq(simulation1.getID()))).thenReturn(w1, w1, w1, w1, null);

        workflowDAO.get(w1.getId()).setId(newName);

        // perform a getWorkflows()
        mockMvc.perform(
                put("/rest/executions/" + simulation1.getID())
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
                        .value(ApiException.ApiError.INPUT_FIELD_NOT_VALID.getCode())
                );
    }

    @Test
    public void shouldDeleteWithFilesExecution2() throws Exception {
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, w2, w2, w2, null);

        // perform a getWorkflows() and then delete
        mockMvc.perform(
                        put("/rest/executions/" + simulation2.getID() + "/delete")
                                .contentType("application/json")
                                .content("{\"deleteFiles\":true}")
                                .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldReturn500() throws Exception {
        when(workflowDAO.get(Collections.singletonList(baseUser1.getFullName()), null, null, null, null, null, null)).thenThrow(new RuntimeException("test exception"));

        // perform a getWorkflows() with an undetermined error
        mockMvc.perform(
                        get("/rest/executions").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.GENERIC_API_ERROR.getCode()));
    }

    @Test
    public void shouldReturn400() throws Exception {
        // perform a getWorkflows() with a client error
        mockMvc.perform(
                        put("/rest/executions/whynotthisid").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.GENERIC_API_ERROR.getCode()));
    }


    @Test
    public void shouldGetExecution2Stderr() throws Exception {
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, w2, null);
        when(server.getWorkflowsPath()).thenReturn("src/test/resources/testFolder");

        String testOutput = "blablabla\n";

        mockMvc.perform(get("/rest/executions/" + simulation2.getID() + "/stderr").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.TEXT_CONTENT_TYPE_UTF8))
                .andExpect(content().string(testOutput));
    }

    @Test
    public void shouldGetExecution2Stdout() throws Exception {
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2);
        when(server.getWorkflowsPath()).thenReturn("src/test/resources/testFolder");

        String testOutput = "blablabla\n";

        mockMvc.perform(
                        get("/rest/executions/" + simulation2.getID() + "/stdout").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.TEXT_CONTENT_TYPE_UTF8))
                .andExpect(content().string(testOutput));
    }

    @Test
    public void shouldDeleteWithoutExecution2() throws Exception {
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, w2, w2, w2, null);

        mockMvc.perform(
                        put("/rest/executions/" + simulation2.getID() + "/delete")
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
                        put("/rest/executions/" + simulation1.getID() + "/play")
                                .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.NOT_IMPLEMENTED.getCode()));
    }

    @Test
    public void shouldGetExecution2Results() throws Exception {
        String resultPath = "/root/user/user1/path/to/result.res";

        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, null);
        Output output = new Output(new OutputID("workflowID", resultPath, "processor"), DataType.URI, "port");
        when(outputDAO.get(eq(simulation2.getID()))).thenReturn(Arrays.asList(output), null);

        Mockito.when(server.getDataManagerUsersHome()).thenReturn("/root/user");
        Mockito.when(gridaClient.exist(resultPath)).thenReturn(true);
        Mockito.when(gridaClient.getFolderData(resultPath, true)).thenReturn(Arrays.asList(
                new GridData("result.res", GridData.Type.File, 42, "modifData", "", "", "")));

        mockMvc.perform(
                        get("/rest/executions/" + simulation2.getID() + "/results").with(baseUser1()))
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
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, w2, null);

        mockMvc.perform(
                        put("/rest/executions/" + simulation2.getID() + "/kill").with(baseUser1()))
                .andDo(print());

        verify(webServiceEngine).kill(simulation2.getID());
    }

    @Test
    public void testInitGwendiaExecution() throws Exception
    {
        String appName = "test application", groupName = "testGroup", versionName = "4.2";
        String engineName = "testEngine", engineEndpoint = "endpoint", worflowId = "test-workflow-id";
        Date startDate = new Date();

        configureGwendiaTestApp(appName, groupName, versionName);

        createGroup("testResources", GroupType.RESOURCE);
        createUserInGroups(baseUser1.getEmail(), "", groupName, "testResources");

        ArgumentCaptor<File> workflowFile = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<List<ParameterSweep>> inputsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Workflow> workflowCaptor = ArgumentCaptor.forClass(Workflow.class);

        Mockito.when(server.getVoName()).thenReturn("test-vo-name");
        Mockito.when(server.getServerProxy("test-vo-name")).thenReturn("/path/to/proxy");
        Mockito.when(getWebServiceEngine().launch("/path/to/proxy", null)).thenReturn("full-test-workflow-id", (String) null);
        Mockito.when(getWebServiceEngine().getSimulationId("full-test-workflow-id")).thenReturn(worflowId, (String) null);
        Mockito.when(getWebServiceEngine().getStatus(worflowId)).thenReturn(SimulationStatus.Running, (SimulationStatus) null);
        Mockito.when(getWebServiceEngine().getAddressWS()).thenReturn(engineEndpoint, (String) null);

        Workflow w = new Workflow(worflowId, baseUser1.getFullName(), WorkflowStatus.Running, startDate, null, "Exec test 1", appName, versionName, "", engineName, null);
        when(workflowDAO.get(worflowId)).thenReturn(w, (Workflow) null);

        Execution expectedExecution = new Execution(worflowId, "Exec test 1", appName + "/" + versionName, 0, ExecutionStatus.RUNNING, null, null, startDate.getTime(), null, null);
        expectedExecution.clearReturnedFiles();

        setUpResourceAndEngine(appName, versionName);

        mockMvc.perform(
                        post("/rest/executions").contentType("application/json")
                                .content(getResourceAsString("jsonObjects/execution1.json"))
                                .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$",
                        jsonCorrespondsToExecution(expectedExecution)
                ));

        // verify workflow path
        Mockito.verify(getWebServiceEngine()).setWorkflow(workflowFile.capture());
        Assertions.assertEquals(getGwendiaTestFile().getAbsolutePath(), workflowFile.getValue().getAbsolutePath());

        // verify inputs
        Mockito.verify(getWebServiceEngine()).setInput(inputsCaptor.capture());
        List<ParameterSweep> inputs = inputsCaptor.getValue();
        Assertions.assertEquals(5, inputs.size());
        MatcherAssert.assertThat(inputs, Matchers.containsInAnyOrder(
                both(hasProperty("parameterName", is("testFileInput"))).
                        and(hasProperty("values",  Matchers.contains(ServerMockConfig.TEST_USERS_ROOT + "/" +  baseUser1.getFolder() + "/path/to/input.in"))),
                both(hasProperty("parameterName", is("testTextInput"))).
                        and(hasProperty("values",  Matchers.contains("best test text value"))),
                both(hasProperty("parameterName", is("results-directory"))).
                        and(hasProperty("values",  Matchers.contains(ServerMockConfig.TEST_USERS_ROOT + "/" +  baseUser1.getFolder()))),
                both(hasProperty("parameterName", is("testOptionalTextInput"))).
                        and(hasProperty("values",  Matchers.contains("No_value_provided"))),
                both(hasProperty("parameterName", is("testFlagInput"))).
                        and(hasProperty("values",  Matchers.contains("false")))
        ));

        // verify created workflow
        Mockito.verify(workflowDAO).add(workflowCaptor.capture());
        Workflow workflow = workflowCaptor.getValue();
        Assertions.assertEquals(appName, workflow.getApplication());
        Assertions.assertEquals(versionName, workflow.getApplicationVersion());
        Assertions.assertEquals(worflowId, workflow.getId());
        Assertions.assertEquals(WorkflowStatus.Running, workflow.getStatus());
        Assertions.assertEquals("Exec test 1", workflow.getDescription());
        Assertions.assertEquals(engineEndpoint, workflow.getEngine());
        Assertions.assertEquals(baseUser1.getFullName(), workflow.getUsername());
        Assertions.assertNull(workflow.getFinishedTime());
        MatcherAssert.assertThat(workflow.getStartedTime().getTime(),
                is(both(greaterThan(startDate.getTime())).and(lessThan(new Date().getTime()))));

    }

    // the difference (at the moment) is that with moteurLite the optional and absent parameters are not included
    @Test
    public void testInitBoutiquesExecution() throws Exception
    {
        String appName = "test application", groupName = "testGroup", versionName = "4.2";
        String engineName = "testEngine", engineEndpoint = "endpoint", worflowId = "test-workflow-id";
        Date startDate = new Date();

        configureBoutiquesTestApp(appName, groupName, versionName);

        createGroup("testResources", GroupType.RESOURCE);
        createUserInGroups(baseUser1.getEmail(), "", groupName, "testResources");

        ArgumentCaptor<File> workflowFile = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<List<ParameterSweep>> inputsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Workflow> workflowCaptor = ArgumentCaptor.forClass(Workflow.class);

        Mockito.when(server.useMoteurlite()).thenReturn(true);
        Mockito.when(server.getVoName()).thenReturn("test-vo-name");
        Mockito.when(server.getServerProxy("test-vo-name")).thenReturn("/path/to/proxy");
        Mockito.when(getWebServiceEngine().launch("/path/to/proxy", null)).thenReturn("full-test-workflow-id", (String) null);
        Mockito.when(getWebServiceEngine().getSimulationId("full-test-workflow-id")).thenReturn(worflowId, (String) null);
        Mockito.when(getWebServiceEngine().getStatus(worflowId)).thenReturn(SimulationStatus.Running, (SimulationStatus) null);
        Mockito.when(getWebServiceEngine().getAddressWS()).thenReturn(engineEndpoint, (String) null);

        Workflow w = new Workflow(worflowId, baseUser1.getFullName(), WorkflowStatus.Running, startDate, null, "Exec test 1", appName, versionName, "", engineName, null);
        when(workflowDAO.get(worflowId)).thenReturn(w, (Workflow) null);

        Execution expectedExecution = new Execution(worflowId, "Exec test 1", appName + "/" + versionName, 0, ExecutionStatus.RUNNING, null, null, startDate.getTime(), null, null);
        expectedExecution.clearReturnedFiles();

        setUpResourceAndEngine(appName, versionName);

        mockMvc.perform(
                        post("/rest/executions").contentType("application/json")
                                .content(getResourceAsString("jsonObjects/execution1.json"))
                                .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$",
                        jsonCorrespondsToExecution(expectedExecution)
                ));

        // verify workflow path
        Mockito.verify(getWebServiceEngine()).setWorkflow(workflowFile.capture());
        Assertions.assertEquals(getBoutiquesTestFile().getAbsolutePath(), workflowFile.getValue().getAbsolutePath());

        // verify inputs
        Mockito.verify(getWebServiceEngine()).setInput(inputsCaptor.capture());
        List<ParameterSweep> inputs = inputsCaptor.getValue();
        Assertions.assertEquals(4, inputs.size());
        MatcherAssert.assertThat(inputs, Matchers.containsInAnyOrder(
                both(hasProperty("parameterName", is("testFileInput"))).
                        and(hasProperty("values",  Matchers.contains(ServerMockConfig.TEST_USERS_ROOT + "/" +  baseUser1.getFolder() + "/path/to/input.in"))),
                both(hasProperty("parameterName", is("testTextInput"))).
                        and(hasProperty("values",  Matchers.contains("best test text value"))),
                both(hasProperty("parameterName", is("results-directory"))).
                        and(hasProperty("values",  Matchers.contains(ServerMockConfig.TEST_USERS_ROOT + "/" +  baseUser1.getFolder()))),
                both(hasProperty("parameterName", is("testFlagInput"))).
                        and(hasProperty("values",  Matchers.contains("false")))
        ));

        // verify created workflow
        Mockito.verify(workflowDAO).add(workflowCaptor.capture());
        Workflow workflow = workflowCaptor.getValue();
        Assertions.assertEquals(appName, workflow.getApplication());
        Assertions.assertEquals(versionName, workflow.getApplicationVersion());
        Assertions.assertEquals(worflowId, workflow.getId());
        Assertions.assertEquals(WorkflowStatus.Running, workflow.getStatus());
        Assertions.assertEquals("Exec test 1", workflow.getDescription());
        Assertions.assertEquals(engineEndpoint, workflow.getEngine());
        Assertions.assertEquals(baseUser1.getFullName(), workflow.getUsername());
        Assertions.assertNull(workflow.getFinishedTime());
        MatcherAssert.assertThat(workflow.getStartedTime().getTime(),
                is(both(greaterThan(startDate.getTime())).and(lessThan(new Date().getTime()))));

    }

    public void setUpResourceAndEngine(String appName, String version) throws Exception {
        Engine engine = new Engine("testEngine", "bla", "enabled");
        Resource resource = new Resource(
            "testResource", 
            true, 
            true, 
            ResourceType.BATCH, 
            "bla", 
            Arrays.asList(engine.getName()),
            Arrays.asList("testResources"));

        engineBusiness.add(engine);
        resourceBusiness.add(resource);

        resourceBusiness.associate(resource, new AppVersion(appName, version));
    }
}
