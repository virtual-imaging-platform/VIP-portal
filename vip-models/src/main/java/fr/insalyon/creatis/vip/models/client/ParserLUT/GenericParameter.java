/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.ParserLUT;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author amarion
 */
public class GenericParameter implements IsSerializable{

    private String name;
    private Distribution distrib;
    private ChemicalComponent comp;

    
    public GenericParameter() {
        this.distrib = null;
    }
 public void setName(String name) {
       this.name = name;
    }
    
    public Distribution getDistrib() {
        return distrib;
    }

    public String getName() {
        return name;
    }

    public ChemicalComponent getComp() {
        return comp;
    }

    public void setDistrib(Distribution distrib) {
        this.distrib = distrib;
    }



    public void setComp(ChemicalComponent comp) {
        this.comp = comp;
    }
}
