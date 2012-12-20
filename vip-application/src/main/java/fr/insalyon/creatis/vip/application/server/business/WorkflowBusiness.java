/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Processor;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputM2Parser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.ScuflParser;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAO;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAOFactory;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class WorkflowBusiness {

    private static final Logger logger = Logger.getLogger(WorkflowBusiness.class);
    private WorkflowEngineInstantiator engine;
    private static WorkflowDAO workflowDB;
    private static ApplicationDAO applicationDB;

    public WorkflowBusiness() {

        try {

            workflowDB = WorkflowDAOFactory.getDAOFactory().getWorkflowDAO();
            applicationDB = ApplicationDAOFactory.getDAOFactory().getApplicationDAO();
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            logger.error(ex);
        }

        String executionMode = Server.getInstance().getWorflowsExecMode();
        engine = WorkflowEngineInstantiator.create(executionMode);
    }

    /**
     *
     * @param user
     * @return
     * @throws BusinessException
     */
    public List<Simulation> getSimulations(User user) throws BusinessException {

        try {
            return workflowDB.getList(user != null ? user.getFullName() : null);
        } catch (DAOException ex) {
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
            return workflowDB.getList(user != null ? user.getFullName() : null, lastDate);
        } catch (DAOException ex) {
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

        Descriptor descriptor = null;
        try {

            AppVersion version = applicationDB.getVersion(applicationName, applicationVersion);
            DataManagerBusiness dmBusiness = new DataManagerBusiness();
            String localDirectory = Server.getInstance().getConfigurationFolder()
                    + "workflows/"
                    + FilenameUtils.getPath(version.getLfn()) + "/"
                    + FilenameUtils.getName(version.getLfn());
            String workflowPath = dmBusiness.getRemoteFile(user, version.getLfn(), localDirectory);
            descriptor = workflowPath.endsWith(".gwendia")
                    ? new GwendiaParser().parse(workflowPath)
                    : new ScuflParser().parse(workflowPath);
        } catch (org.xml.sax.SAXException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (java.io.IOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }

        return descriptor;
    }

    /**
     *
     * @param user
     * @param groups
     * @param parametersMap
     * @param applicationName
     * @param applicationVersion
     * @param simulationName
     * @return
     * @throws BusinessException
     */
    public synchronized String launch(User user, List<String> groups,
            Map<String, String> parametersMap, String applicationName,
            String applicationVersion, String simulationName)
            throws BusinessException {

        String workflowID = null;
        try {

            int runningWorkflows = workflowDB.getRunningWorkflows(user.getFullName());
            if (runningWorkflows >= user.getLevel().getMaxRunningSimulations()) {

                logger.warn("Unable to launch simulation '" + simulationName + "': max "
                        + "number of running workflows reached for user '" + user + "'.");
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException(
                        "Max number of running simulations reached.<br />You already have "
                        + runningWorkflows + " running simulations.");
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
            }

            AppVersion version = applicationDB.getVersion(applicationName, applicationVersion);
            DataManagerBusiness dmBusiness = new DataManagerBusiness();
            String workflowPath = dmBusiness.getRemoteFile(user, version.getLfn(),
                    Server.getInstance().getConfigurationFolder() + "workflows/"
                    + FilenameUtils.getName(version.getLfn()));

            engine.setWorkflow(new File(workflowPath));
            engine.setInput(parameters);
            String launchID = engine.launch(Server.getInstance().getServerProxy(), null);
            workflowID = engine.getSimulationId(launchID);
            Simulation simulation = new Simulation(applicationName, applicationVersion,
                    workflowID, user.getFullName(), new Date(), simulationName,
                    engine.getMode().equalsIgnoreCase("pool") ? SimulationStatus.Queued.name() : SimulationStatus.Running.name());
            workflowDB.add(simulation);

        } catch (javax.xml.rpc.ServiceException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (java.rmi.RemoteException ex) {
//             do nothing!
        }

        return workflowID;
    }

    /**
     *
     * @param simulationID
     * @throws fr.insalyon.creatis.vip.core.server.business.BusinessException
     */
    public void kill(String simulationID)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        try {

            workflowDB.updateStatus(simulationID, SimulationStatus.Killed.name());
            engine.kill(simulationID);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (javax.xml.rpc.ServiceException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (java.rmi.RemoteException ex) {
            // do nothing!
        }
    }

    /**
     *
     * @param simulationID
     * @param email
     * @throws BusinessException
     */
    public void clean(String simulationID, String email)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        try {

            workflowDB.updateStatus(simulationID, SimulationStatus.Cleaned.name());
            List<String> outputs = workflowDB.getOutputs(simulationID);
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();
            for (String output : outputs) {
                client.delete(output, email);
            }
            workflowDB.cleanWorkflow(simulationID);

        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (fr.insalyon.creatis.grida.client.GRIDAClientException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @throws BusinessException
     */
    public void purge(String simulationID)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        try {

            workflowDB.delete(simulationID);
            String workflowsPath = Server.getInstance().getWorkflowsPath();
            File workflowDir = new File(workflowsPath, simulationID);
            FileUtils.deleteQuietly(workflowDir);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
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
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        //TODO fix
        Map<String, String> inputs = new InputM2Parser(currentUserFolder).parse(
                Server.getInstance().getWorkflowsPath() + "/" + simulationID + "/input-m2.xml");

        return inputs;
    }

    /**
     * Get the simulation information for a specific user
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
            String status, Date startDate, Date endDate)
            throws fr.insalyon.creatis.vip.core.server.business.BusinessException {

        List<Simulation> simulations = null;
        try {

            if (endDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();
            }

            simulations = workflowDB.getList(userName, application, status, startDate, endDate);
            for (Simulation simulation : simulations) {
                if (simulation.getStatus() == SimulationStatus.Running) {

                    SimulationStatus simulationStatus = this.getStatus(simulation.getID());
                    workflowDB.updateStatus(simulation.getID(), simulationStatus.name());
                    simulation.setStatus(simulationStatus);
                }
            }
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }

        return simulations;
    }

    /**
     * Get the simulation information for selected users
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
            String status, Date startDate, Date endDate)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        List<Simulation> simulations = null;
        try {

            if (endDate != null) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();
            }

            simulations = workflowDB.getList(users, application, status, startDate, endDate);
            for (Simulation simulation : simulations) {
                if (simulation.getStatus() == SimulationStatus.Running) {

                    SimulationStatus simulationStatus = this.getStatus(simulation.getID());
                    workflowDB.updateStatus(simulation.getID(), simulationStatus.name());
                    simulation.setStatus(simulationStatus);
                }
            }
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }

        return simulations;
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
    public Simulation getSimulation(String simulationID)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        Simulation simulation = null;
        try {
            simulation = workflowDB.get(simulationID);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }

        return simulation;
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws fr.insalyon.creatis.vip.core.server.business.BusinessException
     */
    public SimulationStatus getStatus(String simulationID)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        SimulationStatus status = SimulationStatus.Unknown;
        try {
            status = engine.getStatus(simulationID);
        } catch (javax.xml.rpc.ServiceException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (java.rmi.RemoteException ex) {
            // do nothing!
        }

        return status;
    }

    /**
     *
     * @param simulationID
     * @param currentUserFolder
     * @return
     * @throws BusinessException
     */
    public List<InOutData> getOutputData(String simulationID, String currentUserFolder)
            throws fr.insalyon.creatis.vip.core.server.business.BusinessException {

        List<InOutData> list = null;
        try {

            list = workflowDB.getInOutData(simulationID, "Outputs");
            for (InOutData data : list) {

                String path = DataManagerUtil.parseRealDir(data.getPath(), currentUserFolder);
                data.setPath(path);
            }
        } catch (fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
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
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        List<InOutData> list = null;
        try {

            list = workflowDB.getInOutData(simulationID, "Inputs");
            for (InOutData data : list) {

                String path = DataManagerUtil.parseRealDir(data.getPath(), currentUserFolder);
                data.setPath(path);
            }
        } catch (fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }

        return list;
    }

    /**
     *
     * @param path
     * @throws BusinessException
     */
    public void deleteLogData(String path)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        File file = new File(Server.getInstance().getWorkflowsPath(), path);
        if (file.isDirectory()) {

            try {
                FileUtils.deleteDirectory(file);
            } catch (java.io.IOException ex) {
                WorkflowBusiness.logAndThrow(ex);
            }
        } else {
            if (!file.delete()) {

                logger.error("Unable to delete data: " + path);
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException("Unable to delete data: " + path);
            }
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
    public List<Processor> getProcessors(String simulationID)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        List<Processor> list = null;
        try {
            list = workflowDB.getProcessors(simulationID);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }

        return list;
    }

    /**
     *
     * @param simulationIDList
     * @param type
     * @param binSize
     * @return
     * @throws BusinessException
     */
    public String getPerformanceStats(List<Simulation> simulationIDList, int type)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        String stats = null;
        try {
            switch (type) {
                case 1:

                    stats = workflowDB.getTimeAnalysis(simulationIDList);
                    break;
                case 2:

                    stats = workflowDB.getJobStatuses(simulationIDList);
                    break;
                default:

                    logger.error("Type '" + type + "' not supported.");
                    throw new fr.insalyon.creatis.vip.core.server.business.BusinessException("Type '" + type + "' not supported.");
            }
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
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
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

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
        } catch (fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException ex) {
            WorkflowBusiness.logAndThrow(ex);
        } catch (fr.insalyon.creatis.grida.client.GRIDAClientException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }
    }

    /**
     *
     * @param currentUser
     * @param newUser
     * @throws BusinessException
     */
    public void updateUser(String currentUser, String newUser)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        try {
            workflowDB.updateUser(currentUser, newUser);
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }
    }

    /**
     *
     * @return @throws BusinessException
     */
    public List<Simulation> getRunningSimulations()
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        List<Simulation> list = null;
        try {
            list = workflowDB.getRunningWorkflows();
        } catch (fr.insalyon.creatis.vip.core.server.dao.DAOException ex) {
            WorkflowBusiness.logAndThrow(ex);
        }

        return list;
    }

    private void checkFolderACL(User user, List<String> groups, String path)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {


        if (path.startsWith(Server.getInstance().getDataManagerUsersHome())) {

            path = path.replace(Server.getInstance().getDataManagerUsersHome() + "/", "");
            if (!path.startsWith(user.getFolder())) {

                logger.error("User '" + user + "' tried to access data from another user: " + path + "");
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException("Access denied to another user's home.");
            }
        } else if (path.startsWith(Server.getInstance().getDataManagerGroupsHome())) {

            path = path.replace(Server.getInstance().getDataManagerGroupsHome() + "/", "");
            if (path.indexOf("/") != -1) {
                path = path.substring(0, path.indexOf("/"));
            }

            if (!groups.contains(path)) {

                logger.error("User '" + user + "' tried to access data from a non-autorized group: " + path + "");
                throw new fr.insalyon.creatis.vip.core.server.business.BusinessException("Access denied to group '" + path + "'.");
            }
        }
    }

    private static void logAndThrow(Exception ex)
            throws
            fr.insalyon.creatis.vip.core.server.business.BusinessException {

        logger.error(ex);
        throw new fr.insalyon.creatis.vip.core.server.business.BusinessException(ex);
    }
}
