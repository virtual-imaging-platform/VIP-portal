package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.application.server.dao.SimulationDAO;
import org.hibernate.SessionFactory;
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
    public WorkflowsDBDAOFactory mockWorkflowsDBDAOFactory() {
        return Mockito.mock(WorkflowsDBDAOFactory.class);
    }

    @Bean
    @Primary
    public SessionFactory mockSessionFactory() {
        return Mockito.mock(SessionFactory.class);
    }

    @Bean
    @Primary
    public WorkflowDAO mockWorkflowDAO() {
        return Mockito.mock(WorkflowDAO.class);
    }

    @Bean
    @Primary
    public ProcessorDAO mockProcessorDAO() {
        return Mockito.mock(ProcessorDAO.class);
    }

    @Bean
    @Primary
    public OutputDAO mockOutputDAO() {
        return Mockito.mock(OutputDAO.class);
    }

    @Bean
    @Primary
    public InputDAO mockInputDAO() {
        return Mockito.mock(InputDAO.class);
    }

    @Bean
    @Primary
    public StatsDAO mockStatsDAO() {
        return Mockito.mock(StatsDAO.class);
    }

    @Bean
    @Primary
    public WorkflowEngineInstantiator mockWebServiceEngine() {
        return Mockito.mock(WorkflowEngineInstantiator.class);
    }

    @Bean
    @Primary
    public SimulationDAO mockSimulationDAO() {
        return Mockito.mock(SimulationDAO.class);
    }

}
