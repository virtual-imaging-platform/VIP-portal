package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.hibernate.*;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.application.server.dao.hibernate.SimulationStatsData;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class WorkflowsDBLocalConfiguration {

    @Value("${local.workflowsdb.url}")
    private String workflowsDbUrl;

    @Bean
    public SessionFactory workflowsDbSessionFactory() {
        try {
            org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration();
            cfg.setProperty("hibernate.default_schema", "workflowsdb");
            cfg.setProperty("hibernate.connection.driver_class", "org.h2.driver");
            cfg.setProperty("hibernate.connection.url", workflowsDbUrl);
            cfg.setProperty("hibernate.dialect", H2Dialect.class.getCanonicalName());
            cfg.setProperty("hibernate.connection.username", "sa");
            cfg.setProperty("hibernate.connection.password", "");
            cfg.setProperty("javax.persistence.validation.mode", "none");
            cfg.setProperty("hibernate.validator.apply_to_ddl", "false");
            cfg.setProperty("hibernate.validator.autoregister_listeners", "false");
            cfg.addAnnotatedClass(Workflow.class);
            cfg.addAnnotatedClass(Processor.class);
            cfg.addAnnotatedClass(ProcessorID.class);
            cfg.addAnnotatedClass(Input.class);
            cfg.addAnnotatedClass(InputID.class);
            cfg.addAnnotatedClass(Output.class);
            cfg.addAnnotatedClass(OutputID.class);
            cfg.addAnnotatedClass(Stats.class);
            ServiceRegistry serviceRegistry = (new ServiceRegistryBuilder()).applySettings(cfg.getProperties()).buildServiceRegistry();
            return cfg.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new BeanInitializationException("Error creating workflows db local hibernate session factory", e);
        }
    }

    @Bean
    public SimulationStatsDAO getSimulationStatsDAO() throws WorkflowsDBDAOException {
        return new SimulationStatsData(workflowsDbSessionFactory());
    }

    @Bean
    public WorkflowDAO getWorkflowDAO()  {
        return new WorkflowData(workflowsDbSessionFactory());
    }

    @Bean
    public ProcessorDAO getProcessorDAO() {
        return new ProcessorData(workflowsDbSessionFactory());
    }

    @Bean
    public OutputDAO getOutputDAO() {
        return new OutputData(workflowsDbSessionFactory());
    }

    @Bean
    public InputDAO getInputDAO() {
        return new InputData(workflowsDbSessionFactory());
    }

    @Bean
    public StatsDAO getStatsDAO() {
        return new StatsData(workflowsDbSessionFactory());
    }
}
