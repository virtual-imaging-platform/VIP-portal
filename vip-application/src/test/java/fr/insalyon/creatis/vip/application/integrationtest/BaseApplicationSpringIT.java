package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.simulation.WebServiceEngine;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BaseApplicationSpringIT extends BaseSpringIT {

    @Autowired
    protected WorkflowDAO workflowDAO;
    @Autowired
    protected OutputDAO outputDAO;
    @Autowired
    protected InputDAO inputDAO;
    @Autowired
    protected WebServiceEngine webServiceEngine;

    @Autowired
    protected ApplicationBusiness applicationBusiness;
    @Autowired
    protected ClassBusiness classBusiness;
    @Autowired
    protected EngineBusiness engineBusiness;

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        Mockito.reset(workflowDAO);
        Mockito.reset(outputDAO);
        Mockito.reset(webServiceEngine);
        Mockito.reset(inputDAO);
    }

    protected ApplicationBusiness getApplicationBusiness() {
        return applicationBusiness;
    }

    protected ClassBusiness getClassBusiness() {
        return classBusiness;
    }

    protected EngineBusiness getEngineBusiness() {
        return engineBusiness;
    }

    public WebServiceEngine getWebServiceEngine() {
        return webServiceEngine;
    }

    protected void createClass(String className, String... groupNames) throws BusinessException {
        getClassBusiness().addClass(new AppClass(className, Collections.emptyList(), List.of(groupNames)));
    }

    protected void createAnApplication(String appName, String... classNames) throws BusinessException {
        getApplicationBusiness().add(new Application(appName, Arrays.asList(classNames), "test citation"));
    }

    protected AppVersion createAVersion(String appName, String versionName, boolean visible, String gwendiaPath, String jsonPath) throws BusinessException {
        AppVersion appVersion = new AppVersion(appName, versionName, gwendiaPath, jsonPath, visible, true);
        getApplicationBusiness().addVersion(appVersion);
        return appVersion;
    }

    protected AppVersion configureAnApplication(String appName, String versionName, String groupName, String className) throws BusinessException {
        createGroup(groupName);
        createClass(className, groupName);
        createAnApplication(appName, className);
        return createAVersion(appName, versionName, true, null, null);
    }

    protected void configureVersion(AppVersion appVersion, String gwendiaPath, String jsonPath) throws BusinessException {
        appVersion = new AppVersion(
                appVersion.getApplicationName(), appVersion.getVersion(), gwendiaPath, jsonPath,
                appVersion.isVisible(), appVersion.isBoutiquesForm());
        getApplicationBusiness().updateVersion(appVersion);
    }

    protected void addEngineToClass(String className, String engineName, String endpoint) throws BusinessException {
        AppClass appClass = getClassBusiness().getClass(className);
        getEngineBusiness().add(new Engine(engineName, endpoint, "enabled"));
        appClass.getEngines().add(engineName);
        getClassBusiness().updateClass(appClass);
    }
}
