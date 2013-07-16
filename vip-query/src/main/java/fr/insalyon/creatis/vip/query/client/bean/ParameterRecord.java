/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.bean;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Boujelben
 */
public class ParameterRecord extends ListGridRecord{
     public ParameterRecord(String name,String type,String description,String example ) {

        setAttribute("name", name);
        setAttribute("type", type);
        setAttribute("description", description);
        setAttribute("example", example);
       
        
    }
    
}




