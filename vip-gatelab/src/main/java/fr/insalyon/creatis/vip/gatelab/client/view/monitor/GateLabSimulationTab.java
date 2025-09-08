package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.smartgwt.client.widgets.Canvas;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.ChartsTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.JobsTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.LogsTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.SummaryTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.gatelab.client.GateLabConstants;

/**
 *
 * @author Sorina Camarasu, Rafael Ferreira da Silva
 */
public class GateLabSimulationTab extends AbstractSimulationTab {

    private GateLabGeneralTab generalTab;
    private JobsTab jobsTab;
    private SummaryTab tasksTab;
    private ChartsTab chartsTab;
    private LogsTab logsTab;

    public GateLabSimulationTab(String simulationID, String simulationName, SimulationStatus status, String date) {

        super(simulationID, simulationName, status);
        
        this.setTitle(Canvas.imgHTML(GateLabConstants.ICON_APPLICATION) + " " + simulationName);

        generalTab = new GateLabGeneralTab(simulationID, status, date);
        jobsTab = new JobsTab(simulationID, completed);
        chartsTab = new ChartsTab(simulationID);

        tabSet.addTab(generalTab);
        tabSet.addTab(jobsTab);
        tabSet.addTab(chartsTab);
        
        if (CoreModule.user.isSystemAdministrator()) {
            
            tasksTab = new SummaryTab(simulationID, completed);
            tabSet.addTab(tasksTab);
            
            logsTab = new LogsTab(simulationID);
            tabSet.addTab(logsTab);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        jobsTab.destroy();
    }
}
