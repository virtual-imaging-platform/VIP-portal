package fr.insalyon.creatis.vip.core.integrationtest.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.SpringInternalApiConfig;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;

@ContextConfiguration(classes = { SpringInternalApiConfig.class })
public class SessionControllerIT extends BaseSpringIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserDAO userDAO;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/internal"))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    public void getSession() throws Exception {
        createUser(emailUser1);
        userDAO.updateSession(emailUser1, "super_session");
        user1 = userDAO.getUser(emailUser1);

        // not connected
        mockMvc.perform(get("/internal/session"))
                .andExpect(status().isUnauthorized());

        // with user connected
        mockMvc.perform(get("/internal/session").with(getUserSecurityMock(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("super_session"))    
                .andExpect(jsonPath("$.email").value(emailUser1))
                .andExpect(jsonPath("$.password").value(nullValue()))
                .andExpect(jsonPath("$.userlevel").value(UserLevel.Beginner.toString()));
    }

    @Test
    public void createSession() throws Exception {
        AuthenticationCredentials credentials = new AuthenticationCredentials();
        credentials.setUsername(emailUser1);
        credentials.setPassword("wrongPassword");

        user1 = createUser(emailUser1);

        // connection with wrong credentials
        mockMvc.perform(post("/internal/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(credentials)))
            .andExpect(status().isBadRequest())
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_USER))
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_SESSION));

        // connection with good password
        credentials.setPassword("testPassword");
        MvcResult result = mockMvc.perform(post("/internal/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(credentials)))
            .andExpect(status().isOk())
            // this check that authentication context was properly set since we use the getSession()
            // method just after need
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.email").value(emailUser1))
            .andExpect(jsonPath("$.password").value(nullValue()))
            .andExpect(jsonPath("$.userlevel").value(UserLevel.Beginner.toString()))
            .andExpect(cookie().exists(CoreConstants.COOKIES_USER))
            .andExpect(cookie().exists(CoreConstants.COOKIES_SESSION))
            .andReturn();

        // try to retrieve session using the cookies generated
        mockMvc.perform(get("/internal/session")
            .cookie(result.getResponse().getCookies()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())    
                .andExpect(jsonPath("$.email").value(emailUser1))
                .andExpect(jsonPath("$.password").value(nullValue()))
                .andExpect(jsonPath("$.userlevel").value(UserLevel.Beginner.toString()));

        // of course without cookies it fails!
        mockMvc.perform(get("/internal/session")).andExpect(status().isUnauthorized());
    }

    @Test
    // This will also check the CSRF Token
    public void deleteSession() throws Exception {
        AuthenticationCredentials credentials = new AuthenticationCredentials();
        credentials.setUsername(emailUser1);
        credentials.setPassword("testPassword");

        user1 = createUser(emailUser1);

        // generate session cookies
        MvcResult result = mockMvc.perform(post("/internal/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(credentials)))
            .andExpect(status().isOk())
            .andReturn();

        // try to destroy session without CSRF Token
        mockMvc.perform(delete("/internal/session")
            .cookie(result.getResponse().getCookies()))
                .andExpect(status().isForbidden());

        // destroy session with CSRF Token
        mockMvc.perform(delete("/internal/session")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .cookie(result.getResponse().getCookies()))
                .andDo(print())
                .andExpect(status().isOk());

        // try to reuse the "destroyed" cookies
        mockMvc.perform(get("/internal/session")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .cookie(result.getResponse().getCookies()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void chainedSessions() throws Exception {
        AuthenticationCredentials credentials = new AuthenticationCredentials();
        credentials.setUsername(emailUser1);
        credentials.setPassword("testPassword");

        user1 = createUser(emailUser1);

        // login user1
        MvcResult result = mockMvc.perform(post("/internal/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(credentials)))
            .andExpect(status().isOk())
            .andReturn();

        // logout user1
        mockMvc.perform(delete("/internal/session")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .cookie(result.getResponse().getCookies()))
                .andExpect(status().isOk());

        user2 = createUser(emailUser2, "2");
        credentials.setUsername(emailUser2);

        // login user2 and check session info
        mockMvc.perform(post("/internal/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(credentials)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(emailUser2));
    }
}
