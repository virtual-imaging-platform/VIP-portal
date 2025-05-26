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
        String query = "INSERT INTO VIPTags (tag_key, tag_value, type, application, version, visible, boutiques) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getKey());
            ps.setString(2, tag.getValue());
            ps.setString(3, tag.getType().toString());
            ps.setString(4, tag.getApplication());
            ps.setString(5, tag.getVersion());
            ps.setBoolean(6, tag.isVisible());
            ps.setBoolean(7, tag.isBoutiques());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation") || e.getMessage().contains("Duplicate entry ")) {
                logger.error("A tag named \"{}\" already exists for {} {}.", tag.toString(), tag.getApplication(), tag.getVersion());
                throw new DAOException("A tag named " + tag.toString() + " already exists for " + tag.getApplication() + tag.getVersion() + ".");
            } else {
                logger.error("Error adding tag " + tag.toString(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void update(Tag oldTag, Tag newTag) throws DAOException {
        String query = "UPDATE VIPTags SET tag_key = ?, tag_value = ?, type = ?, application = ?, version = ?, visible = ?, boutiques = ? "
        +              "WHERE tag_key = ? AND tag_value = ? AND application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, newTag.getKey());
            ps.setString(2, newTag.getValue());
            ps.setString(3, newTag.getType().toString());
            ps.setString(4, newTag.getApplication());
            ps.setString(5, newTag.getVersion());
            ps.setBoolean(6, newTag.isVisible());
            ps.setBoolean(7, newTag.isBoutiques());
            ps.setString(8, oldTag.getKey());
            ps.setString(9, oldTag.getValue());
            ps.setString(10, oldTag.getApplication());
            ps.setString(11, oldTag.getVersion());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry ")) {
                logger.error("A tag named \"{}\" already exists for {} {}.", newTag.toString(), newTag.getApplication(), newTag.getVersion());
                throw new DAOException("A tag named " + newTag.toString() + " already exists for " + newTag.getApplication() + newTag.getVersion() + ".");
            } else {
                logger.error("Error updating tag : " + oldTag.toString(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void remove(Tag tag) throws DAOException {
        String query = "DELETE FROM VIPTags WHERE tag_key = ? AND tag_value = ? AND application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getKey());
            ps.setString(2, tag.getValue());
            ps.setString(3, tag.getApplication());
            ps.setString(4, tag.getVersion());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error removing tag " + tag.toString(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public Tag get(String key, String value, String application, String version) throws DAOException {
        String query = "SELECT * FROM VIPTags WHERE tag_key = ? AND tag_value = ? AND application = ? AND version = ? ORDER BY tag_key";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, key);
            ps.setString(2, value);
            ps.setString(3, application);
            ps.setString(4, version);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return fromRs(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error retrieving tag " + key + ":" + value + "for " + application + version, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Tag> getAll() throws DAOException {
        String query = "SELECT DISTINCT * FROM VIPTags ORDER BY tag_key";

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
        +              "WHERE application = ? AND version = ? ORDER BY tag_key";

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
            rs.getString("tag_key"),
            rs.getString("tag_value"),
            Tag.ValueType.valueOf(rs.getString("type").toUpperCase()),
            rs.getString("application"),
            rs.getString("version"),
            rs.getBoolean("visible"),
            rs.getBoolean("boutiques")
        );
    }
}
