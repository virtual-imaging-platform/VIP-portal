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
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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

    This allows to test the database with jndi to be as close as possible from
    production configuration. Unfortunately, jndi configuration is complicated
    in automatic testing and the jndi.properties file needs an absolute path, which is not
    possible for a versioned and shared projects. This is disabled but project
    members are invited to temporally activate it and edit the jndi.properties
    in case of database issue to investigate.

    The SpringDatabaseIT is very close as this one and works on an h2 in-memory
    database without jndi, and should be enough to detect most database issues
    in a automatic way
 */
@SpringJUnitConfig(SpringCoreConfig.class) // launch all spring environment for testing, also take test bean though automatic package scan
@TestPropertySource(properties = {
        "db.tableEngine=",     // to disable the default mysql/innodb engine on database init
        "db.jsonType=TEXT" })  // to workaround h2/mysql differences on JSON type
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles({"jndi-db", "test"}) // to use default jndi datasource but avoid default server config
public class SpringJndiIT {

    @Autowired private ConfigurationBusiness configurationBusiness;
    @Autowired private DataSource dataSource;
    @Autowired private PlatformTransactionManager transactionManager;
    @Autowired private DataSource lazyDataSource;
    @Autowired private EmailBusiness emailBusiness;
    @Autowired private GRIDAClient gridaClient;
    @Autowired private GroupBusiness groupBusiness;

    /*
        First launch
     */
    @Test
    @Order(1)
    public void testJNDIConfig() throws BusinessException {
        // verify the vip-support group created on init is present
        assertNotNull(configurationBusiness);
        List<Group> groups = groupBusiness.get();
        assertEquals(0, groups.size());

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
        Connection connection2 = DataSourceUtils.getConnection(dataSource);
        assertNotEquals(connection1, connection2);
    }

    /*
        Add a group (there is already one after init)
    */
    @Test
    @Order(2)
    public void addNewGroup() throws BusinessException {
        List<Group> groups = groupBusiness.get();
        assertEquals(0, groups.size());
        groupBusiness.add(new Group("test group", true, GroupType.RESOURCE));
        groups = groupBusiness.get();
        assertEquals(1, groups.size());
    }


    /*
        Verify the group is still there
    */
    @Test
    @Order(3)
    public void isGroupStillThere() throws BusinessException {
        List<Group> groups = groupBusiness.get();
        assertEquals(1, groups.size());
    }

    /*
        Restart spring, group should still be there
    */
    @Test
    @Order(4)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD) // to restart spring
    public void isGroupStillThereAfterRestart() throws BusinessException {
        List<Group> groups = groupBusiness.get();
        assertEquals(1, groups.size());
    }

    @Test
    @Order(5)
    public void shouldRollbackWithRuntimeException() throws BusinessException, GRIDAClientException, DAOException {
        testRollbackInTransaction(new RuntimeException(""), true);
    }

    @Test
    @Order(6)
    public void shouldNotRollbackWithCheckedException() throws BusinessException, GRIDAClientException, DAOException {
        testRollbackInTransaction(new BusinessException(""), false);
    }

    private void testRollbackInTransaction(
            Exception exception, boolean shouldRollback) throws BusinessException, GRIDAClientException, DAOException {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        Supplier<Integer> countUser = () -> JdbcTestUtils.countRowsInTable(jdbcTemplate, "VIPUsers");        
        Mockito.doReturn(new String[]{"test@admin.test"}).when(emailBusiness).getAdministratorsEmails();

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
                "testPassword", CountryCode.fr,
                null);
        Mockito.when(gridaClient.exist(anyString())).thenReturn(true, false);
        configurationBusiness.signup(newUser, "", (Group) null);
    }

    @Test
    @Order(7)
    public void shouldHandleConnectionCreationIssue() throws SQLException {
        // as connection are lazy, connections are created when they are actually called
        // and not when the connection is obtained through spring and so errors cause SqlException
        // and not spring DataAccessException
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        // close the datasource to make the next request fail
        try { jdbcTemplate.execute("SHUTDOWN"); } catch (Exception e) {e.printStackTrace();}
        assertThrows(BusinessException.class, () -> configurationBusiness.addTermsUse());
    }

    @Test
    @Order(8)
    public void connectionShouldBeLazyInTransaction() throws SQLException, MalformedURLException, URISyntaxException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(lazyDataSource);
        // close the datasource to make the next request fail
        try { jdbcTemplate.execute("SHUTDOWN"); } catch (Exception e) {e.printStackTrace();}
        // getConnection throw an exception but should not be called as 'getLoginUrlCas' do not need db access
        String res = configurationBusiness.getLoginUrlCas(new URI("file:/plop").toURL());
        assertEquals(ServerMockConfig.TEST_CAS_URL + "/login?service=file:/plop", res);
    }
}
