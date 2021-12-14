package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.CarminProperties;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.api.model.AuthenticationInfo;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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

    public AuthenticationInfo authenticate(AuthenticationCredentials authCreds)
            throws ApiException {
        String username = authCreds.getUsername(), password = authCreds.getPassword();
        logger.debug("Verifying credential for " + username);
        signin(username, password);
        logger.debug("Constructing authentication info for " + username);
        AuthenticationInfo authInfo = new AuthenticationInfo();
        String headerName = env.getProperty(CarminProperties.APIKEY_HEADER_NAME);
        String apikey = getAnApikeyForUser(username); // the username is an email
        authInfo.setHttpHeader(headerName);
        authInfo.setHttpHeaderValue(apikey);
        return authInfo;
    }

    private void signin(String username, String password) throws ApiException {
        try {
            // we do not care about the session, we're not in browser action
            configurationBusiness
                    .signinWithoutResetingSession(username, password);
            logger.info("Credentials OK for " + username);
        } catch (BusinessException e) {
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


    //get user by email
    public User getUser(String email) throws ApiException {
        try {
            User user = configurationBusiness.getUser(email);
            logger.info("Found the user with " + email);
            return user;
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }
}
