package fr.insalyon.creatis.vip.core.client.view.system.user;

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
public class UsersToolStrip extends ToolStrip {

    public UsersToolStrip() {

        this.setWidth100();
        ToolStripButton  searchButton = new ToolStripButton("Search");
        searchButton.setIcon(CoreConstants.ICON_SEARCH);
        searchButton.setWidth(150);
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
               ManageUsersTab usersTab = (ManageUsersTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_MANAGE_USERS);
                usersTab.setFilter();
            }
        });

        
        
        ToolStripButton refreshButton = new ToolStripButton("Refresh");
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ManageUsersTab usersTab = (ManageUsersTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_MANAGE_USERS);
                usersTab.loadUsers();
            }
        });
        this.addButton(refreshButton);
        this.addButton(searchButton);
    }
}
