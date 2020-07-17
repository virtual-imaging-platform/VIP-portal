package fr.insalyon.creatis.vip.application.server;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.application.server.dao.hibernate.SimulationStatsData;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"default", "prod"})
public class SpringApplicationConfig {

    @Bean
    public SimulationStatsDAO getSimulationStatsDAO() throws WorkflowsDBDAOException {
        return new SimulationStatsData(WorkflowsDBDAOFactory.getInstance().getSessionFactory());
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
