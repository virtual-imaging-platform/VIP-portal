/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.client.view;

import fr.insalyon.creatis.vip.cardiac.client.CardiacConstants;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author glatard
 */
public class CardiacParser extends ApplicationParser {

    @Override
    public void loadApplications() {
        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.hasGroupAccess(CardiacConstants.CARDIAC_GROUP)) {
            
            addApplication(CardiacConstants.APP_SD, CardiacConstants.APP_IMG_SD);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {
         if (applicationName.equals(CardiacConstants.APP_SD)) {
           
              Layout.getInstance().addTab(new CardiacTab());
            return true;
        }
        return false;
    }
    
}
