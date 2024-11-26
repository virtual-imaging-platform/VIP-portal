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

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.model.ErrorCodeAndMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Created by abonnet on 7/26/16.
 *
 * Entry point that writes error response in json with a Jackson object mapper.
 */
@Component
public class VipAuthenticationEntryPoint implements AuthenticationEntryPoint, AuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;

    @Autowired
    public VipAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.onAuthenticationFailure(request, response, authException);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // OIDC resource server handler may already have set this header
        if ( ! response.containsHeader("WWW-Authenticate")) {
            response.addHeader("WWW-Authenticate", "API-key");
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorCodeAndMessage error = new ErrorCodeAndMessage();
        logger.debug("handling auth error", authException);
        if (authException instanceof BadCredentialsException) {
            error.setErrorCode(ApiError.BAD_CREDENTIALS.getCode());
        } else if (authException instanceof InsufficientAuthenticationException) {
            error.setErrorCode(ApiError.INSUFFICIENT_AUTH.getCode());
        } else {
            error.setErrorCode(ApiError.AUTHENTICATION_ERROR.getCode());
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        error.setErrorMessage(authException.getMessage());
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
