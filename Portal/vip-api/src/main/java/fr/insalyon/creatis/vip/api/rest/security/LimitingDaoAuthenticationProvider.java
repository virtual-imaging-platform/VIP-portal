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
package fr.insalyon.creatis.vip.api.rest.security;

import fr.insalyon.creatis.devtools.MD5;
import fr.insalyon.creatis.vip.core.server.dao.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by abonnet on 7/25/16.
 *
 * Extends spring authentication provicer to update failed authentications attempts
 * counter
 */
@Service
public class LimitingDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public static final Logger logger = Logger.getLogger(LimitingDaoAuthenticationProvider.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // we should not rely on the password from the user loaded (whcih does not include it)
        // the only acceptable method is userDao.authenticate.
        try {
            String encodedpassword = MD5.get(authentication.getCredentials().toString());
            if (!userDAO.authenticate(
                    userDetails.getUsername(),
                    encodedpassword)) {
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
            }
        } catch (DAOException e) {
            logger.error("error authenticating user " + userDetails.getUsername(), e);
            logger.error("Doing as if there is an auth error");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        } catch (NoSuchAlgorithmException e) {
            logger.error("error authenticating user " + userDetails.getUsername(), e);
            logger.error("Doing as if there is an auth error");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        } catch (UnsupportedEncodingException e) {
            logger.error("error authenticating user " + userDetails.getUsername(), e);
            logger.error("Doing as if there is an auth error");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        try {
            return super.authenticate(authentication);
        } catch (BadCredentialsException e) {
            increaseFailedAuth(username);
            throw e;
        }
    }

    protected void increaseFailedAuth(String username) {
        try {
            logger.info("wrong credentials, increasing fail auths for" + username);
            userDAO.incNFailedAuthentications(username);
            if (userDAO.getNFailedAuthentications(username) > 5) {
                logger.info("too many failed attempts for" + username + ". Locking the account");
                userDAO.lock(username);
            }
        } catch (Exception e) {
            logger.error("error increasing failed auth counter for :" + username);
        }
    }
}
