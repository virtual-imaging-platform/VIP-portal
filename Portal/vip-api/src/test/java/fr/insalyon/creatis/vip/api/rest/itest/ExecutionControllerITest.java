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

import fr.insalyon.creatis.vip.api.ExecutionTestUtils;
import fr.insalyon.creatis.vip.api.rest.itest.config.*;
import org.junit.Test;

import java.util.Arrays;

import static fr.insalyon.creatis.vip.api.AppVersionTestUtils.*;
import static fr.insalyon.creatis.vip.api.ApplicationTestUtils.*;
import static fr.insalyon.creatis.vip.api.ExecutionTestUtils.*;
import static fr.insalyon.creatis.vip.api.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.api.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.UserTestUtils.baseUser2;
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
                            mapCorrespondsToExecution(summariseExecution(execution1)),
                            mapCorrespondsToExecution(summariseExecution(execution2))
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
                        mapCorrespondsToExecution(execution1)
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
                        mapCorrespondsToExecution(execution2)
                ));
    }

    //@Test
    public void testInitExecution() throws Exception {
        mockMvc.perform(
                post("/executions").contentType("application/json")
                        .content(getResourceAsString("jsonObjects/execution1.json"))
                        .with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
