package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static fr.insalyon.creatis.vip.api.data.AuthenticationInfoTestUtils.jsonCorrespondsToAuthenticationInfo;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_APIKEY_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthenticationControllerIT extends BaseWebSpringIT {

    @Test
    public void badPasswordAuthentication() throws Exception {
        createUser(emailUser2);
        mockMvc.perform(
                        post("/rest/authenticate")
                                .contentType("application/json")
                                .content(getResourceAsString("jsonObjects/user-credentials.json")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.BAD_CREDENTIALS.getCode()));;
    }

    @Test
    public void okAuthentication() throws Exception {
        createUserWithPassword(emailUser2, "coucou");
        String apikey = getConfigurationBusiness().generateNewUserApikey(emailUser2);

        mockMvc.perform(
                post("/rest/authenticate")
                        .contentType("application/json")
                        .content(getResourceAsString("jsonObjects/user-credentials.json")))
                .andDo(print())
                .andExpect(jsonPath(
                        "$",
                        jsonCorrespondsToAuthenticationInfo(TEST_APIKEY_HEADER, apikey)))
                .andExpect(status().isOk());
    }

    @Test
    public void missingInfoAuthentication() throws Exception {
        mockMvc.perform(
                post("/rest/authenticate")
                        .contentType("application/json")
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.INPUT_FIELD_NOT_VALID.getCode()));;
    }

}
