package fr.insalyon.creatis.vip.core.server.security;

import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.security.oidc.OidcLoginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring security configuration.
 *
 * Defines Spring security settings for all inbound requests on paths other than /rest/**,
 * which is handled in {@link RestApiSecurityConfig}
 *
 * Created by abonnet on 7/22/16.
 */

@Configuration
@EnableWebSecurity
public class GeneralSecurityConfig {

    private final Server server;
    private final OidcLoginConfig oidcLoginConfig;

    @Autowired
    public GeneralSecurityConfig(Server server, OidcLoginConfig oidcLoginConfig) {
        this.server = server;
        this.oidcLoginConfig = oidcLoginConfig;
    }

    // Global CORS configuration used when cors is configured with ".cors(Customizer.withDefaults())" in the filter chain
    // The bean has to be named corsConfigurationSource to be picked up by spring security
    // It forbids any CORS request (preflight or not)
    // It is overridden for /rest in RestApiSecurityConfig
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // default constructor allows nothing
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Protect all requests of this security chain configured withDefaults ("/internal" and "/").
        // the "/rest" security chain has another config with exceptions
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Order(3)
    public SecurityFilterChain generalFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .cors(Customizer.withDefaults())
                .headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()))
                .csrf((csrf) -> csrf.disable());
        if (oidcLoginConfig.getLoginProviders().size() > 0) {
            http.oauth2Login((oauth2)->oauth2
                    .clientRegistrationRepository(oidcLoginConfig.getClientRegistrationRepository())
                    .authorizationEndpoint((authorization)->authorization
                            .baseUri("/oauth2/authorize-client")
                            .authorizationRequestRepository(new HttpSessionOAuth2AuthorizationRequestRepository()))
                    .tokenEndpoint((token)->token
                            .accessTokenResponseClient(new DefaultAuthorizationCodeTokenResponseClient()))
                    .defaultSuccessUrl("/rest/loginOIDC")
                    .failureUrl("/loginFailure"));
        }
        return http.build();
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
