package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class MonitorParser {

    public static MonitorParser instance;
    public List<MonitorParserInterface> parsers;

    public static MonitorParser getInstance() {

        if (instance == null) {
            instance = new MonitorParser();
        }
        return instance;
    }

    private MonitorParser() {

        parsers = new ArrayList<MonitorParserInterface>();
    }

    public void addParser(MonitorParserInterface parser) {

        parsers.add(parser);
    }

    public Layout.TabFactoryAndId parse(
        final String simulationId,
        final String simulationName,
        final String applicationName,
        final SimulationStatus status,
        final Date launchedDate) {

        for (MonitorParserInterface parser : parsers) {
            if (parser.parse(applicationName)) {
                return parser.getTab(simulationId, simulationName, status, launchedDate);
            }
        }
        return new Layout.TabFactoryAndId(
            () -> new SimulationTab(simulationId, simulationName, status),
            AbstractSimulationTab.tabIdFrom(simulationId));
    }
}
