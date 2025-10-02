/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.server.security.apikey;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by abonnet on 7/25/16.
 *
 * Authenticate a user with its api key.
 * Automaticaly taken into account by spring security by implementing {@link AuthenticationProvider}
 */
@Service
public class ApikeyAuthenticationProvider implements
        AuthenticationProvider, InitializingBean, MessageSourceAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // ~ Instance fields
    // ================================================================================================

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private ConfigurationBusiness configurationBusiness;

    @Autowired
    private UserDAO userDAO;

    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.messages, "A message source must be set");
    }

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
            springUser = new SpringApiPrincipal(vipUser);
        } catch (BusinessException e) {
            logger.error("error when getting user groups for {}. Doing as if there is an auth error",
                    vipUser.getEmail(), e);
            throw new BadCredentialsException(
                messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        checkUserInfo(springUser);
        try {
            logger.info(
                "successful logging for " + springUser.getUsername());
            userDAO.resetNFailedAuthentications(springUser.getUsername());
        } catch (DAOException e) {
            logger.error("Error resetting failed auth attempts. Ignoring", e);
        }
            return new ApikeyAuthenticationToken(
                    springUser, apikey,
                    vipUser.getLevel().name().toUpperCase());
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    private void checkUserInfo(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            logger.info("User account is locked");

            throw new LockedException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.locked",
                    "User account is locked"));
        }

        if (!user.isEnabled()) {
            logger.info("User account is disabled");

            throw new DisabledException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.disabled",
                    "User is disabled"));
        }

        if (!user.isAccountNonExpired()) {
            logger.info("User account is expired");

            throw new AccountExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.expired",
                    "User account has expired"));
        }
        if (!user.isCredentialsNonExpired()) {
            logger.info("User account credentials have expired");

            throw new CredentialsExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                    "User credentials have expired"));
        }
    }
}
