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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
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

    public void closeConnection(String workflowID);

    public void killWorkflow(String workflowID);
    
    public void killWorkflows(List<String> simulationIDs) throws ApplicationException;

    public void cleanWorkflow(String workflowID, String userDN, String proxyFileName);
    
    public void cleanWorkflows(List<String> simulationIDs, String userDN, String proxyFileName) throws ApplicationException;

    public void purgeWorkflow(String workflowID);
    
    public void purgeWorkflows(List<String> simulationIDs) throws ApplicationException;
    
    public List<Simulation> getWorkflows(String user, String application, String status, Date startDate, Date endDate);

    public String getFile(String baseDir, String fileName);

    public String getFileURL(String baseDir, String fileName);

    public List<String>[] getApplicationsAndUsersList(String applicationClass);
    
    public List<String> getLogs(String baseDir);

    public List<Source> getWorkflowSources(String user, String proxyFileName, String workflowName) throws ApplicationException;

    public String getWorkflowInputs(String fileName);

    public String launchWorkflow(String user, Map<String, String> parameters, String workflowName, String proxyFileName, String simulationName) throws ApplicationException;

    public List<SimulationInput> getWorkflowsInputByUser(String user);
    
    public List<SimulationInput> getWorkflowsInputByUserAndAppName(String user, String appName);

    public void addSimulationInput(String user, SimulationInput workflowInput) throws ApplicationException;
    
    public void updateSimulationInput(String user, SimulationInput workflowInput) throws ApplicationException;

    public SimulationInput getInputByNameUserApp(String user, String inputName, String appName) throws ApplicationException;

    public void removeWorkflowInput(String user, String inputName, String application);

    public List<String> getStats( List<Simulation> workflowIdList, int type, int binSize);
    
    public List<InOutData> getOutputData(String simulationID) throws ApplicationException;
    
    public List<InOutData> getInputData(String simulationID) throws ApplicationException;
}
