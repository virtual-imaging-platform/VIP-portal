/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.DAO;

import fr.insalyon.creatis.vip.warehouse.mysql.MedImgWarehouseData;
import fr.insalyon.creatis.vip.warehouse.mysql.MedImgWarehouseDataSQL;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author cervenansky
 */
public class MedImgStoreDAOFactory extends MedImgStoreDAOAbstractFactory {
    
    private static MedImgStoreDAOAbstractFactory instance;
    private final static Logger logger = Logger.getLogger(MedImgStoreDAOFactory.class);
 
    
      public static MedImgStoreDAOAbstractFactory getInstance(){
        if(instance == null)
            instance = new MedImgStoreDAOFactory();
        return instance;
    }
      public MedImgStoreDAOFactory()
      {
            logger.info("Configuring VIP Medical Images warehouses database.");
        try {        
            System.out.println("create table");
            PlatformConnection.getInstance().createTable("MedImgWarehouse",
                          "email VARCHAR(255), nickname VARCHAR(255), url VARCHAR(255), " 
                          + "site VARCHAR(255), session TEXT, type VARCHAR(255), "
                          + "PRIMARY KEY (nickname, url), "
                          + "FOREIGN KEY (email) REFERENCES VIPUsers(email) "
                    + "ON DELETE CASCADE ON UPDATE CASCADE");
            
            
            
                     //     "email VARCHAR(255), nickname VARCHAR(255), url TEXT, site VARCHAR(255), session TEXT, type VARCHAR(255), PRIMARY KEY (nickname, url), FOREIGN KEY (email) REFERENCES VIPUsers(email) ON DELETE CASCADE ON UPDATE CASCADE"
            
            
            
            System.out.println("table created");
        } catch (DAOException ex) {
            System.out.println("created table");
            java.util.logging.Logger.getLogger(MedImgStoreDAOFactory.class.getName()).log(Level.SEVERE, null, ex);
        }


      }
      
      @Override
      public MedImgWarehouseData getMedicalImageStorage() throws DAOException
      {
          return new MedImgWarehouseDataSQL();
      }
}
