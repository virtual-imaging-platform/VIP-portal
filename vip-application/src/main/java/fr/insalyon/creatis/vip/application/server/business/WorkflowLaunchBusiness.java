package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.boutiques.model.Input;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.ApplicationError;
import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.application.models.*;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.base.CommonBusiness;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class WorkflowLaunchBusiness extends CommonBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Server server;
    private final WorkflowDAO workflowDAO;
    private final AppVersionBusiness appVersionBusiness;
    private final ExternalPlatformBusiness externalPlatformBusiness;
    private final LfcPathsBusiness lfcPathsBusiness;
    private final ResourceBusiness resourceBusiness;
    private final EngineBusiness engineBusiness;
    private final WorkflowExecutionBusiness workflowExecutionBusiness;
    private final EmailBusiness emailBusiness;
    private final BoutiquesBusiness boutiquesBusiness;

    @Autowired
    public WorkflowLaunchBusiness(Server server, WorkflowDAO workflowDAO, AppVersionBusiness appVersionBusiness,
                                  ExternalPlatformBusiness externalPlatformBusiness,
                                  LfcPathsBusiness lfcPathsBusiness, ResourceBusiness resourceBusiness,
                                  EngineBusiness engineBusiness, WorkflowExecutionBusiness workflowExecutionBusiness,
                                  EmailBusiness emailBusiness, BoutiquesBusiness boutiquesBusiness) {
        this.server = server;
        this.workflowDAO = workflowDAO;
        this.appVersionBusiness = appVersionBusiness;
        this.externalPlatformBusiness = externalPlatformBusiness;
        this.lfcPathsBusiness = lfcPathsBusiness;
        this.resourceBusiness = resourceBusiness;
        this.engineBusiness = engineBusiness;
        this.workflowExecutionBusiness = workflowExecutionBusiness;
        this.emailBusiness = emailBusiness;
        this.boutiquesBusiness = boutiquesBusiness;
    }

    public Workflow launch(String workflowName, String applicationName, String applicationVersion, Map<String, String> parametersMap) throws VipException {
        Workflow workflow = new Workflow(workflowName, applicationName, applicationVersion);
        Map<String,WorkflowInput> inputsMap = new HashMap<>();
        for (String inputName : parametersMap.keySet()) {
            String valuesStr = parametersMap.get(inputName);

            if (valuesStr.contains(ApplicationConstants.SEPARATOR_INPUT)) {
                String[] values = valuesStr.split(ApplicationConstants.SEPARATOR_INPUT);
                if (values.length != 3) {
                    logger.error("Invalid interval input for {}, need 3 values but has {} / [{}]", inputName, values.length, values);
                    throw new VipException(ApplicationError.INVALID_WORKFLOW_INPUT, inputName, "An number interval needs 3 values");
                }
                Double start = Double.parseDouble(values[0]);
                Double stop = Double.parseDouble(values[1]);
                Double step = Double.parseDouble(values[2]);
                inputsMap.put(inputName, WorkflowInput.ofinterval(List.of(start, stop, step)));
            } else if (valuesStr.contains(ApplicationConstants.SEPARATOR_LIST)) {
                String[] values = valuesStr.split(ApplicationConstants.SEPARATOR_LIST);
                inputsMap.put(inputName, WorkflowInput.ofList(List.of(values)));
            } else {
                inputsMap.put(inputName, WorkflowInput.ofList(List.of(valuesStr)));
            }
        }
        workflow.setInputs(inputsMap);
        return launch(workflow);
    }

    public synchronized Workflow launch(Workflow workflow) throws VipException {
        checkWorkflowForLaunch(workflow);
        checkVIPCapacities(getUser(), workflow.getWorkflowName());

        AppVersion appVersion = appVersionBusiness.getVersion(workflow.getApplicationName(), workflow.getApplicationVersion());
        BoutiquesDescriptor boutiquesDescriptor = boutiquesBusiness.parseBoutiquesString(appVersion.getDescriptor());

        // TODO : check boutiques constraint, optional and stuff
        // TODO : check input format (character white list, see API). Maybe also boutiques types

        // get the inputs as a list of maps of String -> List<String>
        // from CARMIN it is already like that. from the internal API or GWT, it is just a map of String -> List<String>
        // 3 other things to do :
        //    - transform the number intervals (start, end, step) into actual values
        //    - transform the VIP paths into actual/real paths
        //    - managing custom "vip:overriddenInputs" in boutiques
        List<Map<String, List<String>>> actualInputs = getActualValuesInputs(workflow, boutiquesDescriptor);

        List<Resource> resources = resourceBusiness.getAvailableForExecution(getUser(), appVersion);
        if (resources.isEmpty()) {
            logger.error("No resource available to launch app {}/{} for user {}",
                    appVersion.getApplicationName(), appVersion.getVersion(), getUserEmail());
            throw new VipException(ApplicationError.WORKFLOW_LAUNCH_IMPOSSIBLE, "No resource available at the moment !");
        }
        Resource resource = resources.getFirst();
        Engine engine = engineBusiness.selectEngine(engineBusiness.getUsableEngines(resource));

        String workflowId;
        try {
            workflowId = workflowExecutionBusiness.launch(workflow.getWorkflowName(), resource, engine, appVersion, actualInputs);
        } catch (Exception e) {
            // this method manage the email sending, and return the exception to make bubble up
            throw sendMailsOnLaunchException(e, engine);
        }
        logger.info("Launched workflow {}", workflowId);
        workflow.setId(workflowId);
        workflow.setStatus(WorkflowStatus.Running);
        workflow.setStartDate(new Date());
        workflow.setUser(getUser());
        addWorkflowInDb(workflow, engine);
        return workflow;
    }

    private VipException sendMailsOnLaunchException(Exception e, Engine engine) {
        String mailSubject = "[VIP] Warn: Workflow submission failed!!";
        String mailContent = "An error occured while submitting a workflow";
        VipException exceptionToRethrow;

        if (e instanceof VipException vipEx && ! vipEx.getVipError().equals(DefaultError.GENERIC_ERROR_WITH_MESSAGE)) {
            // not default error code, so it's an intended errors, only warning by mail
            logger.warn("Error occurred during workflow submission. Not disabling, sending mail to admins");
            exceptionToRethrow = vipEx;
        } else {
            if ( e instanceof VipException vipEx) {
                exceptionToRethrow = vipEx;
            } else {
                logger.error("Unexpected exception while launching a workflow", e);
                exceptionToRethrow = new VipException(ApplicationError.LAUNCH_ERROR, e);
            }
            logger.warn("Error occurred during workflow submission. Disabling engine and sending mail to admins");
            mailSubject = "[VIP] Urgent: VIP engine disabled !";
            mailContent = "Engine " + engine.getName() + " has just been disabled.";
            engine.setStatus("disabled");
            try {
                engineBusiness.update(engine);
            } catch (VipException ex) {
                logger.error("Error updating the engine {} after a exec failed. Ignoring as there is already an exception going on", engine.getName(), ex);
                mailContent += "\n(Also, there was an error updating the engine, see logs)";
                // ignoring as there is already an exception going on
            }
        }
        mailContent += "\n\nException:" + e.getMessage() + "\nStacktrace: " + e.getStackTrace();
        try {
            emailBusiness.sendEmailToAdmins(mailSubject, mailContent, true, getUser().getEmail());
        } catch (VipException ex) {
            logger.error("Error sending the email informing of an execution launch error. Ignoring as there is already an exception going on", ex);
            // ignoring as there is already an exception going on
        }
        return exceptionToRethrow;
    }

    private void addWorkflowInDb(Workflow workflow, Engine engine) throws VipException {
        fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow dbWorkflow =
                new fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow(
                        workflow.getID(),
                        getUser().getFullName(),
                        fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus.Running,
                        workflow.getStartDate(),
                        null,
                        workflow.getWorkflowName(),
                        workflow.getApplicationName(),
                        workflow.getApplicationVersion(),
                        null,
                        engine.getEndpoint(),
                        null
                );
        try {
            workflowDAO.add(dbWorkflow);
        } catch (WorkflowsDBDAOException e) {
            logger.error("Error launching simulation {}", workflow.getWorkflowName(), e);
            throw new VipException(e);
        }
    }

    private void checkWorkflowForLaunch(Workflow workflow) throws VipException {
        assertInputIsValid(workflow.getID() == null, "id", "Must be null on launch");
        assertStringInputNotNullNotBlank(workflow.getWorkflowName(), "workflowName", "Must be present on launch");
        assertStringInputNotNullNotBlank(workflow.getApplicationName(), "applicationName", "Must be present on launch");
        assertStringInputNotNullNotBlank(workflow.getApplicationVersion(), "applicationVersion", "Must be present on launch");
        assertInputIsValid(workflow.getUserId() == null, "userId", "Must be null on launch");
        assertInputIsValid(workflow.getStatus() == null || workflow.getStatus() == WorkflowStatus.Queued,
                "status", "Must be null or QUEUD on launch");
        // inputs must contain results-dir. Return if ok. Throw error an the end of the method if not
        if (workflow instanceof CarminWorkflow carminWorkflow) {
            assertInputIsValid(carminWorkflow.getInputsMapsList() != null && ! carminWorkflow.getInputsMapsList().isEmpty(),
                    "inputs", "Must be present on launch");
            if (carminWorkflow.getInputsMapsList().stream()
                    .allMatch(i -> i.containsKey(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME))) {
                return;
            }
        } else {
            assertInputIsValid(workflow.getInputs() != null,
                    "inputs", "Must be present on launch");
            if ( workflow.getInputs().containsKey(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME)) {
                return;
            }
        }
        // Must have return if res-dir if present : throw error
        logger.error("results-directory absent for new workflow to launch");
        throw new VipException(ApplicationError.INVALID_WORKFLOW_INPUT, CoreConstants.RESULTS_DIRECTORY_PARAM_NAME, "Must be present on launch");
    }

    private void checkVIPCapacities(User user, String workflowName) throws VipException {
        long runningWorkflows;
        long runningSimulations;
        try {
            runningWorkflows = workflowDAO.getNumberOfRunning(user.getFullName());
            runningSimulations = workflowDAO.getRunning().size();
        } catch (WorkflowsDBDAOException e) {
            logger.error("Error getting workflows stats when launching {}", workflowName, e);
            throw new VipException(e);
        }

        if (runningSimulations >= server.getMaxPlatformRunningSimulations()) {
            logger.warn("Unable to launch execution '{}': max number of"
                            + " running workflows reached in the platform : {}",
                    workflowName, runningSimulations);
            throw new VipException(ApplicationError.PLATFORM_MAX_EXECS);
        } else if (runningWorkflows >= user.getMaxRunningSimulations()) {
            logger.warn("Unable to launch execution '{}': max number of "
                            + "running workflows reached ({}/{}) for user '{}'.",
                    workflowName, runningWorkflows,
                    user.getMaxRunningSimulations(), user);
            throw new VipException(ApplicationError.USER_MAX_EXECS, runningWorkflows);
        }
    }
    private List<Map<String, List<String>>> getActualValuesInputs(Workflow workflow, BoutiquesDescriptor boutiquesDescriptor) throws VipException {
        if (workflow instanceof CarminWorkflow carminWorkflow) {
            List<Map<String, List<String>>> actualValuesInputs = new ArrayList<>();
            for (Map<String,WorkflowInput> inputsMap : carminWorkflow.getInputsMapsList()) {
                actualValuesInputs.add(getActualValuesInputs(inputsMap, boutiquesDescriptor));
            }
            return actualValuesInputs;
        } else {
            return List.of(getActualValuesInputs(workflow.getInputs(), boutiquesDescriptor));
        }

    }

    private Map<String, List<String>> getActualValuesInputs(Map<String,WorkflowInput> inputsMap, BoutiquesDescriptor boutiquesDescriptor) throws VipException {
        Map<String, String> overriddenInputs = boutiquesBusiness.getOverriddenInputs(boutiquesDescriptor);
        Map<String, List<String>> actualValuesInputsMap = new HashMap<>();

        for (String inputName : inputsMap.keySet()) {
            WorkflowInput workflowInput = inputsMap.get(inputName);
            List<String> data = getInputActualValues(inputName, workflowInput, boutiquesDescriptor);
            if ( ! data.isEmpty()) {
                actualValuesInputsMap.put(inputName, data);
            }
        }
        // overridden inputs are hidden inputs that must be copied from others
        if (overriddenInputs != null) {
            for (String key : overriddenInputs.keySet()) {
                String value = overriddenInputs.get(key);
                if (actualValuesInputsMap.containsKey(value)) {
                    actualValuesInputsMap.put(key, actualValuesInputsMap.get(value));
                } else {
                    logger.error("Overriding a missing parameter {}", value);
                    throw new VipException(ApplicationError.INVALID_WORKFLOW_INPUT, value, "Missing but needed to override another parameter");
                }
            }
        }
        return actualValuesInputsMap;
    }

    private List<String> getInputActualValues(String inputName, WorkflowInput workflowInput, BoutiquesDescriptor boutiquesDescriptor) throws VipException {
        Input.Type inputType;
        if (CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(inputName)) {
            inputType = Input.Type.FILE;
        } else {
            Optional<Input> boutiqueInput = boutiquesBusiness.getInput(boutiquesDescriptor, inputName);
            if (boutiqueInput.isEmpty()) {
                logger.error("Launching with an unknown descriptor input : {}", inputName);
                throw new VipException(ApplicationError.INVALID_WORKFLOW_INPUT, inputName, "Missing in application descriptor");
            }
            inputType = boutiqueInput.get().getType();
        }
        List<String> data = new ArrayList<>();
        if (workflowInput.isInterval()) {
            List<Double> interval = workflowInput.getInterval();
            if (interval.size() != 3) {
                logger.error("Invalid interval input for {}, need 3 values but has {} / [{}]", inputName, interval.size(), interval);
                throw new VipException(ApplicationError.INVALID_WORKFLOW_INPUT, inputName, "An number interval needs 3 values");
            }
            for (double d = interval.get(0); d <= interval.get(1); d += interval.get(2)) {
                data.add(d + "");
            }
        } else if (inputType == Input.Type.FILE) {
            // TODO : check file exist
            for (String inputValue : workflowInput.getValues()) {
                data.add(transformParameter(inputName, inputValue));
            }
        } else {
            data.addAll(workflowInput.getValues());
        }
        return data;
    }

    private String transformParameter(String parameterName, String parameterValue) throws VipException {

        parameterValue = parameterValue.trim();

        ExternalPlatformBusiness.ParseResult parseResult = externalPlatformBusiness
                .parseParameter(parameterName, parameterValue, getUser());
        if (parseResult.isUri) {
            // The uri has been generated
            return parseResult.result;
        }
        // not an external platform parameter, use legacy format
        String parsedPath = lfcPathsBusiness.parseBaseDir(getUser(), parameterValue);
        if ( ! getUser().isSystemAdministrator()) {
            checkFolderACL(parameterName, parsedPath);
        }
        return (server.useLocalFilesInInputs() ? "file:" : "lfn:") + parsedPath;
    }

    private void checkFolderACL(String parameterName, String path)
            throws VipException {
        if (path.startsWith(server.getDataManagerUsersHome())) {

            path = path.replace(server.getDataManagerUsersHome() + "/", "");
            if (!path.startsWith(getUser().getFolder())) {
                logger.error("User {} tried to access data from another user: {}", getUser(), path);
                throw new VipException(ApplicationError.INVALID_WORKFLOW_INPUT, parameterName, "Access denied");
            }
        } else if (path.startsWith(server.getDataManagerGroupsHome())) {

            path = path.replace(server.getDataManagerGroupsHome() + "/", "");
            if (path.contains("/")) {
                path = path.substring(0, path.indexOf("/"));
            }

            if (!DataManagerUtil.getPaths(getUser().getGroups()).contains(path)) {
                logger.error("User {} tried to access data from a non-authorized group: {}", getUser(), path);
                throw new VipException(ApplicationError.INVALID_WORKFLOW_INPUT, parameterName, "Access denied");
            }
        }
    }
}
