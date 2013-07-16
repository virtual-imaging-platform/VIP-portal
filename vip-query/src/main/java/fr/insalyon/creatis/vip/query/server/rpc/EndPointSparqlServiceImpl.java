/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.rpc;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;

/**
 *
 * @author nouha
 */
public class EndPointSparqlServiceImpl extends RemoteServiceServlet
   implements EndPointSparqlService {
    
    
    public String getUrlResult(String param1,String param2){    
    
           UrlBuilder builder = new UrlBuilder();
            
           builder.setHost("ginseng.unice.fr");
           builder.setPort(9000);
           builder.setPath("/sparql"); 
       
        /* builder.setParameter("query","select ?type (count(*) as ?c)" 
                 + " where {?x a ?type}"
                 + " group by ?type"
                 + " order by desc(?c)"); 
                 * */
           builder.setParameter(param1);
           builder.setParameter(param2);
        // builder.setParameter("format","json" );
          return builder.buildString();
          
    }
          
           //com.google.gwt.user.client.Window.open(builder.buildString(),"_self","");  
    
    
}
