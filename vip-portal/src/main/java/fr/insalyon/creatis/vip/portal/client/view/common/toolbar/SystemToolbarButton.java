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
package fr.insalyon.creatis.vip.portal.client.view.common.toolbar;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import fr.insalyon.creatis.vip.portal.client.view.system.application.ManageApplicationsCenterPanel;
import fr.insalyon.creatis.vip.portal.client.view.system.application.ManageClassesCenterPanel;
import fr.insalyon.creatis.vip.portal.client.view.system.user.ManageGroupsCenterPanel;
import fr.insalyon.creatis.vip.portal.client.view.system.user.ManageUsersCenterPanel;

/**
 *
 * @author Rafael Silva
 */
public class SystemToolbarButton extends ToolbarButton {

    public SystemToolbarButton(String title) {

        super(title);

        Menu systemMenu = new Menu();
        systemMenu.setShadow(true);
        systemMenu.setMinWidth(10);

        Menu confSubMenu = new Menu();
        confSubMenu.setShadow(true);

        Item usersItem = new Item("Manage Users", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                Layout.getInstance().setCenterPanel(ManageUsersCenterPanel.getInstance());
            }
        });
        
        Item groupsItem = new Item("Manage Groups", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                Layout.getInstance().setCenterPanel(ManageGroupsCenterPanel.getInstance());
            }
        });

        confSubMenu.addItem(usersItem);
        confSubMenu.addItem(groupsItem);

        MenuItem confMenuItem = new MenuItem("Configuration", confSubMenu);
        systemMenu.addItem(confMenuItem);

        Menu appSubMenu = new Menu();
        appSubMenu.setShadow(true);

        Item manageApplicationsItem = new Item("Manage Applications", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                Layout.getInstance().setCenterPanel(ManageApplicationsCenterPanel.getInstance());
            }
        });

        Item manageClassesItem = new Item("Manage Classes", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                Layout.getInstance().setCenterPanel(ManageClassesCenterPanel.getInstance());
            }
        });

        appSubMenu.addItem(manageApplicationsItem);
        appSubMenu.addItem(manageClassesItem);

        MenuItem appMenuItem = new MenuItem("Applications", appSubMenu);
        systemMenu.addItem(appMenuItem);

        this.setMenu(systemMenu);
    }
}
