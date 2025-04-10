package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.server.dao.PublicExecutionDAO;
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

@Repository
@Transactional
public class PublicExecutionData extends JdbcDaoSupport implements PublicExecutionDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public PublicExecution get(String experienceName) throws DAOException {
        String query = "SELECT * FROM VIPPublicExecutions WHERE experience_name=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, experienceName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return fromRs(rs);
            } else {
                return null;
            }

        } catch (SQLException ex) {
            logger.error("Error getting public execution {}", experienceName, ex);
            throw new DAOException("Error getting a public execution", ex);
        }
    }

    @Override
    public void add(PublicExecution publicExecution) throws DAOException {
        String query =  "INSERT INTO VIPPublicExecutions(experience_name, workflows_ids, "
        +               "applications_names, applications_versions, status, author, output_ids, comments, doi) "
        +               "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, publicExecution.getExperienceName());
            ps.setString(2, String.join(PublicExecution.SEPARATOR, publicExecution.getWorkflowsIds()));
            ps.setString(3, String.join(PublicExecution.SEPARATOR, publicExecution.getApplicationsNames()));
            ps.setString(4, String.join(PublicExecution.SEPARATOR, publicExecution.getApplicationsVersions()));
            ps.setString(5, publicExecution.getStatus().name());
            ps.setString(6, publicExecution.getAuthor());
            ps.setString(7, String.join(PublicExecution.SEPARATOR, publicExecution.getOutputIds()));
            ps.setString(8, publicExecution.getComments());
            ps.setString(9, publicExecution.getDoi());
            ps.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("Error creating public execution {} : it already exists", publicExecution.getExperienceName());
                throw new DAOException("Error creating public execution " + publicExecution.getExperienceName() + " : it already exists", ex);
            } else {
                logger.error("Error adding public execution {}", publicExecution.getExperienceName(), ex);
                throw new DAOException(ex);
            }
        }

    }

    @Override
    public void update(String experienceName, PublicExecution publicExecution) throws DAOException {
        String query =  "UPDATE VIPPublicExecutions SET workflows_ids = ?, applications_names = ?, "
        +               "applications_versions = ?, status = ?, author = ?, output_ids = ?, comments = ?, doi = ? "
        +               "WHERE experience_name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, String.join(PublicExecution.SEPARATOR, publicExecution.getWorkflowsIds()));
            ps.setString(2, String.join(PublicExecution.SEPARATOR, publicExecution.getApplicationsNames()));
            ps.setString(3, String.join(PublicExecution.SEPARATOR, publicExecution.getApplicationsVersions()));
            ps.setString(4, publicExecution.getStatus().name());
            ps.setString(5, publicExecution.getAuthor());
            ps.setString(6, String.join(PublicExecution.SEPARATOR, publicExecution.getOutputIds()));
            ps.setString(7, publicExecution.getComments());
            ps.setString(8, publicExecution.getDoi());
            ps.setString(9, publicExecution.getExperienceName());
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error updating public execution {}", experienceName, ex);
            throw new DAOException("Error updating public execution", ex);
        }
    }

    @Override
    public List<PublicExecution> getAll() throws DAOException {
        String query = "SELECT * FROM VIPPublicExecutions ORDER BY experience_name";
        List<PublicExecution> publicExecutions = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                publicExecutions.add(fromRs(rs));
            }
            return publicExecutions;

        } catch (SQLException ex) {
            logger.error("Error getting all public executions", ex);
            throw new DAOException("Error getting all public executions", ex);
        }
    }

    @Override
    public boolean exist(String executionId) throws DAOException {
        String query = "SELECT COUNT(*) FROM VIPPublicExecutions WHERE experience_name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, executionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException ex) {
            logger.error("Error checking if public execution {} exists", executionId, ex);
            throw new DAOException("Error checking if execution exists", ex);
        }
    }

    private PublicExecution fromRs(ResultSet set) throws SQLException {
        return new PublicExecution(
            set.getString("experience_name"),
            set.getString("workflows_ids"),
            set.getString("applications_names"),
            set.getString("applications_versions"),
            PublicExecution.PublicExecutionStatus.valueOf(set.getString("status")),
            set.getString("author"),
            set.getString("comments"),
            set.getString("output_ids"),
            set.getString("doi"));
    }
}
