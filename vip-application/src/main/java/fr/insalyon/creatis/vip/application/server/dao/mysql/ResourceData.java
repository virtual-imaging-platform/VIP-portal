package fr.insalyon.creatis.vip.application.server.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.server.dao.ResourceDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import javax.sql.DataSource;

@Repository
@Transactional
public class ResourceData extends JdbcDaoSupport implements ResourceDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(Resource resource) throws DAOException {
        String query = "INSERT INTO VIPResources (name, visible, status, type, configuration) "
        +              "VALUES (?,?,?,?,?)";
        
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, resource.getName());
            ps.setBoolean(2, resource.isVisible());
            ps.setBoolean(3, resource.getStatus());
            ps.setString(4, resource.getType().toString());
            ps.setString(5, resource.getConfiguration());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation")) {
                logger.error("A resource named \"{}\" already exists.", resource.getName());
                throw new DAOException("A resource named " + resource.getName() + " already exists.");
            } else {
                logger.error("Error adding resource " + resource.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void update(Resource resource) throws DAOException {
        String query = "UPDATE VIPResources SET visible = ?, status = ?, type = ?, configuration = ? "
        +              "WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setBoolean(1, resource.isVisible());
            ps.setBoolean(2, resource.getStatus());
            ps.setString(3, resource.getType().toString());
            ps.setString(4, resource.getConfiguration());
            ps.setString(5, resource.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error updating resource : " + resource.getName(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public void remove(Resource resource) throws DAOException {
        String query = "DELETE FROM VIPResources WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, resource.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation") || e.getMessage().contains("Duplicate entry ")) {
                logger.error("There is no resource registered with the name {}", resource.getName());
                throw new DAOException("There is no resource registered with the name : " + resource.getName());
            } else {
                logger.error("Error removing resource " + resource.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public List<Resource> getAll() throws DAOException {
        String query = "SELECT * FROM VIPResources ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<Resource> results = new ArrayList<>();

            while (rs.next()) {
                results.add(resultsetToResource(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting all resources.", e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Resource> getByUser(User user) throws DAOException {
        String query = "SELECT * FROM VIPResources r "
        +              "JOIN VIPGroupResources gr ON r.name = gr.resourcename "
        +              "JOIN VIPUsersGroups ug ON gr.groupname = ug.groupname "
        +              "WHERE ug.email = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, user.getEmail());

            ResultSet rs = ps.executeQuery();
            List<Resource> results = new ArrayList<>();

            while (rs.next()) {
                results.add(resultsetToResource(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting resources for user " + user.getEmail(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Resource> getByEngine(Engine engine) throws DAOException {
        String query = "SELECT * FROM VIPResources r "
        +              "JOIN VIPResourceEngines re ON r.name = re.resourcename "
        +              "WHERE re.name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, engine.getName());

            ResultSet rs = ps.executeQuery();
            List<Resource> results = new ArrayList<>();

            while (rs.next()) {
                results.add(resultsetToResource(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting resources for engine " + engine.getName(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Resource> getByAppVersion(AppVersion appVersion) throws DAOException {
        String query = "SELECT * FROM VIPResources r "
        +              "JOIN VIPResourceAppVersions rav ON r.name = rav.resourcename "
        +              "WHERE rav.application = ? AND rav.version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, appVersion.getApplicationName());
            ps.setString(2, appVersion.getVersion());

            ResultSet rs = ps.executeQuery();
            List<Resource> results = new ArrayList<>();

            while (rs.next()) {
                results.add(resultsetToResource(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting resources for application " 
                + appVersion.getApplicationName()
                + " version " + appVersion.getVersion(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Resource> getByGroup(Group group) throws DAOException {
        String query = "SELECT * FROM VIPResources r "
        +              "JOIN VIPGroupResources gr ON r.name = gr.resourcename "
        +              "WHERE gr.groupname = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, group.getName());

            ResultSet rs = ps.executeQuery();
            List<Resource> results = new ArrayList<>();

            while (rs.next()) {
                results.add(resultsetToResource(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting resources for group " + group.getName(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public void putInGroup(Resource resource, Group group) throws DAOException {
        String query = "INSERT INTO VIPGroupResources (resourcename, groupname) "
        +              "VALUES (?,?)";
        
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, resource.getName());
            ps.setString(2, group.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation")) {
                logger.error("A resource name \"{}\" already exists in this group \"{}\"", resource.getName(), group.getName());
            } else {
                logger.error("Error adding resource " + resource.getName() + " to group " + group.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void removeFromGroup(Resource resource, Group group) throws DAOException {
        String query = "DELETE FROM VIPGroupResources WHERE resourcename = ? AND groupname = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, resource.getName());
            ps.setString(2, group.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation") || e.getMessage().contains("Duplicate entry ")) {
                logger.error("There is no resource registered with the name {} and the group {}", resource.getName(), group.getName());
                throw new DAOException("There is no resource registered with the name " + resource.getName() + " and the group " + group.getName());
            } else {
                logger.error("Error removing resource/group pair " + resource.getName() + "/" + group.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    private Resource resultsetToResource(ResultSet rs) throws SQLException {
        return new Resource(
            rs.getString("name"), 
            rs.getBoolean("visible"),
            rs.getBoolean("status"), 
            rs.getString("type"),
            rs.getString("configuration"));
    }
}
