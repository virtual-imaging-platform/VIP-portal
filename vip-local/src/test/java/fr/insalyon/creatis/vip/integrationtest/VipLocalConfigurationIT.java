package fr.insalyon.creatis.vip.integrationtest;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
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
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Automatic test class that verifies a local configuration and do some
 * tests validating it can run executions.
 *
 * The local instance location must be configured through the "vipConfigFolder"
 * property in the "$HOME/.vip/local-config-folder.properties" file. It must
 * include a single line "vipConfigFolder = /path/to/vip/local/folder"
 *
 * The selected folder needs to include the two mandatory files "vip.conf" and
 * "vip-api.conf", and also a third "vip-local.conf" for local test
 * configuration purposes. A archive containing these three files with
 * valid content for local test is available in "src/main/resources/local-config.zip"
 *
 * Theses tests are disabled because they are not meant to be run in a classic
 * build lifecycle. They are meant to validate a specific local vip
 * installation or to be adapted to add more content in a local vip.
 */
@SpringJUnitWebConfig(value = SpringCoreConfig.class)
@ActiveProfiles({"local", "config-file", "local-db"})
@TestPropertySource(locations = "file:${user.home}/.vip/local-config-folder.properties")
@Disabled
@TestMethodOrder(OrderAnnotation.class)
public class VipLocalConfigurationIT {

    // test application data
    public final Integer TEST_APP_TIMEOUT_IN_SECONDS = 30;
    @Autowired
    private ApplicationBusiness applicationBusiness;
    @Autowired
    private WorkflowBusiness workflowBusiness;
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
    @Value("${local.data.class.name:localClass}")
    private String className;
    @Value("${local.data.application.name}")
    private String applicationName;
    @Value("${local.data.application.version}")
    private String applicationVersion;
    @Value("${local.data.application.input}")
    private String applicationInputFileLocation;
    @Value("${local.data.application.parameters.file}")
    private String applicationFileParameter;
    @Value("${local.data.application.parameters.text}")
    private String applicationTextParameter;
    @Value("${local.data.application.output}")
    private String applicationOutput;

    @Test
    @Order(1)
    public void testConfig() throws BusinessException {
        Assertions.assertEquals(1, applicationBusiness.getApplications().size());
    }

    // execution tests

    @Test
    @Order(8)
    public void launchSuccessfulExecution() throws BusinessException, IOException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());
        String resultLFN = "/vip/Home/" + applicationOutput;
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));

        Map<String, String> inputs = buildTestInputs("net", "/vip/Home");
        String simulationID =
                launchTestExecution(admin, "Test simu ok", inputs);

        // verify it's successful
        assertExecutionFinishWithStatus(simulationID, SimulationStatus.Completed);
        // verify output
        assertOutputFile(admin, resultLFN, "lorem_ipsum-grep_net.result");

        transferPoolBusiness.delete(admin, resultLFN);
        Assertions.assertFalse(lfcBusiness.exists(admin, resultLFN));
    }

    @Test
    @Order(9)
    public void launchExecutionWithoutInput() throws BusinessException, IOException {
        User admin = configurationBusiness.getUser(server.getAdminEmail());
        String resultLFN = "/vip/Home/" + applicationOutput;
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
        String resultLFN = "/vip/Home/" + applicationOutput;
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
        String resultLFN = "/vip/Home/" + applicationOutput;
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

        String inputFileName = Paths.get(applicationInputFileLocation).getFileName().toString();

        Map<String, String> inputs = new HashMap<>();
        inputs.put(applicationTextParameter, text );
        inputs.put(applicationFileParameter, "/vip/Home/" + inputFileName);
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
                applicationName, applicationVersion, simulationName);
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
