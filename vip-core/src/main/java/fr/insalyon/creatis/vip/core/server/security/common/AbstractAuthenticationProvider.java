package fr.insalyon.creatis.vip.core.server.security.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;

// this class was factorized from the Apikey Security provider 
// since we have added the Session Security Provider
@Service
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    protected UserDAO userDAO;

    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.messages, "A message source must be set");
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
 
    protected void afterSuccess(UserDetails user) {
        try {
            logger.info("Successful logging for " + user.getUsername());
            userDAO.resetNFailedAuthentications(user.getUsername());
        } catch (DAOException e) {
            logger.error("Error resetting failed auth attempts. Ignoring", e);
        }
    }

    protected void checkUserInfo(UserDetails user) {
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
