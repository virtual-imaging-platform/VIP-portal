/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.dao;

import fr.insalyon.creatis.vip.cardiac.dao.mysql.CardiacData;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard
 */
public class MYSQLDAOFactory extends CardiacDAOFactory {
    
    private static CardiacDAOFactory instance;
    private final static Logger logger = Logger.getLogger(MYSQLDAOFactory.class);
        
    public static CardiacDAOFactory getInstance(){
        if(instance == null)
            instance = new MYSQLDAOFactory();
        return instance;
    }

    private MYSQLDAOFactory(){
         logger.info("Configuring VIP Cardiac database.");
        try {
            PlatformConnection.getInstance().createTable("CardiacSimulations",
                          "name VARCHAR(255), description TEXT, files TEXT, modalities TEXT, simulation VARCHAR(255),"
                          + "PRIMARY KEY (name)");

//                  PlatformConnection.getInstance().createTable("CardiacFiles",
//                          "uri VARCHAR(255), "
//                          + "name VARCHAR(255), "
//                          + "description TEXT,"
//                          + "PRIMARY KEY (uri)"
//                          );
//
//                  PlatformConnection.getInstance().createTable("CardiacAssociations",
//                          "name VARCHAR(255), "
//                          + "uri VARCHAR(255), "
//                          + "PRIMARY KEY (name,uri)");
        } catch (DAOException ex) {
            logger.error(ex);
        }
    }
    
    @Override
    public CardiacDAO getCardiacDAO() throws DAOException {
        return new CardiacData();
    }
    
}
