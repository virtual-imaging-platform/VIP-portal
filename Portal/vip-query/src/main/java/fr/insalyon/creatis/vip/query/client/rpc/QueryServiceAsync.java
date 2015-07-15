/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public interface QueryServiceAsync {

    public void getQueries(AsyncCallback<List<String[]>> asyncCallback);

    public void getVersion(AsyncCallback<List<String[]>> asyncCallback);

    public void add(Query query, AsyncCallback<Long> asyncCallback);

    public void addVersion(QueryVersion version,boolean bodyTypeHtml, AsyncCallback<Long> asyncCallback);

    public void removeVersion(Long versionid, AsyncCallback<Void> asyncCallback);

    public void addParameter(Parameter param, AsyncCallback<List<Long>> asyncCallback);

    public void getQuerie(Long queryversionid, Long queryID, AsyncCallback<List<String[]>> asyncCallback);

    public void getParameter(Long queryVersionID, AsyncCallback<List<Parameter>> asyncCallback);

    public void addValue(Value value, AsyncCallback<Long> asyncCallback);

    public void addQueryExecution(QueryExecution queryExecution, AsyncCallback<Long> asyncCallback);

    public void getQueryHistory(String state, AsyncCallback<List<String[]>> asyncCallback);

    public void getBody(Long queryVersionID, Long queryExecutionID, boolean parameter, AsyncCallback<String> asyncCallback);

    public void updateQueryExecution(String bodyResult, String status, Long executionID, AsyncCallback<Void> asyncCallback);

    public void updateQueryExecutionStatusWaiting(String status, Long executionID, AsyncCallback<Void> asyncCallback);

    public void updateQueryExecutionStatusFailed(String status, Long executionID, AsyncCallback<Void> asyncCallback);

    public void updateQueryVersion(Long queryID, String name, String description, boolean isPublic, AsyncCallback<Void> asyncCallback);

    public void getDescriptionQueryMaker(Long queryVersionID, AsyncCallback<List<String>> asyncCallback);

    public void getParameterValue(Long queryExecutionID, AsyncCallback< List<String[]>> asyncCallback);

    public void removeQueryExecution(Long executionID, AsyncCallback< Void> asyncCallback);

    public void maxVersion(Long queryID, AsyncCallback<Long> asyncCallback);

    public void getQueryID(Long queryVersionID, AsyncCallback<Long> asyncCallback);

    public void getBodies(Long queryID, String body, AsyncCallback<Boolean> asyncCallback);
}
