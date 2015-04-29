/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client;

/**
 *
 * @author Nouha Boujelben
 */
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.n4u.client.view.N4uHomeParser;
/**
 * 
 * @author nouha
 */
public class N4uModule extends Module {

    @Override
    public void load() {
        CoreModule.addGeneralApplicationParser(new N4uHomeParser());
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate() {
    }
}
