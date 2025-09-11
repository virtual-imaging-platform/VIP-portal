package fr.insalyon.creatis.vip.core.server.security.oidc;

import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.Server.OidcLoginProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OidcLoginConfig {

    private final Server server;
    private final List<String> loginProviders;
    private final List<ClientRegistration> clientRegistrations;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public OidcLoginConfig(Server server) {
        this.server = server;
        loginProviders = new ArrayList<>();
        clientRegistrations = new ArrayList<>();
        addLoginProvider("egi", server.getOidcConfigEGI(),
                (registrationId,conf) -> egiClientRegistration(registrationId, conf) );
        addLoginProvider("lslogin", server.getOidcConfigLSLOGIN(),
                (registrationId,conf) -> lsloginClientRegistration(registrationId, conf));
        // creating a clientRegistrationRepository repository is only valid when it has at least one member,
        // leave it null when we have none
        if (clientRegistrations.size() > 0) {
            this.clientRegistrationRepository = new InMemoryClientRegistrationRepository(clientRegistrations);
        } else {
            this.clientRegistrationRepository = null;
        }
    }

    private interface OidcLoginClientRegistrationBuilder {
        ClientRegistration build(String registrationId, OidcLoginProviderConfig conf);
    }
    private void addLoginProvider(String registrationId, OidcLoginProviderConfig conf,
                                  OidcLoginClientRegistrationBuilder registrationBuilder) {
        if (conf != null) {
            loginProviders.add(registrationId);
            clientRegistrations.add(registrationBuilder.build(registrationId, conf));
        }
    }

    private ClientRegistration egiClientRegistration(String registrationId, OidcLoginProviderConfig conf) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(conf.clientId)
                .clientSecret(conf.clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(conf.redirectUri)
                .scope("openid", "profile", "email", "voperson_id", "eduperson_scoped_affiliation")
                .authorizationUri(conf.authorizationUri)
                .tokenUri(conf.tokenUri)
                .userInfoUri(conf.userInfoUri)
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(conf.jwkSetUri)
                .clientName(registrationId.toUpperCase())
                .build();
    }

    private ClientRegistration lsloginClientRegistration(String registrationId, OidcLoginProviderConfig conf) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(conf.clientId)
                .clientSecret(conf.clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(conf.redirectUri)
                .scope("openid", "profile", "email", "voperson_external_id", "eduperson_scoped_affiliation")
                .authorizationUri(conf.authorizationUri)
                .tokenUri(conf.tokenUri)
                .userInfoUri(conf.userInfoUri)
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(conf.jwkSetUri)
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
