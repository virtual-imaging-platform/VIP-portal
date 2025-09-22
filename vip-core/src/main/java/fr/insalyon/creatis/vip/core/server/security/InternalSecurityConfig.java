package fr.insalyon.creatis.vip.core.server.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationFilter;
import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class InternalSecurityConfig {

    private final SessionAuthenticationProvider sessionAuthenticationProvider;

    @Autowired
    public InternalSecurityConfig(SessionAuthenticationProvider sessionAuthenticationProvider) {
        this.sessionAuthenticationProvider = sessionAuthenticationProvider;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain getFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(getSessionAuthenticationFilter(), BasicAuthenticationFilter.class)
                .securityMatcher(antMatcher("/internal/**"))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(antMatcher(HttpMethod.POST, "/internal/session")).permitAll()
                        .requestMatchers(antMatcher("/internal/**")).authenticated())
                .cors((cors) -> cors.disable())
                .csrf((csrf) -> csrf
                        // CSRF token managed by SpringSecurity but MANUALLY setted in the controller
                        // after successfull login
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // we could active CSRF protection on whole endpoint but we will need a
                        // /internal/csrf GET endpoint
                        .ignoringRequestMatchers(antMatcher(HttpMethod.POST, "/internal/session")));
        return http.build();
    }

    private SessionAuthenticationFilter getSessionAuthenticationFilter() {
        return new SessionAuthenticationFilter(sessionAuthenticationProvider);
    }
}
