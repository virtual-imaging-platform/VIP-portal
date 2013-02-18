/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cowork.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.cowork.client.CoworkConstants;

/**
 *
 * @author glatard
 */
public class CoworkParser extends ApplicationParser {

    @Override
    public void loadApplications() {
        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.hasGroupAccess(CoworkConstants.COWORK_GROUP)) {

            addApplication(CoworkConstants.APP_SD, CoworkConstants.APP_IMG_SD);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {
         if (applicationName.equals(CoworkConstants.APP_SD)) {
           CoworkWindow window = new CoworkWindow();
           window.show();
            return true;
        }
        return false;
    }
    
}
