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
package fr.insalyon.creatis.vip.core.server.dao.h2;

import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.client.bean.Application;
import fr.insalyon.creatis.vip.core.server.dao.ApplicationDAO;
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
public class ApplicationData implements ApplicationDAO {

    private final static Logger logger = Logger.getLogger(ApplicationData.class);
    private Connection connection;

    public ApplicationData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    public String add(Application workflowDescriptor) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO WorkflowDescriptor(name, lfn) "
                    + "VALUES (?, ?)");

            ps.setString(1, workflowDescriptor.getName());
            ps.setString(2, workflowDescriptor.getLfn());
            ps.execute();

            for (String className : workflowDescriptor.getApplicationClasses()) {
                addClassToWorkflow(workflowDescriptor.getName(), className);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            return "Error: an application named \"" + workflowDescriptor.getName() + "\" already exists.";
        } catch (DAOException ex) {
            logger.error(ex);
            return "Error: " + ex.getMessage();
        }
        return "The application was succesfully saved!";
    }

    public String update(Application workflowDescriptor) {
        try {
            PreparedStatement stat = connection.prepareStatement("UPDATE "
                    + "WorkflowDescriptor "
                    + "SET lfn=? "
                    + "WHERE name=?");

            stat.setString(1, workflowDescriptor.getLfn());
            stat.setString(2, workflowDescriptor.getName());
            stat.executeUpdate();

            removeAllClassesFromWorkflow(workflowDescriptor.getName());
            for (String className : workflowDescriptor.getApplicationClasses()) {
                addClassToWorkflow(workflowDescriptor.getName(), className);
            }

            return "The application was succesfully updated!";

        } catch (SQLException ex) {
            logger.error(ex);
            return "Error: " + ex.getMessage();
        } catch (DAOException ex) {
            logger.error(ex);
            return "Error: " + ex.getMessage();
        }
    }

    public void remove(String name) {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM WorkflowDescriptor WHERE name=?");

            stat.setString(1, name);
            stat.execute();

        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    public void removeClassFromApplication(String applicationClass, String name) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM WorkflowClasses WHERE class=? AND workflow=?");
            ps.setString(1, applicationClass);
            ps.setString(2, name);
            ps.execute();
            
        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    public List<Application> getApplications(String applicationClass) throws DAOException {
        try {

            List<Application> applications = new ArrayList<Application>();
            PreparedStatement stat = null;
            if (applicationClass == null) {
                stat = connection.prepareStatement("SELECT name, lfn FROM "
                        + "WorkflowDescriptor ORDER BY name");
            } else {
                stat = connection.prepareStatement("SELECT name, lfn FROM "
                        + "WorkflowDescriptor wd, WorkflowClasses wc "
                        + "WHERE (wc.workflow=wd.name AND class=?)");
                stat.setString(1, applicationClass);
            }

            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                PreparedStatement stat2 = connection.prepareStatement("SELECT "
                        + "class FROM WorkflowClasses WHERE workflow=?");
                stat2.setString(1, name);

                List<String> classes = new ArrayList<String>();
                ResultSet rs2 = stat2.executeQuery();
                while (rs2.next()) {
                    classes.add(rs2.getString("class"));
                }

                applications.add(new Application(name,
                        rs.getString("lfn"), classes));
            }
            return applications;

        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    public List<String> getApplicationsName(String applicationClass) {
        try {

            List<String> applications = new ArrayList<String>();
            PreparedStatement stat = null;
            if (applicationClass == null) {
                stat = connection.prepareStatement("SELECT name, lfn FROM "
                        + "WorkflowDescriptor ORDER BY name");
            } else {
                stat = connection.prepareStatement("SELECT name, lfn FROM "
                        + "WorkflowDescriptor wd, WorkflowClasses wc "
                        + "WHERE (wc.workflow=wd.name AND class=?)");
                stat.setString(1, applicationClass);
            }

            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                applications.add(rs.getString("name"));
            }
            return applications;

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return null;
    }

    public Application getApplication(String name) {
        try {
            List<String> classes = new ArrayList<String>();

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "class "
                    + "FROM WorkflowClasses "
                    + "WHERE workflow=?");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                classes.add(rs.getString("class"));
            }

            stat = connection.prepareStatement("SELECT "
                    + "name, lfn "
                    + "FROM WorkflowDescriptor "
                    + "WHERE name=?");

            stat.setString(1, name);
            rs = stat.executeQuery();
            rs.next();

            return new Application(rs.getString("name"),
                    rs.getString("lfn"), classes);

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return null;
    }

    private void addClassToWorkflow(String workflowName, String className) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO WorkflowClasses(workflow, class) " + "VALUES(?, ?)");
            ps.setString(1, workflowName);
            ps.setString(2, className);
            ps.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException("Error: an application named \"" + workflowName + "\" is already associated with classes.");
        }
    }

    private void removeAllClassesFromWorkflow(String workflowName) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM WorkflowClasses WHERE workflow=?");
            ps.setString(1, workflowName);
            ps.execute();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex.getMessage());
        }
    }
}
