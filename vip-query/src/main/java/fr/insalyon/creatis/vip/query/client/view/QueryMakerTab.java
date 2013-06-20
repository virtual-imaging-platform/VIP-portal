/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.query.client.view.monitor.QueryLayout;
import fr.insalyon.creatis.vip.query.client.view.monitor.VersionLayout;
import fr.insalyon.creatis.vip.query.client.view.monitor.ParameterLayout;
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
        HLayout qLayout = new HLayout();
        qLayout.setHeight("50%");
        
        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        this.setPane(vLayout);
        
        
        
        queryLayout=new QueryLayout();
       
        createQuery=new CreateQuery();
       
        
        vLayout.addMember(queryLayout);
        vLayout.addMember(createQuery);
   
        
        
     }
    
}

