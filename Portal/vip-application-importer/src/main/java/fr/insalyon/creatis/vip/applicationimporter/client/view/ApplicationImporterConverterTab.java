/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
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