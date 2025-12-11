package fr.insalyon.creatis.vip.core.server.business;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SessionBusiness {

    final private ConfigurationBusiness configurationBusiness;
    final private Supplier<User> userProvider;
    final private Server server;


    @Autowired
    public SessionBusiness(ConfigurationBusiness configurationBusiness, Supplier<User> userProvider, Server server) {
        this.configurationBusiness = configurationBusiness;
        this.userProvider = userProvider;
        this.server = server;
    }

    public Session signin(AuthenticationCredentials creds)
            throws BusinessException {
        User user = configurationBusiness.signin(creds.getUsername(), creds.getPassword());

        return getSession(user);
    }

    public void signout() throws BusinessException {
        configurationBusiness.signout(userProvider.get().getEmail());

        // remove current user from Spring context
        SecurityContextHolder.clearContext();
    }

    public Session getSession(User user) {
        Session session = new Session();

        session.id = user.getSession();
        session.email = user.getEmail();
        session.userlevel = user.getLevel();
        return session;
    }

    public void createLoginCookies(HttpServletRequest request, HttpServletResponse response, Session session) throws UnsupportedEncodingException {
        CsrfToken token = new CookieCsrfTokenRepository().generateToken(request);

        response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, session.id,
                CoreConstants.COOKIES_DURATION, true));
        response.addCookie(createCookie(CoreConstants.COOKIES_USER, URLEncoder.encode(session.email, "UTF-8"),
                CoreConstants.COOKIES_DURATION, true));
        response.addCookie(createCookie(
                CoreConstants.COOKIES_CSRF_TOKEN, token.getToken(), CoreConstants.COOKIES_DURATION, false));
    }

    public Cookie createCookie(String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        boolean isSecure = server.isDevMode();

        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(isSecure);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
