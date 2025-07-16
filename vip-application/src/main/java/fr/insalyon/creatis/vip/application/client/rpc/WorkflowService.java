package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.core.client.bean.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public List<Simulation> getSimulations() throws ApplicationException;
    
    public List<Simulation> getSimulations(Date lastDate) throws ApplicationException;

    public String getApplicationDescriptorString(String applicationName, String applicationVersion) throws ApplicationException;

    public List<String> getApplicationsDescriptorsString(List<Pair<String, String>> applications) throws ApplicationException;

    public void launchSimulation(Map<String, String> parameters, String applicationName, String applicationVersion, String applicationClass, String simulationName) throws ApplicationException;

    public SimulationInput getInputByNameUserApp(String inputName, String appName) throws ApplicationException;

    public void addSimulationInput(SimulationInput simulationInput) throws ApplicationException;

    public void updateSimulationInput(SimulationInput simulationInput) throws ApplicationException;

    public void removeSimulationInput(String inputName, String applicationName) throws ApplicationException;

    public List<SimulationInput> getSimulationInputByUser() throws ApplicationException;

    public void saveInputsAsExamples(SimulationInput simulationInput) throws ApplicationException;

    public List<SimulationInput> getSimulationInputExamples(String applicationName) throws ApplicationException;

    public void removeSimulationInputExample(String inputName, String applicationName) throws ApplicationException;

    public void killSimulations(List<String> simulationIDs) throws ApplicationException;

    public void cleanSimulations(List<String> simulationIDs) throws ApplicationException;

    public void purgeSimulations(List<String> simulationIDs) throws ApplicationException;
    
    public void markSimulationsCompleted(List<String> simulationIDs) throws ApplicationException;

    public void killWorkflow(String simulationID) throws ApplicationException;

    public void cleanWorkflow(String simulationID) throws ApplicationException;

    public void purgeWorkflow(String simulationID) throws ApplicationException;
    
    public void markWorkflowCompleted(String simulationID) throws ApplicationException;

    public Map<String, String> relaunchSimulation(String simulationID) throws ApplicationException;

    public List<Simulation> getSimulations(String user, String application, String status, Date startDate, Date endDate) throws ApplicationException;

    public Simulation getSimulation(String simulationID) throws ApplicationException;

    public String getFile(String baseDir, String fileName);

    public String getFileURL(String baseDir, String fileName);

    public List<String> getLogs(String baseDir);

    public void deleteLogData(String path) throws ApplicationException;

    public List<SimulationInput> getWorkflowsInputByUserAndAppName(String user, String appName);

    public List<String> getPerformanceStats(List<Simulation> simulationList, int type) throws ApplicationException;

    public List<InOutData> getOutputData(String simulationID) throws ApplicationException;

    public List<InOutData> getInputData(String simulationID) throws ApplicationException;

    public List<Activity> getProcessors(String simulationID) throws ApplicationException;

    public void validateInputs(List<String> inputs) throws ApplicationException;

    public void updateUser(String currentUser, String newUser) throws ApplicationException;
    
    public void changeSimulationUser(String simulationId, String user) throws ApplicationException;
}
