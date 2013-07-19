/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

/**
 *
 * @author Boujelben
 */
import com.google.gwt.core.client.GWT;
import java.util.List;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import java.sql.Timestamp;

public interface QueryService extends RemoteService {
    
   
public static final String SERVICE_URI = "/queryService";

    public static class Util {

        public static QueryServiceAsync getInstance() {

            QueryServiceAsync instance = (QueryServiceAsync) GWT.create(QueryService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    
    
    
    
    
     public List<String[]> getQureies() throws QueryException;
     public List<String[]> getVersion() throws QueryException;
     public Long add(Query query)throws QueryException;
     public Long addVersion(QueryVersion version)throws QueryException;
     public void removeVersion(Long versionid)throws QueryException;
     public List<Long> addParameter(Parameter param)throws QueryException;
     public List<String[]> getQuerie(Long queryversionid)throws  QueryException;
     public List<Parameter> getParameter(Long queryVersionID) throws  QueryException;
     public Long addValue(Value value) throws QueryException;
     public Long addQueryExecution(QueryExecution queryExecution) throws QueryException;
     public List<String[]> getQueryHistory() throws QueryException;
     public String getBody(Long queryVersionID,Long queryExecutionID) throws QueryException;
     
     public void updateQueryExecution(String urlResult, String status,Long executionID)throws QueryException;
}
