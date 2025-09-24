package fr.insalyon.creatis.vip.core.server.business;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationProvider;

@Service
public class SessionBusiness {

    final private ConfigurationBusiness configurationBusiness;
    final private SessionAuthenticationProvider sessionAuthenticationProvider;
    final private Supplier<User> userProvider;

    @Autowired
    public SessionBusiness(ConfigurationBusiness configurationBusiness, Supplier<User> userProvider,
            SessionAuthenticationProvider sessionAuthenticationProvider) {
        this.configurationBusiness = configurationBusiness;
        this.userProvider = userProvider;
        this.sessionAuthenticationProvider = sessionAuthenticationProvider;
    }

    public Session signin(AuthenticationCredentials creds)
            throws BusinessException {
        User user = configurationBusiness.signin(creds.getUsername(), creds.getPassword());

        // define authenticated user in Spring context for the current request
        // this is usefull to call getSession()
        SecurityContextHolder.getContext()
                .setAuthentication(sessionAuthenticationProvider.createAuthenticationFromUser(user));

        return getSession();
    }

    public void signout() throws BusinessException {
        configurationBusiness.signout(userProvider.get().getEmail());

        // remove current user from Spring context
        SecurityContextHolder.clearContext();
    }

    public Session getSession() {
        User user = userProvider.get();
        Session session = new Session();

        session.id = user.getSession();
        session.email = user.getEmail();
        session.userlevel = user.getLevel();
        return session;
    }
}
