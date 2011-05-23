/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.simulationgui.client;



import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author glatard
 */
public class SimulationGUIMenuButton extends ToolStripMenuButton {

    public SimulationGUIMenuButton() {

        this.setTitle("Simulation editor");
        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(3);

       MenuItem launch = new MenuItem("New Simulation");
       launch.addClickHandler(new ClickHandler(){
                public void onClick(MenuItemClickEvent event) {
            Layout.getInstance().addTab(new SimulationGUITab());
            }
       });
       menu.setItems(launch);
       this.setMenu(menu);
    }



}
