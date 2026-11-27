package fr.insalyon.creatis.vip.application.integrationtest;

import java.util.GregorianCalendar;

import fr.insalyon.creatis.vip.application.models.Workflow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.JobStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.models.Job;
import fr.insalyon.creatis.vip.application.models.Task;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// testing framework must recreate a MessageBusiness after each test method
public class WorkflowIT extends BaseSpringIT {

    //FIXME : initialize better the class

    private SimulationBusiness simulationBusiness;

    private Workflow workflow;
    private Job job;

    @BeforeEach
    @Override
    public void setUp() throws DAOException, VipException {

        workflow = new Workflow("execId1", "Exec test 1", "pipelineTest1", "3",
                (String) null, WorkflowStatus.Running.toString(),
                new GregorianCalendar(2016, 9, 2).getTime(), null,
                "test engine", null);


        job = new Job(1, "command", JobStatus.Completed);

        // Create tasks associated to the job
        Task task1 = new Task(1, TaskStatus.COMPLETED, "command");
        Task task2 = new Task(1, TaskStatus.COMPLETED, "command");
        Task task3 = new Task(1, TaskStatus.COMPLETED, "command");

        simulationBusiness = Mockito.mock(SimulationBusiness.class);
        Mockito.doNothing().when(simulationBusiness).sendTaskSignal("execId1", "1", TaskStatus.COMPLETED);
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* get properties *********************************************************** */
    /* ********************************************************************************************************************************************** */


    @Test
    public void testSimulationGetProperties() {
        Assertions.assertEquals("execId1", workflow.getID(), "Incorrect simulation id");
        Assertions.assertEquals(WorkflowStatus.Running, workflow.getStatus(), "Incorrect simulation status");
    }

    @Test
    public void testJobGetProperties() {
        Assertions.assertEquals(1, job.getId(), "Incorrect job id");
        Assertions.assertEquals(JobStatus.Completed, job.getStatus(), "Incorrect job status");
    }

    @Test
    public void testSendEmail() throws VipException {
        simulationBusiness.sendTaskSignal("execId1", "1", TaskStatus.COMPLETED);
    }
}
