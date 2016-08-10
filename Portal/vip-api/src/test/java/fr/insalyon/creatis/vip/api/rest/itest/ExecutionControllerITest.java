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

import fr.insalyon.creatis.vip.api.rest.itest.config.*;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.ResourceUtils;

import java.util.Arrays;

import static fr.insalyon.creatis.vip.api.AppVersionTestUtils.*;
import static fr.insalyon.creatis.vip.api.ApplicationTestUtils.*;
import static fr.insalyon.creatis.vip.api.ClassesTestUtils.*;
import static fr.insalyon.creatis.vip.api.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.api.UserTestUtils.baseUser1;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/20/16.
 *
 * Test method on platform path
 */
public class ExecutionControllerITest extends BaseVIPSpringITest {

    @Test
    @WithMockUser
    public void testInitExecution() throws Exception {
        mockMvc.perform(post("/executions").contentType("application/json")
                            .content(getResourceAsString("jsonObjects/execution1.json")))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
