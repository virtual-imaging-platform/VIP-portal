/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
