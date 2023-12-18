package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PublicExecution implements IsSerializable {

    public static String OUTPUT_NAMES_SEPARATOR = "##";

    private String id;
    private String simulationName;
    private String applicationName;
    private String applicationVersion;
    private PublicExecutionStatus status;
    private String author;
    private String comments;
    private List<String> outputNames;

    public PublicExecution(String simulationID, String simulationName,
                           String applicationName, String applicationVersion, String author) {
        this(simulationID, simulationName, applicationName, applicationVersion,
                null, author, null, Collections.emptyList());
    }

    public enum PublicExecutionStatus {
        REQUESTED,
        DIRECTORY_CREATED,
        PUBLISHED;
    }
    public PublicExecution() {}

    public PublicExecution(
            String id, String simulationName, String applicationName, String applicationVersion,
            PublicExecutionStatus status, String author, String comments, String outputNamesAsString) {
        this(id, simulationName, applicationName, applicationVersion, status, author, comments,
                Arrays.asList(outputNamesAsString.split(OUTPUT_NAMES_SEPARATOR)));
    }

    public PublicExecution(
            String id, String simulationName, String applicationName, String applicationVersion,
            PublicExecutionStatus status, String author, String comments, List<String> outputNames) {
        this.id = id;
        this.simulationName = simulationName;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.status = status;
        this.author = author;
        this.comments = comments;
        this.outputNames = outputNames;
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

    public List<String> getOutputNames() {
        return outputNames;
    }
}

