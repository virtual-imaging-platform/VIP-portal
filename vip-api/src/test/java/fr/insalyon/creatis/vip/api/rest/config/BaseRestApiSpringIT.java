/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.api.rest.mockconfig.DataConfigurator;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.WorkflowData;
import fr.insalyon.creatis.vip.application.integrationtest.BaseApplicationSpringIT;
import fr.insalyon.creatis.vip.api.SpringRestApiConfig;
import fr.insalyon.creatis.vip.application.integrationtest.SpringApplicationTestConfig;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.application.server.business.util.FileUtil;
import fr.insalyon.creatis.vip.core.integrationtest.BaseWebSpringIT;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.GroupBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCPermissionBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
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

    protected void configureDataFS() throws BusinessException {
        DataConfigurator.configureFS(this);
    }

    protected AppVersion configureBoutiquesTestApp(String appName, String groupName, String versionName) throws BusinessException, GRIDAClientException, IOException {
        return configureTestApp(appName, groupName, versionName);
    }

    protected File getBoutiquesTestFile() throws IOException {
        return getResourceFromClasspath("boutiques/test-boutiques.json").getFile();
    }

    protected AppVersion configureTestApp(String appName, String groupName, String versionName) throws BusinessException, GRIDAClientException, IOException {
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
