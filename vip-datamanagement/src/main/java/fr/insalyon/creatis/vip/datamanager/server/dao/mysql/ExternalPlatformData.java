package fr.insalyon.creatis.vip.datamanager.server.dao.mysql;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.models.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.dao.ExternalPlatformsDAO;
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

@Repository
@Transactional
public class ExternalPlatformData extends JdbcDaoSupport implements ExternalPlatformsDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public ExternalPlatform getById(String identifier) throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT * FROM VIPExternalPlatforms " +
                    "WHERE identifier=?")) {
            ps.setString(1, identifier);
            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                ExternalPlatform externalPlatform = new ExternalPlatform();
                externalPlatform.setIdentifier(rs.getString("identifier"));
                externalPlatform.setType(
                        getExternalPlatformTypeFromBDDString(rs.getString("type")));
                externalPlatform.setUrl(rs.getString("url"));
                externalPlatform.setDescription(rs.getString("description"));
                externalPlatform.setUploadUrl(rs.getString("upload_url"));
                externalPlatform.setKeycloakClientId(rs.getString("keycloak_client_id"));
                externalPlatform.setRefreshTokenUrl(rs.getString("refresh_token_url"));
                return externalPlatform;
            }

            logger.error("Cannot find external plaform {" + identifier + "}");
            throw new DAOException("Cannot find an external platform");

        } catch (SQLException e) {
            logger.error("Error getting external platform {} ", identifier, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<ExternalPlatform> getAll() throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT * FROM VIPExternalPlatforms ")) {

            ResultSet rs = ps.executeQuery();
            List<ExternalPlatform> externalPlatformsList = new ArrayList<>();

            while (rs.next()) {
                ExternalPlatform externalPlatform = new ExternalPlatform();
                externalPlatform.setIdentifier(rs.getString("identifier"));
                externalPlatform.setType(
                        getExternalPlatformTypeFromBDDString(rs.getString("type")));
                externalPlatform.setUrl(rs.getString("url"));
                externalPlatform.setDescription(rs.getString("description"));
                externalPlatform.setUploadUrl(rs.getString("upload_url"));
                externalPlatform.setKeycloakClientId(rs.getString("keycloak_client_id"));
                externalPlatform.setRefreshTokenUrl(rs.getString("refresh_token_url"));
                externalPlatformsList.add(externalPlatform);
            }

            return externalPlatformsList;

        } catch (SQLException e) {
            logger.error("Error getting all external platforms", e);
            throw new DAOException(e);
        }
    }

    private ExternalPlatform.Type getExternalPlatformTypeFromBDDString(String bddString) throws DAOException {
        try {
            return ExternalPlatform.Type.valueOf(bddString.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("External platform type not found {}", bddString, e);
            throw new DAOException(e);
        }
    }

}
