/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.persistance;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public class QueryBusiness {
    
    
  
  
    public List<Query> getQueries() throws BusinessException {

        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQueries();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
  public List<String[]> getVersion() throws BusinessException {

        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getVersion();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
  
     public void add(Query query) throws BusinessException {
     try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().add(query);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
}
   public void addVersion(QueryVersion version,Query query) throws BusinessException {
     try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().addVersion(version,query);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
}    
     
     
}