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
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class CorsRestApiIT extends BaseRestApiSpringIT {

    // GLOBAL note : preflight request does not have credentials
    // preflight = OPTIONS + "Origin" header + "Access-Control-Request-Method" header

    /**
     * Axel :
     *
     * How CORS is handled in spring and spring security :
     * - CORS config/exceptions can be configured in the spring MVC config (with WebMvcConfigurer),
     *      this ensures CORS security finely when the request gets to the spring MVC dispatcher,
     *      but with spring security, spring security must not block the request itself
     * - Spring security can get automatically CORS config/exceptions from the MVC config (with WebMvcConfigurer)
     *      BUT it takes it from the ROOT webApplicationContext, not from a child one
     *      (so not from the SpringRestApiConfig in our case)
     *      In VIP, when the context was split from 1 big single context to 1 ROOT and 2 children context,
     *      CORS was then misconfigured because without the proper config, spring security let the GET/POST/ETC requests
     *      go on but blocks the preflight (OPTION) ones.
     *      Preflight requests are mandatory in browsers, so this makes CORS fail
     *
     * The easiest solution I found is to make custom CORS configurations in the spring security filter chain,
     * let spring security handle everything and not configure anything in spring MVC.
     * So :
     * - The "/rest" filter chain has the CORS config with exceptions
     * - The other filter chains ("/internal" and "/") has the default security (Blocks preflight but let the rest)
     *   This should not be strict because in the case where the web server is behind a reverse proxy, spring will
     *   interpret
     *
     * see @{@link fr.insalyon.creatis.vip.core.server.security.RestApiSecurityConfig}
     * see @{@link fr.insalyon.creatis.vip.core.server.security.GeneralSecurityConfig}
     */

    public String apikey;

    /**
     * insert a user with a valid and known api key
     */
    @BeforeEach
    public void setUpTestUser() throws BusinessException, GRIDAClientException {
        createUserWithPassword(emailUser2, "coucou");
        apikey = getConfigurationBusiness().generateNewUserApikey(emailUser2);
    }

    // without an Origin header (not CORS), with a valid api key, GET is ok
    @Test
    public void testGetWithoutOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rest/pipelines")
                        .with(ApikeyRequestPostProcessor.apikey("testapikey", apikey)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // without an Origin header (not CORS), with a wrong api key, GET is 401
    @Test
    public void testGetWithBadApiKeyWithoutOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rest/pipelines")
                        .with(ApikeyRequestPostProcessor.apikey("testapikey", "WRONGAPIKEY")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // without an Origin header and without credential, OPTION is not a preflight, so not CORS, and is rejected
    @Test
    public void testPreflightWithoutOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .options("/rest/pipelines"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // with a bad origin, GET should be forbidden by CORS
    @Test
    public void testCORSGetWithBadOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rest/pipelines")
                        .header("Origin", "https://bad-url.com")
                        .with(ApikeyRequestPostProcessor.apikey("testapikey", apikey)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // with a bad origin, preflight OPTION should be forbidden by CORS
    @Test
    public void testCORSPreflightWithBadOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .options("/rest/pipelines")
                        .header("Origin", "https://bad-url.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // with an allowed origin an valid api key, GET should be OK
    @Test
    public void testCORSGetWithOriginOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rest/pipelines")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .with(ApikeyRequestPostProcessor.apikey("testapikey", apikey)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", ServerMockConfig.TEST_CORS_URL));
    }

    // with an allowed origin, preflight OPTION should be ok and return CORS info
    // a preflight specify the HTTP method to check, so we test each one
    public void testCORSPreflightWithOriginOk(String method) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .options("/rest/pipelines")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .header("Access-Control-Request-Method", method))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", ServerMockConfig.TEST_CORS_URL))
                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Methods", method));
    }


    @Test
    public void testCORSPreflightGETWithOriginOk() throws Exception {
        testCORSPreflightWithOriginOk("GET");
    }

    @Test
    public void testCORSPreflightPOSTWithOriginOk() throws Exception {
        testCORSPreflightWithOriginOk("POST");
    }

    @Test
    public void testCORSPreflightPUTWithOriginOk() throws Exception {
        testCORSPreflightWithOriginOk("PUT");
    }

    @Test
    public void testCORSPreflightDELETEWithOriginOk() throws Exception {
        testCORSPreflightWithOriginOk("DELETE");
    }

    // If the url does not begin with /rest, there should not be an exception, but with the default behavior only
    // preflight are blocked.
    // Here the request should go through CORS checks, but fails on spring security because /internal/*** is secured
    // by cookies
    // we build another MockMvc with a servlet path different than "/rest" to test that
    // This test uses the non-secured /platform endpoint.
    // With a secured endpoint (actually an endpoint needed a connected user), the user will be null (and will trigger
    // a NPE) as the rest spring security chain won't be used (because mapped on /rest) and so spring won't provide
    // the current user
    // Also note that we use the /internal servlet path to also ensure the CORS config of /internal endpoints in spring
    // security. Actually, the /internal controllers are not loaded in this test context, only the /rest ones.
    @Test
    public void testCORSGetWithOriginOkButOnInternalApi() throws Exception {
        buildMockMvc("internal").perform(MockMvcRequestBuilders
                        .get("/internal/platform")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .with(ApikeyRequestPostProcessor.apikey("testapikey", apikey)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // Same thing with a servlet path different than /rest or /internal
    // not a preflight, so OK
    @Test
    public void testCORSGetWithOriginOkButNonRestApi() throws Exception {
        buildMockMvc("testservletpath").perform(MockMvcRequestBuilders
                        .get("/testservletpath/platform")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .with(ApikeyRequestPostProcessor.apikey("testapikey", apikey)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // Same thing but verify it is ok without CORS
    @Test
    public void testGetWithOriginOkButNonRestApi() throws Exception {
        buildMockMvc("testservletpath").perform(MockMvcRequestBuilders
                        .get("/testservletpath/platform"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // if the url does not begin with /rest, there should not be an exception for preflight
    // must use the non-secured /platform endpoint, see previous test why
    @Test
    public void testCORSPreflightWithOriginOkButOnInternalApi() throws Exception {
        buildMockMvc("internal").perform(MockMvcRequestBuilders
                        .options("/internal/platform")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .header("Access-Control-Request-Method", "GET"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

}
