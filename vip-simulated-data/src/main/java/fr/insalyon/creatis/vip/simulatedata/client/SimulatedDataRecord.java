/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author glatard
 */
public class SimulatedDataRecord extends ListGridRecord {
    
    public SimulatedDataRecord() {
    }
    
    public SimulatedDataRecord(String file, String type, String parameters, String model,String simulation) {
        
        setAttribute("icon", "icon-file");
        setAttribute("file", file);
        setAttribute("type", type);
        setAttribute("parameters", parameters);
        setAttribute("model",model);
        setAttribute("simulation",simulation);
        
        setAttribute("short-file",file.substring(file.lastIndexOf('/')+1));
        setAttribute("short-param",parameters.substring(parameters.lastIndexOf('/')+1));
        setAttribute("short-model",model.substring(model.lastIndexOf('/')+1));
        
        
    }
    
}
