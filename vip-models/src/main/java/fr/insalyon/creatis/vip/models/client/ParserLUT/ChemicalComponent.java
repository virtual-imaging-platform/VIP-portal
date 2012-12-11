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
public class ChemicalComponent implements IsSerializable{
    private String name;
    private String ratio;

    public ChemicalComponent()
    {
      
    }
 public void setName(String name) {
       this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getRatio() {
        return ratio;
    }

   

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    
}
