package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.SimulationBoxLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineParserInterface;
import fr.insalyon.creatis.vip.gatelab.client.GateLabConstants;
import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabTimelineParser implements TimelineParserInterface {

    @Override
    public boolean parse(String applicationName) {
        return applicationName.toLowerCase().contains(GateLabConstants.TAG_GATELAB.toLowerCase());
    }

    @Override
    public SimulationBoxLayout getLayout(String id, String name, String applicationName,
            String applicationVersion, String applicationClass, String user, 
            SimulationStatus status, Date launchedDate) {

        return new GateLabSimulationBoxLayout(id, name, applicationName, 
                applicationVersion, applicationClass, user, status, launchedDate);
    }
}
