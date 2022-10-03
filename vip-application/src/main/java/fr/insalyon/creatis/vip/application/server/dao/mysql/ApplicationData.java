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
package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.ClassDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Repository
@Transactional
public class ApplicationData extends JdbcDaoSupport implements ApplicationDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ClassDAO classDAO;

    @Autowired
    public ApplicationData(DataSource dataSource, ClassDAO classDAO) {
        setDataSource(dataSource);
        this.classDAO = classDAO;
    }

    @Override
    public void add(Application application) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO VIPApplications(name, citation, owner) "
                    + "VALUES (?, ?, ?)");

            ps.setString(1, application.getName());
            ps.setString(2, application.getCitation());
            ps.setString(3, application.getOwner());
            ps.execute();

            for (String className : application.getApplicationClasses()) {
                addClassToApplication(application.getName(), className);
            }
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("An application named \"{}\" already exists.", application.getName());
                throw new DAOException("An application named \"" + application.getName() + "\" already exists.", ex);
            } else {
                logger.error("Error adding application {}", application.getName(), ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void update(Application application) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                                                               + "VIPApplications "
                                                               + "SET citation=?,owner=? "
                                                               + "WHERE name=?");

            ps.setString(1, application.getCitation());
            ps.setString(2, application.getOwner());
            ps.setString(3, application.getName());
            ps.executeUpdate();
            ps.close();
            removeAllClassesFromApplication(application.getName());
            for (String className : application.getApplicationClasses()) {
                if (!className.equals("")) {
                    addClassToApplication(application.getName(), className);
                }
            }

        } catch (SQLException ex) {
            logger.error("Error updating application {}", application.getName(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remove(String name) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE "
                                                               + "FROM VIPApplications WHERE name=?");

            ps.setString(1, name);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing application {}", name, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remove(String email, String name) throws DAOException {

        try {
            for (AppClass c : classDAO.getUserClasses(email, true)) {
                PreparedStatement ps = getConnection().prepareStatement("DELETE "
                                                                   + "FROM VIPApplicationClasses "
                                                                   + "WHERE class=? AND application=?");

                ps.setString(1, c.getName());
                ps.setString(2, name);
                ps.execute();
                ps.close();
            }

        } catch (SQLException ex) {
            logger.error("Error removing application {} for user {}", name, email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Application> getApplications() throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                                                               + "name, owner, citation FROM "
                                                               + "VIPApplications ORDER BY name");

            ResultSet rs = ps.executeQuery();
            List<Application> applications = new ArrayList<Application>();

            while (rs.next()) {

                String owner = rs.getString("owner");
                PreparedStatement ps3 = getConnection().prepareStatement("SELECT "
                                                                    + "first_name,last_name FROM VIPUsers WHERE email=?");
                ps3.setString(1, owner);
                ResultSet rs3 = ps3.executeQuery();
                String firstName = null;
                String lastName = null;
                while (rs3.next()) {
                    firstName = rs3.getString("first_name");
                    lastName = rs3.getString("last_name");
                }
                ps3.close();

                String name = rs.getString("name");
                PreparedStatement ps2 = getConnection().prepareStatement("SELECT "
                                                                    + "class FROM VIPApplicationClasses WHERE application=?");
                ps2.setString(1, name);

                ResultSet rs2 = ps2.executeQuery();
                List<String> classes = new ArrayList<String>();

                while (rs2.next()) {
                    classes.add(rs2.getString("class"));
                }
                ps2.close();

                applications.add(new Application(name, classes, rs.getString("owner"), firstName + " " + lastName, rs.getString("citation")));
            }
            ps.close();
            return applications;

        } catch (SQLException ex) {
            logger.error("Error getting all applications", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Application> getApplicationsOnlyDev(String email) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                                                                + "name, owner, citation FROM "
                                                                + "VIPApplications WHERE owner IN (?)");

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            List<Application> applications = new ArrayList<Application>();

            while (rs.next()) {

                String owner = rs.getString("owner");
                PreparedStatement ps3 = getConnection().prepareStatement("SELECT "
                        + "first_name,last_name FROM VIPUsers WHERE email=?");
                ps3.setString(1, owner);
                ResultSet rs3 = ps3.executeQuery();
                String firstName = null;
                String lastName = null;
                while (rs3.next()) {
                    firstName = rs3.getString("first_name");
                    lastName = rs3.getString("last_name");
                }
                ps3.close();

                String name = rs.getString("name");
                PreparedStatement ps2 = getConnection().prepareStatement("SELECT "
                        + "class FROM VIPApplicationClasses WHERE application=?");
                ps2.setString(1, name);

                ResultSet rs2 = ps2.executeQuery();
                List<String> classes = new ArrayList<String>();

                while (rs2.next()) {
                    classes.add(rs2.getString("class"));
                }
                ps2.close();

                applications.add(new Application(name, classes, rs.getString("owner"), firstName + " " + lastName, rs.getString("citation")));
            }
            ps.close();
            return applications;

        } catch (SQLException ex) {
            logger.error("Error getting all applications", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<String[]> getApplications(String className) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                                                               + "name, version FROM "
                                                               + "VIPApplications app, VIPAppVersions ver, VIPApplicationClasses appc "
                                                               + "WHERE appc.class = ? AND app.name = appc.application AND "
                                                               + "app.name = ver.application AND visible = ? "
                                                               + "ORDER BY app.name");
            ps.setString(1, className);
            ps.setBoolean(2, true);

            ResultSet rs = ps.executeQuery();
            List<String[]> applications = new ArrayList<String[]>();

            while (rs.next()) {
                applications.add(new String[]{rs.getString("name"), rs.getString("version")});
            }
            ps.close();
            return applications;

        } catch (SQLException ex) {
            logger.error("Error getting all applications for class {}", className, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public Application getApplication(String applicationName) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                                                               + "name, citation, owner FROM VIPApplications "
                                                               + "WHERE name = ?");
            ps.setString(1, applicationName);

            ResultSet rs = ps.executeQuery();
            if (rs.first()) {

                PreparedStatement ps2 = getConnection().prepareStatement("SELECT "
                                                                    + "class FROM VIPApplicationClasses WHERE application = ?");

                ps2.setString(1, applicationName);

                ResultSet rs2 = ps2.executeQuery();
                List<String> appClasses = new ArrayList<String>();

                while (rs2.next()) {
                    appClasses.add(rs2.getString("class"));
                }
                ps2.close();
                return new Application(rs.getString("name"), appClasses, rs.getString("owner"), rs.getString("citation"));
            }
            return null;
        } catch (SQLException ex) {
            logger.error("Error getting application {}", applicationName, ex);
            throw new DAOException(ex);
        }
    }

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

                PreparedStatement ps = getConnection().prepareStatement("SELECT DISTINCT "
                                                                   + "name, owner, citation FROM "
                                                                   + "VIPApplications app, VIPApplicationClasses appc "
                                                                   + "WHERE app.name = appc.application " + clause + " "
                                                                   + "ORDER BY name");

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("name");
                    PreparedStatement ps2 = getConnection().prepareStatement("SELECT "
                                                                        + "class FROM VIPApplicationClasses WHERE application = ?");

                    ps2.setString(1, name);

                    ResultSet rs2 = ps2.executeQuery();
                    List<String> appClasses = new ArrayList<String>();

                    while (rs2.next()) {
                        appClasses.add(rs2.getString("class"));
                    }
                    ps2.close();

                    String owner = rs.getString("owner");
                    PreparedStatement ps3 = getConnection().prepareStatement("SELECT "
                                                                        + "first_name,last_name FROM VIPUsers WHERE email=?");
                    ps3.setString(1, owner);
                    ResultSet rs3 = ps3.executeQuery();
                    String firstName = null;
                    String lastName = null;
                    while (rs3.next()) {
                        firstName = rs3.getString("first_name");
                        lastName = rs3.getString("last_name");
                    }
                    ps3.close();

                    applications.add(new Application(name, appClasses, rs.getString("owner"), firstName + " " + lastName, rs.getString("citation")));
                }
                ps.close();
            }
            return applications;

        } catch (SQLException ex) {
            logger.error("Error getting applications for classes {}", classes, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<String> getApplicationsName(String applicationClass) {
        try {

            List<String> applications = new ArrayList<String>();
            PreparedStatement ps = null;
            if (applicationClass == null) {
                ps = getConnection().prepareStatement("SELECT name FROM "
                                                 + "WorkflowDescriptor ORDER BY name");
            } else {
                ps = getConnection().prepareStatement("SELECT name FROM "
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
            logger.error("Error getting applications name {}", applicationClass, ex);
        }
        return null;
    }

    private void addClassToApplication(String applicationName, String className)
            throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO "
                                                               + "VIPApplicationClasses(application, class) "
                                                               + "VALUES(?, ?)");

            ps.setString(1, applicationName);
            ps.setString(2, className);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("An application named \"" + applicationName + "\" is already associated with clas \"" + className + "\".");
                throw new DAOException("An application named \"" + applicationName + "\" is already associated with clas \"" + className + "\".", ex);
            } else {
                logger.error("Error adding class {} to application {}", className, applicationName, ex);
                throw new DAOException(ex);
            }
        }
    }

    private void removeAllClassesFromApplication(String workflowName) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM "
                                                               + "VIPApplicationClasses WHERE application=?");

            ps.setString(1, workflowName);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing classes from application {}", workflowName, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public String getCitation(String name) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT citation "
                                                               + "FROM VIPApplications WHERE name = ?");
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            rs.next();
            String citation = rs.getString("citation");
            ps.close();

            return citation;

        } catch (SQLException ex) {
            logger.error("Error getting citation for application {}", name, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AppVersion> getVersions(String name) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                                                               + "version, lfn, json_lfn, doi, visible, useBoutiquesForm FROM "
                                                               + "VIPAppVersions "
                                                               + "WHERE application = ? "
                                                               + "ORDER BY version");
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            List<AppVersion> versions = new ArrayList<AppVersion>();

            while (rs.next()) {
                versions.add(new AppVersion(
                        name,
                        rs.getString("version"),
                        rs.getString("lfn"),
                        rs.getString("json_lfn"),
                        rs.getString("doi"),
                        rs.getBoolean("visible"),
                        rs.getBoolean("useBoutiquesForm")));
            }
            ps.close();
            return versions;

        } catch (SQLException ex) {
            logger.error("Error getting versions for application {}", name, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AppVersion> getAllVisibleVersions() throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                    + "application, version, lfn, json_lfn, doi, visible, useBoutiquesForm FROM "
                    + "VIPAppVersions "
                    + "WHERE visible = ? "
                    + "ORDER BY version");
            ps.setBoolean(1, true);

            ResultSet rs = ps.executeQuery();
            List<AppVersion> versions = new ArrayList<AppVersion>();

            while (rs.next()) {
                versions.add(new AppVersion(
                        rs.getString("application"),
                        rs.getString("version"),
                        rs.getString("lfn"),
                        rs.getString("json_lfn"),
                        rs.getString("doi"),
                        rs.getBoolean("visible"),
                        rs.getBoolean("useBoutiquesForm")));
            }
            ps.close();
            return versions;

        } catch (SQLException ex) {
            logger.error("Error getting all visible versions", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void addVersion(AppVersion version) throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO VIPAppVersions(application, version, lfn, json_lfn, visible, useBoutiquesForm) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");

            ps.setString(1, version.getApplicationName());
            ps.setString(2, version.getVersion());
            ps.setString(3, version.getLfn());
            ps.setString(4, version.getJsonLfn());
            ps.setBoolean(5, version.isVisible());
            ps.setBoolean(6, version.isBoutiquesForm());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("A version named \"" + version.getApplicationName() + "\" already exists.");
                throw new DAOException("A version named \"" + version.getApplicationName() + "\" already exists.", ex);
            } else {
                logger.error("Error adding version {} for {}",
                        version.getVersion(), version.getApplicationName(), ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void updateVersion(AppVersion version) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                                                               + "VIPAppVersions "
                                                               + "SET lfn=?, json_lfn=?, visible=?, useBoutiquesForm=? "
                                                               + "WHERE application=? AND version=?");

            ps.setString(1, version.getLfn());
            ps.setString(2, version.getJsonLfn());
            ps.setBoolean(3, version.isVisible());
            ps.setBoolean(4, version.isBoutiquesForm());
            ps.setString(5, version.getApplicationName());
            ps.setString(6, version.getVersion());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error adding version {} for {}",
                    version.getVersion(), version.getApplicationName(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateDoiForVersion(
            String doi, String applicationName, String version)
            throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE "
                    + "VIPAppVersions "
                    + "SET doi=? "
                    + "WHERE application=? AND version=?");

            ps.setString(1, doi);
            ps.setString(2, applicationName);
            ps.setString(3, version);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error updating doi {} for {}/{}",
                    doi, applicationName, version, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void removeVersion(String applicationName, String version)
            throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE "
                                                               + "FROM VIPAppVersions WHERE application=? AND version=?");

            ps.setString(1, applicationName);
            ps.setString(2, version);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error("Error removing version {}/{}", applicationName, version, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public AppVersion getVersion(String applicationName, String applicationVersion)
            throws DAOException {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT "
                                                               + "application, version, lfn, json_lfn, "
                                                               + "doi, visible, useBoutiquesForm "
                                                               + "FROM VIPAppVersions WHERE "
                                                               + "application = ? AND version = ?");
            ps.setString(1, applicationName);
            ps.setString(2, applicationVersion);

            ResultSet rs = ps.executeQuery();
            rs.next();

            AppVersion version = new AppVersion(rs.getString("application"),
                                                rs.getString("version"),
                                                rs.getString("lfn"),
                                                rs.getString("json_lfn"),
                                                rs.getString("doi"),
                                                rs.getBoolean("visible"),
                                                rs.getBoolean("useBoutiquesForm"));
            ps.close();

            return version;

        } catch (SQLException ex) {
            logger.error("Error getting versions for {}/{}",
                    applicationName, applicationVersion, ex);
            throw new DAOException(ex);
        }
    }

}
