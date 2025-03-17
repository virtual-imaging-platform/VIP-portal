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
package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.application.server.business.InputBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationInputDAO;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.Pair;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class WorkflowServiceImpl extends AbstractRemoteServiceServlet implements WorkflowService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WorkflowBusiness workflowBusiness;
    private InputBusiness inputBusiness;
    private ConfigurationBusiness configurationBusiness;
    private ApplicationInputDAO applicationInputDAO;
    private BoutiquesBusiness boutiquesBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        inputBusiness = getBean(InputBusiness.class);
        applicationInputDAO = getBean(ApplicationInputDAO.class);
        configurationBusiness = getBean(ConfigurationBusiness.class);
        workflowBusiness = getBean(WorkflowBusiness.class);
        boutiquesBusiness = getBean(BoutiquesBusiness.class);
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
        } catch (CoreException | BusinessException ex) {
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
        } catch (CoreException | BusinessException ex) {
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
                                           String status, String appClass, Date startDate, Date endDate) throws ApplicationException {
        try {
            User user = getSessionUser();
            if (user.isSystemAdministrator()) {
                return workflowBusiness.getSimulations(userName, application,
                        status, appClass, startDate, endDate);

            } else {

                if (userName != null) {
                    return workflowBusiness.getSimulations(userName,
                            application, status, appClass, startDate, endDate);

                } else {
                    List<String> users = configurationBusiness
                            .getUserNames(user.getEmail(), true);

                    return workflowBusiness.getSimulations(users,
                            application, status, appClass, startDate, endDate, null);
                }
            }
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public Descriptor getApplicationDescriptor(String applicationName, String applicationVersion) throws ApplicationException {
        try {
            return workflowBusiness.getApplicationDescriptor(
                    getSessionUser(),
                    applicationName,
                    applicationVersion);
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public String getApplicationDescriptorString(String applicationName, String applicationVersion) throws ApplicationException {
        try {
            return boutiquesBusiness.getApplicationDescriptorString(getSessionUser(), applicationName,
                                                                    applicationVersion);
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Map(ApplicationName, ApplicationVersion)
     */
    @Override
    public List<String> getApplicationsDescriptorsString(List<Pair<String, String>> applications) throws ApplicationException {
        List<String> result = new ArrayList<>();

        try {
            for (var pair : applications) {
                result.add(boutiquesBusiness.getApplicationDescriptorString(
                    getSessionUser(), pair.getFirst(), pair.getSecond()));
            }
            return result;
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void launchSimulation(Map<String, String> parametersMap,
            String applicationName, String applicationVersion,
            String applicationClass, String simulationName) throws ApplicationException {

        try {
            trace(logger, "Launching simulation '" + simulationName + "' (" + applicationName + ").");
            User user = getSessionUser();

            List<String> groups = new ArrayList<String>();
            for (Group group : getUserGroupsFromSession().keySet()) {
                groups.add(group.getName());
            }

            for (Map.Entry<String,String> p : parametersMap.entrySet()) {
                logger.info("received param {} : {}", p.getKey(), p.getValue());
            }

            String simulationID = workflowBusiness.launch(
                user, groups,
                parametersMap, applicationName, applicationVersion,
                applicationClass, simulationName);

            trace(logger, "Simulation '" + simulationName + "' launched with ID '" + simulationID + "'.");

        } catch (BusinessException | CoreException ex) {
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
        } catch (BusinessException | CoreException ex) {
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
            inputBusiness.addSimulationInput(
                getSessionUser().getEmail(), simulationInput);
        } catch (BusinessException | CoreException ex) {
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
            inputBusiness.updateSimulationInput(
                getSessionUser().getEmail(), simulationInput);
        } catch (BusinessException | CoreException ex) {
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
            inputBusiness.removeSimulationInput(
                getSessionUser().getEmail(), inputName, applicationName);
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param inputName
     * @param applicationName
     * @throws ApplicationException
     */
    public void removeSimulationInputExample(
        String inputName, String applicationName)
        throws ApplicationException {
        try {
            inputBusiness.removeSimulationInputExample(
                inputName, applicationName);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @return @throws ApplicationException
     */
    public List<SimulationInput> getSimulationInputByUser()
        throws ApplicationException {
        try {
            return inputBusiness.getSimulationInputByUser(
                getSessionUser().getEmail());
        } catch (BusinessException | CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationInput
     * @throws ApplicationException
     */
    public void saveInputsAsExamples(SimulationInput simulationInput)
        throws ApplicationException {
        try {
            inputBusiness.saveSimulationInputAsExample(
                simulationInput);
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
    public List<SimulationInput> getSimulationInputExamples(
        String applicationName) throws ApplicationException {
        try {
            return inputBusiness.getSimulationInputExamples(
                applicationName);
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
                logger.error("Unable to kill the following simulations: {}", sb.toString());
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
                logger.error("Unable to clean the following simulations: {}", sb.toString());
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
            authenticateSystemAdministrator(logger);
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
                logger.error("Unable to purge the following simulations: {}", sb.toString());
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

        } catch (CoreException | BusinessException ex) {
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

        } catch (CoreException | BusinessException ex) {
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
            authenticateSystemAdministrator(logger);
            trace(logger, "Purging simulation '" + simulationID + "'.");
            workflowBusiness.purge(simulationID);

        } catch (CoreException | BusinessException ex) {
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
            return workflowBusiness.relaunch(
                simulationID, getSessionUser().getFolder());
        } catch (BusinessException | CoreException ex) {
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
                    server.getWorkflowsPath() + "/"
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
            logger.error("Error getting workflow file {}", fileName, ex);
        }
        return null;
    }

    public String getFileURL(String baseDir, String fileName) {
        return "https://" + server.getApacheHost() + ":"
                + server.getApacheSSLPort()
                + "/workflows"
                + baseDir + "/" + fileName;
    }

    public List<String> getLogs(String baseDir) {
        List<String> list = new ArrayList<String>();

        File folder = new File(server.getWorkflowsPath()
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

    public List<SimulationInput> getWorkflowsInputByUserAndAppName(
        String user, String appName) {
        try {
            return applicationInputDAO.getWorkflowInputByUserAndAppName(user, appName);
        } catch (DAOException ex) {
            logger.error("Error in getWorkflowsInputByUserAndAppName. Ignoring", ex);
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
    public List<String> getPerformanceStats(List<Simulation> simulationList, int type) throws ApplicationException {

        try {
            return workflowBusiness.getPerformanceStats(simulationList, type);
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting perf stats for {}", simulationList, ex);
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
    public List<InOutData> getOutputData(String simulationID) throws ApplicationException {
        try {
            return workflowBusiness.getOutputData(
                simulationID, getSessionUser().getFolder());
        } catch (BusinessException | CoreException ex) {
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
            return workflowBusiness.getInputData(
                simulationID, getSessionUser().getFolder());
        } catch (BusinessException | CoreException ex) {
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
        } catch (BusinessException | CoreException ex) {
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

        } catch (CoreException | BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void markSimulationsCompleted(List<String> simulationIDs) throws ApplicationException {
        try {
            trace(logger, "Marking simulations completed: " + simulationIDs);
            StringBuilder sb = new StringBuilder();
            for (String simulationID : simulationIDs) {
                try {
                    workflowBusiness.markCompleted(simulationID);
                } catch (BusinessException ex) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(simulationID);
                }
            }

            if (sb.length() > 0) {
                logger.error("Unable to mark completed the following simulations: {}", sb.toString());
                throw new ApplicationException("Unable to mark completed the following "
                        + "simulations: " + sb.toString());
            }
        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void markWorkflowCompleted(String simulationID) throws ApplicationException {
         try {
            trace(logger, "Marking simulation '" + simulationID + "' completed.");
            workflowBusiness.markCompleted(simulationID);

        } catch (CoreException | BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public void changeSimulationUser(String simulationId, String user) throws ApplicationException {
         try {
            trace(logger, "Changing user of simulation '" + simulationId + "' to "+user+".");
            workflowBusiness.changeSimulationUser(simulationId,user);

        } catch (CoreException | BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }
}
