/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.query.client.view.monitor.QueryMakerTab;
import fr.insalyon.creatis.vip.query.client.view.monitor.QueryHistoryTab;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.query.client.view.monitor.QueryExplorerTab;

/**
 *
 * @author Boujelben
 */
public class QueryHomeParser extends ApplicationParser {

    @Override
    public void loadApplications() {
//
        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.hasGroupAccess(QueryConstants.QUERY_GROUP)) {
            addApplication(QueryConstants.APP_QUERYMAKER, QueryConstants.APP_IMG_QUERYMAKER);
            addApplication(QueryConstants.APP_QUERYHISTORY, QueryConstants.APP_IMG_QUERYHISTORY);
           // addApplication(QueryConstants.APP_QUERYEXPLORER, QueryConstants.APP_IMG_QUERYEXPLORER);
            CoreModule.addApplicationsTileGrid(new QueryTitleGrid());
            
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(QueryConstants.APP_QUERYMAKER)) {
            Layout.getInstance().addTab(new QueryMakerTab());
            return true;

        } else if (applicationName.equals(QueryConstants.APP_QUERYHISTORY)) {
            Layout.getInstance().addTab(new QueryHistoryTab());
           
            return true;
        }
         else if (applicationName.equals(QueryConstants.APP_QUERYEXPLORER)) {
            Layout.getInstance().addTab(new QueryExplorerTab());
           
            return true;
        }
        return false;
    }
}
