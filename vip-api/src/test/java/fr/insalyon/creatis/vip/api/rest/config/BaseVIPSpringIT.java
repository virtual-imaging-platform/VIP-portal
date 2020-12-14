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
package fr.insalyon.creatis.vip.api.rest.config;

import fr.insalyon.creatis.vip.api.rest.mockconfig.DataConfigurator;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import static fr.insalyon.creatis.vip.api.CarminProperties.*;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.*;

/**
 * Created by abonnet on 7/28/16.
 *
 * Base test config that allow spring integration testing for vip API
 *
 * The spring test tools allow to simulate the http layer but everything else
 * is the same as production configuration
 *
 * To authenticate, tests should either
 * * login via with httpbasic(user, password)
 * * use {@link WithMockUser} annotation
 *
 * The interaction with VIP outside vip-api are mocked (see {@link SpringTestConfig} )
 * TODO : after spring is now everywhere, this does not work anymore, work needed here
 */
@SpringJUnitWebConfig(classes = SpringTestConfig.class)
@TestPropertySource(properties = {
        PLATFORM_NAME + "=" + TEST_PLATFORM_NAME,
        PLATFORM_DESCRIPTION + "=" + TEST_PLATFORM_DESCRIPTION,
        PLATFORM_EMAIL + "=" + TEST_PLATFORM_EMAIL,
        SUPPORTED_TRANSFER_PROTOCOLS + "=" + TEST_SUPPORTED_TRANSFER_PROTOCOLS_STRING,
        SUPPORTED_MODULES + "=" + TEST_SUPPORTED_MODULES_STRING,
        DEFAULT_LIMIT_LIST_EXECUTION + "=" + TEST_DEFAULT_LIST_LIMIT,
        UNSUPPORTED_METHODS + "=" + TEST_UNSUPPORTED_METHODS_STRING,
        SUPPORTED_API_VERSION + "=" + TEST_SUPPORTED_API_VERSION,
        APIKEY_HEADER_NAME + "=" + TEST_APIKEY_HEADER,
        APIKEY_GENERATE_NEW_EACH_TIME + "=" + TEST_GENERATE_NEW_APIKEY_EACH_TIME,
        API_DIRECTORY_MIME_TYPE + "=" + TEST_DIR_MIMETYPE,
        API_DEFAULT_MIME_TYPE + "=" + TEST_DEFAULT_MIMETYPE,
        API_DOWNLOAD_RETRY_IN_SECONDS + "=" + Test_DATA_DOWNLOAD_RETRY,
        API_DOWNLOAD_TIMEOUT_IN_SECONDS + "=" + TEST_DATA_DOWNLOAD_TIMEOUT,
        API_DATA_TRANSFERT_MAX_SIZE + "=" + TEST_DATA_MAX_SIZE,
        API_PIPELINE_WHITE_LIST + "=" + TEST_PIPELINE_WHITELIST
})
abstract public class BaseVIPSpringIT {

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
    @Autowired
    protected ResourceLoader resourceLoader;
    @Autowired
    protected UserDAO testUserDAO;
    @Autowired
    protected ConfigurationBusiness configurationBusiness;
    @Autowired
    protected WorkflowBusiness workflowBusiness;
    @Autowired
    protected ApplicationBusiness applicationBusiness;
    @Autowired
    protected ClassBusiness classBusiness;
    @Autowired
    protected TransferPoolBusiness transferPoolBusiness;
    @Autowired
    protected SimulationBusiness simulationBusiness;
    @Autowired
    protected LFCBusiness lfcBusiness;
    @Autowired
    protected LFCPermissionBusiness lfcPermissionBusiness;

    @BeforeAll
    public static void setupEnvVariables() throws Exception {
        String fakeHomePath = Paths.get(ClassLoader.getSystemResource("TestHome").toURI())
                .toAbsolutePath().toString();
        setEnv(Collections.singletonMap("HOME", fakeHomePath));
    }

    /* hack from :
     * https://stackoverflow.com/a/7201825
     */
    public static void setEnv(Map<String, String> newenv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for(Class cl : classes) {
                if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }

    @BeforeEach
    public final void setup() throws URISyntaxException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/rest"))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        Mockito.reset(testUserDAO, configurationBusiness, workflowBusiness, applicationBusiness,
                classBusiness, transferPoolBusiness, simulationBusiness,
                lfcBusiness);
    }

    protected String getResourceAsString(String pathFromClasspath) throws IOException {
        Resource resource = getResourceFromClasspath(pathFromClasspath);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    protected Resource getResourceFromClasspath(String pathFromClasspath) {
        return resourceLoader.getResource("classpath:"+pathFromClasspath);
    }

    public WebApplicationContext getWac() {
        return wac;
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public UserDAO getUserDAO() {
        return testUserDAO;
    }

    public ConfigurationBusiness getConfigurationBusiness() {
        return configurationBusiness;
    }

    public WorkflowBusiness getWorkflowBusiness() {
        return workflowBusiness;
    }

    public ApplicationBusiness getApplicationBusiness() {
        return applicationBusiness;
    }

    public ClassBusiness getClassBusiness() {
        return classBusiness;
    }

    public TransferPoolBusiness getTransferPoolBusiness() {
        return transferPoolBusiness;
    }

    public SimulationBusiness getSimulationBusiness() {
        return simulationBusiness;
    }

    public LFCBusiness getLfcBusiness() {
        return lfcBusiness;
    }

    public LFCPermissionBusiness lfcPermissionBusiness() {return lfcPermissionBusiness;}

    protected void configureDataFS() throws BusinessException {
        DataConfigurator.configureFS(this);
    }
}
