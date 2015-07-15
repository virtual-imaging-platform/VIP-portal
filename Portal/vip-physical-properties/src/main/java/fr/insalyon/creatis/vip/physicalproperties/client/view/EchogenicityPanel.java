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
