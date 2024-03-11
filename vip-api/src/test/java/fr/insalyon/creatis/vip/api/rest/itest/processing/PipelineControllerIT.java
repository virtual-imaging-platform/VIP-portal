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

import fr.insalyon.creatis.vip.api.data.ClassesTestUtils;
import fr.insalyon.creatis.vip.api.data.UserTestUtils;
import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.ClassDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static fr.insalyon.creatis.vip.api.data.AppVersionTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.ApplicationTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.rest.mockconfig.ApplicationsConfigurator.configureAnApplication;
import static fr.insalyon.creatis.vip.api.rest.mockconfig.ApplicationsConfigurator.configureApplications;
import static fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError.WRONG_APPLICATION_DESCRIPTOR;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/20/16.
 * <p>
 * Test methods on pipeline path
 * <p>
 * Include 2 tests on error handling
 */
@Disabled
public class PipelineControllerIT extends BaseWebSpringIT {

    @Autowired
    @Qualifier("mockClassDAO")
    ClassDAO classDAO;
    @Autowired
    @Qualifier("mockApplicationDAO")
    ApplicationDAO applicationDAO;
    @Autowired
    @Qualifier("mockDataManagerBusiness")
    DataManagerBusiness dataManagerBusiness;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(classDAO);
        Mockito.reset(applicationDAO);
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
        /*configureApplications(this, baseUser1, Arrays.asList(class1, class2),
                app2, version42);*/

        String pipelineId = app2.getName() + "/" + version42.getVersion();
        when(workflowBusiness.getApplicationDescriptor(
                eq(baseUser1), eq(app2.getName()), eq(version42.getVersion())))
                .thenThrow(new BusinessException(WRONG_APPLICATION_DESCRIPTOR, pipelineId));
        mockMvc.perform(get("/rest/pipelines/" + pipelineId).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(WRONG_APPLICATION_DESCRIPTOR.getCode()));
    }

    @Test
    public void userGetAPipelineWithPathParameterNonEncoded() throws Exception {
        /*configureApplications(this, baseUser1, Arrays.asList(class1, class2),
                app2, version42);*/

        String pipelineId = configureAnApplication(this, baseUser1, app2, version42, 0, 1);
        mockMvc.perform(get("/rest/pipelines/" + pipelineId)
                        .with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(getFullPipeline(app2, version42, "desc test", 0, 1))));
    }

    @Test
    public void shouldReturnPipelines() throws Exception {

        AppClass appClass = new AppClass("appClass", new ArrayList<>());
        classBusiness.addClass(appClass);

        createUser("test1@test.fr");

        Application app1 = new Application("app1", Arrays.asList(appClass.getName()), "test1@test.fr", "test1", "citation1");
        Application app2 = new Application("app2", Arrays.asList(appClass.getName()), "test1@test.fr", "test1", "citation1");
        Application app3 = new Application("app3", Arrays.asList(appClass.getName()), "test1@test.fr", "test1", "citation1");
        applicationBusiness.add(app1);
        applicationBusiness.add(app2);
        applicationBusiness.add(app3);

        AppVersion version42App1 = new AppVersion("app1", "version42", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(version42App1);

        AppVersion version42App3 = new AppVersion("app3", "version42", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(version42App3);

        AppVersion version01App2= new AppVersion("app2", "version01", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(version01App2);

        AppVersion version01App3= new AppVersion("app3", "version01", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(version01App3);

        AppVersion version43App3= new AppVersion("app3", "version43", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(version43App3);


        ResultActions result = mockMvc.perform(get("/rest/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        jsonCorrespondsToPipeline(getPipeline(app1, version42)),
                        jsonCorrespondsToPipeline(getPipeline(app2, version01)),
                        jsonCorrespondsToPipeline(getPipeline(app3, version01)),
                        jsonCorrespondsToPipeline(getPipeline(app3, version42)),
                        jsonCorrespondsToPipeline(getPipeline(app3, version43)))));

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println("Results : "+content);
    }

    @Test
    public void userGetAPipelineWithQueryParameter() throws Exception {
        /*configureApplications(this, baseUser1, Arrays.asList(class1, class2),
                app2, version42);*/

        String pipelineId = configureAnApplication(this, baseUser1, app2, version42, 0, 1);
        mockMvc.perform(get("/rest/pipelines").param("pipelineId", pipelineId)
                .with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(getFullPipeline(app2, version42, "desc test", 0, 1))));
    }
}
