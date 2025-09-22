package fr.insalyon.creatis.vip.core.server.business;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Session signin(AuthenticationCredentials creds, HttpServletRequest request, HttpServletResponse response)
            throws BusinessException {
        User user = configurationBusiness.signin(creds.getUsername(), creds.getPassword());
        CsrfToken token = new CookieCsrfTokenRepository().generateToken(request);
        Session session = new Session();

        session.email = user.getEmail();
        session.userlevel = user.getLevel();

        try {

            response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, user.getSession(),
                CoreConstants.COOKIES_DURATION, true));
            response.addCookie(createCookie(CoreConstants.COOKIES_USER, URLEncoder.encode(user.getEmail(), "UTF-8"),
                CoreConstants.COOKIES_DURATION, true));
            response.addCookie(createCookie(
                CoreConstants.COOKIES_CSRF_TOKEN, token.getToken(), CoreConstants.COOKIES_DURATION, false));

        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("Failed to encode email in cookie!");
        }
        return session;
    }

    public void signout(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        configurationBusiness.signout(userProvider.get().getEmail());

        response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, null, 0, true));
        response.addCookie(createCookie(CoreConstants.COOKIES_USER, null, 0, true));
        response.addCookie(createCookie(CoreConstants.COOKIES_CSRF_TOKEN, null, 0, false));
    }

    public Session getSession() {
        User user = userProvider.get();
        Session session = new Session();

        session.email = user.getEmail();
        session.userlevel = user.getLevel();
        return session;
    }

    private Cookie createCookie(String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        // for the moment we use that, but a property might be created in vip.conf
        // instead!
        boolean isSecure = server.getApacheSSLPort() != 80;

        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(isSecure);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
