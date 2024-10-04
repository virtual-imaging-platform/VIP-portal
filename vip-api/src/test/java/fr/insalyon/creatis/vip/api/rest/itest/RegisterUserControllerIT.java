package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.api.data.UserTestUtils;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

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
    public void registerEndpointWithAppOk() throws Exception {
        String appName = "testApp", groupName = "testGroup", className = "testClass";
        getConfigurationBusiness().addGroup(new Group(groupName, true, true, true));
        getClassBusiness().addClass(new AppClass(className, Collections.emptyList(), List.of(groupName)));
        getApplicationBusiness().add(new Application(appName, List.of(className), "test citation"));
        UserTestUtils.restUser1.getApplications().add("testApp");
        mockMvc.perform(
                        post("/rest/register").
                                content(asJsonString(UserTestUtils.restUser1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        User u = getConfigurationBusiness().getUserWithGroups(UserTestUtils.restUser1.getEmail());
        Assertions.assertEquals(1, u.getGroups().size());
        Assertions.assertEquals(groupName, u.getGroups().iterator().next().getName());
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