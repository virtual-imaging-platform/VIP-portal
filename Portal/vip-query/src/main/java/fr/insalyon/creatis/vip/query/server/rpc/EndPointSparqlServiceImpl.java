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
package fr.insalyon.creatis.vip.query.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.utils.URIBuilder;
import java.lang.Object.*;
import java.net.URI;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.query.server.dao.business.QueryData;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nouha Boujelben
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
    
    
   
    
     public List<String[]> getUrlResultFormatTable(String val1, String val2) {
       
   
         Query query = QueryFactory.create(Server.getInstance().getQueryTree());
         QueryExecution qexec = QueryExecutionFactory.sparqlService("http://ginseng.i3s.unice.fr:9000/sparql", Server.getInstance().getQueryTree());
         ResultSet results = qexec.execSelect();
         List<String[]> rslt = new ArrayList<String[]>();
        

         while (results.hasNext()) {
             QuerySolution row = results.next();
             rslt.add(new String[]{row.get(val1).toString(),row.get(val2).toString()});    
             }
           
        return rslt;
        
    }
     
     
     
      public List<String[]> getUrlResultFormatTable(String param1,String val1, String val2,String val3,String val4) {
       
   try{
       
         Query query = QueryFactory.create(param1);
        
         QueryExecution qexec = QueryExecutionFactory.sparqlService("http://ginseng.i3s.unice.fr:9000/sparql", param1);
       
         ResultSet results = qexec.execSelect();
       
         List<String[]> rslt = new ArrayList<String[]>();
        
       
         while (results.hasNext()) {
             QuerySolution row = results.next();
          
             String valueVal1;
             String valueVal2;
             String valueVal3;
             String valueVal4;
             try {
                 valueVal1 = row.get(val1).toString();
                 valueVal2 = row.get(val2).toString();
                 valueVal3 = row.get(val3).toString();
                 valueVal4 = row.get(val4).toString();
             } catch (Exception ex) {
                  valueVal1 = "empty";
                  valueVal2 = "empty";
                  valueVal3 = "empty";
                  valueVal4 = "empty";
                   logger.error(ex);
             }  
             
             rslt.add(new String[]{valueVal1,valueVal2,valueVal3,valueVal4});
             logger.info(row.get(val3).toString());
          
             }
           return rslt;
        
         } catch (Exception ex) {
        
         logger.info(ex);
         return null;
         }
        
    }
    
}
