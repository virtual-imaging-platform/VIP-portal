package fr.insalyon.creatis.vip.api.business;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;


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
     * @throws ApiException
     */
    public void signup(User user, String comments) throws ApiException {
        try {
            configurationBusiness.signup(
                user, 
                comments,
                false, 
                true, 
                new ArrayList<>());
            logger.info("Signing up with the " + user.getEmail());
        } catch (VipException e) {
            throw new ApiException("Signing up Error", e);
        }
    }



    public void sendResetCode(String email) throws ApiException {
        try {
            configurationBusiness.sendResetCode(email);
            logger.info("Sending reset code for user with email: " + email);
        } catch (VipException e) {
            throw new ApiException("Error sending reset password", e);
        }
    }

    public void resetPassword(String email, String code, String password) throws ApiException {
        try {
            configurationBusiness.resetPassword(email, code, password);
            logger.info("Resetting password for user with email: " + email);
        } catch (VipException e) {
            throw new ApiException("Error resetting password", e);
        }
    }
}
