package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.SimulationFileType;
import java.util.List;
import java.util.Map;

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

    public List<Job> getList(String simulationID) throws ApplicationException;

    public List<Task> getTasks(String simulationID, int jobID) throws ApplicationException;

    public String[] readSimulationFile(String simulationID, String taskID, SimulationFileType fileType) throws ApplicationException;

    public void sendTaskSignal(String simulationID, String taskID, String status) throws ApplicationException;

    //
    public List<Task> getJobsList(String simulationID) throws ApplicationException;

    public String readFile(String simulationID, String dir, String fileName, String ext) throws ApplicationException;

    public List<String> getExecutionPerNumberOfJobs(String simulationID, int binSize) throws ApplicationException;

    public List<String> getDownloadPerNumberOfJobs(String simulationID, int binSize) throws ApplicationException;

    public List<String> getUploadPerNumberOfJobs(String simulationID, int binSize) throws ApplicationException;

    public List<String> getJobFlow(String simulationID) throws ApplicationException;

    public List<String> getCkptsPerJob(String simulationID) throws ApplicationException;

    public List<String> getSiteHistogram(String simulationID) throws ApplicationException;

    public Node getNode(String simulationID, String siteName, String nodeName) throws ApplicationException;

    public void sendSignal(String simulationID, String jobID, String status) throws ApplicationException;

    public void sendSignal(String simulationID, List<String> jobIDs, String status) throws ApplicationException;

    public Map<String, Integer> getCountriesMap(String simulationID) throws ApplicationException;
}
