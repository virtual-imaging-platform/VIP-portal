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
                    .requestMatchers(antMatcher("/internal/**")).authenticated()
                )
                .anonymous((anonymous) -> anonymous.disable())
                .cors((cors) -> cors.disable())
                .csrf((csrf) -> csrf.disable());// SETUP CSRF BEFORE PRODUCTION
        return http.build();
    }

    private SessionAuthenticationFilter getSessionAuthenticationFilter() {
        return new SessionAuthenticationFilter(sessionAuthenticationProvider);
    }    
}
