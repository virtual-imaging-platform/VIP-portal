/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Boujelben
 */
import java.sql.Date;
public class QueryRecord extends ListGridRecord {

    public QueryRecord(String name,Date dateCreation,String version) {

        setAttribute("name", name);
        setAttribute("dateCreation", dateCreation);
        setAttribute("version", version);
      
       
        
    }
}
    

