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
        imp.setIcon("icon-add.png");
        imp.addClickHandler(new ClickHandler(){

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelImportTab());
            }
        });

        MenuItem browse = new MenuItem("List");
        browse.setIcon("icon-list.png");
        browse.addClickHandler(new ClickHandler(){

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelBrowseTab());
            }
        });
        browse.setEnabled(true);
        
        menu.setItems(imp,browse);
        this.setMenu(menu);
    }



}
