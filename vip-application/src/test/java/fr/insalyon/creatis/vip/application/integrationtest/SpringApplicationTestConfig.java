package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/*
   replaces workflowsdb beans by mocks in tests
 */
@Configuration
@Profile("test")
public class SpringApplicationTestConfig {


    @Bean
    @Primary
    public WorkflowsDBDAOFactory workflowsDBDAOFactory() {
        return Mockito.mock(WorkflowsDBDAOFactory.class);
    }

    @Bean
    @Primary
    public WorkflowDAO getTestWorkflowDAO() {
        return Mockito.mock(WorkflowDAO.class);
    }

    @Bean
    @Primary
    public ProcessorDAO getTestProcessorDAO() {
        return Mockito.mock(ProcessorDAO.class);
    }

    @Bean
    @Primary
    public OutputDAO getTestOutputDAO() {
        return Mockito.mock(OutputDAO.class);
    }

    @Bean
    @Primary
    public InputDAO getTestInputDAO() {
        return Mockito.mock(InputDAO.class);
    }

    @Bean
    @Primary
    public StatsDAO getTestStatsDAO() {
        return Mockito.mock(StatsDAO.class);
    }
}
