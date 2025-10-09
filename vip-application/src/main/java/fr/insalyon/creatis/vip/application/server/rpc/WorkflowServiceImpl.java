package fr.insalyon.creatis.vip.application.server.rpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.boutiques.model.BoutiquesDescriptor;
import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import fr.insalyon.creatis.vip.application.client.bean.Activity;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.application.server.business.InputBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.application.server.dao.ApplicationInputDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.Pair;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import jakarta.servlet.ServletException;

public class WorkflowServiceImpl extends AbstractRemoteServiceServlet implements WorkflowService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WorkflowBusiness workflowBusiness;
    private InputBusiness inputBusiness;
    private ApplicationInputDAO applicationInputDAO;
    private BoutiquesBusiness boutiquesBusiness;
    private AppVersionBusiness appVersionBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        inputBusiness = getBean(InputBusiness.class);
        applicationInputDAO = getBean(ApplicationInputDAO.class);
        workflowBusiness = getBean(WorkflowBusiness.class);
        boutiquesBusiness = getBean(BoutiquesBusiness.class);
        appVersionBusiness = getBean(AppVersionBusiness.class);
    }

    /**
     * Gets a list of recently launched simulations.
     *
     * @return
     * @throws VipException
     */
    @Override
    public List<Simulation> getSimulations() throws VipException {
        if (isSystemAdministrator()) {
            return workflowBusiness.getSimulations(null, null);
        } else {
            return workflowBusiness.getSimulations(getSessionUser(), null);
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
    public List<Simulation> getSimulations(Date lastDate) throws VipException {
        if (isSystemAdministrator()) {
            return workflowBusiness.getSimulations(null, lastDate);
        } else {
            return workflowBusiness.getSimulations(getSessionUser(), lastDate);
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
     * @throws VipException
     */
    @Override
    public List<Simulation> getSimulations(String userName, String application,
                                           String status, Date startDate, Date endDate) throws VipException {
        User user = getSessionUser();
        if (user.isSystemAdministrator() || (userName != null && userName.equalsIgnoreCase(user.getFullName()))) {
            return workflowBusiness.getSimulations(userName, application, status, startDate, endDate);
        } else if (userName == null) {
            return workflowBusiness.getSimulationsWithGroupAdminRights(user, application, status, startDate, endDate,
                    null);
        } else {
            throw new VipException("You can't see another person's simulation!");
        }
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

    private void fillInOverriddenInputs(Map<String, String> parametersMap,
                                        String applicationName, String applicationVersion) throws VipException {
        AppVersion appVersion = appVersionBusiness.getVersion(applicationName, applicationVersion);
        BoutiquesDescriptor descriptor = boutiquesBusiness.parseBoutiquesString(appVersion.getDescriptor());
        Map<String, String> overriddenInputs = boutiquesBusiness.getOverriddenInputs(descriptor);
        if (overriddenInputs != null) {
            for (String key : overriddenInputs.keySet()) {
                String value = overriddenInputs.get(key);
                if (parametersMap.containsKey(value)) {
                    parametersMap.put(key, parametersMap.get(value));
                } else {
                    logger.error("missing parameter {}", value);
                    throw new VipException("missing parameter " + value);
                }
            }
        }
    }

    @Override
    public void launchSimulation(Map<String, String> parametersMap,
            String applicationName, String applicationVersion,
            String applicationClass, String simulationName) throws VipException {

        // fill in overriddenInputs from explicit inputs

        trace(logger, "Launching simulation '" + simulationName + "' (" + applicationName + ").");
        User user = getSessionUser();

        List<String> groups = new ArrayList<String>();
        for (Group group : getUserGroupsFromSession().keySet()) {
            groups.add(group.getName());
        }

        for (Map.Entry<String, String> p : parametersMap.entrySet()) {
            logger.info("received param {} : {}", p.getKey(), p.getValue());
        }
        fillInOverriddenInputs(parametersMap, applicationName, applicationVersion);
        String simulationID = workflowBusiness.launch(user, groups,
                parametersMap, applicationName, applicationVersion, simulationName);

        trace(logger, "Simulation '" + simulationName + "' launched with ID '" + simulationID + "'.");
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
     * @param simulationID
     * @return
     * @throws VipException
     */
    public Simulation getSimulation(String simulationID) throws VipException {
        return workflowBusiness.getSimulation(simulationID);
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
     * @throws VipException
     */
    public void deleteLogData(String path) throws VipException {
        workflowBusiness.deleteLogData(path);
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
     * @throws VipException
     */
    public List<String> getPerformanceStats(List<Simulation> simulationList, int type) throws VipException {

        try {
            return workflowBusiness.getPerformanceStats(simulationList, type);
        } catch (WorkflowsDBDAOException ex) {
            logger.error("Error getting perf stats for {}", simulationList, ex);
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
