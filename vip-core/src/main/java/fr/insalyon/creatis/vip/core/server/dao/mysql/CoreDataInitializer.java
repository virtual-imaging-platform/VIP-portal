package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.models.TermsOfUse;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.PasswordBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.TermsUseDAO;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CoreDataInitializer extends JdbcDaoSupport {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TableInitializer tableInitializer;
    private Server server;
    private UserDAO userDAO;
    private TermsUseDAO termsUseDAO;
    private PasswordBusiness passwordBusiness;

    @Autowired
    public CoreDataInitializer(DataSource dataSource, TableInitializer tableInitializer, Server server,
                                UserDAO userDAO, TermsUseDAO termsUseDAO, PasswordBusiness passwordBusiness) {
        this.setDataSource(dataSource);
        this.tableInitializer = tableInitializer;
        this.userDAO = userDAO;
        this.server = server;
        this.termsUseDAO = termsUseDAO;
        this.passwordBusiness = passwordBusiness;
    }

    @EventListener({ContextRefreshedEvent.class})
    @Order(10)
    public void onStartup() {
        this.logger.info("Configuring VIP core database.");
        this.initializeUserTables();
        this.initializeGroupTables();
        this.initializeTermsOfUseTable();
    }

    private void initializeUserTables() {

        if (this.tableInitializer.createTable("VIPUsers", "id VARCHAR(8), email VARCHAR(255), next_email VARCHAR(255), pass VARCHAR(120), first_name VARCHAR(255), last_name VARCHAR(255), institution VARCHAR(255), code VARCHAR(40), confirmed BOOLEAN, folder VARCHAR(100), session VARCHAR(255), registration TIMESTAMP, last_login TIMESTAMP, level VARCHAR(50), country_code VARCHAR(2), max_simulations INT, termsUse TIMESTAMP, lastUpdatePublications TIMESTAMP,failed_authentications INT,account_locked BOOLEAN,apikey VARCHAR(255),PRIMARY KEY(email),UNIQUE (id),UNIQUE (first_name,last_name),UNIQUE (apikey)")) {
            String var10000 = this.server.getAdminFirstName().toLowerCase();
            String folder = var10000 + "_" + this.server.getAdminLastName().toLowerCase();

            try {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                this.userDAO.add(new User(CoreUtil.createUUID(), this.server.getAdminFirstName(), this.server.getAdminLastName(), this.server.getAdminEmail(), (String) null, this.server.getAdminInstitution(), true, UUID.randomUUID().toString(), folder, "", now, now, UserLevel.Administrator, CountryCode.fr, 100, (Timestamp) null, (Timestamp) null, 0, false, (String) null));
                this.userDAO.definePassword(this.server.getAdminEmail(), this.passwordBusiness.hash(this.server.getAdminPassword()));
            } catch (DAOException ex) {
                this.logger.error("Error creating VIPUsers table", ex);
            }
        }
    }

    private void initializeGroupTables() {
        this.tableInitializer.createTable("VIPGroups", "name VARCHAR(50), public BOOLEAN, type VARCHAR(30), auto BOOLEAN, PRIMARY KEY(name)");
        this.tableInitializer.createTable("VIPUsersGroups", "email VARCHAR(255), groupname VARCHAR(50), role VARCHAR(30), PRIMARY KEY (email, groupname), FOREIGN KEY (email) REFERENCES VIPUsers(email) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (groupname) REFERENCES VIPGroups(name) ON DELETE CASCADE ON UPDATE CASCADE");
    }

    private void initializeTermsOfUseTable() {
        if (this.tableInitializer.createTable("VIPTermsOfUse", "id INT NOT NULL AUTO_INCREMENT, date TIMESTAMP NULL, PRIMARY KEY (id)")) {
            try {
                Date today = new Date();
                this.termsUseDAO.add(new TermsOfUse(new Timestamp(today.getTime())));
            } catch (DAOException ex) {
                this.logger.error("Error creating VIPGroups table", ex);
            }
        }
    }
}