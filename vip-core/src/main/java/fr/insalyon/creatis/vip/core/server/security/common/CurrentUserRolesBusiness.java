package fr.insalyon.creatis.vip.core.server.security.common;

import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserRolesBusiness {

    public boolean hasRole(String role) {

        return AuthorityAuthorizationManager.hasRole(role)
                .authorize(() -> SecurityContextHolder.getContext().getAuthentication(), null)
                .isGranted();
    }
}