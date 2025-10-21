package fr.insalyon.creatis.vip.application.client;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.inter.CustomApplicationModule;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationHomeParser;
import fr.insalyon.creatis.vip.application.client.view.ApplicationSystemParser;
import fr.insalyon.creatis.vip.application.client.view.ApplicationTileGrid;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineLayout;
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.CenterTabSet;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationModule extends Module {

    public static List<CustomApplicationModule> customModules;
    private Map<String, List<AppVersion>> userTagApps;

    public ApplicationModule() {
        customModules = new ArrayList<>();
        userTagApps = new HashMap<>();

        CoreModule.getHomePageActions().put(CoreConstants.HOME_ACTION_SHOW_APPLICATIONS, new Runnable() {
            @Override
            public void run() {
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_APPLICATION,
                    () -> new ManageApplicationsTab(true));
            }
        });
    }

    @Override
    public void load() {
        final AsyncCallback<Map<Application, List<AppVersion>>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load users groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<Application, List<AppVersion>> apps) {
                apps.forEach((app, versions) -> {
                    versions.forEach((version) -> {
                        String classTag = version.getTags().stream()
                            .filter(t -> t.getKey().equalsIgnoreCase("class"))
                            .findFirst()
                            .map(Tag::getValue)
                            .orElse("Others");

                        if (version.isVisible()) {
                            userTagApps.computeIfAbsent(classTag, k -> new ArrayList<>()).add(version);
                        }
                    });
                });
                render();
            }
        };
        ApplicationService.Util.getInstance().getApplications(callback);
    }

    private void render() {
        Layout.getInstance().removeTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
        
        CoreModule.addGeneralApplicationParser(new ApplicationHomeParser());
        CoreModule.addSystemApplicationParser(new ApplicationSystemParser());
        CoreModule.addLayoutToHomeTab(TimelineLayout.getInstance());

        userTagApps.keySet().stream().sorted().forEach(name ->
                CoreModule.addApplicationsTileGrid(new ApplicationTileGrid(name, userTagApps.get(name))));

        // Simulation close tab
        CenterTabSet.getInstance().addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(TabCloseClickEvent event) {
                Tab tab = event.getTab();
                try {
                    ((AbstractSimulationTab) tab).destroy();
                } catch (ClassCastException ex) {
                    // do nothing
                }
            }
        });
    }

    @Override
    public void postLoading() { }

    @Override
    public void terminate(Set<Tab> removedTabs) {
        TimelineLayout.getInstance().terminate();
        for (Tab tab : removedTabs) {
            if (tab instanceof AbstractSimulationTab) {
                ((AbstractSimulationTab) tab).destroy();
            }
        }
    }

    @Override
    public void userRemoved(User user) {
        final AsyncCallback<Void> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to anonymize user data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
            }
        };
        WorkflowService.Util.getInstance().updateUser(user.getFullName(), "User-" + Random.nextInt(100000), callback);
    }

    @Override
    public void userUpdated(User oldUser, User updatedUser) {
        if ( ! oldUser.getFullName().equals(updatedUser.getFullName())) {
            final AsyncCallback<Void> callback = new AsyncCallback<>() {
                @Override
                public void onFailure(Throwable caught) {
                    Layout.getInstance().setWarningMessage("Unable to anonymize user data:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                }
            };
            WorkflowService.Util.getInstance().updateUser(oldUser.getFullName(), updatedUser.getFullName(), callback);
        }
    }
}
