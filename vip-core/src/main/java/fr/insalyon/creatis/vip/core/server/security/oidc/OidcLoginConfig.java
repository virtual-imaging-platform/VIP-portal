package fr.insalyon.creatis.vip.core.server.security.oidc;

import fr.insalyon.creatis.vip.core.server.CarminProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/* OidcLoginConfig : defines the list of OIDC login providers,
 * i.e. things that provide a "Login with" button on VIP login page,
 * for interactive user authentication with an "Authorization Code" flow.
 */
@Service
/*
    This needs the properties that could be in vip-api.conf (especially in the test)
    For vip-api.conf properties to be loaded, the ApiPropertiesInitializer must be run before
    Spring is not aware of that, so we must tell him explicitly with @DependsOn
 */
@DependsOn("apiPropertiesInitializer")
public class OidcLoginConfig {

    private final Environment env;
    private final List<String> loginProviders;
    private final List<ClientRegistration> clientRegistrations;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public OidcLoginConfig(Environment env) {
        this.env = env;
        loginProviders = new ArrayList<>();
        clientRegistrations = new ArrayList<>();
        addLoginProvider("egi", CarminProperties.EGI_CLIENT_ID,
                (registrationId,clientId) -> egiClientRegistration(registrationId, clientId) );
        addLoginProvider("lslogin", CarminProperties.LSLOGIN_CLIENT_ID,
                (registrationId,clientId) -> lsloginClientRegistration(registrationId, clientId));
        // creating a clientRegistrationRepository repository is only valid when it has at least one member,
        // leave it null when we have none
        if (clientRegistrations.size() > 0) {
            this.clientRegistrationRepository = new InMemoryClientRegistrationRepository(clientRegistrations);
        } else {
            this.clientRegistrationRepository = null;
        }
    }

    private interface OidcLoginClientRegistrationBuilder {
        ClientRegistration build(String registrationId, String clientId);
    }
    private void addLoginProvider(String registrationId, String clientIdProperty,
                                  OidcLoginClientRegistrationBuilder registrationBuilder) {
        String clientId = env.getProperty(clientIdProperty);
        if (clientId != null && !clientId.isEmpty()) {
            loginProviders.add(registrationId);
            clientRegistrations.add(registrationBuilder.build(registrationId, clientId));
        }
    }

    private ClientRegistration egiClientRegistration(String registrationId, String clientId) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientId)
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
                .clientName(registrationId.toUpperCase())
                .build();
    }

    private ClientRegistration lsloginClientRegistration(String registrationId, String clientId) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientId)
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
                .clientName(registrationId.toUpperCase())
                .build();
    }

    public List<String> getLoginProviders() {
        return loginProviders;
    }

    public ClientRegistrationRepository getClientRegistrationRepository() {
        return clientRegistrationRepository;
    }
}
