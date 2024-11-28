package fr.insalyon.creatis.vip.application.server.dao.mysql;

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
        createApplicationsTables();
        createResourcesTables();
        createTagsTables();
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

        tableInitializer.createTable("VIPGroupsResources",
                    "groupname VARCHAR(50), "
                +   "resourcename VARCHAR(255), "
                +   "PRIMARY KEY (groupname, resourcename), "
                +   "FOREIGN KEY (groupname) REFERENCES VIPGroups(name) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE, "
                +   "FOREIGN KEY (resourcename) REFERENCES VIPResources(name) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE");

        tableInitializer.createTable("VIPResourcesEngines",
                    "resourcename VARCHAR(255), "
                +   "enginename VARCHAR(255), "
                +   "PRIMARY KEY (resourcename, enginename), "
                +   "FOREIGN KEY (resourcename) REFERENCES VIPResources(name) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE, "
                +   "FOREIGN KEY (enginename) REFERENCES VIPEngines(name) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE"
        );

        tableInitializer.createTable("VIPResourcesAppVersions",
                    "resourcename VARCHAR(255), "
                +   "application VARCHAR(255), "
                +   "version VARCHAR(255), "
                +   "PRIMARY KEY (resourcename, application, version), "
                +   "FOREIGN KEY (resourcename) REFERENCES VIPResources(name) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE, "
                +   "FOREIGN KEY (application, version) REFERENCES VIPAppVersions(application, version) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE"
        );
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

        tableInitializer.createTable("VIPAppExamples",
                "application VARCHAR(255), "
                        + "name VARCHAR(255), "
                        + "inputs VARCHAR(32000), "
                        + "PRIMARY KEY (application, name)");
    }

    private void createTagsTables() {
        tableInitializer.createTable("VIPTags",
                    "name VARCHAR(255), "
                +   "PRIMARY KEY (name)");

        tableInitializer.createTable("VIPTagsAppVersions",
                    "application VARCHAR(255), "
                +   "version VARCHAR(255), "
                +   "tagname VARCHAR(255), "
                +   "PRIMARY KEY (application, version, tagname), "
                +   "FOREIGN KEY (application, version) REFERENCES VIPAppVersions(application, version) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE, "
                +   "FOREIGN KEY (tagname) REFERENCES VIPTags(name) "
                +   "ON DELETE CASCADE ON UPDATE CASCADE");
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
