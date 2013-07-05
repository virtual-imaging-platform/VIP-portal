/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client;

/**
 *
 * @author Boujelben
 */
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.layout.CenterTabSet;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.main.GeneralTileGrid;
import fr.insalyon.creatis.vip.core.client.view.main.HomeTab;
import fr.insalyon.creatis.vip.core.client.view.main.SystemTileGrid;
import fr.insalyon.creatis.vip.query.client.view.QueryHomeParser;
import fr.insalyon.creatis.vip.query.client.view.QueryTitleGrid;
public class QueryModule extends Module {
 
   @Override
    public void load() {
        
        CoreModule.addGeneralApplicationParser(new QueryHomeParser());
        CoreModule.addApplicationsTileGrid(new QueryTitleGrid("My Queries"));
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate() {
    }
     

       
    
       
    
}
