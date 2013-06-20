/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.rpc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import fr.insalyon.creatis.vip.query.server.dao.persistance.QueryBusiness;
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
   public List<Query> getQureies() throws QueryException  {

        try {
            return queryBusiness.getQueries();
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
  }

    public List<String[]> getVersion() throws QueryException {
       try {
            return queryBusiness.getVersion();
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    public void add(Query query) throws QueryException {
        
    try {
             queryBusiness.add(query);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }
 public void addVersion(QueryVersion version,Query query) throws QueryException {
        
    try {
             queryBusiness.addVersion(version,query);
        
        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }
    
    
         
         
    }
    
    
   
    


   