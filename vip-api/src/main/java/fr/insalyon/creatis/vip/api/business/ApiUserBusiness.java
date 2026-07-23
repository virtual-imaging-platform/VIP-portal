package fr.insalyon.creatis.vip.api.business;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.AuthenticationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.PasswordBusiness;


@Service
public class ApiUserBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EmailBusiness emailBusiness;
    private final PasswordBusiness passwordBusiness;
    private final AuthenticationBusiness authenticationBusiness;

    @Autowired
    public ApiUserBusiness(PasswordBusiness passwordBusiness, AuthenticationBusiness authenticationBusiness, EmailBusiness emailBusiness) {
        this.emailBusiness = emailBusiness;
        this.passwordBusiness = passwordBusiness;
        this.authenticationBusiness = authenticationBusiness;
    }

    public User signup(User user, String comments) throws VipException {
        logger.info("Signing up with the " + user.getEmail());
        return authenticationBusiness.signup(
                user,
                comments);
    }

    public void sendResetCode(String email) throws VipException {
        emailBusiness.sendResetCode(email);
        logger.info("Sending reset code for user with email: " + email);
    }

    public void resetPassword(String email, String code, String password) throws VipException {
        passwordBusiness.reset(email, code, password);
        logger.info("Resetting password for user with email: " + email);
    }
}
