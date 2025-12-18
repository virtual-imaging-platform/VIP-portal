package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.models.Task;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;
import java.util.Map;

public interface SimulationDAO {

    public List<Task> getTasks() throws DAOException;
    
    public List<Task> getTasks(int jobID) throws DAOException;

    public Task getTask(String taskID) throws DAOException;

    public List<String> getInputData(String taskID) throws DAOException;

    public List<String> getOutputData(String invocationFilename) throws DAOException;

    public void sendTaskSignal(String jobID, TaskStatus status) throws DAOException;

    public List<Task> getJobs() throws DAOException;

    public List<String> getExecutionPerNumberOfJobs(int binSize) throws DAOException;

    public List<String> getDownloadPerNumberOfJobs(int binSize) throws DAOException;

    public List<String> getUploadPerNumberOfJobs(int binSize) throws DAOException;

    public List<String> getJobsPerTime() throws DAOException;

    public List<String> getCkptsPerJob() throws DAOException;

    public void sendSignal(String jobID, TaskStatus status) throws DAOException;

    public List<String> getSiteHistogram() throws DAOException;

    public int[] getNumberOfActiveTasks() throws DAOException;

    public Map<String, Integer> getNodeCountriesMap() throws DAOException;
}
