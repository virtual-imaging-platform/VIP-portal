/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataConstants;

/**
 *
 * @author glatard
 */
public class SimulatedDataParser extends ApplicationParser{

    @Override
    public void loadApplications() {
       if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.hasGroupAccess(SimulatedDataConstants.GROUP_VIP)
                || CoreModule.user.hasGroupAccess("Tutorial")) {

            addApplication(SimulatedDataConstants.APP_SD, SimulatedDataConstants.APP_IMG_SD);
        }
    }

    @Override
    public boolean parse(String applicationName) {
         if (applicationName.equals(SimulatedDataConstants.APP_SD)) {
            Layout.getInstance().addTab(new SimulatedDataTab());
            return true;
        }
        return false;
    }
    
}
