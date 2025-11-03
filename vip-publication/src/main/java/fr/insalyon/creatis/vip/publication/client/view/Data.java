package fr.insalyon.creatis.vip.publication.client.view;


import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

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

        DataSourceTextField vipApplication = new DataSourceTextField("VipApplication", "VipApplication");
        
        DataSourceTextField doi = new DataSourceTextField("doi", "doi");
             
        setFields(id,title,type,typeName, date,authors,vipApplication,doi );
        setClientOnly(true);


    }
}
