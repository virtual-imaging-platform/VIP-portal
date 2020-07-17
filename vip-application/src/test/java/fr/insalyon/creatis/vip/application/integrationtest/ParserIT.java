package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/*
 To test InputM2Parser, but more importantly to test the prototype injections
 in spring
 */
public class ParserIT extends BaseSpringIT {

    @Autowired
    private WorkflowBusiness workflowBusiness;

    @Autowired
    private GroupDAO groupDAO;

    @Test
    public void testInputM2Parsing() throws BusinessException, DAOException {
        // test data
        String simulationId = "input_m2_parsing_it";
        String currentUserFolder = "test_user_1";
        Group testGroup = new Group("test group 1", true, true, true);

        // test configuration
        groupDAO.add(testGroup);

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
