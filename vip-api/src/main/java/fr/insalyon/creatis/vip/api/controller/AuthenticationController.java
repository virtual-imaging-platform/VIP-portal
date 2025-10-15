package fr.insalyon.creatis.vip.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.api.business.ApiBusiness;
import fr.insalyon.creatis.vip.api.business.ApiUserBusiness;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.ResetPasswordDTO;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationInfo;
import jakarta.validation.Valid;

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
