package fr.insalyon.creatis.vip.application.models;

import fr.insalyon.creatis.vip.core.models.Triplet;

public class WorkflowData extends Triplet<String, String, String> {
    
    public WorkflowData() {
        super();
    }

    public WorkflowData(String workflowId, String appName, String appVersion){
        super(workflowId, appName, appVersion);
    }

    public String getWorkflowId() {
        return getFirst();
    }

    public String getAppName() {
        return getSecond();
    }

    public String getAppVersion() {
        return getThird();
    }
}
