package fr.insalyon.creatis.vip.application.integrationtest;

import java.util.HashSet;

import org.hibernate.SessionFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.ProcessorDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.StatsDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOFactory;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.application.server.dao.SimulationDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.TestConfigurer;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;

/*
   Replaces workflowsdb beans by mocks in tests.
   And configure them with a TestConfigurer
 */
@Configuration
@Profile("test")
public class SpringApplicationTestConfig {

    @Component
    public static class ApplicationTestConfigurer implements TestConfigurer {
        @Autowired protected WorkflowDAO workflowDAO;
        @Autowired protected OutputDAO outputDAO;
        @Autowired protected InputDAO inputDAO;
        @Autowired protected WorkflowEngineInstantiator webServiceEngine;
        @Autowired protected ApplicationBusiness applicationBusiness;
        @Autowired protected EngineBusiness engineBusiness;
        @Autowired protected AppVersionBusiness appVersionBusiness;
        @Autowired protected GroupBusiness groupBusiness;

        @Override
        public void setUpBeforeEachTest() {
            Mockito.reset(workflowDAO);
            Mockito.reset(outputDAO);
            Mockito.reset(webServiceEngine);
            Mockito.reset(inputDAO);
        }

        public void createAnApplication(String appName, String groupname) throws VipException {
            Application app = new Application(appName, "test citation", "test note", new HashSet<>());

            applicationBusiness.add(app);

            if (groupname != null) {
                putApplicationInGroup(appName, groupname);
            }
        }

        public void putApplicationInGroup(String appName, String groupname) throws VipException {
            applicationBusiness.associate(new Application(appName, null), groupname);
        }

        public AppVersion createAVersion(String appName, String versionName, boolean visible) throws VipException {
            AppVersion appVersion = new AppVersion(appName, versionName, null, visible);
            appVersionBusiness.add(appVersion);
            return appVersion;
        }

        public AppVersion configureAnApplication(String appName, String versionName, String groupName) throws VipException {
            groupBusiness.add(new Group(groupName, true, GroupType.APPLICATION));
            createAnApplication(appName, groupName);
            return createAVersion(appName, versionName, true);
        }

        public void configureVersion(AppVersion appVersion, String descriptor) throws VipException {
            appVersion = new AppVersion(
                    appVersion.getApplicationName(), appVersion.getVersion(), descriptor,
                    appVersion.isVisible());
            appVersionBusiness.update(appVersion);
        }
    }

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
