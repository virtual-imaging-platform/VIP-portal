package fr.insalyon.creatis.vip.api.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;

import fr.insalyon.creatis.vip.application.models.Workflow;
import fr.insalyon.creatis.vip.application.server.business.ListWorkflowsBusiness;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;

public class ExecutionBusinessTest {

    public static final String[] USER_FIRST_NAME = {"firstName_1", "firstName_2"};
    public static final String[] USER_LAST_NAME = {"lastName_1", "lastName_2"};
    public static final String[] USER_MAIL = {"mail_1@test.tst", "mail_2@test.tst"};
    public static final String EXEC_ID = "exec-test-1";

    @Test
    public void checkIfAdminCanAccessAnyExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, true);
        ListWorkflowsBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, new Workflow());
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, null, null, mockedWb, null, null, null);
        sut.checkIfUserCanAccessExecution(EXEC_ID);
    }

    @Test
    public void checkIfBasicUserCannotAccessAnyExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, false);
        Workflow workflow = prepareRunningSimulation(EXEC_ID, 1); // choose a different user
        ListWorkflowsBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, workflow);
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, null, null, mockedWb, null, null, null);
        VipException vipException = assertThrows(VipException.class,
            () -> sut.checkIfUserCanAccessExecution(EXEC_ID)
        );
        assertEquals("Error : Permission denied (Error code 9000)", vipException.getMessage());
    }

    @Test
    public void checkIfBasicUserCanAccessItsExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, false);
        Workflow workflow = prepareRunningSimulation(EXEC_ID, 0); // the creator of the execution is the same user
        ListWorkflowsBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, workflow);
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, null, null, mockedWb, null, null, null);
        sut.checkIfUserCanAccessExecution(EXEC_ID);
    }

    @Test
    public void checkErrorWhenAccessingACleanedExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, false);
        Workflow workflow = prepareSimulation(EXEC_ID, WorkflowStatus.Cleaned, 0); // the creator of the execution is the same user
        ListWorkflowsBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, workflow);
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, null, null, mockedWb, null, null, null);
        VipException ex = Assertions.assertThrows(VipException.class, () -> sut.getExecution(EXEC_ID, false));
        Assertions.assertEquals(ApiError.INVALID_EXECUTION_ID.getCode(), ex.getVipErrorCode());
    }


    // UTILS to be externalized later

    private User prepareTestUser(int userIndex, boolean isAdmin) {
        return new User(USER_FIRST_NAME[userIndex], USER_LAST_NAME[userIndex], USER_MAIL[userIndex], null,
                isAdmin ? UserLevel.Administrator : UserLevel.Beginner, null);
    }

    private Workflow prepareRunningSimulation(String exedId, int userIndex) {
        return prepareSimulation(exedId, WorkflowStatus.Running, userIndex);
    }

    private Workflow prepareSimulation(String exedId, WorkflowStatus status, int userIndex) {
        User creator = prepareTestUser(userIndex, false);
        
        return new Workflow(
                exedId,
                "simuName",
                "appName",
                "1.0",
                creator.getFullName(),
                status.toString(),
                new java.util.Date(),
                null,
                "engine",
                null
            );
                
    }

    private ListWorkflowsBusiness prepareMockedWorkflowBusiness(String execId, Workflow simu) throws Exception {
        ListWorkflowsBusiness mockedWb = Mockito.mock(ListWorkflowsBusiness.class);
        Mockito.when(mockedWb.getNotRefreshedWorkflow(execId)).thenReturn(simu);
        return mockedWb;
    }

}