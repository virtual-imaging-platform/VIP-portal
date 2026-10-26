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
import fr.insalyon.creatis.vip.core.client.DefaultError;

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
    private final TwoFactorBusiness twoFactorBusiness;
    private final UserBusiness userBusiness;
    private static final String SESSION_2FA = "SESSION_2FA";
    private static final String  SESSION_2FA_LAST_SENT = "SESSION_2FA_LAST_SENT";
    private static final long RESEND_DELAY_MS = 30_000; // 30 seconds


    @Autowired
    public SessionBusiness(Supplier<User> userProvider, Server server, UserDAO userDAO, AuthenticationBusiness authenticationBusiness, TwoFactorBusiness twoFactorBusiness, UserBusiness userBusiness) {
        this.userProvider = userProvider;
        this.server = server;
        this.userDAO = userDAO;
        this.authenticationBusiness = authenticationBusiness;
        this.twoFactorBusiness = twoFactorBusiness;
        this.userBusiness = userBusiness;
    }

    public void signin(AuthenticationCredentials creds, HttpServletRequest request) throws VipException {
        User user = authenticationBusiness.signinWithoutResetingSession(
                creds.getUsername(), creds.getPassword());

        twoFactorBusiness.sendCode(user.getEmail());

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(SESSION_2FA, user.getEmail());
        HttpSession httpSessionLastSent = request.getSession(true);
        httpSessionLastSent.setAttribute(SESSION_2FA_LAST_SENT, System.currentTimeMillis());
    }

    public Session verifyTwoFactorCode(String code, HttpServletRequest request)
            throws VipException {
        HttpSession httpSession = request.getSession(false);
        String email = (httpSession == null) ? null
                : (String) httpSession.getAttribute(SESSION_2FA);

        if (email == null) {
            logger.warn("2FA code submitted without a pending sign-in");
            throw new VipException(DefaultError.BAD_CREDENTIALS);
        }
        if (!twoFactorBusiness.verifyCode(email, code)) {
            throw new VipException(DefaultError.BAD_CREDENTIALS);
        }
        httpSession.removeAttribute(SESSION_2FA);
        request.changeSessionId();

        try {
            User user = userBusiness.getUserWithSession(email);
            return getSession(user);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }
    
    public  Session resendTwoFactorCode(HttpServletRequest request) throws VipException {
        HttpSession httpSession = request.getSession(false);
        String email = (httpSession == null) ? null
                : (String) httpSession.getAttribute(SESSION_2FA);

        if (email == null) {
            logger.warn("2FA code resend requested without a pending sign-in");
            throw new VipException(DefaultError.BAD_CREDENTIALS);
        }

        Long lastSentTime = (Long) httpSession.getAttribute(SESSION_2FA_LAST_SENT);
        long currentTime = System.currentTimeMillis();
        if (lastSentTime != null && (currentTime - lastSentTime) < RESEND_DELAY_MS) {
            logger.warn("2FA code resend requested too soon for user {}", email);
            throw new VipException("Please wait before requesting a new code.");
        }

        twoFactorBusiness.sendCode(email);
        httpSession.setAttribute(SESSION_2FA_LAST_SENT, currentTime);

        try {
            User user = userBusiness.getUserWithSession(email);
            return getSession(user);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
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
