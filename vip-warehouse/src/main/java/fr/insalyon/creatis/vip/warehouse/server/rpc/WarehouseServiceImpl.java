/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.warehouse.server.rpc;


import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.warehouse.client.rpc.WarehouseService;
import fr.insalyon.creatis.vip.warehouse.server.business.XnatBusiness;
import fr.insalyon.creatis.vip.warehouse.client.view.WareHouseException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.warehouse.server.business.ItemDB;
import fr.insalyon.creatis.vip.warehouse.server.business.MedImgWarehouseBusiness;
import fr.insalyon.creatis.vip.warehouse.server.business.MidasBusiness;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.json.JSONException;
import org.apache.log4j.Logger;


/**
 *
 * @author Frederic Cervenansky
 */
public class WarehouseServiceImpl extends AbstractRemoteServiceServlet implements WarehouseService {
    private String user = "cervenansky";
    private String pwd = "oeufs_sont_cuits";
    private String url = " https://central.xnat.org";
    private XnatBusiness xness;
    private MidasBusiness mness;
    private MedImgWarehouseBusiness midbness;
        private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(WarehouseServiceImpl.class);
     public WarehouseServiceImpl() {
        xness = new XnatBusiness();
        mness = new MidasBusiness();
        midbness = new MedImgWarehouseBusiness();
    }
    
    
    @Override
   public List<String> getSites()
   {
       try {
              System.out.println("I was here");
             Logger.getLogger("gettheSites");
                return midbness.getSites(getSessionUser().getEmail());
        } catch (DAOException ex) {
            Logger.getLogger(WarehouseServiceImpl.class.getName());
            return null;
        }
        catch (CoreException ex) {
                Logger.getLogger(WarehouseServiceImpl.class.getName());
                return null;
            }
   }
    
    @Override
    public String  test()
    {
        return "ok";
    }
  
    @Override
   public Boolean isSessionAlive(String nick, String site)
   {
       Boolean bres = false;
        try {
            return xness.isSessionAlive(nick, site);
        } catch (DAOException ex) {
            //Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bres;
   }
    
    @Override
   public void getConnection(String nickname, String pwd, String url, String type) 
   {
        try {
           
            if (type.contains("midas"))
                 mness.getConnection(nickname, pwd, url);
            else if (type.equalsIgnoreCase("xnat"))
                xness.getConnection(nickname, pwd, url);
            else{}
        } catch (IOException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
             
        } catch (DAOException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
           
        }  catch (JSONException ex) {
            java.util.logging.Logger.getLogger(WarehouseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            
        }
   }
    
    @Override
   public HashMap<String,String> getProjects(String nickname, String url, String type) throws WareHouseException
   { 
       HashMap<String, String> result = new HashMap<String, String>();
        try {
             if (type.contains("midas"))
                 return mness.getTopFolder(nickname, url);
            else if (type.equalsIgnoreCase("xnat"))
                return result;//xness.getProjects(nickname, url);
            else{
            return result;}
          
        } catch (MalformedURLException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return result;
        } catch (IOException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return result;
        } 
         catch (DAOException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return result;
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(WarehouseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
             return result;
        }
   }
   
   
    @Override
    public String getProject(String nick, String site, String projectid) throws WareHouseException
   {
        try {
            return xness.getProject(nick, site, projectid);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
         catch (DAOException ex) {
            Logger.getLogger(ex.toString());//Logger.getLogger(XnatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(WarehouseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
   }
 
//   public JSONObject getData(String nick, String site,String project) throws XnatException
//   {
//         return xness.getData( project);
//   }

}
