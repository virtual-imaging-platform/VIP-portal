/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.InputBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationDAOFactory;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class WorkflowServiceImpl extends AbstractRemoteServiceServlet implements WorkflowService {

    private static final Logger logger = Logger.getLogger(WorkflowServiceImpl.class);
    private WorkflowBusiness workflowBusiness;
    private InputBusiness inputBusiness;

    public WorkflowServiceImpl() {

        workflowBusiness = new WorkflowBusiness();
        inputBusiness = new InputBusiness();
    }

    /**
     * Gets a list of recently launched simulations.
     *
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<Simulation> getSimulations() throws ApplicationException {

        try {
            if (isSystemAdministrator()) {
                return workflowBusiness.getSimulations(null, null);
            } else {
                return workflowBusiness.getSimulations(getSessionUser(), null);
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Gets a list of recently launched simulations from a date.
     *
     * @param lastDate
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<Simulation> getSimulations(Date lastDate) throws ApplicationException {

        try {
            if (isSystemAdministrator()) {
                return workflowBusiness.getSimulations(null, lastDate);
            } else {
                return workflowBusiness.getSimulations(getSessionUser(), lastDate);
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param applicationName
     * @param applicationVersion
     * @return
     * @throws ApplicationException
     */
    @Override
    public Descriptor getApplicationDescriptor(String applicationName, String applicationVersion) throws ApplicationException {

        try {
            return workflowBusiness.getApplicationDescriptor(getSessionUser(),
                    applicationName, applicationVersion);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Launches a simulation.
     *
     * @param parametersMap Simulation parameters map
     * @param applicationName Application name
     * @param applicationVersion Application version
     * @param applicationClass Application class
     * @param simulationName Simulation name
     * @throws ApplicationException
     */
    @Override
    public void launchSimulation(Map<String, String> parametersMap,
            String applicationName, String applicationVersion,
            String applicationClass, String simulationName) throws ApplicationException {

        try {
            trace(logger, "Launching simulation '" + simulationName + "' (" + applicationName + ").");
            User user = getSessionUser();

            List<String> groups = new ArrayList<String>();
            for (Group group : getSessionUserGroups().keySet()) {
                groups.add(group.getName());
            }

            String simulationID = workflowBusiness.launch(user, groups,
                    parametersMap, applicationName, applicationVersion, 
                    applicationClass, simulationName);

            trace(logger, "Simulation '" + simulationName + "' launched with ID '" + simulationID + "'.");

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param name
     * @param appName
     * @return
     * @throws ApplicationException
     */
    @Override
    public SimulationInput getInputByNameUserApp(String name, String appName)
            throws ApplicationException {

        try {
            return inputBusiness.getInputByUserAndName(
                    getSessionUser().getEmail(), name, appName);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationInput
     * @throws ApplicationException
     */
    public void addSimulationInput(SimulationInput simulationInput)
            throws ApplicationException {

        try {
            inputBusiness.addSimulationInput(getSessionUser().getEmail(),
                    simulationInput);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationInput
     * @throws ApplicationException
     */
    public void updateSimulationInput(SimulationInput simulationInput)
            throws ApplicationException {

        try {
            inputBusiness.updateSimulationInput(getSessionUser().getEmail(),
                    simulationInput);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    public String loadSimulationInput(String fileName) throws ApplicationException {

        try {
            return inputBusiness.loadSimulationInput(fileName);

        } catch (BusinessException ex) {
            logger.error(ex);
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param inputName
     * @param applicationName
     * @throws ApplicationException
     */
    public void removeSimulationInput(String inputName, String applicationName)
            throws ApplicationException {

        try {
            inputBusiness.removeSimulationInput(getSessionUser().getEmail(),
                    inputName, applicationName);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param inputName
     * @param applicationName
     * @throws ApplicationException
     */
    public void removeSimulationInputExample(String inputName, String applicationName)
            throws ApplicationException {

        try {
            inputBusiness.removeSimulationInputExample(inputName, applicationName);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @return @throws ApplicationException
     */
    public List<SimulationInput> getSimulationInputByUser() throws ApplicationException {

        try {
            return inputBusiness.getSimulationInputByUser(getSessionUser().getEmail());

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationInput
     * @throws ApplicationException
     */
    public void saveInputsAsExamples(SimulationInput simulationInput) throws ApplicationException {

        try {
            inputBusiness.saveSimulationInputAsExample(simulationInput);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param applicationName
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<SimulationInput> getSimulationInputExamples(String applicationName) throws ApplicationException {

        try {
            return inputBusiness.getSimulationInputExamples(applicationName);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationIDs
     * @throws ApplicationException
     */
    @Override
    public void killSimulations(List<String> simulationIDs) throws ApplicationException {

        try {
            trace(logger, "Killing simulations: " + simulationIDs);
            StringBuilder sb = new StringBuilder();
            for (String simulationID : simulationIDs) {
                try {
                    workflowBusiness.kill(simulationID);

                } catch (BusinessException ex) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(simulationID);
                }
            }
            if (sb.length() > 0) {
                logger.error("Unable to kill the following "
                        + "simulations: " + sb.toString());
                throw new ApplicationException("Unable to kill the following "
                        + "simulations: " + sb.toString());
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationIDs
     * @throws ApplicationException
     */
    @Override
    public void cleanSimulations(List<String> simulationIDs) throws ApplicationException {

        try {
            trace(logger, "Cleaning simulations: " + simulationIDs);
            StringBuilder sb = new StringBuilder();
            for (String simulationID : simulationIDs) {
                try {
                    workflowBusiness.clean(simulationID, getSessionUser().getEmail());

                } catch (BusinessException ex) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(simulationID);
                } catch (CoreException ex) {
                    throw new ApplicationException(ex);
                }
            }

            if (sb.length() > 0) {
                logger.error("Unable to clean the following "
                        + "simulations: " + sb.toString());
                throw new ApplicationException("Unable to clean the following "
                        + "simulations: " + sb.toString());
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationIDs
     * @throws ApplicationException
     */
    public void purgeSimulations(List<String> simulationIDs) throws ApplicationException {

        try {
            trace(logger, "Purging simulations: " + simulationIDs);
            StringBuilder sb = new StringBuilder();
            for (String simulationID : simulationIDs) {
                try {
                    workflowBusiness.purge(simulationID);
                } catch (BusinessException ex) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(simulationID);
                }
            }

            if (sb.length() > 0) {
                logger.error("Unable to purge the following "
                        + "simulations: " + sb.toString());
                throw new ApplicationException("Unable to purge the following "
                        + "simulations: " + sb.toString());
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @throws ApplicationException
     */
    @Override
    public void killWorkflow(String simulationID) throws ApplicationException {

        try {
            trace(logger, "Killing simulation '" + simulationID + "'.");
            workflowBusiness.kill(simulationID);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @throws ApplicationException
     */
    public void cleanWorkflow(String simulationID) throws ApplicationException {

        try {
            trace(logger, "Cleaning simulation '" + simulationID + "'.");
            workflowBusiness.clean(simulationID, getSessionUser().getEmail());

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @throws ApplicationException
     */
    @Override
    public void purgeWorkflow(String simulationID) throws ApplicationException {

        try {
            trace(logger, "Purging simulation '" + simulationID + "'.");
            workflowBusiness.purge(simulationID);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws ApplicationException
     */
    @Override
    public Map<String, String> relaunchSimulation(String simulationID) throws ApplicationException {

        try {
            trace(logger, "Relaunching simulation '" + simulationID + "'.");
            return workflowBusiness.relaunch(simulationID, getSessionUser().getFolder());

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
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
     * @throws ApplicationException
     */
    @Override
    public List<Simulation> getSimulations(String userName, String application,
            String status, Date startDate, Date endDate) throws ApplicationException {

        try {
            User user = getSessionUser();
            if (user.isSystemAdministrator()) {
                return workflowBusiness.getSimulations(userName, application,
                        status, startDate, endDate);

            } else {

                if (userName != null) {
                    return workflowBusiness.getSimulations(userName,
                            application, status, startDate, endDate);

                } else {
                    List<String> users = configurationBusiness.getUserNames(user.getEmail(), true);

                    return workflowBusiness.getSimulations(users,
                            application, status, startDate, endDate);
                }
            }

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws ApplicationException
     */
    public Simulation getSimulation(String simulationID) throws ApplicationException {

        try {
            return workflowBusiness.getSimulation(simulationID);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public String getFile(String baseDir, String fileName) {
        try {
            FileReader fr = new FileReader(
                    Server.getInstance().getWorkflowsPath() + "/"
                    + baseDir + "/" + fileName);

            BufferedReader br = new BufferedReader(fr);

            String strLine;
            StringBuilder sb = new StringBuilder();

            while ((strLine = br.readLine()) != null) {
                sb.append(strLine);
                sb.append("\n");
            }

            br.close();
            fr.close();
            return sb.toString();

        } catch (IOException ex) {
            logger.error(ex);
        }
        return null;
    }

    public String getFileURL(String baseDir, String fileName) {
        Server configuration = Server.getInstance();
        return "https://" + configuration.getApacheHost() + ":"
                + configuration.getApacheSSLPort()
                + "/workflows"
                + baseDir + "/" + fileName;
    }

    public List<String> getLogs(String baseDir) {
        List<String> list = new ArrayList<String>();

        File folder = new File(Server.getInstance().getWorkflowsPath()
                + "/" + baseDir);

        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                list.add(f.getName() + "-#-Folder");
            } else {
                String fileSize = FileUtils.parseFileSize(f.length());
                String info = f.getName() + "##" + fileSize
                        + "##" + new Date(f.lastModified());
                list.add(info + "-#-File");
            }
        }
        return list;
    }

    /**
     *
     * @param path
     * @throws ApplicationException
     */
    public void deleteLogData(String path) throws ApplicationException {

        try {
            workflowBusiness.deleteLogData(path);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public List<SimulationInput> getWorkflowsInputByUserAndAppName(String user, String appName) {
        try {
            return ApplicationDAOFactory.getDAOFactory().getApplicationInputDAO().getWorkflowInputByUserAndAppName(user, appName);
        } catch (DAOException ex) {
            return null;
        }
    }

    /**
     *
     * @param simulationList
     * @param type
     * @return
     * @throws ApplicationException
     */
    public String getPerformanceStats(List<Simulation> simulationList, int type) throws ApplicationException {

        try {
            return workflowBusiness.getPerformanceStats(simulationList, type);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<InOutData> getOutputData(String simulationID) throws ApplicationException {

        try {
            return workflowBusiness.getOutputData(simulationID, getSessionUser().getFolder());

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<InOutData> getInputData(String simulationID) throws ApplicationException {

        try {
            return workflowBusiness.getInputData(simulationID, getSessionUser().getFolder());

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws ApplicationException
     */
    public List<Activity> getProcessors(String simulationID) throws ApplicationException {

        try {
            return workflowBusiness.getProcessors(simulationID);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param inputs
     * @throws ApplicationException
     */
    @Override
    public void validateInputs(List<String> inputs) throws ApplicationException {

        try {
            workflowBusiness.validateInputs(getSessionUser(), inputs);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param currentUser
     * @param newUser
     * @throws ApplicationException
     */
    public void updateUser(String currentUser, String newUser) throws ApplicationException {

        try {
            trace(logger, "Updating user '" + currentUser + "' to '" + newUser + "'.");
            workflowBusiness.updateUser(currentUser, newUser);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    private String getJenaDirName(String baseDir) throws ApplicationException {
        File dir = new File(baseDir);

        if (dir.isDirectory()) {
            for (String names : dir.list()) {
                if (names.startsWith("JENA-TDB-dir")) {
                    return names;
                }
            }
        } else {
            throw new ApplicationException(baseDir + "is not a directory");
        }
        return null;
    }

    private String cleanse(String s) throws DataManagerException {
        if (s.startsWith("lfn://") || s.startsWith("/grid")) {
            return DataManagerUtil.parseBaseDir(CoreModule.user, s);
        } else {
            return s;
        }
    }
}
