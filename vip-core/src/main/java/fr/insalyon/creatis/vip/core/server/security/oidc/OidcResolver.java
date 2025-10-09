package fr.insalyon.creatis.vip.core.server.security.oidc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;

@Service
public class OidcResolver {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ConfigurationBusiness configurationBusiness;
    private final OidcConfig oidcConfig;
    private final SimpleAuthorityMapper authoritiesMapper;

    @Autowired
    public OidcResolver(ConfigurationBusiness configurationBusiness, OidcConfig oidcConfig) {
        this.configurationBusiness = configurationBusiness;
        this.oidcConfig = oidcConfig;
        this.authoritiesMapper = new SimpleAuthorityMapper();
        this.authoritiesMapper.setConvertToUpperCase(true);
    }

    // Common "Bad credentials" exception for Jwt to User resolution errors
    private BadCredentialsException authError() {
        return new BadCredentialsException(
                SpringSecurityMessageSource.getAccessor().getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
    }

    // Get DB User from authenticated JWT, using email-based resolution
    private User getVipUser(Jwt jwt) throws BadCredentialsException {
        String email = jwt.getClaim("email");
        if (email == null) { // no email field in token
            logger.warn("Can't authenticate from OIDC token: no email in token");
            throw authError();
        }
        User vipUser;
        try {
            vipUser = configurationBusiness.getUserWithGroups(email);
        } catch (VipException e) { // DB lookup failed
            logger.error("Error when getting user from OIDC token: doing as if there is an auth error", e);
            throw authError();
        }
        if (vipUser == null) { // user not found
            logger.warn("Can't authenticate from OIDC token: user does not exist in VIP: {}", email);
            throw authError();
        }
        if (vipUser.isAccountLocked()) { // account locked
            logger.info("Can't authenticate from OIDC token: account is locked: {}", email);
            throw authError();
        }
        return vipUser;
    }

    // Create authorities list from jwt claims.
    // Parsing realm_access.roles or resource_access.<resourceName>.roles is Keycloak-specific.
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> parseAuthorities(User user, Jwt jwt) {
        List<String> roles = new ArrayList<>(); // default to no roles
        // At this point, jwt has already been verified by Spring resource server, so we assume the issuer is known (server != null).
        // Also, we only handle Keycloak-generated tokens for now, so we assume realm_access and roles fields to exist.
        // Thus, the only error case we handle in token parsing below is a mismatch on resourceName:
        // any other issue will deliberately cause an exception and request error 500.
        OidcConfig.OidcServer server = oidcConfig.getServerConfig(jwt.getIssuer().toString());
        if (server.useResourceRoleMappings) { // use resource-level roles
            String resource = server.resourceName;
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            Map<String, Object> realmAccess = (Map<String, Object>) resourceAccess.get(resource);
            if (realmAccess != null) {
                roles = (List<String>) realmAccess.get("roles");
            } else {
                logger.warn("Can't get roles for user {}: resource '{}' not found in token, defaulting to no roles",
                        user.getEmail(), resource);
            }
        } else { // use realm-level roles
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            roles = (List<String>) realmAccess.get("roles");
        }
        // Note that, contrary to Apikey auth, we don't derive any permission from user level here, and only rely on JWT roles.
        Collection<GrantedAuthority> oidcRoles = AuthorityUtils.createAuthorityList(roles);
        // Map JWT roles to Spring authorities, with implicit "ROLE_" prefix and uppercase conversion.
        // For instance, a Keycloak "administrator" role is mapped to the "ROLE_ADMINISTRATOR" authority.
        Collection<GrantedAuthority> mappedAuthorities = authoritiesMapper.mapAuthorities(oidcRoles);
        return mappedAuthorities;
    }

    // Custom converter class to transform Spring-provided Jwt into our own OidcToken, so that we can resolve and cache
    // User as the authentication principal, and thus avoid multiple DB lookups per token within a given request.
    static private class OidcJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        private final OidcResolver oidcResolver;
        public OidcJwtConverter(OidcResolver oidcResolver) {
            this.oidcResolver = oidcResolver;
        }
        public AbstractAuthenticationToken convert(Jwt jwt) {
            // At this point, jwt has been checked for: iss, exp, nbf: i.e. trusted issuer + validity dates.
            // We could also, but do not, check aud: this is optional in OIDC spec, and wasn't done by previous implementation.
            // Now we have to resolve DB user, and map authorizations.
            User user = oidcResolver.getVipUser(jwt);
            Collection<GrantedAuthority> authorities = oidcResolver.parseAuthorities(user, jwt);
            return new OidcToken(user, jwt, authorities);
        }
    }

    // Create a JwtIssuerAuthenticationManagerResolver instance with our custom converter.
    // This is functionally equivalent to jwt.jwtAuthenticationConverter(), but for multi-tenant environment
    // (see https://github.com/spring-projects/spring-security/issues/9096#issuecomment-973224956).
    public JwtIssuerAuthenticationManagerResolver getAuthenticationManagerResolver() {
        OidcJwtConverter converter = new OidcJwtConverter(this);
        Map<String, AuthenticationManager> managers = new HashMap<>();
        for (String issuer : oidcConfig.getServers()) {
            JwtDecoder decoder = new SupplierJwtDecoder(() -> JwtDecoders.fromIssuerLocation(issuer));
            JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
            provider.setJwtAuthenticationConverter(converter);
            managers.put(issuer, provider::authenticate);
        }
        return new JwtIssuerAuthenticationManagerResolver(managers::get);
    }
}
