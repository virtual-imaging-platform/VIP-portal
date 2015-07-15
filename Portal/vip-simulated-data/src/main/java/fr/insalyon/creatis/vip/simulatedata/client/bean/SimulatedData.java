/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
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
    private List <SemEntity> files;
    private List<SemEntity> parameters;
    private List<SemEntity> models;
    private String simulation;
    private String name;
    
    private Date date;

    public SimulatedData() {
        files = new ArrayList<SemEntity>();
        parameters = new ArrayList<SemEntity>();
        models = new ArrayList<SemEntity>();
    }

    public SimulatedData(String id, Modality modality, String simulation) {
        this.id = id;
        this.modality = modality;
        
        this.simulation = simulation;
        files = new ArrayList<SemEntity>();
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

    public List<SemEntity> getParameters() {
        return parameters;
    }

    public List<SemEntity> getModels() {
        return models;
    }
    
    public List<SemEntity> getFiles() {
        return files;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date data) {
        this.date = data;
    }
    
     public String getName() {
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        String sout =
                "SimulatedData{"
                + "\n\tmodality=" + modality + ", "
                + "\n\tparameters= ";
        for (Iterator<SemEntity> it = parameters.iterator(); it.hasNext();) {
            sout += it.next() + " | ";
        }
        sout += "\n\tmodel=";
        for (Iterator<SemEntity> it = models.iterator(); it.hasNext();) {
            sout += it.next() + " | ";
        }
        sout += "\n\tfiles=";
        for (Iterator<SemEntity> it = files.iterator(); it.hasNext();) {
            sout += it.next() + " | ";
        }
        sout += "\n}";

        return sout;
    }
}
