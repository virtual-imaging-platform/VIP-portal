package fr.insalyon.creatis.vip.core.server.security.common;

import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserRolesBusiness {

    public boolean hasRole(String role) {

        AuthorizationResult result = AuthorityAuthorizationManager.hasRole(role)
            .authorize(() -> SecurityContextHolder.getContext().getAuthentication(), null);
        return result != null && result.isGranted();
    }
}