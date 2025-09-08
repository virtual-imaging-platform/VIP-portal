package fr.insalyon.creatis.vip.core.client.view.system.setting;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

/**
 *
 * @author Nouha Boujelben
 */
public class ManageSettingTab extends AbstractManageTab {

    private SettingTermsUseLayout settingTermsUseLayout;

    public ManageSettingTab() {

        super(CoreConstants.ICON_SETTING, CoreConstants.APP_SETTING, CoreConstants.TAB_MANAGE_SETTING);

        settingTermsUseLayout = new SettingTermsUseLayout();


        HLayout hLayout = new HLayout(5);

        VLayout leftLayout = new VLayout(14);
        leftLayout.setWidth(350);
        leftLayout.setHeight100();
        leftLayout.addMember(settingTermsUseLayout);
        
        VLayout rightLayout = new VLayout(14);
        rightLayout.setWidth(350);
        rightLayout.setHeight100();

        hLayout.addMember(leftLayout);
        hLayout.addMember(rightLayout);

        vLayout.setPadding(10);
        vLayout.addMember(hLayout);

    }
}
