/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import static fr.insalyon.creatis.vip.query.client.rpc.QueryService.SERVICE_URI;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public interface EndPointSparqlService extends RemoteService {

    public static final String SERVICE_URI = "/endPointService";

    public static class Util {

        public static EndPointSparqlServiceAsync getInstance() {

            EndPointSparqlServiceAsync instance = (EndPointSparqlServiceAsync) GWT.create(EndPointSparqlService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    String getUrlResult(String param1, String param2);
    List<String[]> getUrlResultFormatTable(String param1,String val1, String val2);
    List<String[]> getUrlResultFormatTable(String param1,String val1, String val2, String val3,String val4);
}
