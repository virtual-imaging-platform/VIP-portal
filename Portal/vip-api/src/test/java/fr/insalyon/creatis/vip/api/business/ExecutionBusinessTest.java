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
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

/**
 * Created by abonnet on 7/6/16.
 */
public class ExecutionBusinessTest {

    public static final String[] USER_FIRST_NAME = {"firstName_1", "firstName_2"};
    public static final String[] USER_LAST_NAME = {"lastName_1", "lastName_2"};
    public static final String[] USER_MAIL = {"mail_1@test.tst", "mail_2@test.tst"};
    public static final String EXEC_ID = "exec-test-1";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void checkIfAdminCanAccessAnyExecution() throws Exception {
        ApiContext apiContext = new ApiContext(null, null, prepareTestUser(0, true));
        WorkflowBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, new Simulation());
        ExecutionBusiness sut = new ExecutionBusiness(apiContext, null, mockedWb, null, null, null);
        sut.checkIfUserCanAccessExecution(EXEC_ID);
    }

    @Test
    public void checkIfBasicUserCannotAccessAnyExecution() throws Exception {
        ApiContext apiContext = new ApiContext(null, null, prepareTestUser(0, false));
        Simulation simulation = prepareSimulation(EXEC_ID, 1); // choose a different user
        WorkflowBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, simulation);
        ExecutionBusiness sut = new ExecutionBusiness(apiContext, null, mockedWb, null, null, null);
        exception.expect(ApiException.class);
        exception.expectMessage("Permission denied");
        sut.checkIfUserCanAccessExecution(EXEC_ID);
    }

    //@Test
    public void checkIfBasicUserCanAccessItsExecution() throws Exception {
        ApiContext apiContext = new ApiContext(null, null, prepareTestUser(0, false));
        Simulation simulation = prepareSimulation(EXEC_ID, 0); // the creator of the execution is the same user
        WorkflowBusiness mockedWb = prepareMockedWorkflowBusiness(EXEC_ID, simulation);
        ExecutionBusiness sut = new ExecutionBusiness(apiContext, null, mockedWb, null, null, null);
        sut.checkIfUserCanAccessExecution(EXEC_ID);
    }

    // UTILS to be externalized later

    private User prepareTestUser(int userIndex, boolean isAdmin) {
        return new User(USER_FIRST_NAME[userIndex], USER_LAST_NAME[userIndex], USER_MAIL[userIndex], null, null,
                isAdmin ? UserLevel.Administrator : UserLevel.Beginner, null);
    }

    private Simulation prepareSimulation(String exedId, int userIndex) {
        User creator = prepareTestUser(userIndex, false);
        return new Simulation(null, null, null, exedId, creator.getFullName(), null, null,
                SimulationStatus.Running.name(), null);
    }

    private WorkflowBusiness prepareMockedWorkflowBusiness(String execId, Simulation simu) throws Exception {
        WorkflowBusiness mockedWb = Mockito.mock(WorkflowBusiness.class);
        Mockito.when(mockedWb.getSimulation(execId)).thenReturn(simu);
        return mockedWb;
    }

}