package fr.insalyon.creatis.vip.application.server.rpc;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.SimulationFileType;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.models.Job;
import fr.insalyon.creatis.vip.application.models.Node;
import fr.insalyon.creatis.vip.application.models.Task;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import jakarta.servlet.ServletException;

public class JobServiceImpl extends AbstractRemoteServiceServlet implements JobService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SimulationBusiness simulationBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        simulationBusiness = getBean(SimulationBusiness.class);
    }

    /**
     * Gets a list of jobs of a simulation.
     *
     * @param simulationID Simulation identification
     * @return
     * @throws VipException
     */
    @Override
    public List<Job> getList(String simulationID) throws VipException {
        return simulationBusiness.getList(simulationID);
    }

    /**
     * Gets a list of tasks of a simulation with determined parameters.
     *
     * @param simulationID Simulation identification
     * @param jobID Job identification
     * @return
     * @throws VipException
     */
    @Override
    public List<Task> getTasks(String simulationID, int jobID) throws VipException {
        return simulationBusiness.getTasks(
            simulationID, jobID, getSessionUser().getFolder());
    }

    /**
     * Reads a simulation file.
     *
     * @param simulationID Simulation identification
     * @param taskID Task identification
     * @param fileType Simulation file type
     * @return
     * @throws VipException
     */
    @Override
    public String[] readSimulationFile(String simulationID, String taskID,
            SimulationFileType fileType) throws VipException {
        return simulationBusiness.readSimulationFile(simulationID, taskID, fileType);
    }

    /**
     * Sends a signal to a task.
     *
     * @param simulationID Simulation identification
     * @param taskID Task identification
     * @param status Simulation
     */
    @Override
    public void sendTaskSignal(String simulationID, String taskID, String status) throws VipException {
        simulationBusiness.sendTaskSignal(simulationID, taskID, TaskStatus.valueOf(status));
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public List<Task> getJobsList(String simulationID) throws VipException {
        return simulationBusiness.getJobsList(simulationID);
    }

    /**
     *
     * @param simulationID
     * @param folder
     * @param fileName
     * @param extension
     * @return
     * @throws VipException
     */
    @Override
    public String readFile(String simulationID, String folder, String fileName,
            String extension) throws VipException {
        return simulationBusiness.readFile(simulationID, folder, fileName, extension);
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws VipException
     */
    @Override
    public List<String> getExecutionPerNumberOfJobs(String simulationID,
            int binSize) throws VipException {
        return simulationBusiness.getExecutionPerNumberOfJobs(simulationID, binSize);
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws VipException
     */
    @Override
    public List<String> getDownloadPerNumberOfJobs(String simulationID,
            int binSize) throws VipException {
        return simulationBusiness.getDownloadPerNumberOfJobs(simulationID, binSize);
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws VipException
     */
    @Override
    public List<String> getUploadPerNumberOfJobs(String simulationID,
            int binSize) throws VipException {
        return simulationBusiness.getUploadPerNumberOfJobs(simulationID, binSize);
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public List<String> getJobFlow(String simulationID) throws VipException {
        return simulationBusiness.getJobsPerTime(simulationID);
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public List<String> getCkptsPerJob(String simulationID) throws VipException {
        return simulationBusiness.getCkptsPerJob(simulationID);
    }

    /**
     *
     * @param simulationID
     * @param siteName
     * @param nodeName
     * @return
     * @throws VipException
     */
    @Override
    public Node getNode(String simulationID, String siteName, String nodeName)
            throws VipException {

        return simulationBusiness.getExecutionNode(simulationID, siteName, nodeName);
    }

    /**
     *
     * @param simulationID
     * @param jobID
     * @param status
     * @throws VipException
     */
    @Override
    public void sendSignal(String simulationID, String jobID, String status)
            throws VipException {
        trace(logger, "Sending '" + status + "' signal to '" + jobID
                + "' (" + simulationID + ").");
        simulationBusiness.sendSignal(simulationID, jobID, TaskStatus.valueOf(status));
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public List<String> getSiteHistogram(String simulationID) throws VipException {
        return simulationBusiness.getSiteHistogram(simulationID);
    }

    /**
     *
     * @param simulationID
     * @param jobIDs
     * @param status
     * @throws VipException
     */
    @Override
    public void sendSignal(String simulationID, List<String> jobIDs,
            String status) throws VipException {

        trace(logger, "Sending '" + status + "' signal to '"
                 + jobIDs.toString() + "' (" + simulationID + ").");
        simulationBusiness.sendSignal(simulationID, jobIDs, TaskStatus.valueOf(status));

    }

    /**
     *
     * @param simulationID
     * @return
     * @throws VipException
     */
    @Override
    public Map<String, Integer> getCountriesMap(String simulationID) throws VipException {
        return simulationBusiness.getCountriesMap(simulationID);
    }
}
