package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabGeneralLayout extends AbstractFormLayout {

    private String simulationID;
    private String date;
    private Label generalLabel;
    private Label infoLabel;
    private GateLabProgressLayout progressLayout;
    private LabelButton stopAndMergeButton;

    public GateLabGeneralLayout(String simulationID, SimulationStatus status, String date) {

        super("100%", "160px");
        this.simulationID = simulationID;
        this.date = date;

        generalLabel = new Label();
        generalLabel.setHeight(20);
        generalLabel.setIcon(ApplicationConstants.ICON_GENERAL);
        generalLabel.setCanSelectText(true);
        this.addMember(generalLabel);

        infoLabel = new Label();
        infoLabel.setHeight(20);
        infoLabel.setCanSelectText(true);
        this.addMember(infoLabel);

        addTitle("<font color=\"#333333\">Simulation Progress</font>", null);
        progressLayout = new GateLabProgressLayout(simulationID, status);
        this.addMember(progressLayout);

        HLayout bottomLayout = new HLayout();
        bottomLayout.setWidth100();
        bottomLayout.setHeight(25);
        this.addMember(bottomLayout);

        stopAndMergeButton = getStopAndMergeButton();
        if (status != SimulationStatus.Running) {
            stopAndMergeButton.setDisabled(true);
        }
        bottomLayout.addMember(stopAndMergeButton);

        loadData();
    }

    private LabelButton getStopAndMergeButton() {

        LabelButton button = new LabelButton("Stop and Merge", null);
        button.setWidth(150);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to stop and merge this simulation?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    Layout.getInstance().setWarningMessage("Unable to execute stop and merge:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    stopAndMergeButton.setDisabled(true);
                                    Layout.getInstance().setNoticeMessage("The simulation was successfully Stopped... Starting Merge in few minutes!");
                                }
                            };
                            GateLabService.Util.getInstance().StopWorkflowSimulation(simulationID, callback);
                        }
                    }
                });
            }
        });
        return button;
    }

    private void loadData() {

        AsyncCallback<Map<String, String>> callback = new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load general information:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, String> result) {

                String gateVersion = result.get("gate_version");
                GateLabSimulationType type = GateLabSimulationType.valueOf(result.get("simulation"));
                double particles = Double.parseDouble(result.get("particles"));
                double runnedParticles = Double.parseDouble(result.get("runnedparticles"));

                StringBuilder sb = new StringBuilder();
                sb.append("<font color=\"#333333\"><b>GateLab</b> launched on <b>");
                sb.append(date);
                sb.append("</b>");
                if (CoreModule.user.isSystemAdministrator()) {
                    sb.append(" (");
                    sb.append(simulationID);
                    sb.append(")</font>");
                }
                generalLabel.setContents(sb.toString());

                sb = new StringBuilder();
                sb.append("<font color=\"#333333\">Simulation Type: <b>");
                sb.append(type.name());
                sb.append("</b> - Gate Release: <b>");
                sb.append(gateVersion.substring(gateVersion.lastIndexOf("/") + 1));
                sb.append("</b></font>");
                infoLabel.setContents(sb.toString());

                progressLayout.update(particles, runnedParticles);
            }
        };
        GateLabService.Util.getInstance().getGatelabWorkflowInputs(simulationID, callback);
    }

    public void update() {
        loadData();
    }
}
