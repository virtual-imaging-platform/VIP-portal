package fr.insalyon.creatis.vip.core.server.security.common;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.User;

// Provide authenticated user after a successful authentification (session, API and OIDC)
// this class use the SecurityContextHolder defined in AuthenticationProvider classes
@Service
public class CurrentUserProvider implements Supplier<User> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        } else {
            Object principal = authentication.getPrincipal();

            if (principal instanceof SpringPrincipalUser) { // API key / Session authentication
                return ((SpringPrincipalUser) principal).getVipUser();
            } else if (principal instanceof User) { // OIDC authentication
                return (User) principal;
            } else { // no resolvable user found (shouldn't happen)
                logger.error("CurrentUserProvider: unknown principal class {}", principal.getClass());
                return null;
            }
        }
    }
}