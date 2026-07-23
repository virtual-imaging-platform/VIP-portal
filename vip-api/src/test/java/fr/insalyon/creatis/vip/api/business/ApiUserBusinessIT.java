package fr.insalyon.creatis.vip.api.business;

import java.sql.Timestamp;

import fr.insalyon.creatis.vip.core.models.Group;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;

public class ApiUserBusinessIT extends BaseSpringIT {

    @Autowired private ApiUserBusiness apiUserBusiness;

    private Group group1;
    private User user1;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Create test group
        group1 = new Group("group1", true, GroupType.getDefault());
        asAdminContext(() -> {
           groupBusiness.add(group1); 
        });

        // Create test users
        user1 = new User("firstName", "lastName", emailUser1, "institution", CountryCode.fr);
        user1 = apiUserBusiness.signup(user1, "comment");

    }

    @Test
    public void testInitialization() throws VipException {
        Assertions.assertEquals(2, userBusiness.getUsers().size(), "Incorrect number of users"); // admin + user1
    }

    @Test
    public void testSignup() throws VipException {
        User user2 = new User("firstName2", "lastName2", emailUser2, "institution", CountryCode.fr);
        apiUserBusiness.signup(user2, "comment");

        Assertions.assertEquals(3, userBusiness.getUsers().size(), "Incorrect number of users");
    }

    @Test
    public void testResetPassword() throws VipException {
        apiUserBusiness.resetPassword(emailUser1, userBusiness.getUser(emailUser1).getCode(), "test new password");
    }

    @Test
    public void testResetCode() throws VipException {
        String oldCode = userBusiness.getUser(emailUser1).getCode();
        apiUserBusiness.sendResetCode(emailUser1);
        String newCode = userBusiness.getUser(emailUser1).getCode();
        Assertions.assertFalse(oldCode.equals(newCode));
    }

}
