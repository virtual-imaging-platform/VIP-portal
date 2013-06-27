/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;

/**
 *
 * @author glatard
 */
public abstract class SSHDAOFactory {
    
    public static SSHDAOFactory getDAOFactory() {

        return MySQLDAOFactory.getInstance();
    }
    
    public abstract SSHDAO getSSHDAO() throws DAOException;
}
