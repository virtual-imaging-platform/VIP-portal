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
        String query = "INSERT INTO VIPTags (name) VALUES (?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation") || e.getMessage().contains("Duplicate entry ")) {
                logger.error("A tag named \"{}\" already exists.", tag.getName());
                throw new DAOException("A tag named " + tag.getName() + " already exists.");
            } else {
                logger.error("Error adding tag " + tag.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void update(Tag tag, String newName) throws DAOException {
        String query = "UPDATE VIPTags SET name = ? WHERE name = ? ";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, newName);
            ps.setString(2, tag.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry ")) {
                logger.error("Error updating tag name, value already exists !", e);
                throw new DAOException("A tag named " + newName + " already exists.");
            } else {
                logger.error("Error updating tag : " + tag.getName(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void remove(Tag tag) throws DAOException {
        String query = "DELETE FROM VIPTags WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error removing tag " + tag.getName(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Tag> getAll() throws DAOException {
        String query = "SELECT * FROM VIPTags ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<Tag> results = new ArrayList<>();

            while (rs.next()) {
                results.add(new Tag(rs.getString("name")));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting all tags.", e);
            throw new DAOException(e);
        }
    }

    @Override
    public void associate(Tag tag, AppVersion appVersion) throws DAOException {
        String query = "INSERT INTO VIPTagsAppVersions (tagname, application, version) "
        +              "VALUES (?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getName());
            ps.setString(2, appVersion.getApplicationName());
            ps.setString(3, appVersion.getVersion());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Unique index or primary key violation") || e.getMessage().contains("Duplicate entry ")) {
                logger.error("The tag name \"{}\" is already associated to \"{}\"", tag.getName(), appVersion.getApplicationName() + " " + appVersion.getVersion());
            } else {
                logger.error("Error adding tag " + tag.getName() + " to appversion " + appVersion.getApplicationName() + " " + appVersion.getVersion(), e);
                throw new DAOException(e);
            }
        }
    }

    @Override
    public void dissociate(Tag tag, AppVersion appVersion) throws DAOException {
        String query = "DELETE FROM VIPTagsAppVersions WHERE tagname = ? AND application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getName());
            ps.setString(2, appVersion.getApplicationName());
            ps.setString(3, appVersion.getVersion());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error removing tag/appversion pair " + tag.getName() + "/" + appVersion.getApplicationName() + " " + appVersion.getVersion(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Tag> getTags(AppVersion appVersion) throws DAOException {
        String query = "SELECT * FROM VIPTagsAppVersions "
        +              "WHERE application = ? AND version = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, appVersion.getApplicationName());
            ps.setString(2, appVersion.getVersion());

            ResultSet rs = ps.executeQuery();
            List<Tag> results = new ArrayList<>();

            while (rs.next()) {
                results.add(new Tag(rs.getString("tagname")));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting tags for application " 
                + appVersion.getApplicationName()
                + " version " + appVersion.getVersion(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<AppVersion> getAssociated(Tag tag) throws DAOException {
        String query = "SELECT * FROM VIPAppVersions av "
        +              "JOIN VIPTagsAppVersions tav ON av.application = tav.application "
        +              "AND av.version = tav.version "
        +              "WHERE tav.tagname = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, tag.getName());

            ResultSet rs = ps.executeQuery();
            List<AppVersion> results = new ArrayList<>();

            while (rs.next()) {
                results.add(new AppVersion(
                    rs.getString("application"),
                    rs.getString("version"),
                    rs.getString("lfn"),
                    rs.getString("json_lfn"),
                    rs.getString("doi"),
                    rs.getBoolean("visible"),
                    rs.getBoolean("useBoutiquesForm")));
            }
            return results;

        } catch (SQLException e) {
            logger.error("Error getting applications for tag " 
                + tag.getName(), e);
            throw new DAOException(e);
        }
    }
}
