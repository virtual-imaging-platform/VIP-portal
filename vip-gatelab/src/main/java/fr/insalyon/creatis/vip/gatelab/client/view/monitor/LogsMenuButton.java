/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import fr.insalyon.creatis.vip.application.client.view.monitor.ViewerWindow;

/**
 *
 * @author camarasu
 */
public class LogsMenuButton extends ToolStripMenuButton{
 public LogsMenuButton(final String simulationID) {

        this.setTitle("Logs");

        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(3);

        MenuItem simulationOutItem = new MenuItem("Simulation Output File");
        simulationOutItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Simulation Output File", simulationID,
                        "", "workflow", ".out").show();
            }
        });

        MenuItem simulationErrItem = new MenuItem("Simulation Error File");
        simulationErrItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Simulation Error File", simulationID,
                        "", "workflow", ".err").show();
            }
        });

        MenuItem simulationInputsItem = new MenuItem("Simulation Inputs File");
        simulationInputsItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Simulation Inputs File", simulationID,
                        "", "input-m2", ".xml").show();
            }
        });

        MenuItem simulationDescItem = new MenuItem("Simulation Descriptor File");
        simulationDescItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Simulation Descriptor File", simulationID,
                        "", "workflow", ".xml").show();
            }
        });

        menu.setItems(simulationOutItem, simulationErrItem, simulationInputsItem,
                simulationDescItem);
        this.setMenu(menu);
    }
}
