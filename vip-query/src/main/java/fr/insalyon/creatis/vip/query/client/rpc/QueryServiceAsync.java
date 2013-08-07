/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;

import fr.insalyon.creatis.vip.query.client.view.QueryException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public interface QueryServiceAsync {

    public void getQureies(AsyncCallback<List<String[]>> asyncCallback);

    public void getVersion(AsyncCallback<List<String[]>> asyncCallback);

    public void add(Query query, AsyncCallback<Long> asyncCallback);

    public void addVersion(QueryVersion version, AsyncCallback<Long> asyncCallback);

    public void removeVersion(Long versionid, AsyncCallback<Void> asyncCallback);

    public void addParameter(Parameter param, AsyncCallback<List<Long>> asyncCallback);

    public void getQuerie(Long queryversionid,Long queryID, AsyncCallback<List<String[]>> asyncCallback);

    public void getParameter(Long queryVersionID, AsyncCallback<List<Parameter>> asyncCallback);

    public void addValue(Value value, AsyncCallback<Long> asyncCallback);

    public void addQueryExecution(QueryExecution queryExecution, AsyncCallback<Long> asyncCallback);

    public void getQueryHistory(AsyncCallback<List<String[]>> asyncCallback);

    public void getBody(Long queryVersionID, Long queryExecutionID, boolean parameter, AsyncCallback<String> asyncCallback);

    public void updateQueryExecution(String urlResult, String status, Long executionID, AsyncCallback<Void> asyncCallback);

    public void updateQueryVersion(Long queryID, String name, String description, AsyncCallback<Void> asyncCallback);

    public void getDescription(Long queryVersionID, AsyncCallback<String> asyncCallback);

    public void getParameterValue(Long queryExecutionID, AsyncCallback< List<String[]>> asyncCallback);

    public void removeQueryExecution(Long executionID, AsyncCallback< Void> asyncCallback);

    public void maxVersion(Long queryID, AsyncCallback<Long> asyncCallback);

    public void getQueryID(Long queryVersionID, AsyncCallback<Long> asyncCallback);
     
     public void getBodies(Long queryID,String body, AsyncCallback <Boolean> asyncCallback);
}
