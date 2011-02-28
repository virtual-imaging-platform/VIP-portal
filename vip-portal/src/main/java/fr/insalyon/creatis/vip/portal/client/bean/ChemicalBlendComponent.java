/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class ChemicalBlendComponent implements IsSerializable {

    private String elementName;
    private double massRatio;

    public ChemicalBlendComponent() {
    }

    public ChemicalBlendComponent(String elementName, double massRatio) {
        this.elementName = elementName;
        this.massRatio = massRatio;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public double getMassRatio() {
        return massRatio;
    }

    public void setMassRatio(double massRatio) {
        this.massRatio = massRatio;
    }
}
