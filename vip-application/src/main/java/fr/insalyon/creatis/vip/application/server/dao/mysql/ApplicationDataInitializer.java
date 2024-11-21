package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.TableInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component
@Transactional
public class ApplicationDataInitializer extends JdbcDaoSupport {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TableInitializer tableInitializer;

    @Autowired
    public ApplicationDataInitializer(
            DataSource dataSource, TableInitializer tableInitializer) {
        setDataSource(dataSource);
        this.tableInitializer = tableInitializer;
    }


    @EventListener(ContextRefreshedEvent.class)
    @Order(20) // Applications tables references vip-core tables and must be created after
    public void onStartup() {
        logger.info("Configuring VIP Application database.");

        createEngineTable();
        createResourcesTables();
        createClassesTables();
        createApplicationsTables();
        createOthersTables();
    }

    private void createEngineTable() {
        tableInitializer.createTable("VIPEngines",
                "name VARCHAR(255), "
                        + "endpoint VARCHAR(255), "
                        + "status VARCHAR(255) DEFAULT NULL, "
                        + "PRIMARY KEY (name)");
    }

    private void createResourcesTables() {
        tableInitializer.createTable("VIPResources", 
                    "name VARCHAR(255) NOT NULL, "
                +   "visible BOOLEAN DEFAULT FALSE, "
                +   "status BOOLEAN DEFAULT FALSE, "
                +   "type VARCHAR(255), "
                +   "configuration VARCHAR(255), "
                +   "PRIMARY KEY (name)"
        );

        tableInitializer.createTable("VIPGroupResources",
                    "name VARCHAR(255), "
                +   "resourcename VARCHAR(255), "
                +   "PRIMARY KEY (name, resourcename), "
                +   "FOREIGN KEY (name) REFERENCES VIPGroups(name) "
                +   "ON DELETE CASCADE ON UPDATE RESTRICT, "
                +   "FOREIGN KEY (resourcename) REFERENCES VIPResources(name) "
                +   "ON DELETE CASCADE ON UPDATE RESTRICT");

        tableInitializer.createTable("VIPResourceEngines",
                    "resourcename VARCHAR(255), "
                +   "enginename VARCHAR(255), "
                +   "PRIMARY KEY (resourcename, enginename), "
                +   "FOREIGN KEY (resourcename) REFERENCES VIPResources(name) "
                +   "ON DELETE CASCADE ON UPDATE RESTRICT, "
                +   "FOREIGN KEY (enginename) REFERENCES VIPEngines(name) "
                +   "ON DELETE CASCADE ON UPDATE RESTRICT"
        );

        tableInitializer.createTable("VIPResourceAppVersions",
                    "resourcename VARCHAR(255), "
                +   "application VARCHAR(255), "
                +   "version VARCHAR(255), "
                +   "PRIMARY KEY (resourcename, application, version), "
                +   "FOREIGN KEY (resourcename) REFERENCES VIPResources(name) "
                +   "ON DELETE CASCADE ON UPDATE RESTRICT, "
                +   "FOREIGN KEY (application, version) REFERENCES VIPAppVersions(application, version) "
                +   "ON DELETE CASCADE ON UPDATE RESTRICT"
        );
    }

    private void createClassesTables() {
        tableInitializer.createTable("VIPClasses",
                "name VARCHAR(255), "
                        + "PRIMARY KEY (name)");

        tableInitializer.createTable("VIPClassesEngines",
                "class VARCHAR(255), "
                        + "engine VARCHAR(255), "
                        + "PRIMARY KEY (class, engine), "
                        + "FOREIGN KEY (class) REFERENCES VIPClasses(name) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE, "
                        + "FOREIGN KEY (engine) REFERENCES VIPEngines(name) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE");

        tableInitializer.createTable("VIPGroupsClasses",
                "classname VARCHAR(255), "
                        + "name VARCHAR(255), "
                        + "PRIMARY KEY (classname, name), "
                        + "FOREIGN KEY (classname) REFERENCES VIPClasses(name) "
                        + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                        + "FOREIGN KEY (name) REFERENCES VIPGroups(name) "
                        + "ON DELETE CASCADE ON UPDATE RESTRICT");
    }

    private void createApplicationsTables() {
        tableInitializer.createTable("VIPApplications",
                "name VARCHAR(255), "
                        + "citation TEXT, "
                        + "owner VARCHAR(255), "
                        + "PRIMARY KEY (name), "
                        + "FOREIGN KEY (owner) REFERENCES VIPUsers(email) "
                        + "ON DELETE SET NULL ON UPDATE CASCADE");

        tableInitializer.createTable("VIPAppVersions",
                "application VARCHAR(255), "
                        + "version VARCHAR(255), "
                        + "lfn VARCHAR(255), "
                        + "json_lfn VARCHAR(255), "
                        + "doi VARCHAR(255), "
                        + "visible BOOLEAN, "
                        + "useBoutiquesForm BOOLEAN, "
                        + "PRIMARY KEY (application, version), "
                        + "FOREIGN KEY (application) REFERENCES VIPApplications(name) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE");

        tableInitializer.createTable("VIPApplicationClasses",
                "class VARCHAR(255), "
                        + "application VARCHAR(255), "
                        + "PRIMARY KEY (class, application), "
                        + "FOREIGN KEY (class) REFERENCES VIPClasses(name) "
                        + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                        + "FOREIGN KEY (application) REFERENCES VIPApplications(name) "
                        + "ON DELETE CASCADE ON UPDATE RESTRICT");

        tableInitializer.createTable("VIPAppExamples",
                "application VARCHAR(255), "
                        + "name VARCHAR(255), "
                        + "inputs VARCHAR(32000), "
                        + "PRIMARY KEY (application, name)");
    }

    private void createOthersTables() {
        tableInitializer.createTable("VIPAppInputs",
                "email VARCHAR(255), "
                        + "application VARCHAR(255), "
                        + "name VARCHAR(255), "
                        + "inputs VARCHAR(32000), "
                        + "PRIMARY KEY (email, application, name), "
                        + "FOREIGN KEY (email) REFERENCES VIPUsers(email) "
                        + "ON DELETE CASCADE ON UPDATE CASCADE");

        tableInitializer.createTable("VIPPublicExecutions",
                "execution_ID VARCHAR(255), "
                        + "simulation_name VARCHAR(255), "
                        + "application_name VARCHAR(255), "
                        + "application_version VARCHAR(255), "
                        + "status  VARCHAR(50), "
                        + "author VARCHAR(255), "
                        + "output_names VARCHAR(255), "
                        + "comments TEXT, "
                        + "PRIMARY KEY(execution_ID)");
    }
} 
