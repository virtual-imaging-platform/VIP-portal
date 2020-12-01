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
    public WorkflowsDBDAOFactory workflowsDBDAOFactory() throws WorkflowsDBDAOException {
        return new WorkflowsDBDAOFactory();
    }

    @Bean
    public SimulationStatsDAO getSimulationStatsDAO() throws WorkflowsDBDAOException {
        return new SimulationStatsData(workflowsDBDAOFactory().getSessionFactory());
    }

    @Bean
    public WorkflowDAO getWorkflowDAO() throws WorkflowsDBDAOException {
        return workflowsDBDAOFactory().getWorkflowDAO();
    }

    @Bean
    public ProcessorDAO getProcessorDAO() throws WorkflowsDBDAOException {
        return workflowsDBDAOFactory().getProcessorDAO();
    }

    @Bean
    public OutputDAO getOutputDAO() throws WorkflowsDBDAOException {
        return workflowsDBDAOFactory().getOutputDAO();
    }

    @Bean
    public InputDAO getInputDAO() throws WorkflowsDBDAOException {
        return workflowsDBDAOFactory().getInputDAO();
    }

    @Bean
    public StatsDAO getStatsDAO() throws WorkflowsDBDAOException {
        return workflowsDBDAOFactory().getStatsDAO();
    }
}
