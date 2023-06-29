package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Execution implements IsSerializable {
    private String id;
    private String simulationName;
    private String applicationName;
    private String version;
    private String status;
    private String author;
    private String comments;
    public Execution() {}

    public Execution(String id, String simulationName,String applicationName, String version, String status, String author, String comments) {
        this.id = id;
        this.simulationName = simulationName;
        this.applicationName = applicationName;
        this.version = version;
        this.status = status;
        this.author = author;
        this.comments = comments;
    }

    public String getId() { return id; }
    public String getSimulationName() {
        return simulationName;
    }
    public String getApplicationName() { return applicationName;}
    public String getVersion() {
        return version;
    }
    public String getStatus() {
        return status;
    }
    public String getAuthor() {
        return author;
    }
    public String getComments() {
        return comments;
    }
}

