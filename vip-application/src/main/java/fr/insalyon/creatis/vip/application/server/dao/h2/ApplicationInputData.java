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

import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationInputDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.h2.PlatformConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationInputData implements ApplicationInputDAO {

    private static final Logger logger = Logger.getLogger(ApplicationInputData.class);
    private Connection connection;

    public ApplicationInputData() throws DAOException {

        connection = PlatformConnection.getInstance().getConnection();
    }

    /**
     *
     * @param email
     * @param SimulationInput
     * @throws DAOException
     */
    public void addSimulationInput(String email, SimulationInput SimulationInput)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPAppInputs(email, application, name, inputs) "
                    + "VALUES (?, ?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, SimulationInput.getApplication());
            ps.setString(3, SimulationInput.getName());
            ps.setString(4, SimulationInput.getInputs());
            ps.execute();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation")) {
                logger.error("An input named \"" + SimulationInput.getName() + "\" already exists.");
                throw new DAOException("An input named \"" + SimulationInput.getName() + "\" already exists.");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    /**
     *
     * @param email
     * @param inputName
     * @param application
     * @throws DAOException
     */
    public void removeSimulationInput(String email, String inputName,
            String application) throws DAOException {

        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM VIPAppInputs WHERE email=? AND name=? AND application=?");

            stat.setString(1, email);
            stat.setString(2, inputName);
            stat.setString(3, application);
            stat.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param email
     * @param SimulationInput
     * @throws DAOException
     */
    public void updateSimulationInput(String email, SimulationInput SimulationInput)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE VIPAppInputs SET inputs=? "
                    + "WHERE email=? AND application=? AND name=?");

            ps.setString(1, SimulationInput.getInputs());
            ps.setString(2, email);
            ps.setString(3, SimulationInput.getApplication());
            ps.setString(4, SimulationInput.getName());
            ps.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public List<SimulationInput> getSimulationInputByUser(String email)
            throws DAOException {

        try {

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "application, name, inputs "
                    + "FROM VIPAppInputs WHERE email=? "
                    + "ORDER BY application, name");

            stat.setString(1, email);
            ResultSet rs = stat.executeQuery();
            List<SimulationInput> inputs = new ArrayList<SimulationInput>();

            while (rs.next()) {
                inputs.add(new SimulationInput(
                        rs.getString("application"),
                        rs.getString("name"),
                        rs.getString("inputs")));
            }

            return inputs;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public List<SimulationInput> getWorkflowInputByUserAndAppName(String user, String appName) throws DAOException {
        try {
            List<SimulationInput> inputs = new ArrayList<SimulationInput>();
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "username, application, name, inputs "
                    + "FROM WorkflowInput WHERE username=? AND application=? "
                    + "ORDER BY name");

            stat.setString(1, user);
            stat.setString(2, appName);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                inputs.add(new SimulationInput(
                        rs.getString("application"),
                        rs.getString("name"),
                        rs.getString("inputs")));
            }

            return inputs;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param email
     * @param name
     * @param appName
     * @return
     * @throws DAOException
     */
    public SimulationInput getInputByNameUserApp(String email, String name,
            String appName) throws DAOException {

        try {
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "email, application, name, inputs "
                    + "FROM VIPAppInputs "
                    + "WHERE email = ? AND name = ? AND application = ? "
                    + "ORDER BY name");

            stat.setString(1, email);
            stat.setString(2, name);
            stat.setString(3, appName);
            ResultSet rs = stat.executeQuery();

            rs.next();
            return new SimulationInput(
                    rs.getString("application"),
                    rs.getString("name"),
                    rs.getString("inputs"));

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     *
     * @param SimulationInput
     * @throws DAOException
     */
    public void saveSimulationInputAsExample(SimulationInput SimulationInput) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPAppExamples(application, name, inputs) "
                    + "VALUES (?, ?, ?)");

            ps.setString(1, SimulationInput.getApplication());
            ps.setString(2, SimulationInput.getName());
            ps.setString(3, SimulationInput.getInputs());
            ps.execute();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation")) {
                logger.error("An input named \"" + SimulationInput.getName() + "\" already exists.");
                throw new DAOException("An input named \"" + SimulationInput.getName() + "\" already exists.");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    /**
     * 
     * @return
     * @throws DAOException 
     */
    public List<SimulationInput> getSimulationInputExamples() throws DAOException {

        try {

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "application, name, inputs "
                    + "FROM VIPAppExamples "
                    + "ORDER BY application, name");

            ResultSet rs = stat.executeQuery();
            List<SimulationInput> inputs = new ArrayList<SimulationInput>();

            while (rs.next()) {
                inputs.add(new SimulationInput(
                        rs.getString("application"),
                        rs.getString("name"),
                        rs.getString("inputs")));
            }

            return inputs;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
    /**
     * 
     * @param inputName
     * @param application
     * @throws DAOException 
     */
    public void removeSimulationInputExample(String inputName, String application) 
            throws DAOException {

        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM VIPAppExamples WHERE name=? AND application=?");

            stat.setString(1, inputName);
            stat.setString(2, application);
            stat.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
