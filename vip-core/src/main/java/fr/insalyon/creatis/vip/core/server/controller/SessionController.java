package fr.insalyon.creatis.vip.core.server.controller;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.SessionBusiness;
import fr.insalyon.creatis.vip.core.server.business.UserBusiness;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import fr.insalyon.creatis.vip.core.server.model.TwoFactorCodeRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final SessionBusiness sessionBusiness;
    private final UserBusiness userBusiness;
    final private Supplier<User> userProvider;

    @Autowired
    public SessionController(SessionBusiness sessionBusiness, Supplier<User> userProvider, UserBusiness userBusiness) {
        this.sessionBusiness = sessionBusiness;
        this.userBusiness = userBusiness;
        this.userProvider = userProvider;
    }

    @GetMapping
    public Session getSession(HttpServletRequest request, HttpServletResponse response) throws VipException {
        User user = userProvider.get();
        Session session = sessionBusiness.getSession(user);

        // renew existing cookies
        sessionBusiness.createLoginCookies(request, response, session);
        userBusiness.updateUserLastLogin(user.getEmail());

        return session;
    }
    @PostMapping
    public Map<String, Boolean> createSession(@RequestBody @Valid AuthenticationCredentials credentials,
            HttpServletRequest request)
            throws VipException {
        try {
            sessionBusiness.signin(credentials, request);
            return Map.of("twoFactorRequired", true);
        } catch (VipException e) {
            if (e.getMessage().startsWith("Authentication failed")) {
                throw new VipException(DefaultError.BAD_CREDENTIALS);
            }
            throw new VipException("Failed to create user session!", e);
        }
    }
    @PostMapping("/code")
    public Session verifyTwoFactorCode(@RequestBody @Valid TwoFactorCodeRequest codeRequest,
            HttpServletRequest request, HttpServletResponse response)
            throws VipException {
        Session session = sessionBusiness.verifyTwoFactorCode(codeRequest.getCode(), request);

        sessionBusiness.createLoginCookies(request, response, session);
        return session;
    }
    @PostMapping("/code/resend")
    public void resendTwoFactorCode(HttpServletRequest request) throws VipException {
        sessionBusiness.resendTwoFactorCode(request);
    }

    @DeleteMapping
    public void deleteSession(HttpServletRequest request, HttpServletResponse response) throws VipException {
        sessionBusiness.signout(request, response);
    }
}