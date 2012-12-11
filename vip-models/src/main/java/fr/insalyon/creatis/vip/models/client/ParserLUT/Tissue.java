/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.models.client.ParserLUT;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author amarion
 */
public class Tissue implements IsSerializable{
    private String name;
    private  ArrayList<GenericProperty> properties;
  
    public Tissue() {
        this.properties = new ArrayList<GenericProperty>();
    }
    public void setName(String name) {
       this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public ArrayList<GenericProperty> getProperties() {
        return properties;
    }
    
    public GenericProperty lastAdd()
    {
        return properties.get(properties.size()-1);
    }
}
