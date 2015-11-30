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
import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.bean.PipelineParameter;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
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

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExecutionBusiness.class);

    public ExecutionBusiness(WebServiceContext wsContext) throws ApiException {
        super(wsContext, true);
    }

    public ExecutionBusiness(ApiBusiness ab) {
        super(ab);
    }

    public String getStdOut(String executionId) throws ApiException {
        try {
            WorkflowBusiness wb = new WorkflowBusiness();
            SimulationBusiness sb = new SimulationBusiness();
            Simulation s = wb.getSimulation(executionId);
            String stdout = sb.readFile(s.getID(), "", "workflow", ".out");
            return stdout;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public String getStdErr(String executionId) throws ApiException {
        try {
            WorkflowBusiness wb = new WorkflowBusiness();
            SimulationBusiness sb = new SimulationBusiness();
            Simulation s = wb.getSimulation(executionId);
            String stderr = sb.readFile(s.getID(), "", "workflow", ".err");
            return stderr;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public Execution getExecution(String executionId, boolean summarize) throws ApiException {
        try {
            WorkflowBusiness wb = new WorkflowBusiness();

            // Get main execution object
            Simulation s = wb.getSimulation(executionId);

            // Return null if execution doesn't exist or is cleaned (cleaned status is not supported in Carmin)
            if (s == null || s.getStatus() == SimulationStatus.Cleaned) {
                return null;
            }

            // Build Carmin's execution object
            Execution e = new Execution(
                    s.getID(),
                    s.getSimulationName(),
                    ApiUtils.getPipelineIdentifier(s.getApplicationName(), s.getApplicationVersion()),
                    0, // timeout (no timeout set in VIP)
                    VIPtoCarminStatus(s.getStatus()),
                    null, // study identifier (not available in VIP yet)
                    null, // error codes and mesasges (not available in VIP yet)
                    s.getDate().getTime(),
                    null // last status modification date (not available in VIP yet)
            );

            if(summarize) // don't look into inputs and outputs
                return e;
            
            // Inputs
            List<InOutData> inputs = wb.getInputData(executionId, getUser().getFolder());
            logger.info("Execution has " + inputs.size() + " inputs ");
            for (InOutData iod : inputs) {
                ParameterTypedValue value = new ParameterTypedValue(ApiUtils.getCarminType(iod.getType()), iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(), value);
                logger.info("Adding input " + skpv.toString());
                e.getInputValues().add(skpv);
            }

            // Outputs
            List<InOutData> outputs = wb.getOutputData(executionId, getUser().getFolder());
            for (InOutData iod : outputs) {
                ParameterTypedValue value = new ParameterTypedValue(ApiUtils.getCarminType(iod.getType()), iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(), value);
                e.getReturnedFiles().add(skpv);
            }

            if (!(e.getStatus() == Execution.ExecutionStatus.Finished) && !(e.getStatus() == Execution.ExecutionStatus.Killed) && e.getReturnedFiles().isEmpty()) {
                e.clearReturnedFiles();
            }

            return e;

        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

    public Execution[] listExecutions(int maxReturned) throws ApiException {
        try {
            
            WorkflowBusiness wb = new WorkflowBusiness();
            List<Simulation> simulations = wb.getSimulations(
                    getUser().getFullName(),
                    null, // application
                    null, // status
                    null, // class
                    null, // startDate
                    null // endDate
            );
            logger.info("Found "+simulations.size()+" simulations.");
            ArrayList<Execution> executions = new ArrayList<>();
            int count = 0;
            for (Simulation s : simulations) {
                if (!(s == null) && !(s.getStatus() == SimulationStatus.Cleaned)) {
                    count++;
                    executions.add(getExecution(s.getID(),true));
                    if(count >= maxReturned){
                        getWarnings().add("Only the "+maxReturned+" most recent pipelines were returned.");
                        break;
                    }
                }
            }
            logger.info("Returning " + executions.size() + " executions");
            Execution[] array_executions = new Execution[executions.size()];
            return executions.toArray(array_executions);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void updateExecution(String executionId, ArrayList<StringKeyValuePair> values) throws ApiException {
        try {
            WorkflowDAO wd = WorkflowsDBDAOFactory.getInstance().getWorkflowDAO();
            Workflow w = wd.get(executionId);
            for (StringKeyValuePair skvp : values) {
                if (!skvp.getName().equals("name")) // in the current spec, update can only deal with the timeout (unsupported here) or the name.
                {
                    throw new ApiException("Update of parameter " + skvp.getName() + " is not supported.");
                } else {
                    if (skvp.getValue() == null) {
                        throw new ApiException("Value of parameter " + skvp.getName() + " is empty.");
                    }
                    logger.info("Updating parameter " + skvp.getName() + " with value \"" + skvp.getValue().toString() + "\"");
                    w.setDescription(skvp.getValue().toString());
                }
            }
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
            // Set warnings
            if (studyId != null) {
                getWarnings().add("Study identifier was ignored.");
            }
            if (timeoutInSeconds != null && timeoutInSeconds != 0) {
                getWarnings().add("Timeout value (" + timeoutInSeconds.toString() + ") was ignored.");
            }

            // Build input parameter map
            WorkflowBusiness wb = new WorkflowBusiness();
            Map<String, String> pm = new HashMap<>();
            for (StringKeyParameterValuePair skpvp : inputValues) {
                logger.info("Adding value " + skpvp.getValue().getValue() + " to input " + skpvp.getName());
                pm.put(skpvp.getName(), skpvp.getValue().getValue());
            }

            // Check that all pipeline inputs are present
            PipelineBusiness pb = new PipelineBusiness(this);
            Pipeline p = pb.getPipeline(pipelineId);
            for (PipelineParameter pp : p.getParameters()) {
                if (pp.isReturnedValue()) {
                    continue;
                }
                // pp is an input
                if (!(pm.get(pp.getName()) == null)) {
                    continue;
                }
                // pp is an empty input
                if (pp.getDefaultValue() != null) {
                    pm.put(pp.getName(), pp.getDefaultValue().getValue());
                    continue;
                }
                // pp is an empty input with no default value
                if (pp.isOptional()) {
                    pm.put("no", pp.getDefaultValue().getValue());//that's how optional values are handled in VIP
                    continue;
                }
                // pp is an empty input with no default value and it is not optional
                throw new ApiException("Parameter " + pp.getName() + " is empty while it is not optional and it has no default value.");
            }

            // Get user groups
            ConfigurationBusiness cb = new ConfigurationBusiness();
            List<String> groupNames = new ArrayList<>();
            for (Group g : cb.getUserGroups(getUser().getEmail()).keySet()) {
                groupNames.add(g.getName());
            }

            // Get application name and version
            String applicationName = ApiUtils.getApplicationName(pipelineId);
            String applicationVersion = ApiUtils.getApplicationVersion(pipelineId);

            // Get application classes
            ApplicationBusiness ab = new ApplicationBusiness();
            List<String> classes = ab.getApplication(applicationName).getApplicationClasses();
            if (classes.isEmpty()) {
                throw new ApiException("Application " + applicationName + " cannot be launched because it doesn't belong to any VIP class.");
            }

            logger.info("Launching workflow with the following parameters: ");
            logger.info(getUser());
            logger.info(groupNames);
            logger.info(pm);
            logger.info(applicationName);
            logger.info(applicationVersion);
            logger.info(classes.get(0));
            logger.info(executionName);

            // Launch the workflow
            String executionId = wb.launch(getUser(),
                                           groupNames,
                                           pm,
                                           applicationName,
                                           applicationVersion,
                                           classes.get(0),
                                           executionName
            );
            return executionId;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public ExecutionStatus playExecution(String executionId) throws ApiException {
        try {
            // Execution cannot be "played" (i.e. started) because it was already started in initExecution method.
            // So we just return the execution status.
            WorkflowBusiness wb = new WorkflowBusiness();
            getWarnings().add("In VIP, playExecution only returns the status of the execution.");
            return VIPtoCarminStatus(wb.getSimulation(executionId).getStatus());
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void killExecution(String executionId) throws ApiException {
        WorkflowBusiness wb = new WorkflowBusiness();
        try {
            wb.kill(executionId);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void deleteExecution(String executionId, Boolean deleteFiles) throws ApiException {
        checkIfUserCanAccessExecution(executionId);
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
    public String[] getExecutionResults(String executionId, String protocol) throws ApiException {
        try {
            if (protocol == null) {
                protocol = "https";
            }

            if (!protocol.equals("https") && !protocol.equals("http")) {
                throw new ApiException("Unsupported protocol: " + protocol);
            }

            ArrayList<String> urls = new ArrayList<>();

            TransferPoolBusiness tpb = new TransferPoolBusiness();
            WorkflowBusiness wb = new WorkflowBusiness();
            List<InOutData> outputs = wb.getOutputData(executionId, getUser().getFolder());
            for (InOutData output : outputs) {

                String operationId = tpb.downloadFile(getUser(), output.getPath());

                String url = getRequest().getRequestURL() + "/../fr.insalyon.creatis.vip.portal.Main/filedownloadservice?operationid=" + operationId;
                URL u = new URL(url); // just to check that it is a well-formed URL
                urls.add(url);
            }

            String[] array_urls = new String[urls.size()];
            return urls.toArray(array_urls);
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

    public void checkIfUserCanAccessExecution(String executionId) throws ApiException {
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

}
