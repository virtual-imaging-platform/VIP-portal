package fr.insalyon.creatis.vip.application.client.view.monitor;

import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SimulationTab extends AbstractSimulationTab {

    private GeneralTab generalTab;
    private JobsTab jobsTab;
    private SummaryTab tasksTab;
    private ChartsTab chartsTab;
    private LogsTab logsTab;

    public SimulationTab(String simulationID, String simulationName, SimulationStatus status) {

        super(simulationID, simulationName, status);

        generalTab = new GeneralTab(simulationID, status);
        jobsTab = new JobsTab(simulationID, completed);
        chartsTab = new ChartsTab(simulationID);

        tabSet.addTab(generalTab);
        tabSet.addTab(jobsTab);
        tabSet.addTab(chartsTab);

        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isDeveloper()) {
            
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
