package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.rpc.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author khalilkes service to signup a user in VIP
 */
@Service
public class ApiUserBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Environment env;
    private final ConfigurationBusiness configurationBusiness;

    public ApiUserBusiness(Environment env, ConfigurationBusiness configurationBusiness) {
        this.env = env;
        this.configurationBusiness = configurationBusiness;
    }

    /**
     *
     * @param user
     * @param comments
     * @throws ApiException
     */
    public void signup(User user, String comments) throws ApiException {
        try {
            configurationBusiness
                    .signup(user, comments, false, true, null);
            logger.info("Signing up with the " + user.getEmail());
        } catch (BusinessException e) {
            throw new ApiException("Signing up Error", e);
        }
    }

    public void resetCode(String email) throws ApiException {
        try {
            configurationBusiness.sendResetCode(email);
            logger.info("Resetting password for user with email: " + email);
        } catch (BusinessException e) {
            throw new ApiException("Error resetting password", e);
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
