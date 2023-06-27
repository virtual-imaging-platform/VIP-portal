package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ExecutionsRecord extends ListGridRecord {
    public ExecutionsRecord(String name, String version, String status, String author, String comments) {
        setAttribute("name", name);
        setAttribute("version", version);
        setAttribute("status", status);
        setAttribute("author", author);
        setAttribute("comments", comments);
    }
}


