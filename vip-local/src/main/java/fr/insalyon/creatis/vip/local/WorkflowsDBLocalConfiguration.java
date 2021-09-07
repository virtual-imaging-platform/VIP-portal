package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.hibernate.*;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.application.server.dao.hibernate.SimulationStatsData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * overrides workflowsdb dao by others configured with a h2 database
 */

@Configuration
@Profile("local")
public class WorkflowsDBLocalConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Resource vipConfigFolder;

    private SessionFactory workflowsDbSessionFactory;

    @PreDestroy
    public void closeWorkflowsDB() {
        logger.info("closing workflowsdb. Already closed : {}", workflowsDbSessionFactory.isClosed());
        // close connection pool to close connections and h2 database
        if(workflowsDbSessionFactory instanceof SessionFactoryImpl) {
            SessionFactoryImpl sf = (SessionFactoryImpl)workflowsDbSessionFactory;
            ConnectionProvider conn = sf.getConnectionProvider();
            logger.info("ConnectionProvider : {}", conn);
            ((DriverManagerConnectionProviderImpl) conn).stop();
        }
        workflowsDbSessionFactory.close();
        logger.info("workflowsdb closed ");
    }

    @Bean(destroyMethod = "")
    public SessionFactory workflowsDbSessionFactory() throws IOException {
        logger.info("building a new workflowsdb session factory");
        String h2URL = "jdbc:h2:" + vipConfigFolder.getFile().getAbsolutePath() + "/workflowsdb";

        try {
            org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration();
            //cfg.setProperty("hibernate.default_schema", "workflowsdb");
            cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
            cfg.setProperty("hibernate.connection.url", h2URL);
            cfg.setProperty("hibernate.dialect", H2Dialect.class.getCanonicalName());
            cfg.setProperty("hibernate.connection.username", "sa");
            cfg.setProperty("hibernate.connection.password", "");
            cfg.setProperty("javax.persistence.validation.mode", "none");
            cfg.setProperty("hibernate.validator.apply_to_ddl", "false");
            cfg.setProperty("hibernate.validator.autoregister_listeners", "false");
            cfg.setProperty("hibernate.hbm2ddl.auto", "update");
            cfg.addAnnotatedClass(Workflow.class);
            cfg.addAnnotatedClass(Processor.class);
            cfg.addAnnotatedClass(ProcessorID.class);
            cfg.addAnnotatedClass(Input.class);
            cfg.addAnnotatedClass(InputID.class);
            cfg.addAnnotatedClass(Output.class);
            cfg.addAnnotatedClass(OutputID.class);
            cfg.addAnnotatedClass(Stats.class);
            ServiceRegistry serviceRegistry = (new ServiceRegistryBuilder()).applySettings(cfg.getProperties()).buildServiceRegistry();
            workflowsDbSessionFactory = cfg.buildSessionFactory(serviceRegistry);
            return workflowsDbSessionFactory;
        } catch (Exception e) {
            throw new BeanInitializationException("Error creating workflows db local hibernate session factory", e);
        }
    }

    @Bean
    public WorkflowDAO getWorkflowDAO() throws IOException {
        return new WorkflowData(workflowsDbSessionFactory());
    }

    @Bean
    public ProcessorDAO getProcessorDAO() throws IOException {
        return new ProcessorData(workflowsDbSessionFactory());
    }

    @Bean
    public OutputDAO getOutputDAO() throws IOException {
        return new OutputData(workflowsDbSessionFactory());
    }

    @Bean
    public InputDAO getInputDAO() throws IOException {
        return new InputData(workflowsDbSessionFactory());
    }

    @Bean
    public StatsDAO getStatsDAO() throws IOException {
        return new StatsData(workflowsDbSessionFactory());
    }
}
