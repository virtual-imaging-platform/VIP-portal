package fr.insalyon.creatis.vip.core.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationFilter;
import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationProvider;
import org.springframework.security.web.firewall.RequestRejectedHandler;

@Configuration
@EnableWebSecurity
public class InternalSecurityConfig {
    private static final PathPatternRequestMatcher.Builder PATH = PathPatternRequestMatcher.withDefaults();

    private final SessionAuthenticationProvider sessionAuthenticationProvider;
    private final VipAuthenticationEntryPoint vipAuthenticationEntryPoint;
    private final RequestRejectedHandler requestRejectedHandler;

    @Autowired
    public InternalSecurityConfig(
            SessionAuthenticationProvider sessionAuthenticationProvider,
            VipAuthenticationEntryPoint vipAuthenticationEntryPoint,
            RequestRejectedHandler requestRejectedHandler) {
        this.sessionAuthenticationProvider = sessionAuthenticationProvider;
        this.vipAuthenticationEntryPoint = vipAuthenticationEntryPoint;
        this.requestRejectedHandler = requestRejectedHandler;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain getFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(getSessionAuthenticationFilter(), BasicAuthenticationFilter.class)
                .securityMatcher(pathMatcher("/internal/**"))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(pathMatcher(HttpMethod.POST, "/internal/session")).permitAll()
                        .requestMatchers(pathMatcher(HttpMethod.POST, "/internal/users")).permitAll()
                        .requestMatchers(pathMatcher(HttpMethod.PUT, "/internal/users/*/activate")).permitAll()
                        .requestMatchers(pathMatcher(HttpMethod.GET, "/internal/applications/public")).permitAll()
                        .requestMatchers(pathMatcher(HttpMethod.GET, "/internal/publications/public")).permitAll()
                        .requestMatchers(pathMatcher("/internal/**")).authenticated())
                // default CORS : no configuration, so block preflight and let the rest
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf
                        // CSRF token managed by SpringSecurity but MANUALLY setted in the controller
                        // after successfull login
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // we could active CSRF protection on whole endpoint but we will need a
                        // /internal/csrf GET endpoint
                        .ignoringRequestMatchers(
                            pathMatcher(HttpMethod.POST, "/internal/session"),
                            pathMatcher(HttpMethod.POST, "/internal/users"),
                            pathMatcher(HttpMethod.PUT, "/internal/users/*/activate")
                        ))
                .exceptionHandling((handler) -> handler.authenticationEntryPoint(vipAuthenticationEntryPoint));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer customizeRequestRejectedException() {
        return (web) -> web.requestRejectedHandler(requestRejectedHandler);
    }

    private SessionAuthenticationFilter getSessionAuthenticationFilter() {
        return new SessionAuthenticationFilter(sessionAuthenticationProvider, vipAuthenticationEntryPoint);
    }

    private static PathPatternRequestMatcher pathMatcher(String pattern) {
        return PATH.matcher(pattern);
    }

    private static PathPatternRequestMatcher pathMatcher(HttpMethod method, String pattern) {
        return PATH.matcher(method, pattern);
    }
}