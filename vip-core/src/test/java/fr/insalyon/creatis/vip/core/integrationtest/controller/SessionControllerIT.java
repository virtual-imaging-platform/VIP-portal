package fr.insalyon.creatis.vip.core.integrationtest.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.BaseInternalApiSpringIT;
import fr.insalyon.creatis.vip.core.server.dao.TwoFactorCodeDAO;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;

public class SessionControllerIT extends BaseInternalApiSpringIT {

    private static final String KNOWN_CODE = "123456";
    private static final String WRONG_CODE = "000000";

    @Autowired
    private TwoFactorCodeDAO twoFactorCodeDAO;

    private static String sha256(String value) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
    }

    private MockHttpSession passwordStep(String email, String password) throws Exception {
        AuthenticationCredentials credentials = new AuthenticationCredentials();
        credentials.setUsername(email);
        credentials.setPassword(password);

        MockHttpSession httpSession = new MockHttpSession();
        mockMvc.perform(post("/internal/session")
            .session(httpSession)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(credentials)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.twoFactorRequired").value(true))
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_USER))
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_SESSION));

        twoFactorCodeDAO.save(email, sha256(KNOWN_CODE),
                new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000L));
        return httpSession;
    }

    private ResultActions codeStep(MockHttpSession httpSession, String code) throws Exception {
        return mockMvc.perform(post("/internal/session/code")
            .session(httpSession)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"code\":\"" + code + "\"}"));
    }
    private ResultActions resendStep(MockHttpSession httpSession) throws Exception {
        return mockMvc.perform(post("/internal/session/code/resend")
            .session(httpSession));
    }

    private MvcResult fullLogin(String email, String password) throws Exception {
        MockHttpSession httpSession = passwordStep(email, password);
        return codeStep(httpSession, KNOWN_CODE)
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void getSession() throws Exception {
        createUser(emailUser1);
        userDAO.updateSession(emailUser1, "super_session");
        user1 = userDAO.get(emailUser1);

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
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_USER))
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_SESSION));

        // connection with good password
        MockHttpSession httpSession = passwordStep(emailUser1, "testPassword");

        mockMvc.perform(get("/internal/session").session(httpSession))
            .andExpect(status().isUnauthorized());

        MvcResult result = codeStep(httpSession, KNOWN_CODE)
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
        user1 = createUser(emailUser1);

        // generate session cookies
        MvcResult result = fullLogin(emailUser1, "testPassword");

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
        user1 = createUser(emailUser1);

        // login user1
        MvcResult result = fullLogin(emailUser1, "testPassword");

        // logout user1
        mockMvc.perform(delete("/internal/session")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .cookie(result.getResponse().getCookies()))
                .andExpect(status().isOk());

        user2 = createUser(emailUser2, "2");

        // login user2 and check session info
        MockHttpSession httpSession = passwordStep(emailUser2, "testPassword");
        codeStep(httpSession, KNOWN_CODE)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(emailUser2));
    }

    @Test
    public void twoFactorWrongCode() throws Exception {
        user1 = createUser(emailUser1);
        MockHttpSession httpSession = passwordStep(emailUser1, "testPassword");

        codeStep(httpSession, WRONG_CODE)
            .andExpect(status().isUnauthorized())
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_USER))
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_SESSION));
    }

    @Test
    public void twoFactorCodeWithoutPasswordStep() throws Exception {
        user1 = createUser(emailUser1);
        twoFactorCodeDAO.save(emailUser1, sha256(KNOWN_CODE),
                new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000L));

        codeStep(new MockHttpSession(), KNOWN_CODE)
            .andExpect(status().isUnauthorized())
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_SESSION));
    }

    @Test
    public void twoFactorCodeIsSingleUse() throws Exception {
        user1 = createUser(emailUser1);
        MockHttpSession httpSession = passwordStep(emailUser1, "testPassword");

        codeStep(httpSession, KNOWN_CODE).andExpect(status().isOk());
        codeStep(httpSession, KNOWN_CODE).andExpect(status().isUnauthorized());
    }

    @Test
    public void twoFactorTooManyAttempts() throws Exception {
        user1 = createUser(emailUser1);
        MockHttpSession httpSession = passwordStep(emailUser1, "testPassword");

        for (int i = 0; i < 5; i++) {
            codeStep(httpSession, WRONG_CODE).andExpect(status().isUnauthorized());
        }

        codeStep(httpSession, KNOWN_CODE)
            .andExpect(status().isUnauthorized())
            .andExpect(cookie().doesNotExist(CoreConstants.COOKIES_SESSION));
    }

    @Test
    public void twoFactorResendWithoutPasswordStep() throws Exception {
        resendStep(new MockHttpSession())
            .andExpect(status().isUnauthorized());
    }
        @Test
    public void twoFactorResendTooSoon() throws Exception {
        user1 = createUser(emailUser1);
        MockHttpSession httpSession = passwordStep(emailUser1, "testPassword");
        resendStep(httpSession).andExpect(status().isBadRequest());
        codeStep(httpSession, KNOWN_CODE).andExpect(status().isOk());
    }
    @Test
    public void twoFactorResendReplacesOldCode() throws Exception {
        user1 = createUser(emailUser1);
        MockHttpSession httpSession = passwordStep(emailUser1, "testPassword");
        httpSession.setAttribute("SESSION_2FA_LAST_SENT", System.currentTimeMillis() - 31_000);
        resendStep(httpSession).andExpect(status().isOk());
        codeStep(httpSession, KNOWN_CODE)
            .andExpect(status().isUnauthorized());
    }

}