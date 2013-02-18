/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cowork.client;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.cowork.client.view.CoworkParser;

/**
 *
 * @author glatard
 */
public class CoworkModule extends Module {

    @Override
    public void load() {
        CoreModule.addGeneralApplicationParser(new CoworkParser());
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate() {
    }
    
}
