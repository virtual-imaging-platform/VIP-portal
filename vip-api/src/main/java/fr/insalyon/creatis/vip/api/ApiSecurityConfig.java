package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.security.SpringCompatibleUser;
import fr.insalyon.creatis.vip.api.security.apikey.ApikeyAuthenticationEntryPoint;
import fr.insalyon.creatis.vip.api.security.apikey.ApikeyAuthentificationConfigurer;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Supplier;

@EnableWebSecurity
@Order(1)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApikeyAuthenticationEntryPoint apikeyAuthenticationEntryPoint;

    private final Environment env;

    @Autowired
    public ApiSecurityConfig(ApikeyAuthenticationEntryPoint apikeyAuthenticationEntryPoint, Environment env) {
        this.apikeyAuthenticationEntryPoint = apikeyAuthenticationEntryPoint;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/rest/**")
                .authorizeRequests()
                .antMatchers("/rest/platform").permitAll()
                .antMatchers("/rest/loginEgi").permitAll()
                .antMatchers("/rest/authenticate").permitAll()
                .antMatchers("/rest/statistics/**").hasAnyRole("ADVANCED", "ADMINISTRATOR")
                .anyRequest().authenticated()
                .and()
                .apply(new ApikeyAuthentificationConfigurer<>(
                        env.getRequiredProperty(CarminProperties.APIKEY_HEADER_NAME),
                        apikeyAuthenticationEntryPoint))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().and()
                .headers().frameOptions().sameOrigin().and()
                .csrf().disable();
    }

    @Bean
    public Supplier<User> currentUserProvider() {
        return () -> {
            // get VIP user from the spring one
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
            if ( authentication == null ||
                    !  (authentication.getPrincipal() instanceof SpringCompatibleUser)) {
                // anonymous
                return null;
            }
            SpringCompatibleUser springCompatibleUser =
                    (SpringCompatibleUser) authentication.getPrincipal();
            return springCompatibleUser.getVipUser();
        };
    }
}
