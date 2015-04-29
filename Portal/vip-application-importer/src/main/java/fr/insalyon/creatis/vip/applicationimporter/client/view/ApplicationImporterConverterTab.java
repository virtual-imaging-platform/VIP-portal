/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client.view;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 *
 * @author Nouha Boujelben
 */
public class ApplicationImporterConverterTab extends Tab {

    private HLayout layout;
    private LayoutConverterExpressLaneOption1 layoutOption1;
    private LayoutConverterExpressLaneOption2 layoutOption2;
    private LayoutConverterBoutiques layoutOption3;
    final TabSet topTabSet;

    /**
     *
     */
    public ApplicationImporterConverterTab() {
        topTabSet = new TabSet();
        this.setTitle(Canvas.imgHTML(ApplicationImporterConstants.ICON_IMPORTER) + " Application Importer");
        this.setID(ApplicationImporterConstants.TAB_ID_EXPRESSLANE_1);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
        this.setPane(topTabSet);
    }

    /**
     *
     */
    private void configure() {

        topTabSet.setTabBarPosition(Side.TOP);
        topTabSet.setWidth100();
        topTabSet.setHeight100();
        
        Tab tTab3 = new Tab("Boutiques JSON File", ApplicationImporterConstants.ICON_BOUTIQUES);
        layoutOption3 = new LayoutConverterBoutiques("100%", "100%");
        tTab3.setPane(layoutOption3);
        
        Tab tTab1 = new Tab("ExpressLane Files", ApplicationImporterConstants.ICON_EXPRESSLANE2VIP);
        layoutOption1 = new LayoutConverterExpressLaneOption1("100%", "100%");
        tTab1.setPane(layoutOption1);

        Tab tTab2 = new Tab("XML ExpressLane File", ApplicationImporterConstants.ICON_EXPRESSLANE2VIP);  
        layoutOption2 = new LayoutConverterExpressLaneOption2("100%", "100%");
        tTab2.setPane(layoutOption2);
        
       
        
        topTabSet.addTab(tTab3);
        topTabSet.addTab(tTab1);     
        topTabSet.addTab(tTab2);
        

        layout = new HLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMargin(6);
        layout.setMembersMargin(5);
        layout.addMember(layoutOption1);
        layout.addMember(layoutOption2);
        layout.addMember(layoutOption3);


    }
}