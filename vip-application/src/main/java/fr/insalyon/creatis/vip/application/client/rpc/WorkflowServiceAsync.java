package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.core.client.bean.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface WorkflowServiceAsync {

    public void getSimulations(AsyncCallback<List<Simulation>> asyncCallback);
    
    public void getSimulations(Date lastDate, AsyncCallback<List<Simulation>> asyncCallback);

    public void getApplicationDescriptorString(String applicationName, String applicationVersion, AsyncCallback<String> asyncCallback);

    public void getApplicationsDescriptorsString(List<Pair<String, String>> applications, AsyncCallback<List<String>> asyncCallback);

    public void launchSimulation(Map<String, String> parameters, String applicationName, String applicationVersion, String applicationClass, String simulationName, AsyncCallback<Void> asyncCallback);

    public void getInputByNameUserApp(String inputName, String appName, AsyncCallback<SimulationInput> asyncCallback);

    public void addSimulationInput(SimulationInput simulationInput, AsyncCallback<Void> asyncCallback);

    public void updateSimulationInput(SimulationInput simulationInput, AsyncCallback<Void> asyncCallback);

    public void removeSimulationInput(String inputName, String applicationName, AsyncCallback<Void> asyncCallback);

    public void getSimulationInputByUser(AsyncCallback<List<SimulationInput>> asyncCallback);
    
    public void saveInputsAsExamples(SimulationInput simulationInput, AsyncCallback<Void> asyncCallback);
    
    public void getSimulationInputExamples(String applicationName, AsyncCallback<List<SimulationInput>> asyncCallback);
    
    public void removeSimulationInputExample(String inputName, String applicationName, AsyncCallback<Void> asyncCallback);

    public void killSimulations(List<String> simulationIDs, AsyncCallback<Void> asyncCallback);
    
    public void markSimulationsCompleted(List<String> simulationIDs, AsyncCallback<Void> asyncCallback);

    public void cleanSimulations(List<String> simulationIDs, AsyncCallback<Void> asyncCallback);

    public void purgeSimulations(List<String> simulationIDs, AsyncCallback<Void> asyncCallback);

    public void killWorkflow(String simulationID, AsyncCallback<Void> asyncCallback);

    public void cleanWorkflow(String simulationID, AsyncCallback<Void> asyncCallback);

    public void purgeWorkflow(String simulationID, AsyncCallback<Void> asyncCallback);
    
    public void markWorkflowCompleted(String simulationID, AsyncCallback<Void> asyncCallback);
    
    public void relaunchSimulation(String simulationID, AsyncCallback<Map<String, String>> asyncCallback);

    public void getSimulations(String user, String application, String status, Date startDate, Date endDate, AsyncCallback<List<Simulation>> asyncCallback);
    
    public void getSimulation(String simulationID, AsyncCallback<Simulation> asyncCallback);

    public void getFile(String baseDir, String fileName, AsyncCallback<String> asyncCallback);

    public void getLogs(String baseDir, AsyncCallback<List<String>> asyncCallback);

    public void deleteLogData(String path, AsyncCallback<Void> asyncCallback);

    public void getWorkflowsInputByUserAndAppName(String user, String appName, AsyncCallback<List<SimulationInput>> asyncCallback);

    public void getFileURL(String baseDir, String fileName, AsyncCallback<String> asyncCallback);

    public void getPerformanceStats(List<Simulation> simulationList, int type, AsyncCallback<List<String>> asyncCallback);

    public void getOutputData(String simulationID, AsyncCallback<List<InOutData>> asyncCallback);

    public void getInputData(String simulationID, AsyncCallback<List<InOutData>> asyncCallback);
    
    public void getProcessors(String simulationID, AsyncCallback<List<Activity>> asyncCallback);
    
    public void validateInputs(List<String> inputs, AsyncCallback<Void> asyncCallback);
    
    public void updateUser(String currentUser, String newUser, AsyncCallback<Void> asyncCallback);
    
    public void changeSimulationUser(String simulationId, String user, AsyncCallback<Void> asyncCallback);
}
