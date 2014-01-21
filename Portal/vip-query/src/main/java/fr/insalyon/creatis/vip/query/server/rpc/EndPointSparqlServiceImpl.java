/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.utils.URIBuilder;
import java.lang.Object.*;
import java.net.URI;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import fr.insalyon.creatis.vip.query.server.dao.business.QueryData;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nouha
 */
public class EndPointSparqlServiceImpl extends RemoteServiceServlet
        implements EndPointSparqlService {
 private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EndPointSparqlServiceImpl.class);
    public String getUrlResult(String param1, String param2) {
        String url = null;
        String k = null;



        URIBuilder builder = new URIBuilder();
        builder.setHost("ginseng.unice.fr");
        builder.setPort(9000);
        builder.setPath("/sparql");

        builder.setParameter("query", param1);

        builder.setParameter("format", param2);
        try {
            url = builder.build().toString();
            

        } catch (URISyntaxException ex) {
            Logger.getLogger(EndPointSparqlServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return url;
        
    }
    
    
   
    
     public List<String[]> getUrlResultFormatTable(String param1) {
       
   
         Query query = QueryFactory.create(param1);
         QueryExecution qexec = QueryExecutionFactory.sparqlService("http://ginseng.i3s.unice.fr:9000/sparql", param1);
         ResultSet results = qexec.execSelect();
         List<String[]> rslt = new ArrayList<String[]>();
        

         while (results.hasNext()) {
             QuerySolution row = results.next();
           
              rslt.add(new String[]{row.get("x").toString(),row.get("label").toString()});
           
         }
       
        

        return rslt;
        
    }
    
}
