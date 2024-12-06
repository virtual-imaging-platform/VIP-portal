package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @author khalilkes service to signup a user in VIP
 */
@Service
public class ApiUserBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigurationBusiness configurationBusiness;
    private final GroupBusiness groupBusiness;

    public ApiUserBusiness(ConfigurationBusiness configurationBusiness, GroupBusiness groupBusiness) {
        this.configurationBusiness = configurationBusiness;
        this.groupBusiness = groupBusiness;
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
                groupBusiness.getPublic());
            logger.info("Signing up with the " + user.getEmail());
        } catch (BusinessException e) {
            throw new ApiException("Signing up Error", e);
        }
    }



    public void sendResetCode(String email) throws ApiException {
        try {
            configurationBusiness.sendResetCode(email);
            logger.info("Sending reset code for user with email: " + email);
        } catch (BusinessException e) {
            throw new ApiException("Error sending reset password", e);
        }
    }

    public void resetPassword(String email, String code, String password) throws ApiException {
        try {
            configurationBusiness.resetPassword(email, code, password);
            logger.info("Resetting password for user with email: " + email);
        } catch (BusinessException e) {
            throw new ApiException("Error resetting password", e);
        }
    }
}
