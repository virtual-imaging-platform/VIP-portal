/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.monitor.QueryLayout;


 import fr.insalyon.creatis.vip.query.client.view.monitor.CreateQuery;

/**
 *
 * @author Boujelben
 */
public class QueryMakerTab extends Tab {
    
    private final QueryLayout queryLayout;
    private final CreateQuery createQuery;
    protected VLayout vLayout;
    
    public QueryMakerTab() {
        this.setTitle(Canvas.imgHTML(QueryConstants.ICON_QUERYMAKER) + "Query Maker");
        this.setID(QueryConstants.TAB_QUERYMAKER);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
       
        
        vLayout = new VLayout();
        vLayout.setMargin(5);
        //vLayout.setWidth100();
       // vLayout.setHeight100();
        
        
        this.setPane(vLayout);
        
        
        
        queryLayout=new QueryLayout();
       
        createQuery=new CreateQuery();
       
        
        vLayout.addMember(queryLayout);
        vLayout.addMember(createQuery);
   
        
        
     }
    
     public void setQuery(boolean bodyState,boolean test,String name, String description, String body) {
        createQuery.setQuery(bodyState,test, name, description, body);     
    }
     public String getVersionID(){
         return queryLayout.getVersionID();
     }
    
    public void loadData(){
         queryLayout.loadData();
     }
    
}

