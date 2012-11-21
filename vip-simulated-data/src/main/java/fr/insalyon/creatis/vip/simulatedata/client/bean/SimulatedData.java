/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author glatard
 */
//public class SimulatedData implements IsSerializable {
public class SimulatedData implements IsSerializable {

 

    public enum Modality {

        PET, US, CT, MRI
    };
    
    private Modality modality;
    private String id;
    private String file;
    private String type;
    private List<SemEntity> parameters;
    private List<SemEntity> models;
    private String simulation;
    
    private Date date;

    public SimulatedData() {
        parameters = new ArrayList<SemEntity>();
        models = new ArrayList<SemEntity>();
    }

    public SimulatedData(String id, Modality modality, String file, String type, String simulation) {
        this.id = id;
        this.modality = modality;
        this.file = file;
        this.type = type;
        this.simulation = simulation;
        parameters = new ArrayList<SemEntity>();
        models = new ArrayList<SemEntity>();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<SemEntity> getParameters() {
        return parameters;
    }

    public List<SemEntity> getModels() {
        return models;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date data) {
        this.date = data;
    }

    @Override
    public String toString() {
        String sout =
                "SimulatedData{"
                + "\n\tmodality=" + modality + ", "
                + "\n\tfile=" + file + ", "
                + "\n\ttype=" + type + ", "
                + "\n\tparameters= ";
        for (Iterator<SemEntity> it = parameters.iterator(); it.hasNext();) {
            sout += it.next() + " | ";
        }
        sout += "\n\tmodel=";
        for (Iterator<SemEntity> it = models.iterator(); it.hasNext();) {
            sout += it.next() + " | ";
        }
        sout += "\n}";

        return sout;
    }
}
