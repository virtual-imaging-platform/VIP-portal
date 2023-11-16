package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.ExecutionPublicDAO;
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
public class ExecutionDataPublic extends JdbcDaoSupport implements ExecutionPublicDAO {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }
    @Override
    public void add(Execution execution) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "INSERT INTO VIPExecutionPublic(execution_ID, simulation_name, application_name, version, status, author, comments) VALUES(?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, execution.getId());
            ps.setString(2, execution.getSimulationName());
            ps.setString(3, execution.getApplicationName());
            ps.setString(4, execution.getVersion());
            ps.setString(5, execution.getStatus());
            ps.setString(6, execution.getAuthor());
            ps.setString(7, execution.getComments());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                throw new DAOException("Error creating an execution", ex);
            } else {
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void update(String executionId, String newStatus) throws DAOException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                "UPDATE VIPExecutionPublic SET status = ? WHERE execution_ID = ?")) {
            ps.setString(1, newStatus);
            ps.setString(2, executionId);
            ps.execute();

        } catch (SQLException ex) {
            throw new DAOException("Error updating execution status", ex);
        }
    }

    @Override
    public List<Execution> getExecutions() throws DAOException {
        try {
            List<Execution> executions = new ArrayList<>();
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM VIPExecutionPublic");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                executions.add(new Execution(
                        rs.getString("execution_ID"),
                        rs.getString("simulation_name"),
                        rs.getString("application_name"),
                        rs.getString("version"),
                        rs.getString("status"),
                        rs.getString("author"),
                        rs.getString("comments")
                ));
                logger.info(executions.toString());
            }
            rs.close();
            ps.close();
            return executions;
        } catch (SQLException ex) {
            throw new DAOException("Error getting executions", ex);
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
            throw new DAOException("Error checking if execution exists", ex);
        }
    }
}

