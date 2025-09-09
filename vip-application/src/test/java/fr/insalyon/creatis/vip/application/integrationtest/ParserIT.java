package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;


/*
 To test InputFileParser, but more importantly to test the prototype injections
 in spring
 */
public class ParserIT extends BaseSpringIT {

    @Autowired
    private WorkflowBusiness workflowBusiness;

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void testInputM2Parsing() throws BusinessException, DAOException, IOException {
        // test data
        String simulationId = "input_m2_parsing_it";
        String currentUserFolder = "test_user_1";
        Group testGroup = new Group("test group 1", true, GroupType.getDefault());

        // test configuration
        groupDAO.add(testGroup);
        Resource testWorkflowPath = applicationContext.getResource("classpath:test_workflow_path");
        Mockito.when(server.getWorkflowsPath()).thenReturn(testWorkflowPath.getFile().getAbsolutePath());

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

}