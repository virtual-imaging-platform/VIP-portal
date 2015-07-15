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
package fr.insalyon.creatis.vip.models.client.ParserLUT;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author amarion
 */
public class PhysicalParameterLUT  implements IsSerializable{

   private String name;
    private ArrayList<Tissue> tissues;
      public PhysicalParameterLUT() {
        this.tissues = new ArrayList<Tissue>();
    }
  public void setName(String name) {
       this.name = name;
    }
    public String getName() {
        return name;
    }

    public ArrayList<Tissue> getTissues() {
        return tissues;
    }

    public Tissue lastAdd()
    {
        return tissues.get(tissues.size()-1);
    }
    public void print() {
        for (Tissue t : tissues) {
            System.out.println("Tissue: " + t.getName());
            for (GenericProperty gp : t.getProperties()) {
                System.out.println("\tProperty: " + gp.getName());
                if (!(gp.getBlendDensity() == null)) {
                    System.out.println("\tBlend Density: " + gp.getBlendDensity());
                }
                if (!(gp.getBlendPhase() == null)) {
                    System.out.println("\tBlend Phase: " + gp.getBlendPhase());
                }
                if (!(gp.getScatterersDensity() == null)) {
                    System.out.println("\tScatterers Density: " + gp.getScatterersDensity());
                }

                for (GenericParameter gpa : gp.getPhysParameters()) {
                    System.out.println("\t\tPhysical parameter: " + gpa.getName());
                    if (!(gpa.getDistrib() == null)) {
                        System.out.println("\t\t\tDistribution: " + gpa.getDistrib().getName());
                        for (Entry<String, Double> par : gpa.getDistrib().getParameters().entrySet()) {
                            System.out.println("\t\t\t Param: " + par.getKey() + " ; value: " + par.getValue().toString());
                        }
                    }
                }
            }
        }
    }

    public boolean hasParametersfor(String parameter)
    {
        boolean btest = false;
        for (Tissue t : tissues) {
            for (GenericProperty gp : t.getProperties()) {
                for (GenericParameter gpa : gp.getPhysParameters()) {
                    if(gpa.getName().equals(parameter) && !gpa.getDistrib().getParameters().isEmpty())
                    {
                        btest = true;
                        break;
                    }
                }
            }
        }
        return btest;
    }
    
    public   HashMap<String, GenericParameter> getPhysicalParameters(String parameter)
    {
        HashMap<String, GenericParameter> pp = new HashMap<String, GenericParameter>();
        for (Tissue t : tissues) {
            System.out.println("Tissue: " + t.getName());
            for (GenericProperty gp : t.getProperties()) {
                for (GenericParameter gpa : gp.getPhysParameters()) {
                    if(gpa.getName().equals(parameter))
                        pp.put(t.getName(), gpa);
                }
            }
        }
        return pp;
    }
  
}
