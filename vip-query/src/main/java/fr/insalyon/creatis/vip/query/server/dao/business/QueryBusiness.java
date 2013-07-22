/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.business;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public class QueryBusiness {
    
    
  
  
    public List<String[]> getQueries() throws BusinessException {

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
  
     public Long add(Query query) throws BusinessException {
     try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().add(query);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
}
   public Long addVersion(QueryVersion version) throws BusinessException {
     try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addVersion(version);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
}
   
   
   
   public void  removeVersion(Long versionid) throws BusinessException {
     try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().removeVersion(versionid);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
   
   }
     
   
   
  public List<Long> addParameter(Parameter param)throws  BusinessException {
    try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addParameter(param);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } 
      
      
  }
  
  
   public List<String[]> getQuerie(Long queryversionid)throws  BusinessException{
  
   try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQuerie(queryversionid);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } 
  
   }
   
   
   public List<Parameter> getParameter(Long queryVersionID) throws  BusinessException{
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getParameter(queryVersionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } 
  
   }
   
   
   public Long addValue(Value value) throws BusinessException{
   try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addValue(value);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } 
  
   }
   
     public Long addQueryExecution(QueryExecution queryExecution)throws BusinessException{
   try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addQueryExecution(queryExecution);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } 
  
   }
     
     
     public List<String[]> getQueryHistory() throws BusinessException{
   try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQueryHistory();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } 
  
   }
     
     
     public String getBody(Long queryVersionID,Long queryExecutionID) throws BusinessException{
          try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getBody(queryVersionID, queryExecutionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        } 
  
   }
     
     
     public void updateQueryExecution(String urlResult, String status,Long executionID )  throws BusinessException{
         try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().updateQueryExecution(urlResult, status, executionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
     }
     
     }
  
