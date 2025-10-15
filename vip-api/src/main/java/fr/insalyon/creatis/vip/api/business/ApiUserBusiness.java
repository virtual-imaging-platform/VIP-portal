package fr.insalyon.creatis.vip.api.business;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;


@Service
public class ApiUserBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigurationBusiness configurationBusiness;

    public ApiUserBusiness(ConfigurationBusiness configurationBusiness) {
        this.configurationBusiness = configurationBusiness;
    }

    /**
     *
     * @param user
     * @param comments
     * @param applicationNames
     * @throws VipException
     */
    public void signup(User user, String comments) throws VipException {
        try {
            configurationBusiness.signup(
                user, 
                comments,
                false, 
                true, 
                new ArrayList<>());
            logger.info("Signing up with the " + user.getEmail());
        } catch (VipException e) {
            throw new VipException("Signing up Error", e);
        }
    }



    public void sendResetCode(String email) throws VipException {
        try {
            configurationBusiness.sendResetCode(email);
            logger.info("Sending reset code for user with email: " + email);
        } catch (VipException e) {
            throw new VipException("Error sending reset password", e);
        }
    }

    public void resetPassword(String email, String code, String password) throws VipException {
        try {
            configurationBusiness.resetPassword(email, code, password);
            logger.info("Resetting password for user with email: " + email);
        } catch (VipException e) {
            throw new VipException("Error resetting password", e);
        }
    }
}
