package fr.insalyon.creatis.vip.api.security.keycloak;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;

@Component
public class VipKeycloakAuthenticationProvider extends KeycloakAuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigurationBusiness configurationBusiness;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    public VipKeycloakAuthenticationProvider(ConfigurationBusiness configurationBusiness) {
        this.configurationBusiness = configurationBusiness;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken keycloakAuthenticationToken =
                (KeycloakAuthenticationToken) super.authenticate(authentication);

        KeycloakSecurityContext session = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext();
        String email = session.getToken().getEmail();

        User vipUser;
        try{
            vipUser = configurationBusiness.getUserWithGroups(email);
        } catch (BusinessException e) {
            logger.error("Error when getting user from keycloak token. Doing as if there is an auth error", e);
            throw new BadCredentialsException(
                    messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
        }
        if (vipUser == null) {
            logger.info("Can't authenticate from keycloak because user does not exist in VIP:" + email);
            throw new BadCredentialsException(
                    messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
        }
        // recreate whole keycloak user data to add the vip user in it
        SpringKeycloakPrincipal springKeycloakPrincipal = new SpringKeycloakPrincipal(
                vipUser,
                keycloakAuthenticationToken.getAccount().getPrincipal().getName(),
                keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext());
        KeycloakAccount keycloakAccount = new SimpleKeycloakAccount(
                springKeycloakPrincipal,
                keycloakAuthenticationToken.getAccount().getRoles(),
                (RefreshableKeycloakSecurityContext) keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext());
        return new KeycloakAuthenticationToken(
                keycloakAccount,
                keycloakAuthenticationToken.isInteractive(),
                keycloakAuthenticationToken.getAuthorities());
    }
}
