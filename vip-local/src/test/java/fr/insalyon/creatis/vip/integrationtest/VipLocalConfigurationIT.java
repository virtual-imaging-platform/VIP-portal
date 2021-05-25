package fr.insalyon.creatis.vip.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LFCBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Automatic test class that initialize a local configuration and do some
 * tests validating it can run executions. In particular, it
 * - creates necessary folders
 * - add an engine, a class and an simple test application
 * - launch several executions
 *
 * The local instance location must be configured through the "vipConfigFolder"
 * property in the "$HOME/.vip/local-config-folder.properties" file. It must
 * include a single line "vipConfigFolder = /path/to/vip/local/folder"
 *
 * The selected folder needs to include the two mandatory files "vip.conf" and
 * "vip-api.conf", and also a third "vip-local.conf" for local test
 * configuration purpose. A archive containing these three files with
 * valid content for local test is available in "src/test/resources/local-config.zip"
 *
 * Theses tests are disabled because they are not meant to be run in a classic
 * build lifecycle. They are meant to be run in specific cases to
 * initialize a local vip instance or to test a scenario.
 *
 * To sum up, to initialize a local vip instance, one has to :
 * - create a dedicated empty folder on his machine
 * - unzip local-config.zip and put the 3 config files in the chosen folder
 * - create a "$HOME/.vip/local-config-folder.properties" file configuring "vipConfigFolder" to the chosen folder
 * - comment the "@Disabled" annotation and run these tests which should be ok
 *
 * Afterward, this vip local instance should be usable through a local
 * vip instance with a web server like tomcat.
 *
 */
