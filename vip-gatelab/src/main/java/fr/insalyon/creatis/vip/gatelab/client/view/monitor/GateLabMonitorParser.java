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
package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.smartgwt.client.widgets.tab.Tab;
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

    /**
     *
     * @param applicationName
     * @return
     */
    @Override
    public boolean parse(String applicationName) {
        return applicationName.toLowerCase().contains(GateLabConstants.GROUP_GATELAB.toLowerCase());
    }

    /**
     *
     * @param simulationId
     * @param simulatioName
     * @param status
     * @param launchedDate
     * @return
     */
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
