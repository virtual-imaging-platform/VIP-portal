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
public class ValueRecord extends ListGridRecord {

    public ValueRecord(String value, Long parameterID) {

        setAttribute("value", value);
        setAttribute("parameterID", parameterID);

    }
}
