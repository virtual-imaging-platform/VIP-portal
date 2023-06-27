package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;


public class ReproVipTab extends AbstractManageTab {
    private ExecutionsLayout executionsLayout;

    public ReproVipTab() {

        super(ApplicationConstants.ICON_APPLICATION, ApplicationConstants.APP_REPRO_VIP, ApplicationConstants.TAB_REPROVIP);

        executionsLayout = new ExecutionsLayout();

        HLayout appLayout = new HLayout(5);
        appLayout.setHeight("50%");
        appLayout.addMember(executionsLayout);
        vLayout.addMember(appLayout);
    }

    public void loadApplications() {
        executionsLayout.loadData();
    }

}
