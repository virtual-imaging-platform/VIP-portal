package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;

public class ExecutionsRecord extends ListGridRecord {

    public ExecutionsRecord(
            String id, String simulationName, String applicationName, String version,
            PublicExecution.PublicExecutionStatus status, String author, String comments) {
        setAttribute("id", id);
        setAttribute("simulation_name", simulationName);
        setAttribute("application_name", applicationName);
        setAttribute("version", version);
        setAttribute("status", status.name());
        setAttribute("author", author);
        setAttribute("comments", comments);
    }
}


