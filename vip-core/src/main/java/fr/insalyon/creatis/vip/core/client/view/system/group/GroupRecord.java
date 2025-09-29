package fr.insalyon.creatis.vip.core.client.view.system.group;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.core.client.bean.Group;

/**
 *
 * @author Rafael Silva
 */
public class GroupRecord extends ListGridRecord {

    public GroupRecord() { }

    public GroupRecord(Group group) {
        setAttribute("name", group.getName());
        setAttribute("isPublic", group.isPublicGroup());
        setAttribute("type", group.getType().toString());
        setAttribute("auto", group.isAuto());
    }
}
