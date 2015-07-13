/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.simulatedata.client.view.SimulatedDataParser;

/**
 *
 * @author glatard
 */
public class SimulatedDataModule extends Module{

    @Override
    public void load() {
         CoreModule.addGeneralApplicationParser(new SimulatedDataParser());
    }

    @Override
    public void postLoading() {
   
    }

    @Override
    public void terminate() {
      
    }
    
}
