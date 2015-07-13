/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author glatard
 */
public class Simulation implements IsSerializable {
    String files;
    String description;
    String name;
    String modalities;
    String simulationID;
    
    public Simulation(){
       
    }
    
    public Simulation(String name, String description, String files, String modalities, String simulationID){
        this();
        this.name = name;
        this.description = description;
        this.files = files;
        this.modalities = modalities;
        this.simulationID = simulationID;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public List<String> getFiles(){
        ArrayList<String> f = new ArrayList<String>();
        for (String s : files.split("\\s+"))
            f.add(s);
        return f;
    }

       public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFiles(String files) {
        this.files = files;
    }
    
    public String getFilesAsString(){
        return files;
    }

    public String getModalities() {
        return modalities;
    }

    public String getSimulationID() {
        return simulationID;
    }

    public void setSimulationID(String simulationID) {
        this.simulationID = simulationID;
    }
    
    
       
}
