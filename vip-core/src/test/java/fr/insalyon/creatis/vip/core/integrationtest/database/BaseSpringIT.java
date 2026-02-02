package fr.insalyon.creatis.vip.core.integrationtest.database;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.integrationtest.TestConfigurer;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.security.common.SpringPrincipalUser;
import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationProvider;

import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Base superclass to launch tests with the whole "root" spring application context.
 * This does not include the children web application context (/rest and /internal)
 * See {@link fr.insalyon.creatis.vip.core.integrationtest.BaseWebSpringIT}
 *
 * Spring will automatically get the beans available on the class path, so class should be the base of all tests
 * on the root application context in all module.
 * Dedicated Test Classes could extend this to add helpers for other module, but should not change the Test/Spring config
 * see {@link fr.insalyon.creatis.vip.application.integrationtest.BaseApplicationSpringIT} (in vip-application)
 *
 * To configure beans, just add beans with the "test" profile, and a TestConfigurer bean to configure them before each
 * test (to configure and reset mocks especially).
 * This will harvest all TestConfigurer automatically.
 * See {@link fr.insalyon.creatis.vip.core.integrationtest.SpringTestConfig}
 *
 * The "test" profile overrides all the external dependencies
 * that would throw exception by mocked and configurable ones.
 * The "test-db" profile disable the default jndi datasource and uses a
 * h2 in-memory database instead
 */

@SpringJUnitWebConfig(name="root", classes=SpringCoreConfig.class)
// launch all spring environment for testing, also take test bean though automatic package scan
@ActiveProfiles({"test-db", "test"}) // to take random h2 database and not the test h2 jndi one
@TestPropertySource(properties = {
        "db.tableEngine=",             // to disable the default mysql/innodb engine on database init
        "db.jsonType=TEXT"})           // to workaround h2/mysql differences on JSON type
@Transactional // each test is in a transaction that is rollbacked at the end to always leave a "clean" state
public abstract class BaseSpringIT {
    
    @Autowired @Qualifier("db-datasource") protected DataSource dataSource; // this is a mockito spy wrapping the h2 memory datasource
    @Autowired protected ApplicationContext applicationContext;
    @Autowired protected ConfigurationBusiness configurationBusiness;
    @Autowired protected ApplicationContext appContext;
    @Autowired protected DataSource lazyDataSource;
    @Autowired protected Server server;
    @Autowired protected EmailBusiness emailBusiness;
    @Autowired protected GRIDAClient gridaClient;
    @Autowired protected GroupBusiness groupBusiness;
    @Autowired protected GroupDAO groupDAO;
    @Autowired protected List<TestConfigurer> testConfigurers;

    protected ObjectMapper mapper;

    protected final String emailUser1 = "test1@test.fr";
    protected final String emailUser2 = "test2@test.fr";
    protected final String emailUser3 = "test3@test.fr";
    protected final String emailUser4 = "test4@test.fr";
    protected final String nameGroup1 = "group1";    
    protected String adminEmail = "test-admin@test.com";

    protected User user1;
    protected User user2;
    protected User user3;
    protected User user4;
    protected User admin;
    protected Group group1;
    protected Group group2;
    protected User nonExistentUser = new User("test firstName suffix0",
            "test lastName suffix0", "unexisting_user@test.fr", "institution",
            "testPassword", CountryCode.fr,
            null);

    @BeforeEach
    protected void setUp() throws Exception {
        // by default spring mvc json path validation uses JsonPath that use a json-smart parser
        // we change that to Jackson, and we make jackson strict to refuse things like : {"foo":42}bar
        setUpStrictJacksonMapper();
        for (TestConfigurer testConfigurer : testConfigurers) {
            testConfigurer.setUpBeforeEachTest();
        }
    }

    protected void assertRowsNbInTable(String tableName, int expectedNb) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        int rowsNb = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        assertEquals(expectedNb, rowsNb);
    }

    protected User createUser(String email, UserLevel level) throws GRIDAClientException, BusinessException {
        User u = createUser(email, UUID.randomUUID().toString().substring(0, 4));

        configurationBusiness.updateUser(u.getEmail(), level, u.getCountryCode(), u.getMaxRunningSimulations(), false);
        return configurationBusiness.getUser(email);
    }

    protected User createUser(String testEmail) throws GRIDAClientException, BusinessException {
        return createUser(testEmail, "");
    }

    protected User createUserWithPassword(String testEmail, String password) throws GRIDAClientException, BusinessException {
        return createUser(testEmail, "", password);
    }

    protected User createUser(String testEmail, String nameSuffix) throws GRIDAClientException, BusinessException {
        return createUser(testEmail, nameSuffix, "testPassword");
    }

    protected User createUser(String testEmail, String nameSuffix, String password) throws GRIDAClientException, BusinessException {
        User newUser = new User("test firstName " + nameSuffix,
                "test lastName " + nameSuffix, testEmail, "test institution",
                password, CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        configurationBusiness.signup(newUser, "", (Group) null);
        return newUser;
    }

    protected User createUserInGroup(String userEmail, String groupName) throws BusinessException, GRIDAClientException {
        return createUserInGroup(userEmail, "", groupName);
    }

    protected User createUserInGroup(String userEmail, String nameSuffix, String groupName) throws BusinessException, GRIDAClientException {
        return createUserInGroups(userEmail, nameSuffix, groupName);
    }

    public void createGroup(String groupName) throws BusinessException {
        groupBusiness.add(new Group(groupName, true, GroupType.APPLICATION));
    }

    public void createGroup(String groupName, GroupType type) throws BusinessException {
        groupBusiness.add(new Group(groupName, true, type));
    }

    public void createGroup(String groupName, GroupType type, Boolean isPublic) throws BusinessException {
        groupBusiness.add(new Group(groupName, isPublic, type));
    }

    public void setAdminContext() throws BusinessException, GRIDAClientException {
        SessionAuthenticationProvider provider = new SessionAuthenticationProvider();
        User adminUser = configurationBusiness.getUserWithGroups(adminEmail);

        SecurityContextHolder.getContext().setAuthentication(provider.createAuthenticationFromUser(adminUser));
    }

    protected RequestPostProcessor getUserSecurityMock(User user) {
        return SecurityMockMvcRequestPostProcessors.user(new SpringPrincipalUser(user));
    }

    protected User createUserInGroups(String userEmail, String nameSuffix, String... groupNames) throws BusinessException, GRIDAClientException {
        User newUser = new User("test firstName " + nameSuffix,
                "test lastName " + nameSuffix, userEmail, "test institution",
                "testPassword", CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        List<Group> groups = new ArrayList<>();
        for (String groupName : groupNames) {
            groups.add(groupBusiness.get(groupName));
        }
        configurationBusiness.signup(newUser, "", false, true, groups);
        return configurationBusiness.getUserWithGroups(userEmail);
    }

    protected Date getNextSecondDate() {
        return new Date(new Date().getTime() + (1000));
    }

    protected void setUpStrictJacksonMapper() throws Exception {
        // by default JsonPath uses json-smart, change to jackson with strict mode
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        Configuration.setDefaults(new Configuration.Defaults() {
            @Override
            public JsonProvider jsonProvider() {
                return new JacksonJsonProvider(mapper);
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }

            @Override
            public MappingProvider mappingProvider() {
                return new JacksonMappingProvider();
            }
        });
    }


}
