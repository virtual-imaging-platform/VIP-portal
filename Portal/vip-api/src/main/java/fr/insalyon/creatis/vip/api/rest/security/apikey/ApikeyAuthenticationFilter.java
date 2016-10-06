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
package fr.insalyon.creatis.vip.api.rest.security.apikey;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by abonnet on 10/6/16.
 */
public class ApikeyAuthenticationFilter extends OncePerRequestFilter {

    private final String apikeyHeader;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationManager authenticationManager;

    public ApikeyAuthenticationFilter(
            String apikeyHeader,
            AuthenticationEntryPoint authenticationEntryPoint,
            AuthenticationManager authenticationManager) {
        this.apikeyHeader = apikeyHeader;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.authenticationManager,
                "An AuthenticationManager is required");

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
            this.logger.debug("no apikey header " + apikeyHeader +" found.");
            filterChain.doFilter(request, response);
            return;
        }
        try {

            this.logger.debug("apikey header found.");

            ApikeyAuthenticationToken authRequest = new ApikeyAuthenticationToken(apikey);
            Authentication authResult = this.authenticationManager
                    .authenticate(authRequest);

            this.logger.debug("Authentication success for : " + authResult.getName());

            SecurityContextHolder.getContext().setAuthentication(authResult);

        }
        catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            this.logger.debug("Authentication request for failed: " + failed);
            this.authenticationEntryPoint.commence(request, response, failed);
        }

        filterChain.doFilter(request, response);
    }

}
