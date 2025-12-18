package fr.insalyon.creatis.vip.core.server.security.common;

import fr.insalyon.creatis.vip.core.client.bean.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Vip user proxy that is conform to spring security (must implement a specific class)
 */
public class SpringPrincipalUser implements UserDetails, Principal {

    private final User vipUser;

    public SpringPrincipalUser(User vipUser) {
        this.vipUser = vipUser;
    }

    public User getVipUser() {
        return vipUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(); // not used at the moment
    }

    @Override
    public String getPassword() {
        return vipUser.getPassword();
    }

    @Override
    public String getUsername() {
        return vipUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // not used at the moment
    }

    @Override
    public boolean isAccountNonLocked() {
        return !vipUser.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // not used at the moment
    }

    @Override
    public boolean isEnabled() {
        return true; // not used at the moment
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
