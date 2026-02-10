package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.SimulationBoxLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabSimulationBoxLayout extends SimulationBoxLayout {

    public GateLabSimulationBoxLayout(String id, String name, String applicationName,
            String applicationVersion, String applicationClass, String user,
            SimulationStatus status, Date date) {

        super(id, name, applicationName, applicationVersion, applicationClass,
                user, status, date);

        handler.removeHandler();
        mainLayout.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                    AbstractSimulationTab.tabIdFrom(simulationID),
                    () -> new GateLabSimulationTab(
                        simulationID,
                        simulationName,
                        simulationStatus,
                        launchedDate.toString()));
            }
        });
    }
}
