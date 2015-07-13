/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.client;

import fr.insalyon.creatis.vip.cardiac.client.view.CardiacParser;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;

/**
 *
 * @author glatard
 */
public class CardiacModule extends Module{

    @Override
    public void load() {
        CoreModule.addGeneralApplicationParser(new CardiacParser());
    }

    @Override
    public void postLoading() {
       
    }

    @Override
    public void terminate() { throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
