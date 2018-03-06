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
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOFactory;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Activity;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProcessorStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputM2Parser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.ScuflParser;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAO;
import fr.insalyon.creatis.vip.application.server.dao.SimulationStatsDAOFactory;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class WorkflowBusiness {

    private static final Logger logger = Logger.getLogger(WorkflowBusiness.class);
    private static ApplicationDAO applicationDB;
    private static SimulationStatsDAO simulationStatsDAO;
    private static WorkflowDAO workflowDAO;
    private static ProcessorDAO processorDAO;
    private static OutputDAO outputDAO;
    private static InputDAO inputDAO;
    private static StatsDAO statsDAO;
    private final EngineBusiness engineBusiness;

    public WorkflowBusiness() {

        engineBusiness = new EngineBusiness();
        try {
            applicationDB = ApplicationDAOFactory.getDAOFactory().getApplicationDAO();
            simulationStatsDAO = SimulationStatsDAOFactory.getInstance().getSimulationStatsDAO();
            workflowDAO = WorkflowsDBDAOFactory.getInstance().getWorkflowDAO();
            processorDAO = WorkflowsDBDAOFactory.getInstance().getProcessorDAO();
            outputDAO = WorkflowsDBDAOFactory.getInstance().getOutputDAO();
            inputDAO = WorkflowsDBDAOFactory.getInstance().getInputDAO();
            statsDAO = WorkflowsDBDAOFactory.getInstance().getStatsDAO();

        } catch (DAOException ex) {
            logger.error(ex);
        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
        }
    }

    private Engine selectEngine(String applicationClass) throws BusinessException {
        long min = Integer.MAX_VALUE;
        Engine engineBean = null;
        try {
            List<Engine> availableEngines = ApplicationDAOFactory.getDAOFactory().getEngineDAO().getByClass(applicationClass);
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
            java.util.logging.Logger.getLogger(WorkflowBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (engineBean == null || engineBean.getEndpoint().isEmpty()) {
            throw new BusinessException("No available engines for class " + applicationClass);
        } else {
            return engineBean;
        }      
    }
    
    private Engine selectRandomEngine(String applicationClass) throws BusinessException {
        
        Engine engineBean = null;
        try {
            List<Engine> availableEngines = ApplicationDAOFactory.getDAOFactory().getEngineDAO().getByClass(applicationClass);
            Random randomizer = new Random();
            engineBean = availableEngines.get(randomizer.nextInt(availableEngines.size()));

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
        if (engineBean == null || engineBean.getEndpoint().isEmpty()) {
            throw new BusinessException("No available engines for class " + applicationClass);
        } 
        
        return engineBean;   
    }
    
    /**
     *
     * @param user
     * @param groups
     * @param parametersMap
     * @param applicationName
     * @param applicationVersion
     * @param applicationClass
     * @param simulationName
     * @return
     * @throws BusinessException
     */
    public synchronized String launch(User user, List<String> groups,
            Map<String, String> parametersMap, String applicationName,
            String applicationVersion, String applicationClass,
            String simulationName) throws BusinessException {

        try {
            long runningWorkflows = workflowDAO.getNumberOfRunning(user.getFullName());
            long runningSimulations=workflowDAO.getRunning().size();
            if(runningSimulations >= Server.getInstance().getMaxPlatformRunningSimulations()){
            logger.warn("Unable to launch execution '" + simulationName + "': max "
                        + "number of running workflows reached in the platform.");
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException(
                        "Max number of running executions reached.");
            }
            if (runningWorkflows >= user.getMaxRunningSimulations()) {

                logger.warn("Unable to launch execution '" + simulationName + "': max "
                        + "number of running workflows reached for user '" + user + "'.");
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException(
                        "Max number of running executions reached.<br />You already have "
                        + runningWorkflows + " running executions.");
            }

            StringBuilder inputsJsonFile = new StringBuilder("{\n");
            int parametersMapSize = parametersMap.size() ;
            int treatedParameters = 0 ;
            boolean isLastParameter = false ;
            

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

                        String parsedPath = DataManagerUtil.parseBaseDir(user, v.trim());
                        if (!user.isSystemAdministrator()) {
                            checkFolderACL(user, groups, parsedPath);
                        }
                        ps.addValue(parsedPath);
                    }
                } else {

                    String parsedPath = DataManagerUtil.parseBaseDir(user, valuesStr.trim());
                    if (!user.isSystemAdministrator()) {
                        checkFolderACL(user, groups, parsedPath);
                    }
                    ps.addValue(parsedPath);
                }
                parameters.add(ps);
         
                treatedParameters +=1;
                
                if(treatedParameters == parametersMapSize) {
                    isLastParameter = true;
                }
                logger.info("************* ParameterName : " +  ps.getParameterName() + " ps.getValues().get(0) : " + ps.getValues().get(0));
                addInputLineInJson(ps, inputsJsonFile, isLastParameter);

            }
            
            inputsJsonFile.append("}");
            
            String inputsJsonFileString = new String(inputsJsonFile.toString());
            logger.info("************* JSON : " +  inputsJsonFileString);
            String inputFieldJson = new String(applicationName + "_inputs.json");

            AppVersion version = applicationDB.getVersion(applicationName, applicationVersion);
            DataManagerBusiness dmBusiness = new DataManagerBusiness();
            String workflowPath = dmBusiness.getRemoteFile(user, version.getLfn(),
                    Server.getInstance().getConfigurationFolder() + "workflows/"
                    + FilenameUtils.getName(version.getLfn()));

            // Write application json 
             try {
                    File jsonFile = new File(inputFieldJson);
                     PrintWriter writer = new PrintWriter(jsonFile, "UTF-8");
                    writer.write(inputsJsonFileString);
                    writer.close();
            
                    uploadFile(inputFieldJson, workflowPath);
             } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    logger.error(ex);
                     throw new BusinessException(ex);     
             }
            
            //selectRandomEngine could also be used; TODO: make this choice configurable
            Engine engine = selectEngine(applicationClass);
            WorkflowExecutionBusiness executionBusiness = new WorkflowExecutionBusiness(engine.getEndpoint());
            Workflow workflow = null;
            try {
                workflow = executionBusiness.launch(applicationName,
                        applicationVersion, applicationClass, user, simulationName,
                        workflowPath, parameters);
            } catch (BusinessException be) {
                be.printStackTrace();
                logger.error("BusinessException caught on launch workflow, engine " + engine.getName() + " will be disabled");
            } finally {
                if (workflow == null) {
                    engine.setStatus("disabled");
                    this.engineBusiness.update(engine);
                    for (User u : CoreDAOFactory.getDAOFactory().getUsersGroupsDAO().getUsersFromGroup(CoreConstants.GROUP_SUPPORT)) {
                        logger.info("Sending warning email to user " + u.toString() + " having email address " + u.getEmail());
                        CoreUtil.sendEmail("Urgent: VIP engine disabled",
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

        } catch (WorkflowsDBDAOException | DAOException | DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } 
    }

    /**
     *
     * @param user
     * @param lastDate
     * @return
     * @throws BusinessException
     */
    public List<Simulation> getSimulations(User user, Date lastDate) throws BusinessException {

        try {
            return parseWorkflows(workflowDAO.get(user != null ? user.getFullName() : null, lastDate));

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * Get the simulation information
     *
     * @param userName
     * @param application
     * @param status
     * @param startDate
     * @param endDate
     * @return
     * @throws BusinessException
     */
    public List<Simulation> getSimulations(String userName, String application,
            String status, String appClass, Date startDate, Date endDate) throws BusinessException {

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

            List<Simulation> simulations = parseWorkflows(workflowDAO.get(userName,
                    application, wStatus, appClass, startDate, endDate));
            checkRunningSimulations(simulations);

            return simulations;

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * Get the simulation information
     *
     * @param users list of users
     * @param application
     * @param status Simulation status to filter the request
     * @param startDate
     * @param endDate
     * @return
     * @throws BusinessException
     */
    public List<Simulation> getSimulations(List<String> users, String application,
            String status, String appClass, Date startDate, Date endDate) throws BusinessException {

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

            List<Simulation> simulations = parseWorkflows(workflowDAO.get(users,
                    application, wStatus, appClass, startDate, endDate));
            checkRunningSimulations(simulations);

            return simulations;

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param applicationName
     * @param applicationVersion
     * @return
     * @throws BusinessException
     */
    public Descriptor getApplicationDescriptor(User user, String applicationName,
            String applicationVersion) throws BusinessException {

        try {

            AppVersion version = applicationDB.getVersion(applicationName, applicationVersion);
            DataManagerBusiness dmBusiness = new DataManagerBusiness();
            String localDirectory = Server.getInstance().getConfigurationFolder()
                    + "workflows/"
                    + FilenameUtils.getPath(version.getLfn()) + "/"
                    + FilenameUtils.getName(version.getLfn());
            String workflowPath = dmBusiness.getRemoteFile(user, version.getLfn(), localDirectory);
            return workflowPath.endsWith(".gwendia")
                    ? new GwendiaParser().parse(workflowPath)
                    : new ScuflParser().parse(workflowPath);

        } catch (org.xml.sax.SAXException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (java.io.IOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @throws BusinessException
     */
    public void kill(String simulationID) throws BusinessException {

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Killed);
            workflowDAO.update(workflow);
            WorkflowExecutionBusiness executionBusiness = new WorkflowExecutionBusiness(workflow.getEngine());
            executionBusiness.kill(simulationID);

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param email
     * @param deleteFiles
     * @throws BusinessException
     */
    public void clean(String simulationID, String email, boolean deleteFiles) throws BusinessException {

        try {
            Workflow workflow = workflowDAO.get(simulationID);
            workflow.setStatus(WorkflowStatus.Cleaned);
            workflowDAO.update(workflow);
            if(deleteFiles){
                GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
                for (Output output : outputDAO.get(simulationID)) {
                    client.delete(output.getOutputID().getPath(), email);
                }
            }
            inputDAO.removeById(simulationID);
            outputDAO.removeById(simulationID);

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
    
    /**
     *
     * @param simulationId
     * @param email
     * @throws BusinessException
     */
    public void clean(String simulationId, String email) throws BusinessException{
        clean(simulationId,email,true);
    }

    /**
     *
     * @param simulationID
     * @throws BusinessException
     */
    public void purge(String simulationID) throws BusinessException {

        try {
            workflowDAO.removeById(simulationID);
            processorDAO.removeById(simulationID);
            inputDAO.removeById(simulationID);
            outputDAO.removeById(simulationID);
            statsDAO.removeById(simulationID);
            String workflowsPath = Server.getInstance().getWorkflowsPath();
            File workflowDir = new File(workflowsPath, simulationID);
            FileUtils.deleteQuietly(workflowDir);

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public Map<String, String> relaunch(String simulationID, String currentUserFolder)
            throws BusinessException {

        //TODO fix
        Map<String, String> inputs = new InputM2Parser(currentUserFolder).parse(
                Server.getInstance().getWorkflowsPath() + "/" + simulationID + "/input-m2.xml");

        return inputs;
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
    public Simulation getSimulation(String simulationID) throws BusinessException {
        return getSimulation(simulationID, false);
    }

    /**
     *
     * @param simulationID
     * @param refresh
     * @return
     * @throws BusinessException
     */
    public Simulation getSimulation(String simulationID, boolean refresh) throws BusinessException {

        Simulation simulation = null;
        try {
            Workflow workflow = workflowDAO.get(simulationID);
            if (workflow == null) {
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
            logger.error(ex);
            throw new BusinessException(ex);
        }

        return simulation;
    }

    /**
     *
     * @param simulationID
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public List<InOutData> getOutputData(String simulationID, String currentUserFolder)
            throws BusinessException {

        List<InOutData> list = new ArrayList<InOutData>();
        try {
            for (Output output : outputDAO.get(simulationID)) {
                String path = DataManagerUtil.parseRealDir(output.getOutputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, output.getOutputID().getProcessor(),
                        output.getType().name()));
            }
        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }

        return list;
    }

    /**
     *
     * @param simulationID
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public List<InOutData> getInputData(String simulationID, String currentUserFolder)
            throws BusinessException {

        try {
            List<InOutData> list = new ArrayList<InOutData>();
            for (Input input : inputDAO.get(simulationID)) {
                String path = DataManagerUtil.parseRealDir(input.getInputID().getPath(), currentUserFolder);
                list.add(new InOutData(path, input.getInputID().getProcessor(),
                        input.getType().name()));
            }
            return list;

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param path
     * @throws BusinessException
     */
    public void deleteLogData(String path) throws BusinessException {

        try {
            File file = new File(Server.getInstance().getWorkflowsPath(), path);
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);

            } else if (!file.delete()) {
                logger.error("Unable to delete data: " + path);
                throw new BusinessException("Unable to delete data: " + path);
            }
        } catch (java.io.IOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
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
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationIDList
     * @param type
     * @return
     * @throws BusinessException
     */
    public List<String> getPerformanceStats(List<Simulation> simulationIDList, int type)
            throws BusinessException, WorkflowsDBDAOException {

        List<String> workflowIDList = new ArrayList<String>();
        List<String> stats = new ArrayList<String>();
        if (simulationIDList != null) {
            for (int i = 0; i < simulationIDList.size(); i++) {
                //logger.error("Stat module, id is "+simulationIDList.get(i).getID());
                workflowIDList.add(simulationIDList.get(i).getID());
            }
        } else {
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
                logger.error(ex);
                throw new BusinessException(ex);
            }

        } else {
            throw new BusinessException("Empty workflow list!");
        }
        return stats;
    }

    /**
     *
     * @param user
     * @param inputs
     * @throws BusinessException
     */
    public void validateInputs(User user, List<String> inputs)
            throws BusinessException {

        try {

            GRIDAClient client = CoreUtil.getGRIDAClient();
            StringBuilder sb = new StringBuilder();
            for (String input : inputs) {
                if (!client.exist(DataManagerUtil.parseBaseDir(user, input))) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }

                    sb.append(DataManagerUtil.parseBaseDir(user, input));
                }
            }

            if (sb.length() > 0) {
                logger.error("The following data does not exist: " + sb.toString());
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException(
                        "The following data does not exist: " + sb.toString());
            }
        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param currentUser
     * @param newUser
     * @throws BusinessException
     */
    public void updateUser(String currentUser, String newUser) throws BusinessException {

        try {
            workflowDAO.updateUsername(newUser, currentUser);

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void updateDescription(String simulationID, String newDescription) throws BusinessException {
        try {
            Workflow w= workflowDAO.get(simulationID);
            w.setDescription(newDescription);
            workflowDAO.update(w);
        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<Simulation> getRunningSimulations() throws BusinessException {


        try {
            return parseWorkflows(workflowDAO.getRunning());

        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param user
     * @param groups
     * @param path
     * @throws BusinessException
     */
    private void checkFolderACL(User user, List<String> groups, String path)
            throws BusinessException {


        if (path.startsWith(Server.getInstance().getDataManagerUsersHome())) {

            path = path.replace(Server.getInstance().getDataManagerUsersHome() + "/", "");
            if (!path.startsWith(user.getFolder())) {

                logger.error("User '" + user + "' tried to access data from another user: " + path + "");
                throw new BusinessException("Access denied to another user's home.");
            }
        } else if (path.startsWith(Server.getInstance().getDataManagerGroupsHome())) {

            path = path.replace(Server.getInstance().getDataManagerGroupsHome() + "/", "");
            if (path.indexOf("/") != -1) {
                path = path.substring(0, path.indexOf("/"));
            }

            if (!DataManagerUtil.getPaths(groups).contains(path)) {
                logger.error("User '" + user + "' tried to access data from a non-autorized group: " + path + "");
                throw new BusinessException("Access denied to group '" + path + "'.");
            }
        }
    }

   
    
    /**
     *
     * @param list
     * @return
     */
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

    /**
     *
     * @param simulations
     * @throws BusinessException
     * @throws WorkflowsDBDAOException
     */
    private void checkRunningSimulations(List<Simulation> simulations)
            throws BusinessException, WorkflowsDBDAOException {

        for (Simulation simulation : simulations) {

            if (simulation.getStatus() == SimulationStatus.Running
                    || simulation.getStatus() == SimulationStatus.Unknown) {
                WorkflowExecutionBusiness executionBusiness = new WorkflowExecutionBusiness(simulation.getEngine());
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
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    public void changeSimulationUser(String simulationId, String user) throws BusinessException {
        try {
            Workflow workflow = workflowDAO.get(simulationId);
            workflow.setUsername(user);
            workflowDAO.update(workflow);


        } catch (WorkflowsDBDAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
    
    public StringBuilder addInputLineInJson(ParameterSweep ps, StringBuilder inputsJsonFile, boolean isLastParameter) {
        StringBuilder inputLine = new StringBuilder();
         if(!ps.getParameterName().equals("results-directory")) {
            StringBuilder value;
            if(ps.getValues().get(0).contains("/")) {
                int index = ps.getValues().get(0).lastIndexOf("/");
                logger.info("****************** INDEX : "+ index);
                value = new StringBuilder(".");
                value.append(ps.getValues().get(0).substring(index));
                logger.info("****************** ps.getValues().get(0).substring(index) : "+ ps.getValues().get(0).substring(index));
            }
            else {
                value = new StringBuilder(ps.getValues().get(0));
            }
            
            inputLine = new StringBuilder("\t\"" + ps.getParameterName() + "\"" + " : " + "\"" + value + "\"");
            if (!isLastParameter) {
                inputLine.append(",");
            }
            inputLine.append("\n");
        }
        return inputsJsonFile.append(inputLine);
    }
    
     private void uploadFile(String localFile, String lfn) throws BusinessException {
        try {
            GRIDAClient gc = CoreUtil.getGRIDAClient();
            logger.info("Uploading file " + localFile + " to " + lfn);
            if (gc.exist(lfn)) {
                gc.delete(lfn);
            }
            gc.uploadFile(localFile, (new File(lfn)).getParent());
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
    
}
