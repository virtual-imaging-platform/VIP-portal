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
package fr.insalyon.creatis.vip.application.client.view.monitor.timeline;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.application.client.view.launch.RelaunchService;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationTab;
import fr.insalyon.creatis.vip.application.models.Simulation;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SimulationBoxLayout extends HLayout {

    protected String simulationID;
    protected String simulationName;
    protected String applicationName;
    protected String applicationVersion;
    protected String applicationClass;
    protected SimulationStatus simulationStatus;
    protected Date launchedDate;
    private Img img;
    private Label nameLabel;
    private Label actionButton;
    private Timer timer;
    protected VLayout mainLayout;
    protected HandlerRegistration handler;

    public SimulationBoxLayout(String id, String name, String applicationName,
            String applicationVersion, String applicationClass, String user,
            SimulationStatus status, Date launchedDate) {

        this.simulationID = id;
        this.simulationName = name;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationClass = applicationClass;
        this.simulationStatus = status;
        this.launchedDate = launchedDate;

        this.setMembersMargin(10);
        this.setWidth(350);
        this.setHeight(50);
        this.setPadding(5);
        this.setBackgroundColor("#FFFFFF");
        this.setAlign(VerticalAlignment.CENTER);

        actionButton = WidgetUtil.getIconLabel(CoreConstants.ICON_CLOSE, "", 16, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                performAction();
            }
        });

        img = new Img();
        img.setWidth(32);
        img.setHeight(32);
        parseStatus();
        this.addMember(img);

        mainLayout = new VLayout(3);
        mainLayout.setWidth100();
        mainLayout.setHeight100();
        mainLayout.setCursor(Cursor.HAND);
        nameLabel = WidgetUtil.getLabel("<b>" + simulationName + "</b>", 12, Cursor.HAND);
        mainLayout.addMember(nameLabel);
        mainLayout.addMember(WidgetUtil.getLabel(
                applicationVersion == null ? applicationName : applicationName + " " + applicationVersion,
                12, Cursor.HAND));
        StringBuilder sb = new StringBuilder();
        sb.append("<font color=\"#333333\">").append(DateTimeFormat.getFormat("MM/dd/yyyy HH:mm").format(launchedDate)).append("</font>");
        if (CoreModule.user.isSystemAdministrator()) {
            sb.append(" - (").append(user).append(")");
        }
        mainLayout.addMember(WidgetUtil.getLabel(sb.toString(), 12, Cursor.HAND));
        handler = mainLayout.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                    AbstractSimulationTab.tabIdFrom(simulationID),
                    () -> new SimulationTab(
                        simulationID, simulationName, simulationStatus));
            }
        });
        this.addMember(mainLayout);

        VLayout buttonsLayout = new VLayout(10);
        buttonsLayout.setWidth(20);
        buttonsLayout.setHeight100();
        buttonsLayout.addMember(actionButton);
        buttonsLayout.addMember(WidgetUtil.getIconLabel(ApplicationConstants.ICON_MONITOR_RELAUNCH,
                "Relaunch simulation", 16, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                relaunchSimulation();
            }
        }));
        this.addMember(buttonsLayout);
    }

    private void parseStatus() {

        switch (simulationStatus) {
            case Running:
                timer = new Timer() {
                    @Override
                    public void run() {
                        loadData();
                    }
                };
                timer.scheduleRepeating(120000);
                actionButton.setPrompt("Kill simulation");
                img.setSrc(ApplicationConstants.ICON_MONITOR_SIMULATION_RUNNING);
                break;
            case Killed:
                cancelTimer();
                actionButton.setPrompt("Clean simulation");
                img.setSrc(ApplicationConstants.ICON_MONITOR_SIMULATION_KILLED);
                break;
            case Cleaned:
                cancelTimer();
                if (CoreModule.user.isSystemAdministrator()) {
                    actionButton.setPrompt("Purge simulation");
                } else {
                    actionButton.hide();
                }
                img.setSrc(ApplicationConstants.ICON_MONITOR_SIMULATION_CLEANED);
                break;
            case Failed:
                cancelTimer();
                actionButton.setPrompt("Clean simulation");
                img.setSrc(ApplicationConstants.ICON_MONITOR_SIMULATION_FAILED);
                break;
            default:
                cancelTimer();
                actionButton.setPrompt("Clean simulation");
                img.setSrc(ApplicationConstants.ICON_MONITOR_SIMULATION_COMPLETED);
        }
        img.setPrompt(simulationStatus.name());
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void loadData() {

        AsyncCallback<Simulation> callback = new AsyncCallback<Simulation>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load simulation info:<br />" + caught.getMessage());
                timer.cancel();
            }

            @Override
            public void onSuccess(Simulation result) {

                SimulationStatus status = result.getStatus();
                if (status != simulationStatus) {
                    simulationStatus = status;
                    parseStatus();
                }
            }
        };
        WorkflowService.Util.getInstance().getSimulation(simulationID, callback);
    }

    private void performAction() {

        String question;
        switch (simulationStatus) {
            case Running:
                question = "kill";
                break;
            case Cleaned:
                question = "purge";
                break;
            default:
                question = "clean";
        }
        SC.ask("Do you really want to " + question + " '" + simulationName + "' execution?", new BooleanCallback() {
            @Override
            public void execute(Boolean value) {
                if (value) {
                    switch (simulationStatus) {
                        case Running:
                            killSimulation();
                            break;
                        case Cleaned:
                            purgeSimulation();
                            break;
                        default:
                            cleanSimulation();
                    }
                }
            }
        });
    }

    /**
     * Kills the simulation
     */
    private void killSimulation() {
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                setLoading(false, null);
                Layout.getInstance().setWarningMessage("Unable to kill execution:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                setLoading(false, null);
                loadData();
            }
        };
        WorkflowService.Util.getInstance().killWorkflow(simulationID, callback);
        setLoading(true, "Killing");
    }

    /**
     * Cleans the simulation.
     */
    private void cleanSimulation() {
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                setLoading(false, null);
                Layout.getInstance().setWarningMessage("Unable to clean execution:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                if (CoreModule.user.isSystemAdministrator()) {
                    setLoading(false, null);
                    loadData();
                } else {
                    destroy();
                }
            }
        };
        WorkflowService.Util.getInstance().cleanWorkflow(simulationID, callback);
        setLoading(true, "Cleaning");
    }

    /**
     * Purges the simulation.
     */
    private void purgeSimulation() {
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                setLoading(false, null);
                Layout.getInstance().setWarningMessage("Unable to purge simulation:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                destroy();
            }
        };
        WorkflowService.Util.getInstance().purgeWorkflow(simulationID, callback);
        setLoading(true, "Purging");
    }

    /**
     * Relaunches the simulation.
     */
    private void relaunchSimulation() {

        AsyncCallback<Map<String, String>> callback = new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                setLoading(false, null);
                Layout.getInstance().setWarningMessage("Unable to relauch simulation:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(final Map<String, String> result) {
                setLoading(false, null);
                String tabId = ApplicationConstants.getLaunchTabID(applicationName);
                Layout.getInstance().removeTab(tabId);
                RelaunchService.getInstance().relaunch(
                        applicationName, applicationVersion, applicationClass, simulationName, result, tabId);
            }
        };
        WorkflowService.Util.getInstance().relaunchSimulation(simulationID, callback);
        setLoading(true, "Relaunching");
    }

    private void setLoading(boolean loading, String action) {
        if (loading) {
            nameLabel.setContents("<b>" + action + " " + simulationName + "...</b>");
            nameLabel.setIcon(CoreConstants.ICON_LOADING);
            actionButton.setDisabled(true);
        } else {
            nameLabel.setContents("<b>" + simulationName + "</b>");
            nameLabel.setIcon(null);
            actionButton.setDisabled(false);
        }
    }

    public void updateStatus(SimulationStatus status) {

        if (status != simulationStatus) {
            simulationStatus = status;
            parseStatus();
        }
    }

    public String getSimulationID() {
        return simulationID;
    }

    public Date getLaunchedDate() {
        return launchedDate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
