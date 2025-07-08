package fr.insalyon.creatis.vip.api.security.egi;

import fr.insalyon.creatis.vip.api.CarminProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

@Configuration
public class EgiSecurityClientConfig {

    private final Environment env;

    @Autowired
    public EgiSecurityClientConfig(Environment env) {
        this.env = env;
    }

    /*
        This needs the properties that could be in vip-api.conf (especially in the test)
        For vip-api.conf properties to be loaded, the ApiPropertyInitializer must be run before
        Spring is not aware of that, so we must tell him explicitly with @DependsOn
     */
    @Bean
    @DependsOn("apiPropertiesInitializer")
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.egiClientRegistration(), this.lsloginClientRegistration());
    }

    private ClientRegistration egiClientRegistration() {
        return ClientRegistration.withRegistrationId("egi")
                .clientId(env.getRequiredProperty(CarminProperties.EGI_CLIENT_ID))
                .clientSecret(env.getRequiredProperty(CarminProperties.EGI_CLIENT_SECRET))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(env.getRequiredProperty(CarminProperties.EGI_REDIRECT_URI))
                .scope("openid", "profile", "email", "voperson_id", "eduperson_scoped_affiliation")
                .authorizationUri(env.getRequiredProperty(CarminProperties.EGI_AUTHORIZATION_URI))
                .tokenUri(env.getRequiredProperty(CarminProperties.EGI_TOKEN_URI))
                .userInfoUri(env.getRequiredProperty(CarminProperties.EGI_USER_INFO_URI))
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(env.getRequiredProperty(CarminProperties.EGI_JWK_SET_URI))
                .clientName("EGI")
                .build();
    }

    private ClientRegistration lsloginClientRegistration() {
        return ClientRegistration.withRegistrationId("lslogin")
                .clientId(env.getRequiredProperty(CarminProperties.LSLOGIN_CLIENT_ID))
                .clientSecret(env.getRequiredProperty(CarminProperties.LSLOGIN_CLIENT_SECRET))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(env.getRequiredProperty(CarminProperties.LSLOGIN_REDIRECT_URI))
                .scope("openid", "profile", "email", "voperson_external_id", "eduperson_scoped_affiliation")
                .authorizationUri(env.getRequiredProperty(CarminProperties.LSLOGIN_AUTHORIZATION_URI))
                .tokenUri(env.getRequiredProperty(CarminProperties.LSLOGIN_TOKEN_URI))
                .userInfoUri(env.getRequiredProperty(CarminProperties.LSLOGIN_USER_INFO_URI))
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(env.getRequiredProperty(CarminProperties.LSLOGIN_JWK_SET_URI))
                .clientName("LSLOGIN")
                .build();
    }
}
