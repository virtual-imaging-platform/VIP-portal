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

import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Rafael Silva
 */
public class PlatformConnection {

    private static PlatformConnection instance;
    private final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private final String DBURL = "jdbc:derby://localhost:1527/";
    private Connection connection;

    public synchronized static PlatformConnection getInstance() {
        if (instance == null) {
            instance = new PlatformConnection();
        }
        return instance;
    }

    private PlatformConnection() {
        connect();
        createTables();
    }

    private void connect() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DBURL
                    + ServerConfiguration.getInstance().getConfDirPath()
                    + "vip.db;create=true");
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            try {
                connection = DriverManager.getConnection(DBURL
                        + ServerConfiguration.getInstance().getConfDirPath()
                        + "vip.db");
                connection.setAutoCommit(true);

            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            //TODO parse exeception
            ex.printStackTrace();
        }
    }

    private void createTables() {
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE WorkflowInput ("
                    + "username VARCHAR(255), "
                    + "application VARCHAR(100), "
                    + "name VARCHAR(255), "
                    + "inputs VARCHAR(32000), "
                    + "PRIMARY KEY (username, name, application)"
                    + ")");
//            stat.executeUpdate("CREATE INDEX name_workflow_input_idx "
//                    + "ON WorkflowInput(username, name)");
        } catch (SQLException ex) {
            System.out.println("Table WorkflowInput already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE WorkflowDescriptor ("
                    + "name VARCHAR(255), "
                    + "lfn VARCHAR(255), "
                    + "PRIMARY KEY (name)"
                    + ")");

        } catch (SQLException ex) {
            System.out.println("Table WorkflowDescriptor already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE WorkflowClass ("
                    + "name VARCHAR(255), "
                    + "PRIMARY KEY (name)"
                    + ")");

        } catch (SQLException ex) {
            System.out.println("Table WorkflowClass already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE WorkflowClasses ("
                    + "class VARCHAR(255), "
                    + "workflow VARCHAR(255), "
                    + "PRIMARY KEY (class, workflow), "
                    + "FOREIGN KEY (class) REFERENCES WorkflowClass(name) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "FOREIGN KEY (workflow) REFERENCES WorkflowDescriptor(name) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT"
                    + ")");

        } catch (SQLException ex) {
            System.out.println("Table WorkflowClasses already created!");
        }
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE PlatformGroups ("
                    + "groupname VARCHAR(255), "
                    + "PRIMARY KEY (groupname) "
                    + ")");

            stat.executeUpdate("INSERT INTO PlatformGroups(groupname) VALUES('Administrator')");

        } catch (SQLException ex) {
            System.out.println("Table PlatformGroups already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE PlatformUsers ("
                    + "dn VARCHAR(255), "
                    + "PRIMARY KEY (dn) "
                    + ")");

        } catch (SQLException ex) {
            System.out.println("Table PlatformUsers already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE PlatformUsersGroups ("
                    + "userdn VARCHAR(255), "
                    + "groupname VARCHAR(255), "
                    + "role VARCHAR(30), "
                    + "PRIMARY KEY (userdn, groupname), "
                    + "FOREIGN KEY (userdn) REFERENCES PlatformUsers(dn) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "FOREIGN KEY (groupname) REFERENCES PlatformGroups(groupname) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT"
                    + ")");

        } catch (SQLException ex) {
            System.out.println("Table PlatformUsersGroups already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("INSERT INTO PlatformUsers (dn) "
                    + "VALUES('" + ServerConfiguration.getInstance().getAdminDN() + "')");

            stat.executeUpdate("INSERT INTO PlatformUsersGroups (userdn, groupname, role) "
                    + "VALUES('" + ServerConfiguration.getInstance().getAdminDN() + "', "
                    + "'Administrator', 'admin')");

        } catch (SQLException ex) {
            System.out.println("Administrator user already setted!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE PlatformGroupsClasses ("
                    + "classname VARCHAR(255), "
                    + "groupname VARCHAR(255), "
                    + "PRIMARY KEY (classname, groupname), "
                    + "FOREIGN KEY (classname) REFERENCES WorkflowClass(name) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "FOREIGN KEY (groupname) REFERENCES PlatformGroups(groupname) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT"
                    + ")");

        } catch (SQLException ex) {
            System.out.println("Table PlatformGroupsClasses already created!");
        }

        //// tissues and physical parameters
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE Tissues ("
                    + "name VARCHAR(255), "
                    + "ontologyId INT,"
                    + "PRIMARY KEY (name)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table Tissues already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE PhysicalProperties ("
                    + "tissueName VARCHAR(255),"
                    + "physicalPropertyId INT, "
                    + "author VARCHAR(255),"
                    + "comment VARCHAR(255),"
                    + "type VARCHAR(255),"
                    + "date DATE,"
                    + "PRIMARY KEY (physicalPropertyId)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table PhysicalProperties already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE ChemicalBlend ("
                    + "physicalPropertyId INT,"
                    + "density DOUBLE,"
                    + "phase VARCHAR(255),"
                    + "PRIMARY KEY (physicalPropertyId)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table ChemicalBlend already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE ChemicalComponents ("
                    + "physicalPropertyId INT,"
                    + "massPercentage DOUBLE,"
                    + "elementName VARCHAR(255),"
                    + " PRIMARY KEY (physicalPropertyId, elementName))");
        } catch (SQLException ex) {
            System.out.println("Table ChemicalComponent already created!");
        }


        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE MagneticProperties ("
                    + "physicalPropertyId INT,"
                    + "propertyName VARCHAR(255),"
                    + "distInstancId INT,"
                    + "PRIMARY KEY (physicalPropertyId, propertyName)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table MagneticProperties already created!");
        }


        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE MagneticPropertyNames ("
                    + "propertyName VARCHAR(255),"
                    + "PRIMARY KEY (propertyName)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table MagneticPropertyNames already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE Echogenicities ("
                    + "physicalPropertyId INT,"
                    + "spatDistInstanceId INT,"
                    + "ampDistInstanceId INT,"
                    + "PRIMARY KEY (physicalPropertyId)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table Echogenicities already created!");
        }

        ////distributions
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE Distribution ("
                    + "distributionName VARCHAR(255),"
                    + "expression VARCHAR(255),"
                    + "PRIMARY KEY (distributionName)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table Distribution already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE DistributionParameters ("
                    + "distributionName VARCHAR(255),"
                    + "parameterName VARCHAR(255),"
                    + "symbol VARCHAR(255),"
                    + "PRIMARY KEY (distributionName,symbol)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table DistributionParameters already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE DistributionInstance ("
                    + "instanceid INT,"
                    + "distName VARCHAR(255),"
                    + "PRIMARY KEY (instanceid)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table DistributionInstance already created!");
        }

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE DistributionInstanceValues ("
                    + "instanceid INT,"
                    + "parameterSymbol VARCHAR(255),"
                    + "value DOUBLE,"
                    + "PRIMARY KEY (instanceid,parameterSymbol)"
                    + ")");
        } catch (SQLException ex) {
            System.out.println("Table DistributionInstanceValues already created!");
        }

    }

    public Connection getConnection() {
        return connection;
    }
}
