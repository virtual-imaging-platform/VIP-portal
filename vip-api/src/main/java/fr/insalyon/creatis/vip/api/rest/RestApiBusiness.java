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
package fr.insalyon.creatis.vip.api.rest;

import fr.insalyon.creatis.vip.api.*;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.api.rest.security.SpringCompatibleUser;
import fr.insalyon.creatis.vip.api.business.ApiContext;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;

/**
 * Used to create an ApiContext in REST requests
 *
 * Created by abonnet on 7/25/16.
 */
@Service
public class RestApiBusiness {

    private final static Logger logger = Logger.getLogger(RestApiBusiness.class);

    @Autowired
    private VipConfigurer vipConfigurer;
    @Autowired
    private ApiContext apiContext;
    @Autowired
    private Environment env;

    @Autowired
    private ConfigurationBusiness configurationBusiness;

    public ApiContext getApiContext(HttpServletRequest request, boolean isAuthenticated) {
        User vipUser = null;
        if (isAuthenticated) {
            // if the user is authenticated, fetch it in the request info
            Authentication authentication = (Authentication) request.getUserPrincipal();
            SpringCompatibleUser springCompatibleUser =
                    (SpringCompatibleUser) authentication.getPrincipal();
            vipUser = springCompatibleUser.getVipUser();
        }
        // configure VIP if it has not been done today
        vipConfigurer.configureIfNecessary();
        // populate the request scoped ApiContext
        if (apiContext != null) {
            apiContext.init(request, null, vipUser);
        }
        return new ApiContext(request, null, vipUser); // TODO is it necessary to always create a new instance ?
    }

    public AuthenticationInfo authenticate(
        AuthenticationCredentials authCreds, Connection connection)
        throws ApiException {
        String username = authCreds.getUsername(), password = authCreds.getPassword();
        logger.debug("Verifying credential for " + username);
        signin(username, password, connection);
        logger.debug("Constructing authentication info for " + username);
        AuthenticationInfo authInfo = new AuthenticationInfo();
        String headerName = env.getProperty(CarminProperties.APIKEY_HEADER_NAME);
        String apikey = getAnApikeyForUser(username, connection); // the username is an email
        authInfo.setHttpHeader(headerName);
        authInfo.setHttpHeaderValue(apikey);
        return authInfo;
    }

    private void signin(String username, String password, Connection connection)
        throws ApiException {
        try {
            // we do not care about the session, we're not in browser action
            configurationBusiness
                .signinWithoutResetingSession(username, password, connection);
            logger.info("Credentials OK for " + username);
        } catch (BusinessException e) {
            logger.error("Error authenticating {" + username + "}. Considered as bad credentials", e);
            throw new ApiException("Authentication Error");
        }
    }

    private String getAnApikeyForUser(String email, Connection connection)
        throws ApiException {
        boolean generateNewApiKey =
                env.getProperty(CarminProperties.APIKEY_GENERATE_NEW_EACH_TIME, Boolean.class);
        try {
            if (generateNewApiKey) {
                logger.info("generating a new apikey for " + email);
                return configurationBusiness
                    .generateNewUserApikey(email, connection);
            } else {
                logger.debug("keeping the current api key for " + email);
                return configurationBusiness.getUserApikey(email, connection);
            }
        } catch (BusinessException e) {
            logger.error("Error dealing with api key");
            throw new ApiException(e);
        }
    }


}
