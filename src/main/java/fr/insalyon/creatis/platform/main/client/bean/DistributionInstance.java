/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.platform.main.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author glatard
 */
public class DistributionInstance implements IsSerializable {

    private int  id;
    private Distribution distributionType;
    private List<DistributionParameterValue> values;

    public DistributionInstance() {
        values = new ArrayList<DistributionParameterValue>();
    }

    public DistributionInstance(int id, Distribution distributionType, List <DistributionParameterValue> values) {
        this.distributionType = distributionType;
        this.values = values;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Distribution getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(Distribution distributionType) {
        this.distributionType = distributionType;
    }

    public List <DistributionParameterValue> getValues() {
        return values;
    }

    public void setValues(List <DistributionParameterValue> values) {
        this.values = values;
    }
}
