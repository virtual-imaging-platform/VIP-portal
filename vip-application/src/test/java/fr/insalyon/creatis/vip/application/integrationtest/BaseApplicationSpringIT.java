package fr.insalyon.creatis.vip.application.integrationtest;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;

public class BaseApplicationSpringIT extends BaseSpringIT {

    @Autowired protected WorkflowDAO workflowDAO;
    @Autowired protected OutputDAO outputDAO;
    @Autowired protected InputDAO inputDAO;
    @Autowired protected ApplicationBusiness applicationBusiness;
    @Autowired protected EngineBusiness engineBusiness;
    @Autowired protected AppVersionBusiness appVersionBusiness;
    @Autowired protected WorkflowEngineInstantiator webServiceEngine;

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

    protected EngineBusiness getEngineBusiness() {
        return engineBusiness;
    }

    protected AppVersionBusiness getAppVersionBusiness() {
        return appVersionBusiness;
    }

    public WorkflowEngineInstantiator getWebServiceEngine() {
        return webServiceEngine;
    }

    /**
     * @param appName
     * @param groupname (can be null to be ignored)
     * @throws VipException
     */
    protected void createAnApplication(String appName, String groupname) throws VipException {
        Application app = new Application(appName, "test citation", new ArrayList<>());

        getApplicationBusiness().add(app);

        if (groupname != null) {
            putApplicationInGroup(appName, groupname);
        }
    }

    protected void putApplicationInGroup(String appName, String groupname) throws VipException {
        getApplicationBusiness().associate(new Application(appName, null), new Group(groupname));
    }

    protected AppVersion createAVersion(String appName, String versionName, boolean visible) throws VipException {
        AppVersion appVersion = new AppVersion(appName, versionName, null, visible);
        getAppVersionBusiness().add(appVersion);
        return appVersion;
    }

    protected AppVersion configureAnApplication(String appName, String versionName, String groupName) throws VipException {
        createGroup(groupName);
        createAnApplication(appName, groupName);
        return createAVersion(appName, versionName, true);
    }

    protected void configureVersion(AppVersion appVersion, String descriptor) throws VipException {
        appVersion = new AppVersion(
                appVersion.getApplicationName(), appVersion.getVersion(), descriptor,
                appVersion.isVisible());
        getAppVersionBusiness().update(appVersion);
    }
}
