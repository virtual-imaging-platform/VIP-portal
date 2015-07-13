/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.models.client.ParserLUT;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.HashMap;

/**
 *
 * @author amarion
 */
public class Distribution implements IsSerializable{
    private HashMap<String,Double> parameters;
    private String name;

    public Distribution() {
        this.parameters = new HashMap<String, Double>();
    }
 public void setName(String name) {
       this.name = name;
    }
    public HashMap<String, Double> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    
}
