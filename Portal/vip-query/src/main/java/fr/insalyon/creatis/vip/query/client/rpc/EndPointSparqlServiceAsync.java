/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author nouha
 */
public interface EndPointSparqlServiceAsync {

    public void getUrlResult(String param1, String param2, AsyncCallback<String> asyncCallback);
    public void getUrlResultFormatTable(String param1, AsyncCallback <List<String[]>> asyncCallback);
}
