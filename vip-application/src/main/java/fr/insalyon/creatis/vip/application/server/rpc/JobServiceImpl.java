package fr.insalyon.creatis.vip.application.server.rpc;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.SimulationFileType;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
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
     * @throws ApplicationException
     */
    @Override
    public List<Job> getList(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getList(simulationID);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Gets a list of tasks of a simulation with determined parameters.
     *
     * @param simulationID Simulation identification
     * @param jobID Job identification
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<Task> getTasks(String simulationID, int jobID) throws ApplicationException {
        try {
            return simulationBusiness.getTasks(
                simulationID, jobID, getSessionUser().getFolder());
        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Reads a simulation file.
     *
     * @param simulationID Simulation identification
     * @param taskID Task identification
     * @param fileType Simulation file type
     * @return
     * @throws ApplicationException
     */
    @Override
    public String[] readSimulationFile(String simulationID, String taskID,
            SimulationFileType fileType) throws ApplicationException {

        try {
            return simulationBusiness.readSimulationFile(simulationID, taskID, fileType);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Sends a signal to a task.
     *
     * @param simulationID Simulation identification
     * @param taskID Task identification
     * @param status Simulation
     */
    @Override
    public void sendTaskSignal(String simulationID, String taskID, String status) throws ApplicationException {

        try {
            simulationBusiness.sendTaskSignal(simulationID, taskID, TaskStatus.valueOf(status));

        } catch (VipException ex) {
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
    public List<Task> getJobsList(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getJobsList(simulationID);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param folder
     * @param fileName
     * @param extension
     * @return
     * @throws ApplicationException
     */
    @Override
    public String readFile(String simulationID, String folder, String fileName,
            String extension) throws ApplicationException {

        try {
            return simulationBusiness.readFile(simulationID, folder, fileName, extension);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<String> getExecutionPerNumberOfJobs(String simulationID,
            int binSize) throws ApplicationException {

        try {
            return simulationBusiness.getExecutionPerNumberOfJobs(simulationID, binSize);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<String> getDownloadPerNumberOfJobs(String simulationID,
            int binSize) throws ApplicationException {

        try {
            return simulationBusiness.getDownloadPerNumberOfJobs(simulationID, binSize);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<String> getUploadPerNumberOfJobs(String simulationID,
            int binSize) throws ApplicationException {

        try {
            return simulationBusiness.getUploadPerNumberOfJobs(simulationID, binSize);

        } catch (VipException ex) {
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
    public List<String> getJobFlow(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getJobsPerTime(simulationID);

        } catch (VipException ex) {
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
    public List<String> getCkptsPerJob(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getCkptsPerJob(simulationID);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param siteName
     * @param nodeName
     * @return
     * @throws ApplicationException
     */
    @Override
    public Node getNode(String simulationID, String siteName, String nodeName)
            throws ApplicationException {

        try {
            return simulationBusiness.getExecutionNode(simulationID, siteName, nodeName);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param jobID
     * @param status
     * @throws ApplicationException
     */
    @Override
    public void sendSignal(String simulationID, String jobID, String status)
            throws ApplicationException {

        try {
            trace(logger, "Sending '" + status + "' signal to '" + jobID
                    + "' (" + simulationID + ").");
            simulationBusiness.sendSignal(simulationID, jobID, TaskStatus.valueOf(status));

        } catch (VipException ex) {
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
    public List<String> getSiteHistogram(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getSiteHistogram(simulationID);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param jobIDs
     * @param status
     * @throws ApplicationException
     */
    @Override
    public void sendSignal(String simulationID, List<String> jobIDs,
            String status) throws ApplicationException {

        try {
            trace(logger, "Sending '" + status + "' signal to '"
                    + jobIDs.toString() + "' (" + simulationID + ").");
            simulationBusiness.sendSignal(simulationID, jobIDs, TaskStatus.valueOf(status));

        } catch (VipException ex) {
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
    public Map<String, Integer> getCountriesMap(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getCountriesMap(simulationID);

        } catch (VipException ex) {
            throw new ApplicationException(ex);
        }
    }
}
