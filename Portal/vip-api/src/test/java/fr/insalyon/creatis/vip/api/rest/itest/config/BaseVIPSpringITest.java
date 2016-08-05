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
package fr.insalyon.creatis.vip.api.rest.itest.config;

import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static fr.insalyon.creatis.vip.api.CarminAPITestConstants.*;
import static fr.insalyon.creatis.vip.api.CarminProperties.*;

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
        SUPPORTED_TRANSFER_PROTOCOLS + "=" + TEST_SUPPORTED_TRANSFER_PROTOCOLS_STRING,
        SUPPORTED_MODULES + "=" + TEST_SUPPORTED_MODULES_STRING,
        DEFAULT_LIMIT_LIST_EXECUTION + "=" + TEST_DEFAULT_LIST_LIMIT,
        UNSUPPORTED_METHODS + "=" + TEST_UNSUPPORTED_METHODS_STRING,
        SUPPORTED_API_VERSION + "=" + TEST_SUPPORTED_API_VERSION
})
abstract public class BaseVIPSpringITest {

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
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

    @Before
    public final void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        Mockito.reset(userDAO, configurationBusiness, workflowBusiness, applicationBusiness,
                classBusiness);
    }
}
