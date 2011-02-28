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
package fr.insalyon.creatis.vip.portal.server.dao.derby.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class JobsConnection {

    private static JobsConnection instance;
    private final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private final String DBURL = "jdbc:derby://localhost:1527/";
    private Map<String, Connection> connections = new HashMap<String, Connection>();
    private Map<String, Integer> usersConnections = new HashMap<String, Integer>();

    public synchronized static JobsConnection getInstance() {
        if (instance == null) {
            instance = new JobsConnection();
        }
        return instance;
    }

    private JobsConnection() {
    }

    public synchronized Connection connect(String dbPath) {

        Connection connection;
        try {
            if (!connections.containsKey(dbPath)) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(DBURL + dbPath + ";create=true");
                connection.setAutoCommit(true);
                connections.put(dbPath, connection);
                usersConnections.put(dbPath, 1);
            } else {
                connection = connections.get(dbPath);
                int n = usersConnections.get(dbPath);
                usersConnections.put(dbPath, n + 1);
            }
            return connection;

        } catch (SQLException ex) {
            try {
                connection = DriverManager.getConnection(DBURL + dbPath);
                connection.setAutoCommit(true);
                connections.put(dbPath, connection);
                usersConnections.put(dbPath, 1);

            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            //TODO parse exeception
            ex.printStackTrace();
        }
        return null;
    }

    public synchronized void close(String dbPath) {
        try {
            int n = usersConnections.get(dbPath);
            if (n == 1) {
                connections.get(dbPath).close();
                usersConnections.remove(dbPath);
                connections.remove(dbPath);
            } else {
                usersConnections.put(dbPath, n - 1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
