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
