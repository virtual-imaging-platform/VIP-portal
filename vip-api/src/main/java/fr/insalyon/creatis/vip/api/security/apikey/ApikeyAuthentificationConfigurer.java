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
package fr.insalyon.creatis.vip.api.security.apikey;

import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.accept.*;

import java.util.*;

/**
 * add apikey filter to spring security filter chain
 * inspired from {@link HttpBasicConfigurer}
 *
 * Created by abonnet on 10/6/16.
 */
public class ApikeyAuthentificationConfigurer<B extends HttpSecurityBuilder<B>> extends
        SecurityConfigurerAdapter<DefaultSecurityFilterChain,B> {

    private String apikeyHeader;
    private AuthenticationEntryPoint authenticationEntryPoint;


    public ApikeyAuthentificationConfigurer(String apikeyHeader,
                                            AuthenticationEntryPoint authenticationEntryPoint) {
        this.apikeyHeader = apikeyHeader;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void init(B http) throws Exception {
        registerDefaults(http);
    }

    private void registerDefaults(B http) {
        ContentNegotiationStrategy contentNegotiationStrategy = http
                .getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher restMatcher = new MediaTypeRequestMatcher(
                contentNegotiationStrategy, MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_XML,
                MediaType.MULTIPART_FORM_DATA, MediaType.TEXT_XML);
        restMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));

        RequestMatcher notHtmlMatcher = new NegatedRequestMatcher(
                new MediaTypeRequestMatcher(contentNegotiationStrategy,
                        MediaType.TEXT_HTML));
        RequestMatcher preferredMatcher = new AndRequestMatcher(
                Arrays.asList(notHtmlMatcher, restMatcher));

        registerDefaultEntryPoint(http, preferredMatcher);
        registerDefaultLogoutSuccessHandler(http, preferredMatcher);
    }

    private void registerDefaultEntryPoint(B http, RequestMatcher preferredMatcher) {
        ExceptionHandlingConfigurer<B> exceptionHandling = http
                .getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        exceptionHandling.defaultAuthenticationEntryPointFor(
                postProcess(this.authenticationEntryPoint), preferredMatcher);
    }

    private void registerDefaultLogoutSuccessHandler(B http, RequestMatcher preferredMatcher) {
        LogoutConfigurer<B> logout = http
                .getConfigurer(LogoutConfigurer.class);
        if (logout == null) {
            return;
        }
        LogoutConfigurer<B> handler = logout.defaultLogoutSuccessHandlerFor(
                postProcess(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)), preferredMatcher);
    }

    @Override
    public void configure(B http) throws Exception {
        AuthenticationManager authenticationManager = http
                .getSharedObject(AuthenticationManager.class);
        ApikeyAuthenticationFilter authenticationFilter =
                new ApikeyAuthenticationFilter(
                        apikeyHeader, authenticationEntryPoint, authenticationManager);
        authenticationFilter = postProcess(authenticationFilter);
        http.addFilterAfter(authenticationFilter,
                BasicAuthenticationFilter.class);
    }
}
