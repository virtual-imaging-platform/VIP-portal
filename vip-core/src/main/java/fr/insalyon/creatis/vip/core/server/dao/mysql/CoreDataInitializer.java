package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class CoreDataInitializer extends JdbcDaoSupport {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GroupDAO groupDAO;

    @Value("${db.tableEngine:InnoDB}")
    private String tableEngine = "InnoDB";

    @Autowired
    public CoreDataInitializer(GroupDAO groupDAO, DataSource dataSource) {
        setDataSource(dataSource);
        this.groupDAO = groupDAO;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onStartup() {
        initializeGroupTables();
        initializeAccountTables();
    }

    private void initializeGroupTables() {
        if (createTable("VIPGroups",
                "groupname VARCHAR(50), "
                        + "public BOOLEAN, "
                        + "gridfile BOOLEAN DEFAULT 0, "
                        + "gridjobs BOOLEAN DEFAULT 0, "
                        + "PRIMARY KEY(groupname)")) {

            try {
                groupDAO.add(new Group(CoreConstants.GROUP_SUPPORT, false, true, true));
            } catch (DAOException ex) {
                logger.error("Error creating VIPGroups table", ex);
            }
        }

        /*
        if (createTable("VIPUsersGroups",
                "email VARCHAR(255), "
                        + "groupname VARCHAR(100), "
                        + "role VARCHAR(30), "
                        + "PRIMARY KEY (email, groupname), "
                        + "FOREIGN KEY (email) REFERENCES VIPUsers(email) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE, "
                        + "FOREIGN KEY (groupname) REFERENCES VIPGroups(groupname) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE")) {
            try {
                CoreDAOFactory.getDAOFactory().getUsersGroupsDAO(connection).
                        add(Server.getInstance().getAdminEmail(),
                                CoreConstants.GROUP_SUPPORT,
                                GROUP_ROLE.Admin);
            } catch (DAOException ex) {
                logger.error("Error adding admin user to admin group", ex);
            }
        }   */
    }

    private void initializeAccountTables() {
        createTable("VIPAccounts",
                "name VARCHAR(255), "
                        + "PRIMARY KEY (name)");

        createTable("VIPAccountsGroups",
                "name VARCHAR(255), "
                        + "groupname VARCHAR(255), "
                        + "PRIMARY KEY (name, groupname), "
                        + "FOREIGN KEY (name) REFERENCES VIPAccounts(name) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE, "
                        + "FOREIGN KEY (groupname) REFERENCES VIPGroups(groupname) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE");
    }

    private boolean createTable(String name, String columnsDefinition) {

        try {
            String suffix = tableEngine.isEmpty() ? "" : " ENGINE=" + tableEngine;
            Statement stat = getConnection().createStatement();
            stat.executeUpdate("CREATE TABLE " + name + " ("
                    + columnsDefinition + ")" + suffix);

            logger.info("Table " + name + " successfully created.");
            return true;

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("already exists")) {
                logger.error("Error creating db table {}", name, ex);
            }
            return false;
        }
    }
}
