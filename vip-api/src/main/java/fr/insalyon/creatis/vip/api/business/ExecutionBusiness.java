package fr.insalyon.creatis.vip.api.business;

import static fr.insalyon.creatis.vip.application.client.ApplicationConstants.INPUT_VALID_CHARS;
import static fr.insalyon.creatis.vip.core.client.view.CoreConstants.RESULTS_DIRECTORY_PARAM_NAME;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import fr.insalyon.creatis.vip.application.models.*;
import fr.insalyon.creatis.vip.application.server.business.ListWorkflowsBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowLaunchBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.model.Execution;
import fr.insalyon.creatis.vip.api.model.ExecutionStatus;
import fr.insalyon.creatis.vip.api.model.PathProperties;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.api.model.PipelineParameter;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.CarminProperties;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;

@Service
public class ExecutionBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // API dependencies
    private final Supplier<User> currentUserProvider;
    private final DataApiBusiness dataApiBusiness;

    // other modules dependencies
    private final SimulationBusiness simulationBusiness;
    private final WorkflowBusiness workflowBusiness;
    private final WorkflowLaunchBusiness workflowLaunchBusiness;
    private final ListWorkflowsBusiness listWorkflowsBusiness;
    private final PipelineBusiness pipelineBusiness;
    private final ExternalPlatformBusiness externalPlatformBusiness;

    @Autowired
    public ExecutionBusiness(Supplier<User> currentUserProvider,
                             SimulationBusiness simulationBusiness,
                             WorkflowBusiness workflowBusiness,
                             WorkflowLaunchBusiness workflowLaunchBusiness,
                             ListWorkflowsBusiness listWorkflowsBusiness,
                             PipelineBusiness pipelineBusiness,
                             DataApiBusiness dataApiBusiness,
                             ExternalPlatformBusiness externalPlatformBusiness) {
        this.currentUserProvider = currentUserProvider;
        this.simulationBusiness = simulationBusiness;
        this.workflowBusiness = workflowBusiness;
        this.workflowLaunchBusiness = workflowLaunchBusiness;
        this.listWorkflowsBusiness = listWorkflowsBusiness;
        this.pipelineBusiness = pipelineBusiness;
        this.dataApiBusiness = dataApiBusiness;
        this.externalPlatformBusiness = externalPlatformBusiness;
    }

    public String getLog(String executionId, String type) throws VipException {
        return getLog(executionId, null, type);
    }

    public String getLog(String executionId, Integer invocationId, String type) throws VipException {
        Workflow s = listWorkflowsBusiness.getNotRefreshedWorkflow(executionId);
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
            throw new VipException("no file name for job " + invocationId + " in execution " + executionId);
        }
    }

    public Execution getExample(String executionId) throws VipException {
        Workflow workflow = listWorkflowsBusiness.getExample(executionId);
        return getExecutionFromWorkflow(workflow, false);
    }

    public Execution getExecution(String executionId, boolean summarize) throws VipException {
        // Get simulation object
        Workflow s = listWorkflowsBusiness.getRefreshedWorkflow(executionId); // check running execution for update

        // Return null if execution doesn't exist or is cleaned (cleaned status is not
        // supported in Carmin)
        if (s == null || s.getStatus() == WorkflowStatus.Cleaned) {
            logger.error("Error accessing invalid execution {}. (is cleaned : {})", executionId, s != null);
            throw new VipException(ApiError.INVALID_EXECUTION_ID, executionId);
        }

        return getExecutionFromWorkflow(s, summarize);
    }

    @SuppressWarnings("unchecked")
    private Execution getExecutionFromWorkflow(Workflow s, boolean summarize) throws VipException {
        // Build Carmin's execution object
        Execution e = new Execution(
                s.getID(),
                s.getWorkflowName(),
                pipelineBusiness.getPipelineIdentifier(s.getApplicationName(), s.getApplicationVersion()),
                0,// timeout (no timeout set in VIP)
                s.getStatus() == null ? null : convertVIPtoCarminStatus(s.getStatus()),
                null, //study identifier (not available in VIP yet)
                null, //  error codes and mesasges (not available in VIP yet)
                s.getStartDate().getTime(),
                s.getEndDate() != null ? s.getEndDate().getTime() : null,
                null // results location (handled later as a special input in VIP
        );

        if (summarize) {
                return e;
        }
        //get the current user's folder to filter file access
        String userFolder = currentUserProvider.get().getFolder();

        // retrieves all input data associated with this simulation
        List<InOutData> inputs = workflowBusiness.getInputData(s.getID(), userFolder);
        for (InOutData iod : inputs) {
            String key = iod.getProcessor();
            String value = iod.getPath();
            ((List<Object>) e.getInputValuesForDisplay().computeIfAbsent(key, k -> new ArrayList<>())).add(value);
        }
        // retrieves results directory
        List<Object> resDirList = (List<Object>) e.getInputValuesForDisplay().get(RESULTS_DIRECTORY_PARAM_NAME);
        if (resDirList == null) {
            resDirList = new ArrayList<>();
        }
        if (!resDirList.isEmpty()) {
            e.setResultsLocation(resDirList);
            e.getInputValuesForDisplay().remove(RESULTS_DIRECTORY_PARAM_NAME);
        }

        List<InOutData> outputs = workflowBusiness.getOutputData(s.getID(), userFolder);
        for (InOutData iod : outputs) {
            String key = iod.getProcessor();
            String value = iod.getPath();
            if (!e.getReturnedFiles().containsKey(key)) {
                e.getReturnedFiles().put(key, new ArrayList<Object>());
            }
            e.getReturnedFiles().get(key).add(value);
        }
        List<Task> tasks = simulationBusiness.getJobsList(s.getID());
        if (tasks == null) tasks = new ArrayList<>();

        Map<Integer, Task> latestTaskPerInvocation = new HashMap<>();

        // Group tasks by invocation ID to keep only the most recent attempt for each job
        for (Task t : tasks) {
            int invId = t.getInvocationID();
            Task current = latestTaskPerInvocation.get(invId);
            if (current == null || t.getCreationDate().after(current.getCreationDate())) {
                latestTaskPerInvocation.put(invId, t);
            }
        }
        Map<Integer, Map<String, Object>> jobsMap = new HashMap<>(); 
        //  detailed job data including sanitized inputs and outputs
        for (Map.Entry<Integer, Task> entry : latestTaskPerInvocation.entrySet()) {
            Integer invocationId = entry.getKey(); 
            Task t = entry.getValue();
            String jobName = t.getFileName();

            List<String> dbInputs = simulationBusiness.getJobInputs(s.getID(), jobName, userFolder);
            List<String> dbOutputs = simulationBusiness.getJobOutputs(s.getID(), jobName, userFolder);

            List<String> jobInputs = externalPlatformBusiness.sanitizeUriList(dbInputs);
            List<String> jobOutputs = externalPlatformBusiness.sanitizeUriList(dbOutputs);

            // Build the data structure 
            Map<String, Object> jobData = new HashMap<>();
            
            logger.info("SLURM TEST | execution={} | invocation={} | task={} | executionTimeSlurm={}",
                            s.getID(),
                            invocationId,
                            t.getId(),
                            t.getExecutionTimeSlurm()
                        );

            jobData.put("status", t.getStatus().name());
            jobData.put("exitCode", t.getExitCode());
            jobData.put("exitMessage", t.getExitMessage());
            jobData.put("inputs", jobInputs);   
            jobData.put("outputs", jobOutputs);
            jobData.put("executionTimeSlurm", t.getExecutionTimeSlurm());
            jobsMap.put(invocationId, jobData);
        }
        // Attach the compiled jobs map to the Execution object
        e.setJobs(jobsMap);
        return e;
    }

    private boolean isSimulationAnExample(Workflow workflow) {
        return workflow.getTags() != null &&
                workflow.getTags().contains(ApplicationConstants.WORKKFLOW_EXAMPLE_TAG);
    }

    public List<Execution> listExecutions(int maxReturned) throws VipException {
        List<Workflow> workflows =
                listWorkflowsBusiness.refreshRunningWorkflows(
                        listWorkflowsBusiness.getCurrentUserWorkflows(null));
        logger.debug("Found {} simulations", workflows.size());
        ArrayList<Execution> executions = new ArrayList<>();
        int count = 0;
        for (Workflow s : workflows) {
            if (!(s == null) && !(s.getStatus() == WorkflowStatus.Cleaned)) {
                count++;
                executions.add(getExecutionFromWorkflow(s, true));
                if (count >= maxReturned) {
                    logger.warn("Only the {} most recent pipelines were returned.", maxReturned);
                    break;
                }
            }
        }
        logger.debug("Returning {} executions", executions.size());
        return executions;
    }

    public List<Execution> listExamples() throws VipException {
        List<Workflow> workflows = listWorkflowsBusiness.getAllExamples();
        List<Execution> executions = new ArrayList<>();
        for (Workflow workflow : workflows) {
            executions.add(getExecutionFromWorkflow(workflow, true));
        }
        return executions;
    }

    public int countExecutions() throws VipException {
        List<Workflow> workflows =
                listWorkflowsBusiness.refreshRunningWorkflows(
                        listWorkflowsBusiness.getCurrentUserWorkflows(null));
        logger.debug("Counting executions, found {} simulations.", workflows.size());
        int count = 0;
        for (Workflow s : workflows) {
            if (!(s == null) && !(s.getStatus() == WorkflowStatus.Cleaned)) {
                count++;
            }
        }
        logger.debug("After removing null and cleaned, found {}", count);
        return count;
    }

    public void updateExecution(Execution execution) throws VipException {
        if (execution.getTimeout() > 0) {
            logger.error("Unsupported change of execution timeout {}",
                    execution.getIdentifier());
            throw new VipException("Update of execution timeout is not supported.");
        }
        checkInputExecNameIsValid(execution.getName());
        logger.info("updating execution " + execution.getIdentifier()
                + " name to " + execution.getName());
        workflowBusiness.updateDescription(execution.getIdentifier(), execution.getName());
    }

    public String initExecution(Execution execution) throws VipException {
        List<Map<String, WorkflowInput>> inputsMapsList = new ArrayList<>();
        Object resultsLocation = execution.getResultsLocation();
        boolean isInputMapList = execution.getInputValuesForInit().size() > 1;
        for (Map<String, Object> inputValuesMap : execution.getInputValuesForInit()) {
            Map<String, WorkflowInput> inputsMap = new HashMap<>();
            for (Entry<String, Object> restInput : inputValuesMap.entrySet()) {
                if (isInputMapList && restInput.getValue() instanceof List) {
                    logger.error("Parameter '{}' contains a list, it should only have a single value when providing a list of input maps.", restInput.getKey());
                    throw new VipException(
                        "Parameter '" + restInput.getKey() + "' contains a list, it should only have a single value when providing a list of input maps.");
                }

                inputsMap.put(
                        restInput.getKey(),
                        handleRestParameter(restInput.getKey(), restInput.getValue()));
            }

            // We handle resultsLocation the same as others restInputs, since it can either be a String or a List<String>
            if (resultsLocation != null) {
                inputsMap.put(
                        CoreConstants.RESULTS_DIRECTORY_PARAM_NAME,
                        handleRestParameter(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME, resultsLocation));
            }

            inputsMapsList.add(inputsMap);
        }

        checkInputExecNameIsValid(execution.getName());
        return initExecution(
            execution.getPipelineIdentifier(), inputsMapsList, execution.getTimeout(),
            execution.getName(), execution.getStudyIdentifier());
    }

    private WorkflowInput handleRestParameter(String parameterName, Object restParameterValue)
            throws VipException {
        List<String> workflowInputList = new ArrayList<>();
        if (restParameterValue instanceof List valueAsList) {
            for (Object singleElement : valueAsList) {
                checkInputIsValid(parameterName, singleElement.toString());
                workflowInputList.add(singleElement.toString());
            }
        } else {
            String inputValue = restParameterValue.toString();
            checkInputIsValid(parameterName, inputValue);
            workflowInputList.add(inputValue);
        }
        return WorkflowInput.ofList(workflowInputList);
    }

    private String initExecution(String pipelineId,
                                 List<Map<String, WorkflowInput>> inputMapsList,
                                 Integer timeoutInSeconds,
                                 String executionName,
                                 String studyId) throws VipException {
        // studyId and timeout not implemented yet.
        if (studyId != null) {
            logger.warn("Study identifier ({}) was ignored.", studyId);
        }
        if (timeoutInSeconds != null && timeoutInSeconds != 0) {
            logger.warn("Timeout value ({}) was ignored.", timeoutInSeconds);
        }

        // Check that all pipeline inputs are present
        // TODO : do that in (New)WorkflowBusiness (and/or do less and rely on boutiques to manage default values)
        Pipeline p = pipelineBusiness.getPipelineWithResultsDirectory(pipelineId);
        for (PipelineParameter pp : p.getParameters()) {
            // always true on vip
            if (pp.isReturnedValue()) {
                continue;
            }

            List<Map<String, WorkflowInput>> mapsWithoutKey = inputMapsList.stream()
                    .filter(inputMap -> !inputMap.containsKey(pp.getName()))
                    .toList();

            // ok if input is present
            if (mapsWithoutKey.isEmpty()) {
                continue;
            }

            // then ok if input has a default value
            if (pp.getDefaultValue() != null) {
                continue;
            }

            // then ok if it is optional
            if (pp.isOptional()) {
                continue;
            }

            // error : pp is an empty input with no default value and it is not optional
            logger.error("Error initialising {}, missing {} parameter", pipelineId, pp.getName());
            throw new VipException(ApiError.INPUT_FIELD_MISSING, pp.getName());
        }

        // Get application name and version
        String applicationName = pipelineBusiness.getApplicationName(pipelineId);
        String applicationVersion = pipelineBusiness.getApplicationVersion(pipelineId);

        CarminWorkflow carminWorkflow = new CarminWorkflow(
                executionName,
                applicationName,
                applicationVersion);
        carminWorkflow.setInputsMapsList(inputMapsList);

        // Launch the workflow
        return workflowLaunchBusiness.launch(carminWorkflow).getID();
    }

    private void checkInputIsValid(String inputName, String inputValue) throws VipException {
        String validChars = INPUT_VALID_CHARS + CarminProperties.ADDITIONNAL_INPUT_VALID_CHARS;
        if( ! inputValue.matches("[" + validChars + "]+")) {
            logger.error("Input {} is not valid. Value : {}, Authorized characters are {}",
                    inputName, inputValue, validChars);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, inputName, "Authorized characters are " + validChars);
        }
    }

    private void checkInputExecNameIsValid(String input) throws VipException {
        if( ! input.matches("[" + ApplicationConstants.EXEC_NAME_VALID_CHARS + "]+")) {
            logger.error("Execution name {} is not valid. Authorized characters are {}",
                    input, ApplicationConstants.EXEC_NAME_VALID_CHARS);
            throw new VipException(ApiError.INVALID_EXECUTION_NAME, "Authorized characters are " + ApplicationConstants.EXEC_NAME_VALID_CHARS);
        }
    }

    public void killExecution(String executionId) throws VipException {
        workflowBusiness.kill(executionId);
    }

    public void deleteExecution(String executionId, Boolean deleteFiles) throws VipException {
        checkIfUserCanAccessExecution(executionId);
        Workflow s = listWorkflowsBusiness.getNotRefreshedWorkflow(executionId);
        if (s.getStatus() != WorkflowStatus.Completed && s.getStatus() != WorkflowStatus.Killed) {
            logger.error("Cannot delete exec {}, it is {}", executionId, s.getStatus());
            throw new VipException(
                    "Cannot delete execution " + executionId + " because status is " + s.getStatus().toString());
        }
        // Note: this won't delete the intermediate files in case the execution was run
        // locally, which violates the spec.
        // Purge should be called in that case but purge also violates the spec.
        workflowBusiness.clean(executionId, currentUserProvider.get().getEmail(), deleteFiles);
    }

    public List<PathProperties> getExecutionResultsPaths(String executionId)
            throws VipException {

        List<PathProperties> pathResults = new ArrayList<>();
        List<InOutData> outputs;
        outputs = workflowBusiness.getOutputData(
                executionId, currentUserProvider.get().getFolder());
        for (InOutData output : outputs) {
            String outputPath = output.getPath();
            pathResults.add(dataApiBusiness.getPathProperties(outputPath));
        }
        return pathResults;
    }

    private ExecutionStatus convertVIPtoCarminStatus(WorkflowStatus s) {

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

    public void checkIfUserCanAccessExecution(String executionId) throws VipException {
        User user = currentUserProvider.get();
        if (user.isSystemAdministrator()) {
            return;
        }
        Workflow s = listWorkflowsBusiness.getNotRefreshedWorkflow(executionId);
        if (s.getUserFullName() != null && s.getUserFullName().equals(user.getFullName())) {
            return;
        }
        logger.error("Permission denied for {} on exec {}", user, executionId);
        throw new VipException("Permission denied");
    }

}
