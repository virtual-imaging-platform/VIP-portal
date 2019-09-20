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
package fr.insalyon.creatis.vip.datamanager.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.datamanager.server.dao.mysql.ApiKeysData;
import fr.insalyon.creatis.vip.datamanager.server.dao.mysql.SSHData;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author glatard
 */
class MySQLDAOFactory extends SSHDAOFactory {

    private final static Logger logger = Logger.getLogger(MySQLDAOFactory.class);
    private static MySQLDAOFactory instance;

    // Singleton
    protected static MySQLDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    private MySQLDAOFactory() {
        try(Connection connection = PlatformConnection.getInstance().getConnection()) {
            logger.info("Configuring VIP SSH database.");
            PlatformConnection.getInstance().createTable(
                connection,
                "VIPSSHAccounts",
                "email VARCHAR(255), LFCDir VARCHAR(255), "
                    + "sshUser VARCHAR(255), sshHost VARCHAR(255), sshDir VARCHAR(255), sshPort INT, validated BOOLEAN, "
                    + "auth_failed BOOLEAN, theEarliestNextSynchronistation TIMESTAMP, numberSynchronizationFailed BIGINT, "
                    + "transferType VARCHAR(255), deleteFilesFromSource BOOLEAN DEFAULT 0, active BOOLEAN DEFAULT 1, PRIMARY KEY(email,LFCDir), "
                    + "FOREIGN KEY (email) REFERENCES VIPUsers(email) "
                    + "ON DELETE CASCADE ON UPDATE CASCADE");

            PlatformConnection.getInstance().createTable(
                connection,
                "VIPApiKeys",
                "email VARCHAR(255),"
                + "identifier VARCHAR(50) NOT NULL,"
                + "apiKey VARCHAR(255),"
                + "FOREIGN KEY (email) REFERENCES VIPUsers(email) "
                + "ON DELETE CASCADE ON UPDATE CASCADE");
        } catch (SQLException ex) {
            logger.error("Error configuring DataManager database", ex);
        }
    }

    @Override
    public SSHDAO getSSHDAO(Connection connection) throws DAOException {
        return new SSHData(connection);
    }

    @Override
    public ApiKeysDAO getApiKeysDao(Connection connection) throws DAOException {
        return new ApiKeysData(connection);
    }
}
