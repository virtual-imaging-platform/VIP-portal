package fr.insalyon.creatis.vip.core.server.business;

import java.net.URLEncoder;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;

@Service
public class SessionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Supplier<User> userProvider;
    private final Server server;
    private final UserDAO userDAO;
    private final AuthenticationBusiness authenticationBusiness;


    @Autowired
    public SessionBusiness(Supplier<User> userProvider, Server server, UserDAO userDAO, AuthenticationBusiness authenticationBusiness) {
        this.userProvider = userProvider;
        this.server = server;
        this.userDAO = userDAO;
        this.authenticationBusiness = authenticationBusiness;
    }

    public Session signin(AuthenticationCredentials creds)
            throws VipException {
        User user = authenticationBusiness.signin(creds.getUsername(), creds.getPassword());

        return getSession(user);
    }

    public void signout(HttpServletRequest request, HttpServletResponse response) throws VipException {
        authenticationBusiness.signout(userProvider.get().getEmail());
        // remove current user from the HTTP session
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            httpSession.removeAttribute(CoreConstants.SESSION_USER);
            httpSession.removeAttribute(CoreConstants.SESSION_GROUPS);
        }
        // remove current user from Spring context
        SecurityContextHolder.clearContext();
        clearLoginCookies(response);
    }

    public Session getSession(User user) {
        Session session = new Session();

        session.id = user.getSession();
        session.email = user.getEmail();
        session.userlevel = user.getLevel();
        session.confirmed = user.isConfirmed();
        return session;
    }

    public void createLoginCookies(HttpServletRequest request, HttpServletResponse response, Session session) {       
        CsrfToken token = new CookieCsrfTokenRepository().generateToken(request);
        response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, session.id,
                CoreConstants.COOKIES_DURATION, true));
        response.addCookie(createCookie(CoreConstants.COOKIES_USER, URLEncoder.encode(session.email, StandardCharsets.UTF_8),                CoreConstants.COOKIES_DURATION, true));
        response.addCookie(createCookie(
                CoreConstants.COOKIES_CSRF_TOKEN, token.getToken(), CoreConstants.COOKIES_DURATION, false));
    }

    public void clearLoginCookies(HttpServletResponse response) {
        response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, null, 0, true));
        response.addCookie(createCookie(CoreConstants.COOKIES_USER, null, 0, true));
        response.addCookie(createCookie(CoreConstants.COOKIES_CSRF_TOKEN, null, 0, false));
    }

    private Cookie createCookie(String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        boolean isSecure = ! server.isDevMode();

        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(isSecure);
        cookie.setMaxAge(maxAge);
        // GDPR / CNIL: these cookies are strictly necessary (authentication, security)
        // and therefore exempt from consent. SameSite=Lax limits them to first-party use
        // while still allowing the top-level redirect back from the OIDC provider (EGI Check-in).
        cookie.setAttribute("SameSite", "Lax");
        logger.debug("Creating {} cookie, secured : {}, httpOnly : {}", name, isSecure, httpOnly);
        return cookie;
    }

    public boolean validateSession(String email, String session) throws VipException {
        try {
            if (email != null && session != null) {
                if (userDAO.verifySession(email, session) && !userDAO.isLocked(email)) {
                    return true;
                }
                logger.info("Failed to verify user [{}]'s session {}", email, session);
                userDAO.incNFailedAuthentications(email); // just in case...
                if (userDAO.getNFailedAuthentications(email) > 5) {
                    userDAO.lock(email);
                }
            }
            return false;
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }
}
