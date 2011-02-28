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
public class MagneticProperty implements IsSerializable {

    private String propertyName;//e.g. T1, T2
    private DistributionInstance distributionInstance;

    public MagneticProperty() {
    }

    public MagneticProperty(String propertyName, DistributionInstance distributionInstance) {
        this.propertyName = propertyName;
        this.distributionInstance = distributionInstance;
    }

    public DistributionInstance getDistributionInstance() {
        return distributionInstance;
    }

    public void setDistributionInstance(DistributionInstance distributionInstance) {
        this.distributionInstance = distributionInstance;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
