/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.server.dao.h2.*;
import fr.insalyon.creatis.vip.application.server.dao.mysql.ApplicationData;
import fr.insalyon.creatis.vip.application.server.dao.mysql.ApplicationInputData;
import fr.insalyon.creatis.vip.application.server.dao.mysql.ClassData;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.h2.PlatformConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class H2DAOFactory extends ApplicationDAOFactory {

    private final static Logger logger = Logger.getLogger(H2DAOFactory.class);
    private static ApplicationDAOFactory instance;

    // Singleton
    protected static ApplicationDAOFactory getInstance() {
        if (instance == null) {
            instance = new H2DAOFactory();
        }
        return instance;
    }

    private H2DAOFactory() {

        try {
            logger.info("Configuring VIP Application database.");

            PlatformConnection.getInstance().createTable("VIPClasses",
                    "name VARCHAR(255), "
                    + "PRIMARY KEY (name)");

            PlatformConnection.getInstance().createTable("VIPGroupsClasses",
                    "classname VARCHAR(255), "
                    + "groupname VARCHAR(255), "
                    + "PRIMARY KEY (classname, groupname), "
                    + "FOREIGN KEY (classname) REFERENCES VIPClasses(name) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "FOREIGN KEY (groupname) REFERENCES VIPGroups(groupname) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT");

            PlatformConnection.getInstance().createTable("VIPApplications",
                    "name VARCHAR(255), "
                    + "lfn VARCHAR(255), "
                    + "citation CLOB, "
                    + "PRIMARY KEY (name)");

            PlatformConnection.getInstance().createTable("VIPApplicationClasses",
                    "class VARCHAR(255), "
                    + "application VARCHAR(255), "
                    + "PRIMARY KEY (class, application), "
                    + "FOREIGN KEY (class) REFERENCES VIPClasses(name) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT, "
                    + "FOREIGN KEY (application) REFERENCES VIPApplications(name) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT");

            PlatformConnection.getInstance().createTable("VIPAppInputs",
                    "email VARCHAR(255), "
                    + "application VARCHAR(255), "
                    + "name VARCHAR(255), "
                    + "inputs VARCHAR(32000), "
                    + "PRIMARY KEY (email, application, name), "
                    + "FOREIGN KEY (email) REFERENCES VIPUsers(email) "
                    + "ON DELETE CASCADE ON UPDATE RESTRICT");

            PlatformConnection.getInstance().createTable("VIPAppExamples",
                    "application VARCHAR(255), "
                    + "name VARCHAR(255), "
                    + "inputs VARCHAR(32000), "
                    + "PRIMARY KEY (application, name)");

        } catch (DAOException ex) {
            logger.error(ex);
        }
    }

    @Override
    public ApplicationDAO getApplicationDAO() throws DAOException {
        return new ApplicationData();
    }

    @Override
    public ClassDAO getClassDAO() throws DAOException {
        return new ClassData();
    }

    @Override
    public ApplicationInputDAO getApplicationInputDAO() throws DAOException {
        return new ApplicationInputData();
    }

    @Override
    public SimulationDAO getSimulationDAO(String dbPath) throws DAOException {
        return new SimulationData(dbPath);
    }

    @Override
    public ExecutionNodeDAO getExecutionNodeDAO(String dbPath) throws DAOException {
        return new ExecutionNodeData(dbPath);
    }

    @Override
    public EngineDAO getEngineDAO() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
