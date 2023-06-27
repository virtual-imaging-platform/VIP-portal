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
                    "INSERT INTO ExecutionPublic(execution_name, version, status, author, comments) VALUES(?, ?, ?, ?, ?)");
            ps.setString(1, execution.getName());
            ps.setString(2, execution.getVersion());
            ps.setString(3, execution.getStatus());
            ps.setString(4, execution.getAuthor());
            ps.setString(5, execution.getComments());
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
    public void update(Execution execution) throws DAOException {
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "UPDATE ExecutionPublic SET execution_name = ?, version = ?, status = ?, author = ?, comments = ? WHERE id = ?");
            ps.setString(1, execution.getName());
            ps.setString(2, execution.getVersion());
            ps.setString(3, execution.getStatus());
            ps.setString(4, execution.getAuthor());
            ps.setString(5, execution.getComments());
            ps.setInt(6, execution.getId());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            throw new DAOException("Error updating an execution", ex);
        }
    }

    @Override
    public List<Execution> getExecutions() throws DAOException {
        try {
            List<Execution> executions = new ArrayList<>();
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM ExecutionPublic");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                executions.add(new Execution(
                        rs.getString("execution_name"),
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
}

