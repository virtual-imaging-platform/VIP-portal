/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
import fr.insalyon.creatis.vip.application.client.bean.Workflow;
import fr.insalyon.creatis.vip.application.client.bean.WorkflowInput;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public interface WorkflowServiceAsync {

    public void getFile(String baseDir, String fileName, AsyncCallback<String> asyncCallback);

    public void getApplicationsAndUsersList(String applicationClass, AsyncCallback<List<String>[]> asyncCallback);

    public void getWorkflows(String user, String application, String status, Date startDate, Date endDate, AsyncCallback<List<Workflow>> asyncCallback);
    
    public void getLogs(String baseDir, AsyncCallback<List<String>> asyncCallback);

    public void getWorkflowSources(String user, String proxyFileName, String workflowName, AsyncCallback<List<String>> asyncCallback);

    public void launchWorkflow(String user, Map<String, String> parameters, String workflowName, String proxyFileName, AsyncCallback<String> asyncCallback);

    public void getWorkflowInputs(String fileName, AsyncCallback<Map<String, String>> asyncCallback);

    public void addWorkflowInput(String user, WorkflowInput workflowInput, AsyncCallback<String> asyncCallback);

    public void closeConnection(String workflowID, AsyncCallback<Void> asyncCallback);

    public void killWorkflow(String workflowID, AsyncCallback<Void> asyncCallback);

    public void cleanWorkflow(String workflowID, String userDN, String proxyFileName, AsyncCallback<Void> asyncCallback);

    public void getWorkflowsInputByUser(String user, AsyncCallback<List<WorkflowInput>> asyncCallback);
    
    public void getWorkflowsInputByUserAndAppName(String user, String appName, AsyncCallback<List<WorkflowInput>> asyncCallback);

    public void getWorkflowInputByUserAndName(String user, String inputName, AsyncCallback<WorkflowInput> asyncCallback);

    public void removeWorkflowInput(String user, String inputName, AsyncCallback<Void> asyncCallback);

    public void getFileURL(String baseDir, String fileName, AsyncCallback<String> asyncCallback);

    public void getStats( List<Workflow> workflowIdList, int type, int binSize, AsyncCallback<List<String>> asyncCallback);

    public void purgeWorkflow(String workflowID, AsyncCallback<Void> asyncCallback);
}
