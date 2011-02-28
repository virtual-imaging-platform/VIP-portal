/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.List;

/**
 *
 * @author glatard
 */
public class ChemicalBlend implements IsSerializable {

    private double density;
    private String phase; //solid, liquid, gas
    private List<ChemicalBlendComponent> components;

    public ChemicalBlend() {
    }

    public ChemicalBlend(double density, String phase, List<ChemicalBlendComponent> components) {
        this.density = density;
        this.phase = phase;
        this.components = components;
    }

    public List<ChemicalBlendComponent> getComponents() {
        return components;
    }

    public void setComponents(List<ChemicalBlendComponent> components) {
        this.components = components;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
