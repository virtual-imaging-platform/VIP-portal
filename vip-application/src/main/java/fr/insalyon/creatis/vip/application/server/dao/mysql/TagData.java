package fr.insalyon.creatis.vip.application.server.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.server.dao.TagDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Repository
@Transactional
public class TagData extends JdbcDaoSupport implements TagDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(Tag tag) throws DAOException {
        String query = "INSERT INTO VIPTags (name, application, version, visible, boutiques) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getName());
            ps.setString(2, tag.getApplication());
            ps.setString(3, tag.getVersion());
            ps.setBoolean(4, tag.isVisible());
            ps.setBoolean(5, tag.isBoutiques());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation") || e.getMessage().contains("Duplicate entry ")) {
                logger.error("A tag named \"{}\" already exists for {} {}.", tag.getName(), tag.getApplication(), tag.getVersion());
                throw new DAOException("A tag named " + tag.getName() + " already exists for " + tag.getApplication() + tag.getVersion() + ".");
            } else {
                logger.error("Error adding tag " + tag.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void update(Tag oldTag, Tag newTag) throws DAOException {
        String query = "UPDATE VIPTags SET name = ?, application = ?, version = ?, visible = ?, boutiques = ? "
        +              "WHERE name = ? AND application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, newTag.getName());
            ps.setString(2, newTag.getApplication());
            ps.setString(3, newTag.getVersion());
            ps.setBoolean(4, newTag.isVisible());
            ps.setBoolean(5, newTag.isBoutiques());
            ps.setString(6, oldTag.getName());
            ps.setString(7, oldTag.getApplication());
            ps.setString(8, oldTag.getVersion());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry ")) {
                logger.error("A tag named \"{}\" already exists for {} {}.", newTag.getName(), newTag.getApplication(), newTag.getVersion());
                throw new DAOException("A tag named " + newTag.getName() + " already exists for " + newTag.getApplication() + newTag.getVersion() + ".");
            } else {
                logger.error("Error updating tag : " + oldTag.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void remove(Tag tag) throws DAOException {
        String query = "DELETE FROM VIPTags WHERE name = ? AND application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getName());
            ps.setString(2, tag.getApplication());
            ps.setString(3, tag.getVersion());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error removing tag " + tag.getName(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public Tag get(String name, String application, String version) throws DAOException {
        String query = "SELECT * FROM VIPTags WHERE name = ? AND application = ? AND version = ? ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, application);
            ps.setString(3, version);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return fromRs(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error retrieving tag " + name + "for " + application + version, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Tag> getAll() throws DAOException {
        String query = "SELECT DISTINCT * FROM VIPTags ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<Tag> results = new ArrayList<>();

            while (rs.next()) {
                results.add(fromRs(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting all tags.", e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Tag> getTags(AppVersion appVersion) throws DAOException {
        String query = "SELECT * FROM VIPTags "
        +              "WHERE application = ? AND version = ? ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, appVersion.getApplicationName());
            ps.setString(2, appVersion.getVersion());

            ResultSet rs = ps.executeQuery();
            List<Tag> results = new ArrayList<>();

            while (rs.next()) {
                results.add(fromRs(rs));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting tags for application " 
                + appVersion.getApplicationName()
                + " version " + appVersion.getVersion(), e);
            throw new DAOException(e);
        }
    }

    private Tag fromRs(ResultSet rs) throws SQLException {
        return new Tag(
            rs.getString("name"),
            rs.getString("application"),
            rs.getString("version"),
            rs.getBoolean("visible"),
            rs.getBoolean("boutiques")
        );
    }
}
