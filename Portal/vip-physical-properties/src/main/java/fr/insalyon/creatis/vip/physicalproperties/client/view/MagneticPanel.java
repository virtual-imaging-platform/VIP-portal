/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.physicalproperties.client.view;

import com.gwtext.client.widgets.Panel;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.PhysicalProperty;

/**
 *
 * @author glatard
 */
class MagneticPanel extends Panel {

    private static MagneticPanel instance = null;
    private PhysicalProperty property;
    private SelectDistributionFieldSet mpRho;
    private SelectDistributionFieldSet mpT1;
    private SelectDistributionFieldSet mpT2;
    private SelectDistributionFieldSet mpKhi;

    public static MagneticPanel getInstance() {
        if (instance == null) {
            instance = new MagneticPanel();
        }
        return instance;
    }

    public PhysicalProperty getProperty() {
        return property;
    }

    public void setProperty(PhysicalProperty property) {
        this.property = property;
    }

    private MagneticPanel() {
        super("Magnetic Properties");

        this.setWidth(450);
        this.setBorder(false);

        //Rho
        mpRho = new SelectDistributionFieldSet("Proton Density");
        this.add(mpRho);

        //T1
        mpT1 = new SelectDistributionFieldSet("T1");
        this.add(mpT1);

        //T2
        mpT2 = new SelectDistributionFieldSet("T2");
        this.add(mpT2);

        //Khi
        mpKhi = new SelectDistributionFieldSet("Susceptibility");
        this.add(mpKhi);

    }

    public void clearParams() {
        mpRho.clearParams();
        mpT1.clearParams();
        mpT2.clearParams();
        mpKhi.clearParams();
    }

    public String getRhoDistName() {
        return mpRho.getCurrentDist().getName();
    }

    public String getT1DistName() {
        return mpT1.getCurrentDist().getName();
    }

    public String getT2DistName() {
        return mpT2.getCurrentDist().getName();
    }

    public String getKhiDistName() {
        return mpKhi.getCurrentDist().getName();
    }

    public DistributionInstance getRhoDistributionInstance() {
        return mpRho.getDistributionInstance();
    }

    public DistributionInstance getT1DistributionInstance() {
        return mpT1.getDistributionInstance();
    }

    public DistributionInstance getT2DistributionInstance() {
        return mpT2.getDistributionInstance();
    }

    public DistributionInstance getKhiDistributionInstance() {
        return mpKhi.getDistributionInstance();
    }

    public void setRhoDistributionInstance(DistributionInstance di) {
        mpRho.setDistributionInstance(di);
    }

    public void setT1DistributionInstance(DistributionInstance di) {
        mpT1.setDistributionInstance(di);
    }

    public void setT2DistributionInstance(DistributionInstance di) {
        mpT2.setDistributionInstance(di);
    }

    public void setKhiDistributionInstance(DistributionInstance di) {
        mpKhi.setDistributionInstance(di);
    }
}
