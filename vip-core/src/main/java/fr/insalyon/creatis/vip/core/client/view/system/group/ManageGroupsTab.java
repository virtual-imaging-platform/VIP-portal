package fr.insalyon.creatis.vip.core.client.view.system.group;

import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

/**
 *
 * @author Rafael Silva
 */
public class ManageGroupsTab extends AbstractManageTab {

    private GroupsLayout groupsLayout;
    private EditGroupLayout editLayout;

    public ManageGroupsTab() {
        super(CoreConstants.ICON_GROUP, CoreConstants.APP_GROUP, CoreConstants.TAB_MANAGE_GROUPS);

        groupsLayout = new GroupsLayout();
        editLayout = new EditGroupLayout();
        
        HLayout hLayout = new HLayout(5);
        hLayout.addMember(groupsLayout);
        hLayout.addMember(editLayout);
        
        vLayout.addMember(hLayout);
    }

    public void loadGroups() {
        groupsLayout.loadData();
    }

    public void setGroup(String name, boolean isPublic, String type, boolean auto) {
        editLayout.setGroup(name, isPublic, type, auto);
    }
}
