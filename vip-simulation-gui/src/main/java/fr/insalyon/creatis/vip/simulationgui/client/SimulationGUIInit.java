
package fr.insalyon.creatis.vip.simulationgui.client;

import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.simulationgui.client.gui.SimulationGUIMenuButton;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;

/**
 *
 * @author moulin
 */
public class SimulationGUIInit{
    private static SimulationGUIInit instance;

    public static SimulationGUIInit getInstance() {
        if (instance == null) {
            instance = new SimulationGUIInit();
        }
        return instance;
    }
    
    private SimulationGUIInit(){
      if (Context.getInstance().hasGroupAccess(new String[]{"Administrator", "VIP"})) {
        MainToolStrip.getInstance().addMenuButton(new SimulationGUIMenuButton());
       }
    }
}
