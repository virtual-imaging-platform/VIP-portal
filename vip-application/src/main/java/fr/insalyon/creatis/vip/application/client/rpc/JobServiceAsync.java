package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.application.client.view.monitor.job.SimulationFileType;
import fr.insalyon.creatis.vip.application.models.Job;
import fr.insalyon.creatis.vip.application.models.Node;
import fr.insalyon.creatis.vip.application.models.Task;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface JobServiceAsync {

    public void getList(String simulationID, AsyncCallback<List<Job>> asyncCallback);

    public void getTasks(String simulationID, int jobID, AsyncCallback<List<Task>> asyncCallback);

    public void readSimulationFile(String simulationID, String taskID, SimulationFileType fileType, AsyncCallback<String[]> asyncCallback);
    
    public void sendTaskSignal(String simulationID, String taskID, String status, AsyncCallback<Void> asyncCallback);
    
    //
    public void getJobsList(String simulationID, AsyncCallback<List<Task>> asyncCallback);

    public void readFile(String simulationID, String dir, String fileName, String ext, AsyncCallback<String> asyncCallback);

    public void getExecutionPerNumberOfJobs(String simulationID, int binSize, AsyncCallback<List<String>> asyncCallback);

    public void getDownloadPerNumberOfJobs(String simulationID, int binSize, AsyncCallback<List<String>> asyncCallback);

    public void getUploadPerNumberOfJobs(String simulationID, int binSize, AsyncCallback<List<String>> asyncCallback);

    public void getJobFlow(String simulationID, AsyncCallback<List<String>> asyncCallback);

    public void getCkptsPerJob(String simulationID, AsyncCallback<List<String>> asyncCallback);

    public void getSiteHistogram(String simulationID, AsyncCallback<List<String>> asyncCallback);

    public void getNode(String simulationID, String siteName, String nodeName, AsyncCallback<Node> asyncCallback);

    public void sendSignal(String simulationID, String jobID, String status, AsyncCallback<Void> asyncCallback);

    public void sendSignal(String simulationID, List<String> jobIDs, String status, AsyncCallback<Void> asyncCallback);

    public void getCountriesMap(String simulationID, AsyncCallback<Map<String, Integer>> asyncCallback);
}
