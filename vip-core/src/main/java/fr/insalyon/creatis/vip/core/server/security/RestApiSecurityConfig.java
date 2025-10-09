package fr.insalyon.creatis.vip.core.server.security;

import  fr.insalyon.creatis.vip.core.server.business.Server;

import fr.insalyon.creatis.vip.core.server.security.apikey.ApikeyAuthenticationFilter;
import fr.insalyon.creatis.vip.core.server.security.apikey.ApikeyAuthenticationProvider;

import fr.insalyon.creatis.vip.core.server.security.oidc.OidcConfig;
import fr.insalyon.creatis.vip.core.server.security.oidc.OidcResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * VIP API configuration for API key and OIDC authentications.
 *
 * Authenticates /rest requests with either:
 * - a static per-user API key, in apikeyAuthenticationFilter()
 * - or an OIDC Bearer token, in oauth2ResourceServer()
 */
@Configuration
@EnableWebSecurity
public class RestApiSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Server server;
    private final VipAuthenticationEntryPoint vipAuthenticationEntryPoint;
    private final ApikeyAuthenticationProvider apikeyAuthenticationProvider;
    private final OidcConfig oidcConfig;
    private final OidcResolver oidcResolver;

    @Autowired
    public RestApiSecurityConfig(
            Server server, ApikeyAuthenticationProvider apikeyAuthenticationProvider,
            VipAuthenticationEntryPoint vipAuthenticationEntryPoint,
            OidcConfig oidcConfig, OidcResolver oidcResolver) {
        this.server = server;
        this.vipAuthenticationEntryPoint = vipAuthenticationEntryPoint;
        this.apikeyAuthenticationProvider = apikeyAuthenticationProvider;
        this.oidcConfig = oidcConfig;
        this.oidcResolver = oidcResolver;
    }

    // Do not make it a bean as it is only used to configure CORS exceptions specific to /rest endpoints
    // It is used in the apiFilterChain method just bellow
    // It allows some CORS exceptions only for the /rest endpoints
    public CorsConfigurationSource restCorsConfigurationSource() {
        // applyPermitDefaultValues allows all origins, GET-HEAD-POST, and all headers
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        // We override with a white list of origins
        configuration.setAllowedOrigins(Arrays.asList(server.getCarminCorsAuthorizedDomains()));
        // We override with all methods (to allow PUT and DELETE)
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // protect all requests of this security chain (the /rest one)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        // Spring Security configuration for /rest API endpoints, common to both API key and OIDC authentications.
        // Note that it is required to used AntPathRequestMatcher.antMatcher() everywhere below,
        // otherwise Spring uses MvcRequestMatcher as the default requestMatchers implementation.
        http
                .securityMatcher(antMatcher("/rest/**"))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(antMatcher("/rest/platform")).permitAll()
                        .requestMatchers(antMatcher("/rest/authenticate")).permitAll()
                        .requestMatchers(antMatcher("/rest/session")).permitAll()
                        .requestMatchers(new RegexRequestMatcher("/rest/pipelines\\?public", "GET")).permitAll()
                        .requestMatchers(antMatcher("/rest/publications")).permitAll()
                        .requestMatchers(antMatcher("/rest/reset-password")).permitAll()
                        .requestMatchers(antMatcher("/rest/register")).permitAll()
                        .requestMatchers(antMatcher("/rest/executions/{executionId}/summary")).hasAnyRole("SERVICE")
                        .requestMatchers(antMatcher("/rest/statistics/**")).hasAnyRole("ADVANCED", "ADMINISTRATOR")
                        .requestMatchers(antMatcher("/rest/admin/**")).hasAnyRole("ADMINISTRATOR")
                        .requestMatchers(antMatcher("/rest/**")).authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(vipAuthenticationEntryPoint))
                // session must be activated otherwise OIDC auth info will be lost when accessing /rest/loginOIDC
                // .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous((anonymous) -> anonymous.disable())
                .cors(corsConfigurer -> corsConfigurer.configurationSource(restCorsConfigurationSource()))
                .headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()))
                .csrf((csrf) -> csrf.disable());
        // API key authentication always active
        http.addFilterBefore(apikeyAuthenticationFilter(), BasicAuthenticationFilter.class);
        // OIDC Bearer token authentication, if enabled
        if (oidcConfig.isOIDCActive()) {
            // We configure each OIDC server with issuerLocation instead of jwks_uri: on first token verification,
            // this does two requests to the relevant OIDC server (obtained from the JWT "iss" field):
            // - a GET .well-known/openid-configuration request to the OIDC server, to get this server jwks_uri
            // - then a GET on the jwks_uri, to get the public key which is then used to verify the token
            // Note that these two requests are done just once per OIDC server (not once per inbound API request).
            // We also use a customized authenticationManagerResolver instead of simpler JwtDecoder bean, so that:
            // - requests to the OIDC server happen at inbound-request-time instead of boot, and can be retried on failure
            // - multiple servers can be supported
            // - on successful authentication, Jwt principal is converted to a User principal, so DB lookup happens only once
            http.oauth2ResourceServer((oauth2) -> oauth2
                    .authenticationManagerResolver(oidcResolver.getAuthenticationManagerResolver()));
        }
        return http.build();
    }

    private ApikeyAuthenticationFilter apikeyAuthenticationFilter() throws Exception {
        return new ApikeyAuthenticationFilter(
                server.getApikeyHeaderName(),
                vipAuthenticationEntryPoint, apikeyAuthenticationProvider);
    }

    /*
        Do not use the default firewall (StrictHttpFirewall) because it blocks
        "//" in url and it is used in gwt rpc calls
     */
    @Bean
    public DefaultHttpFirewall httpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
}
