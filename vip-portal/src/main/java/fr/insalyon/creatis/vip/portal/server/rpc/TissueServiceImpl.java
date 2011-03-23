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
import fr.insalyon.creatis.vip.portal.server.dao.DAOException;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import java.util.List;

/**
 *
 * @author glatard
 */
public class TissueServiceImpl extends RemoteServiceServlet implements TissueService {

    public List<Tissue> getTissues() {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getTissues();
        } catch (DAOException ex) {
            return null;
        }
    }

    public void addTissue(Tissue t) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().addTissue(t);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateTissue(Tissue t) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().updateTissue(t);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteTissue(Tissue t) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().deleteTissue(t);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public List<PhysicalProperty> getPhysicalProperties(Tissue t) {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getPhysicalProperties(t);
        } catch (DAOException ex) {
            return null;
        }
    }

//    public int addPhysicalProperty(Tissue t, PhysicalProperty p) {
//        return DAOFactory.getDAOFactory().getTissueDAO().addPhysicalProperty(t, p);
//    }

    public void updatePhysicalProperty(PhysicalProperty p) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().updatePhysicalProperty(p);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void deletePhysicalProperty(PhysicalProperty p) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().deletePhysicalProperty(p);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public int getNextPhysicalPropertyId() {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getNextPhysicalPropertyId();
        } catch (DAOException ex) {
            return -1;
        }
    }

    public Echogenicity getEchogenicity(PhysicalProperty p) {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getEchogenicity(p);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void setEchogenicity(Tissue t, PhysicalProperty p, Echogenicity e) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().setEchogenicity(t, p, e);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateEchogenicity(PhysicalProperty p, Echogenicity e) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().updateEchogenicity(p, e);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public ChemicalBlend getChemicalBlend(PhysicalProperty p) {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getChemicalBlend(p);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void setChemicalBlend(Tissue t,PhysicalProperty p, ChemicalBlend c) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().setChemicalBlend(t, p, c);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateChemicalBlend(PhysicalProperty p, ChemicalBlend c) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().updateChemicalBlend(p, c);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public List<MagneticProperty> getMagneticProperties(PhysicalProperty p) {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getMagneticProperties(p);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void setMagneticProperties(Tissue t, PhysicalProperty p, List<MagneticProperty> mp) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().setMagneticProperties(t, p, mp);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateMagneticProperties(PhysicalProperty p, List<MagneticProperty> mp) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().updateMagneticProperties(p, mp);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void addMagneticPropertyName(String name) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().addMagneticPropertyName(name);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteMagneticPropertyName(String name) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().deleteMagneticPropertyName(name);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getMagneticPropertyNames() {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getMagneticPropertyNames();
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<Distribution> getDistributions() {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getDistributions();
        } catch (DAOException ex) {
            return null;
        }
    }

    public void addDistribution(Distribution d) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().addDistribution(d);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateDistribution(Distribution d) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().updateDistribution(d);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteDistribution(Distribution d) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().deleteDistribution(d);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public List<DistributionInstance> getDistributionInstances() {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getDistributionInstances();
        } catch (DAOException ex) {
            return null;
        }
    }

    public DistributionInstance getDistributionInstance(int instanceId) {
        try {
            return DAOFactory.getDAOFactory().getTissueDAO().getDistributionInstance(instanceId);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void addDistributionInstance(DistributionInstance di) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().addDistributionInstance(di);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateDistributionInstance(DistributionInstance di) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().updateDistributionInstance(di);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteDistributionInstance(DistributionInstance di) {
        try {
            DAOFactory.getDAOFactory().getTissueDAO().deleteDistributionInstance(di);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }
}
