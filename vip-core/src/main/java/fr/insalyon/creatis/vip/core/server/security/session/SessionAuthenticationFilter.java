package fr.insalyon.creatis.vip.core.server.security.session;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionAuthenticationFilter extends OncePerRequestFilter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthenticationProvider authenticationProvider;

    public SessionAuthenticationFilter(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CoreConstants.COOKIES_SESSION.equals(cookie.getName())) {
                    try {
                        logger.debug("Cookie session found!");

                        SessionAuthenticationToken token = new SessionAuthenticationToken(null, cookie.getValue(), null, false);
                        Authentication result = authenticationProvider.authenticate(token);

                        logger.debug("Session authentication success for: {}", result);

                        // set the authentication in spring context
                        SecurityContextHolder.getContext().setAuthentication(result);
                    } catch (AuthenticationException e) {
                        SecurityContextHolder.clearContext();
                        logger.debug("Session authentication failed for session: {}", cookie.getValue(), e);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
