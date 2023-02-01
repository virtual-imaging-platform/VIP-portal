package fr.insalyon.creatis.vip.core.integrationtest.database;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

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
@SpringJUnitConfig(SpringCoreConfig.class) // launch all spring environment for testing, also take test bean though automatic package scan
@ActiveProfiles({"test-db", "test"}) // to take random h2 database and not the test h2 jndi one
@TestPropertySource(properties = "db.tableEngine=") // to disable the default mysql/innodb engine on database init
@Transactional // each test is in a transaction that is rollbacked at the end to always leave a "clean" state
public abstract class BaseSpringIT {

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

    @BeforeEach
    protected void setUp() {
        ServerMockConfig.reset(server);
    }

    protected void assertRowsNbInTable(String tableName, int expectedNb) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        int rowsNb = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        assertEquals(expectedNb, rowsNb);
    }

    protected void createUser(String testEmail) throws GRIDAClientException, BusinessException {
        createUser(testEmail, "");
    }

    protected void createUser(String testEmail, String nameSuffix) throws GRIDAClientException, BusinessException {
        User newUser = new User("firstName"+nameSuffix,
                "LastName"+nameSuffix, testEmail, "Test institution",
                "testPassword", CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        configurationBusiness.signup(newUser, "", (Group) null);
    }
}
