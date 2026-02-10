package fr.insalyon.creatis.vip.application.client.view;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.application.client.view.system.engines.ManageEnginesTab;
import fr.insalyon.creatis.vip.application.client.view.system.resources.ManageResourcesTab;
import fr.insalyon.creatis.vip.application.client.view.system.tags.ManageTagsTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationSystemParser extends ApplicationParser {

    @Override
    public void loadApplications() {
        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isDeveloper()) {
            addApplication(ApplicationConstants.APP_APPLICATION, ApplicationConstants.APP_IMG_APPLICATION);
        }
        if (CoreModule.user.isSystemAdministrator()) {
            addApplication(ApplicationConstants.APP_ENGINE, ApplicationConstants.APP_IMG_ENGINE);
            addApplication(ApplicationConstants.APP_RESOURCE, ApplicationConstants.APP_IMG_RESOURCE);
            addApplication(ApplicationConstants.APP_TAG, ApplicationConstants.APP_IMG_TAG);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {
        switch (applicationName) {
            case ApplicationConstants.APP_APPLICATION:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_APPLICATION,
                    () -> new ManageApplicationsTab(false));
                return true;

            case ApplicationConstants.APP_ENGINE:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_ENGINE, ManageEnginesTab::new);
                return true;

            case ApplicationConstants.APP_RESOURCE:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_RESOURCE, ManageResourcesTab::new);
                return true;

            case ApplicationConstants.APP_TAG:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_TAG, ManageTagsTab::new);
                return true;

            default:
                return false;
        }
    }
}
