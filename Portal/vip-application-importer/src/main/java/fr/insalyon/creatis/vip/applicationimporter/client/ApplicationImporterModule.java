/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client;
        
/**
 *
 * @author Nouha Boujelben
 */
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.applicationimporter.client.view.ApplicationImporterHomeParser;
/**
 * 
 * @author Nouha Boujelben
 */
public class ApplicationImporterModule extends Module {

    @Override
    public void load() {
        CoreModule.addGeneralApplicationParser(new ApplicationImporterHomeParser());
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate() {
    }
}
