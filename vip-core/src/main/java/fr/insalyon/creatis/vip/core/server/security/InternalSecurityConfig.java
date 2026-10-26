package fr.insalyon.creatis.vip.core.server.security;

import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.withDefaults;

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
        PathPatternRequestMatcher.Builder internal = withDefaults().basePath("/internal");
        http
                .addFilterBefore(getSessionAuthenticationFilter(), BasicAuthenticationFilter.class)
                .securityMatcher(internal.matcher("/**"))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(internal.matcher(HttpMethod.POST, "/session")).permitAll()
                        .requestMatchers(internal.matcher(HttpMethod.POST, "/session/code")).permitAll()
                        .requestMatchers(internal.matcher(HttpMethod.POST, "/session/code/resend")).permitAll()
                        .requestMatchers(internal.matcher(HttpMethod.POST, "/users")).permitAll()
                        .requestMatchers(internal.matcher(HttpMethod.PUT, "/users/*/activate")).permitAll()
                        .requestMatchers(internal.matcher(HttpMethod.GET, "/applications/public")).permitAll()
                        .requestMatchers(internal.matcher(HttpMethod.GET, "/publications/public")).permitAll()
                        .requestMatchers(internal.matcher("/**")).authenticated())
                // default CORS : no configuration, so block preflight and let the rest
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf
                        // CSRF token managed by SpringSecurity but MANUALLY setted in the controller
                        // after successfull login
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // we could active CSRF protection on whole endpoint but we will need a
                        // /internal/csrf GET endpoint
                        .ignoringRequestMatchers(
                            internal.matcher(HttpMethod.POST, "/session"),
                            internal.matcher(HttpMethod.POST, "/session/code"),
                            internal.matcher(HttpMethod.POST, "/session/code/resend"),
                            internal.matcher(HttpMethod.POST, "/users"),
                            internal.matcher(HttpMethod.PUT, "/users/*/activate")
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
}