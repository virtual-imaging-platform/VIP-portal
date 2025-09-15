package fr.insalyon.creatis.vip.core.server.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class InternalSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain getFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(antMatcher("/internal/**"))
                .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers(antMatcher(HttpMethod.POST, "/internal/session")).permitAll()
                    .requestMatchers(antMatcher("/internal/**")).authenticated()
                )
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable()); // SETUP CSRF BEFORE PRODUCTION
        return http.build();
    }
}
