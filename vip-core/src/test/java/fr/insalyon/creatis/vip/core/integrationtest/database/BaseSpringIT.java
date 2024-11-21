package fr.insalyon.creatis.vip.core.integrationtest.database;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Utility superclass to launch tests with the whole spring configuration, as
 * in production. Subclass only need to extend it and can benefit from dependency
 * injection.
 *
 *
 * The "test" profile overrides all the external dependencies
 * that would throw exception by mocked and configurable ones.
 * The "test-db" profile disable the default jndi datasource and uses a
 * h2 in-memory database instead
 */

@SpringJUnitConfig(SpringCoreConfig.class)
// launch all spring environment for testing, also take test bean though automatic package scan
@ActiveProfiles({"test-db", "test"}) // to take random h2 database and not the test h2 jndi one
@TestPropertySource(properties = {
        "db.tableEngine=",             // to disable the default mysql/innodb engine on database init
        "vipConfigFolder=classpath:"}) // to find vip-api.conf for vip-api tests
@Transactional // each test is in a transaction that is rollbacked at the end to always leave a "clean" state
public abstract class BaseSpringIT {

    protected final String emailUser1 = "test1@test.fr";
    protected final String emailUser2 = "test2@test.fr";
    protected final String emailUser3 = "test3@test.fr";
    protected final String emailUser4 = "test4@test.fr";
    protected final String nameGroup1 = "group1";
    @Autowired
    protected ApplicationContext appContext;
    @Autowired
    protected ConfigurationBusiness configurationBusiness;
    @Autowired
    @Qualifier("db-datasource")
    protected DataSource dataSource; // this is a mockito spy wrapping the h2 memory datasource
    @Autowired
    protected DataSource lazyDataSource;
    @Autowired
    protected EmailBusiness emailBusiness;
    @Autowired
    protected Server server;
    @Autowired
    protected GRIDAClient gridaClient;
    protected String adminEmail = "test-admin@test.com";
    protected User user1;
    protected User user2;
    protected User user3;
    protected User user4;
    protected User admin;
    protected Group group1;
    protected Group group2;
    protected List<String> applicationClasses = new ArrayList<>();

    protected User nonExistentUser = new User("test firstName suffix0",
            "test lastName suffix0", "unexisting_user@test.fr", "institution",
            "testPassword", CountryCode.fr,
            null);

    @BeforeEach
    protected void setUp() throws Exception {
        ServerMockConfig.reset(server);
        Mockito.reset(gridaClient);
        Mockito.doReturn(new String[]{"test@admin.test"}).when(emailBusiness).getAdministratorsEmails();
    }

    protected void assertRowsNbInTable(String tableName, int expectedNb) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        int rowsNb = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        assertEquals(expectedNb, rowsNb);
    }

    protected void createUser(String testEmail) throws GRIDAClientException, BusinessException {
        createUser(testEmail, "");
    }

    protected void createUserWithPassword(String testEmail, String password) throws GRIDAClientException, BusinessException {
        createUser(testEmail, "", password);
    }

    protected void createUser(String testEmail, String nameSuffix) throws GRIDAClientException, BusinessException {
        createUser(testEmail, nameSuffix, "testPassword");
    }

    protected void createUser(String testEmail, String nameSuffix, String password) throws GRIDAClientException, BusinessException {
        User newUser = new User("test firstName " + nameSuffix,
                "test lastName " + nameSuffix, testEmail, "test institution",
                password, CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        configurationBusiness.signup(newUser, "", (Group) null);
    }

    protected void createUserInGroup(String userEmail, String groupName) throws BusinessException, GRIDAClientException {
        createUserInGroup(userEmail, "", groupName);
    }

    protected void createUserInGroup(String userEmail, String nameSuffix, String groupName) throws BusinessException, GRIDAClientException {
        createUserInGroups(userEmail, nameSuffix, groupName);
    }

    public void createGroup(String groupName) throws BusinessException {
        configurationBusiness.addGroup(new Group(groupName, true, GroupType.APPLICATION));
    }

    public void createGroup(String groupName, GroupType type) throws BusinessException {
        configurationBusiness.addGroup(new Group(groupName, true, type));
    }

    protected void createUserInGroups(String userEmail, String nameSuffix, String... groupNames) throws BusinessException, GRIDAClientException {
        User newUser = new User("test firstName " + nameSuffix,
                "test lastName " + nameSuffix, userEmail, "test institution",
                "testPassword", CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        List<Group> groups = new ArrayList<>();
        for (String groupName : groupNames) {
            groups.add(configurationBusiness.getGroup(groupName));
        }
        configurationBusiness.signup(newUser, "", false, true, groups);
    }

    protected Date getNextSecondDate() {
        return new Date(new Date().getTime() + (1000));
    }


}
