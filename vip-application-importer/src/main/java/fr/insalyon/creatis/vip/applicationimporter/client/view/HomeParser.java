package fr.insalyon.creatis.vip.applicationimporter.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Nouha Boujelben
 */
public class HomeParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        if (CoreModule.user.isSystemAdministrator()|| CoreModule.user.isDeveloper() || CoreModule.user.hasGroupAccess(Constants.APPLICATION_IMPORTER_GROUP)) {
            addApplication(Constants.APP_APPLICATION_IMPORTER, Constants.APP_IMG_IMPORTER);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(Constants.APP_APPLICATION_IMPORTER)) {
            Layout.getInstance().addTab(
                Constants.TAB_ID_BOUTIQUES, ImportTab::new);
            return true;
        }
        return false;
    }
}
