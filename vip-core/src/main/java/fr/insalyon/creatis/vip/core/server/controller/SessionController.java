package fr.insalyon.creatis.vip.core.server.controller;

import java.io.UnsupportedEncodingException;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.SessionBusiness;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final SessionBusiness sessionBusiness;
    private final ConfigurationBusiness configurationBusiness;
    final private Supplier<User> userProvider;

    @Autowired
    public SessionController(SessionBusiness sessionBusiness, Supplier<User> userProvider, ConfigurationBusiness configurationBusiness) {
        this.sessionBusiness = sessionBusiness;
        this.userProvider = userProvider;
        this.configurationBusiness = configurationBusiness;
    }

    @GetMapping
    public Session getSession(HttpServletRequest request, HttpServletResponse response) throws ApiException {
        User user = userProvider.get();
        Session session = sessionBusiness.getSession(user);

        try {
            // renew existing cookies
            sessionBusiness.createLoginCookies(request, response, session);
            configurationBusiness.updateUserLastLogin(user.getEmail());

            return session;
        } catch (UnsupportedEncodingException | VipException e) {
            throw new ApiException("Failed to retrieve user session!", e);
        }
    }

    @PostMapping
    public Session createSession(@RequestBody @Valid AuthenticationCredentials credentials, HttpServletRequest request,
            HttpServletResponse response)
            throws VipException {
        try {
            Session session = sessionBusiness.signin(credentials);

            sessionBusiness.createLoginCookies(request, response, session);
            return session;
        } catch (UnsupportedEncodingException e) {
            throw new VipException("Failed to create user session!", e);
        }
    }

    @DeleteMapping
    public void deleteSession(HttpServletRequest request, HttpServletResponse response) throws VipException {
        try {
            sessionBusiness.signout();
            sessionBusiness.clearLoginCookies(response);

        } catch (VipException e) {
            throw new VipException(e); // change
        }
    }
}
