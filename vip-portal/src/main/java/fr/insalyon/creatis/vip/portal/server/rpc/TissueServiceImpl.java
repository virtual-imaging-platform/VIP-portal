/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.portal.client.bean.ChemicalBlend;
import fr.insalyon.creatis.vip.portal.client.bean.Distribution;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.portal.client.bean.Echogenicity;
import fr.insalyon.creatis.vip.portal.client.bean.MagneticProperty;
import fr.insalyon.creatis.vip.portal.client.bean.PhysicalProperty;
import fr.insalyon.creatis.vip.portal.client.bean.Tissue;
import fr.insalyon.creatis.vip.portal.client.rpc.TissueService;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import java.util.List;

/**
 *
 * @author glatard
 */
public class TissueServiceImpl extends RemoteServiceServlet implements TissueService {

    public List<Tissue> getTissues() {
        return DAOFactory.getDAOFactory().getTissueDAO().getTissues();
    }

    public void addTissue(Tissue t) {
        DAOFactory.getDAOFactory().getTissueDAO().addTissue(t);
    }

    public void updateTissue(Tissue t) {
        DAOFactory.getDAOFactory().getTissueDAO().updateTissue(t);
    }

    public void deleteTissue(Tissue t) {
        DAOFactory.getDAOFactory().getTissueDAO().deleteTissue(t);
    }

    public List<PhysicalProperty> getPhysicalProperties(Tissue t) {
        return DAOFactory.getDAOFactory().getTissueDAO().getPhysicalProperties(t);
    }

//    public int addPhysicalProperty(Tissue t, PhysicalProperty p) {
//        return DAOFactory.getDAOFactory().getTissueDAO().addPhysicalProperty(t, p);
//    }

    public void updatePhysicalProperty(PhysicalProperty p) {
        DAOFactory.getDAOFactory().getTissueDAO().updatePhysicalProperty(p);
    }

    public void deletePhysicalProperty(PhysicalProperty p) {
        DAOFactory.getDAOFactory().getTissueDAO().deletePhysicalProperty(p);
    }

    public int getNextPhysicalPropertyId() {
        return DAOFactory.getDAOFactory().getTissueDAO().getNextPhysicalPropertyId();
    }

    public Echogenicity getEchogenicity(PhysicalProperty p) {
        return DAOFactory.getDAOFactory().getTissueDAO().getEchogenicity(p);
    }

    public void setEchogenicity(Tissue t, PhysicalProperty p, Echogenicity e) {
        DAOFactory.getDAOFactory().getTissueDAO().setEchogenicity(t, p, e);
    }

    public void updateEchogenicity(PhysicalProperty p, Echogenicity e) {
        DAOFactory.getDAOFactory().getTissueDAO().updateEchogenicity(p, e);
    }

    public ChemicalBlend getChemicalBlend(PhysicalProperty p) {
        return DAOFactory.getDAOFactory().getTissueDAO().getChemicalBlend(p);
    }

    public void setChemicalBlend(Tissue t,PhysicalProperty p, ChemicalBlend c) {
        DAOFactory.getDAOFactory().getTissueDAO().setChemicalBlend(t,p, c);
    }

    public void updateChemicalBlend(PhysicalProperty p, ChemicalBlend c) {
        DAOFactory.getDAOFactory().getTissueDAO().updateChemicalBlend(p, c);
    }

    public List<MagneticProperty> getMagneticProperties(PhysicalProperty p) {
        return DAOFactory.getDAOFactory().getTissueDAO().getMagneticProperties(p);
    }

    public void setMagneticProperties(Tissue t, PhysicalProperty p, List<MagneticProperty> mp) {
        DAOFactory.getDAOFactory().getTissueDAO().setMagneticProperties(t,p, mp);
    }

    public void updateMagneticProperties(PhysicalProperty p, List<MagneticProperty> mp) {
        DAOFactory.getDAOFactory().getTissueDAO().updateMagneticProperties(p, mp);
    }

    public void addMagneticPropertyName(String name) {
        DAOFactory.getDAOFactory().getTissueDAO().addMagneticPropertyName(name);
    }

    public void deleteMagneticPropertyName(String name) {
        DAOFactory.getDAOFactory().getTissueDAO().deleteMagneticPropertyName(name);
    }

    public List<String> getMagneticPropertyNames() {
        return DAOFactory.getDAOFactory().getTissueDAO().getMagneticPropertyNames();
    }

    public List<Distribution> getDistributions() {
        return DAOFactory.getDAOFactory().getTissueDAO().getDistributions();
    }

    public void addDistribution(Distribution d) {
        DAOFactory.getDAOFactory().getTissueDAO().addDistribution(d);
    }

    public void updateDistribution(Distribution d) {
        DAOFactory.getDAOFactory().getTissueDAO().updateDistribution(d);
    }

    public void deleteDistribution(Distribution d) {
        DAOFactory.getDAOFactory().getTissueDAO().deleteDistribution(d);
    }

    public List<DistributionInstance> getDistributionInstances() {
        return DAOFactory.getDAOFactory().getTissueDAO().getDistributionInstances();
    }

    public DistributionInstance getDistributionInstance(int instanceId) {
        return DAOFactory.getDAOFactory().getTissueDAO().getDistributionInstance(instanceId);
    }

    public void addDistributionInstance(DistributionInstance di) {
        DAOFactory.getDAOFactory().getTissueDAO().addDistributionInstance(di);
    }

    public void updateDistributionInstance(DistributionInstance di) {
        DAOFactory.getDAOFactory().getTissueDAO().updateDistributionInstance(di);
    }

    public void deleteDistributionInstance(DistributionInstance di) {
        DAOFactory.getDAOFactory().getTissueDAO().deleteDistributionInstance(di);
    }
}
