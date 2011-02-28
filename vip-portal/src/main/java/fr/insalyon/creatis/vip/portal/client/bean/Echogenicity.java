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
public class Echogenicity implements IsSerializable {
    private DistributionInstance spatialDistribution;
    private DistributionInstance amplitudeDistribution;

    public Echogenicity() {
    }

    public Echogenicity(DistributionInstance spatialDistribution, DistributionInstance amplitudeDistribution) {
        this.spatialDistribution = spatialDistribution;
        this.amplitudeDistribution = amplitudeDistribution;
    }

    public DistributionInstance getAmplitudeDistribution() {
        return amplitudeDistribution;
    }

    public void setAmplitudeDistribution(DistributionInstance amplitudeDistribution) {
        this.amplitudeDistribution = amplitudeDistribution;
    }

    public DistributionInstance getSpatialDistribution() {
        return spatialDistribution;
    }

    public void setSpatialDistribution(DistributionInstance spatialDistribution) {
        this.spatialDistribution = spatialDistribution;
    }

   


}
