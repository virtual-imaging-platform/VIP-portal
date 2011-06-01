/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.models.client.view;


import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;


/**
 *
 * @author glatard
 */
public class ModelButton extends ToolStripButton{

    public ModelButton() {
        this.setTitle("Models");
       this.addClickHandler(new ClickHandler() {
             public void onClick(ClickEvent event) {
                  Layout.getInstance().addTab(new ModelListTab());
            }
        }); 
        
//        Menu menu = new Menu();
//        menu.setShowShadow(true);
//        menu.setShadowDepth(3);
//
//        MenuItem imp = new MenuItem("Upload");
//        imp.setIcon("icon-add.png");
//        imp.addClickHandler(new ClickHandler(){
//
//            public void onClick(MenuItemClickEvent event) {
//                Layout.getInstance().addTab(new ModelImportTab());
//            }
//        });
//
//        MenuItem browse = new MenuItem("List");
//        browse.setIcon("icon-list.png");
//        browse.addClickHandler(new ClickHandler(){
//
//            public void onClick(MenuItemClickEvent event) {
//                Layout.getInstance().addTab(new ModelListTab());
//            }
//        });
//        browse.setEnabled(true);
//        
//        menu.setItems(imp,browse);
//        this.setMenu(menu);
    }



}
