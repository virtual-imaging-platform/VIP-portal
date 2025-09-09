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
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.*;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProcessorStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputFileParser;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError.*;

/**
 *
 * @author Rafael Ferreira da Silva
 */
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
            String appName, String version, String simulationName) throws BusinessException {
        Workflow workflow = null;

        try {
            checkVIPCapacities(user, simulationName);

            AppVersion appVersion = appVersionBusiness.getVersion(appName, version);
            Map<String, List<String>> parameters = getParameters(appVersion.getDescriptor(), parametersMap, user, groups);

            List<Resource> resources = resourceBusiness.getUsableResources(user, appVersion);
            if (resources.isEmpty()) {
                throw new BusinessException("There are no ressources available for the moment !");
            }

            Resource resource = resources.get(0);
            Engine engine = engineBusiness.selectEngine(engineBusiness.getUsableEngines(resource));

            appVersion.getSettings().put(ApplicationConstants.DEFAULT_EXECUTOR_GASW, resource.getType().toString());
            try {
                workflow = workflowExecutionBusiness.launch(engine.getEndpoint(), appVersion, user, simulationName, parameters, resource.getConfiguration());
            } catch (BusinessException be) {
                logger.error("BusinessException caught on launch workflow, engine {} will be disabled", engine.getName());
            } catch (Exception e) {
                logger.error("Unexpected exception caught on launch workflow, engine {} will be disabled", engine.getName(), e);
            } finally {
                if (workflow == null) {
                    engine.setStatus("disabled");
                    engineBusiness.update(engine);

                    logger.info("Sending warning email to admins !");
                    emailBusiness.sendEmailToAdmins(
                        "Urgent: VIP engine disabled", 
                        "Engine " + engine.getName() + " has just been disabled. Please check that there is at least one active engine left.", 
                        true, user.getEmail());
                    throw new BusinessException("Workflow is null, engine " + engine.getName() + " has been disabled");
                } else {
                    logger.info("Launched workflow " + workflow.toString());
                }
            }

            workflowDAO.add(workflow);
            return workflow.getId();

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error launching simulation {}", simulationName, ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    private void checkVIPCapacities(User user, String simulationName) throws BusinessException, WorkflowsDBDAOException {
        long runningWorkflows = workflowDAO.getNumberOfRunning(user.getFullName());
        long runningSimulations = workflowDAO.getRunning().size();

        if (runningSimulations >= server.getMaxPlatformRunningSimulations()) {
            logger.warn("Unable to launch execution '{}': max number of"
                    + " running workflows reached in the platform : {}",
                    simulationName, runningSimulations);
            throw new BusinessException(PLATFORM_MAX_EXECS);
        } else if (runningWorkflows >= user.getMaxRunningSimulations()) {
            logger.warn("Unable to launch execution '{}': max number of "
                    + "running workflows reached ({}/{}) for user '{}'.",
                    simulationName, runningWorkflows,
                    user.getMaxRunningSimulations(), user);
            throw new BusinessException(USER_MAX_EXECS, runningWorkflows);
        }
    }

    private Map<String, List<String>> getParameters(String boutiquesDescriptor, Map<String, String> parametersMap, User user, List<String> groups)
            throws DataManagerException, BusinessException {
        Map<String, List<String>> parameters = new HashMap<>();
        BoutiquesDescriptor boutiques = boutiquesBusiness.parseBoutiquesString(boutiquesDescriptor);

        for (String name : parametersMap.keySet()) {
            List<String> data = new ArrayList<>();
            String valuesStr = parametersMap.get(name);

            if (valuesStr.contains(ApplicationConstants.SEPARATOR_INPUT)) {
                String[] values = valuesStr.split(ApplicationConstants.SEPARATOR_INPUT);
                if (values.length != 3) {
                    throw new BusinessException("Error in range.");
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
            throws DataManagerException, BusinessException {

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

    public List<Simulation> getSimulations(User user, Date lastDate) throws BusinessException {
        try {
            return parseWorkflows(workflowDAO.get(user != null ? user.getFullName() : null, lastDate));

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulations for {} since {}", user, lastDate, ex);
            throw new BusinessException(ex);
        }
    }

    public List<Simulation> getSimulations(String userName, String application, String status,
            Date startDate, Date endDate) throws BusinessException {

        return getSimulations(userName, application, status, startDate, endDate, null);
    }

    public List<Simulation> getSimulations(String userName, String application, String status, Date startDate, Date endDate, String tag) throws BusinessException {
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
            throw new BusinessException(ex);
        }
    }

    private List<Workflow> getSimulationsAdminGroup(String userEmail, List<String> filterApplications, 
            WorkflowStatus filterStatus, Date filterStartDate, Date filterEndDate, String filterTag) throws WorkflowsDBDAOException, DAOException, BusinessException {

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
            throws BusinessException {
        
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
            throw new BusinessException(ex);
        }
    }

    public void kill(String simulationID) throws BusinessException {

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Killed);
            workflowDAO.update(workflow);
            workflowExecutionBusiness.kill(workflow.getEngine(), simulationID);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error killing simulation {}", simulationID, ex);
            throw new BusinessException(ex);
        }
    }

    public void clean(String simulationID, String email, boolean deleteFiles)
            throws BusinessException {

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
            throw new BusinessException(ex);
        }
    }

    public void clean(String simulationId, String email) throws BusinessException{
        clean(simulationId,email,true);
    }

    public void purge(String simulationID) throws BusinessException {
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
            throw new BusinessException(ex);
        }
    }

    public Map<String, String> relaunch(String simulationID, String currentUserFolder) throws BusinessException {
        return getInputFileParser(currentUserFolder).parse(Path.of(server.getWorkflowsPath() + "/" + simulationID + "/inputs"));
    }

    public Simulation getSimulation(String simulationID) throws BusinessException {
        return getSimulation(simulationID, false);
    }

    public Simulation getSimulation(String simulationID, boolean refresh) throws BusinessException {
        Simulation simulation;

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            if (workflow == null) {
                logger.error("Cannot find execution with id {}", simulationID);
                throw new BusinessException("Cannot find execution with id " + simulationID);
            }
            simulation = parseWorkflow(workflow);
            if (refresh) {
                checkRunningSimulations(Collections.singletonList(simulation));
            }

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulation {}", simulationID, ex);
            throw new BusinessException(ex);
        }

        return simulation;
    }


    public List<InOutData> getOutputData(String simulationID, String currentUserFolder)
            throws BusinessException {
        List<InOutData> list = new ArrayList<>();
        try {
            for (Output output : outputDAO.get(simulationID)) {
                String path = lfcPathsBusiness.parseRealDir(output.getOutputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, output.getOutputID().getProcessor(), output.getType().name()));
            }
        } catch (WorkflowsDBDAOException | DataManagerException ex) {
            logger.error("Error getting output data for {}", simulationID, ex);
            throw new BusinessException(ex);
        }
        return list;
    }

    public List<InOutData> getInputData(String simulationID, String currentUserFolder) throws BusinessException {
        try {
            List<InOutData> list = new ArrayList<>();
            for (Input input : inputDAO.get(simulationID)) {
                String path = lfcPathsBusiness.parseRealDir(input.getInputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, input.getInputID().getProcessor(), input.getType().name()));
            }
            return list;

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting input data for {}", simulationID, ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    public void deleteLogData(String path) throws BusinessException {
        try {
            File file = new File(server.getWorkflowsPath(), path);
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else if (!file.delete()) {
                logger.error("Unable to delete log : {}", path);
                throw new BusinessException("Unable to delete data: " + path);
            }
        } catch (java.io.IOException ex) {
            logger.error("Error deleting log data for {}", path, ex);
            throw new BusinessException(ex);
        }
    }

    public List<Activity> getProcessors(String simulationID) throws BusinessException {
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
            throw new BusinessException(ex);
        }
    }

    public List<String> getPerformanceStats(
            List<Simulation> simulationIDList, int type)
            throws BusinessException, WorkflowsDBDAOException {

        if (simulationIDList == null || simulationIDList.isEmpty()) {
            logger.error("Incorrect call of getPerformanceStats : Execution list is null or empty : {}", simulationIDList);
            throw new BusinessException("Error getting performance stats");
        }

        List<String> workflowIDList = simulationIDList.stream().map(Simulation::getID).collect(Collectors.toList());

        try {
            switch (type) {
                case 1: return simulationStatsDAO.getBySimulationID(workflowIDList);
                case 2: return simulationStatsDAO.getWorkflowsPerUser(workflowIDList);
                case 3: return simulationStatsDAO.getApplications(workflowIDList);
                default:
                    logger.error("Unsupported type to get performance stats : {}", type);
                    throw new BusinessException("Error getting performance stats");
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void validateInputs(User user, List<String> inputs) throws BusinessException {
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
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException(
                        "The following data does not exist: " + sb);
            }
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error validating inputs for {}", user, ex);
            throw new BusinessException(ex);
        }
    }

    public void updateUser(String currentUser, String newUser) throws BusinessException {
        try {
            workflowDAO.updateUsername(newUser, currentUser);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error updating username from {} to {}", currentUser, newUser, ex);
            throw new BusinessException(ex);
        }
    }

    public void updateDescription(String simulationID, String newDescription)
            throws BusinessException {
        try {
            Workflow w= workflowDAO.get(simulationID);
            w.setDescription(newDescription);
            workflowDAO.update(w);
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error updating description for {} to {}", simulationID, newDescription, ex);
            throw new BusinessException(ex);
        }
    }

    public List<Simulation> getRunningSimulations() throws BusinessException {
        try {
            return parseWorkflows(workflowDAO.getRunning());

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting all running simulations", ex);
            throw new BusinessException(ex);
        }
    }

    private void checkFolderACL(User user, List<String> groups, String path)
            throws BusinessException {
        if (path.startsWith(server.getDataManagerUsersHome())) {

            path = path.replace(server.getDataManagerUsersHome() + "/", "");
            if (!path.startsWith(user.getFolder())) {

                logger.error("User {} tried to access data from another user: {}", user, path);
                throw new BusinessException("Access denied to another user's home.");
            }
        } else if (path.startsWith(server.getDataManagerGroupsHome())) {

            path = path.replace(server.getDataManagerGroupsHome() + "/", "");
            if (path.contains("/")) {
                path = path.substring(0, path.indexOf("/"));
            }

            if (!DataManagerUtil.getPaths(groups).contains(path)) {
                logger.error("User {} tried to access data from a non-autorized group: {}", user, path);
                throw new BusinessException("Access denied to group '" + path + "'.");
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

    private void checkRunningSimulations(List<Simulation> simulations) throws BusinessException, WorkflowsDBDAOException {
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

    public void markCompleted(String simulationID) throws BusinessException {
        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Completed);
            workflowDAO.update(workflow);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error marking simulation {} completed", simulationID, ex);
            throw new BusinessException(ex);
        }
    }

    public void changeSimulationUser(String simulationId, String user) throws BusinessException {
        try {
            Workflow workflow = workflowDAO.get(simulationId);
            workflow.setUsername(user);
            workflowDAO.update(workflow);

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error changing simulation {} owner to {}", simulationId, user, ex);
            throw new BusinessException(ex);
        }
    }
}
