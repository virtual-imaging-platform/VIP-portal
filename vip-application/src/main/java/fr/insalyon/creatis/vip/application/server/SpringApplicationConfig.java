package fr.insalyon.creatis.vip.application.server;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.application.server.dao.hibernate.SimulationStatsData;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApplicationConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public SimulationStatsDAO getSimulationStatsDAO() throws DAOException {
        try {
            return new SimulationStatsData(WorkflowsDBDAOFactory.getInstance().getSessionFactory());

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting SimulationStatsDAO", ex);
            throw new DAOException(ex);
        }
    }

    @Bean
    public WorkflowDAO getWorkflowDAO() throws WorkflowsDBDAOException {
        return WorkflowsDBDAOFactory.getInstance().getWorkflowDAO();
    }

    @Bean
    public ProcessorDAO getProcessorDAO() throws WorkflowsDBDAOException {
        return WorkflowsDBDAOFactory.getInstance().getProcessorDAO();
    }

    @Bean
    public OutputDAO getOutputDAO() throws WorkflowsDBDAOException {
        return WorkflowsDBDAOFactory.getInstance().getOutputDAO();
    }

    @Bean
    public InputDAO getInputDAO() throws WorkflowsDBDAOException {
        return WorkflowsDBDAOFactory.getInstance().getInputDAO();
    }

    @Bean
    public StatsDAO getStatsDAO() throws WorkflowsDBDAOException {
        return WorkflowsDBDAOFactory.getInstance().getStatsDAO();
    }
}
