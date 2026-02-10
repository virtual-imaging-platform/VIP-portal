package fr.insalyon.creatis.vip.gatelab.client;

import com.smartgwt.client.widgets.tab.Tab;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationModule;
import fr.insalyon.creatis.vip.application.client.inter.CustomApplicationModule;
import fr.insalyon.creatis.vip.application.client.view.launch.RelaunchService;
import fr.insalyon.creatis.vip.application.client.view.monitor.MonitorParser;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineParser;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab;
import fr.insalyon.creatis.vip.gatelab.client.view.monitor.GateLabMonitorParser;
import fr.insalyon.creatis.vip.gatelab.client.view.monitor.GateLabTimelineParser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabModule extends Module implements CustomApplicationModule {

    public GateLabModule() { }

    @Override
    public void load() {
        ApplicationModule.customModules.add(this);
        TimelineParser.getInstance().addParser(new GateLabTimelineParser());
        MonitorParser.getInstance().addParser(new GateLabMonitorParser());
        RelaunchService.getInstance().addApplicationRelauncher(new RelaunchService.ApplicationRelauncher() {
            @Override
            public boolean relaunchIfSupported(
                    String applicationName, String applicationVersion, String applicationClass,
                    String simulationName, Map<String, String> inputs, String tabId) {
                if (applicationName.toLowerCase().contains(GateLabConstants.TAG_GATELAB.toLowerCase())) {
                    Layout.getInstance().addTab(
                            tabId,
                            () -> new GateLabLaunchTab(applicationName, applicationVersion, applicationClass, simulationName, inputs));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void postLoading() { }

    @Override
    public void terminate(Set<Tab> removedTabs) { }

    @Override
    public boolean doOverride(List<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getKey().equalsIgnoreCase(GateLabConstants.TAG_GATELAB) && tag.getValue().equalsIgnoreCase("True")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run(String appName, String appVersion, String tileName) {
        Layout.getInstance().addTab(
            ApplicationConstants.getLaunchTabID(appName),
            () -> new GateLabLaunchTab(appName, appVersion, tileName));
    }
}
