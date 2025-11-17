package fr.insalyon.creatis.vip.core.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.SessionBusiness;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final SessionBusiness sessionBusiness;
    private final Server server;
    final private Supplier<User> userProvider;

    @Autowired
    public SessionController(SessionBusiness sessionBusiness, Server server, Supplier<User> userProvider) {
        this.sessionBusiness = sessionBusiness;
        this.server = server;
        this.userProvider = userProvider;
    }

    @GetMapping
    public Session getSession(HttpServletRequest request, HttpServletResponse response) throws ApiException {
        Session session = sessionBusiness.getSession(userProvider.get());
        CsrfToken token = new CookieCsrfTokenRepository().generateToken(request);

        try {
            // renew existing cookies
            createLoginCookies(response, token, session);
            return session;
        } catch (UnsupportedEncodingException e) {
            throw new ApiException("Failed to retrieve user session!", e);
        }
    }

    @PostMapping
    public Session createSession(@RequestBody @Valid AuthenticationCredentials credentials, HttpServletRequest request,
            HttpServletResponse response)
            throws ApiException {
        try {
            Session session = sessionBusiness.signin(credentials);
            CsrfToken token = new CookieCsrfTokenRepository().generateToken(request);

            createLoginCookies(response, token, session);
            return session;
        } catch (UnsupportedEncodingException | BusinessException e) {
            throw new ApiException("Failed to create user session!", e);
        }
    }

    @DeleteMapping
    public void deleteSession(HttpServletRequest request, HttpServletResponse response) throws ApiException {
        try {
            sessionBusiness.signout();

            response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, null, 0, true));
            response.addCookie(createCookie(CoreConstants.COOKIES_USER, null, 0, true));
            response.addCookie(createCookie(CoreConstants.COOKIES_CSRF_TOKEN, null, 0, false));
        } catch (BusinessException e) {
            throw new ApiException(e); // change
        }
    }

    private void createLoginCookies(HttpServletResponse response, CsrfToken token, Session session) throws UnsupportedEncodingException {
        response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, session.id,
                CoreConstants.COOKIES_DURATION, true));
        response.addCookie(createCookie(CoreConstants.COOKIES_USER, URLEncoder.encode(session.email, "UTF-8"),
                CoreConstants.COOKIES_DURATION, true));
        response.addCookie(createCookie(
                CoreConstants.COOKIES_CSRF_TOKEN, token.getToken(), CoreConstants.COOKIES_DURATION, false));
    }

    private Cookie createCookie(String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        boolean isSecure = server.isDevMode();

        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(isSecure);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
