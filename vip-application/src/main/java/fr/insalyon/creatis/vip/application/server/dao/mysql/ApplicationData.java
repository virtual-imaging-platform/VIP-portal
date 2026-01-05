package fr.insalyon.creatis.vip.application.server.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;

@Repository
@Transactional
public class ApplicationData extends JdbcDaoSupport implements ApplicationDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserDAO userDAO;

    @Autowired
    public ApplicationData(DataSource dataSource, UserDAO userDAO) {
        setDataSource(dataSource);
        this.userDAO = userDAO;
    }

    @Override
    public void add(Application application) throws DAOException {
        String query = "INSERT INTO VIPApplications(name, citation, owner) VALUES (?,?,?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, application.getName());
            ps.setString(2, application.getCitation());
            ps.setString(3, application.getOwner());
            ps.execute();

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
        String query = "UPDATE VIPApplications SET citation=?, owner=? WHERE name=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, application.getCitation());
            ps.setString(2, application.getOwner());
            ps.setString(3, application.getName());
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error updating application {}", application.getName(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remove(String name) throws DAOException {
        String query = "DELETE FROM VIPApplications WHERE name=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, name);
            ps.execute();

        } catch (SQLException ex) {
            logger.error("Error removing application {}", name, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public Application getApplication(String applicationName) throws DAOException {
        String query = "SELECT * FROM VIPApplications WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, applicationName);

            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return applicationFromResultset(rs);
            }
            return null;
        } catch (SQLException ex) {
            logger.error("Error getting application {}", applicationName, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Application> getApplications() throws DAOException {
        String query = "SELECT * FROM VIPApplications ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<Application> applications = new ArrayList<Application>();

            while (rs.next()) {
                applications.add(applicationFromResultset(rs));
            }
            return applications;

        } catch (SQLException ex) {
            logger.error("Error getting all applications", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Application> getApplicationsWithOwner(String owner) throws DAOException {
        String query = "SELECT * FROM VIPApplications WHERE owner = ? ORDER BY name ";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, owner);
            ResultSet rs = ps.executeQuery();
            List<Application> applications = new ArrayList<Application>();

            while (rs.next()) {
                String appOwner = rs.getString("owner");
                User user = userDAO.getUser(appOwner);

                applications.add(new Application(
                    rs.getString("name"), 
                    rs.getString("owner"), 
                    user.getFirstName() + " " + user.getLastName(), 
                    rs.getString("citation")));
            }
            return applications;

        } catch (SQLException ex) {
            logger.error("Error getting all applications", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Application> getApplicationsByGroup(Group group) throws DAOException {
        String query =  "SELECT * FROM VIPApplications a "
        +               "JOIN VIPGroupsApplications ga ON ga.applicationname = a.name "
        +               "WHERE ga.groupname = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, group.getName());

            ResultSet rs = ps.executeQuery();
            List<Application> results = new ArrayList<>();

            while (rs.next()) {
                results.add(applicationFromResultset(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting applications for group " + group.getName(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public String getCitation(String name) throws DAOException {
        String query = "SELECT citation FROM VIPApplications WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("citation");
            } else {
                return null;
            }

        } catch (SQLException ex) {
            logger.error("Error getting citation for application {}", name, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AppVersion> getVersions(String name) throws DAOException {
        String query = "SELECT * FROM VIPAppVersions WHERE application = ? ORDER BY version";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            List<AppVersion> versions = new ArrayList<AppVersion>();

            while (rs.next()) {
                versions.add(appVersionFromResultset(rs));
            }
            return versions;

        } catch (SQLException ex) {
            logger.error("Error getting versions for application {}", name, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<AppVersion> getAllVisibleVersions() throws DAOException {
        String query = "SELECT * FROM VIPAppVersions WHERE visible = ? ORDER BY version";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setBoolean(1, true);

            ResultSet rs = ps.executeQuery();
            List<AppVersion> versions = new ArrayList<AppVersion>();

            while (rs.next()) {
                versions.add(appVersionFromResultset(rs));
            }
            return versions;

        } catch (SQLException ex) {
            logger.error("Error getting all visible versions", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void addVersion(AppVersion version) throws DAOException {
        String query =  "INSERT INTO VIPAppVersions(application, version, descriptor, visible, settings, source) "
        +               "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, version.getApplicationName());
            ps.setString(2, version.getVersion());
            ps.setString(3, version.getDescriptor());
            ps.setBoolean(4, version.isVisible());
            ps.setString(5, mapToSettingsJson(version.getSettings()));
            ps.setString(6, version.getSource());
            ps.executeUpdate();

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
        String query =  "UPDATE VIPAppVersions SET descriptor=?, visible=?, settings=?, source=? "
        +               "WHERE application=? AND version=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, version.getDescriptor());
            ps.setBoolean(2, version.isVisible());
            ps.setString(3, mapToSettingsJson(version.getSettings()));
            ps.setString(4, version.getSource());
            ps.setString(5, version.getApplicationName());
            ps.setString(6, version.getVersion());
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error adding version {} for {}",
                    version.getVersion(), version.getApplicationName(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateDoiForVersion(String doi, String applicationName, String version) throws DAOException {
        String query = "UPDATE VIPAppVersions SET doi=? WHERE application=? AND version=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, doi);
            ps.setString(2, applicationName);
            ps.setString(3, version);
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error updating doi {} for {}/{}",
                    doi, applicationName, version, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void removeVersion(String applicationName, String version) throws DAOException {
        String query = "DELETE FROM VIPAppVersions WHERE application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, applicationName);
            ps.setString(2, version);
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error removing version {}/{}", applicationName, version, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public AppVersion getVersion(String applicationName, String applicationVersion) throws DAOException {
        String query = "SELECT * FROM VIPAppVersions WHERE application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, applicationName);
            ps.setString(2, applicationVersion);

            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return appVersionFromResultset(rs);
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
        String query = "INSERT INTO VIPGroupsApplications (applicationname, groupname) "
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
        String query = "DELETE FROM VIPGroupsApplications WHERE applicationname = ? AND groupname = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, app.getName());
            ps.setString(2, group.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error removing app/group pair " + app.getName() + "/" + group.getName(), e);
            throw new DAOException(e);
        }
    }

    private Application applicationFromResultset(ResultSet rs) throws SQLException {
        return new Application(
            rs.getString("name"),
            rs.getString("owner"),
            rs.getString("citation"));
    }

    private AppVersion appVersionFromResultset(ResultSet rs) throws SQLException, DAOException {
        return new AppVersion(
            rs.getString("application"),
            rs.getString("version"),
            rs.getString("descriptor"),
            rs.getString("doi"),
            settingsJsonToMap(rs.getString("settings")),
            rs.getBoolean("visible"),
            rs.getString("source")
        );
    }

    private Map<String, String> settingsJsonToMap(String json) throws DAOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            if (json == null) {
                return new HashMap<>();
            } else {
                return mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            }

        } catch (JsonProcessingException e) {
            logger.error("Failed to convert JSON settings to map!", e);
            throw new DAOException(e);
        }
    }

    private String mapToSettingsJson(Map<String, String> map) throws DAOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert JSON settings to map!", e);
            throw new DAOException(e);
        }
    }
}
