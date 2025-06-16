package fr.insalyon.creatis.vip.application.client.view.system.resources;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.bean.Resource;

public class ResourceRecord extends ListGridRecord {

    public ResourceRecord(Resource resource) {
        setAttribute("name", resource.getName());
        setAttribute("status", resource.getStatus());
        setAttribute("type", resource.getType());
        setAttribute("configuration", resource.getConfiguration());
        setAttribute("engines", resource.getEngines());
        setAttribute("groups", resource.getGroups());
    }
}