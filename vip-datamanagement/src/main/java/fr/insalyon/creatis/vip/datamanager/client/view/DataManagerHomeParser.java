package fr.insalyon.creatis.vip.datamanager.client.view;

import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class DataManagerHomeParser extends ApplicationParser {

    @Override
    public void loadApplications() {
        addApplication(DataManagerConstants.APP_FILE_TRANSFER, 
                DataManagerConstants.APP_IMG_FILE_TRANSFER);
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(DataManagerConstants.APP_FILE_TRANSFER)) {
            ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
            return true;
        }
        return false;
    }
}
