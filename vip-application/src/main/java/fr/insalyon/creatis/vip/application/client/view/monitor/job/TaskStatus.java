package fr.insalyon.creatis.vip.application.client.view.monitor.job;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public enum TaskStatus {

    SUCCESSFULLY_SUBMITTED("#CC9933", "Submitted"),
    QUEUED("#DBA400", "Queued"),
    RUNNING("#8CC653", "Running"),
    COMPLETED("#287fd6", "Completed"),
    DELETED("#FF8575", "Deleted"),
    DELETED_REPLICA("#287fd6", "Deleted (Replica)"),
    CANCELLED("#FF8575", "Cancelled"),
    CANCELLED_REPLICA("#287fd6", "Cancelled (Replica)"),
    ERROR_FINISHING("#d64949", "Failed"), // not yet in gasw
    ERROR_RESUBMITTING("#d64949", "Failed"), // not yet in gasw
    ERROR("#d64949", "Failed"),
    STALLED("#1A767F", "Stalled"),
    STALLED_FINISHING("#1A767F", "Stalled"), // not yet in gasw
    STALLED_RESUBMITTING("#1A767F", "Stalled"), // not yet in gasw
    REPLICATE("#8CC653", "Replicate"),
    REPLICATING("#8CC653", "Replicating"), // not yet in gasw
    REPLICATED("#8CC653", "Replicated"), // not yet in gasw
    KILL("#8CC653", "Kill"),
    KILL_REPLICA("#8CC653", "Kill"),
    RESCHEDULE("#8CC653", "Reschedule"),
    ERROR_HELD("#9CAB4C", "Held"),
    STALLED_HELD("#9CAB4C", "Held"),
    UNHOLD_ERROR("#9CAB4C", "Unhold"),
    UNHOLD_STALLED("#9CAB4C", "Unhold");
    //
    private String color;
    private String description;

    private TaskStatus(String color, String description) {

        this.color = color;
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if the status is a running state.
     *
     * @return
     */
    public boolean isRunningState() {

        if (this == SUCCESSFULLY_SUBMITTED
                || this == QUEUED
                || this == RUNNING
                || this == REPLICATE
                || this == REPLICATED
                || this == REPLICATING
                || this == KILL
                || this == KILL_REPLICA
                || this == RESCHEDULE) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the status is a completed state with outputs.
     *
     * @return
     */
    public boolean isCompletedStateWithOutputs() {

        if (this == COMPLETED
                || this == ERROR) {
            return true;
        }
        return false;
    }
}
