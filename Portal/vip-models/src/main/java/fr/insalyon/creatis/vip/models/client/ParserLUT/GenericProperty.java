package fr.insalyon.creatis.vip.models.client.ParserLUT;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author amarion
 */
public class GenericProperty implements IsSerializable{
    private String name;
    private ArrayList<GenericParameter> physParameters;
    private Double blendDensity;
    private String blendPhase;
    private Double scatterersDensity;

    public GenericProperty(){
        this.physParameters = new ArrayList<GenericParameter>();
    }
 public void setName(String name) {
       this.name = name;
    }
    
    public ArrayList<GenericParameter> getPhysParameters() {
        return physParameters;
    }

    public String getName() {
        return name;
    }

    public Double getBlendDensity() {
        return blendDensity;
    }

    public Double getScatterersDensity() {
        return scatterersDensity;
    }

    public String getBlendPhase() {
        return blendPhase;
    }
    
    public void setBlendDensity(Double blendDensity) {
        this.blendDensity = blendDensity;
    }

    public void setBlendPhase(String blendPhase) {
        this.blendPhase = blendPhase;
    }

    public void setScatterersDensity(Double scatterersDensity) {
        this.scatterersDensity = scatterersDensity;
    }
    
    public GenericParameter lastAdd()
    {
        return physParameters.get(physParameters.size()-1);
    }
}
