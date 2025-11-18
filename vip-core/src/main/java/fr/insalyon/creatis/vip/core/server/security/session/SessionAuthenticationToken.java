package fr.insalyon.creatis.vip.core.server.security.session;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class SessionAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails principal;
    private String session;

    public SessionAuthenticationToken(UserDetails details, String session, String role, boolean connected) {
        super(AuthorityUtils.createAuthorityList("ROLE_" + role));
        principal = details;
        this.session = session;
        setAuthenticated(connected);
    }

    @Override
    public Object getCredentials() {
        return session;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
