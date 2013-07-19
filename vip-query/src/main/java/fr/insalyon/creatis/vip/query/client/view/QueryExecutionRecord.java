/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.sql.Date;

/**
 *
 * @author nouha
 */
public class QueryExecutionRecord extends ListGridRecord {
     public QueryExecutionRecord(String name,String queryName,String version,String executer,String dateExecution, String status, String urlResult){
     setAttribute("name",name);   
     setAttribute("query", queryName);
     setAttribute("version", version);
     setAttribute("executer", executer);
     setAttribute("dateExecution", dateExecution);
     setAttribute("status", status);
     setAttribute("urlResult", urlResult);
       
    }
    
}
