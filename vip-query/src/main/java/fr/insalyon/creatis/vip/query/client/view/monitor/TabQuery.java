/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.smartgwt.client.widgets.tab.TabSet;import com.smartgwt.client.types.Side;  
import com.smartgwt.client.types.TabTitleEditEvent;  
import com.smartgwt.client.util.SC;  
import com.smartgwt.client.widgets.Canvas;  
import com.smartgwt.client.widgets.IButton;  
import com.smartgwt.client.widgets.Img;  
import com.smartgwt.client.widgets.events.ClickEvent;  
import com.smartgwt.client.widgets.events.ClickHandler;  
import com.smartgwt.client.widgets.layout.HLayout;  
import com.smartgwt.client.widgets.layout.VLayout;  
import com.smartgwt.client.widgets.tab.Tab;  
import com.smartgwt.client.widgets.tab.TabSet;  
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;  
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;  
import com.smartgwt.client.widgets.tab.events.TabTitleChangedEvent;  
import com.smartgwt.client.widgets.tab.events.TabTitleChangedHandler;  


/**
 *
 * @author nouha
 */
public class TabQuery extends TabSet{
    private final CreateQuery createQuery;
    private final CheckboxTree checkboxTree;

    public TabQuery() {
        createQuery=new CreateQuery();
        checkboxTree=new CheckboxTree();
        this.setTabBarPosition(Side.TOP);  
        this.setTabBarAlign(Side.LEFT);  
       
        //this.setHeight(200);  
  
        this.setCanEditTabTitles(true);  
        this.setTitleEditEvent(TabTitleEditEvent.DOUBLECLICK);  
        this.setTitleEditorTopOffset(2);
        Tab tab1 = new Tab();
        Tab tab2 = new Tab();
        tab1.setPane(createQuery);
        tab2.setPane(checkboxTree);
        this.addTab(tab1);
        this.addTab(tab2);
    
    }

    public CreateQuery getCreateQuery() {
        return createQuery;
    }

    public CheckboxTree getCheckboxTree() {
        return checkboxTree;
    }
    
    
    
    
    
}
