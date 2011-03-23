/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.portal.server.dao.derby;

import fr.insalyon.creatis.vip.portal.client.bean.ChemicalBlend;
import fr.insalyon.creatis.vip.portal.client.bean.ChemicalBlendComponent;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionParameter;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionParameterValue;
import fr.insalyon.creatis.vip.portal.client.bean.Distribution;
import fr.insalyon.creatis.vip.portal.client.bean.DistributionInstance;
import fr.insalyon.creatis.vip.portal.client.bean.Echogenicity;
import fr.insalyon.creatis.vip.portal.client.bean.MagneticProperty;
import fr.insalyon.creatis.vip.portal.client.bean.PhysicalProperty;
import fr.insalyon.creatis.vip.portal.client.bean.Tissue;
import fr.insalyon.creatis.vip.portal.server.dao.DAOException;
import fr.insalyon.creatis.vip.portal.server.dao.TissueDAO;
import fr.insalyon.creatis.vip.portal.server.dao.derby.connection.PlatformConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author glatard
 */
public class TissueData implements TissueDAO {

    private static TissueData instance;
    private Connection connection;

    public TissueData() throws DAOException {
        this.connection = PlatformConnection.getInstance().getConnection();
    }

//tissues
    public List<Tissue> getTissues() {
        
        Vector<Tissue> t = new Vector<Tissue>();
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("SELECT name from Tissues");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                Tissue u = new Tissue(rs.getString("name"));
                t.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }

