/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.sql.Date;

/**
 *
 * @author Boujelben
 */
public class QueryVersionRecord extends ListGridRecord {
    public QueryVersionRecord(String queryName,String version,Date dateCreation){
     setAttribute("name", queryName);
     setAttribute("version", version);
     setAttribute("name", dateCreation);
       
    }
    
}
