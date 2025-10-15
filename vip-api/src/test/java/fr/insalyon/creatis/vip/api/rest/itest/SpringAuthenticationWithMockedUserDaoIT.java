package fr.insalyon.creatis.vip.api.rest.itest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.api.tools.spring.ApikeyRequestPostProcessor;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;

/**
 * These tests check the authentication with the spring test tools.
 * It requests a wrong url that should be secured and expects a 404 when OK
 * <p>
 * Use common vip spring test configuration ({@link BaseWebSpringIT}
 */
@ContextConfiguration(classes = SpringAuthenticationWithMockedUserDaoIT.TestContextConfiguration.class)
public class SpringAuthenticationWithMockedUserDaoIT extends BaseWebSpringIT {

    static class TestContextConfiguration {
        @Bean
        @Primary
        public UserDAO mockUser() {
            return Mockito.mock(UserDAO.class);
        }
    }

    /**
     * Test that an unexpected runtime exception ends up with a clean AuthenticationError
     */
    @Test
    public void authenticationWithCoreKo() throws Exception {
        Mockito.when(getUserDAO().getUserByApikey("apikeyvalue"))
                .thenThrow(new RuntimeException("hey hey"));
        mockMvc.perform(get("/rest/wrongUrl")
                .with(ApikeyRequestPostProcessor.apikey("testapikey", "apikeyvalue")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(DefaultError.AUTHENTICATION_ERROR.getCode()));
    }
}
