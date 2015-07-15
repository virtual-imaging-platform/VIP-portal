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
package fr.insalyon.creatis.vip.query.server.dao.business;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author Nouha Boujelben
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
                    "queryID BIGINT(20) AUTO_INCREMENT NOT NULL, "   
                    + "queryMaker VARCHAR(255) NOT NULL, "
                    + "queryName VARCHAR(255), "
                    + "PRIMARY KEY (queryID), "
                    + "FOREIGN KEY (queryMaker) REFERENCES VIPUsers(email) "
                    + "ON DELETE RESTRICT ON UPDATE RESTRICT");


            PlatformConnection.getInstance().createTable("QueryVersion",
                    "queryVersionID BIGINT(20) AUTO_INCREMENT, "
                    + "queryVersion BIGINT(20), "
                    + "queryID BIGINT(20), "
                    + "body TEXT, "
                    + "dateCreation TIMESTAMP, "
                    + "description TEXT NULL, "
                    + "isPublic BOOLEAN DEFAULT 0, "
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
                    + "executer VARCHAR(255), "
                    + "status VARCHAR(255), "
                    + "bodyResult VARCHAR(1000), "
                    + "pathFileResult VARCHAR(1000), "
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
