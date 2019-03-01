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

import fr.insalyon.creatis.vip.api.CarminProperties;
import fr.insalyon.creatis.vip.api.bean.*;
import fr.insalyon.creatis.vip.api.bean.pairs.*;
import fr.insalyon.creatis.vip.api.rest.model.PathProperties;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.client.bean.*;
import fr.insalyon.creatis.vip.core.server.business.*;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.Object;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Tristan Glatard
 */
@Service
public class ExecutionBusiness {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExecutionBusiness.class);

    private final ApiContext apiContext;
    private final SimulationBusiness simulationBusiness;
    private final WorkflowBusiness workflowBusiness;
    private final PipelineBusiness pipelineBusiness;
    private final ConfigurationBusiness configurationBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final TransferPoolBusiness transferPoolBusiness;

    // only used in a Spring context (aka from REST api, not SOAP)
    @Autowired
    private DataApiBusiness dataApiBusiness;
    @Autowired
    private Environment env;

    public ExecutionBusiness(ApiContext apiContext) {
        WorkflowBusiness workflowBusiness = new WorkflowBusiness();
        ApplicationBusiness applicationBusiness = new ApplicationBusiness();
        this.apiContext = apiContext;
        this.simulationBusiness = new SimulationBusiness();
        this.workflowBusiness = workflowBusiness;
        this.configurationBusiness = new ConfigurationBusiness();
        this.applicationBusiness = applicationBusiness;
        this.transferPoolBusiness = new TransferPoolBusiness();
        this.pipelineBusiness = new PipelineBusiness(apiContext, workflowBusiness, applicationBusiness, new ClassBusiness());
        this.dataApiBusiness = new DataApiBusiness();
    }

    @Autowired
    public ExecutionBusiness(ApiContext apiContext,
                             SimulationBusiness simulationBusiness,
                             WorkflowBusiness workflowBusiness,
                             ConfigurationBusiness configurationBusiness,
                             ApplicationBusiness applicationBusiness,
                             PipelineBusiness pipelineBusiness,
                             TransferPoolBusiness transferPoolBusiness) {
        this.apiContext = apiContext;
        this.simulationBusiness = simulationBusiness;
        this.workflowBusiness = workflowBusiness;
        this.configurationBusiness = configurationBusiness;
        this.applicationBusiness = applicationBusiness;
        this.pipelineBusiness = pipelineBusiness;
        this.transferPoolBusiness = transferPoolBusiness;
    }

    public String getStdOut(String executionId) throws ApiException {
        try {
            Simulation s = workflowBusiness.getSimulation(executionId);
            String stdout = simulationBusiness.readFile(s.getID(), "", "workflow", ".out");
            return stdout;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public String getStdErr(String executionId) throws ApiException {
        try {
            Simulation s = workflowBusiness.getSimulation(executionId);
            String stderr = simulationBusiness.readFile(s.getID(), "", "workflow", ".err");
            return stderr;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public Execution getExecution(String executionId, boolean summarize) throws ApiException {
        try {
            // Get main execution object
            Simulation s = workflowBusiness.getSimulation(executionId, true); // check running execution for update

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
            List<InOutData> inputs = workflowBusiness.getInputData(executionId, apiContext.getUser().getFolder());
            logger.info("Execution has " + inputs.size() + " inputs ");
            for (InOutData iod : inputs) {
                ParameterTypedValue value = new ParameterTypedValue(ApiUtils.getCarminType(iod.getType()), iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(), value);
                logger.info("Adding input " + skpv.toString());
                e.getInputValues().add(skpv);
                e.getRestInputValues().put(iod.getProcessor(), iod.getPath());
            }

            // Outputs
            List<InOutData> outputs = workflowBusiness.getOutputData(executionId, apiContext.getUser().getFolder());
            for (InOutData iod : outputs) {
                ParameterTypedValue value = new ParameterTypedValue(ApiUtils.getCarminType(iod.getType()), iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(), value);
                e.getReturnedFiles().add(skpv);
                if (!e.getRestReturnedFiles().containsKey(iod.getProcessor())) {
                     e.getRestReturnedFiles().put(iod.getProcessor(), new ArrayList<>());
                }
                e.getRestReturnedFiles().get(iod.getProcessor()).add(iod.getPath());
            }

            if (!(e.getStatus() == ExecutionStatus.FINISHED) && !(e.getStatus() == ExecutionStatus.KILLED) && e.getReturnedFiles().isEmpty()) {
                e.clearReturnedFiles();
            }

            return e;

        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

    public Execution[] listExecutions(int maxReturned) throws ApiException {
        try {

            List<Simulation> simulations = workflowBusiness.getSimulations(
                    apiContext.getUser().getFullName(),
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
                        apiContext.getWarnings().add("Only the "+maxReturned+" most recent pipelines were returned.");
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

    public int countExecutions() throws ApiException {
        try {

            List<Simulation> simulations = workflowBusiness.getSimulations(
                    apiContext.getUser().getFullName(),
                    null, // application
                    null, // status
                    null, // class
                    null, // startDate
                    null // endDate
            );
            logger.info("Counting executions, found "+simulations.size()+" simulations.");
            int count = 0;
            for (Simulation s : simulations) {
                if (!(s == null) && !(s.getStatus() == SimulationStatus.Cleaned)) {
                    count++;
                }
            }
            logger.info("After removing null and cleaned, found "+count);
            return count;
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void updateExecution(Execution execution) throws ApiException {
        try {
            if (execution.getTimeout() > 0) {
                throw new ApiException("Update of execution timeout is not supported.");
            }
            logger.info("updating execution " + execution.getIdentifier()
                    + " name to " + execution.getName());
            workflowBusiness.updateDescription(execution.getIdentifier(), execution.getName());
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }

    public void updateExecution(String executionId, ArrayList<StringKeyValuePair> values) throws ApiException {
        try {
            for (StringKeyValuePair skvp : values) {
                if (!skvp.getName().equals("name")) // in the current spec, update can only deal with the timeout (unsupported here) or the name.
                {
                    throw new ApiException("Update of parameter " + skvp.getName() + " is not supported.");
                } else {
                    if (skvp.getValue() == null) {
                        throw new ApiException("Value of parameter " + skvp.getName() + " is empty.");
                    }
                    logger.info("Updating parameter " + skvp.getName() + " with value \"" + skvp.getValue().toString() + "\"");
                    workflowBusiness.updateDescription(executionId, skvp.getValue().toString());
                }
            }
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }


    public String initExecution(Execution execution) throws ApiException {
        Map<String, String> inputMap = new HashMap<>();
        for (Entry<String,Object> restInput : execution.getRestInputValues().entrySet()) {
            inputMap.put(restInput.getKey(),
                    handleRestParameter(restInput.getKey(), restInput.getValue()));
        }
        return initExecution(execution.getPipelineIdentifier(), inputMap, execution.getTimeout(),
                execution.getName(), execution.getStudyIdentifier(), true);
    }

    private String handleRestParameter(String parameterName, Object restParameterValue) {
        if (restParameterValue instanceof List) {
            StringBuilder paramBuilder = new StringBuilder();
            boolean isFirst = true;
            for (Object listElement : (List) restParameterValue) {
                if (!isFirst) {
                    paramBuilder.append(ApplicationConstants.SEPARATOR_LIST);
                }
                paramBuilder.append(listElement.toString());
                isFirst = false;
            }
            logger.info("Handling list parameter for parameter [" + parameterName +"]");
            return paramBuilder.toString();
        } else {
            return  restParameterValue.toString();
        }
    }


    public String initExecution(String pipelineId,
                                ArrayList<StringKeyParameterValuePair> inputValues,
                                Integer timeoutInSeconds,
                                String executionName,
                                String studyId,
                                Boolean playExecution) throws ApiException {// Build input parameter map
        Map<String, String> inputMap = new HashMap<>();
        for (StringKeyParameterValuePair skpvp : inputValues) {
            logger.info("Adding value " + skpvp.getValue().getValue() + " to input " + skpvp.getName());
            inputMap.put(skpvp.getName(), skpvp.getValue().getValue());
        }
        return initExecution(pipelineId, inputMap, timeoutInSeconds, executionName, studyId,
                playExecution);
    }

    public String initExecution(String pipelineId,
                                Map<String,String> inputValues,
                                Integer timeoutInSeconds,
                                String executionName,
                                String studyId,
                                Boolean playExecution) throws ApiException {
        try {

            // We cannot easily initialize an execution without starting it.
            // So we will just launch the execution, and launch an error in case playExecution is not true.
            // Set warnings
            if (studyId != null) {
                apiContext.getWarnings().add("Study identifier was ignored.");
            }
            if (timeoutInSeconds != null && timeoutInSeconds != 0) {
                apiContext.getWarnings().add("Timeout value (" + timeoutInSeconds.toString() + ") was ignored.");
            }

            // Check that all pipeline inputs are present
            Pipeline p = pipelineBusiness.getPipeline(pipelineId);
            for (PipelineParameter pp : p.getParameters()) {
                if (pp.isReturnedValue()) {
                    continue;
                }
                // pp is an input
                if (!(inputValues.get(pp.getName()) == null)) {
                    continue;
                }
                // pp is an empty input
                if (pp.getDefaultValue() != null) {
                    inputValues.put(pp.getName(), pp.getDefaultValue().getValue());
                    continue;
                }
                // pp is an empty input with no default value
                if (pp.isOptional()) {
                    inputValues.put("no", pp.getDefaultValue().getValue());//that's how optional values are handled in VIP
                    continue;
                }
                // pp is an empty input with no default value and it is not optional
                throw new ApiException("Parameter " + pp.getName() + " is empty while it is not optional and it has no default value.");
            }

            // Get user groups
            List<String> groupNames = new ArrayList<>();
            for (Group g : configurationBusiness.getUserGroups(apiContext.getUser().getEmail()).keySet()) {
                groupNames.add(g.getName());
            }

            // Get application name and version
            String applicationName = ApiUtils.getApplicationName(pipelineId);
            String applicationVersion = ApiUtils.getApplicationVersion(pipelineId);

            // Get application classes
            List<String> classes = applicationBusiness.getApplication(applicationName).getApplicationClasses();
            if (classes.isEmpty()) {
                throw new ApiException("Application " + applicationName + " cannot be launched because it doesn't belong to any VIP class.");
            }

            logger.info("Launching workflow with the following parameters: ");
            logger.info(apiContext.getUser());
            logger.info(groupNames);
            logger.info(inputValues);
            logger.info(applicationName);
            logger.info(applicationVersion);
            logger.info(classes.get(0));
            logger.info(executionName);

            // Launch the workflow
            String executionId = workflowBusiness.launch(apiContext.getUser(),
                                           groupNames,
                                           inputValues,
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
            apiContext.getWarnings().add("In VIP, playExecution only returns the status of the execution.");
            return VIPtoCarminStatus(workflowBusiness.getSimulation(executionId).getStatus());
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void killExecution(String executionId) throws ApiException {
        try {
            workflowBusiness.kill(executionId);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public void deleteExecution(String executionId, Boolean deleteFiles) throws ApiException {
        checkIfUserCanAccessExecution(executionId);
        try {
            Simulation s = workflowBusiness.getSimulation(executionId);
            if (s.getStatus() != SimulationStatus.Completed && s.getStatus() != SimulationStatus.Killed) {
                throw new ApiException("Cannot delete execution " + executionId + " because status is " + s.getStatus().toString());
            }
            // Note: this won't delete the intermediate files in case the execution was run locally, which violates the spec.
            // Purge should be called in that case but purge also violates the spec.
            workflowBusiness.clean(executionId, apiContext.getUser().getEmail(), deleteFiles);
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public List<PathProperties> getExecutionResultsPaths(String executionId) throws ApiException {
        List<PathProperties> pathResults = new ArrayList<>();
        List<InOutData> outputs;
        try {
            outputs = workflowBusiness.getOutputData(executionId, apiContext.getUser().getFolder());
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
        for (InOutData output : outputs) {
            String outputPath = output.getPath();
            pathResults.add(dataApiBusiness.getPathProperties(outputPath));
        }
        return pathResults;
    }

    public String[] getSoapExecutionResultsURLs(String executionId, String protocol) throws ApiException {
        return getExecutionResultsURLs(executionId, protocol, false);
    }

    private String[] getExecutionResultsURLs(String executionId, String protocol, boolean generateRestUrl) throws ApiException {
        if (protocol == null) {
            protocol = "https";
        }

        if (!protocol.equals("https") && !protocol.equals("http")) {
            throw new ApiException("Unsupported protocol: " + protocol);
        }

        ArrayList<String> urls = new ArrayList<>();

        List<InOutData> outputs = null;
        try {
            outputs = workflowBusiness.getOutputData(executionId, apiContext.getUser().getFolder());
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
        for (InOutData output : outputs) {
            if (generateRestUrl) {
                urls.add(getRestExecutionResultUrl(output));
            } else {
                urls.add(getSoapExecutionResultURL(output));
            }
        }

        String[] array_urls = new String[urls.size()];
        return urls.toArray(array_urls);
    }

    private String getRestExecutionResultUrl(InOutData output) throws ApiException {
        // search for the "/rest/" keyword determine the instance path
        String requestUrl = apiContext.getRequest().getRequestURL().toString();
        Integer restStringIndex = requestUrl.indexOf("/rest/");
        if (restStringIndex < 0) {
            logger.error("Results URL called from unknown URL : " + requestUrl);
            throw new ApiException("Results URL called from unknown URL");
        }
        String baseUrl = requestUrl.substring(0, restStringIndex + 5); // "http(s)://host[/...]/rest
        String filePath = output.getPath();
        return baseUrl
                + env.getRequiredProperty(CarminProperties.API_DATA_DOWNLOAD_RELATIVE_PATH)
                + filePath + "?action=content"; // TODO ; parametize the end
    }

    private String getSoapExecutionResultURL(InOutData output) throws ApiException {
        try {
            String operationId = transferPoolBusiness.downloadFile(apiContext.getUser(), output.getPath());
            String url = apiContext.getRequest().getRequestURL()
                    + "/../fr.insalyon.creatis.vip.portal.Main/filedownloadservice?operationid="
                    + operationId;
            // add filename and parameter name to URL
            String filename = extractFileNameFromOutput(output);
            logger.debug("adding filename to url :" + filename);
            url += "&outputname=" + URLEncoder.encode(output.getProcessor(), "UTF-8");
            url += "&filename=" + URLEncoder.encode(filename, "UTF-8");
            new URL(url); // just to check that it is a well-formed URL
            return url;
        } catch (BusinessException | MalformedURLException | UnsupportedEncodingException ex) {
            throw new ApiException(ex);
        }
    }

    private String extractFileNameFromOutput(InOutData output) {
        logger.debug("Extracting filename from path :" + output.getPath());
        String path = output.getPath();
        int lastSlashIndex = path.lastIndexOf("/");
        if (lastSlashIndex >= 0 && lastSlashIndex >= (path.length() -1 )) {
            // there is a final slash, take the one before
            lastSlashIndex = path.lastIndexOf("/", lastSlashIndex-1);
        }
        if (lastSlashIndex >= 0) {
            // there is non final slash
            return path.substring(lastSlashIndex+1);
        } else {
            // no slash
            return path;
        }
    }

    // static methods
    public static ExecutionStatus VIPtoCarminStatus(SimulationStatus s) {

        switch (s) {
            case Running:
                return ExecutionStatus.RUNNING;
            case Completed:
                return ExecutionStatus.FINISHED;
            case Killed:
                return ExecutionStatus.KILLED;
            case Cleaned:
                return ExecutionStatus.UNKOWN;
            case Queued:
                return ExecutionStatus.READY;
            case Unknown:
                return ExecutionStatus.UNKOWN;
            default:
                return ExecutionStatus.UNKOWN;
        }
    }

    public void checkIfUserCanAccessExecution(String executionId) throws ApiException {
        try {
            User user = apiContext.getUser();
            if (user.isSystemAdministrator()) {
                return;
            }
            Simulation s = workflowBusiness.getSimulation(executionId);
            if (s.getUserName().equals(user.getFullName())) {
                return;
            }
            throw new ApiException("Permission denied");
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

}
