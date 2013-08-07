package fr.insalyon.creatis.vip.query.server.dao.business;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class MySQLDAOFactory extends QueryDAOFactory {

    private final static Logger logger = Logger.getLogger(MySQLDAOFactory.class);
    private static QueryDAOFactory instance;

    // Singleton
    protected static QueryDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    private MySQLDAOFactory() {

        try {
            logger.info("Configuring VIP Query database.");

            PlatformConnection.getInstance().createTable("Query",
                    "queryID BIGINT(20) AUTO_INCREMENT, "
                    + "description TEXT NULL, "
                    + "queryMaker VARCHAR(255), "
                    + "queryName VARCHAR(255), "
                    + "PRIMARY KEY (queryID), "
                    + "FOREIGN KEY (queryMaker) REFERENCES vipusers(email) "
                    + "ON DELETE RESTRICT ON UPDATE RESTRICT");


            PlatformConnection.getInstance().createTable("QueryVersion",
                    "queryVersionID BIGINT(20) AUTO_INCREMENT, "
                    + "queryVersion BIGINT(20), "
                    + "queryID BIGINT(20), "
                    + "body TEXT, "
                    + "dateCreation TIMESTAMP, "
                    + "PRIMARY KEY (queryVersionID ), "
                    + "FOREIGN KEY (queryID) REFERENCES Query(queryID) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT");

            PlatformConnection.getInstance().createTable("Parameter",
                    "parameterID BIGINT(20) AUTO_INCREMENT, "
                    + "name VARCHAR(255), "
                    + "type VARCHAR(255), "
                    + "description VARCHAR(255), "
                    + "example VARCHAR(255), "
                    + "queryVersionID BIGINT(20), "
                    + "PRIMARY KEY (parameterID), "
                    + "FOREIGN KEY (queryVersionID) REFERENCES QueryVersion(queryVersionID) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT");

            PlatformConnection.getInstance().createTable("QueryExecution",
                    "queryExecutionID BIGINT(20) AUTO_INCREMENT, "
                    + "queryVersionID BIGINT(20), "
                    + "name VARCHAR(255), "
                    + "dateExecution TIMESTAMP, "
                    + "dateEndExecution TIMESTAMP, "
                    + "urlResult VARCHAR(1000), "
                    + "executer VARCHAR(255), "
                    + "status VARCHAR(255), "
                    + "PRIMARY KEY (queryExecutionID), "
                    + "FOREIGN KEY (queryVersionID) REFERENCES QueryVersion(queryVersionID) "
                    + "ON DELETE RESTRICT ON UPDATE RESTRICT");

            PlatformConnection.getInstance().createTable("Value",
                    "ValueID BIGINT(20) AUTO_INCREMENT, "
                    + "value VARCHAR(255), "
                    + "parameterID BIGINT(20), "
                    + "queryExecutionID BIGINT(20), "
                    + "PRIMARY KEY (ValueID ), "
                    + "FOREIGN KEY (parameterID) REFERENCES Parameter(parameterID) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "FOREIGN KEY (queryExecutionID) REFERENCES QueryExecution(queryExecutionID)"
                    + "ON DELETE CASCADE ON UPDATE RESTRICT");




        } catch (DAOException ex) {
            logger.error(ex);
        }
    }

    @Override
    public QueryDAO getQueryDAO() throws DAOException {
        return new QueryData();
    }
}
