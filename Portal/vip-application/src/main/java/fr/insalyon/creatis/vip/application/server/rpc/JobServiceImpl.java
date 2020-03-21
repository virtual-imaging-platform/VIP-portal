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

import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.SimulationFileType;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobServiceImpl extends AbstractRemoteServiceServlet implements JobService {

    private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);
    private SimulationBusiness simulationBusiness;

    public JobServiceImpl() {

        simulationBusiness = new SimulationBusiness();
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

        } catch (BusinessException ex) {
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
        try(Connection connection = PlatformConnection.getInstance().getConnection()) {
            return simulationBusiness.getTasks(
                simulationID, jobID, getSessionUser().getFolder(), connection);
        } catch (CoreException | BusinessException | SQLException ex) {
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

        } catch (BusinessException ex) {
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
    public List<Task> getJobsList(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getJobsList(simulationID);

        } catch (BusinessException ex) {
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

        } catch (BusinessException ex) {
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

        } catch (BusinessException ex) {
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

        } catch (BusinessException ex) {
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
    public List<String> getJobFlow(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getJobsPerTime(simulationID);

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
    public List<String> getCkptsPerJob(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getCkptsPerJob(simulationID);

        } catch (BusinessException ex) {
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

        } catch (BusinessException ex) {
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
    public List<String> getSiteHistogram(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getSiteHistogram(simulationID);

        } catch (BusinessException ex) {
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
    public Map<String, Integer> getCountriesMap(String simulationID) throws ApplicationException {

        try {
            return simulationBusiness.getCountriesMap(simulationID);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }
}
