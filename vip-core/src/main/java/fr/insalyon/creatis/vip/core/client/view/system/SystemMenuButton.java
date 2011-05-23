/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view.system;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.system.application.classes.ManageClassesTab;
import fr.insalyon.creatis.vip.core.client.view.system.configuration.group.ManageGroupsTab;
import fr.insalyon.creatis.vip.core.client.view.system.configuration.user.ManageUsersTab;

/**
 *
 * @author Rafael Silva
 */
public class SystemMenuButton extends ToolStripMenuButton {

    public SystemMenuButton() {

        this.setTitle("System");
        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(3);

        Menu confSubMenu = new Menu();
        MenuItem manageUsers = new MenuItem("Manage Users");
        manageUsers.setIcon("icon-users.png");
        manageUsers.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ManageUsersTab());
            }
        });
        
        MenuItem manageGroups = new MenuItem("Manage Groups");
        manageGroups.setIcon("icon-groups.png");
        manageGroups.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ManageGroupsTab());
            }
        });
        
        confSubMenu.setItems(manageUsers, manageGroups);
        
        MenuItem configurationItem = new MenuItem("Configuration");
        configurationItem.setSubmenu(confSubMenu);
        configurationItem.setIcon("icon-configuration.png");
        
        Menu appSubMenu = new Menu();
        MenuItem manageApps = new MenuItem("Manage Applications");
        manageApps.setIcon("icon-application-manage.png");
        manageApps.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        MenuItem manageClasses = new MenuItem("Manage Classes");
        manageClasses.setIcon("icon-class.png");
        manageClasses.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ManageClassesTab());
            }
        });
        
        appSubMenu.setItems(manageApps, manageClasses);
        
        MenuItem applicationItem = new MenuItem("Applications");
        applicationItem.setIcon("icon-application.png");
        applicationItem.setSubmenu(appSubMenu);

        menu.setItems(configurationItem, applicationItem);

        this.setMenu(menu);
    }
}
