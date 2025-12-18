package fr.insalyon.creatis.vip.core.server.security.apikey;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.security.common.AbstractAuthenticationProvider;
import fr.insalyon.creatis.vip.core.server.security.common.SpringPrincipalUser;

/**
 * Authenticate a user with its api key.
 * Automaticaly taken into account by spring security by implementing {@link AuthenticationProvider}
 */
@Service
public class ApikeyAuthenticationProvider extends AbstractAuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // ~ Instance fields
    // ================================================================================================

    @Autowired
    private ConfigurationBusiness configurationBusiness;

    @Override
    public boolean supports(Class<?> authentication) {
        return ApikeyAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        Assert.isInstanceOf(ApikeyAuthenticationToken.class, authentication,
                "Only ApikeyAuthenticationToken is supported");

        User vipUser;
        String apikey = authentication.getCredentials().toString();
        try {
            vipUser = userDAO.getUserByApikey(apikey);
        } catch (DAOException e) {
            logger.error("error when getting user by apikey. Doing as if there is an auth error", e);
            throw new BadCredentialsException(
                messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        if (vipUser == null) {
            logger.info(
                "Cant authenticate because apikey not found:" + apikey);
            throw new BadCredentialsException(
                messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        logger.debug("apikey OK for " + vipUser.getEmail());
        UserDetails springUser;
        try {
            Map<Group, CoreConstants.GROUP_ROLE> groups =
                configurationBusiness.getUserGroups(vipUser.getEmail());
            vipUser.setGroups(groups);
            springUser = new SpringPrincipalUser(vipUser);
        } catch (VipException e) {
            logger.error("error when getting user groups for {}. Doing as if there is an auth error",
                    vipUser.getEmail(), e);
            throw new BadCredentialsException(
                messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        checkUserInfo(springUser);
        afterSuccess(springUser);
        return new ApikeyAuthenticationToken(
                    springUser, apikey,
                    vipUser.getLevel().name().toUpperCase());
    }
}
