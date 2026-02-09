package fr.insalyon.creatis.vip.core.integrationtest.controller;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SessionControllerSecurityIT extends BaseInternalApiSpringIT {

    private static String VALID_SESSION = "super_session";

    @BeforeEach
    protected void setUpUser() throws Exception {
        createUser(emailUser1);
        userDAO.updateSession(emailUser1, "super_session");
        user1 = userDAO.getUser(emailUser1);
    }

    // GET with no cookie -> 401
    // GET with invalid cookie -> 401
    // GET with valid cookie -> 200
    @Test
    public void getSession() throws Exception {
        // not cookie
        mockMvc.perform(get("/internal/session"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("8003"));

        // invalid cookie
        mockMvc.perform(get("/internal/session")
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, "WRONGCookie")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("8003"));

        // with valid cookie
        mockMvc.perform(get("/internal/session")
                        .cookie(new Cookie(CoreConstants.COOKIES_SESSION, VALID_SESSION)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_SESSION))
                .andExpect(jsonPath("$.email").value(emailUser1))
                .andExpect(jsonPath("$.password").value(nullValue()))
                .andExpect(jsonPath("$.userlevel").value(UserLevel.Beginner.toString()));
    }

    // With good credentials
    // POST with no cookie -> ok
    // POST with invalid cookie -> ok
    // POST with valid cookie -> ok
    @Test
    public void postValidCredentialsWithNoCookie() throws Exception {
        postValidCredentials(null);
    }

    @Test
    public void postValidCredentialsWithInvalidCookie() throws Exception {
        postValidCredentials("WRONG SESSION");
    }

    @Test
    public void postValidCredentialsWithValidCookie() throws Exception {
        postValidCredentials(VALID_SESSION);
    }

    public void postValidCredentials(String sessionCookieValue) throws Exception {
        AuthenticationCredentials credentials = new AuthenticationCredentials();
        credentials.setUsername(emailUser1);
        credentials.setPassword("testPassword");

        Cookie cookie = sessionCookieValue == null ?
                new Cookie("unused", "unused") :
                new Cookie(CoreConstants.COOKIES_SESSION, sessionCookieValue);

        MvcResult result = mockMvc.perform(post("/internal/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(credentials))
                        .cookie(cookie))
                .andDo(print())
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

        Cookie sessionCookie = result.getResponse().getCookie(CoreConstants.COOKIES_SESSION);
        Assertions.assertNotEquals(VALID_SESSION, sessionCookie.getValue());

        // try to retrieve session using the cookies generated
        mockMvc.perform(get("/internal/session")
                        .cookie(result.getResponse().getCookies()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value(emailUser1))
                .andExpect(jsonPath("$.password").value(nullValue()))
                .andExpect(jsonPath("$.userlevel").value(UserLevel.Beginner.toString()));
    }

    // With invalid credentials
    // POST with no cookie -> 401
    // POST with invalid cookie -> 401
    // POST with valid cookie -> 401
    @Test
    public void postInvalidCredentialsWithNoCookie() throws Exception {
        postInvalidCredentials(null);
    }

    @Test
    public void postInvalidCredentialsWithInvalidCookie() throws Exception {
        postInvalidCredentials("WRONG SESSION");
    }

    @Test
    public void postInvalidCredentialsWithValidCookie() throws Exception {
        postInvalidCredentials(VALID_SESSION);
    }

    public void postInvalidCredentials(String sessionCookieValue) throws Exception {
        AuthenticationCredentials credentials = new AuthenticationCredentials();
        credentials.setUsername(emailUser1);
        credentials.setPassword("WRONG-PASSWORD");

        Cookie cookie = sessionCookieValue == null ?
                new Cookie("unused", "unused") :
                new Cookie(CoreConstants.COOKIES_SESSION, sessionCookieValue);

        mockMvc.perform(post("/internal/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(credentials))
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("8002"));
    }
}
