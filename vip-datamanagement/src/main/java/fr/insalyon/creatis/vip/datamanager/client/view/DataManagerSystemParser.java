package fr.insalyon.creatis.vip.datamanager.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.system.cache.ManageCachedFilesTab;
import fr.insalyon.creatis.vip.datamanager.client.view.system.operation.ManageOperationsTab;

public class DataManagerSystemParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        if (CoreModule.user.isSystemAdministrator()) {
            addApplication(DataManagerConstants.APP_OPERATIONS, DataManagerConstants.APP_IMG_OPERATIONS);
            addApplication(DataManagerConstants.APP_CACHED_FILES, DataManagerConstants.APP_IMG_CACHED_FILES);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(DataManagerConstants.APP_OPERATIONS)) {
            Layout.getInstance().addTab(
                DataManagerConstants.TAB_MANAGE_OPERATIONS,
                ManageOperationsTab::new);
            return true;

        } else if (applicationName.equals(DataManagerConstants.APP_CACHED_FILES)) {
            Layout.getInstance().addTab(
                DataManagerConstants.TAB_MANAGE_CACHED_FILES,
                ManageCachedFilesTab::new);
            return true;
        }
        return false;
    }
}
