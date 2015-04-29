/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client.view;

import fr.insalyon.creatis.vip.core.client.CoreModule;

import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Nouha Boujelben
 */
public class ApplicationImporterHomeParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.hasGroupAccess(ApplicationImporterConstants.APPLICATION_IMPORTER_GROUP)) {
            addApplication(ApplicationImporterConstants.APP_APPLICATION_IMPORTER, ApplicationImporterConstants.APP_IMG_IMPORTER);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(ApplicationImporterConstants.APP_APPLICATION_IMPORTER)) {
            Layout.getInstance().addTab(new ApplicationImporterConverterTab());
            return true;
        }
        return false;
    }
}
