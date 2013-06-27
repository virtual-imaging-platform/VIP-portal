/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.datamanager.server.dao.mysql.SSHData;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard
 */
class MySQLDAOFactory extends SSHDAOFactory {
    
     private final static Logger logger = Logger.getLogger(MySQLDAOFactory.class);
    private static MySQLDAOFactory instance;

    // Singleton
    protected static MySQLDAOFactory getInstance()  {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    private MySQLDAOFactory()  {
         try {
             logger.info("Configuring VIP SSH database.");
             PlatformConnection.getInstance().createTable("VIPSSHAccounts","email VARCHAR(255), LFCDir VARCHAR(255), "
                     + "sshUser VARCHAR(255), sshHost VARCHAR(255), sshDir VARCHAR(255), sshPort INT, validated BOOLEAN,"
                     + " auth_failed BOOLEAN, PRIMARY KEY(email,LFCDir), FOREIGN KEY (email) REFERENCES VIPUsers(email) "
                     + "ON DELETE CASCADE ON UPDATE CASCADE");
         } catch (DAOException ex) {
              logger.error(ex);
         }
       
    }

    @Override
    public SSHDAO getSSHDAO() throws DAOException {
        return new SSHData();
    }
    
}
