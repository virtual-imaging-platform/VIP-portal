package fr.insalyon.creatis.vip.api.security.oidc;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import fr.insalyon.creatis.vip.core.client.bean.User;

import java.util.Collection;

public class OidcToken extends AbstractAuthenticationToken {
    private final User user;
    private Jwt jwt;

    public OidcToken(User user, Jwt jwt, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.jwt = jwt;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() { return jwt; }

    @Override
    public Object getPrincipal() { return user; }

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
        jwt = null;
    }
}
