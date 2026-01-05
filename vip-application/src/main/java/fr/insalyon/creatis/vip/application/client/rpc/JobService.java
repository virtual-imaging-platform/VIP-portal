package fr.insalyon.creatis.vip.application.client.rpc;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.application.client.view.monitor.job.SimulationFileType;
import fr.insalyon.creatis.vip.application.models.Job;
import fr.insalyon.creatis.vip.application.models.Node;
import fr.insalyon.creatis.vip.application.models.Task;
import fr.insalyon.creatis.vip.core.client.VipException;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface JobService extends RemoteService {

    public static final String SERVICE_URI = "/jobservice";

    public static class Util {

        public static JobServiceAsync getInstance() {

            JobServiceAsync instance = (JobServiceAsync) GWT.create(JobService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public List<Job> getList(String simulationID) throws VipException;

    public List<Task> getTasks(String simulationID, int jobID) throws VipException;

    public String[] readSimulationFile(String simulationID, String taskID, SimulationFileType fileType) throws VipException;

    public void sendTaskSignal(String simulationID, String taskID, String status) throws VipException;

    //
    public List<Task> getJobsList(String simulationID) throws VipException;

    public String readFile(String simulationID, String dir, String fileName, String ext) throws VipException;

    public List<String> getExecutionPerNumberOfJobs(String simulationID, int binSize) throws VipException;

    public List<String> getDownloadPerNumberOfJobs(String simulationID, int binSize) throws VipException;

    public List<String> getUploadPerNumberOfJobs(String simulationID, int binSize) throws VipException;

    public List<String> getJobFlow(String simulationID) throws VipException;

    public List<String> getCkptsPerJob(String simulationID) throws VipException;

    public List<String> getSiteHistogram(String simulationID) throws VipException;

    public Node getNode(String simulationID, String siteName, String nodeName) throws VipException;

    public void sendSignal(String simulationID, String jobID, String status) throws VipException;

    public void sendSignal(String simulationID, List<String> jobIDs, String status) throws VipException;

    public Map<String, Integer> getCountriesMap(String simulationID) throws VipException;
}
