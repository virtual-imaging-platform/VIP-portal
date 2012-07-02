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

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
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
public class ApplicationData implements ApplicationDAO {

    private final static Logger logger = Logger.getLogger(ApplicationData.class);
    private Connection connection;

    public ApplicationData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    /**
     * 
     * @param application
     * @throws DAOException 
     */
    @Override
    public void add(Application application) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPApplications(name, lfn) "
                    + "VALUES (?, ?)");

            ps.setString(1, application.getName());
            ps.setString(2, application.getLfn());
            ps.execute();

            for (String className : application.getApplicationClasses()) {
                addClassToApplication(application.getName(), className);
            }
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation")) {
                logger.error("An application named \"" + application.getName() + "\" already exists.");
                throw new DAOException("An application named \"" + application.getName() + "\" already exists.");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    /**
     * 
     * @param application
     * @throws DAOException 
     */
    @Override
    public void update(Application application) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPApplications "
                    + "SET lfn=? "
                    + "WHERE name=?");

            ps.setString(1, application.getLfn());
            ps.setString(2, application.getName());
            ps.executeUpdate();
            ps.close();

            removeAllClassesFromApplication(application.getName());
            for (String className : application.getApplicationClasses()) {
                addClassToApplication(application.getName(), className);
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param name
     * @throws DAOException 
     */
    @Override
    public void remove(String name) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPApplications WHERE name=?");

            ps.setString(1, name);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param email
     * @param name
     * @throws DAOException 
     */
    @Override
    public void remove(String email, String name) throws DAOException {

        try {
            for (AppClass c : ApplicationDAOFactory.getDAOFactory().getClassDAO().getUserClasses(email, true)) {
                PreparedStatement ps = connection.prepareStatement("DELETE "
                        + "FROM VIPApplicationClasses "
                        + "WHERE class=? AND application=?");

                ps.setString(1, c.getName());
                ps.setString(2, name);
                ps.execute();
                ps.close();
            }

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws DAOException 
     */
    @Override
    public List<Application> getApplications() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT name, lfn FROM "
                    + "VIPApplications ORDER BY name");

            ResultSet rs = ps.executeQuery();
            List<Application> applications = new ArrayList<Application>();

            while (rs.next()) {

                String name = rs.getString("name");
                PreparedStatement stat2 = connection.prepareStatement("SELECT "
                        + "class FROM VIPApplicationClasses WHERE application=?");

                stat2.setString(1, name);

                ResultSet rs2 = stat2.executeQuery();
                List<String> classes = new ArrayList<String>();

                while (rs2.next()) {
                    classes.add(rs2.getString("class"));
                }

                applications.add(new Application(name,
                        rs.getString("lfn"), classes));
            }
            ps.close();
            return applications;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param className
     * @return
     * @throws DAOException 
     */
    @Override
    public List<Application> getApplications(String className) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "name, lfn FROM "
                    + "VIPApplications app, VIPApplicationClasses appc "
                    + "WHERE appc.class = ? AND app.name = appc.application "
                    + "ORDER BY name");
            ps.setString(1, className);
            
            ResultSet rs = ps.executeQuery();
            List<Application> applications = new ArrayList<Application>();
            
            while (rs.next()) {
                applications.add(new Application(
                        rs.getString("name"), rs.getString("lfn")));
            }
            ps.close();
            return applications;
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param classes
     * @return
     * @throws DAOException 
     */
    @Override
    public List<Application> getApplications(List<String> classes) throws DAOException {

        try {
            List<Application> applications = new ArrayList<Application>();

            if (!classes.isEmpty()) {
                StringBuilder sb = new StringBuilder();

                for (String c : classes) {
                    if (sb.length() > 0) {
                        sb.append(" OR ");
                    }
                    sb.append("appc.class = '").append(c).append("'");
                }

                String clause = sb.length() > 0 ? " AND (" + sb.toString() + ")" : "";

                PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT "
                        + "name, lfn FROM "
                        + "VIPApplications app, VIPApplicationClasses appc "
                        + "WHERE app.name = appc.application " + clause + " "
                        + "ORDER BY name");

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("name");
                    PreparedStatement stat2 = connection.prepareStatement("SELECT "
                            + "class FROM VIPApplicationClasses WHERE application = ?");

                    stat2.setString(1, name);

                    ResultSet rs2 = stat2.executeQuery();
                    List<String> appClasses = new ArrayList<String>();

                    while (rs2.next()) {
                        appClasses.add(rs2.getString("class"));
                    }

                    applications.add(new Application(name,
                            rs.getString("lfn"), appClasses));
                }
                ps.close();
            }
            return applications;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param applicationName
     * @return
     * @throws DAOException 
     */
    @Override
    public Application getApplication(String applicationName) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "class "
                    + "FROM VIPApplicationClasses "
                    + "WHERE application=?");

            ps.setString(1, applicationName);
            ResultSet rs = ps.executeQuery();
            List<String> classes = new ArrayList<String>();

            while (rs.next()) {
                classes.add(rs.getString("class"));
            }

            ps = connection.prepareStatement("SELECT "
                    + "name, lfn "
                    + "FROM VIPApplications "
                    + "WHERE name=?");

            ps.setString(1, applicationName);
            rs = ps.executeQuery();
            rs.next();
            
            Application application = new Application(rs.getString("name"),
                    rs.getString("lfn"), classes);

            ps.close();
            return application;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param applicationClass
     * @return 
     */
    @Override
    public List<String> getApplicationsName(String applicationClass) {
        try {

            List<String> applications = new ArrayList<String>();
            PreparedStatement ps = null;
            if (applicationClass == null) {
                ps = connection.prepareStatement("SELECT name, lfn FROM "
                        + "WorkflowDescriptor ORDER BY name");
            } else {
                ps = connection.prepareStatement("SELECT name, lfn FROM "
                        + "WorkflowDescriptor wd, WorkflowClasses wc "
                        + "WHERE (wc.workflow=wd.name AND class=?)");
                ps.setString(1, applicationClass);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                applications.add(rs.getString("name"));
            }
            
            ps.close();
            return applications;

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return null;
    }

    /**
     * 
     * @param applicationName
     * @param className
     * @throws DAOException 
     */
    private void addClassToApplication(String applicationName, String className)
            throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO "
                    + "VIPApplicationClasses(application, class) "
                    + "VALUES(?, ?)");

            ps.setString(1, applicationName);
            ps.setString(2, className);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation")) {
                logger.error("An application named \"" + applicationName + "\" is already associated with clas \"" + className + "\".");
                throw new DAOException("An application named \"" + applicationName + "\" is already associated with clas \"" + className + "\".");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    /**
     * 
     * @param workflowName
     * @throws DAOException 
     */
    private void removeAllClassesFromApplication(String workflowName) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM "
                    + "VIPApplicationClasses WHERE application=?");

            ps.setString(1, workflowName);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
