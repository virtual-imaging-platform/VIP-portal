package fr.insalyon.creatis.vip.api.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fr.insalyon.creatis.vip.api.model.serializing.InputValuesDeserializer;

public class Execution {

    private String identifier;
    @NotNull
    private String name;
    @NotNull
    private String pipelineIdentifier;
    private int timeout;
    private ExecutionStatus status;
    /* the inputValues property has a peculiar behavior
    As input, it can be a list of maps or a map. InputValuesDeserializer handles that and produces a list of
    maps in all cases. This list can be accessed through the getInputValuesForInit method.
    As output, it is always (at the moment) a map. This map can be accessed/modified through the inputValuesForDisplay
    property.
    The getInputValuesForJackson ensure the serialization as a map. It is private to keep this behavior
    internal. This needs access READ_WRITE to work.
    So Jackson uses the InputValuesDeserializer for deserialization and the getInputValuesForJackson for serialization.
    All other methods for inputValues are ignored by jackson, and are meant to make available the appropriate input
    values for init or display.
    */
    @NotNull
    @JsonProperty(value = "inputValues", access = JsonProperty.Access.READ_WRITE)
    @JsonDeserialize(using = InputValuesDeserializer.class)
    private List<Map<String, Object>> inputValuesForJackson;
    @JsonIgnore
    private Map<String, Object> inputValuesForDisplay;
    private Map<String, List<java.lang.Object>> returnedFiles;

    // optional arguments
    private String studyIdentifier;
    private Integer errorCode;
    private Long startDate;
    private Long endDate;
    private Object resultsLocation;
    private Map<Integer, Map<String, Object>> jobs; // jobId -> status

    public Execution() {
        inputValuesForDisplay = new HashMap<>();
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
                     Object resultsLocation) {
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
   
    @JsonIgnore
    // allow to fetch the inputValues list deserialized by jackson
    // only used on execution init
    public List<Map<String, Object>> getInputValuesForInit() {
        return inputValuesForJackson;
    }

    /*
      make it private to avoid accidental use
      only used by jackson for serializing
      and we want to serialize the map version
     */
    private Map<String, Object> getInputValuesForJackson() {
        return inputValuesForDisplay;
    }

    public void setInputValuesForDisplay(Map<String, Object> inputValues) {
        this.inputValuesForDisplay = inputValues;
    }

    public Map<String, Object> getInputValuesForDisplay() {
        return inputValuesForDisplay;
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

    public Object getResultsLocation() {
        return resultsLocation;
    }

    public void setResultsLocation(Object resultsLocation) {
        this.resultsLocation = resultsLocation;
    }

    public Map<Integer, Map<String, Object>> getJobs() { return jobs; }

    public void setJobs(Map<Integer, Map<String, Object>> jobs) { this.jobs = jobs; }
}
