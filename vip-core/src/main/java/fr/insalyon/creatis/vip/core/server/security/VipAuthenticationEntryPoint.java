package fr.insalyon.creatis.vip.core.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.core.server.model.ErrorCodeAndMessage;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.server.model.ErrorCodeAndMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Entry point that writes error response in json with a Jackson object mapper.
 */
@Component
public class VipAuthenticationEntryPoint implements AuthenticationEntryPoint, AuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;
    private final Server server;

    @Autowired
    public VipAuthenticationEntryPoint(ObjectMapper objectMapper, Server server) {
        this.objectMapper = objectMapper;
        this.server = server;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.onAuthenticationFailure(request, response, authException);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // OIDC resource server handler may already have set this header
        if ( ! response.containsHeader("WWW-Authenticate")) {
            response.addHeader("WWW-Authenticate", "header; header-name=" + server.getApikeyHeaderName());
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorCodeAndMessage error = new ErrorCodeAndMessage();
        logger.debug("handling auth error", authException);
        if (authException instanceof BadCredentialsException) {
            error.setErrorCode(DefaultError.BAD_CREDENTIALS.getCode());
        } else if (authException instanceof InsufficientAuthenticationException ||
                authException instanceof AuthenticationCredentialsNotFoundException) {
            // These two exceptions are raised when a request contains no credentials:
            // InsufficientAuthenticationException if anonymous requests are enabled
            // AuthenticationCredentialsNotFoundException if anonymous requests are disabled
            // We map them both to the same API error to preserve historical behaviour.
            error.setErrorCode(DefaultError.INSUFFICIENT_AUTH.getCode());
        } else {
            error.setErrorCode(DefaultError.AUTHENTICATION_ERROR.getCode());
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        error.setErrorMessage(authException.getMessage());
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
