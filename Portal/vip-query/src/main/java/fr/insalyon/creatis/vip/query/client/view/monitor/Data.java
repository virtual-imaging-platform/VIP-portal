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
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 *
 * @author Nouha Boujelben
 */
public class Data extends DataSource {

    public Data() {

        DataSourceIntegerField id = new DataSourceIntegerField("queryExecutionID", "queryExecutionID");
        id.setPrimaryKey(true);
        id.setHidden(true);

        DataSourceTextField version = new DataSourceTextField("version", "Version");
        
        DataSourceTextField status = new DataSourceTextField("status", "Status");
        
        DataSourceTextField statusFormatter = new DataSourceTextField("statusFormatter", "Status");
        
        DataSourceTextField name = new DataSourceTextField("name", "Query Execution Name");
        
        DataSourceTextField query = new DataSourceTextField("query", "Query");
        
        DataSourceTextField executer = new DataSourceTextField("executer", "Executer");
        
        DataSourceDateTimeField date = new DataSourceDateTimeField("dateExecution", "Execution Start Time");
        
        DataSourceDateTimeField dateEnd = new DataSourceDateTimeField("dateEndExecution", "Execution End Time");
        
        DataSourceTextField urlResult = new DataSourceTextField("urlResult", "Result Data");
        urlResult.setHidden(Boolean.TRUE);
        
        DataSourceTextField pathFileResult = new DataSourceTextField("pathFileResult", "pathFileResult");
        pathFileResult.setHidden(Boolean.TRUE);
        
        setFields(id, statusFormatter, date, version, executer, status, name, query, urlResult, pathFileResult, dateEnd);
        setClientOnly(true);


    }
}
