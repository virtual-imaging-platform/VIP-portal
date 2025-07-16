package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationStatus implements IsSerializable {

    private int runningWorkflows;
    private int runningTasks;
    private int waitingTasks;

    public ApplicationStatus() {
    }

    public int getRunningTasks() {
        return runningTasks;
    }

    public void setRunningTasks(int runningTasks) {
        this.runningTasks = runningTasks;
    }

    public int getRunningWorkflows() {
        return runningWorkflows;
    }

    public void setRunningWorkflows(int runningWorkflows) {
        this.runningWorkflows = runningWorkflows;
    }

    public int getWaitingTasks() {
        return waitingTasks;
    }

    public void setWaitingTasks(int waitingTasks) {
        this.waitingTasks = waitingTasks;
    }
}
