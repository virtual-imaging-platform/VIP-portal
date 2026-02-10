package fr.insalyon.creatis.vip.core.client.view.main;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.system.group.ManageGroupsTab;
import fr.insalyon.creatis.vip.core.client.view.system.setting.ManageSettingTab;
import fr.insalyon.creatis.vip.core.client.view.system.user.ManageUsersTab;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SystemParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        if (CoreModule.user.isSystemAdministrator()) {
            addApplication(CoreConstants.APP_USER, CoreConstants.APP_IMG_USER);
            addApplication(CoreConstants.APP_GROUP, CoreConstants.APP_IMG_GROUP);
            addApplication(CoreConstants.APP_SETTING, CoreConstants.APP_IMG_SETTING);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {
        if (applicationName.equals(CoreConstants.APP_USER)) {
            Layout.getInstance().addTab(
                CoreConstants.TAB_MANAGE_USERS, ManageUsersTab::new);
            return true;
        } else if (applicationName.equals(CoreConstants.APP_GROUP)) {
            Layout.getInstance().addTab(
                CoreConstants.TAB_MANAGE_GROUPS, ManageGroupsTab::new);
            return true;
        }  else if (applicationName.equals(CoreConstants.APP_SETTING)) {
            Layout.getInstance().addTab(
                CoreConstants.TAB_MANAGE_SETTING, ManageSettingTab::new);
            return true;
        }
        return false;
    }
}
