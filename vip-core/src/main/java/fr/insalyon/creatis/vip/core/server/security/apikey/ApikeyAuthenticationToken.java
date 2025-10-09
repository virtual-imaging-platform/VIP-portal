package fr.insalyon.creatis.vip.core.server.security.apikey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * Used to store the apikey
 * Does not contain user info, because we dont know it at first
 */
public class ApikeyAuthenticationToken extends AbstractAuthenticationToken {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserDetails principal;
    private String apikey;

    public ApikeyAuthenticationToken(String apikey) {
        super(null);
        this.principal = null;
        this.apikey = apikey;
        setAuthenticated(false);
    }

    public ApikeyAuthenticationToken(UserDetails principal, String apikey, String role) {
        super(AuthorityUtils.createAuthorityList("ROLE_" + role));
        this.principal = principal;
        this.apikey = apikey;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return apikey;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        apikey = null;
    }
}
