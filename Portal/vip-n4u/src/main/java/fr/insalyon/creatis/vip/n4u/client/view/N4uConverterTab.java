/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;
import fr.insalyon.creatis.vip.n4u.client.rpc.FileProcessService;
import fr.insalyon.creatis.vip.n4u.client.view.N4uImportTab.InputType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class N4uConverterTab extends Tab {

    private HLayout layout;
    private LayoutConverterOption1 layoutOption1;
    private LayoutConverterOption2 layoutOption2;
    final TabSet topTabSet;

    public N4uConverterTab() {
        topTabSet = new TabSet(); 
        this.setTitle(Canvas.imgHTML(N4uConstants.ICON_EXPRESSLANE1) + " ExpressLane2VIP");
        this.setID(N4uConstants.TAB_EXPRESSLANE_1);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
        this.setPane(topTabSet);
    }

    public void configure() {
         
        topTabSet.setTabBarPosition(Side.LEFT);  
        topTabSet.setWidth100();  
        topTabSet.setHeight100();
        Tab tTab1 = new Tab("EXPRESS,JOB... FILES",N4uConstants.ICON_FILES );  
       
        layoutOption1 = new LayoutConverterOption1("100%", "100%");
        tTab1.setPane(layoutOption1);
  
        Tab tTab2 = new Tab("XML FILE", N4uConstants.ICON_FILE);      
        layoutOption2 = new LayoutConverterOption2("100%", "100%");
        tTab2.setPane(layoutOption2);  
        
        
  
        topTabSet.addTab(tTab1);  
        topTabSet.addTab(tTab2); 
        
        
        
        layout = new HLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMargin(6);
        layout.setMembersMargin(5);
       
        
        layout.addMember(layoutOption1);
        layout.addMember(layoutOption2);


    }
}