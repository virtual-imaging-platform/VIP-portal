package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;


public class ReproVipTab extends AbstractManageTab {

    private PublicExecutionsLayout publicExecutionsLayout;

    public ReproVipTab() {

        super(ApplicationConstants.ICON_APPLICATION, ApplicationConstants.APP_REPRO_VIP, ApplicationConstants.TAB_REPROVIP);

        publicExecutionsLayout = new PublicExecutionsLayout();

        HLayout appLayout = new HLayout(5);
        appLayout.setHeight("50%");
        appLayout.addMember(publicExecutionsLayout);
        vLayout.addMember(appLayout);
    }

}
