package fr.insalyon.creatis.vip.application.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.JobStatus;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Job implements IsSerializable {

    private int id;
    private String command;
    private JobStatus status;

    public Job() {
    }

    public Job(int id, String command, JobStatus status) {

        this.command = command;
        this.status = status;
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
