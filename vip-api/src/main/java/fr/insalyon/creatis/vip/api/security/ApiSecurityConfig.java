/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api.security;

import fr.insalyon.creatis.vip.api.CarminProperties;

import fr.insalyon.creatis.vip.api.security.apikey.SpringApiPrincipal;
import fr.insalyon.creatis.vip.api.security.apikey.ApikeyAuthenticationFilter;
import fr.insalyon.creatis.vip.api.security.apikey.ApikeyAuthenticationProvider;
import fr.insalyon.creatis.vip.core.client.bean.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.util.function.Supplier;
import java.util.ArrayList;

import static fr.insalyon.creatis.vip.api.CarminProperties.KEYCLOAK_ACTIVATED;

/**
 * VIP API configuration for API key and OIDC authentications.
 *
 * Authenticates /rest requests with either:
 * - a static per-user API key (ApikeyAuthenticationFilter)
 * - or an OIDC Bearer token (ex-Keycloak). This part is currently work-in-progress,
 *   with org.keycloak currently removed, and proper OIDC connector not implemented yet.
 */
@Configuration
@EnableWebSecurity
@Order(1)
public class ApiSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Environment env;
    private final VipAuthenticationEntryPoint vipAuthenticationEntryPoint;
    private final AuthenticationManager vipAuthenticationManager;

    @Autowired
    public ApiSecurityConfig(
            Environment env, ApikeyAuthenticationProvider apikeyAuthenticationProvider,
            VipAuthenticationEntryPoint vipAuthenticationEntryPoint) {
        this.env = env;
        this.vipAuthenticationEntryPoint = vipAuthenticationEntryPoint;
        // Build our AuthenticationManager instance, with one provider for each authentication method
        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(apikeyAuthenticationProvider);
        // providers.add(oidcAuthenticationProvider);
        this.vipAuthenticationManager = new ProviderManager(providers);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return vipAuthenticationManager;
    }

    protected boolean isOIDCActive() {
        return env.getProperty(KEYCLOAK_ACTIVATED, Boolean.class, Boolean.FALSE);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/rest/**"))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/platform")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/authenticate")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/session")).permitAll()
                        .requestMatchers(new RegexRequestMatcher("/rest/pipelines\\?public", "GET")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/publications")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/reset-password")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/register")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/executions/{executionId}/summary")).hasAnyRole("SERVICE")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/statistics/**")).hasAnyRole("ADVANCED", "ADMINISTRATOR")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/rest/**")).authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(apikeyAuthenticationFilter(), BasicAuthenticationFilter.class)
                //.addFilterBefore(oidcAuthenticationFilter(), BasicAuthenticationFilter.class)
                // session must be activated otherwise OIDC auth info will be lost when accessing /loginEgi
                // .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()))
                .csrf((csrf) -> csrf.disable());
        return http.build();
    }

    @Bean
    public ApikeyAuthenticationFilter apikeyAuthenticationFilter() throws Exception {
        return new ApikeyAuthenticationFilter(
                env.getRequiredProperty(CarminProperties.APIKEY_HEADER_NAME),
                vipAuthenticationEntryPoint, authenticationManager());
    }

    @Service
    public static class CurrentUserProvider implements Supplier<User> {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public User get() {
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return null;
            }
            User user = getApikeyUser(authentication);
            if (user != null) {
                return user;
            }
            // user = getOidcUser(authentication);
            return null;
        }

        private User getApikeyUser(Authentication authentication) {
            if ( ! (authentication.getPrincipal() instanceof SpringApiPrincipal)) {
                return null;
            }
            SpringApiPrincipal springCompatibleUser =
                    (SpringApiPrincipal) authentication.getPrincipal();
            return springCompatibleUser.getVipUser();
        }
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

    /**
     * customize roles to match keycloak roles without ROLE_
     */
    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);
        return mapper;
    }


}
