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
package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
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

    //
    public Descriptor getApplicationDescriptor(String applicationName, String applicationVersion) throws ApplicationException;
    public String getApplicationDescriptorString(String applicationName, String applicationVersion) throws ApplicationException;

    public void launchSimulation(Map<String, String> parameters, String applicationName, String applicationVersion, String applicationClass, String simulationName) throws ApplicationException;

    public SimulationInput getInputByNameUserApp(String inputName, String appName) throws ApplicationException;

    public void addSimulationInput(SimulationInput simulationInput) throws ApplicationException;

    public void updateSimulationInput(SimulationInput simulationInput) throws ApplicationException;

    public String loadSimulationInput(String fileName) throws ApplicationException;

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

    public List<Simulation> getSimulations(String user, String application, String status, String appClass, Date startDate, Date endDate) throws ApplicationException;

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
