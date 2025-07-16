package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProcessorStatus;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Activity implements IsSerializable {

    private String name;
    private ProcessorStatus status;
    private int completed;
    private int queued;
    private int failed;

    public Activity() {
    }

    public Activity(String name, ProcessorStatus status, int completed, int queued, int failed) {

        this.name = name;
        this.status = status;
        this.completed = completed;
        this.queued = queued;
        this.failed = failed;
    }

    public int getCompleted() {
        return completed;
    }

    public int getFailed() {
        return failed;
    }

    public String getName() {
        return name;
    }

    public int getQueued() {
        return queued;
    }

    public ProcessorStatus getStatus() {
        return status;
    }
}
