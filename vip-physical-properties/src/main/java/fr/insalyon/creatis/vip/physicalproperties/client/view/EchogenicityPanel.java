/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.physicalproperties.client.view;

import com.gwtext.client.widgets.Panel;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.Distribution;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.PhysicalProperty;

/**
 *
 * @author glatard
 */
class EchogenicityPanel extends Panel {

    private static EchogenicityPanel instance = null;
    private PhysicalProperty property;
    private SelectDistributionFieldSet mpAmp;
    private SelectDistributionFieldSet mpSpat;

    public PhysicalProperty getProperty() {
        return property;
    }

    public void setProperty(PhysicalProperty property) {
        this.property = property;
    }

    public static EchogenicityPanel getInstance() {
        if (instance == null) {
            instance = new EchogenicityPanel();
        }
        return instance;
    }

    private EchogenicityPanel() {
        super("Echogenicity");

        this.setWidth(450);
        this.setBorder(false);

        //Amplitude distribution
        mpAmp = new SelectDistributionFieldSet("Scatterer Amplitude");
        this.add(mpAmp);


        //Spatial distribution
        mpSpat = new SelectDistributionFieldSet("Scatterer Position");
        this.add(mpSpat);

    }

    public void clearParams() {
        mpAmp.clearParams();
        mpSpat.clearParams();
    }

    public String getAmplitudeDistName() {
        return mpAmp.getCurrentDist().getName();
    }

    public String getSpatialDistName() {
        return mpSpat.getCurrentDist().getName();
    }

    public Distribution getAmplitudeDistribution() {
        return mpAmp.getCurrentDist();
    }

    public void setAmplitudeDistributionInstance(DistributionInstance di) {
        mpAmp.setDistributionInstance(di);
    }

    public void setSpatialDistributionInstance(DistributionInstance di) {
        mpSpat.setDistributionInstance(di);
    }

    public Distribution getSpatialDistribution() {
        return mpSpat.getCurrentDist();
    }

    public DistributionInstance getAmplitudeDistributionInstance() {
        return mpAmp.getDistributionInstance();
    }

    public DistributionInstance getSpatialDistributionInstance() {
        return mpSpat.getDistributionInstance();
    }
}
