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
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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
    @Autowired
    @Qualifier(value = "MD5PasswordEncoder")
    public void setPasswordEncoder(Object passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
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
