package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author khalilkes service to signup a user in VIP
 */
@Service
public class ApiUserBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Environment env;
    private final ConfigurationBusiness configurationBusiness;
    private final ApplicationBusiness applicationBusiness;

    public ApiUserBusiness(Environment env, ConfigurationBusiness configurationBusiness, ApplicationBusiness applicationBusiness) {
        this.env = env;
        this.configurationBusiness = configurationBusiness;
        this.applicationBusiness = applicationBusiness;
    }

    /**
     *
     * @param user
     * @param comments
     * @param applicationNames
     * @throws ApiException
     */
    public void signup(User user, String comments, List<String> applicationNames) throws ApiException {
        try {
            Set<Group> allGroups = new TreeSet<>(Comparator.comparing(Group::getName));
            for (String applicationName : applicationNames) {
                List<Group> appGroups = applicationBusiness.getPublicGroupsForApplication(applicationName);
                allGroups.addAll(appGroups);
            }
            configurationBusiness.signup(user, comments, false, true, new ArrayList<>(allGroups));
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
