package fr.insalyon.creatis.vip.datamanager.server.dao.mysql;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.bean.UserApiKey;
import fr.insalyon.creatis.vip.datamanager.server.dao.ApiKeysDAO;
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
import java.util.LinkedList;
import java.util.List;

@Repository
@Transactional
public class ApiKeysData extends JdbcDaoSupport implements ApiKeysDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void addKey(UserApiKey apiKey)
        throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                 "insert into VIPApiKeys(email, identifier, apiKey) "
                 + "values(?, ?, ?)")) {
            ps.setString(1, apiKey.getUserEmail());
            ps.setString(2, apiKey.getStorageIdentifier());
            ps.setString(3, apiKey.getApiKey());

            ps.execute();
        } catch (SQLException e) {
            logger.error("Error inserting api key: {}", apiKey, e);
            throw new DAOException(e);
        }
    }

    @Override
    public void updateKey(UserApiKey apiKey)
        throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                 "update VIPApiKeys set apiKey = ? "
                 + "where email = ? and identifier = ?")) {
            ps.setString(1, apiKey.getApiKey());
            ps.setString(2, apiKey.getUserEmail());
            ps.setString(3, apiKey.getStorageIdentifier());

            ps.execute();
        } catch (SQLException e) {
            logger.error("Error updating api key: {}", apiKey, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<UserApiKey> getByUser(String userEmail) throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                 "select * from VIPApiKeys " +
                 "where email=?")) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();

            List<UserApiKey> apiKeys = new LinkedList<>();
            while (rs.next()) {
                UserApiKey apiKey = new UserApiKey(
                    rs.getString("identifier"),
                    rs.getString("email"),
                    rs.getString("apiKey"));
                apiKeys.add(apiKey);
            }

            return apiKeys;
        } catch (SQLException e) {
            logger.error(
                "Error getting api keys for user: {}", userEmail, e);
            throw new DAOException(e);
        }
    }

    @Override
    public void deleteKeyFor(String userEmail, String storageIdentifier)
        throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                 "delete from VIPApiKeys "
                 + "where email = ? and identifier = ?")) {
            ps.setString(1, userEmail);
            ps.setString(2, storageIdentifier);

            int nbRows = ps.executeUpdate();
            if (nbRows != 1) {
                logger.error("Error deleting api key for user {} and storage {} : {} rows deleted",
                        userEmail, storageIdentifier, nbRows);
                throw new DAOException(
                    "Number of deleted rows ("
                    + nbRows
                    + ") not equal to 1, for params: "
                    + "userEmail=" + userEmail
                    + ", storageIdentifier=" + storageIdentifier);
            }
        } catch (SQLException e) {
            logger.error(
                "Error deleting api key for user {} and storage {}",
                    userEmail, storageIdentifier, e);
            throw new DAOException(e);
        }
    }
}
