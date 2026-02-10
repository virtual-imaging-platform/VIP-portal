package fr.insalyon.creatis.vip.application.client.view;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationsTab;
import fr.insalyon.creatis.vip.application.client.view.reprovip.ReproVipTab;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationHomeParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        addApplication(ApplicationConstants.APP_MONITOR,
                ApplicationConstants.APP_IMG_MONITOR);

        addApplication(ApplicationConstants.APP_PUBLIC_APPLICATION,
                ApplicationConstants.APP_IMG_APPLICATION);

        if (CoreModule.user.isSystemAdministrator()  || CoreModule.user.isDeveloper()) {
            addApplication(ApplicationConstants.APP_REPRO_VIP,
                    ApplicationConstants.APP_IMG_APPLICATION);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(ApplicationConstants.APP_MONITOR)) {
            Layout.getInstance().addTab(
                ApplicationConstants.TAB_MONITOR, SimulationsTab::new);
            return true;
        }
        if (applicationName.equals(ApplicationConstants.APP_PUBLIC_APPLICATION)) {
            Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_APPLICATION,
                    () -> new ManageApplicationsTab(true));
            return true;
        }
        if (applicationName.equals(ApplicationConstants.APP_REPRO_VIP)) {
            Layout.getInstance().addTab(
                    ApplicationConstants.TAB_REPROVIP,
                    ReproVipTab::new);
            return true;
        }
        return false;
    }
}
