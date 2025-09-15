package fr.insalyon.creatis.vip.core.server.business;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.Cookie;

@Service
public class SessionBusiness {

    final private ConfigurationBusiness configurationBusiness;
    final private Supplier<User> userProvider;

    @Autowired
    public SessionBusiness(ConfigurationBusiness configurationBusiness, Supplier<User> userProvider) {
        this.configurationBusiness = configurationBusiness;
        this.userProvider = userProvider;
    }

    public Cookie signin(AuthenticationCredentials creds) throws BusinessException {
        User user = configurationBusiness.signin(creds.getUsername(), creds.getPassword());
        Cookie cookie = createSessionCookie(user.getSession(), 86400 * 7);

        return cookie;
    }

    public Cookie signout() throws BusinessException {
        Cookie cookie = createSessionCookie(null, 0);

        configurationBusiness.signout(userProvider.get().getEmail());
        return cookie;
    }

    public Session getSession() {
        User user = userProvider.get();
        Session session = new Session();

        session.email = user.getEmail();
        session.userlevel = user.getLevel();
        return session;
    }

    private Cookie createSessionCookie(String value, int maxAge) {
        Cookie cookie = new Cookie(CoreConstants.COOKIES_SESSION, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
