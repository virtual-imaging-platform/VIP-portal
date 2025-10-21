package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.application.models.PublicExecution;
import fr.insalyon.creatis.vip.application.models.WorkflowData;
import fr.insalyon.creatis.vip.application.models.PublicExecution.PublicExecutionStatus;
import fr.insalyon.creatis.vip.application.server.business.PublicExecutionBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;

public class PublicExecutionIT extends BaseApplicationSpringIT {
    
    @Autowired
    private PublicExecutionBusiness business;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    private PublicExecution dumbExecution(String name) throws VipException {
        var workflowData = new WorkflowData("a", "b", "c");

        PublicExecution execution = new PublicExecution(
            name, 
            Arrays.asList(workflowData), 
            PublicExecution.PublicExecutionStatus.REQUESTED, 
            "superUser", 
            "super_comment", 
            Arrays.asList("d", "e"), 
            "super_doi");

        business.create(execution);
        return execution;
    }

    @Test
    public void add() throws VipException {
        PublicExecution execution = dumbExecution("test");
        PublicExecution retrieved = business.get(execution.getExperienceName());

        assertEquals(execution, retrieved);
    }

    // this test will also check that worklowsIds order is correct
    @Test
    public void get() throws VipException {
        var workflowsIds = List.of("workflow-b", "workflow-c", "workflow-a");
        var appNames = List.of("comet", "lcmodel", "cquest");
        var appVersions = List.of("0.3", "0.2", "0.1");
        var outputs = List.of("lexa.txt", "aniros.txt");

        PublicExecution execution = new PublicExecution(
            "test",
            String.join(PublicExecution.SEPARATOR, workflowsIds),
            String.join(PublicExecution.SEPARATOR, appNames),
            String.join(PublicExecution.SEPARATOR, appVersions),
            PublicExecutionStatus.PUBLISHED,
            "me",
            "wow",
            String.join(PublicExecution.SEPARATOR, outputs),
            "iod"
        );

        business.create(execution);

        assertEquals(execution, business.get(execution.getExperienceName()));
    }

    @Test
    public void exist() throws VipException {
        PublicExecution execution = dumbExecution("test");

        assertTrue(business.exist(execution.getExperienceName()));
    }

    @Test
    public void getAll() throws VipException {
        PublicExecution execution = dumbExecution("test");
        PublicExecution execution2 = dumbExecution("test2");
        List<PublicExecution> retrieved = business.getAll();

        assertEquals(execution, retrieved.get(0));
        assertEquals(execution2, retrieved.get(1));
    }

    @Test
    public void update() throws VipException {
        PublicExecution execution = dumbExecution("test");

        execution.setDoi("doi_super");
        business.update(execution.getExperienceName(), execution);

        assertEquals(execution, business.get(execution.getExperienceName()));
    }

    @Test
    public void updateStatus() throws VipException {
        PublicExecution execution = dumbExecution("test");

        business.updateStatus(execution.getExperienceName(), PublicExecutionStatus.DIRECTORY_CREATED);
        execution.setStatus(PublicExecutionStatus.DIRECTORY_CREATED);

        assertEquals(execution, business.get(execution.getExperienceName()));
    
    }
}
