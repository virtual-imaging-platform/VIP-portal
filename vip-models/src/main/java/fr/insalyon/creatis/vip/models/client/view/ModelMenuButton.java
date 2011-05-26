/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.models.client.view;


import fr.insalyon.creatis.vip.models.client.view.ModelImportTab;
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
public class ModelMenuButton extends ToolStripMenuButton{

    public ModelMenuButton() {
        this.setTitle("Models");
        Menu menu = new Menu();
        menu.setShowShadow(true);
        menu.setShadowDepth(3);

        MenuItem imp = new MenuItem("Import");
        imp.addClickHandler(new ClickHandler(){

            public void onClick(MenuItemClickEvent event) {
                ModelImportTab.getInstance().resetTab();
                Layout.getInstance().addTab(ModelImportTab.getInstance());
            }
        });

        MenuItem browse = new MenuItem("Browse");
        browse.addClickHandler(new ClickHandler(){

            public void onClick(MenuItemClickEvent event) {
                ModelBrowseTab.getInstance().resetTab();
                Layout.getInstance().addTab(ModelBrowseTab.getInstance());
            }
        });
        browse.setEnabled(true);
        
        MenuItem adam = new MenuItem("Adam");
        adam.setIcon("icon-adam.png");
        adam.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(ImportAdamTab.getInstance());
            }
        });

        menu.setItems(imp,browse,adam);
        this.setMenu(menu);
    }



}
