package fr.insalyon.creatis.vip.application.client.view.system.resources;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ResourceRecord extends ListGridRecord {

    public ResourceRecord(String name, boolean visible, boolean status, String type, String configuration) {
        setAttribute("name", name);
        setAttribute("visible", visible);
        setAttribute("status", status);
        setAttribute("type", type);
        setAttribute("configuration", configuration);
    }
}