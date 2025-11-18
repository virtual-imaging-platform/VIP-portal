package fr.insalyon.creatis.vip.core.server.business;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;

@Service
public class SessionBusiness {

    final private ConfigurationBusiness configurationBusiness;
    final private Supplier<User> userProvider;

    @Autowired
    public SessionBusiness(ConfigurationBusiness configurationBusiness, Supplier<User> userProvider) {
        this.configurationBusiness = configurationBusiness;
        this.userProvider = userProvider;
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
}
