package fr.insalyon.creatis.vip.application.client.view.system.resources;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.view.system.SystemUtils;
import fr.insalyon.creatis.vip.application.models.Resource;

public class ResourceRecord extends ListGridRecord {

    public ResourceRecord(Resource resource) {
        setAttribute("name", resource.getName());
        setAttribute("status", resource.getStatus());
        setAttribute("type", resource.getType());
        setAttribute("configuration", resource.getConfiguration());
        setAttribute("engines", resource.getEngines());
        setAttribute("groupsLabel", SystemUtils.formatGroups(resource.getGroups()));
        setAttribute("groups", resource.getGroupsNames());
    }
}