package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;

import fr.insalyon.creatis.vip.application.models.Task;
import fr.insalyon.creatis.vip.application.models.Workflow;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkflowLogBusiness extends CommonBusiness {

    public enum JobLogType {
        APP_STDOUT("out", ".sh.app.out"),
        APP_STDERR("err", ".sh.app.err"),
        STDOUT("out", ".sh.out"),
        STDERR("err", ".sh.err"),
        SCRIPT("sh", ".sh");

        private final String folder;
        private final String extension;

        JobLogType(String folder, String extension) {
            this.folder = folder;
            this.extension = extension;
        }

        public String getFolder() { return folder; }
        public String getExtension() { return extension; }
    }

    private final ListWorkflowsBusiness listWorkflowsBusiness;
    private final SimulationBusiness simulationBusiness;

    @Autowired
    public WorkflowLogBusiness(ListWorkflowsBusiness listWorkflowsBusiness,
                               SimulationBusiness simulationBusiness) {
        this.listWorkflowsBusiness = listWorkflowsBusiness;
        this.simulationBusiness = simulationBusiness;
    }

    public Workflow getWorkflow(String wid) throws VipException {
        return listWorkflowsBusiness.getNotRefreshedWorkflow(wid);
    }

    public List<Task> listJobs(String wid) throws VipException {
        return simulationBusiness.getJobsList(wid);
    }

    public Task getFirstTask(String wid) throws VipException {
        List<Task> tasks = listJobs(wid);
        if (tasks == null || tasks.isEmpty()) {
            throw new VipException("No jobs found for workflow " + wid);
        }
        return tasks.get(0);
    }

    public Task getTaskByInvocationId(String wid, int invocationId) throws VipException {
        List<Task> tasks = listJobs(wid);
        for (Task task : tasks) {
            if (task.getInvocationID() == invocationId) {
                return task;
            }
        }
        throw new VipException("Job with invocationId " + invocationId + " not found for workflow " + wid);
    }

    public String readExecutionStdout(String wid) throws VipException {
        return simulationBusiness.readFile(wid, "", "workflow", ".out");
    }

    public String readExecutionStderr(String wid) throws VipException {
        return simulationBusiness.readFile(wid, "", "workflow", ".err");
    }

    public String readTaskLog(Task task, String wid, JobLogType type) throws VipException {
        return simulationBusiness.readFile(wid, type.getFolder(), task.getFileName(), type.getExtension());
    }
}
