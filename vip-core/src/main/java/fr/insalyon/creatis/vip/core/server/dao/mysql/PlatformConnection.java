/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class PlatformConnection {

    private final static Logger logger = Logger.getLogger(PlatformConnection.class);
    private static PlatformConnection instance;
    private boolean firstExecution;
    private DataSource dataSource;

    public synchronized static PlatformConnection getInstance() {
        if (instance == null) {
            instance = new PlatformConnection();
        }
        return instance;
    }

    private PlatformConnection() {
        firstExecution = true;
        initDatasource();
        createTables();
    }

    private void initDatasource() {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/vip");
        } catch (NamingException ex) {
            logger.error("Error connecting database", ex);
        }
    }

    private void createTables() {
        if (firstExecution) {
            try (Connection connection = getConnection()) {
                logger.info("Configuring VIP database.");

                if (createTable(connection,
                                "VIPUsers",
                                "email VARCHAR(255), "
                                + "next_email VARCHAR(255), "
                                + "pass VARCHAR(40), "
                                + "first_name VARCHAR(255), "
                                + "last_name VARCHAR(255), "
                                + "institution VARCHAR(255), "
                                + "phone VARCHAR(255), "
                                + "code VARCHAR(40), "
                                + "confirmed BOOLEAN, "
                                + "folder VARCHAR(100), "
                                + "session VARCHAR(255), "
                                + "registration TIMESTAMP, "
                                + "last_login TIMESTAMP, "
                                + "level VARCHAR(50), "
                                + "country_code VARCHAR(2), "
                                + "max_simulations int(11), "
                                + "termsUse TIMESTAMP, "
                                + "lastUpdatePublications TIMESTAMP,"
                                + "failed_authentications int(11),"
                                + "account_locked BOOLEAN,"
                                + "apikey VARCHAR(255),"
                                + "PRIMARY KEY(email),"
                                + "UNIQUE KEY(first_name,last_name),"
                                + "UNIQUE KEY(apikey)")) {

                    Server server = Server.getInstance();
                    String folder = server.getAdminFirstName().toLowerCase() + "_"
                        + server.getAdminLastName().toLowerCase();

                    try {
                        CoreDAOFactory.getDAOFactory()
                            .getUserDAO(connection).add(
                            new User(server.getAdminFirstName(),
                                     server.getAdminLastName(),
                                     server.getAdminEmail(),
                                     null,
                                     server.getAdminInstitution(),
                                     server.getAdminPassword(),
                                     server.getAdminPhone(), true,
                                     UUID.randomUUID().toString(), folder, "",
                                     new Date(), new Date(), UserLevel.Administrator,
                                     CountryCode.fr, 100, null,null,0,false));

                    } catch (DAOException ex) {
                        logger.error("Error creating VIPUserstable", ex);
                    }
                }

                if (createTable(connection,
                                "VIPGroups",
                                "groupname VARCHAR(50), "
                                + "public BOOLEAN, "
                                + "gridfile BOOLEAN DEFAULT 0, "
                                + "gridjobs BOOLEAN DEFAULT 0, "
                                + "PRIMARY KEY(groupname)")) {

                    try {
                        CoreDAOFactory.getDAOFactory().getGroupDAO(connection).add(
                            new Group(CoreConstants.GROUP_SUPPORT, false, true, true));
                    } catch (DAOException ex) {
                        logger.error("Error creating VIPGroups table", ex);
                    }
                }

                if (createTable(connection,
                            "VIPUsersGroups",
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
                }

                createTable(connection,
                            "VIPAccounts",
                            "name VARCHAR(255), "
                            + "PRIMARY KEY (name)");

                createTable(connection,
                            "VIPAccountsGroups",
                            "name VARCHAR(255), "
                            + "groupname VARCHAR(255), "
                            + "PRIMARY KEY (name, groupname), "
                            + "FOREIGN KEY (name) REFERENCES VIPAccounts(name) "
                            + "ON DELETE CASCADE ON UPDATE CASCADE, "
                            + "FOREIGN KEY (groupname) REFERENCES VIPGroups(groupname) "
                            + "ON DELETE CASCADE ON UPDATE CASCADE");

                createTable(connection,
                            "VIPPublication",
                            "id INT(11) NOT NULL AUTO_INCREMENT, "
                            + "title VARCHAR(255) NULL, "
                            + "date VARCHAR(45) NULL, "
                            + "doi VARCHAR(255) NULL, "
                            + "autors VARCHAR(255) NULL, "
                            + "type VARCHAR(255) NULL, "
                            + "typeName VARCHAR(255) NULL, "
                            + "VIPAuthor VARCHAR(255) NULL, "
                            + "PRIMARY KEY (id), "
                            + "FOREIGN KEY (VIPAuthor) REFERENCES VIPUsers(email) "
                            + "ON DELETE CASCADE ON UPDATE CASCADE");

                if (createTable(connection,
                            "VIPTermsOfuse",
                            "idTermsOfuse INT(11) NOT NULL AUTO_INCREMENT, "
                            + "date TIMESTAMP NULL, "
                            + "PRIMARY KEY (idTermsOfuse)")) {
                    try {
                        java.util.Date today = new java.util.Date();
                        CoreDAOFactory.getDAOFactory().getTermsUseDAO(connection).add(
                                new TermsOfUse(new java.sql.Timestamp(today.getTime())));
                    } catch (DAOException ex) {
                        logger.error("Error creating VIPGroups table", ex);
                    }
                }

                firstExecution = false;
            } catch (SQLException e) {
                logger.error("Error while getting the connection or closing it", e);
            }
        }
    }

    // We throw a SQLException and not a DAOException because the client of this
    // function must anyway catch the SQLException when closing the connection.
    // This avoids him to have to catch 2 types of exceptions.
    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        return connection;
    }

    /**
     * Creates a table in the platform database.
     *
     * @param name Table name
     * @param columnsDefinition SQL syntax to define columns
     * @return
     */
    public boolean createTable(
        Connection connection, String name, String columnsDefinition) {

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE " + name + " ("
                    + columnsDefinition + ") ENGINE=InnoDB");

            logger.info("Table " + name + " successfully created.");
            return true;

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("already exists")) {
                logger.error("Error creating db table" + name, ex);
            }
            return false;
        }
    }
}
