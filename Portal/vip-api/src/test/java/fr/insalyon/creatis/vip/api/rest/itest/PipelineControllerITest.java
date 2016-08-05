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

import fr.insalyon.creatis.vip.api.*;
import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.rest.itest.config.*;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.application.client.bean.*;
import org.hamcrest.*;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.*;
import java.util.function.Function;

import static com.hp.hpl.jena.util.iterator.Filter.any;
import static fr.insalyon.creatis.vip.api.AppVersionTestUtils.*;
import static fr.insalyon.creatis.vip.api.AppVersionTestUtils.version42;
import static fr.insalyon.creatis.vip.api.ApplicationTestUtils.*;
import static fr.insalyon.creatis.vip.api.CarminAPITestConstants.*;
import static fr.insalyon.creatis.vip.api.ClassesTestUtils.class1;
import static fr.insalyon.creatis.vip.api.ClassesTestUtils.class2;
import static fr.insalyon.creatis.vip.api.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.api.UserTestUtils.*;
import static fr.insalyon.creatis.vip.api.rest.itest.MapHasSamePropertyAs.*;
import static java.util.Collections.*;
import static net.jcores.CoreKeeper.$;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/20/16.
 *
 * Test method on platform path
 */
public class PipelineControllerITest extends BaseVIPSpringITest {

    @Test
    public void pipelineMethodShouldBeSecured() throws Exception {
        mockMvc.perform(get("/pipelines"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnPipelines() throws Exception {
        when(classBusiness.getUserClasses(eq(baseUser1.getEmail()), anyBoolean()))
                .thenReturn(Arrays.asList(class1, class2));
        when(applicationBusiness.getApplications(anyListOf(String.class)))
                .thenReturn(Arrays.asList(app1, app2, app3));
        when(applicationBusiness.getVersions(app1.getName()))
                .thenReturn(singletonList(getVersion(version42, app1)));
        when(applicationBusiness.getVersions(app2.getName()))
                .thenReturn(singletonList(getVersion(version01, app2)));
        when(applicationBusiness.getVersions(app3.getName()))
                .thenReturn(Arrays.asList(getVersion(version42, app3), getVersion(version01, app3)));
        mockMvc.perform(get("/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(4)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        mapHasSamePropertyAs(getPipeline(app1, version42)),
                        mapHasSamePropertyAs(getPipeline(app2, version01)),
                        mapHasSamePropertyAs(getPipeline(app3, version01)),
                        mapHasSamePropertyAs(getPipeline(app3, version42)))));
    }
}
