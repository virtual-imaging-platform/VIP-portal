package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.MonitorParserInterface;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.gatelab.client.GateLabConstants;
import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabMonitorParser implements MonitorParserInterface {

    @Override
    public boolean parse(String applicationName) {
        return applicationName.toLowerCase().contains(GateLabConstants.TAG_GATELAB.toLowerCase());
    }

    @Override
    public Layout.TabFactoryAndId getTab(
        final String simulationId,
        final String simulatioName,
        final SimulationStatus status,
        final Date launchedDate) {

        return new Layout.TabFactoryAndId(
            () -> new GateLabSimulationTab(
                simulationId, simulatioName, status, launchedDate.toString()),
            AbstractSimulationTab.tabIdFrom(simulationId));
    }
}
