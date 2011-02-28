/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.portal.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.portal.client.bean.ChemicalBlend;
import fr.insalyon.creatis.vip.portal.client.bean.Distribution;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.portal.client.bean.Echogenicity;
import fr.insalyon.creatis.vip.portal.client.bean.MagneticProperty;
import fr.insalyon.creatis.vip.portal.client.bean.PhysicalProperty;
import fr.insalyon.creatis.vip.portal.client.bean.Tissue;
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
