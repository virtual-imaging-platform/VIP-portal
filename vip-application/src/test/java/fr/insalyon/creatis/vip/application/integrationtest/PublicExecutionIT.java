package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.application.server.business.PublicExecutionBusiness;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.client.bean.Triplet;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution.PublicExecutionStatus;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

public class PublicExecutionIT extends BaseSpringIT {
    
    @Autowired
    private PublicExecutionBusiness business;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    private PublicExecution dumbExecution(String name) throws BusinessException {
        var workflowData = new Triplet<String, String, String>("a", "b", "c");

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
    public void add() throws BusinessException {
        PublicExecution execution = dumbExecution("test");
        PublicExecution retrieved = business.get(execution.getExperienceName());

        assertEquals(execution, retrieved);
    }

    // this test will also check that worklowsIds order is correct
    @Test
    public void get() throws BusinessException {
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
    public void exist() throws BusinessException {
        PublicExecution execution = dumbExecution("test");

        assertTrue(business.exist(execution.getExperienceName()));
    }

    @Test
    public void getAll() throws BusinessException {
        PublicExecution execution = dumbExecution("test");
        PublicExecution execution2 = dumbExecution("test2");
        List<PublicExecution> retrieved = business.getAll();

        assertEquals(execution, retrieved.get(0));
        assertEquals(execution2, retrieved.get(1));
    }

    @Test
    public void update() throws BusinessException {
        PublicExecution execution = dumbExecution("test");

        execution.setDoi("doi_super");
        business.update(execution.getExperienceName(), execution);

        assertEquals(execution, business.get(execution.getExperienceName()));
    }

    @Test
    public void updateStatus() throws BusinessException {
        PublicExecution execution = dumbExecution("test");

        business.updateStatus(execution.getExperienceName(), PublicExecutionStatus.DIRECTORY_CREATED);
        execution.setStatus(PublicExecutionStatus.DIRECTORY_CREATED);

        assertEquals(execution, business.get(execution.getExperienceName()));
    
    }
}
