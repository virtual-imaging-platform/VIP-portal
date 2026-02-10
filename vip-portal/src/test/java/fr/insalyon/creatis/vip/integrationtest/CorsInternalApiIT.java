package fr.insalyon.creatis.vip.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import jakarta.servlet.http.Cookie;

public class CorsInternalApiIT extends BaseInternalApiSpringIT {

    /**
     * See CorsRestApiIT for information on CORS config and testing in VIP
     *
     * Here, it is just to verify that CORS is configured with the default stuff (block preflight, rest is ok),
     * because the exceptions must only be allowed on /rest
     */

    public String sessionCode;

    @BeforeEach
    public void setUpTestUser() throws VipException, GRIDAClientException, DAOException {
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

    // with a bad origin, GET is ok (net a preflight)
    @Test
    public void testCORSGetWithBadOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/internal/session")
                        .header("Origin", "https://bad-url.com")
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, sessionCode)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
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

    // with an allowed origin for /rest, GET should be also OK for /internal (not a preflight)
    @Test
    public void testCORSGetWithOriginOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/internal/session")
                        .header("Origin", ServerMockConfig.TEST_CORS_URL)
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, sessionCode)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
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
