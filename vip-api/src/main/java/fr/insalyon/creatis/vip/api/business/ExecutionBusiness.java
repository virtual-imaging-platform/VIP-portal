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

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.model.*;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import static fr.insalyon.creatis.vip.api.CarminProperties.ADDITIONNAL_INPUT_VALID_CHARS;
import static fr.insalyon.creatis.vip.application.client.ApplicationConstants.INPUT_VALID_CHARS;

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
    private final ApplicationBusiness applicationBusiness;

    @Autowired
    public ExecutionBusiness(Supplier<User> currentUserProvider,
                             SimulationBusiness simulationBusiness,
                             WorkflowBusiness workflowBusiness,
                             ConfigurationBusiness configurationBusiness,
                             ApplicationBusiness applicationBusiness,
                             PipelineBusiness pipelineBusiness,
                             DataApiBusiness dataApiBusiness) {
        this.currentUserProvider = currentUserProvider;
        this.simulationBusiness = simulationBusiness;
        this.workflowBusiness = workflowBusiness;
        this.configurationBusiness = configurationBusiness;
        this.applicationBusiness = applicationBusiness;
        this.pipelineBusiness = pipelineBusiness;
        this.dataApiBusiness = dataApiBusiness;
    }

    public String getStdOut(String executionId) throws ApiException {
        try {
            Simulation s = workflowBusiness.getSimulation(executionId);
            return simulationBusiness.readFile(s.getID(), "", "workflow", ".out");
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public String getStdErr(String executionId) throws ApiException {
        try {
            Simulation s = workflowBusiness.getSimulation(executionId);
            return simulationBusiness.readFile(s.getID(), "", "workflow", ".err");
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public Execution getExample(String executionId) throws ApiException {
        return getExecution(executionId, false, true);
    }

    public Execution getExecution(String executionId, boolean summarize) throws ApiException {
        return getExecution(executionId, summarize, false);
    }

    public Execution getExecution(String executionId, boolean summarize, boolean onlyExample)
            throws ApiException {
        try {
            // Get simulation object
            Simulation s = workflowBusiness.getSimulation(executionId, true); // check running execution for update

            // Return null if execution doesn't exist or is cleaned (cleaned status is not supported in Carmin)
            if (s == null || s.getStatus() == SimulationStatus.Cleaned) {
                logger.error("Error accessing invalid execution {}. (is cleaned : {})", executionId, s != null);
                throw new ApiException(ApiException.ApiError.INVALID_EXECUTION_ID, executionId);
            }

            if (onlyExample && ! isSimulationAnExample(s)) {
                logger.error("Error trying to get an non-example execution as example : {}", executionId);
                throw new ApiException(ApiException.ApiError.INVALID_EXAMPLE_ID, executionId);
            }

            return getExecutionFromSimulation(s, summarize);

        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    private Execution getExecutionFromSimulation(Simulation s, boolean summarize) throws BusinessException {
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
                    currentUserProvider.get().getEmail(),
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
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
    }

    public List<Execution> listExamples() throws ApiException {
        try {
            List<Simulation> simulations = workflowBusiness.getSimulations(
                    null, // User must be null to take examples from other users
                    null, // application
                    WorkflowStatus.Completed.name(), // status
                    null, // class
                    null, // startDate
                    null, // endDate
                    ApplicationConstants.WORKKFLOW_EXAMPLE_TAG
            );
            List<Execution> executions = new ArrayList<>();
            for (Simulation simulation : simulations) {
                executions.add(getExecutionFromSimulation(simulation, true));
            }
            return executions;
        } catch (BusinessException e) {
            throw new ApiException(e);
        }
    }

    public int countExecutions() throws ApiException {
        try {

            List<Simulation> simulations = workflowBusiness.getSimulations(
                    currentUserProvider.get().getEmail(),
                    null, // application
                    null, // status
                    null, // class
                    null, // startDate
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
        } catch (BusinessException ex) {
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
        } catch (BusinessException e) {
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
        String validChars = INPUT_VALID_CHARS + ADDITIONNAL_INPUT_VALID_CHARS;
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
                // beware : with gwendia, optional always have an defaultValue (either defined or No_Value_Provided)
                if (pp.getDefaultValue() != null) {
                    inputValues.put(pp.getName(), pp.getDefaultValue().toString());
                    continue;
                }
                // then ok if it is optional
                // beware, with gwendia it should not be possible to enter this case (see previous condition)
                if (pp.isOptional()) {
                    continue;
                }
                // error : pp is an empty input with no default value and it is not optional
                logger.error("Error initialising {}, missing {} parameter", pipelineId, pp.getName());
                throw new ApiException(ApiException.ApiError.INPUT_FIELD_MISSING, pp.getName());
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

            // Get application classes
            List<String> classes = applicationBusiness
                    .getApplication(applicationName)
                    .getApplicationClasses();
            if (classes.isEmpty()) {
                logger.error("No class configured for {}", pipelineId);
                throw new ApiException(ApiException.ApiError.INVALID_EXECUTION_INIT,
                        "Application " + applicationName + " cannot be launched because it doesn't belong to any VIP class.");
            }

            logger.info("Launching workflow with the following parameters: ");
            logger.info(currentUserProvider.get().toString());
            logger.info(groupNames.toString());
            logger.info(inputValues.toString());
            logger.info(applicationName);
            logger.info(applicationVersion);
            logger.info(classes.get(0));
            logger.info(executionName);

            // Launch the workflow
            return workflowBusiness.launch(
                    currentUserProvider.get(),
                    groupNames,
                    inputValues,
                    applicationName,
                    applicationVersion,
                    classes.get(0),
                    executionName);
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
                logger.error("Cannot delete exec {}, it is {}", executionId, s.getStatus());
                throw new ApiException("Cannot delete execution " + executionId + " because status is " + s.getStatus().toString());
            }
            // Note: this won't delete the intermediate files in case the execution was run locally, which violates the spec.
            // Purge should be called in that case but purge also violates the spec.
            workflowBusiness.clean(executionId, currentUserProvider.get().getEmail(), deleteFiles);
        } catch (BusinessException ex) {
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
        } catch (BusinessException e) {
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
                return ExecutionStatus.UNKOWN;
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
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }

    }

}
