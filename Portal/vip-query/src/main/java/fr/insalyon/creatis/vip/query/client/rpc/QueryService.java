/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

/**
 *
 * @author Nouha Boujelben
 */
import com.google.gwt.core.client.GWT;
import java.util.List;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import fr.insalyon.creatis.vip.query.client.view.QueryException;

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

    public List<String[]> getQueries() throws QueryException;

    public List<String[]> getVersion() throws QueryException;

    public Long add(Query query) throws QueryException;

    public Long addVersion(QueryVersion version,boolean bodyTypeHtml) throws QueryException;

    public void removeVersion(Long versionid) throws QueryException;

    public List<Long> addParameter(Parameter param) throws QueryException;

    public List<String[]> getQuerie(Long queryversionid, Long queryID) throws QueryException;

    public List<Parameter> getParameter(Long queryVersionID) throws QueryException;

    public Long addValue(Value value) throws QueryException;

    public Long addQueryExecution(QueryExecution queryExecution) throws QueryException;

    public List<String[]> getQueryHistory(String state) throws QueryException;

    public String getBody(Long queryVersionID, Long queryExecutionID, boolean parameter) throws QueryException;

    public void updateQueryVersion(Long queryID, String name, String description, boolean isPublic) throws QueryException;

    public void updateQueryExecutionStatusWaiting(String status, Long executionID) throws QueryException;

    public void updateQueryExecutionStatusFailed(String status, Long executionID) throws QueryException;

    public void updateQueryExecution(String urlResult, String status, Long executionID) throws QueryException;

    public List<String> getDescriptionQueryMaker(Long queryVersionID) throws QueryException;

    public List<String[]> getParameterValue(Long queryExecutionID) throws QueryException;

    public void removeQueryExecution(Long executionID) throws QueryException;

    public Long maxVersion(Long queryID) throws QueryException;

    public Long getQueryID(Long queryVersionID) throws QueryException;

    public boolean getBodies(Long queryID, String body) throws QueryException;
}
