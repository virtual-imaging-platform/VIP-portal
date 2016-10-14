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

import fr.insalyon.creatis.vip.api.tools.spring.ApikeyRequestPostProcessor;
import fr.insalyon.creatis.vip.api.rest.RestErrorCodes;
import fr.insalyon.creatis.vip.api.rest.config.*;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1Password;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/22/16.
 *
 * These tests check the authentication with the spring test tools.
 * It requests a wrong url that should be secured and expects a 404 when OK
 *
 * Use common vip spring test configuration ({@link BaseVIPSpringITest}
 *
 */
public class AuthenticationITest extends BaseVIPSpringITest {

    @Test
    public void authenticationOK() throws Exception {
        prepareUser1Configuration();
        mockMvc.perform(get("/wrongUrl")
                .with(ApikeyRequestPostProcessor.apikey("apikey", "apikeyvalue")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void authenticationWithCoreKo() throws Exception {
        when(userDAO.getUserByApikey("apikeyvalue"))
                .thenThrow(new RuntimeException("hey hey"));
        mockMvc.perform(get("/wrongUrl")
                .with(ApikeyRequestPostProcessor.apikey("apikey", "apikeyvalue")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code")
                        .value(RestErrorCodes.AUTHENTICATION_ERROR.getCode()));
    }

    @Test
    public void authenticationWithBasicShouldBeKo() throws Exception {
        prepareUser1Configuration();
        mockMvc.perform(get("/wrongUrl")
                .with(httpBasic(baseUser1.getEmail(), baseUser1Password)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code")
                        .value(RestErrorCodes.INSUFFICIENT_AUTH.getCode()));
    }

    @Test
    public void authenticationWithWrongApikey() throws Exception {
        prepareUser1Configuration();
        mockMvc.perform(get("/wrongUrl")
                .with(ApikeyRequestPostProcessor.apikey("apikey", "WRONG")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code")
                        .value(RestErrorCodes.BAD_CREDENTIALS.getCode()));
    }

    @Test
    public void authenticationWithoutCredentials() throws Exception {
        prepareUser1Configuration();
        mockMvc.perform(get("/wrongUrl"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(RestTestUtils.JSON_CONTENT_TYPE_UTF8))
                .andExpect(jsonPath("$.code")
                        .value(RestErrorCodes.INSUFFICIENT_AUTH.getCode()));
    }

    private void prepareUser1Configuration() throws DAOException, BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        when(userDAO.getUserByApikey("apikeyvalue")).thenReturn(baseUser1);
    }

}
