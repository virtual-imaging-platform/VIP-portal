package fr.insalyon.creatis.vip.application.server.business;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Input;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Output;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Processor;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.Workflow;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.WorkflowStatus;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.ProcessorDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.StatsDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.ApplicationError;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProcessorStatus;
import fr.insalyon.creatis.vip.application.models.Activity;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.InOutData;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.models.Simulation;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputFileParser;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;

@Service
@Transactional
public class WorkflowBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Server server;
    private final SimulationStatsDAO simulationStatsDAO;
    private final WorkflowDAO workflowDAO;
    private final ProcessorDAO processorDAO;
    private final OutputDAO outputDAO;
    private final InputDAO inputDAO;
    private final StatsDAO statsDAO;
    private final ApplicationDAO applicationDAO;
    private final AppVersionBusiness appVersionBusiness;
    private final EngineBusiness engineBusiness;
    private final ResourceBusiness resourceBusiness;
    private final EmailBusiness emailBusiness;
    private final LfcPathsBusiness lfcPathsBusiness;
    private final GRIDAPoolClient gridaPoolClient;
    private final GRIDAClient gridaClient;
    private final ExternalPlatformBusiness externalPlatformBusiness;
    private final WorkflowExecutionBusiness workflowExecutionBusiness;
    private final ConfigurationBusiness configurationBusiness;
    private final BoutiquesBusiness boutiquesBusiness;

    @Autowired
    public WorkflowBusiness(
            Server server, SimulationStatsDAO simulationStatsDAO,
            WorkflowDAO workflowDAO, ProcessorDAO processorDAO,
            OutputDAO outputDAO, InputDAO inputDAO, StatsDAO statsDAO,
            ApplicationDAO applicationDAO, EngineBusiness engineBusiness,
            EmailBusiness emailBusiness,
            LfcPathsBusiness lfcPathsBusiness, GRIDAPoolClient gridaPoolClient,
            GRIDAClient gridaClient, ExternalPlatformBusiness externalPlatformBusiness,
            ResourceBusiness resourceBusiness, AppVersionBusiness appVersionBusiness,
            WorkflowExecutionBusiness workflowExecutionBusiness, ConfigurationBusiness configurationBusiness,
            BoutiquesBusiness boutiquesBusiness) {
        this.server = server;
        this.simulationStatsDAO = simulationStatsDAO;
        this.workflowDAO = workflowDAO;
        this.processorDAO = processorDAO;
        this.outputDAO = outputDAO;
        this.inputDAO = inputDAO;
        this.statsDAO = statsDAO;
        this.applicationDAO = applicationDAO;
        this.engineBusiness = engineBusiness;
        this.emailBusiness = emailBusiness;
        this.lfcPathsBusiness = lfcPathsBusiness;
        this.gridaPoolClient = gridaPoolClient;
        this.gridaClient = gridaClient;
        this.externalPlatformBusiness = externalPlatformBusiness;
        this.resourceBusiness = resourceBusiness;
        this.appVersionBusiness = appVersionBusiness;
        this.workflowExecutionBusiness = workflowExecutionBusiness;
        this.configurationBusiness = configurationBusiness;
        this.boutiquesBusiness = boutiquesBusiness;
    }

    /*
    The next dependency cannot be injected by spring in a classic way as
    it cannot be singleton (spring default scope). A new instance must be
    created at each use and so we use the prototype scope with lookup methods
    to inject it.
    It needs to be injected by spring (and not created with "new") so spring
    can handle its own dependencies.
     */
    @Lookup
    protected InputFileParser getInputFileParser(String currentUserFolder) {
        // will be generated by spring to return a new instance each time
        return null;
    }

    public synchronized String launch(User user, List<String> groups, Map<String, String> parametersMap,
            String appName, String version, String simulationName) throws VipException {
        Workflow workflow = null;

        try {
            checkVIPCapacities(user, simulationName);

            AppVersion appVersion = appVersionBusiness.getVersion(appName, version);
            Map<String, List<String>> parameters = getParameters(appVersion.getDescriptor(), parametersMap, user, groups);

            List<Resource> resources = resourceBusiness.getUsableResources(user, appVersion);
            if (resources.isEmpty()) {
                throw new VipException("There are no ressources available for the moment !");
            }

            Resource resource = resources.get(0);
            Engine engine = engineBusiness.selectEngine(engineBusiness.getUsableEngines(resource));

            appVersion.getSettings().put(ApplicationConstants.DEFAULT_EXECUTOR_GASW, resource.getType().toString());
            try {
                workflow = workflowExecutionBusiness.launch(engine.getEndpoint(), appVersion, user, simulationName, parameters, resource.getConfiguration());
            } catch (Exception e) {
                String mailSubject = "[VIP] Warn: Workflow submission failed!!";
                String mailContent = "An error occured while submitting a workflow";
                Exception exceptionToRethrow = e;

                if (e instanceof VipException vipEx && vipEx.getVipErrorCode().isEmpty()) {
                    // intended errors, only warning by mail
                    logger.warn("Error occuring during workflow submission. Not disabling, sending mail to admins");
                } else {
                    if ( ! (e instanceof VipException)) {
                        logger.error("Unexpected exception while launching a workflow", e);
                        exceptionToRethrow = new VipException(ApplicationError.LAUNCH_ERROR, e);
                    }
                    logger.warn(
                            "Error occuring during workflow submission. Disabling engine and sending mail to admins");
                    mailSubject = "[VIP] Urgent: VIP engine disabled !";
                    mailContent = "Engine " + engine.getName() + " has just been disabled.";
                    engine.setStatus("disabled");
                    engineBusiness.update(engine);
                }
                mailContent += "\n\nException:" + e.getMessage() + "\nStacktrace: " + e.getStackTrace();
                emailBusiness.sendEmailToAdmins(mailSubject, mailContent, true, user.getEmail());
                throw (VipException) exceptionToRethrow;
            }
            logger.info("Launched workflow " + workflow.toString());

            workflowDAO.add(workflow);
            return workflow.getId();

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error launching simulation {}", simulationName, ex);
            throw new VipException(ex);
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        }
    }

    private void checkVIPCapacities(User user, String simulationName) throws VipException, WorkflowsDBDAOException {
        long runningWorkflows = workflowDAO.getNumberOfRunning(user.getFullName());
        long runningSimulations = workflowDAO.getRunning().size();

        if (runningSimulations >= server.getMaxPlatformRunningSimulations()) {
            logger.warn("Unable to launch execution '{}': max number of"
                    + " running workflows reached in the platform : {}",
                    simulationName, runningSimulations);
            throw new VipException(ApplicationError.PLATFORM_MAX_EXECS);
        } else if (runningWorkflows >= user.getMaxRunningSimulations()) {
            logger.warn("Unable to launch execution '{}': max number of "
                    + "running workflows reached ({}/{}) for user '{}'.",
                    simulationName, runningWorkflows,
                    user.getMaxRunningSimulations(), user);
            throw new VipException(ApplicationError.USER_MAX_EXECS, runningWorkflows);
        }
    }

    private Map<String, List<String>> getParameters(String boutiquesDescriptor, Map<String, String> parametersMap, User user, List<String> groups)
            throws DataManagerException, VipException {
        Map<String, List<String>> parameters = new HashMap<>();
        BoutiquesDescriptor boutiques = boutiquesBusiness.parseBoutiquesString(boutiquesDescriptor);

        for (String name : parametersMap.keySet()) {
            List<String> data = new ArrayList<>();
            String valuesStr = parametersMap.get(name);

            if (valuesStr.contains(ApplicationConstants.SEPARATOR_INPUT)) {
                String[] values = valuesStr.split(ApplicationConstants.SEPARATOR_INPUT);
                if (values.length != 3) {
                    throw new VipException("Error in range.");
                }

                Double start = Double.parseDouble(values[0]);
                Double stop = Double.parseDouble(values[1]);
                Double step = Double.parseDouble(values[2]);
                for (double d = start; d <= stop; d += step) {
                    data.add(d + "");
                }

            } else if (valuesStr.contains(ApplicationConstants.SEPARATOR_LIST)) {
                String[] values = valuesStr.split(ApplicationConstants.SEPARATOR_LIST);

                for (String v : values) {
                    data.add(transformParameter(boutiques, user, groups, name, v));
                }
            } else {
                // this is used to filter optional empty values
                if (valuesStr != null) {
                    data.add(transformParameter(boutiques, user, groups, name, valuesStr));
                }
            }
            if ( ! data.isEmpty()) {
                parameters.put(name, data);
            }
        }
        return parameters;
    }

    private String transformParameter(BoutiquesDescriptor boutiques, User user, List<String> groups, String parameterName, String parameterValue)
            throws DataManagerException, VipException {

        parameterValue = parameterValue.trim();

        if ( ! isAFileParameter(boutiques, parameterName)) {
            return parameterValue;
        }

        ExternalPlatformBusiness.ParseResult parseResult = externalPlatformBusiness
            .parseParameter(parameterName, parameterValue, user);
        if (parseResult.isUri) {
            // The uri has been generated
            return parseResult.result;
        }
        // not an external platform parameter, use legacy format
        String parsedPath = lfcPathsBusiness.parseBaseDir(user, parameterValue);
        if ( ! user.isSystemAdministrator()) {
            checkFolderACL(user, groups, parsedPath);
        }
        return (server.useLocalFilesInInputs() ? "file:" : "lfn:") + parsedPath;
    }

    private boolean isAFileParameter(BoutiquesDescriptor boutiques, String parameterName) {
        Set<fr.insalyon.creatis.boutiques.model.Input> inputs = boutiques.getInputs();

        if (CoreConstants.RESULTS_DIRECTORY_PARAM_NAME.equals(parameterName)) {
            return true;
        }
        return inputs.stream().anyMatch(input ->
                parameterName.equals(input.getId())
                        && fr.insalyon.creatis.boutiques.model.Input.Type.FILE.equals(input.getType()));
    }

    public List<Simulation> getSimulations(User user, Date lastDate) throws VipException {
        try {
            return parseWorkflows(workflowDAO.get(user != null ? user.getFullName() : null, lastDate));

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulations for {} since {}", user, lastDate, ex);
            throw new VipException(ex);
        }
    }

    public List<Simulation> getSimulations(String userName, String application, String status,
            Date startDate, Date endDate) throws VipException {

        return getSimulations(userName, application, status, startDate, endDate, null);
    }

    public List<Simulation> getSimulations(String userName, String application, String status, Date startDate, Date endDate, String tag) throws VipException {
        WorkflowStatus wStatus = (status != null) ? WorkflowStatus.valueOf(status) : null;
        List<String> users = (userName != null) ? Collections.singletonList(userName) : Collections.emptyList();
        List<String> applications = (application != null) ? Collections.singletonList(application) : new ArrayList<>();
        List<Simulation> simulations = new ArrayList<>();

        try {
            if (endDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();
            }

            simulations = parseWorkflows(workflowDAO.get(users, applications, wStatus, null, startDate, endDate, tag));
            checkRunningSimulations(simulations);

            return simulations;

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error searching simulations for users {}", users, ex);
            throw new VipException(ex);
        }
    }

    private List<Workflow> getSimulationsAdminGroup(String userEmail, List<String> filterApplications, 
            WorkflowStatus filterStatus, Date filterStartDate, Date filterEndDate, String filterTag) throws WorkflowsDBDAOException, DAOException, VipException {

        Set<Group> adminGroups = new HashSet<>();
        Set<String> adminApps = new HashSet<>();

        configurationBusiness.getUserGroups(userEmail).forEach((group, role) -> {
            if (role.equals(GROUP_ROLE.Admin) && group.getType().equals(GroupType.APPLICATION)) {
                adminGroups.add(group);
            }
        });

        for (Group group : adminGroups) {
            for (Application app : applicationDAO.getApplicationsByGroup(group)) {
                if (filterApplications.isEmpty() || filterApplications.contains(app.getName())) {
                    adminApps.add(app.getName()); 
                }
            }
        }

        if ( ! adminApps.isEmpty()) {
            return workflowDAO.get(null, new ArrayList<>(adminApps), filterStatus, null, filterStartDate, filterEndDate, filterTag);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Simulation> getSimulationsWithGroupAdminRights(User user, String application, String status, Date startDate, Date endDate, String tag)
            throws VipException {
        
        WorkflowStatus wStatus = (status != null) ? WorkflowStatus.valueOf(status) : null;
        List<String> users = List.of(user.getFullName());
        List<String> applications = (application != null) ? Collections.singletonList(application) : new ArrayList<>();

        List<Simulation> simulations = new ArrayList<>();
        List<Workflow> workflows = new ArrayList<>();

        try {
            if (endDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();
            }

            workflows.addAll(getSimulationsAdminGroup(user.getEmail(), applications, wStatus, startDate, endDate, tag));
            workflows.addAll(workflowDAO.get(users, applications, wStatus, null, startDate, endDate, tag));

            // this is to avoid duplicates + sort them by date
            workflows = new ArrayList<>(workflows.stream()
                    .collect(Collectors.toMap(Workflow::getId, w -> w, (e, r) -> e)).values())
                    .stream().sorted(Comparator.comparing(Workflow::getStartedTime).reversed())
                    .collect(Collectors.toList());

            simulations = parseWorkflows(workflows);
            checkRunningSimulations(simulations);

            return simulations;

        } catch (WorkflowsDBDAOException | DAOException ex) {
            logger.error("Error searching simulations for users {}", users, ex);
            throw new VipException(ex);
        }
    }

    public void kill(String simulationID) throws VipException {

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Killed);
            workflowDAO.update(workflow);
            workflowExecutionBusiness.kill(workflow.getEngine(), simulationID);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error killing simulation {}", simulationID, ex);
            throw new VipException(ex);
        }
    }

    public void clean(String simulationID, String email, boolean deleteFiles)
            throws VipException {

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Cleaned);
            workflowDAO.update(workflow);
            if(deleteFiles){
                for (Output output : outputDAO.get(simulationID)) {
                    gridaPoolClient.delete(output.getOutputID().getPath(), email);
                }
            }
            inputDAO.removeById(simulationID);
            outputDAO.removeById(simulationID);

        } catch (WorkflowsDBDAOException | GRIDAClientException ex) {
            logger.error("Error cleaning simulation {}", simulationID, ex);
            throw new VipException(ex);
        }
    }

    public void clean(String simulationId, String email) throws VipException{
        clean(simulationId,email,true);
    }

    public void purge(String simulationID) throws VipException {
        try {
            workflowDAO.removeById(simulationID);
            processorDAO.removeById(simulationID);
            inputDAO.removeById(simulationID);
            outputDAO.removeById(simulationID);
            statsDAO.removeById(simulationID);
            String workflowsPath = server.getWorkflowsPath();
            File workflowDir = new File(workflowsPath, simulationID);
            FileUtils.deleteQuietly(workflowDir);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error purging simulation {}", simulationID, ex);
            throw new VipException(ex);
        }
    }

    public Map<String, String> relaunch(String simulationID, String currentUserFolder) throws VipException {
        return getInputFileParser(currentUserFolder).parse(Path.of(server.getWorkflowsPath() + "/" + simulationID + "/inputs"));
    }

    public Simulation getSimulation(String simulationID) throws VipException {
        return getSimulation(simulationID, false);
    }

    public Simulation getSimulation(String simulationID, boolean refresh) throws VipException {
        Simulation simulation;

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            if (workflow == null) {
                logger.error("Cannot find execution with id {}", simulationID);
                throw new VipException("Cannot find execution with id " + simulationID);
            }
            simulation = parseWorkflow(workflow);
            if (refresh) {
                checkRunningSimulations(Collections.singletonList(simulation));
            }

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulation {}", simulationID, ex);
            throw new VipException(ex);
        }

        return simulation;
    }


    public List<InOutData> getOutputData(String simulationID, String currentUserFolder)
            throws VipException {
        List<InOutData> list = new ArrayList<>();
        try {
            for (Output output : outputDAO.get(simulationID)) {
                String path = lfcPathsBusiness.parseRealDir(output.getOutputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, output.getOutputID().getProcessor(), output.getType().name()));
            }
        } catch (WorkflowsDBDAOException | DataManagerException ex) {
            logger.error("Error getting output data for {}", simulationID, ex);
            throw new VipException(ex);
        }
        return list;
    }

    public List<InOutData> getInputData(String simulationID, String currentUserFolder) throws VipException {
        try {
            List<InOutData> list = new ArrayList<>();
            for (Input input : inputDAO.get(simulationID)) {
                String path = lfcPathsBusiness.parseRealDir(input.getInputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, input.getInputID().getProcessor(), input.getType().name()));
            }
            return list;

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting input data for {}", simulationID, ex);
            throw new VipException(ex);
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        }
    }

    public void deleteLogData(String path) throws VipException {
        try {
            File file = new File(server.getWorkflowsPath(), path);
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else if (!file.delete()) {
                logger.error("Unable to delete log : {}", path);
                throw new VipException("Unable to delete data: " + path);
            }
        } catch (java.io.IOException ex) {
            logger.error("Error deleting log data for {}", path, ex);
            throw new VipException(ex);
        }
    }

    public List<Activity> getProcessors(String simulationID) throws VipException {
        try {
            List<Activity> list = new ArrayList<>();
            for (Processor processor : processorDAO.get(simulationID)) {

                ProcessorStatus status = ProcessorStatus.Unstarted;

                if (processor.getCompleted() + processor.getQueued() + processor.getFailed() > 0) {
//                    if (failed > 0) {
//                        status = ProcessorStatus.Failed;
//                    } else
                    if (processor.getQueued() > 0) {
                        status = ProcessorStatus.Active;
                    } else {
                        status = ProcessorStatus.Completed;
                    }
                }
                list.add(new Activity(processor.getProcessorID().getProcessor(),
                        status, processor.getCompleted(), processor.getQueued(),
                        processor.getFailed()));
            }
            return list;

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting processors for {}", simulationID, ex);
            throw new VipException(ex);
        }
    }

    public List<String> getPerformanceStats(
            List<Simulation> simulationIDList, int type)
            throws VipException, WorkflowsDBDAOException {

        if (simulationIDList == null || simulationIDList.isEmpty()) {
            logger.error("Incorrect call of getPerformanceStats : Execution list is null or empty : {}", simulationIDList);
            throw new VipException("Error getting performance stats");
        }

        List<String> workflowIDList = simulationIDList.stream().map(Simulation::getID).collect(Collectors.toList());

        try {
            switch (type) {
                case 1: return simulationStatsDAO.getBySimulationID(workflowIDList);
                case 2: return simulationStatsDAO.getWorkflowsPerUser(workflowIDList);
                case 3: return simulationStatsDAO.getApplications(workflowIDList);
                default:
                    logger.error("Unsupported type to get performance stats : {}", type);
                    throw new VipException("Error getting performance stats");
            }
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public void validateInputs(User user, List<String> inputs) throws VipException {
        try {
            StringBuilder sb = new StringBuilder();
            for (String input : inputs) {
                if ( ! gridaClient.exist(lfcPathsBusiness.parseBaseDir(user, input))) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(lfcPathsBusiness.parseBaseDir(user, input));
                }
            }

            if (sb.length() > 0) {
                logger.error("The following data does not exist: " + sb);
                throw new VipException(
                        "The following data does not exist: " + sb);
            }
        } catch (DataManagerException ex) {
            throw new VipException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error validating inputs for {}", user, ex);
            throw new VipException(ex);
        }
    }

    public void updateUser(String currentUser, String newUser) throws VipException {
        try {
            workflowDAO.updateUsername(newUser, currentUser);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error updating username from {} to {}", currentUser, newUser, ex);
            throw new VipException(ex);
        }
    }

    public void updateDescription(String simulationID, String newDescription)
            throws VipException {
        try {
            Workflow w= workflowDAO.get(simulationID);
            w.setDescription(newDescription);
            workflowDAO.update(w);
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error updating description for {} to {}", simulationID, newDescription, ex);
            throw new VipException(ex);
        }
    }

    public List<Simulation> getRunningSimulations() throws VipException {
        try {
            return parseWorkflows(workflowDAO.getRunning());

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting all running simulations", ex);
            throw new VipException(ex);
        }
    }

    private void checkFolderACL(User user, List<String> groups, String path)
            throws VipException {
        if (path.startsWith(server.getDataManagerUsersHome())) {

            path = path.replace(server.getDataManagerUsersHome() + "/", "");
            if (!path.startsWith(user.getFolder())) {

                logger.error("User {} tried to access data from another user: {}", user, path);
                throw new VipException("Access denied to another user's home.");
            }
        } else if (path.startsWith(server.getDataManagerGroupsHome())) {

            path = path.replace(server.getDataManagerGroupsHome() + "/", "");
            if (path.contains("/")) {
                path = path.substring(0, path.indexOf("/"));
            }

            if (!DataManagerUtil.getPaths(groups).contains(path)) {
                logger.error("User {} tried to access data from a non-autorized group: {}", user, path);
                throw new VipException("Access denied to group '" + path + "'.");
            }
        }
    }

    private List<Simulation> parseWorkflows(List<Workflow> list) {
        return list.stream().map(this::parseWorkflow).collect(Collectors.toList());
    }

    private Simulation parseWorkflow(Workflow workflow) {
        return new Simulation(
                    workflow.getApplication(),
                    workflow.getApplicationVersion(),
                    workflow.getApplicationClass(),
                    workflow.getId(),
                    workflow.getUsername(),
                    workflow.getStartedTime(),
                    workflow.getDescription(),
                    workflow.getStatus().name(),
                    workflow.getEngine(),
                    workflow.getTags());
    }

    private void checkRunningSimulations(List<Simulation> simulations) throws VipException, WorkflowsDBDAOException {
        for (Simulation simulation : simulations) {

            if (simulation.getStatus() == SimulationStatus.Running
                    || simulation.getStatus() == SimulationStatus.Unknown) {
                SimulationStatus simulationStatus = workflowExecutionBusiness.getStatus(simulation.getEngine(), simulation.getID());
                logger.debug("Simulation {} : old status : {}, new status : {} ",
                        simulation.getID(), simulation.getStatus(), simulationStatus);

                if (simulationStatus != simulation.getStatus()) {
                    simulation.setStatus(simulationStatus);
                    Workflow workflow = workflowDAO.get(simulation.getID());
                    workflow.setStatus(WorkflowStatus.valueOf(simulationStatus.name()));
                    workflowDAO.update(workflow);
                }
            }
        }
    }

    public void markCompleted(String simulationID) throws VipException {
        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Completed);
            workflowDAO.update(workflow);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error marking simulation {} completed", simulationID, ex);
            throw new VipException(ex);
        }
    }

    public void changeSimulationUser(String simulationId, String user) throws VipException {
        try {
            Workflow workflow = workflowDAO.get(simulationId);
            workflow.setUsername(user);
            workflowDAO.update(workflow);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error changing simulation {} owner to {}", simulationId, user, ex);
            throw new VipException(ex);
        }
    }
}
