package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.models.PublicExecution;

public class PublicExecutionRecord extends ListGridRecord {

    public PublicExecutionRecord(PublicExecution execution) {
        this(execution.getExperienceName(),
            String.join(PublicExecution.SEPARATOR, execution.getWorkflowsIds()),
            String.join(PublicExecution.SEPARATOR, execution.getApplicationsNames()),
            String.join(PublicExecution.SEPARATOR, execution.getApplicationsVersions()),
            execution.getStatus(),
            execution.getAuthor(),
            execution.getComments(),
            execution.getDoi()
        );
    }

    public PublicExecutionRecord(
            String experienceName, String workflowsIds, String applicationsNames, String applicationsVersions,
            PublicExecution.PublicExecutionStatus status, String author, String comments, String doi) {
        setAttribute("experience_name", experienceName);
        setAttribute("workflows_ids", workflowsIds);
        setAttribute("applications_names", applicationsNames);
        setAttribute("applications_versions", applicationsVersions);
        setAttribute("status", status.name());
        setAttribute("author", author);
        setAttribute("comments", comments);
        setAttribute("doi", doi);
    }
}
