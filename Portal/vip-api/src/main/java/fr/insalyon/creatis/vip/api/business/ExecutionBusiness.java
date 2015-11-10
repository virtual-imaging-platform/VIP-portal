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
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOFactory;
import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.bean.Execution.ExecutionStatus;
import fr.insalyon.creatis.vip.api.bean.ParameterTypedValue;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Tristan Glatard
 */
public class ExecutionBusiness extends ApiBusiness {

    public ExecutionBusiness(WebServiceContext wsContext) throws ApiException {
        super(wsContext,true);
    }

    public Execution getExecution(String executionId) throws ApiException {
        try {
            checkIfUserIsOwnerOrAdmin(executionId);

            WorkflowBusiness wb = new WorkflowBusiness();
            SimulationBusiness sb = new SimulationBusiness();

            // Get main execution object
            Simulation s = wb.getSimulation(executionId);
            if (!s.getUserName().equals(getUser().getEmail()) && !getUser().isSystemAdministrator()) {
                throw new ApiException("Execution id '" + executionId + "' is not available or user '" + getUser().getEmail() + "' cannot access it.");
            }

            // Retrieve stdout and stderr.
            // Would be nice if stdout and stderr are requested through another call since reading these files can be costly.
            String stdout = sb.readFile(s.getID(), "", "workflow", ".out");
            String stderr = sb.readFile(s.getID(), "", "workflow", ".err");

            // Build Carmin's execution object
            Execution e = new Execution(
                    s.getID(),
                    s.getSimulationName(),
                    PipelineBusiness.getPipelineIdentifier(s.getApplicationName(), s.getApplicationVersion()),
                    0, // timeout (no timeout set in VIP)
                    VIPtoCarminStatus(s.getStatus()),
                    null, // study identifier (not available in VIP yet)
                    null, // error codes and mesasges (not available in VIP yet)
                    stdout,
                    stderr,
                    s.getDate().getTime(),
                    null // last status modification date (not available in VIP yet)
            );

            // Inputs
            List<InOutData> inputs = wb.getInputData(executionId, getUser().getFolder());
            for (InOutData iod : inputs) {
                ParameterTypedValue value = new ParameterTypedValue(PipelineBusiness.getCarminType(iod.getType()), iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(), value);
                e.getInputValues().add(skpv);
            }

            // Outputs
            List<InOutData> outputs = wb.getOutputData(executionId, getUser().getFolder());
            for (InOutData iod : outputs) {
                ParameterTypedValue value = new ParameterTypedValue(PipelineBusiness.getCarminType(iod.getType()), iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(), value);
                e.getReturnedFiles().add(skpv);
            }

            return e;

        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

    public void updateExecution(String executionId, ArrayList<StringKeyValuePair> values) throws ApiException {
        try {
            checkIfUserIsOwnerOrAdmin(executionId);
            WorkflowDAO wd = WorkflowsDBDAOFactory.getInstance().getWorkflowDAO();
            Workflow w = wd.get(executionId);
            for(StringKeyValuePair skvp : values)
                if(!skvp.getName().equals("name")) // in the current spec, update can only deal with the timeout (unsupported here) or the name.
                    throw new ApiException("Update of parameter "+skvp.getName()+" is not supported.");
                else
                    w.setDescription(skvp.getValue().toString());
            wd.update(w);
        } catch (WorkflowsDBDAOException ex) {
            Logger.getLogger(ExecutionBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String initExecution(String pipelineId,
                                ArrayList<StringKeyParameterValuePair> inputValues,
                                Integer timeoutInSeconds,
                                String executionName,
                                String studyId,
                                Boolean playExecution) throws ApiException {
        try {
            // We cannot easily initialize an execution without starting it.
            // So we will just launch the execution, and launch an error in case playExecution is not true.

            if (!playExecution) {
                throw new ApiException("Cannot initialize an execution without starting it.");
            }

            checkIfUserCanLaunch(pipelineId);

            WorkflowBusiness wb = new WorkflowBusiness();
            Map<String, String> pm = new HashMap<>();
            for (StringKeyParameterValuePair skpvp : inputValues) {
                pm.put(skpvp.getName(), skpvp.getValue().getValue());
            }

            ConfigurationBusiness cb = new ConfigurationBusiness();
            List<String> groupNames = new ArrayList<>();
            for (Group g : cb.getUserGroups(getUser().getEmail()).keySet()) {
                groupNames.add(g.getName());
            }
            String executionId = wb.launch(
                    getUser(),
                    groupNames,
                    pm,
                    PipelineBusiness.getApplicationName(pipelineId),
                    PipelineBusiness.getApplicationVersion(pipelineId),
                    studyId, // application class
                    executionName
            );
            return executionId;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public ExecutionStatus playExecution(String executionId) throws ApiException {
        try {
            checkIfUserIsOwnerOrAdmin(executionId);
            // Execution cannot be "played" (i.e. started) because it was already started in initExecution method.
            // So we just return the execution status.
            WorkflowBusiness wb = new WorkflowBusiness();
            return VIPtoCarminStatus(wb.getSimulation(executionId).getStatus());
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void killExecution(String executionId) throws ApiException {
        checkIfUserIsOwnerOrAdmin(executionId);
        WorkflowBusiness wb = new WorkflowBusiness();
        try {
            wb.kill(executionId);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void deleteExecution(String executionId, Boolean deleteFiles) throws ApiException {
        checkIfUserIsOwnerOrAdmin(executionId);
        WorkflowBusiness wb = new WorkflowBusiness();
        try {
            Simulation s = wb.getSimulation(executionId);
            if (s.getStatus() != SimulationStatus.Completed && s.getStatus() != SimulationStatus.Killed) {
                throw new ApiException("Cannot delete execution " + executionId + " because status is " + s.getStatus().toString());
            }
            // Note: this won't delete the intermediate files in case the execution was run locally, which violates the spec.
            // Purge should be called in that case but purge also violates the spec.
            wb.clean(executionId, getUser().getEmail(), deleteFiles);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

    ;
    public List<URL> getExecutionResults(String executionId, String protocol) throws ApiException {
        try {
            if(!protocol.equals("https") && !protocol.equals("http"))
                throw new ApiException("Unsupported protocol: "+protocol);
            
            checkIfUserIsOwnerOrAdmin(executionId);
            
            List<URL> urls = new ArrayList<>();
            
            TransferPoolBusiness tpb = new TransferPoolBusiness();
            WorkflowBusiness wb = new WorkflowBusiness();
            List<InOutData> outputs = wb.getOutputData(executionId, getUser().getFolder());
            for(InOutData output : outputs){
                String operationId = tpb.downloadFile(getUser(), output.getPath());
                String url = getRequest().getRequestURI() + "../filedownloadservice?operationid="+operationId; // assuming that the api is deployed at /api
                urls.add(new URL(url));
            }
            return urls;
        } catch (BusinessException | MalformedURLException ex) {
            throw new ApiException(ex);
        }
    }
    
    // static methods
    
    public static ExecutionStatus VIPtoCarminStatus(SimulationStatus s) {

        switch (s) {
            case Running:
                return ExecutionStatus.Running;
            case Completed:
                return ExecutionStatus.Finished;
            case Killed:
                return ExecutionStatus.Killed;
            case Cleaned:
                return ExecutionStatus.Unknown;
            case Queued:
                return ExecutionStatus.Ready;
            case Unknown:
                return ExecutionStatus.Unknown;
            default:
                return ExecutionStatus.Unknown;
        }
    }

    // private methods
    
    private void checkIfUserIsOwnerOrAdmin(String executionId) throws ApiException {
        try {
            User user = getUser();
            if (user.isSystemAdministrator()) {
                return;
            }
            WorkflowBusiness wb = new WorkflowBusiness();
            Simulation s = wb.getSimulation(executionId);
            if (s.getUserName().equals(user.getEmail())) {
                return;
            }
            throw new ApiException("Permission denied");
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

    private void checkIfUserCanLaunch(String pipelineId) throws ApiException {
        try {
            ClassBusiness cb = new ClassBusiness();
            ApplicationBusiness ab = new ApplicationBusiness();
            
            String applicationName = PipelineBusiness.getApplicationName(pipelineId);
            List<String> userClassNames = cb.getUserClassesName(getUser().getEmail(), false);
            
            Application a = ab.getApplication(applicationName);
            for (String applicationClassName : a.getApplicationClasses()) {
                if (userClassNames.contains(applicationClassName)) {
                    return;
                }
            }
            throw new ApiException("User " + getUser().getEmail() + " not allowed to launch application " + a.getName());
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

}
