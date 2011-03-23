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
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import fr.insalyon.creatis.vip.portal.client.view.application.launch.LaunchLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.application.manage.ManageCenterPanel;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.MonitorLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.WorkflowsPanel;
import fr.insalyon.creatis.vip.common.client.view.Context;

/**
 *
 * @author Rafael Silva, Tristan Glatard
 */
public class ApplicationToolbarButton extends ToolbarButton {

    public ApplicationToolbarButton(final String applicationClass, final boolean isAdmin) {

        super(applicationClass);

        Menu applicationMenu = new Menu();
        applicationMenu.setShadow(true);
        applicationMenu.setMinWidth(10);

        Item launchApplicationItem = new Item("Launch " + applicationClass, new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                LaunchLeftPanel launchLeftPanel = LaunchLeftPanel.getInstance();
                launchLeftPanel.setApplicationClass(applicationClass);
                Layout.getInstance().setLeftPanel(launchLeftPanel);
            }
        });
        if (!Context.getInstance().getAuthentication().isProxyValid()) {
            launchApplicationItem.setDisabled(true);
        }
        applicationMenu.addItem(launchApplicationItem);

        Item monitorApplicationItem = new Item("Monitor " + applicationClass, new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                Layout layout = Layout.getInstance();
                if (layout.hasCenterPanelTab("app-workflows-panel")) {
                    layout.setActiveCenterPanel("app-workflows-panel");
                    WorkflowsPanel.getInstance().reloadData();
                } else {
                    WorkflowsPanel panel = WorkflowsPanel.getInstance();
                    layout.setCenterPanel(panel);
                    panel.setApplicationClass(applicationClass);
                    if (isAdmin) {
                        panel.loadWorkflowData();
                    } else {
                        String user = Context.getInstance().getAuthentication().getUser();
                        panel.loadWorkflowData(user, null, null, null, null);
                    }
                }
                Ext.get("app-workflows-grid").mask("Loading data...");
                MonitorLeftPanel monitorLeftPanel = MonitorLeftPanel.getInstance();
                monitorLeftPanel.setApplicationClass(applicationClass);
                layout.setLeftPanel(monitorLeftPanel);
            }
        });
        applicationMenu.addItem(monitorApplicationItem);

        if (isAdmin) {
            applicationMenu.addSeparator();
            Item manageApplicationItem = new Item("Manage " + applicationClass, new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    ManageCenterPanel manageCenterPanel = ManageCenterPanel.getInstance();
                    manageCenterPanel.setApplicationClass(applicationClass);
                    manageCenterPanel.loadData();
                    Layout.getInstance().setCenterPanel(manageCenterPanel);
                }
            });
            applicationMenu.addItem(manageApplicationItem);
        }

        this.setMenu(applicationMenu);
    }
}
