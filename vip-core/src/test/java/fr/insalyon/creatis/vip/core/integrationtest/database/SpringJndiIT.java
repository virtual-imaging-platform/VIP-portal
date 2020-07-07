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
import fr.insalyon.creatis.vip.core.server.dao.mysql.CoreDataInitializer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

/*
    Use simple jndi to configure an h2 in-memory database and verify :
    - spring and database config
    - transaction and connection management
    - automatic database creation on first launch
    - database not overridden on next launches
 */
@SpringJUnitConfig(SpringCoreConfig.class) // launch all spring environment for testing, also take test bean though automatic package scan
@TestPropertySource(properties = "db.tableEngine=") // to disable the default mysql/innodb engine on database init
@TestMethodOrder(OrderAnnotation.class)
public class SpringJndiIT {

    @Autowired
    private ConfigurationBusiness configurationBusiness;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource lazyDataSource;

    @Autowired
    private EmailBusiness emailBusiness;

    @Autowired
    private GRIDAClient gridaClient;

    /*
        First launch
     */
    @Test
    @Order(1)
    public void testJNDIConfig() throws BusinessException {
        // verify the vip-support group created on init is present
        assertNotNull(configurationBusiness);
        List<Group> groups = configurationBusiness.getGroups();
        assertEquals(1, groups.size());

        final Connection[] firstTransactionConnections = new Connection[2];
        // check that a connection is shared in a transaction
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                firstTransactionConnections[0] = DataSourceUtils.getConnection(dataSource);
                firstTransactionConnections[1] = DataSourceUtils.getConnection(dataSource);
            }
        });
        assertEquals(firstTransactionConnections[0], firstTransactionConnections[1]);

        // check that a connection is not shared outside a transaction
        Connection connection1 = DataSourceUtils.getConnection(dataSource);
        Connection connection2= DataSourceUtils.getConnection(dataSource);
        assertNotEquals(connection1, connection2);
    }

    /*
        Add an account
    */
    @Test
    @Order(2)
    public void addNewAccount() throws BusinessException {
        List<Account> accounts = configurationBusiness.getAccounts();
        assertEquals(0, accounts.size());
        configurationBusiness.addAccount("test Account", Collections.emptyList());
        accounts = configurationBusiness.getAccounts();
        assertEquals(1, accounts.size());
    }


    /*
        Verify the account is still there
    */
    @Test
    @Order(3)
    public void isAccountStillThere() throws BusinessException {
        List<Account> accounts = configurationBusiness.getAccounts();
        assertEquals(1, accounts.size());
    }

    /*
        Restart spring, account should still be there
    */
    @Test
    @Order(4)
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD) // to restart spring
    public void isAccountStillThereAfterRestart() throws BusinessException {
        List<Account> accounts = configurationBusiness.getAccounts();
        assertEquals(1, accounts.size());
    }

    @Test
    @Order(5)
    public void shouldRollbackWithRuntimeException() throws BusinessException, GRIDAClientException {
        testRollbackInTransaction(new RuntimeException(""), true);
    }

    @Test
    @Order(6)
    public void shouldNotRollbackWithCheckedException() throws BusinessException, GRIDAClientException {
        testRollbackInTransaction(new BusinessException(""), false);
    }

    private void testRollbackInTransaction(
            Exception exception, boolean shouldRollback) throws BusinessException, GRIDAClientException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        Supplier<Integer> countUser =
                () -> JdbcTestUtils.countRowsInTable(jdbcTemplate, "VIPUsers");

        String testEmail = "test@email.fr";
        assertEquals(1, countUser.get());
        createUser(testEmail);
        // verify initial user + new one are there
        assertEquals(2, countUser.get());
        // when sending an email (after the deletion, throw an exception to cause a rollback
        Mockito.doAnswer(invocation -> {
            // but before, verify the user has well been deleted
            assertEquals(1, countUser.get());
            throw exception;
        }).when(emailBusiness).sendEmail(any(), any(), any(), anyBoolean(), any());

        Exception exceptionCatched = null;
        try {
            configurationBusiness.removeUser(testEmail, true);
        } catch (Exception ex) {
            exceptionCatched = ex;
        }
        Mockito.reset(emailBusiness);
        assertEquals(exception, exceptionCatched);
        assertEquals(shouldRollback ? 2:1, countUser.get());
        if (shouldRollback) {
            // clean if necessary
            configurationBusiness.removeUser(testEmail, false);
        }
        assertEquals(1, countUser.get());
    }

    private void createUser(String testEmail) throws GRIDAClientException, BusinessException {
        User newUser = new User("firstName", "LastName",
                testEmail, "Test institution",
                "testPassword", "testPhone", CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        configurationBusiness.signup(newUser, "", (String) null);
    }

    @Test
    @Order(7)
    public void shouldHandleConnectionCreationIssue() throws SQLException {
        // as connection are lazy, connections are created when they are actually called
        // and not when the connection is obtained through spring and so errors cause SqlException
        // and not spring DataAccessException
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        try { jdbcTemplate.execute("SHUTDOWN"); } catch (Exception e) {e.printStackTrace();}
        assertThrows(BusinessException.class, () -> configurationBusiness.addTermsUse());
    }

    @Test
    @Order(8)
    public void connectionShouldBeLazyInTransaction() throws SQLException, MalformedURLException {
        // getConnection throw an exception but should not be called as 'getLoginUrlCas' do not need db access
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        try { jdbcTemplate.execute("SHUTDOWN"); } catch (Exception e) {e.printStackTrace();}
        String res = configurationBusiness.getLoginUrlCas(new URL("file:/plop"));
        assertEquals(ServerMockConfig.TEST_CAS_URL + "/login?service=file:/plop", res);
    }
}
