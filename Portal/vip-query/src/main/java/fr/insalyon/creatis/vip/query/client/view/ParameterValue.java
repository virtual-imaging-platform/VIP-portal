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
public class ParameterValue extends ListGridRecord {

    public ParameterValue(String name, String value) {
        
        setAttribute("name", name);
        setAttribute("value", value);

    }
}
