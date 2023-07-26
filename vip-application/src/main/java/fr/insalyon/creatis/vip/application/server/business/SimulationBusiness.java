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

import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.JobStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.SimulationFileType;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.server.dao.ExecutionNodeDAO;
import fr.insalyon.creatis.vip.application.server.dao.SimulationDAO;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Rafael Ferreira da Silva
 */
@Service
@Transactional
public class SimulationBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LfcPathsBusiness lfcPathsBusiness;
    private Server server;

    @Autowired
    public SimulationBusiness(LfcPathsBusiness lfcPathsBusiness, Server server) {
        this.lfcPathsBusiness = lfcPathsBusiness;
        this.server = server;
    }

    /*
     Both SimulationDAO and ExecutionNodeDAO have a dbPath parameter field which
     is different for each simulation. So they cannot be used as spring singleton
     (spring default scope) - or this would mean adding the dbPath as parameter
     in each method which is not practical.
     Instead, we use the prototype scope with lookup methods which creates a new
     SimulationDAO/ExecutionNodeDAO at each use with the desired dbPath. */

    @Lookup
    protected SimulationDAO getSimulationDAO(String dbPath) {
        // spring will override it to return a new prototype SimulationData
        // each time
        return null;
    }

    @Lookup
    protected ExecutionNodeDAO getExecutionNodeDAO(String dbPath) {
        // spring will override it to return a new prototype ExecutionNodeData
        // each time
        return null;
    }

    public List<Job> getList(String simulationID) throws BusinessException {

        try {
            Map<Integer, Job> jobsMap = new HashMap<Integer, Job>();

            for (Task task : getSimulationDAO(simulationID).getTasks()) {

                switch (task.getStatus()) {
                    case QUEUED:
                    case SUCCESSFULLY_SUBMITTED:
                        if (jobsMap.containsKey(task.getJobID())) {
                            Job job = jobsMap.get(task.getJobID());
                            if (job.getStatus() == JobStatus.Failed || job.getStatus() == JobStatus.Held) {
                                job.setStatus(JobStatus.Queued_with_errors);
                            }
                        } else {
                            jobsMap.put(task.getJobID(), new Job(task.getJobID(), task.getCommand(), JobStatus.Queued));
                        }
                        break;
                    case RUNNING:
                    case REPLICATE:
                    case REPLICATING:
                    case REPLICATED:
                    case RESCHEDULE:
                    case KILL:
                    case KILL_REPLICA:
                        if (jobsMap.containsKey(task.getJobID())) {
                            Job job = jobsMap.get(task.getJobID());
                            if (job.getStatus() == JobStatus.Queued) {
                                job.setStatus(JobStatus.Running);
                            } else if (job.getStatus() == JobStatus.Queued_with_errors
                                    || job.getStatus() == JobStatus.Failed
                                    || job.getStatus() == JobStatus.Held) {
                                job.setStatus(JobStatus.Running_with_erros);
                            }
                        } else {
                            jobsMap.put(task.getJobID(), new Job(task.getJobID(), task.getCommand(), JobStatus.Running));
                        }
                        break;
                    case COMPLETED:
                    case CANCELLED:
                    case DELETED:
                        if (jobsMap.containsKey(task.getJobID())) {
                            jobsMap.get(task.getJobID()).setStatus(JobStatus.Completed);
                        } else {
                            jobsMap.put(task.getJobID(), new Job(task.getJobID(), task.getCommand(), JobStatus.Completed));
                        }
                        break;
                    case ERROR:
                    case ERROR_FINISHING:
                    case ERROR_RESUBMITTING:
                    case STALLED_FINISHING:
                    case STALLED_RESUBMITTING:
                    case STALLED:
                    case UNHOLD_ERROR:
                    case UNHOLD_STALLED:
                        if (jobsMap.containsKey(task.getJobID())) {
                            Job job = jobsMap.get(task.getJobID());
                            if (job.getStatus() == JobStatus.Queued) {
                                job.setStatus(JobStatus.Queued_with_errors);
                            } else if (job.getStatus() == JobStatus.Running) {
                                job.setStatus(JobStatus.Running_with_erros);
                            }
                        } else {
                            jobsMap.put(task.getJobID(), new Job(task.getJobID(), task.getCommand(), JobStatus.Failed));
                        }
                        break;
                    case ERROR_HELD:
                    case STALLED_HELD:
                        if (jobsMap.containsKey(task.getJobID())) {
                            Job job = jobsMap.get(task.getJobID());
                            if (job.getStatus() == JobStatus.Failed) {
                                job.setStatus(JobStatus.Held);
                            }
                        } else {
                            jobsMap.put(task.getJobID(), new Job(task.getJobID(), task.getCommand(), JobStatus.Held));
                        }
                        break;
                    case CANCELLED_REPLICA:
                    case DELETED_REPLICA:
                        // ignore, there should be at least on another task
                        // for the same invocation
                        break;
                }
            }

            return new ArrayList<Job>(jobsMap.values());

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Task> getTasks(
            String simulationID, int jobID, String currentUserFolder)
            throws BusinessException {

        try {
            List<Task> list = getSimulationDAO(simulationID).getTasks(jobID);
            for (Task task : list) {
                String[] params = task.getParameters();
                for (int i = 0; i < params.length; i++) {
                    params[i] = lfcPathsBusiness.parseRealDir(
                        params[i], currentUserFolder);
                }
                task.setParameters(params);
            }
            return list;

        } catch (DataManagerException | DAOException ex) {
            throw new BusinessException(ex);

        }
    }

    public String[] readSimulationFile(
            String simulationID, String taskID, SimulationFileType fileType)
            throws BusinessException {

        try {
            Task task = getSimulationDAO(simulationID).getTask(taskID);
            String folder = null;
            String extension = null;

            switch (fileType) {
                case OutputFile:
                    folder = "out";
                    extension = ".sh.out";
                    break;
                case ErrorFile:
                    folder = "err";
                    extension = ".sh.err";
                    break;
                case ApplicationOutputFile:
                    folder = "out";
                    extension = ".sh.app.out";
                    break;
                case ApplicationErrorFile:
                    folder = "err";
                    extension = ".sh.app.err";
                    break;
                case ScriptFile:
                    folder = "sh";
                    extension = ".sh";
                default:
            }
            String[] result = new String[2];
            result[0] = "/" + simulationID + "/" + folder + "/" + task.getFileName() + extension;
            result[1] = readFile(simulationID, folder, task.getFileName(), extension);
            return result;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendTaskSignal(String simulationID, String taskID, TaskStatus status)
            throws BusinessException {

        try {
            getSimulationDAO(simulationID).sendTaskSignal(taskID, status);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Task> getJobsList(String simulationID) throws BusinessException {

        try {
            return getSimulationDAO(simulationID).getJobs();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public String readFile(
            String simulationID, String folder, String fileName, String extension)
            throws BusinessException {

        try {
            fileName += extension;

            String filePath = server.getWorkflowsPath() + "/" + simulationID + "/" + folder + "/" + fileName;

            Scanner scanner = new Scanner(new FileInputStream(server.getWorkflowsPath()
                    + "/" + simulationID + "/" + folder + "/" + fileName));

            StringBuilder sb = new StringBuilder();

            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
                sb.append("\n");
            }

            scanner.close();
            return sb.toString();

        } catch (IOException ex) {
            logger.error("Error reading Simulation {} file {}", simulationID, fileName, ex);
            throw new BusinessException(ex);
        }
    }

    public List<String> getExecutionPerNumberOfJobs(
            String simulationID, int binSize) throws BusinessException {

        try {
            return getSimulationDAO(simulationID).getExecutionPerNumberOfJobs(binSize);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getDownloadPerNumberOfJobs(
            String simulationID, int binSize) throws BusinessException {

        try {
            return getSimulationDAO(simulationID).getDownloadPerNumberOfJobs(binSize);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getUploadPerNumberOfJobs(
            String simulationID, int binSize) throws BusinessException {

        try {
            return getSimulationDAO(simulationID).getUploadPerNumberOfJobs(binSize);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getJobsPerTime(
            String simulationID) throws BusinessException {

        try {
            return getSimulationDAO(simulationID).getJobsPerTime();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getCkptsPerJob(String simulationID)
            throws BusinessException {

        try {
            return getSimulationDAO(simulationID).getCkptsPerJob();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getSiteHistogram(String simulationID)
            throws BusinessException {
        try {
            return getSimulationDAO(simulationID).getSiteHistogram();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendSignal(String simulationID, String jobID, TaskStatus status)
            throws BusinessException {

        try {
            getSimulationDAO(simulationID).sendSignal(jobID, status);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void sendSignal(
            String simulationID, List<String> jobIDs, TaskStatus status)
            throws BusinessException {

        for (String jobID : jobIDs) {
            sendSignal(simulationID, jobID, status);
        }
    }

    public int[] getNumberOfActiveTasks(List<Simulation> simulations)
            throws BusinessException {

        try {
            int[] tasks = new int[2];
            for (Simulation simulation : simulations) {
                int[] t = getSimulationDAO(simulation.getID()).getNumberOfActiveTasks();
                tasks[0] += t[0];
                tasks[1] += t[1];
            }
            return tasks;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Node getExecutionNode(String simulationID, String siteName, String nodeName)
            throws BusinessException {

        try {
            return getExecutionNodeDAO(simulationID).getNode(siteName, nodeName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Map<String, Integer> getCountriesMap(String simulationID)
            throws BusinessException {

        try {
            return getSimulationDAO(simulationID).getNodeCountriesMap();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