@SpringJUnitWebConfig(value = SpringCoreConfig.class)
@ActiveProfiles({"local", "config-file", "local-db"})
@TestPropertySource(locations = "file:${user.home}/.vip/local-config-folder.properties")
@Disabled
@TestMethodOrder(OrderAnnotation.class)
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
    @Autowired
    private TransferPoolBusiness transferPoolBusiness;
    @Autowired
    private LFCBusiness lfcBusiness;
    @Autowired
    private DataManagerBusiness dataManagerBusiness;
    @Autowired
    private Server server;

    public final String CLASS_NAME = "localClass";
    public final String ENGINE_NAME = "localEngine";

    // test application data
    public final String APP_NAME = "localGrepTest";
    public final String APP_VERSION = "0.1";
    public final String TEST_FILES_FOLDER = "localInstanceFiles";
    public final String INPUT_TESTFILE_FILENAME = "lorem_ipsum.txt";
    public final String TEST_APP_GWENDIA_FILENAME = "localGrepTest.gwendia";
    public final String TEST_APP_SCRIPT_FILENAME = "grep.sh";
    public final String TEST_APP_FILE_PARAM_NAME = "file";
    public final String TEST_APP_TEXT_PARAM_NAME = "text";
    public final String TEST_APP_OUTPUT_NAME = "output";
    public final Integer TEST_APP_TIMEOUT_IN_SECONDS = 30;


    @Test
    @Order(1)
    public void testConfig() throws BusinessException {
        Assertions.assertEquals(0, applicationBusiness.getApplications().size());
    }

    @Test
    @Order(2)
    public void createMissingLFNFolders() throws BusinessException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());
        Assertions.assertFalse(lfcBusiness.exists(admin, server.getDataManagerUsersHome() + "/" + admin.getFolder()));
        // create admin lfn folder
        lfcBusiness.createDir(admin, server.getDataManagerUsersHome(), admin.getFolder());
        // create support group lfn folder
        lfcBusiness.createDir(admin, server.getDataManagerGroupsHome(), CoreConstants.GROUP_SUPPORT);
        // create applications root folder (should be a subfolder of an existing folder
        Path appRootLFN = Paths.get(server.getApplicationImporterRootFolder());
        Assertions.assertTrue(lfcBusiness.exists(admin, appRootLFN.getParent().toString()));
        lfcBusiness.createDir(admin,
                appRootLFN.getParent().toString(),
                appRootLFN.getFileName().toString());
    }

    @Test
    @Order(3)
    public void addLocalEngine() throws BusinessException {
        Engine engine = new Engine(ENGINE_NAME, "localEndpoint", "enabled");
        engineBusiness.add(engine);
        // there is a bug, need to enable the engine afterward
        engineBusiness.update(engine);
    }

    @Test
    @Order(4)
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
    @Order(5)
    public void addApplication() throws BusinessException {
        AppClass localClass = classBusiness.getClass(CLASS_NAME);
        Assertions.assertNotNull(localClass);
        Application application = new Application(APP_NAME, Collections.singletonList(localClass.getName()),null);
        applicationBusiness.add(application);
    }

    @Test
    @Order(6)
    public void addAppVersion() throws BusinessException, IOException {
        // upload application gwendia and script
        User admin = configurationBusiness.getUser(server.getAdminEmail());

        // create app folders
        String appFolderLFN = server.getApplicationImporterRootFolder();
        lfcBusiness.createDir(admin, appFolderLFN, APP_NAME);
        appFolderLFN += "/" + APP_NAME;
        String versionDirname = "v" + APP_VERSION;
        lfcBusiness.createDir(admin, appFolderLFN, versionDirname);
        appFolderLFN += "/" + versionDirname;

        // add gwendia file
        File testGwendiaFile = new ClassPathResource(TEST_FILES_FOLDER + "/" + TEST_APP_GWENDIA_FILENAME).getFile();
        transferPoolBusiness.uploadFile(admin, testGwendiaFile.toString(), appFolderLFN);

        // add script file
        File testScriptFile = new ClassPathResource(TEST_FILES_FOLDER + "/" + TEST_APP_SCRIPT_FILENAME).getFile();
        transferPoolBusiness.uploadFile(admin, testScriptFile.toString(), appFolderLFN);

        // create AppVersion
        String gwendiaLFN = appFolderLFN + "/" + TEST_APP_GWENDIA_FILENAME;
        AppVersion appVersion = new AppVersion(APP_NAME, APP_VERSION, gwendiaLFN, null, true);
        applicationBusiness.addVersion(appVersion);

        // add input file
        File inputTestFile = new ClassPathResource(TEST_FILES_FOLDER + "/" + INPUT_TESTFILE_FILENAME).getFile();
        transferPoolBusiness.uploadFile(admin, inputTestFile.toString(), "/vip/Home");
    }

    @Test
    @Order(7)
    public void getAppVersionDescriptor() throws BusinessException {
        Descriptor appDescriptor = workflowBusiness.getApplicationDescriptor(
                configurationBusiness.getUser(server.getAdminEmail()),
                APP_NAME, APP_VERSION);
        Assertions.assertEquals(3, appDescriptor.getSources().size());
    }

    // execution tests

    @Test
    @Order(8)
    public void launchSuccessfulExecution() throws BusinessException, IOException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());
        String resultLFN = "/vip/Home/" + TEST_APP_OUTPUT_NAME;
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));

        Map<String, String> inputs = buildTestInputs("net", "/vip/Home");
        String simulationID =
                launchTestExecution(admin, "Test simu ok", inputs);

        // verify it's successful
        assertExecutionFinishWithStatus(simulationID, SimulationStatus.Completed);
        // verify output
        assertOutputFile(admin, resultLFN, TEST_FILES_FOLDER + "/lorem_ipsum-grep_net.result");

        transferPoolBusiness.delete(admin, resultLFN);
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));
    }

    @Test
    @Order(9)
    public void launchExecutionWithoutInput() throws BusinessException, IOException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());
        String resultLFN = "/vip/Home/" + TEST_APP_OUTPUT_NAME;
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));

        Map<String, String> inputs = new HashMap<>();
        String simulationID =
                launchTestExecution(admin, "Test simu without inputs", inputs);

        // verify it has failed
        assertExecutionFinishWithStatus(simulationID, SimulationStatus.Failed);
        // verify there is no output
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));
    }

    @Test
    @Order(10)
    public void launchExecutionThatFails() throws BusinessException, IOException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());
        String resultLFN = "/vip/Home/" + TEST_APP_OUTPUT_NAME;
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));

        Map<String, String> inputs = buildTestInputs("PATTERN-THAT-MUST-NOT-BE-FOUND",
                "/vip/Home");
        String simulationID =
                launchTestExecution(admin, "Test simu fail", inputs);

        // verify it has failed
        assertExecutionFinishWithStatus(simulationID, SimulationStatus.Failed);
        // verify there is no output
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));
    }

    @Test
    @Order(11)
    public void killExecution() throws BusinessException, IOException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());
        String resultLFN = "/vip/Home/" + TEST_APP_OUTPUT_NAME;
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));

        Map<String, String> inputs = buildTestInputs("net", "/vip/Home");
        String simulationID =
                launchTestExecution(admin, "Test simu ok", inputs);

        workflowBusiness.kill(simulationID);

        // verify it's killed
        assertExecutionFinishWithStatus(simulationID, SimulationStatus.Killed);
        // verify there is no output
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));
    }

    private Map<String, String> buildTestInputs(
            String text, String resulFoldertLFN) {
        Map<String, String> inputs = new HashMap<>();
        inputs.put(TEST_APP_TEXT_PARAM_NAME, text );
        inputs.put(TEST_APP_FILE_PARAM_NAME, "/vip/Home/" + INPUT_TESTFILE_FILENAME);
        inputs.put("results-directory", resulFoldertLFN);
        return inputs;
    }

    private String launchTestExecution(
            User user, String simulationName, Map<String, String> inputs)
            throws BusinessException {
        return workflowBusiness.launch(
                user,
                Collections.emptyList(),
                inputs,
                APP_NAME, APP_VERSION,
                CLASS_NAME, simulationName);
    }

    private void assertExecutionFinishWithStatus(
            String simulationID, SimulationStatus expectedStatus) throws BusinessException {
        Assertions.assertTimeoutPreemptively(
                Duration.ofSeconds(TEST_APP_TIMEOUT_IN_SECONDS),
                () -> waitForExecutionToFinish(simulationID));
        SimulationStatus status = workflowBusiness.getSimulation(simulationID).getStatus();
        Assertions.assertEquals(expectedStatus, status);
    }

    private void waitForExecutionToFinish(String simulationID) throws BusinessException, InterruptedException {
        while (workflowBusiness.getSimulation(simulationID, true).getStatus().equals(SimulationStatus.Running)){
            // wait a little
            Thread.sleep(500);
        }
    }

    private void assertOutputFile(User user, String resultLFN, String classpathExpectedResutFile) throws BusinessException, IOException {
        // verify output
        Assertions.assertTrue(lfcBusiness.exists(user, resultLFN));
        Path localTempFolder = Files.createTempDirectory("vip-local-test");
        dataManagerBusiness.getRemoteFile(user, resultLFN);

        File result = localTempFolder.resolve(Paths.get(resultLFN).getFileName()).toFile();
        File expected = new ClassPathResource(classpathExpectedResutFile).getFile();

        List<String> resultContent = Files.readAllLines(result.toPath());
        List<String> expectedContent = Files.readAllLines(expected.toPath());
        Assertions.assertEquals(expectedContent, resultContent);
    }
}
