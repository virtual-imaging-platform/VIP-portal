package fr.insalyon.creatis.vip.application.server;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.WorkflowsDBException;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
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
    public WorkflowsDBDAOFactory workflowsDBDAOFactory() throws WorkflowsDBDAOException, WorkflowsDBException {
        return new WorkflowsDBDAOFactory();
    }

    @Bean
    public SessionFactory workflowsDBSessionFactory() throws WorkflowsDBDAOException, WorkflowsDBException {
        return workflowsDBDAOFactory().getSessionFactory();
    }

    @Bean
    public WorkflowDAO getWorkflowDAO() throws WorkflowsDBDAOException, WorkflowsDBException {
        return workflowsDBDAOFactory().getWorkflowDAO();
    }

    @Bean
    public ProcessorDAO getProcessorDAO() throws WorkflowsDBDAOException, WorkflowsDBException {
        return workflowsDBDAOFactory().getProcessorDAO();
    }

    @Bean
    public OutputDAO getOutputDAO() throws WorkflowsDBDAOException, WorkflowsDBException {
        return workflowsDBDAOFactory().getOutputDAO();
    }

    @Bean
    public InputDAO getInputDAO() throws WorkflowsDBDAOException, WorkflowsDBException {
        return workflowsDBDAOFactory().getInputDAO();
    }

    @Bean
    public StatsDAO getStatsDAO() throws WorkflowsDBDAOException, WorkflowsDBException{
        return workflowsDBDAOFactory().getStatsDAO();
    }

}
