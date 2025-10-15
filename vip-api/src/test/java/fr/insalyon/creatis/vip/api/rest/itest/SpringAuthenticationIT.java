package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.tools.spring.ApikeyRequestPostProcessor;
import fr.insalyon.creatis.vip.api.tools.spring.BearerTokenRequestPostProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.api.tools.spring.ApikeyRequestPostProcessor;
import fr.insalyon.creatis.vip.api.tools.spring.BearerTokenRequestPostProcessor;

/**
 * These tests check the authentication with the spring test tools.
 * It requests a wrong url that should be secured and expects a 404 when OK
 * <p>
 * Use common vip spring test configuration ({@link BaseRestApiSpringIT}
 */
public class SpringAuthenticationIT extends BaseRestApiSpringIT {

    @Test
    public void authenticationOK() throws Exception {
        createUserWithPassword(emailUser2, "coucou");
        String apikey = getConfigurationBusiness().generateNewUserApikey(emailUser2);
        mockMvc.perform(get("/rest/wrongUrl")
                .with(ApikeyRequestPostProcessor.apikey("testapikey", apikey)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Basic is not supported anymore
     */
    @Test
    public void authenticationWithBasicShouldBeKo() throws Exception {
        createUserWithPassword(emailUser2, "coucou");
        mockMvc.perform(get("/rest/wrongUrl")
                .with(httpBasic(emailUser2, "coucou")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.INSUFFICIENT_AUTH.getCode()));
    }

    @Test
    public void authenticationWithWrongApikey() throws Exception {
        mockMvc.perform(get("/rest/wrongUrl")
                .with(ApikeyRequestPostProcessor.apikey("testapikey", "WRONG")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.BAD_CREDENTIALS.getCode()));
    }

    @Test
    public void authenticationWithoutCredentials() throws Exception {
        mockMvc.perform(get("/rest/wrongUrl"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.INSUFFICIENT_AUTH.getCode()));
    }

    @Test
    public void authenticationWithInvalidBearerToken() throws Exception {
        mockMvc.perform(get("/rest/wrongUrl")
                        .with(new BearerTokenRequestPostProcessor("invalidToken")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.INSUFFICIENT_AUTH.getCode()));
    }

    @Test
    public void authenticationWithValidBearerToken() throws Exception {
        mockMvc.perform(get("/rest/wrongUrl")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
