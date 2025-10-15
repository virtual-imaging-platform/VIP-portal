package fr.insalyon.creatis.vip.api.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;

public class ExecutionBusinessTest {

    public static final String[] USER_FIRST_NAME = {"firstName_1", "firstName_2"};
    public static final String[] USER_LAST_NAME = {"lastName_1", "lastName_2"};
    public static final String[] USER_MAIL = {"mail_1@test.tst", "mail_2@test.tst"};
    public static final String EXEC_ID = "exec-test-1";

    @Test
    public void checkIfAdminCanAccessAnyExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, true);
        WorkflowBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, new Simulation());
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, mockedWb, null, null, null);
        sut.checkIfUserCanAccessExecution(EXEC_ID);
    }

    @Test
    public void checkIfBasicUserCannotAccessAnyExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, false);
        Simulation simulation = prepareRunningSimulation(EXEC_ID, 1); // choose a different user
        WorkflowBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, simulation);
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, mockedWb, null, null, null);
        VipException vipException = assertThrows(VipException.class,
            () -> sut.checkIfUserCanAccessExecution(EXEC_ID)
        );
        assertEquals("Permission denied", vipException.getMessage());
    }

    @Test
    public void checkIfBasicUserCanAccessItsExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, false);
        Simulation simulation = prepareRunningSimulation(EXEC_ID, 0); // the creator of the execution is the same user
        WorkflowBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, simulation);
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, mockedWb, null, null, null);
        sut.checkIfUserCanAccessExecution(EXEC_ID);
    }

    @Test
    public void checkErrorWhenAccessingACleanedExecution() throws Exception {
        Supplier<User> userSupplier = () -> prepareTestUser(0, false);
        Simulation simulation = prepareSimulation(EXEC_ID, SimulationStatus.Cleaned, 0); // the creator of the execution is the same user
        WorkflowBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, simulation);
        ExecutionBusiness sut = new ExecutionBusiness(userSupplier, null, mockedWb, null, null, null);
        VipException ex = Assertions.assertThrows(VipException.class, () -> sut.getExecution(EXEC_ID, false));
        Assertions.assertTrue(ex.getVipErrorCode().isPresent());
        Assertions.assertEquals(ApiError.INVALID_EXECUTION_ID.getCode(), ex.getVipErrorCode().get());
    }


    // UTILS to be externalized later

    private User prepareTestUser(int userIndex, boolean isAdmin) {
        return new User(USER_FIRST_NAME[userIndex], USER_LAST_NAME[userIndex], USER_MAIL[userIndex], null,
                isAdmin ? UserLevel.Administrator : UserLevel.Beginner, null);
    }

    private Simulation prepareRunningSimulation(String exedId, int userIndex) {
        return prepareSimulation(exedId, SimulationStatus.Running, userIndex);
    }

    private Simulation prepareSimulation(String exedId, SimulationStatus status, int userIndex) {
        User creator = prepareTestUser(userIndex, false);
        return new Simulation(null, null, null, exedId, creator.getFullName(), null, null,
                status.name(), null, null);
    }

    private WorkflowBusiness prepareMockedWorkflowBusiness(String execId, Simulation simu) throws Exception {
        WorkflowBusiness mockedWb = Mockito.mock(WorkflowBusiness.class);
        Mockito.when(mockedWb.getSimulation(execId)).thenReturn(simu);
        return mockedWb;
    }

}