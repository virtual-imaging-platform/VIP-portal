package fr.insalyon.creatis.vip.api.model;

import java.util.*;
import jakarta.validation.constraints.NotNull;

public class Execution {

    private String identifier;
    @NotNull
    private String name;
    @NotNull
    private String pipelineIdentifier;
    private int timeout;
    private ExecutionStatus status;
    @NotNull
    private Map<String, java.lang.Object> inputValues;
    private Map<String, List<java.lang.Object>> returnedFiles;

    // optional arguments
    private String studyIdentifier;
    private Integer errorCode;
    private Long startDate;
    private Long endDate;
    private String resultsLocation;
    private Map<Integer, Map<String, Object>> jobs; // jobId -> status

    public Execution() {
        inputValues = new HashMap<>();
        returnedFiles = new HashMap<>();
        jobs = new HashMap<>();
    }

    public Execution(String identifier,
                     String name,
                     String pipelineIdentifier,
                     int timeout,
                     ExecutionStatus status,
                     String studyIdentifier,
                     Integer errorCode,
                     Long startDate,
                     Long endDate,
                     String resultsLocation) {
        this();
        this.identifier = identifier;
        this.name = name == null ? identifier : name; // null names sometimes happen due to a race condition in VIP.
        this.pipelineIdentifier = pipelineIdentifier;
        this.timeout = timeout;
        this.status = status;
        this.studyIdentifier = studyIdentifier;
        this.errorCode = errorCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resultsLocation = resultsLocation;

    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPipelineIdentifier() {
        return pipelineIdentifier;
    }

    public void setPipelineIdentifier(String pipelineIdentifier) {
        this.pipelineIdentifier = pipelineIdentifier;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Map<String, java.lang.Object> getInputValues() {
        return inputValues;
    }

    public void setInputValues(Map<String, java.lang.Object> inputValues) {
        this.inputValues = inputValues;
    }

    public Map<String, List<java.lang.Object>> getReturnedFiles() {
        return returnedFiles;
    }

    public void setReturnedFiles(Map<String, List<java.lang.Object>>  returnedFiles) {
        this.returnedFiles = returnedFiles;
    }

    public void clearReturnedFiles() {
        returnedFiles = null;
    }

    public String getStudyIdentifier() {
        return studyIdentifier;
    }

    public void setStudyIdentifier(String studyIdentifier) {
        this.studyIdentifier = studyIdentifier;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getResultsLocation() {
        return resultsLocation;
    }

    public void setResultsLocation(String resultsLocation) {
        this.resultsLocation = resultsLocation;
    }

    public Map<Integer, Map<String, Object>> getJobs() { return jobs; }

    public void setJobs(Map<Integer, Map<String, Object>> jobs) { this.jobs = jobs; }
}
