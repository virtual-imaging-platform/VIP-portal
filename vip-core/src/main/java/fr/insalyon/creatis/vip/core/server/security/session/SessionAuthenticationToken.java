package fr.insalyon.creatis.vip.core.server.security.session;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

public class SessionAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails principal;
    private String session;

    public SessionAuthenticationToken(UserDetails details, String session, boolean connected) {
        super(details != null ? details.getAuthorities() : new ArrayList<>());
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
