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

import static fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

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
import fr.insalyon.creatis.vip.application.client.bean.Activity;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProcessorStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputM2Parser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.ScuflParser;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.EngineDAO;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UsersGroupsDAO;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Service
@Transactional
public class WorkflowBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private SimulationStatsDAO simulationStatsDAO;
    private WorkflowDAO workflowDAO;
    private ProcessorDAO processorDAO;
    private OutputDAO outputDAO;
    private InputDAO inputDAO;
    private StatsDAO statsDAO;
    private EngineDAO engineDAO;
    private ApplicationDAO applicationDAO;
    private UsersGroupsDAO usersGroupsDAO;
    private EngineBusiness engineBusiness;
    private DataManagerBusiness dataManagerBusiness;
    private EmailBusiness emailBusiness;
    private LfcPathsBusiness lfcPathsBusiness;
    private GRIDAPoolClient gridaPoolClient;
    private GRIDAClient gridaClient;
    private ExternalPlatformBusiness externalPlatformBusiness;
    private Engine selectedEngine;

    @Autowired
    public WorkflowBusiness(
            Server server, SimulationStatsDAO simulationStatsDAO,
            WorkflowDAO workflowDAO, ProcessorDAO processorDAO,
            OutputDAO outputDAO, InputDAO inputDAO, StatsDAO statsDAO,
            EngineDAO engineDAO, ApplicationDAO applicationDAO,
            UsersGroupsDAO usersGroupsDAO, EngineBusiness engineBusiness,
            DataManagerBusiness dataManagerBusiness, EmailBusiness emailBusiness,
            LfcPathsBusiness lfcPathsBusiness, GRIDAPoolClient gridaPoolClient,
            GRIDAClient gridaClient, ExternalPlatformBusiness externalPlatformBusiness) {
        this.server = server;
        this.simulationStatsDAO = simulationStatsDAO;
        this.workflowDAO = workflowDAO;
        this.processorDAO = processorDAO;
        this.outputDAO = outputDAO;
        this.inputDAO = inputDAO;
        this.statsDAO = statsDAO;
        this.engineDAO = engineDAO;
        this.applicationDAO = applicationDAO;
        this.usersGroupsDAO = usersGroupsDAO;
        this.engineBusiness = engineBusiness;
        this.dataManagerBusiness = dataManagerBusiness;
        this.emailBusiness = emailBusiness;
        this.lfcPathsBusiness = lfcPathsBusiness;
        this.gridaPoolClient = gridaPoolClient;
        this.gridaClient = gridaClient;
        this.externalPlatformBusiness = externalPlatformBusiness;
    }

    /*
    The 4 next dependencies cannot be injected by spring in a classic way as
    they cannot be singleton (spring default scope). A new instance must be
    created at each use and so we use the prototype scope with lookup methods
    to inject them.
    They need to be injected by spring (and not created with "new") so spring
    can handle their own dependencies.
     */
    @Lookup
    protected WorkflowExecutionBusiness getWorkflowExecutionBusiness(String endpoint) {
        // will be generated by spring to return a new instance each time
        return null;
    }

    @Lookup
    protected GwendiaParser getGwendiaParser() {
        // will be generated by spring to return a new instance each time
        return null;
    }

    @Lookup
    protected ScuflParser getScuflParser() {
        // will be generated by spring to return a new instance each time
        return null;
    }

    @Lookup
    protected InputM2Parser getInputM2Parser(String currentUserFolder) {
        // will be generated by spring to return a new instance each time
        return null;
    }

    private Engine selectEngine(String applicationClass)
            throws BusinessException {
        long min = Integer.MAX_VALUE;
        Engine engineBean = null;
        try {
            List<Engine> availableEngines = engineDAO.getByClass(applicationClass);
            for (Engine engine : availableEngines) {
                long runningWorkflows = workflowDAO.getNumberOfRunningPerEngine(engine.getEndpoint());
                if (runningWorkflows < min) {
                    min = runningWorkflows;
                    engineBean = engine;
                }
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error finding an engine for {}", applicationClass, ex);
        }
        if (engineBean == null || engineBean.getEndpoint().isEmpty()) {
            logger.error("No available engines for class {}", applicationClass);
            throw new BusinessException("No available engines for class " + applicationClass);
        } else {
            selectedEngine = engineBean;
            return engineBean;
        }
    }

    private Engine selectRandomEngine(String applicationClass)
            throws BusinessException {
        Engine engineBean = null;
        try {
            List<Engine> availableEngines = engineDAO.getByClass(applicationClass);
            Random randomizer = new Random();
            engineBean = availableEngines.get(randomizer.nextInt(availableEngines.size()));

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
        if (engineBean == null || engineBean.getEndpoint().isEmpty()) {
            logger.error("No available engines for class {}", applicationClass);
            throw new BusinessException("No available engines for class " + applicationClass);
        }

        return engineBean;
    }

    public synchronized String launch(
            User user, List<String> groups, Map<String, String> parametersMap,
            String applicationName, String applicationVersion,
            String applicationClass, String simulationName)
            throws BusinessException {

        try {
            long runningWorkflows = workflowDAO.getNumberOfRunning(user.getFullName());
            long runningSimulations=workflowDAO.getRunning().size();
            if(runningSimulations >= server.getMaxPlatformRunningSimulations()){
                logger.warn("Unable to launch execution '{}': max number of"
                        + " running workflows reached in the platform : {}",
                        simulationName, runningSimulations);
                throw new BusinessException(PLATFORM_MAX_EXECS);
            }
            if (runningWorkflows >= user.getMaxRunningSimulations()) {

                logger.warn("Unable to launch execution '{}': max number of "
                        + "running workflows reached ({}/{}) for user '{}'.",
                        simulationName, runningWorkflows,
                        user.getMaxRunningSimulations(), user);
                throw new BusinessException(USER_MAX_EXECS, runningWorkflows);
            }

            List<ParameterSweep> parameters = new ArrayList<ParameterSweep>();
            for (String name : parametersMap.keySet()) {

                ParameterSweep ps = new ParameterSweep(name);
                String valuesStr = parametersMap.get(name);
                if (valuesStr.contains(ApplicationConstants.SEPARATOR_INPUT)) {

                    String[] values = valuesStr.split(ApplicationConstants.SEPARATOR_INPUT);
                    if (values.length != 3) {
                        throw new fr.insalyon.creatis.vip.core.server.business.BusinessException("Error in range.");
                    }

                    Double start = Double.parseDouble(values[0]);
                    Double stop = Double.parseDouble(values[1]);
                    Double step = Double.parseDouble(values[2]);
                    for (double d = start; d <= stop; d += step) {
                        ps.addValue(d + "");
                    }

                } else if (valuesStr.contains(ApplicationConstants.SEPARATOR_LIST)) {

                    String[] values = valuesStr.split(ApplicationConstants.SEPARATOR_LIST);
                    for (String v : values) {

                        String parsedParameter =
                            parseParameter(user, groups, name, v);
                        ps.addValue(parsedParameter);
                    }
                } else {
                    String parsedParameter =
                        parseParameter(user, groups, name, valuesStr);
                    ps.addValue(parsedParameter);
                }
                parameters.add(ps);
            }

            AppVersion version = applicationDAO.getVersion(
                    applicationName, applicationVersion);
            Engine engine = selectEngine(applicationClass);
            String workflowPath;
            workflowPath = dataManagerBusiness.getRemoteFile(user, version.getLfn());
            if (selectedEngine != null && selectedEngine.getName().equals("moteurlite")) {
                workflowPath = dataManagerBusiness.getRemoteFile(user, version.getJsonLfn());
            }
            else {
                workflowPath = dataManagerBusiness.getRemoteFile(user, version.getLfn());
            }
            //selectRandomEngine could also be used; TODO: make this choice configurable
            
            WorkflowExecutionBusiness executionBusiness =
                    getWorkflowExecutionBusiness(engine.getEndpoint());
            Workflow workflow = null;
            try {
                workflow = executionBusiness.launch(applicationName,
                        applicationVersion, applicationClass, user, simulationName,
                        workflowPath, parameters);
            } catch (BusinessException be) {
                logger.error("BusinessException caught on launch workflow, engine {} will be disabled", engine.getName());
            } finally {
                if (workflow == null) {
                    engine.setStatus("disabled");
                    this.engineBusiness.update(engine);
                    for (User u : usersGroupsDAO
                            .getUsersFromGroup(CoreConstants.GROUP_SUPPORT)) {
                        logger.info("Sending warning email to user " + u.toString() + " having email address " + u.getEmail());
                        emailBusiness.sendEmail("Urgent: VIP engine disabled",
                                "Engine " + engine.getName() + " has just been disabled. Please check that there is at least one active engine left.",
                                new String[]{u.getEmail()}, true, user.getEmail());
                    }
                    throw new BusinessException("Workflow is null, engine " + engine.getName() + " has been disabled");
                }else{
                    logger.info("Launched workflow "+workflow.toString());
                }
            }

            workflowDAO.add(workflow);
            return workflow.getId();

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error launching simulation {}", simulationName, ex);
            throw new BusinessException(ex);
        } catch (DAOException | DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    private String parseParameter(
            User user, List<String> groups, String parameterName, String parameterValue)
            throws DataManagerException, BusinessException {

        parameterValue = parameterValue.trim();

        ExternalPlatformBusiness.ParseResult parseResult =
            externalPlatformBusiness.parseParameter(
                parameterName, parameterValue, user);
        if (parseResult.isUri) {
            // The uri has been generated
            return parseResult.result;
        }
        // not an external platform parameter, use legacy format
        String parsedPath = lfcPathsBusiness.parseBaseDir(
                user, parameterValue);
        if (!user.isSystemAdministrator()) {
            checkFolderACL(user, groups, parsedPath);
        }
        if ( ! parsedPath.equals(parameterValue) // the parameter is a file path
                && server.useLocalFilesInInputs()) {
            parsedPath = "file:" + parsedPath;
        }
        return parsedPath;
    }

    public List<Simulation> getSimulations(User user, Date lastDate)
            throws BusinessException {

        try {
            return parseWorkflows(workflowDAO.get(user != null ? user.getFullName() : null, lastDate));

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulations for {} since {}", user, lastDate, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * Get the simulation information
     *
     */
    public List<Simulation> getSimulations(
            String userName, String application, String status, String appClass,
            Date startDate, Date endDate) throws BusinessException {

        try {
            if (endDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();
            }

            WorkflowStatus wStatus = null;
            if (status != null) {
                wStatus = WorkflowStatus.valueOf(status);
            }

            List<Simulation> simulations = parseWorkflows(
                    workflowDAO.get(
                            userName, application, wStatus,
                            appClass, startDate, endDate));
            checkRunningSimulations(simulations);

            return simulations;

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error searching simulations for {}", userName, ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * Get the simulation information
     *
     * @param users list of users
     * @param status Simulation status to filter the request
     */
    public List<Simulation> getSimulations(
            List<String> users, String application, String status,
            String appClass, Date startDate, Date endDate)
            throws BusinessException {

        try {
            if (endDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();
            }

            WorkflowStatus wStatus = null;
            if (status != null) {
                wStatus = WorkflowStatus.valueOf(status);
            }

            List<Simulation> simulations = parseWorkflows(
                    workflowDAO.get(
                            users, application, wStatus,
                            appClass, startDate, endDate));
            checkRunningSimulations(simulations);

            return simulations;

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error searching simulations for users {}", users, ex);
            throw new BusinessException(ex);
        }
    }

    public Descriptor getApplicationDescriptor(
            User user, String applicationName, String applicationVersion)
            throws BusinessException {

        try {
            AppVersion version = applicationDAO.getVersion(applicationName, applicationVersion);
            String workflowPath = dataManagerBusiness.getRemoteFile(user, version.getLfn());
            workflowPath = dataManagerBusiness.getRemoteFile(user, version.getLfn());
            if (selectedEngine != null && selectedEngine.getName().equals("moteurlite")) {
                workflowPath = dataManagerBusiness.getRemoteFile(user, version.getJsonLfn());
            }
            else {
                workflowPath = dataManagerBusiness.getRemoteFile(user, version.getLfn());
            }
            return workflowPath.endsWith(".gwendia")
                    ? getGwendiaParser().parse(workflowPath)
                    : getScuflParser().parse(workflowPath);

        } catch (SAXException | IOException ex) {
            logger.error("Error getting application descriptor for {}/{}",
                    applicationName, applicationVersion, ex);
            throw new BusinessException(WRONG_APPLICATION_DESCRIPTOR, ex,
                    applicationName + "/" + applicationVersion);
        } catch (DAOException | BusinessException ex) {
            throw new BusinessException(WRONG_APPLICATION_DESCRIPTOR, ex,
                    applicationName + "/" + applicationVersion);
        }
    }

    public void kill(String simulationID) throws BusinessException {

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Killed);
            workflowDAO.update(workflow);
            WorkflowExecutionBusiness executionBusiness = getWorkflowExecutionBusiness(workflow.getEngine());
            executionBusiness.kill(simulationID);

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

    public Map<String, String> relaunch(
            String simulationID, String currentUserFolder)
            throws BusinessException {

        //TODO fix
        Map<String, String> inputs =
            getInputM2Parser(currentUserFolder).parse(
                server.getWorkflowsPath() + "/" + simulationID + "/input-m2.xml");

        return inputs;
    }

    public Simulation getSimulation(String simulationID) throws BusinessException {
        return getSimulation(simulationID, false);
    }

    public Simulation getSimulation(String simulationID, boolean refresh)
            throws BusinessException {

        Simulation simulation = null;
        try {
            Workflow workflow = workflowDAO.get(simulationID);
            if (workflow == null) {
                logger.error("Cannot find execution with id {}", simulationID);
                throw new BusinessException("Cannot find execution with id " + simulationID);
            }
            simulation = new Simulation(
                    workflow.getApplication(),
                    workflow.getApplicationVersion(),
                    workflow.getApplicationClass(),
                    workflow.getId(),
                    workflow.getUsername(),
                    workflow.getStartedTime(),
                    workflow.getDescription(),
                    workflow.getStatus().name(),
                    workflow.getEngine());
            if (refresh) {
                checkRunningSimulations(Collections.singletonList(simulation));
            }

        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting simulation {}", simulationID, ex);
            throw new BusinessException(ex);
        }

        return simulation;
    }


    public List<InOutData> getOutputData(
            String simulationID, String currentUserFolder) throws BusinessException {
        List<InOutData> list = new ArrayList<InOutData>();
        try {
            for (Output output : outputDAO.get(simulationID)) {
                String path = lfcPathsBusiness.parseRealDir(
                        output.getOutputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, output.getOutputID().getProcessor(),
                        output.getType().name()));
            }
        } catch (WorkflowsDBDAOException | DataManagerException ex) {
            logger.error("Error getting output data for {}", simulationID, ex);
            throw new BusinessException(ex);
        }
        return list;
    }

    public List<InOutData> getInputData(
            String simulationID, String currentUserFolder)
            throws BusinessException {

        try {
            List<InOutData> list = new ArrayList<InOutData>();
            for (Input input : inputDAO.get(simulationID)) {
                String path = lfcPathsBusiness.parseRealDir(
                    input.getInputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, input.getInputID().getProcessor(),
                        input.getType().name()));
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
            List<Activity> list = new ArrayList<Activity>();
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

        List<String> workflowIDList = new ArrayList<String>();
        List<String> stats = new ArrayList<String>();
        if (simulationIDList != null) {
            for (int i = 0; i < simulationIDList.size(); i++) {
                //logger.error("Stat module, id is "+simulationIDList.get(i).getID());
                workflowIDList.add(simulationIDList.get(i).getID());
            }
        } else {
            logger.error("Incorrect call of getPerformanceStats : Execution list is null");
            throw new BusinessException("Execution list is null!");
        }

        if (workflowIDList != null && !workflowIDList.isEmpty()) {
            try {
                switch (type) {
                    case 1:
                        stats = simulationStatsDAO.getBySimulationID(workflowIDList);
                        break;
                    case 2:
                        stats = simulationStatsDAO.getWorkflowsPerUser(workflowIDList);
                        break;
                    case 3:
                        stats = simulationStatsDAO.getApplications(workflowIDList);
                        break;
                    case 4:
                        stats = simulationStatsDAO.getClasses(workflowIDList);

                }


            } catch (DAOException ex) {
                throw new BusinessException(ex);
            }

        } else {
            logger.error("Internall error in getPerformanceStats : workflowIDList is null");
            throw new BusinessException("Empty workflow list!");
        }
        return stats;
    }

    public void validateInputs(User user, List<String> inputs)
            throws BusinessException {

        try {

            StringBuilder sb = new StringBuilder();
            for (String input : inputs) {
                if (!gridaClient.exist(
                        lfcPathsBusiness.parseBaseDir(user, input))) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }

                    sb.append(
                        lfcPathsBusiness.parseBaseDir(user, input));
                }
            }

            if (sb.length() > 0) {
                logger.error("The following data does not exist: " + sb.toString());
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException(
                        "The following data does not exist: " + sb.toString());
            }
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error validating inputs for {}", user, ex);
            throw new BusinessException(ex);
        }
    }

    public void updateUser(String currentUser, String newUser)
            throws BusinessException {

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
            if (path.indexOf("/") != -1) {
                path = path.substring(0, path.indexOf("/"));
            }

            if (!DataManagerUtil.getPaths(groups).contains(path)) {
                logger.error("User {} tried to access data from a non-autorized group: {}", user, path);
                throw new BusinessException("Access denied to group '" + path + "'.");
            }
        }
    }

    private List<Simulation> parseWorkflows(List<Workflow> list) {

        List<Simulation> simulationsList = new ArrayList<Simulation>();
        for (Workflow workflow : list) {
            simulationsList.add(new Simulation(
                    workflow.getApplication(),
                    workflow.getApplicationVersion(),
                    workflow.getApplicationClass(),
                    workflow.getId(),
                    workflow.getUsername(),
                    workflow.getStartedTime(),
                    workflow.getDescription(),
                    workflow.getStatus().name(),
                    workflow.getEngine()));
        }
        return simulationsList;
    }

    private void checkRunningSimulations(List<Simulation> simulations)
            throws BusinessException, WorkflowsDBDAOException {

        for (Simulation simulation : simulations) {

            if (simulation.getStatus() == SimulationStatus.Running
                    || simulation.getStatus() == SimulationStatus.Unknown) {
                WorkflowExecutionBusiness executionBusiness = getWorkflowExecutionBusiness(simulation.getEngine());
                SimulationStatus simulationStatus = executionBusiness.getStatus(simulation.getID());

                if (simulationStatus != SimulationStatus.Running
                        && simulationStatus != SimulationStatus.Unknown) {
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

    public void changeSimulationUser(String simulationId, String user)
            throws BusinessException {
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
