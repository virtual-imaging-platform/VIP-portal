package fr.insalyon.creatis.vip.application.client.view.monitor.timeline;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface TimelineParserInterface {

    public boolean parse(String applicationName);

    public SimulationBoxLayout getLayout(String id, String name, String applicationName,
            String applicationVersion, String applicationClass, String user, 
            SimulationStatus status, Date launchedDate);
}
