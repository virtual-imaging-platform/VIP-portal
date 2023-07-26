package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 To test InputM2Parser, but more importantly to test the prototype injections
 in spring
 */
public class ParserIT extends BaseSpringIT {

    @Autowired
    private WorkflowBusiness workflowBusiness;

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationBusiness applicationBusiness;

    @Autowired
    private SimulationBusiness simulationBusiness;

    /*@BeforeEach
    public void setUp() throws BusinessException, GRIDAClientException
    {
        // Adding 4 new users to configurationBusiness
        configurationBusiness.getOrCreateUser("test1@test.fr", "institution", "group1");
        configurationBusiness.getOrCreateUser("test2@test.fr", "institution", "group1");
        configurationBusiness.getOrCreateUser("test3@test.fr", "institution", "group1");
        configurationBusiness.getOrCreateUser("test4@test.fr", "institution", null);

        List<String> applicationClasses = new ArrayList<>();

        applicationClasses.add("class1");
        applicationClasses.add("class2");

        List<String> applicationGroups = new ArrayList<>();

        applicationClasses.add("group1");


        Application application = new Application("app", applicationClasses, "test1@test.fr", "fullAppName", "citation", applicationGroups);
        applicationBusiness.add(application);


    }



    @Test
    public void testInputM2Parsing() throws BusinessException, DAOException, IOException {
        // test data
        String simulationId = "input_m2_parsing_it";
        String currentUserFolder = "test_user_1";
        Group testGroup = new Group("test group 1", true, true, true);

        // test configuration
        groupDAO.add(testGroup);
        Resource testWorkflowPath = applicationContext.getResource("classpath:test_workflow_path");
        when(server.getWorkflowsPath()).thenReturn(testWorkflowPath.getFile().getAbsolutePath());

        // do test
        Map<String, String> res = workflowBusiness.relaunch(simulationId, currentUserFolder);

        // verify
        Assertions.assertEquals(3, res.size());
        Assertions.assertEquals("/vip/test group 1 (group)/inputs/input_test_1.txt", res.get("file"));
        Assertions.assertEquals("mi", res.get("text"));
        Assertions.assertEquals("/vip/Home/output", res.get("results-directory"));

        // do test with another user. Another parser with another currentUserFolder should be used
        res = workflowBusiness.relaunch(simulationId, "other_user");

        // verify
        Assertions.assertEquals(3, res.size());
        Assertions.assertEquals("/vip/test group 1 (group)/inputs/input_test_1.txt", res.get("file"));
        Assertions.assertEquals("mi", res.get("text"));
        Assertions.assertEquals("/vip/Users/test_user_1/output", res.get("results-directory"));
    }

    @Test
    public void testGetSimulations() throws BusinessException
    {
        /*Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        assert workflowBusiness.getSimulations(configurationBusiness.getUser(ServerMockConfig.TEST_ADMIN_EMAIL), startDate).size() == 0 : "Incorrect number of simulations";

    }

    @Test
    public void testCatchNonExistentUserGetSimulations() throws BusinessException {

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        workflowBusiness.getSimulations(configurationBusiness.getUser("nonExistent user"), startDate)
        );

        assertTrue(StringUtils.contains(exception.getMessage(), "There is no user registered with the e-mail : nonExistent user"));

    }


    @Test
    public void testLaunch() throws BusinessException {

        Map<String, String> map = new HashMap<>();

        // Ajout des éléments à la carte
        map.put("results-directory", "/vip/Home");
        map.put("mat_files", "/grid/biomed/creatis/vip/data/groups/Diffusion-Perfusion/data/mat_files.zip");
        map.put("uniform_dist_parameter", "100");
        map.put("LP", "1");

        // Création de la liste
        List<String> list = new ArrayList<>();

        // Ajout de l'élément à la liste
        list.add("group1");


        workflowBusiness.launch(
                configurationBusiness.getUser("test1@test.fr"),
                list,
                map,
                "app",
                "applicationVersion",
                "applicationClass",
                "simulationName");
    }


    /*@Test
    public void testGetCompleteSimulations() throws BusinessException {

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        workflowBusiness.getSimulations(
                String userName, String application, String status, String appClass,
                Date startDate, Date endDate)

    }*/


    @Test
    public void testCatchNonExistentUserLaunch() throws BusinessException {

        Map<String, String> map = new HashMap<>();

        // Ajout des éléments à la carte
        map.put("results-directory", "/vip/Home");
        map.put("mat_files", "/grid/biomed/creatis/vip/data/groups/Diffusion-Perfusion/data/mat_files.zip");
        map.put("uniform_dist_parameter", "100");
        map.put("LP", "1");

        // Création de la liste
        List<String> list = new ArrayList<>();

        // Ajout de l'élément à la liste
        list.add("group1");

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        workflowBusiness.launch(
                                configurationBusiness.getUser("nonExistent user"),
                                list,
                                map,
                                "applicationName",
                                "applicationVersion",
                                "applicationClass",
                                "simulationName")
        );

        assertTrue(StringUtils.contains(exception.getMessage(), "There is no user registered with the e-mail: nonExistent user"));

    }

}