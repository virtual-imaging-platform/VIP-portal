package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.api.rest.config.BaseVIPSpringIT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegisterUserControllerTest extends BaseVIPSpringIT {

    @Test
    public void registerEndpointShouldBeSecured() throws Exception {
        mockMvc.perform(get("/rest/register"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}