package fr.insalyon.creatis.vip.application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.application.client.view.monitor.WorkflowStatus;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.inter.DataViews;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is almost the same as the workflow class from workflowsdb
 *
 */
@JsonView(DataViews.User.class)
public class Workflow implements IsSerializable {

    private String id;
    @NotBlank
    private String workflowName;
    @NotBlank
    private String applicationName;
    @NotBlank
    private String applicationVersion;
    private WorkflowStatus status;
    private Map<String,WorkflowInput> inputs;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Map<String,List<String>> outputs;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userFullName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date startDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date endDate;

    @JsonIgnore
    private String engineEndpoint;
    @JsonIgnore
    private String tags;

    public Workflow() {
    }

    public Workflow(String workflowName, String applicationName, String applicationVersion) {
        this.workflowName = workflowName;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
    }

    public Workflow(String id, String workflowName, String applicationName, String applicationVersion,
                    User user, String status, Date startDate, Date endDate,
                    String engineEndpoint, String tags) {

        this.id = id;
        this.workflowName = workflowName;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.userId = user.getId();
        this.userFullName = user.getFullName();
        this.status = status == null ? null : WorkflowStatus.valueOf(status);
        this.startDate = startDate;
        this.endDate = endDate;
        this.engineEndpoint = engineEndpoint;
        this.tags = tags;
    }

    public Workflow(String id, String workflowName, String applicationName, String applicationVersion,
                    String userFullName, String status, Date startDate, Date endDate,
                    String engineEndpoint, String tags) {

        this.id = id;
        this.workflowName = workflowName;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.userId = null;
        this.userFullName = userFullName;
        this.status = WorkflowStatus.valueOf(status);
        this.startDate = startDate;
        this.endDate = endDate;
        this.engineEndpoint = engineEndpoint;
        this.tags = tags;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setUser(User user) {
        if (user == null) {
            this.userId = null;
        } else {
            this.userId = user.getId();
        }
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getID() {
        return id;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }

    public String getEngineEndpoint() {
        return engineEndpoint;
    }

    public void setEngineEndpoint(String engineEndpoint) {
        this.engineEndpoint = engineEndpoint;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return applicationName + "\n" + id + "\n" + userId + "\n" + startDate;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public Map<String, WorkflowInput> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, WorkflowInput> inputs) {
        this.inputs = inputs;
    }

    public Map<String, List<String>> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, List<String>> outputs) {
        this.outputs = outputs;
    }
}
