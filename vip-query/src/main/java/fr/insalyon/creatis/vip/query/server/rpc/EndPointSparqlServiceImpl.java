/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.rpc;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.utils.URIBuilder;
import java.lang.Object.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author nouha
 */
public class EndPointSparqlServiceImpl extends RemoteServiceServlet
   implements EndPointSparqlService {
    
    
    public String getUrlResult(String param1,String param2){  
        String url= null;
       String k=null;
            
         
                
               URIBuilder builder=new URIBuilder();
               builder.setHost("ginseng.unice.fr");
               builder.setPort(9000);
               builder.setPath("/sparql");
       
            builder.setParameter("query",param1) ;
       
               builder.setParameter("format",param2);
        try {
            url=builder.build().toString();
                    /*
                
                       UrlBuilder builder = new UrlBuilder();
                       builder.setHost("ginseng.unice.fr");
                       builder.setPort(9000);
                       builder.setPath("/sparql"); 
                   
                     builder.setParameter("query","select ?type (count(*) as ?c)" 
                             + " where {?x a ?type}"
                             + " group by ?type"
                             + " order by desc(?c)"); 
                            
                       builder.setParameter("query",param1);
                       builder.setParameter("format",param2);
                    // builder.setParameter("format","json" );
                     
                      return builder.toString();
                  */
        } catch (URISyntaxException ex) {
            Logger.getLogger(EndPointSparqlServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            k=URLDecoder.decode(url,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EndPointSparqlServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return k;
    }
    
     
   
    
          
           //;  
    
    
}
