package fr.insalyon.creatis.vip.core.client.view.system.group;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class GroupsToolStrip extends ToolStrip {

    public GroupsToolStrip() {
        
        this.setWidth100();

        ToolStripButton addButton = new ToolStripButton("Add Group");
        addButton.setIcon(CoreConstants.ICON_ADD);
        addButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ManageGroupsTab groupTab = (ManageGroupsTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_MANAGE_GROUPS);
                groupTab.setGroup(null, false, null, false);
            }
        });
        this.addButton(addButton);

        ToolStripButton refreshButton = new ToolStripButton("Refresh");
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ManageGroupsTab usersTab = (ManageGroupsTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_MANAGE_GROUPS);
                usersTab.loadGroups();
            }
        });
        this.addButton(refreshButton);
    }
}
