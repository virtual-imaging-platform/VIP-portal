/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulationgui.client;

import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;

/**
 *
 * @author moulin
 */
public class SimulationGUIInit {
    private static SimulationGUIInit instance;

    public static SimulationGUIInit getInstance() {
        if (instance == null) {
            instance = new SimulationGUIInit();
        }
        return instance;
    }
    
    private SimulationGUIInit(){
        MainToolStrip.getInstance().addMenuButton(new SimulationGUIMenuButton());
    }
}
