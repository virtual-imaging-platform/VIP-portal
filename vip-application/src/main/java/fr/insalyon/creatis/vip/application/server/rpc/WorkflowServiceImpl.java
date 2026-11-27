package fr.insalyon.creatis.vip.application.server.rpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.application.server.business.WorkflowLaunchBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.models.Activity;
import fr.insalyon.creatis.vip.application.models.InOutData;
import fr.insalyon.creatis.vip.application.models.Workflow;
import fr.insalyon.creatis.vip.application.models.SimulationInput;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationInputDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.models.Pair;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import jakarta.servlet.ServletException;

public class WorkflowServiceImpl extends AbstractRemoteServiceServlet implements WorkflowService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WorkflowBusiness workflowBusiness;
    private WorkflowLaunchBusiness workflowLaunchBusiness;
    private ListWorkflowsBusiness listWorkflowsBusiness;
    private InputBusiness inputBusiness;
    private ApplicationInputDAO applicationInputDAO;
    private BoutiquesBusiness boutiquesBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        inputBusiness = getBean(InputBusiness.class);
        applicationInputDAO = getBean(ApplicationInputDAO.class);
        workflowBusiness = getBean(WorkflowBusiness.class);
        workflowLaunchBusiness = getBean(WorkflowLaunchBusiness.class);
        listWorkflowsBusiness = getBean(ListWorkflowsBusiness.class);
        boutiquesBusiness = getBean(BoutiquesBusiness.class);
    }

    /**
     * Gets a list of recently launched simulations.
     *
     * @return
     * @throws VipException
     */
    @Override
    public List<Workflow> getSimulations() throws VipException {
        if (isSystemAdministrator()) {
            return listWorkflowsBusiness.getAllWorkflows(null);
        } else {
            return listWorkflowsBusiness.getCurrentUserWorkflows(null);
        }
    }

    /**
     * Gets a list of recently launched simulations from a date.
     *
     * @param lastDate
     * @return
     * @throws VipException
     */
    @Override
    public List<Workflow> getSimulations(Date lastDate) throws VipException {
        if (isSystemAdministrator()) {
            return listWorkflowsBusiness.getAllWorkflows(lastDate);
        } else {
            return listWorkflowsBusiness.getCurrentUserWorkflows(lastDate);
        }
    }

    @Override
    public List<Workflow> getSimulations(String userName, String applicationName,
                                         String status, Date startDate, Date endDate) throws VipException {
        return listWorkflowsBusiness.refreshRunningWorkflows(
                    listWorkflowsBusiness.searchWithAdminRightsAndIncludeCurrentUserWorkflows(
                        userName, applicationName,status, startDate, endDate, null));
    }

    @Override
    public String getApplicationDescriptorString(String applicationName, String applicationVersion) throws VipException {
        return boutiquesBusiness.getApplicationDescriptorString(applicationName, applicationVersion);
    }

    /**
     * Map(ApplicationName, ApplicationVersion)
     */
    @Override
    public List<String> getApplicationsDescriptorsString(List<Pair<String, String>> applications) throws VipException {
        List<String> result = new ArrayList<>();

        for (var pair : applications) {
            result.add(boutiquesBusiness.getApplicationDescriptorString(pair.getFirst(), pair.getSecond()));
        }
        return result;
    }

    @Override
    public void launchSimulation(Map<String, String> parametersMap,
            String applicationName, String applicationVersion,
            String applicationClass, String simulationName) throws VipException {

        // fill in overriddenInputs from explicit inputs

        trace(logger, "Launching simulation '" + simulationName + "' (" + applicationName + ").");
        Workflow workflow  = workflowLaunchBusiness.launch(simulationName, applicationName, applicationVersion, parametersMap);

        trace(logger, "Simulation '" + simulationName + "' launched with ID '" + workflow.getID() + "'.");
    }

    /**
     *
     * @param name
     * @param appName
     * @return
     * @throws VipException
     */
    @Override
    public SimulationInput getInputByNameUserApp(String name, String appName)
        throws VipException {
        return inputBusiness.getInputByUserAndName(
                getSessionUser().getEmail(), name, appName);
    }

    /**
     *
     * @param simulationInput
     * @throws VipException
     */
    @Override
    public void addSimulationInput(SimulationInput simulationInput)
            throws VipException {
        inputBusiness.addSimulationInput(
                getSessionUser().getEmail(), simulationInput);
    }

    /**
     *
     * @param simulationInput
     * @throws VipException
     */
    @Override
    public void updateSimulationInput(SimulationInput simulationInput)
            throws VipException {
        inputBusiness.updateSimulationInput(
                getSessionUser().getEmail(), simulationInput);
    }

    /**
     *
     * @param inputName
     * @param applicationName
     * @throws VipException
     */
    @Override
    public void removeSimulationInput(String inputName, String applicationName)
            throws VipException {
        inputBusiness.removeSimulationInput(
                getSessionUser().getEmail(), inputName, applicationName);
    }

    /**
     *
     * @param inputName
     * @param applicationName
     * @throws VipException
     */
    @Override
    public void removeSimulationInputExample(
        String inputName, String applicationName)
        throws VipException {
        inputBusiness.removeSimulationInputExample(
                inputName, applicationName);
    }

    /**
     *
     * @return @throws VipException
     */
    @Override
    public List<SimulationInput> getSimulationInputByUser()
        throws VipException {
        return inputBusiness.getSimulationInputByUser(
                getSessionUser().getEmail());
    }

    /**
     *
     * @param simulationInput
     * @throws VipException
     */
    @Override
    public void saveInputsAsExamples(SimulationInput simulationInput)
        throws VipException {
        inputBusiness.saveSimulationInputAsExample(
                simulationInput);
    }

    /**
     *
     * @param applicationName
     * @return
     * @throws VipException
     */
    @Override
    public List<SimulationInput> getSimulationInputExamples(
        String applicationName) throws VipException {

        return inputBusiness.getSimulationInputExamples(
                applicationName);
    }

    /**
     *
     * @param simulationIDs
     * @throws VipException
     */
    @Override
    public void killSimulations(List<String> simulationIDs) throws VipException {

        try {
            trace(logger, "Killing simulations: " + simulationIDs);
            StringBuilder sb = new StringBuilder();
            for (String simulationID : simulationIDs) {
                try {
                    workflowBusiness.kill(simulationID);

                } catch (VipException ex) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(simulationID);
                }
            }
            if (sb.length() > 0) {
                logger.error("Unable to kill the following simulations: {}", sb.toString());
                throw new VipException("Unable to kill the following "
                        + "simulations: " + sb.toString());
            }
        } catch (CoreException ex) {
            throw new VipException(ex);
        }
    }

    /**
     *
     * @param simulationIDs
     * @throws VipException
     */
    @Override
    public void cleanSimulations(List<String> simulationIDs) throws VipException {
        trace(logger, "Cleaning simulations: " + simulationIDs);
        StringBuilder sb = new StringBuilder();
        for (String simulationID : simulationIDs) {
            try {
                workflowBusiness.clean(simulationID, getSessionUser().getEmail());

            } catch (VipException ex) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(simulationID);
            }
        }

        if (sb.length() > 0) {
            logger.error("Unable to clean the following simulations: {}", sb.toString());
            throw new VipException("Unable to clean the following "
                    + "simulations: " + sb.toString());
        }
    }

    /**
     *
     * @param simulationIDs
     * @throws VipException
     */
    @Override
    public void purgeSimulations(List<String> simulationIDs) throws VipException {
        try {
            authenticateSystemAdministrator(logger);
            trace(logger, "Purging simulations: " + simulationIDs);
            StringBuilder sb = new StringBuilder();
            for (String simulationID : simulationIDs) {
                try {
                    workflowBusiness.purge(simulationID);
                } catch (VipException ex) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(simulationID);
                }
            }

            if (sb.length() > 0) {
                logger.error("Unable to purge the following simulations: {}", sb.toString());
                throw new VipException("Unable to purge the following "
                        + "simulations: " + sb.toString());
            }
        } catch (CoreException ex) {
            throw new VipException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @throws VipException
     */
    @Override
    public void killWorkflow(String simulationID) throws VipException {
        trace(logger, "Killing simulation '" + simulationID + "'.");
        workflowBusiness.kill(simulationID);
    }

    /**
     *
     * @param simulationID
     * @throws VipException
     */
    @Override
    public void cleanWorkflow(String simulationID) throws VipException {
        trace(logger, "Cleaning simulation '" + simulationID + "'.");
        workflowBusiness.clean(simulationID, getSessionUser().getEmail());
    }

    /**
     *
     * @param simulationID
     * @throws VipException
     */
    @Override
    public void purgeWorkflow(String simulationID) throws VipException {
        authenticateSystemAdministrator(logger);
        trace(logger, "Purging simulation '" + simulationID + "'.");
        workflowBusiness.purge(simulationID);
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public Map<String, String> relaunchSimulation(String simulationID) throws VipException {
        trace(logger, "Relaunching simulation '" + simulationID + "'.");
        return workflowBusiness.relaunch(
                simulationID, getSessionUser().getFolder());
    }

    /**
     *
     * @param workflowId
     * @return
     * @throws VipException
     */
    @Override
    public Workflow getSimulation(String workflowId) throws VipException {
        return listWorkflowsBusiness.getNotRefreshedWorkflow(workflowId);
    }

    @Override
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

    @Override
    public String getFileURL(String baseDir, String fileName) {
        return "https://" + server.getApacheHost() + ":"
                + server.getApacheSSLPort()
                + "/workflows"
                + baseDir + "/" + fileName;
    }

    @Override
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
     * @throws VipException
     */
    @Override
    public void deleteLogData(String path) throws VipException {
        workflowBusiness.deleteLogData(path);
    }

    @Override
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
     * @param workflowList
     * @param type
     * @return
     * @throws VipException
     */
    @Override
    public List<String> getPerformanceStats(List<Workflow> workflowList, int type) throws VipException {

        try {
            return workflowBusiness.getPerformanceStats(workflowList, type);
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting perf stats for {}", workflowList, ex);
            throw new VipException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public List<InOutData> getOutputData(String simulationID) throws VipException {
        return workflowBusiness.getOutputData(
                simulationID, getSessionUser().getFolder());
    }


    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public List<InOutData> getInputData(String simulationID) throws VipException {
        return workflowBusiness.getInputData(
            simulationID, getSessionUser().getFolder());
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public List<Activity> getProcessors(String simulationID) throws VipException {
        return workflowBusiness.getProcessors(simulationID);
    }

    /**
     *
     * @param inputs
     * @throws VipException
     */
    @Override
    public void validateInputs(List<String> inputs) throws VipException {
        workflowBusiness.validateInputs(getSessionUser(), inputs);
    }

    /**
     *
     * @param currentUser
     * @param newUser
     * @throws VipException
     */
    @Override
    public void updateUser(String currentUser, String newUser) throws VipException {
        trace(logger, "Updating user '" + currentUser + "' to '" + newUser + "'.");
        workflowBusiness.updateUser(currentUser, newUser);
    }

    @Override
    public void markSimulationsCompleted(List<String> simulationIDs) throws VipException {
        try {
            trace(logger, "Marking simulations completed: " + simulationIDs);
            StringBuilder sb = new StringBuilder();
            for (String simulationID : simulationIDs) {
                try {
                    workflowBusiness.markCompleted(simulationID);
                } catch (VipException ex) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(simulationID);
                }
            }

            if (sb.length() > 0) {
                logger.error("Unable to mark completed the following simulations: {}", sb.toString());
                throw new VipException("Unable to mark completed the following "
                        + "simulations: " + sb.toString());
            }
        } catch (CoreException ex) {
            throw new VipException(ex);
        }
    }

    @Override
    public void markWorkflowCompleted(String simulationID) throws VipException {
        trace(logger, "Marking simulation '" + simulationID + "' completed.");
        workflowBusiness.markCompleted(simulationID);
    }

    @Override
    public void changeSimulationUser(String simulationId, String user) throws VipException {
        trace(logger, "Changing user of simulation '" + simulationId + "' to "+user+".");
        workflowBusiness.changeSimulationUser(simulationId,user);
    }
}
