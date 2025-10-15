package fr.insalyon.creatis.vip.api.rest.itest;

import static fr.insalyon.creatis.vip.api.data.AuthenticationInfoTestUtils.jsonCorrespondsToAuthenticationInfo;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_APIKEY_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.core.client.DefaultError;

public class AuthenticationControllerIT extends BaseRestApiSpringIT {

    @Test
    public void badPasswordAuthentication() throws Exception {
        createUser(emailUser2);
        mockMvc.perform(
                        post("/rest/authenticate")
                                .contentType("application/json")
                                .content(getResourceAsString("jsonObjects/user-credentials.json")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(DefaultError.BAD_CREDENTIALS.getCode()));;
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
                        .value(DefaultError.BAD_INPUT_FIELD.getCode()));;
    }

}
