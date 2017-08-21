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
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.server.business.*;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.*;
import static fr.insalyon.creatis.vip.api.CarminProperties.*;
import static fr.insalyon.creatis.vip.core.client.view.util.CountryCode.re;

/**
 * Created by abonnet on 7/28/16.
 *
 * Base test config that allow spring integration testing for vip API
 *
 * The spring test tools allow to simulate the http layer but everything else
 * is the same as production configuration
 *
 * To authenticate, tests should either
 * * login via wirth(httpbasic(user, password)
 * * use {@link WithMockUser} annotation
 *
 * The interaction with VIP outside vip-api are mocked (see {@link SpringTestConfig} )
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = SpringTestConfig.class)
@TestPropertySource(properties = {
        PLATFORM_NAME + "=" + TEST_PLATFORM_NAME,
        PLATFORM_DESCRIPTION + "=" + TEST_PLATFORM_DESCRIPTION,
        PLATFORM_EMAIL + "=" + TEST_PLATFORM_EMAIL,
        SUPPORTED_TRANSFER_PROTOCOLS + "=" + TEST_SUPPORTED_TRANSFER_PROTOCOLS_STRING,
        SUPPORTED_MODULES + "=" + TEST_SUPPORTED_MODULES_STRING,
        DEFAULT_LIMIT_LIST_EXECUTION + "=" + TEST_DEFAULT_LIST_LIMIT,
        UNSUPPORTED_METHODS + "=" + TEST_UNSUPPORTED_METHODS_STRING,
        SUPPORTED_API_VERSION + "=" + TEST_SUPPORTED_API_VERSION,
        IS_KILL_EXECUTION_SUPPORTED + "=" + TEST_IS_KILL_SUPPORTED,
        PLATFORM_ERROR_CODES_AND_MESSAGES + "=" + TEST_ERROR_CODES_AND_MESSAGE_STRING,
        API_URI_PREFIX + "=" + TEST_API_URI_PREFIX,
        API_DEFAULT_MIME_TYPE + "=" + TEST_DEFAULT_MIMETYPE,
        API_DIRECTORY_MIME_TYPE + "=" + TEST_DIR_MIMETYPE,
        API_DOWNLOAD_TIMEOUT_IN_SECONDS + "=" + TEST_DATA_DOWNLOAD_TIMEOUT,
        API_DOWNLOAD_RETRY_IN_SECONDS + "=" + Test_DATA_DOWNLOAD_RETRY,
        API_DATA_TRANSFERT_MAX_SIZE + "=" + TEST_DATA_MAX_SIZE,
        APIKEY_HEADER_NAME + "=" + TEST_APIKEY_HEADER,
        APIKEY_GENERATE_NEW_EACH_TIME + "=" + TEST_GENERATE_NEW_APIKEY_EACH_TIME
})
abstract public class BaseVIPSpringIT {

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
    @Autowired
    protected ResourceLoader resourceLoader;
    @Autowired
    protected UserDAO userDAO;
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

    @Before
    public final void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/rest"))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        Mockito.reset(userDAO, configurationBusiness, workflowBusiness, applicationBusiness,
                classBusiness, transferPoolBusiness, simulationBusiness,
                lfcBusiness);
    }

    protected String getResourceAsString(String pathFromClasspath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:"+pathFromClasspath);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
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
        return userDAO;
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
