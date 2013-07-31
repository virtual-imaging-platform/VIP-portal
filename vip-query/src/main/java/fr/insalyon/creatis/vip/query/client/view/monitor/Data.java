/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 *
 * @author nouha
 */

     public class Data extends DataSource {

   
	
	public Data() {
		
		DataSourceIntegerField id = new DataSourceIntegerField("queryExecutionID", "queryExecutionID");
		
		id.setPrimaryKey(true);
                id.setHidden(true);
		DataSourceTextField version = new DataSourceTextField("version", "Version");
		
                DataSourceTextField status = new DataSourceTextField("status", "Status");
		
                DataSourceTextField name = new DataSourceTextField("name", "Query Execution Name");
		
                DataSourceTextField query = new DataSourceTextField("query", "Query");
		
                DataSourceTextField executer = new DataSourceTextField("executer", "Executer");
		
               DataSourceDateTimeField date = new DataSourceDateTimeField("dateExecution", "Execution Start Time");
		 DataSourceTextField urlResult = new DataSourceTextField("urlResult", "Result Data");
                setFields(id,version,status,name,query,executer,date,urlResult);
		setClientOnly(true);
		
		
		
	}
     }
