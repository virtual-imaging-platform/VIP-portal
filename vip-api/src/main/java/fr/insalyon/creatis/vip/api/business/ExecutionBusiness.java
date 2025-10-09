package fr.insalyon.creatis.vip.api.business;

import static fr.insalyon.creatis.vip.application.client.ApplicationConstants.INPUT_VALID_CHARS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus;
import fr.insalyon.creatis.vip.api.model.Execution;
import fr.insalyon.creatis.vip.api.model.ExecutionStatus;
import fr.insalyon.creatis.vip.api.model.PathProperties;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.api.model.PipelineParameter;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.CarminProperties;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.exception.ApiException;

/**
 *
 * @author Tristan Glatard
 */
@Service
public class ExecutionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // API dependencies
    private final Supplier<User> currentUserProvider;
    private final DataApiBusiness dataApiBusiness;

    // other modules dependencies
    private final SimulationBusiness simulationBusiness;
    private final WorkflowBusiness workflowBusiness;
    private final PipelineBusiness pipelineBusiness;
    private final ConfigurationBusiness configurationBusiness;

    @Autowired
    public ExecutionBusiness(Supplier<User> currentUserProvider,
                             SimulationBusiness simulationBusiness,
                             WorkflowBusiness workflowBusiness,
                             ConfigurationBusiness configurationBusiness,
                             PipelineBusiness pipelineBusiness,
                             DataApiBusiness dataApiBusiness) {
        this.currentUserProvider = currentUserProvider;
        this.simulationBusiness = simulationBusiness;
        this.workflowBusiness = workflowBusiness;
        this.configurationBusiness = configurationBusiness;
        this.pipelineBusiness = pipelineBusiness;
        this.dataApiBusiness = dataApiBusiness;
    }

    public String getLog(String executionId, String type) throws ApiException {
        return getLog(executionId, null, type);
    }

    public String getLog(String executionId, Integer invocationId, String type) throws ApiException {
        try {
            Simulation s = workflowBusiness.getSimulation(executionId);
            List<Task> tasks = simulationBusiness.getJobsList(s.getID());

            if (tasks.isEmpty()) {
                logger.debug("Warning: no .sh.out log file found for execution ID = {} ", executionId);
                return "no log found";
            }

            String extension = ".sh.app." + type;

            Task targetTask = null;

            if (invocationId == null) {
                if (tasks.size() == 1) {
                    targetTask = tasks.get(0);
                    logger.debug("jobId is null, using the only available task with ID = {}", targetTask.getId());
                } else {
                    logger.debug("jobId is null but multiple tasks found for execution ID = {}", executionId);
                    return "jobId is required when multiple tasks exist";
                }
            } else {
                targetTask = tasks.stream()
                        .filter(t -> invocationId.equals(t.getInvocationID()))
                        .max(Comparator.comparing(Task::getCreationDate))
                        .orElse(null);
            }



            if (targetTask == null) {
                logger.debug("No job {} found for execution ID = {}", invocationId, executionId);
                return "no log found for job " + invocationId;
            }

            String fileName = targetTask.getFileName();
            if (fileName != null) {
                return simulationBusiness.readFile(executionId, type, fileName, extension);
            } else {
                throw new ApiException("no file name for job " + invocationId + " in execution " + executionId);
            }
        } catch (VipException e) {
            throw new ApiException(e);
        }
    }



    public Execution getExample(String executionId) throws VipException {
        return getExecution(executionId, false, true);
    }

    public Execution getExecution(String executionId, boolean summarize) throws VipException {
        return getExecution(executionId, summarize, false);
    }

    public Execution getExecution(String executionId, boolean summarize, boolean onlyExample)
            throws VipException {
        // Get simulation object
        Simulation s = workflowBusiness.getSimulation(executionId, true); // check running execution for update

        // Return null if execution doesn't exist or is cleaned (cleaned status is not
        // supported in Carmin)
        if (s == null || s.getStatus() == SimulationStatus.Cleaned) {
            logger.error("Error accessing invalid execution {}. (is cleaned : {})", executionId, s != null);
            throw new ApiException(ApiException.ApiError.INVALID_EXECUTION_ID, executionId);
        }

        if (onlyExample && !isSimulationAnExample(s)) {
            logger.error("Error trying to get an non-example execution as example : {}", executionId);
            throw new ApiException(ApiException.ApiError.INVALID_EXAMPLE_ID, executionId);
        }

        return getExecutionFromSimulation(s, summarize);
    }

    private Execution getExecutionFromSimulation(Simulation s, boolean summarize) throws VipException {
        // Build Carmin's execution object
        Execution e = new Execution(
                s.getID(),
                s.getSimulationName(),
                pipelineBusiness.getPipelineIdentifier(s.getApplicationName(), s.getApplicationVersion()),
                0, // timeout (no timeout set in VIP)
                VIPtoCarminStatus(s.getStatus()),
                null, // study identifier (not available in VIP yet)
                null, // error codes and mesasges (not available in VIP yet)
                s.getDate().getTime(),
                null, // last status modification date (not available in VIP yet)
                null // results location (not available in VIP yet)
        );

        if(summarize) // don't look into inputs and outputs
            return e;

        // Inputs
        List<InOutData> inputs = workflowBusiness.getInputData(
            s.getID(), currentUserProvider.get().getFolder());
        logger.debug("Execution has " + inputs.size() + " inputs ");
        for (InOutData iod : inputs) {
            e.getInputValues().put(iod.getProcessor(), iod.getPath());
        }

        // Outputs
        List<InOutData> outputs = workflowBusiness.getOutputData(
            s.getID(), currentUserProvider.get().getFolder());
        for (InOutData iod : outputs) {
            if (!e.getReturnedFiles().containsKey(iod.getProcessor())) {
                 e.getReturnedFiles().put(iod.getProcessor(), new ArrayList<>());
            }
            e.getReturnedFiles().get(iod.getProcessor()).add(iod.getPath());
        }

        // Jobs
        List<Task> tasks = simulationBusiness.getJobsList(s.getID());
        Map<Integer, Task> latestTaskPerInvocation = new HashMap<>();

        for (Task t : tasks) {
            int invId = t.getInvocationID();
            Task current = latestTaskPerInvocation.get(invId);

            if (current == null || t.getCreationDate().after(current.getCreationDate())) {
                latestTaskPerInvocation.put(invId, t);
            }
        }

        Map<Integer, Map<String, Object>> jobsMap = new HashMap<>();
        for (Map.Entry<Integer, Task> entry : latestTaskPerInvocation.entrySet()) {
            Task t = entry.getValue();
            Map<String, Object> jobInfo = new HashMap<>();
            jobInfo.put("status", t.getStatus().toString());
            jobInfo.put("exitCode", t.getExitCode());
            jobInfo.put("exitMessage", t.getExitMessage());
            jobsMap.put(entry.getKey(), jobInfo);
        }

        e.setJobs(jobsMap);


        if (!(e.getStatus() == ExecutionStatus.FINISHED) && !(e.getStatus() == ExecutionStatus.KILLED) && e.getReturnedFiles().isEmpty()) {
            e.clearReturnedFiles();
        }

        return e;
    }

    private boolean isSimulationAnExample(Simulation simulation) {
        return simulation.getTags() != null &&
                simulation.getTags().contains(ApplicationConstants.WORKKFLOW_EXAMPLE_TAG);
    }

    public List<Execution> listExecutions(int maxReturned) throws ApiException {
        try {

            List<Simulation> simulations = workflowBusiness.getSimulations(
                    currentUserProvider.get().getFullName(),
                    null, // application
                    null, // status
                    null, // class
                    null, // startDate
                    null // endDate
            );
            logger.debug("Found {} simulations", simulations.size());
            ArrayList<Execution> executions = new ArrayList<>();
            int count = 0;
            for (Simulation s : simulations) {
                if (!(s == null) && !(s.getStatus() == SimulationStatus.Cleaned)) {
                    count++;
                    executions.add(getExecutionFromSimulation(s, true));
                    if(count >= maxReturned){
                        logger.warn("Only the {} most recent pipelines were returned.", maxReturned);
                        break;
                    }
                }
            }
            logger.debug("Returning {} executions", executions.size());
            return executions;
        } catch (VipException ex) {
            throw new ApiException(ex);
        }
    }

    public List<Execution> listExamples() throws ApiException {
        try {
            List<Simulation> simulations = workflowBusiness.getSimulations(
                    null, // User must be null to take examples from other users
                    null, // application
                    WorkflowStatus.Completed.name(), // status
                    null, // startDate
                    null, // endDate
                    ApplicationConstants.WORKKFLOW_EXAMPLE_TAG
            );
            List<Execution> executions = new ArrayList<>();
            for (Simulation simulation : simulations) {
                executions.add(getExecutionFromSimulation(simulation, true));
            }
            return executions;
        } catch (VipException e) {
            throw new ApiException(e);
        }
    }

    public int countExecutions() throws ApiException {
        try {

            List<Simulation> simulations = workflowBusiness.getSimulations(
                    currentUserProvider.get().getFullName(),
                    null, // application
                    null, // status
                    null, // startDate
                    null, // endDate
                    null // endDate
            );
            logger.debug("Counting executions, found {} simulations.", simulations.size());
            int count = 0;
            for (Simulation s : simulations) {
                if (!(s == null) && !(s.getStatus() == SimulationStatus.Cleaned)) {
                    count++;
                }
            }
            logger.debug("After removing null and cleaned, found {}", count);
            return count;
        } catch (VipException ex) {
            throw new ApiException(ex);
        }
    }

    public void updateExecution(Execution execution) throws ApiException {
        try {
            if (execution.getTimeout() > 0) {
                logger.error("Unsupported change of execution timeout {}",
                        execution.getIdentifier());
                throw new ApiException("Update of execution timeout is not supported.");
            }
            checkInputExecNameIsValid(execution.getName());
            logger.info("updating execution " + execution.getIdentifier()
                    + " name to " + execution.getName());
            workflowBusiness.updateDescription(execution.getIdentifier(), execution.getName());
        } catch (VipException e) {
            throw new ApiException(e);
        }
    }

    public String initExecution(Execution execution) throws ApiException {
        Map<String, String> inputMap = new HashMap<>();

        for (Entry<String,Object> restInput : execution.getInputValues().entrySet()) {
            inputMap.put(
                    restInput.getKey(),
                    handleRestParameter(restInput.getKey(), restInput.getValue()));
        }
        String resultsLocation = execution.getResultsLocation();
        if (resultsLocation != null) {
            inputMap.put(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME, resultsLocation);
        }

        checkInputExecNameIsValid(execution.getName());
        return initExecution(
            execution.getPipelineIdentifier(), inputMap, execution.getTimeout(),
            execution.getName(), execution.getStudyIdentifier());
    }

    private String handleRestParameter(String parameterName, Object restParameterValue)
            throws ApiException {
        if (restParameterValue instanceof List) {
            StringBuilder paramBuilder = new StringBuilder();
            boolean isFirst = true;
            for (Object listElement : (List) restParameterValue) {
                if (!isFirst) {
                    paramBuilder.append(ApplicationConstants.SEPARATOR_LIST);
                }
                String inputValue = listElement.toString();
                checkInputIsValid(parameterName, inputValue);
                paramBuilder.append(inputValue);
                isFirst = false;
            }
            logger.info("Handling list parameter for parameter [" + parameterName +"]");
            return paramBuilder.toString();
        } else {
            String inputValue = restParameterValue.toString();
            checkInputIsValid(parameterName, inputValue);
            return restParameterValue.toString();
        }
    }

    private void checkInputIsValid(String inputName, String inputValue) throws ApiException {
        String validChars = INPUT_VALID_CHARS + CarminProperties.ADDITIONNAL_INPUT_VALID_CHARS;
        if( ! inputValue.matches("[" + validChars + "]+")) {
            logger.error("Input {} is not valid. Value : {}, Authorized characters are {}",
                    inputName, inputValue, validChars);
            throw new ApiException(ApiException.ApiError.INPUT_FIELD_NOT_VALID, inputName, "Authorized characters are " + validChars);
        }
    }

    private void checkInputExecNameIsValid(String input) throws ApiException {
        if( ! input.matches("[" + ApplicationConstants.EXEC_NAME_VALID_CHARS + "]+")) {
            logger.error("Execution name {} is not valid. Authorized characters are {}",
                    input, ApplicationConstants.EXEC_NAME_VALID_CHARS);
            throw new ApiException(ApiException.ApiError.INVALID_EXECUTION_NAME, "Authorized characters are " + ApplicationConstants.EXEC_NAME_VALID_CHARS);
        }
    }

    private String initExecution(String pipelineId,
                                Map<String,String> inputValues,
                                Integer timeoutInSeconds,
                                String executionName,
                                String studyId) throws ApiException {
        try {
            // We cannot easily initialize an execution without starting it.
            // So we will just launch the execution, and launch an error in case playExecution is not true.
            // Set warnings
            if (studyId != null) {
                logger.warn("Study identifier ({}) was ignored.", studyId);
            }
            if (timeoutInSeconds != null && timeoutInSeconds != 0) {
                logger.warn("Timeout value ({}) was ignored.", timeoutInSeconds);
            }

            // Check that all pipeline inputs are present
            Pipeline p = pipelineBusiness.getPipelineWithResultsDirectory(pipelineId);
            for (PipelineParameter pp : p.getParameters()) {
                // always true on vip
                if (pp.isReturnedValue()) {
                    continue;
                }
                // ok if input is present
                if ( inputValues.get(pp.getName()) != null) {
                    continue;
                }
                // then ok if input has a default value (and we set it)
                if (pp.getDefaultValue() != null) {
                    inputValues.put(pp.getName(), pp.getDefaultValue().toString());
                    continue;
                }
                // then ok if it is optional
                if (pp.isOptional()) {
                    continue;
                }
                // error : pp is an empty input with no default value and it is not optional
                logger.error("Error initialising {}, missing {} parameter", pipelineId, pp.getName());
                throw new ApiException(ApiException.ApiError.INPUT_FIELD_MISSING, pp.getName());
            }

            // fill in overriddenInputs from explicit inputs
            Map<String, String> overriddenInputs = p.getOverriddenInputs();
            if (overriddenInputs != null) {
                for (String key : overriddenInputs.keySet()) {
                    String value = overriddenInputs.get(key);
                    if (inputValues.containsKey(value)) {
                        inputValues.put(key, inputValues.get(value));
                    } else {
                        logger.error("Error initialising {}, missing {} parameter", pipelineId, value);
                        throw new ApiException(ApiException.ApiError.INPUT_FIELD_MISSING, value);
                    }
                }
            }

            boolean inputsContainsResultsDirectoryInput = inputValues.containsKey(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME);
            boolean pipelineHasResultsDirectoryInput = p.getParameters().stream().anyMatch(param ->
                        param.getName().equals(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME));

            if (inputsContainsResultsDirectoryInput && ! pipelineHasResultsDirectoryInput) {
                logger.error("Missing results-directory for {}", pipelineId);
                throw new ApiException(ApiException.ApiError.INVALID_EXECUTION_INIT,
                    "Input has parameter results-directory but it is not defined in pipeline.");
            }

            // Get user groups
            List<String> groupNames = new ArrayList<>();
            for (Group g : configurationBusiness
                    .getUserGroups(currentUserProvider.get().getEmail())
                    .keySet()) {
                groupNames.add(g.getName());
            }

            // Get application name and version
            String applicationName = pipelineBusiness.getApplicationName(pipelineId);
            String applicationVersion = pipelineBusiness.getApplicationVersion(pipelineId);


            logger.info("Launching workflow with the following parameters: ");
            logger.info(currentUserProvider.get().toString());
            logger.info(groupNames.toString());
            logger.info(inputValues.toString());
            logger.info(applicationName);
            logger.info(applicationVersion);
            logger.info(executionName);

            // Launch the workflow
            return workflowBusiness.launch(
                    currentUserProvider.get(),
                    groupNames,
                    inputValues,
                    applicationName,
                    applicationVersion,
                    executionName);
        } catch (VipException ex) {
            throw new ApiException(ex);
        }
    }

    public void killExecution(String executionId) throws ApiException {
        try {
            workflowBusiness.kill(executionId);
        } catch (VipException ex) {
            throw new ApiException(ex);
        }
    }

    public void deleteExecution(String executionId, Boolean deleteFiles) throws ApiException {
        checkIfUserCanAccessExecution(executionId);
        try {
            Simulation s = workflowBusiness.getSimulation(executionId);
            if (s.getStatus() != SimulationStatus.Completed && s.getStatus() != SimulationStatus.Killed) {
                logger.error("Cannot delete exec {}, it is {}", executionId, s.getStatus());
                throw new ApiException("Cannot delete execution " + executionId + " because status is " + s.getStatus().toString());
            }
            // Note: this won't delete the intermediate files in case the execution was run locally, which violates the spec.
            // Purge should be called in that case but purge also violates the spec.
            workflowBusiness.clean(executionId, currentUserProvider.get().getEmail(), deleteFiles);
        } catch (VipException ex) {
            throw new ApiException(ex);
        }
    }

    public List<PathProperties> getExecutionResultsPaths(String executionId)
            throws ApiException {

        List<PathProperties> pathResults = new ArrayList<>();
        List<InOutData> outputs;
        try {
            outputs = workflowBusiness.getOutputData(
                executionId, currentUserProvider.get().getFolder());
        } catch (VipException e) {
            throw new ApiException(e);
        }
        for (InOutData output : outputs) {
            String outputPath = output.getPath();
            pathResults.add(dataApiBusiness.getPathProperties(outputPath));
        }
        return pathResults;
    }

    private ExecutionStatus VIPtoCarminStatus(SimulationStatus s) {

        switch (s) {
            case Running:
                return ExecutionStatus.RUNNING;
            case Completed:
                return ExecutionStatus.FINISHED;
            case Failed:
                return ExecutionStatus.EXECUTION_FAILED;
            case Killed:
                return ExecutionStatus.KILLED;
            case Queued:
                return ExecutionStatus.READY;
            case Cleaned:
            case Unknown:
            default:
                return ExecutionStatus.UNKNOWN;
        }
    }

    public void checkIfUserCanAccessExecution(String executionId) throws ApiException {
        try {
            User user = currentUserProvider.get();
            if (user.isSystemAdministrator()) {
                return;
            }
            Simulation s = workflowBusiness.getSimulation(executionId);
            if (s.getUserName().equals(user.getFullName())) {
                return;
            }
            logger.error("Permission denied for {} on exec {}", user, executionId);
            throw new ApiException("Permission denied");
        } catch (VipException ex) {
            throw new ApiException(ex.getMessage(), ex);
        }

    }

}
