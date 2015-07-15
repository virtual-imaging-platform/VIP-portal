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
package fr.insalyon.creatis.vip.physicalproperties.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.ChemicalBlend;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.Distribution;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.Echogenicity;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.MagneticProperty;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.PhysicalProperty;
import fr.insalyon.creatis.vip.physicalproperties.client.bean.Tissue;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface TissueServiceAsync {

    public void getTissues(AsyncCallback<List<Tissue>> asyncCallback);

    public void addTissue(Tissue t, AsyncCallback<Void> asyncCallback);

    public void updateTissue(Tissue t, AsyncCallback<Void> asyncCallback);

    public void deleteTissue(Tissue t, AsyncCallback<Void> asyncCallback);

    public void getPhysicalProperties(Tissue t, AsyncCallback<List<PhysicalProperty>> asyncCallback);

    public void updatePhysicalProperty(PhysicalProperty p, AsyncCallback<Void> asyncCallback);

    public void deletePhysicalProperty(PhysicalProperty p, AsyncCallback<Void> asyncCallback);

    public void getNextPhysicalPropertyId(AsyncCallback<java.lang.Integer> asyncCallback);

    public void getEchogenicity(PhysicalProperty p, AsyncCallback<Echogenicity> asyncCallback);

    public void setEchogenicity(Tissue t, PhysicalProperty p, Echogenicity e, AsyncCallback<Void> asyncCallback);

    public void updateEchogenicity(PhysicalProperty p, Echogenicity e, AsyncCallback<Void> asyncCallback);

    public void getChemicalBlend(PhysicalProperty p, AsyncCallback<ChemicalBlend> asyncCallback);

    public void setChemicalBlend(Tissue t, PhysicalProperty p, ChemicalBlend c, AsyncCallback<Void> asyncCallback);

    public void updateChemicalBlend(PhysicalProperty p, ChemicalBlend c, AsyncCallback<Void> asyncCallback);

    public void getMagneticProperties(PhysicalProperty p, AsyncCallback<List<MagneticProperty>> asyncCallback);

    public void addMagneticPropertyName(String name, AsyncCallback<Void> asyncCallback);

    public void deleteMagneticPropertyName(String name, AsyncCallback<Void> asyncCallback);

    public void getMagneticPropertyNames(AsyncCallback<List<String>> asyncCallback);

    public void getDistributions(AsyncCallback<List<Distribution>> asyncCallback);

    public void addDistribution(Distribution d, AsyncCallback<Void> asyncCallback);

    public void deleteDistribution(Distribution d, AsyncCallback<Void> asyncCallback);

    public void getDistributionInstances(AsyncCallback<List<DistributionInstance>> asyncCallback);

    public void addDistributionInstance(DistributionInstance di, AsyncCallback<Void> asyncCallback);

    public void updateDistributionInstance(DistributionInstance di, AsyncCallback<Void> asyncCallback);

    public void deleteDistributionInstance(DistributionInstance di, AsyncCallback<Void> asyncCallback);

    public void updateDistribution(Distribution d, AsyncCallback<Void> asyncCallback);

    public void getDistributionInstance(int instanceId, AsyncCallback<DistributionInstance> asyncCallback);

    public void setMagneticProperties(Tissue t, PhysicalProperty p, List<MagneticProperty> mp, AsyncCallback<Void> asyncCallback);

    public void updateMagneticProperties(PhysicalProperty p, List<MagneticProperty> mp, AsyncCallback<Void> asyncCallback);

   // public void addPhysicalProperty(Tissue t, PhysicalProperty p, AsyncCallback<java.lang.Integer> asyncCallback);
  
}
