package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.simulation.WebServiceEngine;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseApplicationSpringIT extends BaseSpringIT {

    @Autowired protected WorkflowDAO workflowDAO;
    @Autowired protected OutputDAO outputDAO;
    @Autowired protected InputDAO inputDAO;
    @Autowired protected WebServiceEngine webServiceEngine;
    @Autowired protected ApplicationBusiness applicationBusiness;
    @Autowired protected EngineBusiness engineBusiness;
    @Autowired protected AppVersionBusiness appVersionBusiness;

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

    public WebServiceEngine getWebServiceEngine() {
        return webServiceEngine;
    }

    protected void createAnApplication(String appName) throws BusinessException {
        getApplicationBusiness().add(new Application(appName, "test citation"));
    }

    protected AppVersion createAVersion(String appName, String versionName, boolean visible, String gwendiaPath, String jsonPath) throws BusinessException {
        AppVersion appVersion = new AppVersion(appName, versionName, gwendiaPath, jsonPath, visible, true);
        getAppVersionBusiness().add(appVersion);
        return appVersion;
    }

    protected AppVersion configureAnApplication(String appName, String versionName, String groupName) throws BusinessException {
        createGroup(groupName);
        createAnApplication(appName);
        return createAVersion(appName, versionName, true, null, null);
    }

    protected void configureVersion(AppVersion appVersion, String gwendiaPath, String jsonPath) throws BusinessException {
        appVersion = new AppVersion(
                appVersion.getApplicationName(), appVersion.getVersion(), gwendiaPath, jsonPath,
                appVersion.isVisible(), appVersion.isBoutiquesForm());
        getAppVersionBusiness().update(appVersion);
    }
}
