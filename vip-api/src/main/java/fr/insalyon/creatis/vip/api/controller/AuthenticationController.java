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
package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.business.ApiBusiness;
import fr.insalyon.creatis.vip.api.business.ApiUserBusiness;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.api.model.AuthenticationInfo;
import fr.insalyon.creatis.vip.api.model.ResetPasswordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Created by abonnet on 8/21/17.
 */
@RestController
public class AuthenticationController extends ApiController{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApiBusiness apiBusiness;
    private final ApiUserBusiness apiUserBusiness;

    @Autowired
    public AuthenticationController(ApiBusiness apiBusiness, ApiUserBusiness apiUserBusiness) {
        this.apiBusiness = apiBusiness;
        this.apiUserBusiness = apiUserBusiness;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public AuthenticationInfo authenticate(
            @RequestBody @Valid AuthenticationCredentials authenticationCredentials)
            throws ApiException {
        logMethodInvocation(logger,"authenticate", authenticationCredentials.getUsername());
        return apiBusiness.authenticate(authenticationCredentials);
    }

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public AuthenticationInfo createSession(
            @RequestBody @Valid AuthenticationCredentials authenticationCredentials)
            throws ApiException {
        logMethodInvocation(logger,"createSession", authenticationCredentials.getUsername());
        return apiBusiness.authenticateSession(authenticationCredentials);
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public void sendResetPassword(@RequestBody @Valid ResetPasswordDTO resetPassword) throws ApiException {
        logMethodInvocation(logger, "resetPassword", resetPassword.getEmail());
        if (resetPassword.getActivationCode() == null) {
            apiUserBusiness.sendResetCode(resetPassword.getEmail());
            logger.info("reset code of  " + resetPassword.getEmail());
        } else {
            apiUserBusiness.resetPassword(resetPassword.getEmail(), resetPassword.getActivationCode(), resetPassword.getNewPassword());
            logger.info("reset password with activation code: " + resetPassword.getActivationCode());
        }

    }
}
