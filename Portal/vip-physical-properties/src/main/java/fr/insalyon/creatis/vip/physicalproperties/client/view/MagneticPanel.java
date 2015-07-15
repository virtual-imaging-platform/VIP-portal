/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
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
