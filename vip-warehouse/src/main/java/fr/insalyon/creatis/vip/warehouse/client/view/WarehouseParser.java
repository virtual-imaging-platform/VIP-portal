/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.warehouse.client.WarehouseConstants;

/**
 *
 * @author cervenansky
 */
public class WarehouseParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        
            addApplication(WarehouseConstants.APP_WAREHOUSE, WarehouseConstants.APP_IMG_WAREHOUSE);
        
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(WarehouseConstants.APP_WAREHOUSE)) {
            Layout.getInstance().addTab(new WarehouseListTab());
            return true;
        }
        return false;
    }
}
