package fr.insalyon.creatis.vip.core;

import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitConfig({TestDataSourceSpringConfig.class, SpringCoreConfig.class}) // launch all spring environment in a test
@TestPropertySource(properties = "db.tableEngine=") // to disable the default mysql/innodb engine on database init
@Transactional // to make spring rollback after each test
public class SpringDatabaseIT {

    @Autowired
    private ConfigurationBusiness configurationBusiness;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    /*
        First launch
     */
    @Test
    public void testTestConfig() throws BusinessException {
        // verify the vip-support group created on init is present
        assertNotNull(configurationBusiness);
        List<Group> groups = configurationBusiness.getGroups();
        assertEquals(1, groups.size());

        // check that we are in the test transaction and that the connection is shared
        Connection connection1 = DataSourceUtils.getConnection(dataSource);
        Connection connection2= DataSourceUtils.getConnection(dataSource);
        assertEquals(connection1, connection2);
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
        Verify the account is not there anymore as each test method is rollbacked
    */
    @Test
    @Order(3)
    public void isAccountStillThere() throws BusinessException {
        List<Account> accounts = configurationBusiness.getAccounts();
        assertEquals(0, accounts.size());
    }

    // TODO add a rollback test (db request ok, then exception, verify rollback)
}
