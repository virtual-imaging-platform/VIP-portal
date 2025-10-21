package fr.insalyon.creatis.vip.application.client.view.system.applications.app;

import java.util.List;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import fr.insalyon.creatis.vip.application.client.view.system.SystemUtils;
import fr.insalyon.creatis.vip.application.models.Application;

public class ApplicationRecord extends ListGridRecord {

    public ApplicationRecord(Application app) {
        setAttribute("name", app.getName());
        setAttribute("owner", app.getOwner());
        setAttribute("ownerFullName", app.getFullName());
        setAttribute("citation", app.getCitation());
        setAttribute("groupsLabel", SystemUtils.formatGroups(app.getGroups()));
        setAttribute("groups", app.getGroupsNames());
    }

    public ApplicationRecord(Application app, List<String> resources) {
        this(app);
        setAttribute("resources", resources);
    }
}
