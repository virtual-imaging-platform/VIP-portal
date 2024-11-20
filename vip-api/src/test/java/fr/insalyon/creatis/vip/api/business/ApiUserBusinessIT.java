package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static fr.insalyon.creatis.vip.core.client.view.user.UserLevel.Beginner;

@WebAppConfiguration
public class ApiUserBusinessIT extends BaseSpringIT {
    @Autowired
    ApiUserBusiness apiUserBusiness;
    @Autowired
    private ConfigurationBusiness configurationBusiness;
    @Autowired
    private ApplicationBusiness applicationBusiness;
    @Autowired
    private Environment env;
    private Group group1;
    private User user1;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Create test group
        group1 = new Group("group1", true, GroupType.getDefault());
        configurationBusiness.addGroup(group1);

        // Create test users
        user1 = new User("firstName", "lastName", "email1@test.fr", "test1@test.fr", "institution", "password", false, "code", "folder", "session", new Date(), new Date(), Beginner, CountryCode.fr, 1, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0, false);
        apiUserBusiness.signup(user1, "comment", new ArrayList<>());

    }

    @Test
    public void testInitialization() throws ApiException, BusinessException {
        Assertions.assertEquals(2, configurationBusiness.getUsers().size(), "Incorrect number of users"); // admin + user1
    }

    @Test
    public void testSignup() throws ApiException, BusinessException {
        User user2 = new User("firstName2", "lastName2", "email2@test.fr", "test3@test.fr", "institution", "password", false, "code", "folder", "session", new Date(), new Date(), Beginner, CountryCode.fr, 1, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0, false);
        apiUserBusiness.signup(user2, "comment", new ArrayList<>());
        Assertions.assertEquals(3, configurationBusiness.getUsers().size(), "Incorrect number of users");
    }

    @Test
    public void testResetPassword() throws ApiException, BusinessException {
        apiUserBusiness.resetPassword("email1@test.fr", configurationBusiness.getUser("email1@test.fr").getCode(), "test new password");
    }

    @Test
    public void testResetCode() throws ApiException, BusinessException {
        String oldCode = configurationBusiness.getUser("email1@test.fr").getCode();
        apiUserBusiness.sendResetCode("email1@test.fr");
        String newCode = configurationBusiness.getUser("email1@test.fr").getCode();
        Assertions.assertFalse(oldCode.equals(newCode));
    }

}
