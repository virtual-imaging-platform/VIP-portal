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
package fr.insalyon.creatis.platform.main.server.dao.derby;

import fr.insalyon.creatis.platform.main.server.dao.derby.connection.PlatformConnection;
import fr.insalyon.creatis.platform.main.client.bean.WorkflowInput;
import fr.insalyon.creatis.platform.main.server.dao.WorkflowInputDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowInputData implements WorkflowInputDAO {

    private Connection connection;

    public WorkflowInputData() {
        connection = PlatformConnection.getInstance().getConnection();
    }

    public String addWorkflowInput(String user, WorkflowInput workflowInput) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO WorkflowInput(username, application, name, inputs) "
                    + "VALUES (?, ?, ?, ?)");

            ps.setString(1, user);
            ps.setString(2, workflowInput.getApplication());
            ps.setString(3, workflowInput.getName());
            ps.setString(4, workflowInput.getInputs());
            ps.execute();

            return "Input values were succesfully saved!";

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error: an entry named \"" + workflowInput.getName() + "\" already exists.";
        }
    }

    public void removeWorkflowInput(String user, String inputName) {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM WorkflowInput WHERE username=? AND name=?");

            stat.setString(1, user);
            stat.setString(2, inputName);
            stat.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<WorkflowInput> getWorkflowInputByUserAndAppName(String user, String appName) {
        try {
            List<WorkflowInput> inputs = new ArrayList<WorkflowInput>();
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "username, application, name, inputs "
                    + "FROM WorkflowInput WHERE username=? AND application=? "
                    + "ORDER BY name");

            stat.setString(1, user);
            stat.setString(2, appName);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                inputs.add(new WorkflowInput(
                        rs.getString("application"),
                        rs.getString("name"),
                        rs.getString("inputs")));
            }

            return inputs;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public WorkflowInput getWorkflowInputByUserAndName(String user, String name) {
        try {
            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "username, application, name, inputs "
                    + "FROM WorkflowInput WHERE username=? AND name=? "
                    + "ORDER BY name");

            stat.setString(1, user);
            stat.setString(2, name);
            ResultSet rs = stat.executeQuery();

            rs.next();
            return new WorkflowInput(
                    rs.getString("application"),
                    rs.getString("name"),
                    rs.getString("inputs"));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
