package fr.insalyon.creatis.vip.core.integrationtest.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;

/**
 * Integration tests that verify the spring database/transactions configuration
 * with the in-memory database.
 *
 * These tests are ordered as this is needed for the last ones.
 */
@TestMethodOrder(OrderAnnotation.class)
public class SpringDatabaseIT extends BaseSpringIT{
    
    /*
        verify database init and that only one connection is shared in a test
     */

    @Test
    @Order(1)
    public void testTestConfig() throws VipException {
        // verify the vip-support group created on init is present
        assertNotNull(configurationBusiness);
        List<Group> groups = groupBusiness.get();
        assertEquals(0, groups.size());

        // check that we are in the test transaction and that the connection is shared
        Connection connection1 = DataSourceUtils.getConnection(dataSource);
        Connection connection2 = DataSourceUtils.getConnection(dataSource);
        assertEquals(connection1, connection2);
    }

    /*
        verify simple database operation
    @Test
    @Order(2)
    public void addNewAccount() throws VipException {
        List<Account> accounts = configurationBusiness.getAccounts();
        assertEquals(0, accounts.size());
        configurationBusiness.addAccount("test Account", Collections.emptyList());
        accounts = configurationBusiness.getAccounts();
        assertEquals(1, accounts.size());
    }
    */


    /*
        Verify the account is not there anymore as last test method is Transactional and rollbacked
    @Test
    @Order(3)
    public void isAccountStillThere() throws VipException {
        List<Account> accounts = configurationBusiness.getAccounts();
        assertEquals(0, accounts.size());
    }
    */

    /*
        from now on, test transactions are disabled to verify rollbacks and
        transaction behaviors of the vip business layer
     */

    @Test
    @Order(4)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldRollbackWithRuntimeException() throws VipException, GRIDAClientException {
        // a runtime exception must rollback the current transaction
        testRollbackInTransaction(new RuntimeException(""), true);
    }

    @Test
    @Order(5)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldNotRollbackWithCheckedException() throws VipException, GRIDAClientException {
        // a checked exception must NOT rollback the current transaction
        testRollbackInTransaction(new VipException(""), false);
    }

    private void testRollbackInTransaction(
            Exception exception, boolean shouldRollback) throws VipException, GRIDAClientException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        Supplier<Integer> countUser =
                () -> JdbcTestUtils.countRowsInTable(jdbcTemplate, "VIPUsers");

        String testEmail = "test@email.fr";
        assertEquals(1, countUser.get());
        createUser(testEmail);
        // verify initial user + new one are there
        assertEquals(2, countUser.get());
        // Now we will remove an user, and throw an exception when an email is sent at the end
        // we will check if the user is still present or not after to see if the transaction have been rollbacked
        Mockito.doAnswer(invocation -> {
            // but before, verify the user has well been deleted
            assertEquals(1, countUser.get());
            throw exception;
        }).when(emailBusiness).sendEmailToAdmins(any(), any(), anyBoolean(), any());

        Exception exceptionCatched = null;
        try {
            configurationBusiness.removeUser(testEmail, true);
        } catch (Exception ex) {
            exceptionCatched = ex;
        }
        Mockito.reset(emailBusiness);
        assertEquals(exception, exceptionCatched);
        assertEquals(shouldRollback ? 2 : 1, countUser.get());
        if (shouldRollback) {
            // clean if necessary
            configurationBusiness.removeUser(testEmail, false);
        }
        assertEquals(1, countUser.get());
    }

    @Test
    @Order(6)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldHandleConnectionCreationIssue() throws SQLException {
        // as connection are lazy, connections are created when they are actually called
        // and not when the connection is obtained through spring and so errors cause SqlException
        // and not spring DataAccessException, so vip is able to catch them and transform them in VipException
        Mockito.doThrow(SQLException.class).when(dataSource).getConnection();
        assertThrows(VipException.class, () -> configurationBusiness.addTermsUse());
        Mockito.reset(dataSource);
    }

    @Test
    @Order(7)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void connectionShouldBeLazyInTransaction() throws SQLException, MalformedURLException, URISyntaxException {
        // getConnection throw an exception but should not be called as 'getLoginUrlCas' do not need db access
        Mockito.doThrow(SQLException.class).when(dataSource).getConnection();
        String res = configurationBusiness.getLoginUrlCas(new URI("file:/plop").toURL());
        assertEquals(ServerMockConfig.TEST_CAS_URL + "/login?service=file:/plop", res);
        Mockito.reset(dataSource);
    }
}
