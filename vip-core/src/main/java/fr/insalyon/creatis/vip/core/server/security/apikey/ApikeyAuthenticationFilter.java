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
package fr.insalyon.creatis.vip.core.server.security.apikey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by abonnet on 10/6/16.
 *
 * Servlet filter that creates the api key token and calls the authentication.
 */
public class ApikeyAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String apikeyHeader;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationProvider authenticationProvider;

    public ApikeyAuthenticationFilter(
            String apikeyHeader,
            AuthenticationEntryPoint authenticationEntryPoint,
            AuthenticationProvider authenticationProvider) {
        this.apikeyHeader = apikeyHeader;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.authenticationProvider,
                "An AuthenticationProvider is required");

        Assert.notNull(this.authenticationEntryPoint,
                "An AuthenticationEntryPoint is required");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String apikey = request.getHeader(apikeyHeader);

        if (apikey == null) {
            logger.debug("no apikey header " + apikeyHeader +" found.");
            filterChain.doFilter(request, response);
            return;
        }
        try {

            logger.debug("apikey header found.");

            ApikeyAuthenticationToken authRequest = new ApikeyAuthenticationToken(apikey);
            Authentication authResult = this.authenticationProvider.authenticate(authRequest);

            logger.debug("Authentication success for : " + authResult);

            SecurityContextHolder.getContext().setAuthentication(authResult);

        }
        catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            logger.debug("Authentication request failed", failed);
            this.authenticationEntryPoint.commence(request, response, failed);
        }
        catch (Exception failed) {
            SecurityContextHolder.clearContext();

            logger.error("Unexpected error while authenticating ", failed);
            this.authenticationEntryPoint.commence(
                    request,
                    response,
                    new AuthenticationServiceException("Internal Authentication error"));
        }

        filterChain.doFilter(request, response);
    }

}
