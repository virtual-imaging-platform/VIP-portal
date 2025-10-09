package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;


public interface SimulationStatsDAO {

    public List<String> getBySimulationID(List<String> simulationID) throws DAOException;
     
    public List<String> getWorkflowsPerUser(List<String> workflowsId) throws WorkflowsDBDAOException;
    
    public List<String> getApplications (List<String> workflowsId) throws WorkflowsDBDAOException;
    
}
