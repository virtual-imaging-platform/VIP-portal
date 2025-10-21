package fr.insalyon.creatis.vip.application.client.rpc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.application.models.Activity;
import fr.insalyon.creatis.vip.application.models.InOutData;
import fr.insalyon.creatis.vip.application.models.Simulation;
import fr.insalyon.creatis.vip.application.models.SimulationInput;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.Pair;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface WorkflowService extends RemoteService {

    public static final String SERVICE_URI = "/workflowservice";

    public static class Util {

        public static WorkflowServiceAsync getInstance() {

            WorkflowServiceAsync instance = (WorkflowServiceAsync) GWT.create(WorkflowService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public List<Simulation> getSimulations() throws VipException;
    
    public List<Simulation> getSimulations(Date lastDate) throws VipException;

    public String getApplicationDescriptorString(String applicationName, String applicationVersion) throws VipException;

    public List<String> getApplicationsDescriptorsString(List<Pair<String, String>> applications) throws VipException;

    public void launchSimulation(Map<String, String> parameters, String applicationName, String applicationVersion, String applicationClass, String simulationName) throws VipException;

    public SimulationInput getInputByNameUserApp(String inputName, String appName) throws VipException;

    public void addSimulationInput(SimulationInput simulationInput) throws VipException;

    public void updateSimulationInput(SimulationInput simulationInput) throws VipException;

    public void removeSimulationInput(String inputName, String applicationName) throws VipException;

    public List<SimulationInput> getSimulationInputByUser() throws VipException;

    public void saveInputsAsExamples(SimulationInput simulationInput) throws VipException;

    public List<SimulationInput> getSimulationInputExamples(String applicationName) throws VipException;

    public void removeSimulationInputExample(String inputName, String applicationName) throws VipException;

    public void killSimulations(List<String> simulationIDs) throws VipException;

    public void cleanSimulations(List<String> simulationIDs) throws VipException;

    public void purgeSimulations(List<String> simulationIDs) throws VipException;
    
    public void markSimulationsCompleted(List<String> simulationIDs) throws VipException;

    public void killWorkflow(String simulationID) throws VipException;

    public void cleanWorkflow(String simulationID) throws VipException;

    public void purgeWorkflow(String simulationID) throws VipException;
    
    public void markWorkflowCompleted(String simulationID) throws VipException;

    public Map<String, String> relaunchSimulation(String simulationID) throws VipException;

    public List<Simulation> getSimulations(String user, String application, String status, Date startDate, Date endDate) throws VipException;

    public Simulation getSimulation(String simulationID) throws VipException;

    public String getFile(String baseDir, String fileName);

    public String getFileURL(String baseDir, String fileName);

    public List<String> getLogs(String baseDir);

    public void deleteLogData(String path) throws VipException;

    public List<SimulationInput> getWorkflowsInputByUserAndAppName(String user, String appName);

    public List<String> getPerformanceStats(List<Simulation> simulationList, int type) throws VipException;

    public List<InOutData> getOutputData(String simulationID) throws VipException;

    public List<InOutData> getInputData(String simulationID) throws VipException;

    public List<Activity> getProcessors(String simulationID) throws VipException;

    public void validateInputs(List<String> inputs) throws VipException;

    public void updateUser(String currentUser, String newUser) throws VipException;
    
    public void changeSimulationUser(String simulationId, String user) throws VipException;
}
