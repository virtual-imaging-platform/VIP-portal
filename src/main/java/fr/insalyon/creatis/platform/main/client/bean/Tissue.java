/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.platform.main.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class Tissue implements IsSerializable {
   
    //tissue name 
    private String tissueName;
    private int ontologyId;

    public Tissue(){
        tissueName="void";
    }

    public Tissue(String tissueName) {
        this.tissueName = tissueName;
        this.ontologyId = -1; //not supported yet
    }

    public String getTissueName() {
        return tissueName;
    }

    public void setTissueName(String tissueName) {
        this.tissueName = tissueName;
    }

    public int getOntologyId() {
        return ontologyId;
    }

    public void setOntologyId(int ontologyId) {
        this.ontologyId = ontologyId;
    }
  
}
