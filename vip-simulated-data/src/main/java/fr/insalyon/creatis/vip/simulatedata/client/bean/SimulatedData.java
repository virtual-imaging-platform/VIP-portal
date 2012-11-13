/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 *
 * @author glatard
 */
public class SimulatedData implements IsSerializable {

   

    public enum Modality {

        PET, US, CT, MRI
    };
    private Modality modality;
    private String file;
    private String type;
    private String parameters;
    private String model;
    private String simulation;
    private Date date;
    
    public SimulatedData() {
    }

    public SimulatedData(Modality modality, String file, String type, String parameters, String model, String simulation) {
        this.modality = modality;
        this.file = file;
        this.type = type;
        this.parameters = parameters;
        this.model = model;
        this.simulation = simulation;
    }

    public static Modality parseModality(String m) {
        if (m.equals("PET")) {
            return Modality.PET;
        }
        if (m.equals("US") || m.equals("Ultrasound")) {
            return Modality.US;
        }
        if (m.equals("CT")) {
            return Modality.CT;
        }
        if (m.equals("MRI")) {
            return Modality.MRI;
        }
        return null;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate(){
         return date;
    }
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getSimulation() {
        return simulation;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
