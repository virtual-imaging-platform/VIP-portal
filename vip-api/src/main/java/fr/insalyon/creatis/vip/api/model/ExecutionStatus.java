package fr.insalyon.creatis.vip.api.model;

import com.fasterxml.jackson.annotation.*;


public enum ExecutionStatus {

    INITIALIZING("Initializing"),
    READY("Ready"),
    RUNNING("Running"),
    FINISHED("Finished"),
    INITIALIZATION_FAILED("InitializationFailed"),
    EXECUTION_FAILED("ExecutionFailed"),
    UNKNOWN("Unknown"),
    KILLED("Killed");

    private String restLabel;

    ExecutionStatus(String restLabel) {
        this.restLabel = restLabel;
    }

    @JsonCreator
    public static ExecutionStatus fromRestLabel(String restlabel) {
        for (ExecutionStatus status : values()) {
            if (status.restLabel.equals(restlabel)) { return status; }
        }
        throw new IllegalArgumentException("Unknown execution status : " + restlabel);
    }

    @JsonValue
    public String getRestLabel() {
        return restLabel;
    }
}
