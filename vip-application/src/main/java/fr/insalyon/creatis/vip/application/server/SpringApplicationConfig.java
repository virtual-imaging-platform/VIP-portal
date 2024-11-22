package fr.insalyon.creatis.vip.application.server;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.vip.application.server.business.simulation.RestServiceEngine;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Spring special configuration for vip-application.
 * This concerns the dao coming from workflowsdb-common, they are created as
 * spring beans here to be injected where they are needed
 *
 * The use of profile is needed to not create these beans in test or local use
 * as WorkflowsDBDAOFactory constructor would throw an exception
 */

@Configuration
@Profile({"default", "prod"})
public class SpringApplicationConfig {

    @Bean
    public WorkflowsDBDAOFactory workflowsDBDAOFactory() throws WorkflowsDBDAOException {
        return new WorkflowsDBDAOFactory();
    }

    @Bean
    public SessionFactory workflowsDBSessionFactory() throws WorkflowsDBDAOException {
        return workflowsDBDAOFactory().getSessionFactory();
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

    @Bean
    public WorkflowEngineInstantiator getWorkflowEngineInstantiator(Server server) {
        return new RestServiceEngine(server);
    }

}
