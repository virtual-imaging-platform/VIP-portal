/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.gatelab.client;

import com.smartgwt.client.widgets.tab.Tab;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationModule;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.inter.CustomApplicationModule;
import fr.insalyon.creatis.vip.application.client.view.launch.RelaunchService;
import fr.insalyon.creatis.vip.application.client.view.monitor.MonitorParser;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineParser;
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
