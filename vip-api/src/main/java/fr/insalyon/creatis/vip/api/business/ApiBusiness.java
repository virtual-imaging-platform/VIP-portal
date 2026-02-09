package fr.insalyon.creatis.vip.api.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationInfo;

@Service
public class ApiBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Server server;
    private final ConfigurationBusiness configurationBusiness;

    public ApiBusiness(final Server server, final ConfigurationBusiness configurationBusiness) {
        this.server = server;
        this.configurationBusiness = configurationBusiness;
    }

    public AuthenticationInfo authenticate(AuthenticationCredentials authCreds) throws VipException {
        return authenticate(authCreds, false);
    }

    public AuthenticationInfo authenticateSession(AuthenticationCredentials authCreds) throws VipException {
        return authenticate(authCreds, true);
    }

    public AuthenticationInfo authenticate(AuthenticationCredentials authCreds, boolean initSession)
            throws VipException {
        String username = authCreds.getUsername(), password = authCreds.getPassword();
        logger.debug("Verifying credential for " + username);
        User user = signin(username, password);
        logger.debug("Constructing authentication info for " + username);
        AuthenticationInfo authInfo = new AuthenticationInfo();
        if (initSession) {
            authInfo.setHttpHeader(CoreConstants.COOKIES_SESSION);
            authInfo.setHttpHeaderValue(user.getSession());
        } else {
            authInfo.setHttpHeader(server.getApikeyHeaderName());
            String apikey = getAnApikeyForUser(username); // the username is an email
            authInfo.setHttpHeaderValue(apikey);
        }
        return authInfo;
    }

    private User signin(String username, String password) throws VipException {
        try {
            // we do not care about the session, we're not in browser action
            User user = configurationBusiness
                    .signin(username, password);
            logger.info("Credentials OK for " + username);
            return user;
        } catch (VipException e) {
            if (e.getMessage().startsWith("Authentication failed")) {
                throw new VipException(DefaultError.BAD_CREDENTIALS);
            }
            throw new VipException("Authentication Error", e);
        }
    }

    private String getAnApikeyForUser(String email) throws VipException {
        boolean generateNewApiKey = server.getCarminApikeyGenerateNewEachTime();

        if (generateNewApiKey) {
            logger.info("generating a new apikey for " + email);
            return configurationBusiness.generateNewUserApikey(email);
        } else {
            logger.debug("keeping the current api key for " + email);
            return configurationBusiness.getUserApikey(email);
        }
    }
}
