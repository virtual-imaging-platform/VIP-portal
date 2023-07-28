package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.JobStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.GregorianCalendar;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// testing framework must recreate a MessageBusiness after each test method
public class SimulationIT extends BaseSpringIT {

    //FIXME : initialize better the class

    private SimulationBusiness simulationBusiness;

    private Simulation simulation;
    private Job job;

    @BeforeEach
    public void setUp() throws DAOException, BusinessException {

        simulation = new Simulation("pipelineTest1", "3", null, "execId1",
                "fullName", new GregorianCalendar(2016, 9, 2).getTime(),
                "Exec test 1", SimulationStatus.Running.toString(), "test engine");


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
        Assertions.assertEquals("execId1", simulation.getID(), "Incorrect simulation id");
        Assertions.assertEquals(SimulationStatus.Running, simulation.getStatus(), "Incorrect simulation status");
    }

    @Test
    public void testJobGetProperties() {
        Assertions.assertEquals(1, job.getId(), "Incorrect job id");
        Assertions.assertEquals(JobStatus.Completed, job.getStatus(), "Incorrect job status");
    }

    @Test
    public void testSendEmail() throws BusinessException {
        simulationBusiness.sendTaskSignal("execId1", "1", TaskStatus.COMPLETED);
    }
}
