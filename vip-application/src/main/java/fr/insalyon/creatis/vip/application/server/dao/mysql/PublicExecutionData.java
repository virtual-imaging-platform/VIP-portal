package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.application.server.dao.PublicExecutionDAO;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
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
    public PublicExecution get(String publicExecutionId) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT execution_ID, simulation_name, application_name," +
                            " application_version, status, author, commentsFROM VIPExecutionPublic" +
                            " FROM FROM VIPExecutionPublic WHERE execution_ID=?");

            ps.setString(1, publicExecutionId);
            ResultSet rs = ps.executeQuery();
            PublicExecution publicExecution = null;

            if (rs.next()) {
                publicExecution = new PublicExecution(
                        rs.getString("execution_ID"),
                        rs.getString("simulation_name"),
                        rs.getString("application_name"),
                        rs.getString("application_version"),
                        PublicExecution.PublicExecutionStatus.valueOf(rs.getString("status")),
                        rs.getString("author"),
                        rs.getString("comments"));
            }

            rs.close();
            ps.close();
            return publicExecution;

        } catch (SQLException ex) {
            logger.error("Error getting public execution {}", publicExecutionId, ex);
            throw new DAOException("Error getting a public execution", ex);
        }
    }

    @Override
    public void add(PublicExecution publicExecution) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO VIPExecutionPublic(execution_ID, simulation_name, application_name," +
                            " application_version, status, author, comments)" +
                            " VALUES(?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, publicExecution.getId());
            ps.setString(2, publicExecution.getSimulationName());
            ps.setString(3, publicExecution.getApplicationName());
            ps.setString(4, publicExecution.getApplicationVersion());
            ps.setString(5, publicExecution.getStatus().name());
            ps.setString(6, publicExecution.getAuthor());
            ps.setString(7, publicExecution.getComments());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("Error creating public execution {} : it already exists", publicExecution.getId());
                throw new DAOException("Error creating public execution " + publicExecution.getId() + " : it already exists", ex);
            } else {
                logger.error("Error adding public execution {}", publicExecution.getId(), ex);
                throw new DAOException(ex);
            }
        }

    }

    @Override
    public void update(String executionId, PublicExecution.PublicExecutionStatus newStatus) throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "UPDATE VIPExecutionPublic SET status = ? WHERE execution_ID = ?")) {
            ps.setString(1, newStatus.name());
            ps.setString(2, executionId);
            ps.execute();

        } catch (SQLException ex) {
            logger.error("Error updating public execution {}", executionId, ex);
            throw new DAOException("Error updating execution status", ex);
        }
    }

    @Override
    public List<PublicExecution> getExecutions() throws DAOException {
        try {
            List<PublicExecution> publicExecutions = new ArrayList<>();
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM VIPExecutionPublic");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                publicExecutions.add(new PublicExecution(
                        rs.getString("execution_ID"),
                        rs.getString("simulation_name"),
                        rs.getString("application_name"),
                        rs.getString("application_version"),
                        PublicExecution.PublicExecutionStatus.valueOf(rs.getString("status")),
                        rs.getString("author"),
                        rs.getString("comments")
                ));
            }
            rs.close();
            ps.close();
            return publicExecutions;
        } catch (SQLException ex) {
            logger.error("Error getting all public executions", ex);
            throw new DAOException("Error getting all public executions", ex);
        }
    }

    @Override
    public boolean doesExecutionExist(String executionId) throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "SELECT COUNT(*) FROM VIPExecutionPublic WHERE execution_ID = ?")) {
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
}

