package fr.insalyon.creatis.vip.core.client.view.main;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;

/**
 *
 * @author Rafael Silva
 */
public class HomeTab extends Tab {

    private VLayout leftLayout;
    private VLayout rightLayout;
    private HLayout hLayout;

    public HomeTab() {

        this.setTitle("Home");
        this.setID(CoreConstants.TAB_HOME);
        this.setIcon(CoreConstants.ICON_HOME);
        

        hLayout = new HLayout(10);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);

        leftLayout = new VLayout();
        leftLayout.setWidth100();
        leftLayout.setHeight100();
        leftLayout.setOverflow(Overflow.AUTO);
        hLayout.addMember(leftLayout);

        rightLayout = new VLayout();
        rightLayout.setWidth(380);
        rightLayout.setHeight100();
        rightLayout.setOverflow(Overflow.VISIBLE);
       

        this.setPane(hLayout);
    }

    public void addTileGrid(ApplicationsTileGrid tileGrid) {

        Label tileName = new Label("<b>" + tileGrid.getTileName() + "</b>");
        tileName.setAlign(Alignment.LEFT);
        tileName.setWidth100();
        tileName.setHeight(20);
        tileName.setBackgroundColor("#F2F2F2");

        leftLayout.addMember(tileName);
        leftLayout.addMember(tileGrid);
    }

    public void addToRightLayout(Layout layout) {
        rightLayout.addMember(layout);
         hLayout.addMember(rightLayout);
    }
}
