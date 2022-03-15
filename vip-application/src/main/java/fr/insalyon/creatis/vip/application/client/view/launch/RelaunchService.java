package fr.insalyon.creatis.vip.application.client.view.launch;

import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelaunchService {

    public interface ApplicationRelauncher {
        boolean relaunchIfSupported(
                String applicationName, String applicationVersion, String applicationClass,
                String simulationName, Map<String,String> inputs, String tabId);
    }

    private static RelaunchService instance;
    private List<ApplicationRelauncher> applicationRelaunchers = new ArrayList<>();

    public static RelaunchService getInstance() {
        if (instance == null) {
            instance = new RelaunchService();
        }
        return instance;
    }

    public void addApplicationRelauncher(ApplicationRelauncher applicationRelauncher) {
        applicationRelaunchers.add(applicationRelauncher);
    }

    public void relaunch(
            String applicationName, String applicationVersion, String applicationClass, String simulationName,
            Map<String,String> inputs, String tabId) {
        for (ApplicationRelauncher applicationRelauncher : applicationRelaunchers) {
            if (applicationRelauncher.relaunchIfSupported(
                    applicationName, applicationVersion, applicationClass, simulationName, inputs, tabId)) {
                return;
            }
        }
        Layout.getInstance().addTab(
                tabId,
                () -> new LaunchTab(applicationName, applicationVersion, applicationClass, simulationName, inputs));
    }
}
