package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.CarminProperties;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.api.model.AuthenticationInfo;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class ApiBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Environment env;
    private final ConfigurationBusiness configurationBusiness;

    public ApiBusiness(Environment env, ConfigurationBusiness configurationBusiness) {
        this.env = env;
        this.configurationBusiness = configurationBusiness;
    }

    public AuthenticationInfo authenticate(AuthenticationCredentials authCreds) throws ApiException {
        return authenticate(authCreds, false);
    }

    public AuthenticationInfo authenticateSession(AuthenticationCredentials authCreds) throws ApiException {
        return authenticate(authCreds, true);
    }

    public AuthenticationInfo authenticate(AuthenticationCredentials authCreds, boolean initSession)
            throws ApiException {
        String username = authCreds.getUsername(), password = authCreds.getPassword();
        logger.debug("Verifying credential for " + username);
        User user = signin(username, password);
        logger.debug("Constructing authentication info for " + username);
        AuthenticationInfo authInfo = new AuthenticationInfo();
        if (initSession) {
            authInfo.setHttpHeader(CoreConstants.COOKIES_SESSION);
            authInfo.setHttpHeaderValue(user.getSession());
        } else {
            authInfo.setHttpHeader(env.getProperty(CarminProperties.APIKEY_HEADER_NAME));
            String apikey = getAnApikeyForUser(username); // the username is an email
            authInfo.setHttpHeaderValue(apikey);
        }
        return authInfo;
    }

    private User signin(String username, String password) throws ApiException {
        try {
            // we do not care about the session, we're not in browser action
            User user = configurationBusiness
                    .signin(username, password);
            logger.info("Credentials OK for " + username);
            return user;
        } catch (BusinessException e) {
            if (e.getMessage().startsWith("Authentication failed")) {
                throw new ApiException(ApiException.ApiError.BAD_CREDENTIALS);
            }
            throw new ApiException("Authentication Error", e);
        }
    }

    private String getAnApikeyForUser(String email) throws ApiException {
        boolean generateNewApiKey = env.getRequiredProperty(
                CarminProperties.APIKEY_GENERATE_NEW_EACH_TIME, Boolean.class);
        try {
            if (generateNewApiKey) {
                logger.info("generating a new apikey for " + email);
                return configurationBusiness.generateNewUserApikey(email);
            } else {
                logger.debug("keeping the current api key for " + email);
                return configurationBusiness.getUserApikey(email);
            }
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }
}
