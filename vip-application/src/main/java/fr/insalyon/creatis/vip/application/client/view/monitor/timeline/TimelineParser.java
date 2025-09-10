package fr.insalyon.creatis.vip.application.client.view.monitor.timeline;

import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class TimelineParser {

    public static TimelineParser instance;
    public List<TimelineParserInterface> parsers;

    public static TimelineParser getInstance() {

        if (instance == null) {
            instance = new TimelineParser();
        }
        return instance;
    }

    private TimelineParser() {

        parsers = new ArrayList<TimelineParserInterface>();
    }

    public void addParser(TimelineParserInterface parser) {
        
        parsers.add(parser);
    }

    public SimulationBoxLayout parse(String id, String name, String applicationName,
            String applicationVersion, String applicationClass, String user, 
            SimulationStatus status, Date launchedDate) {
        
        for (TimelineParserInterface parser : parsers) {
            if (parser.parse(applicationName)) {
                return parser.getLayout(id, name, applicationName, applicationVersion, 
                        applicationClass, user, status, launchedDate);
            }
        }
        return new SimulationBoxLayout(id, name, applicationName, applicationVersion, 
                applicationClass, user, status, launchedDate);
    }
}
