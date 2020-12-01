package fr.insalyon.creatis.vip.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringJUnitWebConfig(value = SpringCoreConfig.class)
// launch all spring environment for testing, also take test bean though automatic package scan
@ActiveProfiles({"local", "spring-config-server", "local-db"}) // to take random h2 database and not the test h2 jndi one
@TestPropertySource(properties = "vipConfigFolder:/home/abonnet/Workspace/Testing/VIP/LocalInstance1")
@Disabled
public class VipLocalConfigurationIT {

    @Autowired
    private ApplicationBusiness applicationBusiness;
    @Autowired
    private WorkflowBusiness workflowBusiness;
    @Autowired
    private ClassBusiness classBusiness;
    @Autowired
    private EngineBusiness engineBusiness;
    @Autowired
    private ConfigurationBusiness configurationBusiness;

    public final String ADMIN_EMAIL = "test_admin@test.tst";
    public final String APP_NAME = "localGrepTest";
    public final String APP_VERSION = "0.1";
    public final String CLASS_NAME = "local";
    public final String ENGINE_NAME = "local";
    public final String APP_LFN = "/root_test/vip_test/data/groups/Support/applications/localGrepTest/v0.1/localGrepTest.gwendia";
    public final String INPUT_FILE_LFN = "/vip/Home/testFile.txt";
    public final String INPUT_RESULT_DIR_LFN = "/vip/Home";
    @Test
    public void testConfig() throws BusinessException {
        Assertions.assertEquals(0, applicationBusiness.getApplications().size());
    }

    @Test
    public void addLocalEngine() throws BusinessException {
        Engine engine = new Engine(ENGINE_NAME, "local", "enabled");
        engineBusiness.add(engine);
    }

    @Test
    public void addLocalClass() throws BusinessException {

        List<Engine> engines = engineBusiness.get();
        Assertions.assertEquals(1, engines.size());
        Assertions.assertEquals(ENGINE_NAME, engines.get(0).getName());
        List<Group> groups = configurationBusiness.getGroups();
        Assertions.assertEquals(1, groups.size());
        Assertions.assertEquals(CoreConstants.GROUP_SUPPORT, groups.get(0).getName());
        AppClass appClass = new AppClass(
                CLASS_NAME,
                Collections.singletonList(ENGINE_NAME),
                Collections.singletonList(CoreConstants.GROUP_SUPPORT));
        classBusiness.addClass(appClass);
    }

    @Test
    public void addApplication() throws BusinessException {
        AppClass localClass = classBusiness.getClass(CLASS_NAME);
        Assertions.assertNotNull(localClass);
        Application application = new Application(APP_NAME, Collections.singletonList(localClass.getName()),null);
        applicationBusiness.add(application);
    }

    @Test
    public void addAppVersion() throws BusinessException {
        AppVersion appVersion = new AppVersion(APP_NAME, APP_VERSION, APP_LFN, null, true);
        applicationBusiness.addVersion(appVersion);
    }

    @Test
    public void getAppVersionDescriptor() throws BusinessException {
        Descriptor appDescriptor = workflowBusiness.getApplicationDescriptor(
                configurationBusiness.getUser(ADMIN_EMAIL),
                APP_NAME, APP_VERSION);
        Assertions.assertEquals(3, appDescriptor.getSources().size());
    }

    @Test
    public void launchExecution() throws BusinessException {
        String simulationName = "TEST SIMU";
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.put("text", "hello" );
        parametersMap.put("file", INPUT_FILE_LFN);
        parametersMap.put("results-directory", INPUT_RESULT_DIR_LFN);
        String simulationID = workflowBusiness.launch(
                configurationBusiness.getUser(ADMIN_EMAIL),
                Collections.emptyList(),
                parametersMap,
                APP_NAME, APP_VERSION,
                CLASS_NAME, simulationName);
        boolean isOver;
        do {
            Simulation simulation = workflowBusiness.getSimulation(simulationID, true);
            isOver = ! simulation.getStatus().equals(SimulationStatus.Running);
        } while ( ! isOver);
    }
}
