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
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
    Use simple jndi to configure an h2 in-memory database and verify :
    - spring and database config
    - transaction and connection management
    - automatic database creation on first launch
    - database not overridden on next launches
 */
@SpringJUnitConfig(SpringCoreConfig.class) // launch all spring environment in a test
@TestPropertySource(properties = "db.tableEngine=") // to disable the default mysql/innodb engine on database init
@TestMethodOrder(OrderAnnotation.class)
public class SpringConnectionIT {

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
}
