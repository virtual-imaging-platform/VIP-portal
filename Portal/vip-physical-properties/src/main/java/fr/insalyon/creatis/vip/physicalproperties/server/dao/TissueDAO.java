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
package fr.insalyon.creatis.vip.physicalproperties.server.dao;

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
public interface TissueDAO {

    //tissues
    public List<Tissue> getTissues();
    public void addTissue(Tissue t);
    public void updateTissue(Tissue t);
    public void deleteTissue(Tissue t);

    //physical properties
    public List<PhysicalProperty> getPhysicalProperties(Tissue t);
    //public int addPhysicalProperty(Tissue t, PhysicalProperty p);
    public void updatePhysicalProperty(PhysicalProperty p);
    public void deletePhysicalProperty(PhysicalProperty p); //deletes all the properties associated with this property
    public int getNextPhysicalPropertyId();

    //echogenicity
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
