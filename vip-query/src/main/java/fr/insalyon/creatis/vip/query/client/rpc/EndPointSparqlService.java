/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 *
 * @author nouha
 */
public interface EndPointSparqlService extends RemoteService {
    String getUrlResult(String param1,String param2);                        
}
