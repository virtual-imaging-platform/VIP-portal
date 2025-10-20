package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.api.model.SignUpUserDTO;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.security.common.SpringPrincipalUser;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;


public class UserTestUtils {

    static public User baseUser1;
    static public User baseUser2;
    static public User baseUser3;
    static public User baseUser4;

    static public SignUpUserDTO restUser1;

    static public String baseUser1Password = "baseUser1password";
    static public String baseUser2Password = "baseUser2password";


    static {
        reset();
    }

    static public void reset() {
        baseUser1 = new User("base1", "User1", "baseuser1@test.tst", null,
                UserLevel.Beginner, null);
        baseUser1.setFolder("user1");
        baseUser2 = new User("base2", "User2", "baseuser2@test.tst", null,
                UserLevel.Advanced, null);
        baseUser2.setFolder("user2");
        baseUser3 = new User("base3", "User3", "baseuser3@test.tst", null,
                UserLevel.Beginner, null);
        baseUser3.setFolder("user3");
        baseUser4 = new User("base4", "User4", "baseuser4@test.tst", null,
                UserLevel.Beginner, null);
        baseUser4.setFolder("user4");

        restUser1 = new SignUpUserDTO("base3", "User3", "baseuser3@test.tst", "test", baseUser2Password, CountryCode.lc, "test comment");
    }

    public static RequestPostProcessor baseUser1() {
        return SecurityMockMvcRequestPostProcessors.user(new SpringPrincipalUser(baseUser1));
    }

    public static RequestPostProcessor baseUser2() {
        return SecurityMockMvcRequestPostProcessors.user(new SpringPrincipalUser(baseUser2));
    }

    public static RequestPostProcessor baseUser3() {
        return SecurityMockMvcRequestPostProcessors.user(new SpringPrincipalUser(baseUser3));
    }

    public static RequestPostProcessor baseUser4() {
        return SecurityMockMvcRequestPostProcessors.user(new SpringPrincipalUser(baseUser4));
    }
}
