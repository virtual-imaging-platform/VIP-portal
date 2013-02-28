/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cardiac.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;

/**
 *
 * @author glatard
 */
public abstract class CardiacDAOFactory {
    
    public static CardiacDAOFactory getDAOFactory(){
        return MYSQLDAOFactory.getInstance();
    }
    
    public abstract CardiacDAO getCardiacDAO() throws DAOException;
    
}
