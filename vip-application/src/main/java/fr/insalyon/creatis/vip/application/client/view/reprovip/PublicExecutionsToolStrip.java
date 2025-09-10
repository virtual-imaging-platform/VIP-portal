package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class PublicExecutionsToolStrip extends ToolStrip {

    public PublicExecutionsToolStrip() {
        
        this.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton("Refresh");
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ReproVipTab reproVipTab = (ReproVipTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_REPROVIP);
                reproVipTab.loadPublicExecutions();
            }
        });
        this.addButton(refreshButton);
    }
}
