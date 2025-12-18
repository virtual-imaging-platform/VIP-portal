package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Label;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProgressLayout;
import fr.insalyon.creatis.vip.application.models.Simulation;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GeneralLayout extends AbstractFormLayout {

    private String simulationID;
    private Label generalLabel;
    private ProgressLayout progressLayout;

    public GeneralLayout(String simulationID, SimulationStatus status) {

        super("100%", "140px");
        this.simulationID = simulationID;

        generalLabel = new Label();
        generalLabel.setHeight(25);
        generalLabel.setIcon(ApplicationConstants.ICON_GENERAL);
        generalLabel.setCanSelectText(true);
        this.addMember(generalLabel);

        addTitle("<font color=\"#333333\">Execution Progress</font>", null);
        progressLayout = new ProgressLayout(simulationID, status);
        this.addMember(progressLayout);
        
        loadData();
    }

    private void loadData() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Simulation> callback = new AsyncCallback<Simulation>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load general information:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Simulation result) {
                
                StringBuilder sb = new StringBuilder();
                sb.append("<font color=\"#333333\"><b>");
                sb.append(result.getApplicationName());
                sb.append(" ");
                sb.append(result.getApplicationVersion());
                sb.append("</b> launched on <b>");
                sb.append(result.getDate().toString());
                sb.append("</b>");
                if (CoreModule.user.isSystemAdministrator()) {
                    sb.append(" by <b>");
                    sb.append(result.getUserName());
                    sb.append("</b> (");
                    sb.append(result.getID());
                    sb.append(")</font>");
                }              
                generalLabel.setContents(sb.toString());
            }
        };
        service.getSimulation(simulationID, callback);
    }

    public void update() {
        progressLayout.update();
    }
}
