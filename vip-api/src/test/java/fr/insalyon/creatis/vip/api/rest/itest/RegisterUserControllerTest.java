package fr.insalyon.creatis.vip.api.rest.itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.vip.api.data.UserTestUtils;
import fr.insalyon.creatis.vip.api.rest.config.BaseVIPSpringIT;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
class RegisterUserControllerTest extends BaseVIPSpringIT {

    @Test
    public void registerEndpointOK() throws Exception {
        mockMvc.perform(post("/rest/register")
                .content(asJsonString(UserTestUtils.restUser1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}