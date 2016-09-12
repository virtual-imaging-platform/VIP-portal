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

import fr.insalyon.creatis.vip.api.business.ApiUtils;
import fr.insalyon.creatis.vip.api.rest.RestErrorCodes;
import fr.insalyon.creatis.vip.api.rest.itest.config.*;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.hp.hpl.jena.util.iterator.Filter.any;
import static fr.insalyon.creatis.vip.api.AppVersionTestUtils.*;
import static fr.insalyon.creatis.vip.api.AppVersionTestUtils.version42;
import static fr.insalyon.creatis.vip.api.ApplicationTestUtils.*;
import static fr.insalyon.creatis.vip.api.ClassesTestUtils.class1;
import static fr.insalyon.creatis.vip.api.ClassesTestUtils.class2;
import static fr.insalyon.creatis.vip.api.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.api.UserTestUtils.*;
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
 * <p>
 * Test methods on pipeline path
 * <p>
 * Include 2 tests on error handling
 */
public class PipelineControllerITest extends BaseVIPSpringITest {

    @Test
    public void pipelineMethodShouldBeSecured() throws Exception {
        mockMvc.perform(get("/pipelines"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnErrorOnAPIException() throws Exception {
        when(classBusiness.getUserClasses(eq(baseUser1.getEmail()), anyBoolean()))
                .thenThrow(new BusinessException("test exception"));
        mockMvc.perform(get("/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code").value(RestErrorCodes.API_ERROR.getCode()));
    }

    @Test
    public void shouldReturnErrorOnUnexpectedException() throws Exception {
        when(classBusiness.getUserClasses(eq(baseUser1.getEmail()), anyBoolean()))
                .thenThrow(new RuntimeException("test exception"));
        mockMvc.perform(get("/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code").value(RestErrorCodes.UNEXPECTED_ERROR.getCode()));
    }

    @Test
    public void shouldReturnPipelines() throws Exception {
        configureApplications(baseUser1, Arrays.asList(class1, class2),
                app1, version42,
                app2, version01,
                app3, version42, version01);
        mockMvc.perform(get("/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(4)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        mapCorrespondsToPipeline(getPipeline(app1, version42)),
                        mapCorrespondsToPipeline(getPipeline(app2, version01)),
                        mapCorrespondsToPipeline(getPipeline(app3, version01)),
                        mapCorrespondsToPipeline(getPipeline(app3, version42)))));
    }

    @Test
    public void userGetAPipeline() throws Exception {
        configureApplications(baseUser1, Arrays.asList(class1, class2),
                app2, version42);
        String pipelineId = configureAnApplication(baseUser1, app2, version42, 0, 1);
        mockMvc.perform(get("/pipelines").param("pipelineId", pipelineId)
                .with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$", mapCorrespondsToPipeline(getFullPipeline(app2, version42, "desc test", 0, 1))));
    }

    // UTILS

    /**
     * Should take only Application and AppVersion classes in the sequence :
     * Application AppVersion+
     * (an application followed by one or more version)
     */
    private void configureApplications(User user, List<AppClass> classes, Object... args) throws BusinessException {
        // parse the args to map the application to the versions
        Application currentApplication = null;
        Map<Application, List<AppVersion>> applicationVersions = new HashMap<>();
        for (Object currentArg : args) {
            if (currentArg instanceof Application) {
                currentApplication = (Application) currentArg;
                applicationVersions.put(currentApplication, new ArrayList<>());
            } else {
                applicationVersions.get(currentApplication).
                        add(getVersion((AppVersion) currentArg, currentApplication));
            }
        }
        List<String> classNames = classes.stream().map(AppClass::getName).collect(Collectors.toList());
        // verify the classes given are good
        Predicate<Application> isAppNotOK =
                application -> Collections.disjoint(application.getApplicationClasses(), classNames);
        if (applicationVersions.keySet().stream().anyMatch(isAppNotOK)) {
            throw new RuntimeException("misconfiguration of test class>app config");
        }
        // configure mocks
        // 1 return user classes
        when(classBusiness.getUserClasses(user.getEmail(), false)).thenReturn(classes);
        when(classBusiness.getUserClassesName(user.getEmail(), false)).thenReturn(classNames);
        // 2 return apps for the classes
        when(applicationBusiness.getApplications(anyListOf(String.class))).
                thenReturn(new ArrayList<>(applicationVersions.keySet()));
        // 3 return versions for each app
        for (Application app : applicationVersions.keySet()) {
            when(applicationBusiness.getVersions(app.getName())).
                    thenReturn(applicationVersions.get(app));
            when(applicationBusiness.getApplication(app.getName()))
                    .thenReturn(app);
        }
    }

    private String configureAnApplication(
            User user, Application app, AppVersion version,
            Integer... appParamsIndexes) throws BusinessException {
        when(workflowBusiness.getApplicationDescriptor(user, app.getName(), version.getVersion()))
                .thenReturn(getDescriptor("desc test", appParamsIndexes));
        return ApiUtils.getPipelineIdentifier(app.getName(), version.getVersion());
    }
}
