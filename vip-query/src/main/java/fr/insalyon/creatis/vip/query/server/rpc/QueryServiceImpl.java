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
import fr.insalyon.creatis.vip.query.server.dao.persistance.QueryBusiness;
import java.sql.Timestamp;
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
            return queryBusiness.addQueryExecution(queryExecution);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }  
    
    }
    
    
   
}

   