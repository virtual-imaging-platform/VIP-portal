/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.server.dao.h2;

import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractJobData {

    private final String DRIVER = "org.h2.Driver";
    private final String DBURL = "jdbc:h2:tcp://"
            + Server.getInstance().getWorkflowsHost() + ":"
            //            + Server.getInstance().getWorkflowsPort() + "/"
            + "9092/"
            + Server.getInstance().getWorkflowsPath() + "/";
    protected Connection connection;

    public AbstractJobData(String dbPath) throws DAOException {

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DBURL + dbPath + "/db/jobs", "gasw", "gasw");
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException ex) {
            throw new DAOException(ex);
        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    /**
     * Closes database connection.
     */
    protected void close(Logger logger) {

        try {
            connection.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }
}
