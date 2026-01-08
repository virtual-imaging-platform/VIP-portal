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
package fr.insalyon.creatis.vip.integrationtest;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.api.tools.spring.ApikeyRequestPostProcessor;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

public class CorsInternalApiIT extends BaseInternalApiSpringIT {

    /**
     * See CorsRestApiIt for information on CORS config and testing in VIP
     *
     * Here, it is just to verify that CORS is always blocked on the internal API, because the exceptions must
     * only be allowed on /rest
     */

    public String sessionCode;

    @BeforeEach
    public void setUpTestUser() throws BusinessException, GRIDAClientException, DAOException {
        createUserWithPassword(emailUser2, "coucou");
        sessionCode = getConfigurationBusiness().getUserWithSession(emailUser2).getSession();
    }

    // without an Origin header, GET is ok
    @Test
    public void testGetWithoutOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/internal/session")
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, sessionCode)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // without an Origin header, with a wrong api code, GET is 401
    @Test
    public void testGetWithBadApiKeyWithoutOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/internal/session")
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, "wrong")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // without an Origin header, OPTION is not really CORS, so not a preflight, and is rejected
    @Test
    public void testPreflightWithoutOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .options("/internal/session"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // with a bad origin, GET should be forbidden
    @Test
    public void testCORSGetWithBadOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/internal/session")
                        .header("Origin", "https://bad-url.com")
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, sessionCode)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // with a bad origin, preflight OPTION should be forbidden
    @Test
    public void testCORSPreflightWithBadOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .options("/internal/session")
                        .header("Origin", "https://bad-url.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // with an allowed origin for /rest, GET should be KO for /internal
    @Test
    public void testCORSGetWithOriginOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/internal/session")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, sessionCode)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // with an allowed origin for /rest, preflight OPTION should be ko for internal
    @Test
    public void testCORSPreflightWithOriginOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .options("/internal/session")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .header("Access-Control-Request-Method", "GET"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

}
