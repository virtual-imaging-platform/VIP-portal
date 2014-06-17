/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.user.publication;


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

        DataSourceIntegerField id = new DataSourceIntegerField("id", "Id");
        id.setPrimaryKey(true);
        id.setHidden(true);

        DataSourceTextField title = new DataSourceTextField("title", "Title");
        
        DataSourceTextField type = new DataSourceTextField("type", "type");
        
        DataSourceTextField typeName = new DataSourceTextField("typeName", "typeName");
        
        DataSourceTextField date = new DataSourceTextField("date", "date");
        
        DataSourceTextField authors = new DataSourceTextField("authors", "authors");
        
        DataSourceTextField doi = new DataSourceTextField("doi", "doi");
             
        setFields(id,title,type,typeName, date,authors,doi );
        setClientOnly(true);


    }
}
