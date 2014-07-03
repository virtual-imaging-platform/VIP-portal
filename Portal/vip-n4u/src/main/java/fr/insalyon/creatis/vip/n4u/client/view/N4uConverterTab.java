/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 *
 * @author Nouha Boujelben
 */
public class N4uConverterTab extends Tab {

    private HLayout layout;
    private LayoutConverterOption1 layoutOption1;
    private LayoutConverterOption2 layoutOption2;
    final TabSet topTabSet;

    /**
     *
     */
    public N4uConverterTab() {
        topTabSet = new TabSet();
        this.setTitle(Canvas.imgHTML(N4uConstants.ICON_EXPRESSLANE1) + " ExpressLane2VIP");
        this.setID(N4uConstants.TAB_EXPRESSLANE_1);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
        this.setPane(topTabSet);
    }

    /**
     *
     */
    private void configure() {

        topTabSet.setTabBarPosition(Side.LEFT);
        topTabSet.setWidth100();
        topTabSet.setHeight100();
        Tab tTab1 = new Tab("EXPRESS,JOB... FILES", N4uConstants.ICON_FILES);
        tTab1.setPrompt("Job, Express, Script, Extension and  Environement Files");
        layoutOption1 = new LayoutConverterOption1("100%", "100%");
        tTab1.setPane(layoutOption1);

        Tab tTab2 = new Tab("Xml File", N4uConstants.ICON_FILE);
        tTab2.setPrompt("Xml File");
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