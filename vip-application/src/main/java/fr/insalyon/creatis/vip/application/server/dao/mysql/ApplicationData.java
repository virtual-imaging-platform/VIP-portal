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

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
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
 * @author Rafael Ferreira da Silva
 */
@Repository
@Transactional
public class ApplicationData extends JdbcDaoSupport implements ApplicationDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ApplicationData(DataSource dataSource) {
        setDataSource(dataSource);
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

            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
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
    public List<Application> getApplications() throws DAOException {
        return this.getApplications((String) null);

    }

    @Override
    public List<Application> getApplicationsWithOwner(String owner) throws DAOException {
        return this.getApplications(owner);
    }

    private List<Application> getApplications(String owner) throws DAOException {

        try {
            String requestSql = null;
            if (owner != null) {
                requestSql = "SELECT "
                        + "name, owner, citation FROM "
                        + "VIPApplications WHERE owner=? ORDER BY name";
            } else {
                requestSql = "SELECT "
                        + "name, owner, citation FROM "
                        + "VIPApplications ORDER BY name";
            }

            PreparedStatement ps = getConnection().prepareStatement(requestSql);
            if (owner != null) {
                ps.setString(1, owner);
            }
            ResultSet rs = ps.executeQuery();
            List<Application> applications = new ArrayList<Application>();

            while (rs.next()) {

                String appOwner = rs.getString("owner");
                PreparedStatement ps3 = getConnection().prepareStatement("SELECT "
                        + "first_name,last_name FROM VIPUsers WHERE email=?");
                ps3.setString(1, appOwner);
                ResultSet rs3 = ps3.executeQuery();
                String firstName = null;
                String lastName = null;
                while (rs3.next()) {
                    firstName = rs3.getString("first_name");
                    lastName = rs3.getString("last_name");
                }
                ps3.close();

                String name = rs.getString("name");

                applications.add(new Application(name, rs.getString("owner"), firstName + " " + lastName, rs.getString("citation")));
            }
            ps.close();
            return applications;

        } catch (SQLException ex) {
            logger.error("Error getting all applications", ex);
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
                return new Application(rs.getString("name"), rs.getString("owner"), rs.getString("citation"));
            }
            return null;
        } catch (SQLException ex) {
            logger.error("Error getting application {}", applicationName, ex);
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
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
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
            if (rs.first()) {
                AppVersion version = new AppVersion(rs.getString("application"),
                        rs.getString("version"),
                        rs.getString("lfn"),
                        rs.getString("json_lfn"),
                        rs.getString("doi"),
                        rs.getBoolean("visible"),
                        rs.getBoolean("useBoutiquesForm"));
                ps.close();

                return version;
            }
            return null;
        } catch (SQLException ex) {
            logger.error("Error getting versions for {}/{}",
                    applicationName, applicationVersion, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void associate(Application app, Group group) throws DAOException {
        String query = "INSERT INTO VIPGroupsApplication (applicationame, groupname) "
        +              "VALUES (?,?)";
        
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, app.getName());
            ps.setString(2, group.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation")) {
                logger.error("A application name \"{}\" already exists in this group \"{}\"", app.getName(), group.getName());
            } else {
                logger.error("Error adding application " + app.getName() + " to group " + group.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void dissociate(Application app, Group group) throws DAOException {
        String query = "DELETE FROM VIPGroupsApplication WHERE applicationame = ? AND groupname = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, app.getName());
            ps.setString(2, group.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error removing app/group pair " + app.getName() + "/" + group.getName(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Application> getApplicationByGroup(Group group) throws DAOException {
        String query =  "SELECT * FROM VIPApplications a "
        +               "JOIN VIPGroupsApplications ga ON ga.applicationame = a.name "
        +               "WHERE ga.groupname = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, group.getName());

            ResultSet rs = ps.executeQuery();
            List<Application> results = new ArrayList<>();

            while (rs.next()) {
                results.add(new Application(
                    rs.getString("name"),
                    rs.getString("owner"),
                    rs.getString("fullname"),
                    rs.getString("citation")));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting applications for group " + group.getName(), e);
            throw new DAOException(e);
        }
    }
}
