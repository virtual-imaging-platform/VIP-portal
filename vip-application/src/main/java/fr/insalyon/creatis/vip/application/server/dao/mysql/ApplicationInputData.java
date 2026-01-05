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

import fr.insalyon.creatis.vip.application.models.SimulationInput;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationInputDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Repository
@Transactional
public class ApplicationInputData extends JdbcDaoSupport implements ApplicationInputDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void addSimulationInput(String email, SimulationInput SimulationInput) throws DAOException {
        String query = "INSERT INTO VIPAppInputs(email, application, name, inputs) VALUES (?,?,?,?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, SimulationInput.getApplication());
            ps.setString(3, SimulationInput.getName());
            ps.setString(4, SimulationInput.getInputs());
            ps.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                logger.error("An input named \"" + SimulationInput.getName() + "\" already exists.");
                throw new DAOException("An input named \"" + SimulationInput.getName() + "\" already exists.", ex);
            } else {
                logger.error("Error adding inputs {} for app {} by {}",
                        SimulationInput.getInputs(),
                        SimulationInput.getApplication(), email, ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void removeSimulationInput(String email, String inputName, String application) throws DAOException {
        String query = "DELETE FROM VIPAppInputs WHERE email=? AND name=? AND application=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, inputName);
            ps.setString(3, application);
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error removing inputs {} by {}", inputName, email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void updateSimulationInput(String email, SimulationInput SimulationInput) throws DAOException {
        String query = "UPDATE VIPAppInputs SET inputs=? WHERE email=? AND application=? AND name=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, SimulationInput.getInputs());
            ps.setString(2, email);
            ps.setString(3, SimulationInput.getApplication());
            ps.setString(4, SimulationInput.getName());
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("Error updating inputs {} for app {} by {}",
                    SimulationInput.getInputs(),
                    SimulationInput.getApplication(), email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<SimulationInput> getSimulationInputByUser(String email) throws DAOException {
        String query =  "SELECT application, name, inputs FROM VIPAppInputs WHERE email=?"
        +               "ORDER BY application, name";
        List<SimulationInput> inputs = new ArrayList<SimulationInput>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                inputs.add(new SimulationInput(
                        rs.getString("application"),
                        rs.getString("name"),
                        rs.getString("inputs")));
            }
            return inputs;

        } catch (SQLException ex) {
            logger.error("Error getting inputs for {}", email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<SimulationInput> getWorkflowInputByUserAndAppName(String user, String appName) throws DAOException {
        String query =  "SELECT username, application, name, inputs FROM WorkflowInput "
        +               "WHERE username=? AND application=? ORDER BY name";
        List<SimulationInput> inputs = new ArrayList<SimulationInput>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, user);
            ps.setString(2, appName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                inputs.add(new SimulationInput(
                        rs.getString("application"),
                        rs.getString("name"),
                        rs.getString("inputs")));
            }
            return inputs;

        } catch (SQLException ex) {
            logger.error("Error getting workflow inputs for {} by {}", appName, user, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public SimulationInput getInputByNameUserApp(String email, String name, String appName) throws DAOException {
        String query =  "SELECT email, application, name, inputs FROM VIPAppInputs "
        +               "WHERE email = ? AND name = ? AND application = ? "
        +               "ORDER BY name";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, appName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return new SimulationInput(
                    rs.getString("application"),
                    rs.getString("name"),
                    rs.getString("inputs"));
            }
            return null;

        } catch (SQLException ex) {
            logger.error("Error getting inputs {} by {}", name, email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void saveSimulationInputAsExample(SimulationInput simulationInput) throws DAOException {
        String query = "INSERT INTO VIPAppExamples(application, name, inputs) VALUES (?,?,?)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, simulationInput.getApplication());
            ps.setString(2, simulationInput.getName());
            ps.setString(3, simulationInput.getInputs());
            ps.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Unique index or primary key violation") || ex.getMessage().contains("Duplicate entry ")) {
                logger.error("An input named \"" + simulationInput.getName() + "\" already exists.");
                throw new DAOException("An input named \"" + simulationInput.getName() + "\" already exists.", ex);
            } else {
                logger.error("Error saving example {} for app {}",
                        simulationInput.getInputs(),
                        simulationInput.getApplication(), ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public List<SimulationInput> getSimulationInputExamples(String applicationName) throws DAOException {
        String query =  "SELECT application, name, inputs FROM VIPAppExamples "
        +               "WHERE application = ? ORDER BY application, name";
        List<SimulationInput> inputs = new ArrayList<SimulationInput>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, applicationName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                inputs.add(new SimulationInput(
                        rs.getString("application"),
                        rs.getString("name"),
                        rs.getString("inputs")));
            }
            return inputs;

        } catch (SQLException ex) {
            logger.error("Error getting examples for app {}", applicationName, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void removeSimulationInputExample(String inputName, String application) throws DAOException {
        String query = "DELETE FROM VIPAppExamples WHERE name=? AND application=?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, inputName);
            ps.setString(2, application);
            ps.execute();

        } catch (SQLException ex) {
            logger.error("Error removing example {} for app {}", inputName, application, ex);
            throw new DAOException(ex);
        }
    }
}
