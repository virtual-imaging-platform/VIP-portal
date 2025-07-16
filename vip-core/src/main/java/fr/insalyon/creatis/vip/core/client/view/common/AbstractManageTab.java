package fr.insalyon.creatis.vip.core.client.view.common;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class AbstractManageTab extends Tab {

    protected ToolStrip toolStrip;
    protected VLayout vLayout;

    public AbstractManageTab(String icon, String title, String tabID) {

        this.setTitle(Canvas.imgHTML(icon) + " " + title);
        this.setID(tabID);
        this.setCanClose(true);

        vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);

        this.setPane(vLayout);
    }
}
