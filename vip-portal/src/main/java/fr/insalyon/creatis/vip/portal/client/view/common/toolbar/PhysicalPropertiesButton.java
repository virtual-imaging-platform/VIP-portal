/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.client.view.common.toolbar;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.vip.portal.client.view.physicalParameters.TissuePanel;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import fr.insalyon.creatis.vip.portal.client.view.physicalParameters.DistributionsCenterPanel;

/**
 *
 * @author glatard
 */
class PhysicalPropertiesButton extends ToolbarButton {

    public PhysicalPropertiesButton(String title) {
        super(title);

        Menu tissueMenu = new Menu();
        tissueMenu.setShadow(true);
        tissueMenu.setMinWidth(10);

        Item manageTissuesItem = new Item("Tissues", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                Layout structure = Layout.getInstance();
                structure.setLeftPanel(TissuePanel.getInstance());
            }
        });
        manageTissuesItem.setIcon("");

        Item manageDistributionsItem = new Item("Distributions", new BaseItemListenerAdapter(){
            @Override
            public void onClick(BaseItem item, EventObject e){
                Layout.getInstance().setCenterPanel(DistributionsCenterPanel.getInstance());
            }

        });

        tissueMenu.addItem(manageTissuesItem);
        tissueMenu.addItem(manageDistributionsItem);

        this.setMenu(tissueMenu);
    }
}
