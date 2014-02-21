/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author nouha
 */



import fr.insalyon.creatis.vip.core.client.CoreModule;

import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;


/**
 *
 * @author Nouha Boujelben
 */
public class N4uHomeParser extends ApplicationParser {

    @Override
    public void loadApplications() {

       /* if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.hasGroupAccess(QueryConstants.QUERY_GROUP)) {**/
            addApplication(N4uConstants.APP_N4UCONVERTER, N4uConstants.APP_IMG_N4UCONVERTER);
            
            //CoreModule.addApplicationsTileGrid(new QueryTitleGrid());

        //}
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(N4uConstants.APP_N4UCONVERTER)) {
            Layout.getInstance().addTab(new N4UConverterTab());
            return true;}

       /** } else if (applicationName.equals(QueryConstants.APP_QUERYHISTORY)) {
            Layout.getInstance().addTab(new QueryHistoryTab());

            return true;
        } else if (applicationName.equals(QueryConstants.APP_QUERYEXPLORER)) {
            Layout.getInstance().addTab(new QueryExplorerTab());

            return true;
        }
        * **/
       
        return false;
    }
}

