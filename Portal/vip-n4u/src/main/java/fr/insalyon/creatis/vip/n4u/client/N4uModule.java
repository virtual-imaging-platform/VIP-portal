/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client;

/**
 *
 * @author Nouha Boujelben
 */
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.query.client.view.QueryHomeParser;
import fr.insalyon.creatis.vip.query.client.view.QueryTitleGrid;

public class QueryModule extends Module {

    @Override
    public void load() {

        CoreModule.addGeneralApplicationParser(new QueryHomeParser());
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate() {
    }
}
