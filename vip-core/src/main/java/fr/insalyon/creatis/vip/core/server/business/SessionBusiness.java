package fr.insalyon.creatis.vip.core.server.business;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.Cookie;
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

    public Session signin(AuthenticationCredentials creds, HttpServletResponse response) throws BusinessException {
        User user = configurationBusiness.signin(creds.getUsername(), creds.getPassword());
        Session session = new Session();

        session.email = user.getEmail();
        session.userlevel = user.getLevel();

        try {
            response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, user.getSession(), 86400 * 7));
            response.addCookie(createCookie(CoreConstants.COOKIES_USER, URLEncoder.encode(user.getEmail(), "UTF-8"), 86400 * 7));
        
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("Failed to encode email in cookie!");
        }
        return session;
    }

    public void signout(HttpServletResponse response) throws BusinessException {
        configurationBusiness.signout(userProvider.get().getEmail());

        response.addCookie(createCookie(CoreConstants.COOKIES_SESSION, null, 0));
        response.addCookie(createCookie(CoreConstants.COOKIES_USER, null, 0));
    }

    public Session getSession() {
        User user = userProvider.get();
        Session session = new Session();

        session.email = user.getEmail();
        session.userlevel = user.getLevel();
        return session;
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        // for the moment we use that, but a property might be created in vip.conf instead!
        boolean isSecure = server.getApacheSSLPort() != 80;

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(isSecure);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
