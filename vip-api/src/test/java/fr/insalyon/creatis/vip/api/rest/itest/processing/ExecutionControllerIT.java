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

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.api.rest.config.RestTestUtils;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.business.simulation.WebServiceEngine;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static fr.insalyon.creatis.vip.api.data.ExecutionTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static org.hamcrest.Matchers.hasSize;
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
    @Autowired
    @Qualifier("mockWorkflowDAO")
    WorkflowDAO workflowDAO;
    @Autowired
    @Qualifier("mockOutputDAO")
    OutputDAO outputDAO;
    @Autowired
    @Qualifier("mockInputDAO")
    InputDAO inputDAO;
    //@Autowired
    //@Qualifier("mockUsersGroupsDAO")
    UsersGroupsDAO usersGroupsDAO;
    //@Autowired
    //@Qualifier("mockApplicationDAO")
    ApplicationDAO applicationDAO;
    @Autowired
    @Qualifier("mockWebServiceEngine")
    WebServiceEngine webServiceEngine;
    //@Autowired
    //@Qualifier("mockGroupDAO")
    GroupDAO groupDAO;

    private Workflow w1;
    private Workflow w2;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(workflowDAO);
        Mockito.reset(outputDAO);
        Mockito.reset(webServiceEngine);
        Mockito.reset(inputDAO);
        //Mockito.reset(usersGroupsDAO);
        //Mockito.reset(applicationDAO);

        w1 = new Workflow(simulation1.getID(), baseUser1.getFullName(), WorkflowStatus.Completed, new Date(), new Date(), "description", "application", "applicationVersion", "applicationClass", "engine", null);
        w2 = new Workflow(simulation2.getID(), baseUser1.getFullName(), WorkflowStatus.Completed, new Date(), new Date(), "description", "application", "applicationVersion", "applicationClass", "engine", null);
    }

    @Test
    public void shouldListExecutions() throws Exception {
        when(workflowDAO.get(eq(simulation1.getID()))).thenReturn(w1, null);
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, null);
        when(workflowDAO.get(eq(baseUser1.getFullName()), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull()))
                .thenReturn(Arrays.asList(w1, w2), null);

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("[*]", hasSize(2)))
                // Check that the returned executions are the good ones
                .andExpect(jsonPath("$[0].status").value("Finished"))
                .andExpect(jsonPath("$[0].identifier").value("execId1"))
                .andExpect(jsonPath("$[1].status").value("Finished"))
                .andExpect(jsonPath("$[1].identifier").value("execId2"));

    }

    @Test
    public void shouldCountExecutions() throws Exception {
        when(workflowDAO.get(eq(baseUser1.getFullName()), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull()))
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
    public void shouldGetErrorWhenGettingUnknownExecution() throws Exception {
        when(workflowDAO.get(argThat(argument -> !simulation1.getID().equals(argument) && !simulation2.getID().equals(argument))))
                .thenAnswer(invocation -> {
                    throw new BusinessException("no test execution");
                });

        // perform a getWorkflows()
        mockMvc.perform(
                        get("/rest/executions/WrongExecId").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.GENERIC_API_ERROR.getCode()));
    }

    @Test
    public void shouldReturnErrorOnUnexpectedException() throws Exception {
        when(workflowDAO.get(argThat(argument -> !simulation1.getID().equals(argument) && !simulation2.getID().equals(argument))))
                .thenAnswer(invocation -> {
                    throw new RuntimeException("TEST RUNTIME EXCEPTION");
                });

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
        when(workflowDAO.get(baseUser1.getFullName(), null, null, null, null, null, null)).thenThrow(new RuntimeException("test exception"));

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
    @Disabled
    public void shouldGetExecution2Results() throws Exception {
        String resultPath = simulation2OutData.get(0).getPath();
        when(workflowDAO.get(eq(simulation2.getID()))).thenReturn(w2, null);
        Output output = new Output(new OutputID("workflowID", resultPath, "processor"), DataType.URI, "port");
        when(outputDAO.get(eq(simulation2.getID()))).thenReturn(Arrays.asList(output), null);
        Group group = new Group("group1", true, true, true);
        configurationBusiness.addGroup(group);
        when(groupDAO.getGroups()).thenReturn(Arrays.asList(group));


        mockMvc.perform(
                        get("/rest/executions/" + simulation2.getID() + "/results").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(1)));
                /*.andExpect(jsonPath("$[0]",
                        PathTestUtils.jsonCorrespondsToPath(PathTestUtils.testFile1PathProperties)));*/
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
    @Disabled
    public void testInitExecution() throws Exception
    {
        // engine test creation
        group1 = new Group("group1", true, true, true);
        configurationBusiness.addGroup(group1);
        List<String> groups = new ArrayList<>();
        groups.add("group1");
        createUserInGroup("test1@test.fr", "suffix1", "group1");
        createUserInGroup("test2@test.fr", "suffix2", "group1");

        // engine test creation
        String engineName = "test engine";
        String engineEndpoint = "test endpoint";
        String engineStatus = "enabled";
        Engine engine = new Engine(engineName, engineEndpoint, engineStatus);
        List<String> engines = new ArrayList<>();
        engines.add("test engine");
        engineBusiness.add(engine);

        // appClass test creation
        AppClass appClass = new AppClass("class1", engines, groups);
        classBusiness.addClass(appClass);
        applicationClasses = new ArrayList<>();
        applicationClasses.add("class1");

        // Application test creation
        Application application = new Application("application 1", applicationClasses, "test1@test.fr", "test1", "citation1");
        applicationBusiness.add(application);

        // AppVersion test creation
        AppVersion version42 = new AppVersion("application 1", "version 4.2", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(version42);

        when(applicationDAO.getApplication("application 1")).thenReturn(application);

        // configure lauch
        when(workflowDAO.get(eq(simulation1.getID()))).thenReturn(w1);
        when(workflowDAO.getNumberOfRunning(baseUser1.getFullName())).thenReturn(1L);
        when(workflowDAO.getRunning()).thenReturn(Arrays.asList(w1));
        when(applicationDAO.getVersion("Application 1", "version 0.0")).thenReturn(version42);
        Input input = new Input(new InputID("workflowId", "jsonObjects/execution1.json", "processor"), DataType.URI);
        when(inputDAO.get(eq(simulation1.getID()))).thenReturn(Arrays.asList(input));
        //when(testLfcPathsBusiness.parseRealDir(anyString(), eq(baseUser1.getFolder()))).thenReturn("path",null);
        Output output = new Output(new OutputID("workflowID", "path", "processor"), DataType.URI, "port");
        when(outputDAO.get(eq(simulation1.getID()))).thenReturn(Arrays.asList(output));
        when(usersGroupsDAO.getUserGroups(eq(baseUser1.getEmail()))).thenReturn(new HashMap<>());

        mockMvc.perform(
                post("/rest/executions").contentType("application/json")
                        .content(getResourceAsString("jsonObjects/execution1.json"))
                        .with(baseUser1()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$",
                jsonCorrespondsToExecution(execution1)
        ));
    }
}
