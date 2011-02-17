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
public class DistributionParameterValue implements IsSerializable {

    double value;
    DistributionParameter param;

    public DistributionParameterValue() {
    }

    public DistributionParameterValue(DistributionParameter param, double value) {
        this.value = value;
        this.param = param;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public DistributionParameter getParam() {
        return param;
    }

    public void setParam(DistributionParameter param) {
        this.param = param;
    }
    
}

