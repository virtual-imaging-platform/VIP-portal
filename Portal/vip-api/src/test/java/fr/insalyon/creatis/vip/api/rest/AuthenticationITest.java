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
package fr.insalyon.creatis.vip.api.rest;

import fr.insalyon.creatis.vip.api.SpringTestConfig;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static fr.insalyon.creatis.vip.api.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.UserTestUtils.baseUser1Password;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/22/16.
 *
 * These tests check the login authentication with the spring test tools.
 *
 * The spring test tools allow to simulate the http layer but everything else
 * is the same as production configuration
 *
 * The interaction sur VIP outside vip-api are mocked ({@link SpringTestConfig}
 *
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = SpringTestConfig.class)
public class AuthenticationITest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ConfigurationBusiness configurationBusiness;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void authenticationOK() throws Exception {
        when(userDAO.getUser(baseUser1.getEmail())).thenReturn(baseUser1);
        when(configurationBusiness.getUser(baseUser1.getEmail())).thenReturn(baseUser1);
        mockMvc.perform(get("/login").with(httpBasic(baseUser1.getEmail(), baseUser1Password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    public void authenticationWithWrongPassport() throws Exception {
        when(userDAO.getUser(baseUser1.getEmail())).thenReturn(baseUser1);
        when(configurationBusiness.getUser(baseUser1.getEmail())).thenReturn(baseUser1);
        mockMvc.perform(get("/login").with(httpBasic(baseUser1.getEmail(), "WRONG")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code")
                        .value(RestErrorCodes.BAD_CREDENTIALS.getCode()));
    }

    @Test
    public void authenticationWithoutCredentials() throws Exception {
        when(userDAO.getUser(baseUser1.getEmail())).thenReturn(baseUser1);
        when(configurationBusiness.getUser(baseUser1.getEmail())).thenReturn(baseUser1);
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code")
                        .value(RestErrorCodes.INSUFFICIENT_AUTH.getCode()));
    }
}
