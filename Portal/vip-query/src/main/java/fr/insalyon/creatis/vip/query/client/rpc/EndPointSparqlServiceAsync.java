/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author nouha
 */
public interface EndPointSparqlServiceAsync {

    public void getUrlResult(String param1, String param2, AsyncCallback<String> asyncCallback);
}
