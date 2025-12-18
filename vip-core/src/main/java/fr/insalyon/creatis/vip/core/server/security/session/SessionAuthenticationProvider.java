package fr.insalyon.creatis.vip.core.server.security.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.security.common.AbstractAuthenticationProvider;
import fr.insalyon.creatis.vip.core.server.security.common.SpringPrincipalUser;

@Service
public class SessionAuthenticationProvider extends AbstractAuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(Class<?> authentication) {
        return SessionAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String session = authentication.getCredentials().toString();
        User user;
        UserDetails springUser;

        try {
            user = userDAO.getUserBySession(session);

            if (user == null) {
                throw new BadCredentialsException("This session do not exist!");
            } else {
                user.setGroups(usersGroupsDAO.getUserGroups(user.getEmail()));

                springUser = new SpringPrincipalUser(user);
                checkUserInfo(springUser);
            }
        } catch (DAOException e) {
            logger.error("DAOException during authentication!", e);
            throw new BadCredentialsException("Failed to retrieve user by using session!", e);
        }
        afterSuccess(springUser);

        return createAuthenticationFromUser(user);
    }

    public Authentication createAuthenticationFromUser(User user) {
        SpringPrincipalUser springUser = new SpringPrincipalUser(user);

        return new SessionAuthenticationToken(
                springUser, user.getSession(), user.getLevel().name().toUpperCase(), true);
    }
}
