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
package fr.insalyon.creatis.vip.publication.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.publication.server.dao.mysql.PublicationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Rafael Ferreira da Silva,Nouha boujelben
 */
public class MySQLDAOFactory extends PublicationDAOFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static PublicationDAOFactory instance;

    // Singleton
    protected static PublicationDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    private MySQLDAOFactory() {

        try(Connection connection = PlatformConnection.getInstance().getConnection()) {

            logger.info("Configuring VIP Publication database.");

            PlatformConnection.getInstance().createTable(
                    connection,
                    "VIPPublication",
                    "id INT(11) NOT NULL AUTO_INCREMENT, "
                            + "title text, "
                            + "date VARCHAR(45), "
                            + "doi VARCHAR(255), "
                            + "authors text, "
                            + "type VARCHAR(255), "
                            + "typeName VARCHAR(255), "
                            + "vipAuthor VARCHAR(255), "
                            + "vipApplication VARCHAR(255), "
                            + "PRIMARY KEY (id), "
                            + "FOREIGN KEY (vipAuthor) REFERENCES VIPUsers(email) "
                            + "ON DELETE SET NULL ON UPDATE CASCADE, "
                            + "FOREIGN KEY (vipApplication) REFERENCES VIPApplications(name) "
                            + "ON DELETE SET NULL ON UPDATE CASCADE");


        } catch (SQLException ex) {
            logger.error("Error initialising VIPPublication database", ex);
        }
    }

    @Override
    public PublicationDAO getPublicationDAO(Connection connection)
        throws DAOException {
        return new PublicationData(connection);
    }

}