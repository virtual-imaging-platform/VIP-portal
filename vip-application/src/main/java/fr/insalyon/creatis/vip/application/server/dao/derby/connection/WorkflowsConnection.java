/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.dao.derby.connection;

import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowsConnection {

    private static WorkflowsConnection instance;
    private final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private final String DBURL = "jdbc:derby://" 
            + ServerConfiguration.getInstance().getWorkflowsHost() + ":"
            + ServerConfiguration.getInstance().getWorkflowsPort() + "/";
    private Connection connection;

    public synchronized static WorkflowsConnection getInstance() throws DAOException {
        if (instance == null) {
            instance = new WorkflowsConnection();
        }
        return instance;
    }

    private WorkflowsConnection() throws DAOException {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DBURL + ServerConfiguration.getInstance().getWorkflowsDB() + ";create=true");
            connection.setAutoCommit(true);
            createTables();

        } catch (SQLException ex) {
            try {
                connection = DriverManager.getConnection(DBURL + ServerConfiguration.getInstance().getWorkflowsDB());
                connection.setAutoCommit(true);

            } catch (SQLException ex1) {
                throw new DAOException(ex1);
            }
        } catch (ClassNotFoundException ex) {
            throw new DAOException(ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() {
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE Workflows ("
                    + "id VARCHAR(255), "
                    + "application VARCHAR(255), "
                    + "username VARCHAR(255), "
                    + "launched TIMESTAMP, "
                    + "status VARCHAR(50), "
                    + "minor_status VARCHAR(100), "
                    + "moteur_id INTEGER, "
                    + "moteur_key INTEGER, "
                    + "PRIMARY KEY (id)"
                    + ")");
            stat.executeUpdate("CREATE INDEX username_workflow_idx "
                    + "ON Workflows(username)");
        } catch (SQLException ex) {
            System.out.println("Table Workflows already created!");
        }
    }
}
