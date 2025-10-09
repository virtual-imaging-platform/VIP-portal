package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.server.dao.EngineDAO;
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
public class EngineData extends JdbcDaoSupport implements EngineDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(Engine engine) throws DAOException {
        String query = "INSERT INTO VIPEngines(name, endpoint, status) VALUES (?,?,?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, engine.getName());
            ps.setString(2, engine.getEndpoint());
            ps.setString(3, engine.getStatus());
            ps.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                logger.error("An engine named \"{}\" already exists.", engine.getName());
                throw new DAOException("An engine named \"" + engine.getName() + "\" already exists.");
            } else {
                logger.error("Error adding engine {}", engine.getEndpoint(), ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void update(Engine engine) throws DAOException {
        String query = "UPDATE VIPEngines SET endpoint = ?, status = ? WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, engine.getEndpoint());
            ps.setString(2, engine.getStatus());
            ps.setString(3, engine.getName());
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error updating engine {} to {}", engine.getName(), engine.getEndpoint(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remove(String name) throws DAOException {
        String query = "DELETE FROM VIPEngines WHERE name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, name);
            ps.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                logger.error("There is no engine registered with the name {}", name);
                throw new DAOException("There is no engine registered with the name : " + name);
            } else {
                logger.error("Error removing engine {}", name, ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public List<Engine> get() throws DAOException {
        String query = "SELECT name, endpoint, status FROM VIPEngines ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            List<Engine> list = new ArrayList<Engine>();

            while (rs.next()) {
                list.add(new Engine(rs.getString("name"), rs.getString("endpoint"), rs.getString("status")));
            }
            return list;

        } catch (SQLException ex) {
            logger.error("Error getting all engines", ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Engine> getByResource(Resource resource) throws DAOException {
        String query =  "SELECT * FROM VIPEngines e "
        +               "JOIN VIPResourcesEngines re ON e.name = re.enginename "
        +               "WHERE re.resourcename = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, resource.getName());

            ResultSet rs = ps.executeQuery();
            List<Engine> list = new ArrayList<Engine>();

            while (rs.next()) {
                list.add(new Engine(rs.getString("engineName"), rs.getString("endpoint"), rs.getString("status")));
            }
            return list;

        } catch (SQLException ex) {
            logger.error("Error getting engines by resource {}", resource.getName(), ex);
            throw new DAOException(ex);
        }  
    }
}
