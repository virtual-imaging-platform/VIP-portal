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
package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.api.rest.config.*;
import fr.insalyon.creatis.vip.api.rest.mockconfig.ApplicationsConfigurator;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import org.hamcrest.Matcher;
import org.junit.*;
import org.mockito.*;

import java.util.*;

import static fr.insalyon.creatis.vip.api.data.AppVersionTestUtils.version42;
import static fr.insalyon.creatis.vip.api.data.ApplicationTestUtils.app1;
import static fr.insalyon.creatis.vip.api.data.ClassesTestUtils.class1;
import static fr.insalyon.creatis.vip.api.data.ExecutionTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.rest.mockconfig.ApplicationsConfigurator.configureAnApplication;
import static fr.insalyon.creatis.vip.api.rest.mockconfig.ApplicationsConfigurator.configureApplications;
import static java.awt.SystemColor.text;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/20/16.
 * <p>
 * Test method on platform path
 */
public class ExecutionControllerITest extends BaseVIPSpringITest {

    // TODO finish tests

    @Test
    public void shouldListExecutions() throws Exception {
        when(workflowBusiness.getSimulations(baseUser1.getFullName(),null,null, null,null,null))
                .thenReturn(Arrays.asList(simulation1, simulation2));
        when(workflowBusiness.getSimulation(simulation1.getID()))
                .thenReturn(simulation1);
        when(workflowBusiness.getSimulation(simulation2.getID()))
                .thenReturn(simulation2);
        mockMvc.perform(
                get("/executions").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("[*]", hasSize(2)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                            jsonCorrespondsToExecution(summariseExecution(execution1)),
                            jsonCorrespondsToExecution(summariseExecution(execution2))
                        )));
    }

    @Test
    public void shouldGetExecution1() throws Exception {
        when(workflowBusiness.getSimulation(simulation1.getID()))
                .thenReturn(simulation1);
        when(workflowBusiness.getInputData(simulation1.getID(), baseUser1.getFolder()))
                .thenReturn(simulation1InData);
        when(workflowBusiness.getOutputData(simulation1.getID(), baseUser1.getFolder()))
                .thenReturn(simulation1OutData);
        mockMvc.perform(
                get("/executions/" + simulation1.getID()).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$",
                        jsonCorrespondsToExecution(execution1)
                ));
    }

    @Test
    public void shouldGetExecution2() throws Exception {
        when(workflowBusiness.getSimulation(simulation2.getID()))
                .thenReturn(simulation2);
        when(workflowBusiness.getInputData(simulation2.getID(), baseUser1.getFolder()))
                .thenReturn(simulation2InData);
        when(workflowBusiness.getOutputData(simulation2.getID(), baseUser1.getFolder()))
                .thenReturn(simulation2OutData);
        mockMvc.perform(
                get("/executions/" + simulation2.getID()).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$",
                        jsonCorrespondsToExecution(execution2)
                ));
    }

    @Test
    public void testInitExecution() throws Exception {
        // configure pipeline access right
        configureApplications(
                this, baseUser1, Collections.singletonList(class1),
                app1, version42);
        // configure pipeline input
        configureAnApplication(this, baseUser1, app1, version42, 0 ,1);
        // configure lauch
        when(workflowBusiness.launch(eq(baseUser1), anyList(), anyMap(), eq(app1.getName()),
                eq(version42.getVersion()), eq(class1.getName()), eq(execution1.getName())))
            .thenReturn(execution1.getIdentifier());
        // configure returne execution
        when(workflowBusiness.getSimulation(execution1.getIdentifier()))
                .thenReturn(simulation1);
        when(workflowBusiness.getInputData(simulation1.getID(), baseUser1.getFolder()))
                .thenReturn(simulation1InData);
        when(workflowBusiness.getOutputData(simulation1.getID(), baseUser1.getFolder()))
                .thenReturn(simulation1OutData);
        // misc config
        when(configurationBusiness.getUserGroups(baseUser1.getEmail()))
                .thenReturn(new HashMap<>());
        mockMvc.perform(
                post("/executions").contentType("application/json")
                        .content(getResourceAsString("jsonObjects/execution1.json"))
                        .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$",
                        jsonCorrespondsToExecution(execution1)
                ));
        ArgumentCaptor<Map> inputCaptor =
                ArgumentCaptor.forClass(Map.class);
        verify(workflowBusiness).launch(eq(baseUser1), anyList(), inputCaptor.capture(), eq(app1.getName()),
                eq(version42.getVersion()), eq(class1.getName()), eq(execution1.getName()));
        Assert.assertEquals(inputCaptor.getValue().size(), 2);
        Assert.<Map<?,?>>assertThat(inputCaptor.getValue(), allOf(
                hasEntry("param 1", "test text"),
                hasEntry("param 2", "/path/test")));
    }
}
