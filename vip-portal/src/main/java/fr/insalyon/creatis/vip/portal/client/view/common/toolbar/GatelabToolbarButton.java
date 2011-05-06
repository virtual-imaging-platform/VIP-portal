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
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.core.Ext;
import fr.insalyon.creatis.vip.common.client.view.Context;
//import fr.insalyon.creatis.vip.portal.client.view.application.launch.LaunchLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import fr.insalyon.creatis.vip.portal.client.view.gatelab.GatelabLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.gatelab.WorkflowsPanel;

/**
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
public class GatelabToolbarButton extends ToolbarButton {

    public GatelabToolbarButton(String title) {
        super(title);

        Menu gateLabMenu = new Menu();
        gateLabMenu.setShadow(true);
        gateLabMenu.setMinWidth(10);

        Item launchGateLabItem = new Item("Monitor Simulations", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
//                Layout layout = Layout.getInstance();
//                if (layout.hasCenterPanelTab("gate-workflows-panel")) {
//                    layout.setActiveCenterPanel("gate-workflows-panel");
//                    WorkflowsPanel.getInstance().reloadData();
//                } else {
//                    WorkflowsPanel panel = WorkflowsPanel.getInstance();
//                    layout.setCenterPanel(panel);
//                    panel.loadWorkflowData(Context.getInstance().getAuthentication().getUser(),
//                            "gate", null, null, null);
//                }
                Ext.get("gate-workflows-grid").mask("Loading data...");
                GatelabLeftPanel panel = GatelabLeftPanel.getInstance();
//                layout.setLeftPanel(panel);
            }
        });
        gateLabMenu.addItem(launchGateLabItem);

        Item launchItem = new Item("Launch Simulation", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
//                LaunchLeftPanel launchLeftPanel = LaunchLeftPanel.getInstance();
//                launchLeftPanel.setApplicationClass("GATE");
//                Layout.getInstance().setLeftPanel(launchLeftPanel);
            }
        });
        if (!Context.getInstance().getAuthentication().isProxyValid()) {
            launchItem.setDisabled(true);
        }
        gateLabMenu.addItem(launchItem);

//        Item launchDocItem = new Item("Documentation", new BaseItemListenerAdapter() {
//
//            @Override
//            public void onClick(BaseItem item, EventObject e) {
//                Layout layout = Layout.getInstance();
//
//                Panel doc=new Panel("GateLab Documentation");
//                doc.setId("GateLab Documentation");
//                doc.setHtml("<IFRAME src=\"http://www.creatis.insa-lyon.fr/site/fr/gatelab\">");
//                //doc.load("http://www.creatis.insa-lyon.fr/site/fr/gatelab");
//                layout.setCenterPanel(doc);
//
//
///*
//
//                Frame doc = new Frame("http://www.creatis.insa-lyon.fr/site/fr/gatelab");
//
//                Window window = new Window();
//                window.add(doc);
//                window.show();
//  */
//            }
//        });
//
//        gateLabMenu.addItem(launchDocItem);

        this.setMenu(gateLabMenu);
    }
}
