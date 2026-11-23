package fr.insalyon.creatis.vip.core.integrationtest.controller;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.models.User;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * verify the strict spring firewall is used.
 * beware of the spring client mockMvc modifying the request with the encoding
 */
public class FirewallIT extends BaseInternalApiSpringIT {

    private User basicUser;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        basicUser = createUser(emailUser3, UserLevel.Beginner);
    }

    @Test
    public void getUser() throws Exception {

        // ok stuff with double slash
        mockMvc.perform(get("/internal/users//" + basicUser.getId())
                .with(request -> {  // needs to post-process it to avoid mockmvc to encode the uri automatically on build
                    request.setRequestURI("/internal/users//" + basicUser.getId());
                    return request;
                })
                .with(getUserSecurityMock(basicUser))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());


        // ko with encoded stuff
        mockMvc.perform(get("/internal/users//%41")
                        .with(request -> {  // needs to post-process it to avoid mockmvc to encode the uri automatically on build
                            request.setRequestURI("/internal/users//%41");
                            return request;
                        })
                        .with(getUserSecurityMock(basicUser))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(DefaultError.GENERIC_ERROR_WITH_MESSAGE.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("the URL contained a potentially malicious String \"%\"")));

    }

}