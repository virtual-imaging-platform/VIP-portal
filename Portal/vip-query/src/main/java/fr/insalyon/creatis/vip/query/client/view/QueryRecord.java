/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Nouha Boujelben
 */


public class QueryRecord extends ListGridRecord {

    public QueryRecord(String name, String dateCreation, String version, String queryversionID,String queryID) {

        setAttribute("name", name);
        setAttribute("dateCreation", dateCreation);
        setAttribute("version", "v." + version);
        setAttribute("queryversionID", queryversionID);
        
        setAttribute("queryID", queryID);


    }
}