    public void addTissue(Tissue t) {
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("INSERT INTO Tissues VALUES(?,?)");
            stat.setString(1, t.getTissueName());
            stat.setInt(2, t.getOntologyId());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateTissue(Tissue t) {
        //nothing to do yet ; name is the only field
    }

    public void deleteTissue(Tissue t) {
        PreparedStatement stat;
        try {
            stat = connection.prepareCall("DELETE FROM Tissues WHERE name=?");
            stat.setString(1, t.getTissueName());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //physical properties
    public List<PhysicalProperty> getPhysicalProperties(Tissue t) {
        PreparedStatement stat;
        Vector<PhysicalProperty> result = new Vector<PhysicalProperty>();
        try {
            System.out.println("SELECT physicalPropertyId,author,comment,type,date from PhysicalProperties where tissuename="+t.getTissueName());
            stat = connection.prepareStatement("SELECT physicalPropertyId,author,comment,type,date from PhysicalProperties where tissuename=?");
            stat.setString(1, t.getTissueName());
            ResultSet res = stat.executeQuery();
            while (res.next()) {
                result.add(new PhysicalProperty(res.getInt("physicalPropertyId"), res.getString("author"), res.getString("comment"), res.getDate("date"), res.getString("type")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private int addPhysicalProperty(Tissue t, PhysicalProperty p) {
        PreparedStatement stat;

        int id = getNextPhysicalPropertyId();

        Vector<PhysicalProperty> result = new Vector<PhysicalProperty>();
        try {
            stat = connection.prepareStatement("INSERT into PhysicalProperties VALUES(?,?,?,?,?,?)");

            stat.setString(1, t.getTissueName());
            stat.setInt(2, id);
            stat.setString(3, p.getAuthor());
            stat.setString(4, p.getComment());
            stat.setString(5, p.getType());
            stat.setDate(6, (Date) p.getDate());
            System.out.println(p.getAuthor());
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    return id;
    }

    public void updatePhysicalProperty(PhysicalProperty p) {
        PreparedStatement stat;
        Vector<PhysicalProperty> result = new Vector<PhysicalProperty>();
        try {
            stat = connection.prepareStatement("UPDATE PhysicalProperties SET author=?,comment=?,type=? where physicalPropertyId=?");
          
            stat.setInt(4, p.getId());
            stat.setString(1,p.getAuthor());
            stat.setString(2, p.getComment());
            stat.setString(3, p.getType());
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deletePhysicalProperty(PhysicalProperty p) {
        PreparedStatement stat;
        Vector<PhysicalProperty> result = new Vector<PhysicalProperty>();
        try {
            stat = connection.prepareStatement("DELETE FROM PhysicalProperties WHERE physicalPropertyId=?");
            stat.setInt(1, p.getId());
            stat.executeUpdate();
            
            stat = connection.prepareStatement("DELETE FROM Echogenicities WHERE physicalPropertyId=?");
            stat.setInt(1, p.getId());
            stat.executeUpdate();

            stat = connection.prepareStatement("DELETE FROM MagneticProperties WHERE physicalPropertyId=?");
            stat.setInt(1, p.getId());
            stat.executeUpdate();

            stat = connection.prepareStatement("DELETE FROM ChemicalComponents WHERE physicalPropertyId=?");
            stat.setInt(1, p.getId());
            stat.executeUpdate();

            stat = connection.prepareStatement("DELETE FROM ChemicalBlend WHERE physicalPropertyId=?");
            stat.setInt(1, p.getId());
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getNextPhysicalPropertyId() {
        PreparedStatement stat;
        try {
            stat = connection.prepareCall("SELECT MAX(physicalPropertyId) from PhysicalProperties");
            ResultSet rs = stat.executeQuery();
            
            rs.next();
            int max = rs.getInt("1");
            max++;
            System.out.println("query was done ; result is "+max);
            return max;
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public Echogenicity getEchogenicity(PhysicalProperty p) {
        PreparedStatement stat;
        Echogenicity result = null;
        try {
            stat = connection.prepareStatement("SELECT spatDistInstanceId,ampDistInstanceId from Echogenicities where physicalPropertyId=?");
            stat.setInt(1, p.getId());
            ResultSet res = stat.executeQuery();
            if (res.next()) {
                int spatId = res.getInt(1);
                int ampId = res.getInt(2);

                getDistributionInstance(spatId);

                result = new Echogenicity(getDistributionInstance(res.getInt("spatDistInstanceId")), getDistributionInstance(res.getInt("ampDistInstanceId")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void deleteAndAddEchogenicity(int id, Echogenicity e){
     //delete any potential echog value
        PreparedStatement stat;
        Vector<PhysicalProperty> result = new Vector<PhysicalProperty>();
        try {
            stat = connection.prepareStatement("DELETE FROM Echogenicities WHERE physicalPropertyId=?");
            stat.setInt(1, id);
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Deleted previous echogenicities");
        //add echog value
        try {
            addDistributionInstance(e.getSpatialDistribution());
            addDistributionInstance(e.getAmplitudeDistribution());

            stat = connection.prepareStatement("INSERT into Echogenicities VALUES(?,?,?)");
            stat.setInt(1, id);
            stat.setInt(2, e.getSpatialDistribution().getId());
            stat.setInt(3, e.getAmplitudeDistribution().getId());
            System.out.println("INSERT into Echogenicities VALUES("+id+","+e.getSpatialDistribution().getId()+","+e.getAmplitudeDistribution().getId()+")");
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Done");
    }
    public void setEchogenicity(Tissue t, PhysicalProperty p, Echogenicity e) {
        //create new physical property
        System.out.println("Setting echogenicity");
        int id = addPhysicalProperty(t,p);
        deleteAndAddEchogenicity(id,e);   
        System.out.println("New physical property created. Id is "+id);

       
    }

    public int getNextDistributionInstanceId(){
        PreparedStatement stat;
        Vector<PhysicalProperty> result = new Vector<PhysicalProperty>();
        int id = -1;
        try {
                stat = connection.prepareStatement("SELECT MAX(instanceid) from distributioninstance");
                ResultSet res = stat.executeQuery();
                if(res.next())
                    id = res.getInt(1);              
        }
     catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id+1;
    }

    public void updateEchogenicity(PhysicalProperty p, Echogenicity e) {
       int id = p.getId();
       updatePhysicalProperty(p);
       deleteAndAddEchogenicity(id,e);
    }

    public ChemicalBlend getChemicalBlend(PhysicalProperty p) {
        PreparedStatement stat;
        ChemicalBlend result = null;
        //get list of components
        List<ChemicalBlendComponent> components = new ArrayList<ChemicalBlendComponent>();
        try {
            stat = connection.prepareStatement("SELECT massPercentage,elementName from ChemicalComponents where physicalPropertyId=?");
            stat.setInt(1, p.getId());
            ResultSet res = stat.executeQuery();
            while (res.next()) {
                components.add(new ChemicalBlendComponent(res.getString("elementName"), res.getDouble("massPercentage")));

                
            }
            stat = connection.prepareStatement("SELECT density,phase from ChemicalBlend where physicalPropertyId=?");
            stat.setInt(1, p.getId());
            res = stat.executeQuery();
            if(res.next())
                result = new ChemicalBlend(res.getDouble("density"), res.getString("phase"), components);

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void setChemicalBlend(Tissue t, PhysicalProperty p, ChemicalBlend c) {
         //create new physical property
        int id = addPhysicalProperty(t,p);
        p.setId(id);

        //delete previous blend
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("DELETE FROM ChemicalBlend,ChemicalComponents WHERE physicalPropertyId=?");
            stat.setInt(1, p.getId());

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        //add blend
        try {
            stat = connection.prepareStatement("INSERT into ChemicalBlend VALUES(?,?,?)");
            stat.setInt(1, p.getId());
            stat.setDouble(2, c.getDensity());
            stat.setString(3, c.getPhase());
            stat.executeUpdate();

            for (ChemicalBlendComponent comp : c.getComponents()) {
                stat = connection.prepareStatement("INSERT into ChemicalComponents VALUES(?,?,?)");
                stat.setInt(1, p.getId());
                stat.setDouble(2, comp.getMassRatio());
                stat.setString(3, comp.getElementName());
                stat.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateChemicalBlend(PhysicalProperty p, ChemicalBlend c) {
        System.err.println("not implemented yet");
       //setChemicalBlend(p, c);

    }

    public String getDistributionName(int instanceid){
        DistributionInstance di = new DistributionInstance();
        PreparedStatement stat;
        String result = null;
        try{
            stat = connection.prepareStatement("SELECT distname from distributionInstance where instanceId=?");
            stat.setInt(0, instanceid);
            ResultSet res = stat.executeQuery();
            if(res.next()){
                result = res.getString(0);
            }
        }
        catch(SQLException e){
                Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, e);

        }
        return result;
    }

    public List<MagneticProperty> getMagneticProperties(PhysicalProperty p) {
        PreparedStatement stat;

        List<MagneticProperty> props = new ArrayList<MagneticProperty>();
        try {
            stat = connection.prepareStatement("SELECT propertyName,distInstancId from MagneticProperties where physicalPropertyId=?");
            stat.setInt(1, p.getId());
            ResultSet res = stat.executeQuery();
            while (res.next()) {
                props.add(new MagneticProperty(res.getString("propertyName"), getDistributionInstance(res.getInt("distInstancId"))));


                
            }
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return props;
    }

    private void deleteAndAddMagneticProperties(int id, List<MagneticProperty> mp){
        //delete existing magnetic properties
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("DELETE from MagneticProperties where physicalPropertyId=?");
            stat.setInt(1, id);
            stat.executeUpdate();

            //add new magnetic properties
            for (MagneticProperty m : mp) {
                addDistributionInstance(m.getDistributionInstance());
                stat = connection.prepareStatement("INSERT INTO MagneticProperties VALUES(?,?,?)");
                stat.setInt(1, id);
                stat.setString(2, m.getPropertyName());
                stat.setInt(3, m.getDistributionInstance().getId());
                stat.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void setMagneticProperties(Tissue t, PhysicalProperty p, List<MagneticProperty> mp) {
        //create new physical property
        int id = addPhysicalProperty(t,p);
        p.setId(id);
        deleteAndAddMagneticProperties(id,mp);
        
    }

    public void updateMagneticProperties(PhysicalProperty p, List<MagneticProperty> mp) {
        updatePhysicalProperty(p);
       deleteAndAddMagneticProperties(p.getId(),mp);
    }

    private Distribution getDistribution(String name,String expression) {
        PreparedStatement stat;

        try {
            //get parameters
            stat = connection.prepareStatement("SELECT parameterName,symbol from DistributionParameters where distributionName=?");
            stat.setString(1, name);
            ResultSet set1 = stat.executeQuery();
            List<DistributionParameter> dparams = new ArrayList<DistributionParameter>();
            while (set1.next()) {
                dparams.add(new DistributionParameter(set1.getString("parameterName"), set1.getString("symbol")));

            }

            return new Distribution(name, expression, dparams);
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private String getDistributionExpression(String name) {
        PreparedStatement stat;
        String result = null;
        try {
            stat = connection.prepareStatement("SELECT expression from Distribution where distributionname=?");
           ResultSet set = stat.executeQuery();
           if(set.next())
               result = set.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public List<Distribution> getDistributions() {
        PreparedStatement stat;

        List<Distribution> dists = new ArrayList<Distribution>();
        try {
            stat = connection.prepareStatement("SELECT distributionName,expression from Distribution");
            ResultSet res = stat.executeQuery();
            while (res.next()) {
                String name = res.getString("distributionName");
                String expression = res.getString("expression");
                dists.add(getDistribution(name, expression));
            }

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dists;
    }

    public void addDistribution(Distribution d) {
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("INSERT INTO Distribution VALUES(?,?)");
            stat.setString(1, d.getName());
            stat.setString(2, d.getExpression());
            stat.executeUpdate();

            for(DistributionParameter p : d.getParameters()){
                stat = connection.prepareStatement("INSERT INTO DistributionParameters VALUES(?,?,?)");
                stat.setString(1,d.getName());
                stat.setString(2,p.getName());
                stat.setString(3,p.getSymbol());
                stat.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDistribution(Distribution d) {
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("UPDATE Distribution set expression=? where distributionName=?");
            stat.setString(1, d.getExpression());
            stat.setString(2, d.getName());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteDistribution(Distribution d) {
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("DELETE from Distribution where distributionName=?");
            stat.setString(1, d.getName());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DistributionInstance getDistributionInstance(int instanceId) {

        PreparedStatement stat;
        DistributionInstance di = null;
        try {
            //build distribution
            stat = connection.prepareStatement("SELECT distributionName,expression from Distribution,DistributionInstance where instanceId=? and distName=distributionName");
            stat.setInt(1, instanceId);
            ResultSet set = stat.executeQuery();
            Distribution d = null;
            if(set.next())
            {
                d = getDistribution(set.getString("distributionName"), set.getString("expression"));
            }
             //getvalues
             stat = connection.prepareStatement("SELECT parameterName,value,symbol from DistributionInstanceValues,DistributionParameters where instanceId=? and symbol=parameterSymbol");
             stat.setInt(1, instanceId);
            ResultSet set1 = stat.executeQuery();
            List <DistributionParameterValue> l = new ArrayList<DistributionParameterValue>();
            while (set1.next()) {
                DistributionParameter param = new DistributionParameter(set1.getString("parameterName"), set1.getString("symbol"));
                DistributionParameterValue value = new DistributionParameterValue(param,set1.getDouble("value"));
                l.add(value);
            }
            di = new DistributionInstance(instanceId, d, l);

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return di;
    }

    public List<DistributionInstance> getDistributionInstances() {
        PreparedStatement stat;
        List<DistributionInstance> instances = new ArrayList<DistributionInstance>();
        try {
            stat = connection.prepareStatement("SELECT instanceId from DistributionInstance");
            ResultSet set = stat.executeQuery();
            while (set.next()) {
                int id = set.getInt("instanceId");
                instances.add(getDistributionInstance(id));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instances;
    }

    public void addDistributionInstance(DistributionInstance di) {
        int id = getNextDistributionInstanceId();
        di.setId(id);
        System.out.println("adding new distribution instance with id "+id);
        PreparedStatement stat;
        try {
            //adding distribution instance
            stat = connection.prepareStatement("INSERT INTO DistributionInstance VALUES (?,?)");
            stat.setInt(1, di.getId());
            stat.setString(2, di.getDistributionType().getName());
            stat.executeUpdate();

            //adding all parameter values
            for (DistributionParameterValue v : di.getValues()) {
                stat = connection.prepareStatement("INSERT INTO DistributionInstanceValues VALUES (?,?,?)");
                stat.setInt(1, di.getId());
                stat.setString(2, v.getParam().getSymbol());
                stat.setDouble(3, v.getValue());
                stat.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDistributionInstance(DistributionInstance di) {
        PreparedStatement stat;
        try {
            //cannot update distribution instance
            //update distribution values
            for (DistributionParameterValue v : di.getValues()) {
                stat = connection.prepareStatement("UPDATE DistributionInstanceValues SET value=? where instanceId=? and parameterSymbol=? ");
                stat.setDouble(1, v.getValue());
                stat.setInt(2, di.getId());
                stat.setString(3, v.getParam().getSymbol());
                stat.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteDistributionInstance(DistributionInstance di) {
        PreparedStatement stat;
        try {

            stat = connection.prepareStatement("DELETE from DistributionInstance,DistributionInstanceValues where instanceId=?");
            stat.setInt(1, di.getId());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addMagneticPropertyName(String name) {
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("INSERT INTO MagneticPropertyNames VALUES(?)");
            stat.setString(1, name);
            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteMagneticPropertyName(String name) {
        PreparedStatement stat;
        try {
            stat = connection.prepareStatement("DELETE FROM MagneticPropertyNames WHERE propertyName=?");
            stat.setString(1, name);
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getMagneticPropertyNames() {
        PreparedStatement stat;
        List<String> result = new ArrayList<String>();
        try {
            stat = connection.prepareStatement("SELECT propertyName from MagneticPropertyNames");
            ResultSet set = stat.executeQuery();
            while (set.next()) {
                result.add(set.getString("propertyName"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(TissueData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }
}
