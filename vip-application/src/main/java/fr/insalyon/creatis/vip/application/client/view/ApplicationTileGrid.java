package fr.insalyon.creatis.vip.application.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationModule;
import fr.insalyon.creatis.vip.application.client.inter.CustomApplicationModule;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.models.Pair;

public class ApplicationTileGrid extends ApplicationsTileGrid {

    private List<String> applicationNames;

    public ApplicationTileGrid(String name, List<AppVersion> versions) {
        super(name);
        applicationNames = new ArrayList<String>();

        for (AppVersion version : versions) {
            addApplication(version.getApplicationName(), version.getVersion(), ApplicationConstants.APP_IMG_APPLICATION);
            applicationNames.add(version.getApplicationName() + " " + version.getVersion());
        }
    }

    @Override
    public void parse(final String applicationName, final String applicationVersion) {
        final AsyncCallback<Pair<Boolean, String>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to check application avaibility:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Pair<Boolean, String> result) {
                if (result.getFirst()) {
                    load(applicationName, applicationVersion);
                } else {
                    Layout.getInstance().setWarningMessage(result.getSecond());
                }
            }
        };
        ApplicationService.Util.getInstance().isAppUsableWithCurrentUser(applicationName, applicationVersion, callback);
    }

    private void load(String appName, String appVersion) {
        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load the application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Tag> result) {
                display(appName, appVersion, result);
            }
        };
        ApplicationService.Util.getInstance().getTags(new AppVersion(appName, appVersion, "", false), callback);
    }


    private void display(final String applicationName, final String applicationVersion, final List<Tag> tags) {
        String appName = applicationVersion == null ? applicationName : applicationName + " " + applicationVersion;
        CustomApplicationModule custom = retrieveView(tags);

        if (applicationNames.contains(appName)) {
            if (custom != null) {
                custom.run(applicationName, applicationVersion, tileName);
            } else {
                Layout.getInstance().addTab(
                    ApplicationConstants.getLaunchTabID(applicationName),
                    () -> new LaunchTab(
                        applicationName, applicationVersion, tileName));
            }
        }
    }

    private CustomApplicationModule retrieveView(List<Tag> tags) {
        for (CustomApplicationModule custom : ApplicationModule.customModules) {
            if (custom.doOverride(tags)) {
                return custom;
            }
        }
        return null;
    }
}
