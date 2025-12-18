package fr.insalyon.creatis.vip.api.rest.config;

import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_DATA_MAX_SIZE;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_DEFAULT_LIST_LIMIT;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_DESCRIPTION;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_EMAIL;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_NAME;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_API_VERSION;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_MODULES;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_PROTOCOLS;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_UNSUPPORTED_METHOD;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.api.SpringRestApiConfig;
import fr.insalyon.creatis.vip.api.rest.mockconfig.DataConfigurator;
import fr.insalyon.creatis.vip.application.integrationtest.BaseApplicationSpringIT;
import fr.insalyon.creatis.vip.application.models.AppVersion;
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
 * Created by abonnet on 7/28/16.
 * <p>
 * Base test config that allow spring integration testing for vip API
 * <p>
 * The spring test tools allow to simulate the http layer but everything else
 * is the same as production configuration
 * <p>
 * To authenticate, tests should either
 * * login via with httpbasic(user, password)
 * * use {@link WithMockUser} annotation
 * <p>
 */
@ContextConfiguration(classes = { SpringRestApiConfig.class })
abstract public class BaseWebSpringIT extends BaseApplicationSpringIT {

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
    @Autowired
    protected ResourceLoader resourceLoader;
    @Autowired
    protected UserDAO testUserDAO;
    @Autowired
    protected ConfigurationBusiness configurationBusiness;
    @Autowired
    protected TransferPoolBusiness transferPoolBusiness;
    @Autowired
    protected SimulationBusiness simulationBusiness;
    @Autowired
    protected LFCBusiness lfcBusiness;
    @Autowired
    protected LFCPermissionBusiness lfcPermissionBusiness;
    @Autowired
    protected GRIDAClient gridaClient;
    @Autowired
    protected WorkflowExecutionBusiness workflowExecutionBusiness;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/rest"))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

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

    protected String getResourceAsString(String pathFromClasspath) throws IOException {
        Resource resource = getResourceFromClasspath(pathFromClasspath);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    protected Resource getResourceFromClasspath(String pathFromClasspath) {
        return resourceLoader.getResource("classpath:" + pathFromClasspath);
    }

    public WebApplicationContext getWac() {
        return wac;
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public UserDAO getUserDAO() {
        return testUserDAO;
    }

    public ConfigurationBusiness getConfigurationBusiness() {
        return configurationBusiness;
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

    public LFCPermissionBusiness lfcPermissionBusiness() {
        return lfcPermissionBusiness;
    }

    protected void configureDataFS() throws VipException {
        DataConfigurator.configureFS(this);
    }

    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected AppVersion configureBoutiquesTestApp(String appName, String groupName, String versionName) throws VipException, GRIDAClientException, IOException {
        return configureTestApp(appName, groupName, versionName);
    }

    protected File getBoutiquesTestFile() throws IOException {
        return getResourceFromClasspath("boutiques/test-boutiques.json").getFile();
    }

    protected AppVersion configureTestApp(String appName, String groupName, String versionName) throws VipException, GRIDAClientException, IOException {
        setAdminContext();
        AppVersion appVersion = configureAnApplication(appName, versionName, groupName);
        configureVersion(appVersion, FileUtil.read(getBoutiquesTestFile()));

        Mockito.when(server.getDataManagerPath()).thenReturn("/test/folder");

        // localDir is datamanagerpath + "downloads" + groupRoot + dir(path)
        File jsonFile = getBoutiquesTestFile();
        Mockito.when(gridaClient.getRemoteFile(
                ServerMockConfig.TEST_GROUP_ROOT + "/testGroup/path/to/desc-boutiques.json",
                "/test/folder/downloads" + ServerMockConfig.TEST_GROUP_ROOT + "/testGroup/path/to")).thenReturn(jsonFile.getAbsolutePath());
        return appVersion;
    }
}
