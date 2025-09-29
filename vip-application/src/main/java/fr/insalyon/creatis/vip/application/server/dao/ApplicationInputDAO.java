package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface ApplicationInputDAO {

    public void addSimulationInput(String email, SimulationInput workflowInput) throws DAOException;

    public void removeSimulationInput(String email, String inputName, String application) throws DAOException;

    public void updateSimulationInput(String email, SimulationInput SimulationInput) throws DAOException;

    public void saveSimulationInputAsExample(SimulationInput SimulationInput) throws DAOException;

    public List<SimulationInput> getSimulationInputByUser(String user) throws DAOException;

    public List<SimulationInput> getWorkflowInputByUserAndAppName(String user, String appName) throws DAOException;

    public SimulationInput getInputByNameUserApp(String email, String name, String appName) throws DAOException;

    public List<SimulationInput> getSimulationInputExamples(String applicationName) throws DAOException;

    public void removeSimulationInputExample(String inputName, String application) throws DAOException;
}
