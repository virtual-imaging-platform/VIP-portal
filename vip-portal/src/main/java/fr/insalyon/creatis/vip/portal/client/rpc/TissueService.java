/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.portal.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
public interface TissueService extends RemoteService {
     public static final String SERVICE_URI = "/tissueservice";

    public static class Util {

        public static TissueServiceAsync getInstance() {

           TissueServiceAsync instance = (TissueServiceAsync) GWT.create(TissueService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }

    }

    //tissues
    public List<Tissue> getTissues();
    public void addTissue(Tissue t);
    public void updateTissue(Tissue t);
    public void deleteTissue(Tissue t);

    //physical properties
    public List<PhysicalProperty> getPhysicalProperties(Tissue t);
    public void updatePhysicalProperty(PhysicalProperty p);
    public void deletePhysicalProperty(PhysicalProperty p); //deletes all the properties associated with this property
    public int getNextPhysicalPropertyId();

    //echogenicity
    //set methods are used to insert the physical property. A new id is generated. Update methods assume that the PhysicalProperty has an id.
    public Echogenicity getEchogenicity(PhysicalProperty p);
    public void setEchogenicity(Tissue t, PhysicalProperty p, Echogenicity e);
    public void updateEchogenicity(PhysicalProperty p, Echogenicity e);
       //no delete: delete the physical property instead

    //chemical blend
    public ChemicalBlend getChemicalBlend(PhysicalProperty p);
    public void setChemicalBlend(Tissue t, PhysicalProperty p, ChemicalBlend c);
    public void updateChemicalBlend(PhysicalProperty p, ChemicalBlend c);

    //magnetic properties
    public List<MagneticProperty> getMagneticProperties(PhysicalProperty p);
    public void setMagneticProperties(Tissue t, PhysicalProperty p, List<MagneticProperty> mp);
    public void updateMagneticProperties(PhysicalProperty p, List<MagneticProperty> mp);

    public void addMagneticPropertyName(String name);
    public void deleteMagneticPropertyName(String name);
    public List<String> getMagneticPropertyNames();

    //distributions
    public List<Distribution> getDistributions();
    public void addDistribution(Distribution d);
    public void updateDistribution(Distribution d);
    public void deleteDistribution(Distribution d);

    //distribution instances
    public List<DistributionInstance> getDistributionInstances();
    public DistributionInstance getDistributionInstance(int instanceId);
    public void addDistributionInstance(DistributionInstance di);
    public void updateDistributionInstance(DistributionInstance di);
    public void deleteDistributionInstance(DistributionInstance di);
   
}
