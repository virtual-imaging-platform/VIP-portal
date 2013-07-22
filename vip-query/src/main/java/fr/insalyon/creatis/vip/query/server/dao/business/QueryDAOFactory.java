/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.business;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public abstract class QueryDAOFactory {
   


    public static QueryDAOFactory getDAOFactory() {

        return MySQLDAOFactory.getInstance();
    }

    public abstract QueryDAO getQueryDAO() throws DAOException;
    
   

    //public abstract ClassDAO getClassDAO() throws DAOException;
    
  //  public abstract EngineDAO getEngineDAO() throws DAOException;

  //  public abstract ApplicationInputDAO getApplicationInputDAO() throws DAOException;

   // public abstract SimulationDAO getSimulationDAO(String dbPath) throws DAOException;

   // public abstract ExecutionNodeDAO getExecutionNodeDAO(String dbPath) throws DAOException;
}
    

