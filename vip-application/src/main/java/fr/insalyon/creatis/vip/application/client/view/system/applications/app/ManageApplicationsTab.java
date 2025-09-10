package fr.insalyon.creatis.vip.application.client.view.system.applications.app;

import java.util.Map;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.system.applications.version.EditVersionLayout;
import fr.insalyon.creatis.vip.application.client.view.system.applications.version.ManageVersionLayout;
import fr.insalyon.creatis.vip.application.client.view.system.applications.version.VersionsLayout;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class ManageApplicationsTab extends AbstractManageTab {

    private ApplicationsLayout appsLayout;
    private EditApplicationLayout editLayout;
    private VersionsLayout versionsLayout;
    private EditVersionLayout editVersionLayout;
    private ManageVersionLayout manageVersionLayout;
    private Label infoAppText;

    public ManageApplicationsTab(boolean onlyPublicApps) {

        super(ApplicationConstants.ICON_APPLICATION, ApplicationConstants.APP_APPLICATION, ApplicationConstants.TAB_MANAGE_APPLICATION);

        appsLayout = new ApplicationsLayout(onlyPublicApps);

        titleInfoApp();
        HLayout appLayout = new HLayout(5);
        appLayout.setHeight("50%");
        appLayout.addMember(appsLayout);
        appLayout.setOverflow(Overflow.SCROLL);

        vLayout.addMember(infoAppText);
        vLayout.addMember(appLayout);

        if( ! onlyPublicApps ) {
            editLayout = new EditApplicationLayout();
            versionsLayout = new VersionsLayout();
            editVersionLayout = new EditVersionLayout();
            manageVersionLayout = new ManageVersionLayout();
            appLayout.addMember(editLayout);
            VLayout versionInfoLayout = new VLayout(5);

            HLayout versionLayout = new HLayout(5);
            versionLayout.setOverflow(Overflow.SCROLL);
            versionLayout.setHeight("50%");
            versionLayout.addMember(versionsLayout);

            versionInfoLayout.addMember(editVersionLayout);
            versionInfoLayout.addMember(manageVersionLayout);
            versionLayout.addMember(versionInfoLayout);
            vLayout.addMember(versionLayout);
        }
    }

    public void loadApplications() {
        appsLayout.loadData();
    }

    public void loadVersions(String applicationName) {
        versionsLayout.setApplication(applicationName);
        editVersionLayout.setApplication(applicationName);
        manageVersionLayout.setApplication(applicationName);
    }

    public void setApplication(String name, String owner, String citation, Map<String, String> groups) {
        editLayout.setApplication(name, owner, citation, groups);
    }

    public void setVersion(String version, String descriptor, String doi, Map<String, String> settings, 
            boolean isVisible, String source, String[] resources) {
        editVersionLayout.setVersion(version, descriptor, isVisible, source, settings, resources);
        manageVersionLayout.setVersion(version, descriptor, doi);
    }

    private void titleInfoApp(){
        infoAppText = WidgetUtil.getLabel("<font size=\"3\"><b> This is a table containing " +
                "all the applications that are publicly available on VIP.</b></font>", 20);
    }
}
