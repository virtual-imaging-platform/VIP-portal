package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.api.data.UserTestUtils;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegisterUserControllerIT extends BaseWebSpringIT {

    @BeforeEach
    public void reset() {
        UserTestUtils.reset();
    }

    @Test
    public void registerEndpointOk() throws Exception {
        mockMvc.perform(
                        post("/rest/register").
                                content(asJsonString(UserTestUtils.restUser1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        User u = getConfigurationBusiness().getUserWithGroups(UserTestUtils.restUser1.getEmail());
        Assertions.assertEquals(CountryCode.lc, u.getCountryCode());
        Assertions.assertEquals(UserLevel.Beginner, u.getLevel());
        Assertions.assertTrue(u.getGroups().isEmpty());
        Assertions.assertFalse(u.isConfirmed());
        Assertions.assertFalse(u.isAccountLocked());
        Assertions.assertNotNull(getConfigurationBusiness().signin(
                UserTestUtils.restUser1.getEmail(), UserTestUtils.restUser1.getPassword()));
    }

    @Test
    public void registerEndpointValidationFailed() throws Exception {
        UserTestUtils.restUser1.setCountryCode(null);
        mockMvc.perform(
                post("/rest/register") .
                        content(asJsonString(UserTestUtils.restUser1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ApiException.ApiError.INPUT_FIELD_NOT_VALID.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(Matchers.containsString("'countryCode'")));
    }


}