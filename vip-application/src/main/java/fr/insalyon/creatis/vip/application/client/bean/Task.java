package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Task implements IsSerializable {

    private String id;
    private TaskStatus status;
    private int exitCode;
    private String siteName;
    private String nodeName;
    private String command;
    private String fileName;
    private String[] parameters;
    private int minorStatus;
    private int jobID;

    public Task() {
    }

    public Task(int jobID, TaskStatus status, String command) {

        this.jobID = jobID;
        this.status = status;
        this.command = command;
    }

    public Task(String id, TaskStatus status, String command, String fileName,
            int exitCode, String siteName, String nodeName, int minorStatus,
            String... parameters) {

        this.id = id;
        this.status = status;
        this.command = command;
        this.fileName = fileName;
        this.exitCode = exitCode;
        this.siteName = siteName;
        this.nodeName = nodeName;
        this.parameters = parameters;
        this.minorStatus = minorStatus;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public int getMinorStatus() {
        return minorStatus;
    }

    public void setMinorStatus(int minorStatus) {
        this.minorStatus = minorStatus;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }
}
