/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public interface EndPointSparqlServiceAsync {

    public void getUrlResult(String param1, String param2, AsyncCallback<String> asyncCallback);
    public void getUrlResultFormatTable(String val1,String val2, AsyncCallback <List<String[]>> asyncCallback);
    public void getUrlResultFormatTable(String param1,String val1,String val2,String val3,String val4, AsyncCallback <List<String[]>> asyncCallback);
}
