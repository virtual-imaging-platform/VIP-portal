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
import fr.insalyon.creatis.vip.application.server.dao.WorkflowInputDAO;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
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
public class WorkflowInputData implements WorkflowInputDAO {

    private static final Logger logger = Logger.getLogger(WorkflowInputData.class);
    private Connection connection;

    public WorkflowInputData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    public String addWorkflowInput(String user, SimulationInput SimulationInput) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO WorkflowInput(username, application, name, inputs) "
                    + "VALUES (?, ?, ?, ?)");

            ps.setString(1, user);
            ps.setString(2, SimulationInput.getApplication());
            ps.setString(3, SimulationInput.getName());
            ps.setString(4, SimulationInput.getInputs());
            ps.execute();

            return "Input values were succesfully saved!";

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException("Error: an entry named \"" + SimulationInput.getName() + "\" already exists.");
        }
    }

    public void removeWorkflowInput(String user, String inputName, String application) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM WorkflowInput WHERE username=? AND name=? AND application=?");

            stat.setString(1, user);
            stat.setString(2, inputName);
            stat.setString(3, application);
            stat.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public List<SimulationInput> getWorkflowInputByUser(String user) throws DAOException {
        try {
            List<SimulationInput> inputs = new ArrayList<SimulationInput>();
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "application, name, inputs "
                    + "FROM WorkflowInput WHERE username=? "
                    + "ORDER BY application, name");

            stat.setString(1, user);
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

    public SimulationInput getWorkflowInputByUserAndName(String user, String name) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "username, application, name, inputs "
                    + "FROM WorkflowInput WHERE username=? AND name=? "
                    + "ORDER BY name");

            stat.setString(1, user);
            stat.setString(2, name);
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
}
