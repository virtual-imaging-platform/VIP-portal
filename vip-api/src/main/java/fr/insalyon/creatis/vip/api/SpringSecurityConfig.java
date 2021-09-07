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
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.security.SpringCompatibleUser;
import fr.insalyon.creatis.vip.api.security.apikey.ApikeyAuthenticationEntryPoint;
import fr.insalyon.creatis.vip.api.security.apikey.ApikeyAuthentificationConfigurer;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import java.util.function.Supplier;

/**
 * Spring security configuration.
 *
 * It secures by api key all rest requests (except /platform)
 * General configuration is done here (what is secured, session management etc).
 *
 * The custom api key configuration is done in {@link ApikeyAuthentificationConfigurer}
 *
 * Created by abonnet on 7/22/16.
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // authentication done by bean LimitigDaoAuthenticationProvider

    private final ApikeyAuthenticationEntryPoint apikeyAuthenticationEntryPoint;

    private final Environment env;

    @Autowired
    public SpringSecurityConfig(ApikeyAuthenticationEntryPoint apikeyAuthenticationEntryPoint, Environment env) {
        this.apikeyAuthenticationEntryPoint = apikeyAuthenticationEntryPoint;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/rest/platform").permitAll()
                .antMatchers("/rest/authenticate").permitAll()
                .antMatchers("/rest/statistics/**").hasAnyRole("ADVANCED", "ADMINISTRATOR")
                .antMatchers("/rest/**").authenticated()
                .anyRequest().permitAll()
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
