/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.rpc;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import fr.insalyon.creatis.vip.query.server.dao.business.QueryBusiness;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.apache.log4j.Logger;
/**
 *
 * @author Boujelben
 */
public class QueryServiceImpl  extends AbstractRemoteServiceServlet implements QueryService  {
     private static Logger logger = Logger.getLogger(QueryServiceImpl.class);
     
     
     private QueryBusiness queryBusiness; 
     
     
     public QueryServiceImpl() {

       
        queryBusiness = new QueryBusiness();
    }
     
  @Override
   public List<String[]> getQureies() throws QueryException  {

        try {
            return queryBusiness.getQueries();
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
  }

  @Override
    public List<String[]> getVersion() throws QueryException {
       try {
            return queryBusiness.getVersion();
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    
  @Override
   public Long add(Query query) throws QueryException {
        
    try {
            query.setQueryMaker(getSessionUser().getEmail());
             } catch (CoreException ex) {
             java.util.logging.Logger.getLogger(QueryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
         }
          
    try{
          return queryBusiness.add(query);
             
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
       
    }
  }
  
    @Override
  public Long addVersion(QueryVersion version) throws QueryException {
        
    try {
             return queryBusiness.addVersion(version);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }
 
 @Override
  public void removeVersion(Long versionid)throws QueryException{
     
     
     try {
             queryBusiness.removeVersion(versionid);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }
     
 @Override
 public List<Long> addParameter(Parameter param)throws QueryException{
  try {
            return queryBusiness.addParameter(param);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }   
 }
     
 
 
 
  @Override
 public List<String[]> getQuerie(Long queryversionid)throws  QueryException{
  try {
            return queryBusiness.getQuerie(queryversionid);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }   
 }
  
       
 @Override
    public List<Parameter> getParameter(Long queryVersionID) throws  QueryException {
     try {
            return queryBusiness.getParameter(queryVersionID);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }   
 }
 
 
 
  @Override
 public Long addValue(Value value) throws QueryException {
 try {
            return queryBusiness.addValue(value);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
         
}
@Override
    public Long addQueryExecution(QueryExecution queryExecution) throws QueryException {
    
    try {
            queryExecution.setExecuter(getSessionUser().getEmail());
           
        } catch (CoreException ex) {  
             java.util.logging.Logger.getLogger(QueryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
         }
    try{
      return queryBusiness.addQueryExecution(queryExecution);
        } catch (BusinessException ex) {
            throw new QueryException(ex);
            
}
    
    }
   @Override 
    public List<String[]> getQueryHistory() throws QueryException{
       try {
            return queryBusiness.getQueryHistory();
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
   
   
   @Override 
    public String getBody(Long queryVersionID,Long queryExecutionID,boolean parameter) throws QueryException{
       try {
            return queryBusiness.getBody(queryVersionID,queryExecutionID,parameter);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
   
   
   @Override 
  public void updateQueryExecution(String urlResult, String status,Long executionID)throws QueryException{
       try {
             queryBusiness.updateQueryExecution(urlResult, status, executionID);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
   
   @Override
   public void updateQueryVersion(Long queryID,String name, String description)throws QueryException{
     try {
             queryBusiness.updateQueryVersion(queryID, name, description);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
   
    @Override
   public String getDescription(Long queryVersionID)throws QueryException{
     try {
             return queryBusiness.getDescription(queryVersionID);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
    
    
     @Override
     public List<String[]> getParameterValue(Long queryExecutionID)throws QueryException{
     try {
             return queryBusiness.getParameterValue(queryExecutionID);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
     
       @Override
      public void  removeQueryExecution(Long executionID) throws QueryException{
     try {
              queryBusiness.removeQueryExecution(executionID);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
       
       
        @Override
         public String  maxVersion(Long queryID) throws QueryException{
       
     try {
             return queryBusiness.maxVersion(queryID);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
        
        @Override
        public Long  getQueryID(Long queryVersionID) throws QueryException{
       
     try {
             return queryBusiness.getQueryID(queryVersionID);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
            
        }  
    
    }
}

   