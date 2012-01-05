/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Processor;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public interface WorkflowServiceAsync {

    public void getApplicationDescriptor(String applicationName, AsyncCallback<Descriptor> asyncCallback);

    public void launchSimulation(Map<String, String> parameters, String applicationName, String simulationName, AsyncCallback<Void> asyncCallback);

    public void getInputByNameUserApp(String inputName, String appName, AsyncCallback<SimulationInput> asyncCallback);

    public void addSimulationInput(SimulationInput simulationInput, AsyncCallback<Void> asyncCallback);

    public void updateSimulationInput(SimulationInput simulationInput, AsyncCallback<Void> asyncCallback);

    public void loadSimulationInput(String fileName, AsyncCallback<String> asyncCallback);

    public void removeSimulationInput(String inputName, String applicationName, AsyncCallback<Void> asyncCallback);

    public void getSimulationInputByUser(AsyncCallback<List<SimulationInput>> asyncCallback);

    public void killSimulations(List<String> simulationIDs, AsyncCallback<Void> asyncCallback);

    public void cleanSimulations(List<String> simulationIDs, AsyncCallback<Void> asyncCallback);

    public void purgeSimulations(List<String> simulationIDs, AsyncCallback<Void> asyncCallback);

    public void killWorkflow(String simulationID, AsyncCallback<Void> asyncCallback);

    public void cleanWorkflow(String simulationID, AsyncCallback<Void> asyncCallback);

    public void purgeWorkflow(String simulationID, AsyncCallback<Void> asyncCallback);

    public void getSimulations(String user, String application, String status, Date startDate, Date endDate, AsyncCallback<List<Simulation>> asyncCallback);
    
    public void getSimulation(String simulationID, AsyncCallback<Simulation> asyncCallback);

    public void getFile(String baseDir, String fileName, AsyncCallback<String> asyncCallback);

    public void getLogs(String baseDir, AsyncCallback<List<String>> asyncCallback);

    public void deleteLogData(String path, AsyncCallback<Void> asyncCallback);

    public void closeConnection(String workflowID, AsyncCallback<Void> asyncCallback);

    public void getWorkflowsInputByUserAndAppName(String user, String appName, AsyncCallback<List<SimulationInput>> asyncCallback);

    public void getFileURL(String baseDir, String fileName, AsyncCallback<String> asyncCallback);

    public void getPerformanceStats(List<Simulation> simulationList, int type, AsyncCallback<String> asyncCallback);

    public void getOutputData(String simulationID, AsyncCallback<List<InOutData>> asyncCallback);

    public void getInputData(String simulationID, AsyncCallback<List<InOutData>> asyncCallback);
    
    public void getProcessors(String simulationID, AsyncCallback<List<Processor>> asyncCallback);
    
    public void validateInputs(List<String> inputs, AsyncCallback<Void> asyncCallback);
    
    public void updateUser(String currentUser, String newUser, AsyncCallback<Void> asyncCallback);
}
