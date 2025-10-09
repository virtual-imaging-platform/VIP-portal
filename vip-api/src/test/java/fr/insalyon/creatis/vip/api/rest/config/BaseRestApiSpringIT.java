package fr.insalyon.creatis.vip.api.rest.config;

import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_DATA_MAX_SIZE;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_DEFAULT_LIST_LIMIT;
import fr.insalyon.creatis.vip.application.client.bean.WorkflowData;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_DESCRIPTION;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_EMAIL;
import fr.insalyon.creatis.vip.application.integrationtest.SpringApplicationTestConfig;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_NAME;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_API_VERSION;
import fr.insalyon.creatis.vip.core.integrationtest.BaseWebSpringIT;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_MODULES;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_PROTOCOLS;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_UNSUPPORTED_METHOD;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.*;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.api.SpringRestApiConfig;
import fr.insalyon.creatis.vip.api.rest.mockconfig.DataConfigurator;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.integrationtest.BaseApplicationSpringIT;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowExecutionBusiness;
import fr.insalyon.creatis.vip.application.server.business.util.FileUtil;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;

/**
 * Base Class to test the Rest API with mocked http requests
 * See {@link BaseWebSpringIT}
 * and ({@link fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT}
 * for more info on configuration
 */
@ContextHierarchy(
        @ContextConfiguration(name="rest-api", classes = SpringRestApiConfig.class)
)
abstract public class BaseRestApiSpringIT extends BaseWebSpringIT {


    @Autowired
    protected TransferPoolBusiness transferPoolBusiness;
    @Autowired
    protected SimulationBusiness simulationBusiness;
    @Autowired
    protected LFCBusiness lfcBusiness;
    @Autowired
    protected LFCPermissionBusiness lfcPermissionBusiness;
    @Autowired
    protected WorkflowExecutionBusiness workflowExecutionBusiness;
    @Autowired
    protected SpringApplicationTestConfig.ApplicationTestConfigurer applicationTestConfigurer;
    @Autowired protected WorkflowDAO workflowDAO;
    @Autowired protected OutputDAO outputDAO;
    @Autowired protected InputDAO inputDAO;
    @Autowired protected WorkflowEngineInstantiator webServiceEngine;
    @Autowired protected ApplicationBusiness applicationBusiness;
    @Autowired protected EngineBusiness engineBusiness;
    @Autowired protected AppVersionBusiness appVersionBusiness;

    @Override
    protected String getServletPath() {
        return "rest";
    }

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // vip.conf API properties mocks
        when(server.getCarminPlatformName()).thenReturn(TEST_PLATFORM_NAME);
        when(server.getCarminPlatformDescription()).thenReturn(TEST_PLATFORM_DESCRIPTION);
        when(server.getCarminPlatformEmail()).thenReturn(TEST_PLATFORM_EMAIL);
        when(server.getCarminSupportedTransferProtocols()).thenReturn(TEST_SUPPORTED_PROTOCOLS);
        when(server.getCarminSupportedModules()).thenReturn(TEST_SUPPORTED_MODULES);
        when(server.getCarminDefaultLimitListExecution()).thenReturn(Long.valueOf(TEST_DEFAULT_LIST_LIMIT));
        when(server.getCarminUnsupportedMethods()).thenReturn(TEST_UNSUPPORTED_METHOD);
        when(server.getCarminApiDataTransfertMaxSize()).thenReturn(Long.valueOf(TEST_DATA_MAX_SIZE));
        when(server.getCarminSupportedApiVersion()).thenReturn(TEST_SUPPORTED_API_VERSION);
        when(server.getCarminApikeyGenerateNewEachTime()).thenReturn(false);
        when(server.getCarminApiPipelineWhiteList()).thenReturn(new String[]{});
    }

    public TransferPoolBusiness getTransferPoolBusiness() {
        return transferPoolBusiness;
    }

    public SimulationBusiness getSimulationBusiness() {
        return simulationBusiness;
    }

    public LFCBusiness getLfcBusiness() {
        return lfcBusiness;
    }

    protected void configureDataFS() throws VipException {
        DataConfigurator.configureFS(this);
    }

    protected AppVersion configureBoutiquesTestApp(String appName, String groupName, String versionName) throws VipException, GRIDAClientException, IOException {
        return configureTestApp(appName, groupName, versionName);
    }

    protected File getBoutiquesTestFile() throws IOException {
        return getResourceFromClasspath("boutiques/test-boutiques.json").getFile();
    }

    protected AppVersion configureTestApp(String appName, String groupName, String versionName) throws VipException, GRIDAClientException, IOException {
        setAdminContext();
        AppVersion appVersion = applicationTestConfigurer.configureAnApplication(appName, versionName, groupName);
        applicationTestConfigurer.configureVersion(appVersion, FileUtil.read(getBoutiquesTestFile()));

        Mockito.when(server.getDataManagerPath()).thenReturn("/test/folder");

        // localDir is datamanagerpath + "downloads" + groupRoot + dir(path)
        File jsonFile = getBoutiquesTestFile();
        Mockito.when(gridaClient.getRemoteFile(
                ServerMockConfig.TEST_GROUP_ROOT + "/testGroup/path/to/desc-boutiques.json",
                "/test/folder/downloads" + ServerMockConfig.TEST_GROUP_ROOT + "/testGroup/path/to")).thenReturn(jsonFile.getAbsolutePath());
        return appVersion;
    }

    // vip-application test utils

    protected void createAnApplication(String appName, String groupname) throws BusinessException {
        applicationTestConfigurer.createAnApplication(appName, groupname);
    }

    public void putApplicationInGroup(String appName, String groupname) throws BusinessException {
        applicationTestConfigurer.putApplicationInGroup(appName, groupname);
    }

    public AppVersion createAVersion(String appName, String versionName, boolean visible) throws BusinessException {
        return applicationTestConfigurer.createAVersion(appName, versionName, visible);
    }
}
