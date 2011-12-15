/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
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
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.MoteurStatus;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Processor;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.server.business.simulation.MoteurPoolConfig;
import fr.insalyon.creatis.vip.application.server.business.simulation.MoteurWSConfig;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.ScuflParser;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAOFactory;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.ServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowBusiness {

    private static final Logger logger = Logger.getLogger(WorkflowBusiness.class);

    /**
     * 
     * @param user
     * @param applicationName
     * @return
     * @throws BusinessException 
     */
    public Descriptor getApplicationDescriptor(String user, String applicationName)
            throws BusinessException {

        try {

            Application app = ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getApplication(applicationName);

            DataManagerBusiness dmBusiness = new DataManagerBusiness();
            String workflowPath = dmBusiness.getRemoteFile(user, app.getLfn(),
                    Server.getInstance().getConfigurationFolder()
                    + "workflows/"
                    + FilenameUtils.getPath(app.getLfn()) + "/"
                    + FilenameUtils.getName(app.getLfn()));

            if (workflowPath.endsWith(".gwendia")) {
                return new GwendiaParser().parse(workflowPath);
            } else {
                return new ScuflParser().parse(workflowPath);
            }

        } catch (IOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (SAXException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param parametersMap
     * @param applicationName
     * @param simulationName
     * @return
     * @throws BusinessException 
     */
    public String launch(String user, List<String> groups, boolean validParameters,
            Map<String, String> parametersMap, String applicationName,
            String simulationName) throws BusinessException {

        try {
            WorkflowDAO workflowDAO = WorkflowDAOFactory.getDAOFactory().getWorkflowDAO();
            int runningWorkflows = workflowDAO.getRunningWorkflows(user);

            if (runningWorkflows >= Server.getInstance().getMoteurMaxWorkflows()) {
                logger.warn("Unable to launch simulation '" + simulationName + "': max "
                        + "number of running workflows reached for user '" + user + "'.");
                throw new BusinessException("Max number of running simulations reached.<br />You "
                        + "already have " + runningWorkflows + " running simulations.");
            }

            String settings = "GRID=DIRAC\n"
                    + "SE=ccsrm02.in2p3.fr\n"
                    + "TIMEOUT=100000\n"
                    + "RETRYCOUNT=3\n"
                    + "MULTIJOB=1";

            List<ParameterSweep> parameters = new ArrayList<ParameterSweep>();
            for (String name : parametersMap.keySet()) {
                ParameterSweep ps = new ParameterSweep(name);
                String valuesStr = parametersMap.get(name);

                if (valuesStr.contains("##")) {
                    String[] values = valuesStr.split("##");
                    if (values.length != 3) {
                        throw (new BusinessException("Error in range."));
                    }
                    Double start = Double.parseDouble(values[0]);
                    Double stop = Double.parseDouble(values[1]);
                    Double step = Double.parseDouble(values[2]);

                    for (double d = start; d <= stop; d += step) {
                        ps.addValue(d + "");
                    }
                } else if (valuesStr.contains("@@")) {
                    String[] values = valuesStr.split("@@");
                    for (String v : values) {
                        String parsedPath = DataManagerUtil.parseBaseDir(user, v.trim());
                        if (validParameters) {
                            checkFolderACL(user, groups, parsedPath);
                        }
                        ps.addValue(parsedPath);
                    }
                } else {
                    String parsedPath = DataManagerUtil.parseBaseDir(user, valuesStr.trim());
                    if (validParameters) {
                        checkFolderACL(user, groups, parsedPath);
                    }
                    ps.addValue(parsedPath);
                }
                parameters.add(ps);
            }

            Application app = ApplicationDAOFactory.getDAOFactory().getApplicationDAO().getApplication(applicationName);

            DataManagerBusiness dmBusiness = new DataManagerBusiness();
            String workflowPath = dmBusiness.getRemoteFile(user, app.getLfn(),
                    Server.getInstance().getConfigurationFolder() + "workflows/"
                    + FilenameUtils.getName(app.getLfn()));

            String workflowID = null;

//            String status = "Running";
//            if (Server.getInstance().getMoteurExecuterConfig().toUpperCase().equals("POOL")) {
//
//                MoteurPoolConfig pool = new MoteurPoolConfig(workflowPath, parameters);
//                pool.setSettings(settings);
//                workflowID = "shiwa-instance-" + pool.launch(Server.getInstance().getServerProxy(), userDN);
//                status = "Queued";
//                
//            } else {
            MoteurWSConfig moteur = new MoteurWSConfig(workflowPath, parameters);
            moteur.setAddressWS(Server.getInstance().getMoteurServer());
            moteur.setSettings(settings);
            String ws = moteur.launch(Server.getInstance().getServerProxy(), null);
            workflowID = ws.substring(ws.lastIndexOf("/") + 1, ws.lastIndexOf("."));
//            }

            workflowDAO.add(new Simulation(applicationName, workflowID, user,
                    new Date(), simulationName, SimulationStatus.Running.name()));

            return workflowID;

        } catch (ServiceException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (RemoteException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
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
//            WorkflowMoteurConfig moteur = new WorkflowMoteurConfig(
//                    Server.getInstance().getMoteurServer());
//            moteur.kill(simulationID);

            if (isShiwaPoolID(simulationID)) {
                MoteurPoolConfig pool = new MoteurPoolConfig();
                pool.kill(simulationID);

            } else {
                MoteurWSConfig ws = new MoteurWSConfig();
                ws.setAddressWS(Server.getInstance().getMoteurServer());
                ws.kill(simulationID);
            }

        } catch (RemoteException ex) {
            // ignore
        } catch (ServiceException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param email
     * @throws BusinessException 
     */
    public void clean(String simulationID, String email) throws BusinessException {

        try {
            WorkflowDAO workflowDAO = WorkflowDAOFactory.getDAOFactory().getWorkflowDAO();
            workflowDAO.updateStatus(simulationID, SimulationStatus.Cleaned.name());
            String simulationsPath = Server.getInstance().getWorkflowsPath();
            File workflowDir = new File(simulationsPath + "/" + simulationID);

            for (File file : workflowDir.listFiles()) {
                if (!file.getName().equals("jobs.db")
                        && !file.getName().equals("workflow.out")
                        && !file.getName().equals("workflow.err")
                        && !file.getName().equals("gasw.log")) {

                    FileUtils.deleteQuietly(file);
                }
            }
            List<String> outputs = workflowDAO.getOutputs(simulationID);
            GRIDAPoolClient client = CoreUtil.getGRIDAPoolClient();

            for (String output : outputs) {
                client.delete(output, email);
            }
            workflowDAO.cleanWorkflow(simulationID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @throws BusinessException
     */
    public void purge(String simulationID) throws BusinessException {

        try {
            WorkflowDAO workflowDAO = WorkflowDAOFactory.getDAOFactory().getWorkflowDAO();
            workflowDAO.delete(simulationID);

            String workflowsPath = Server.getInstance().getWorkflowsPath();
            File workflowDir = new File(workflowsPath + "/" + simulationID);
            FileUtils.deleteQuietly(workflowDir);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
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
            String status, Date startDate, Date endDate) throws BusinessException {

        try {
            if (endDate != null) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(endDate);
                c1.add(Calendar.DATE, 1);
                endDate = c1.getTime();
            }
            WorkflowDAO workflowDAO = WorkflowDAOFactory.getDAOFactory().getWorkflowDAO();
            List<Simulation> simulations = workflowDAO.getList(userName,
                    application, status, startDate, endDate);

            for (Simulation simulation : simulations) {
                if (simulation.getMajorStatus().equals(SimulationStatus.Running.name())) {
                    try {
                        MoteurStatus workflowStatus = MoteurStatus.valueOf(
                                this.getStatus(simulation.getID()));

                        if (workflowStatus == MoteurStatus.COMPLETE) {
                            workflowDAO.updateStatus(simulation.getID(),
                                    SimulationStatus.Completed.name());
                            simulation.setMajorStatus(SimulationStatus.Completed.name());

                        } else if (workflowStatus == MoteurStatus.TERMINATED
                                || workflowStatus == MoteurStatus.UNKNOWN) {
                            workflowDAO.updateStatus(simulation.getID(),
                                    SimulationStatus.Killed.name());
                            simulation.setMajorStatus(SimulationStatus.Killed.name());
                        }
                    } catch (BusinessException ex) {
                        logger.error(ex);
                    }
                }
            }
            return simulations;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param users
     * @param application
     * @param status
     * @param startDate
     * @param endDate
     * @return
     * @throws BusinessException 
     */
    public List<Simulation> getSimulations(List<String> users, String application,
            String status, Date startDate, Date endDate) throws BusinessException {

        try {
            if (endDate != null) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(endDate);
                c1.add(Calendar.DATE, 1);
                endDate = c1.getTime();
            }
            WorkflowDAO workflowDAO = WorkflowDAOFactory.getDAOFactory().getWorkflowDAO();
            List<Simulation> simulations = workflowDAO.getList(users,
                    application, status, startDate, endDate);

            for (Simulation simulation : simulations) {
                if (simulation.getMajorStatus().equals(SimulationStatus.Running.name())) {
                    try {
                        MoteurStatus workflowStatus = MoteurStatus.valueOf(
                                this.getStatus(simulation.getID()));

                        if (workflowStatus == MoteurStatus.COMPLETE) {
                            workflowDAO.updateStatus(simulation.getID(),
                                    SimulationStatus.Completed.name());
                            simulation.setMajorStatus(SimulationStatus.Completed.name());

                        } else if (workflowStatus == MoteurStatus.TERMINATED
                                || workflowStatus == MoteurStatus.UNKNOWN) {
                            workflowDAO.updateStatus(simulation.getID(),
                                    SimulationStatus.Killed.name());
                            simulation.setMajorStatus(SimulationStatus.Killed.name());
                        }
                    } catch (BusinessException ex) {
                        logger.error(ex);
                    }
                }
            }
            return simulations;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @return
     * @throws BusinessException 
     */
    public Simulation getSimulation(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getWorkflowDAO().get(simulationID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @throws BusinessException 
     */
    public String getStatus(String simulationID) throws BusinessException {

        try {
//            WorkflowMoteurConfig moteur = new WorkflowMoteurConfig(
//                    Server.getInstance().getMoteurServer());
//            return moteur.getStatus(simulationID);

            if (isShiwaPoolID(simulationID)) {
                MoteurPoolConfig pool = new MoteurPoolConfig();
                return pool.getStatus(simulationID);

            } else {
                MoteurWSConfig ws = new MoteurWSConfig();
                ws.setAddressWS(Server.getInstance().getMoteurServer());
                return ws.getStatus(simulationID);
            }
        } catch (RemoteException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (ServiceException ex) {
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
    public List<InOutData> getOutputData(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getWorkflowDAO().getInOutData(simulationID, "Outputs");

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @return
     * @throws BusinessException 
     */
    public List<InOutData> getInputData(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getWorkflowDAO().getInOutData(simulationID, "Inputs");

        } catch (DAOException ex) {
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
            File file = new File(Server.getInstance().getWorkflowsPath() + "/" + path);
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);

            } else {
                if (!file.delete()) {
                    logger.error("Unable to delete data: " + path);
                    throw new BusinessException("Unable to delete data: " + path);
                }
            }
        } catch (Exception ex) {
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
    public List<Processor> getProcessors(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getWorkflowDAO().getProcessors(simulationID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
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
            throws BusinessException {

        try {
            switch (type) {
                case 1:
                    return WorkflowDAOFactory.getDAOFactory().getWorkflowDAO().getTimeAnalysis(simulationIDList);
                case 2:
                    return WorkflowDAOFactory.getDAOFactory().getWorkflowDAO().getJobStatuses(simulationIDList);
                default:
                    logger.error("Type '" + type + "' not supported.");
                    throw new BusinessException("Type '" + type + "' not supported.");
            }
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param inputs
     * @throws BusinessException 
     */
    public void validateInputs(String user, List<String> inputs) throws BusinessException {

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
                logger.error("The following data does not exist: "
                        + sb.toString());
                throw new BusinessException("The following data does not exist: "
                        + sb.toString());
            }
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            throw new BusinessException(ex);
        }
    }

    private boolean isShiwaPoolID(String simulationID) {

        if (simulationID.startsWith("shiwa-instance")) {
            return true;
        } else {
            return false;
        }
    }

    private void checkFolderACL(String user, List<String> groups, String path)
            throws BusinessException {

        if (path.startsWith(Server.getInstance().getDataManagerUsersHome())) {

            path = path.replace(Server.getInstance().getDataManagerUsersHome() + "/", "");
            if (!path.startsWith(user.replaceAll(" ", "_").toLowerCase())) {
                logger.error("User '" + user + "' tried to access data from another user: " + path + "");
                throw new BusinessException("Access denied to another user's home.");
            }

        } else if (path.startsWith(Server.getInstance().getDataManagerGroupsHome())) {

            path = path.replace(Server.getInstance().getDataManagerGroupsHome() + "/", "");
            if (path.indexOf("/") != -1) {
                path = path.substring(0, path.indexOf("/"));
            }
            if (!groups.contains(path)) {
                logger.error("User '" + user + "' tried to access data from a non-autorized group: " + path + "");
                throw new BusinessException("Access denied to group '" + path + "'.");
            }
        }
    }
}
