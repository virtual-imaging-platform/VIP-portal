package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.ExecutionPublicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@Transactional
public class ExecutionDataPublic extends JdbcDaoSupport implements ExecutionPublicDAO {

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
}

