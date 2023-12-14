package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PublicExecution implements IsSerializable {

    private String id;
    private String simulationName;
    private String applicationName;
    private String applicationVersion;
    private PublicExecutionStatus status;
    private String author;
    private String comments;

    public enum PublicExecutionStatus {
        REQUESTED,
        DIRECTORY_CREATED,
        PUBLISHED
    }

    public PublicExecution() {}

    public PublicExecution(String id, String simulationName, String applicationName, String applicationVersion, PublicExecutionStatus status, String author, String comments) {
        this.id = id;
        this.simulationName = simulationName;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.status = status;
        this.author = author;
        this.comments = comments;
    }

    public String getId() { return id; }
    public String getSimulationName() {
        return simulationName;
    }
    public String getApplicationName() { return applicationName;}
    public String getApplicationVersion() {
        return applicationVersion;
    }
    public PublicExecutionStatus getStatus() {
        return status;
    }
    public String getAuthor() {
        return author;
    }
    public String getComments() {
        return comments;
    }
}

