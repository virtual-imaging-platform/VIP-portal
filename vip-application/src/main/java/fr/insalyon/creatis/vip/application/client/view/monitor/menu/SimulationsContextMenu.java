package fr.insalyon.creatis.vip.application.client.view.monitor.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.application.client.view.launch.RelaunchService;
import fr.insalyon.creatis.vip.application.client.view.monitor.ChangeSimulationUserLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationsTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SimulationsContextMenu extends Menu {

    private ModalWindow modal;
    private String simulationID;
    private String simulationName;
    private String applicationName;
    private String applicationVersion;
    private String applicationClass;
    private String simulationUser;

    public SimulationsContextMenu(
            ModalWindow modal, final String simulationID, final String title, final SimulationStatus status,
            String applicationName, String applicationVersion, String applicationClass, String simulationUser) {

        this.modal = modal;
        this.simulationID = simulationID;
        this.simulationName = title;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationClass = applicationClass;
        this.simulationUser = simulationUser;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem viewItem = new MenuItem("View Execution");
        viewItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        viewItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(
                        AbstractSimulationTab.tabIdFrom(simulationID),
                        () -> new SimulationTab(simulationID, title, status));
            }
        });

        MenuItem killItem = new MenuItem("Kill Execution");
        killItem.setIcon(ApplicationConstants.ICON_KILL);
        killItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.ask("Do you really want to kill this execution ("
                        + title + ")?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            killSimulation();
                        }
                    }
                });
            }
        });

        MenuItem cleanItem = new MenuItem("Clean Execution");
        cleanItem.setIcon(ApplicationConstants.ICON_CLEAN);
        cleanItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.ask("Do you really want to clean this execution ("
                        + title + ")?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            cleanSimulation();
                        }
                    }
                });
            }
        });

        MenuItem purgeItem = new MenuItem("Purge Execution");
        purgeItem.setIcon(CoreConstants.ICON_CLEAR);
        purgeItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.ask("Do you really want to purge this execution ("
                        + title + ")?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            purgeSimulation();
                        }
                    }
                });
            }
        });

        MenuItem markCompletedItem = new MenuItem("Mark Execution Completed");
        markCompletedItem.setIcon(ApplicationConstants.ICON_MARK_COMPLETED);
        markCompletedItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.ask("Do you really want to mark this execution as completed ("
                        + title + ")?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            markCompleted();
                        }
                    }
                });
            }
        });

        MenuItem relauchItem = new MenuItem("Relaunch Execution");
        relauchItem.setIcon(ApplicationConstants.ICON_RELAUNCH);
        relauchItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                relaunchSimulation();
            }
        });

        MenuItem changeUserItem = new MenuItem("Change Execution User");
        changeUserItem.setIcon(ApplicationConstants.ICON_USER);
        changeUserItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                changeUser();
            }
        });

        MenuItemSeparator separator = new MenuItemSeparator();

        switch (status) {
            case Running:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, killItem, separator, relauchItem, separator, changeUserItem);
                } else {
                    this.setItems(viewItem, killItem, separator, relauchItem);
                }
                break;

            case Completed:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, cleanItem, separator, relauchItem, separator, changeUserItem, separator);
                } else {
                    this.setItems(viewItem, cleanItem, separator, relauchItem, separator);
                }
                break;

            case Cleaned:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, purgeItem, separator, changeUserItem);
                } else {
                    this.setItems(viewItem);
                }
                break;

            case Failed:
            case Killed:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, markCompletedItem, cleanItem, separator, relauchItem, separator, changeUserItem);
                } else {
                    this.setItems(viewItem, cleanItem, separator, relauchItem);
                }
        }
    }

    /**
     * Sends a request to kill the simulation.
     */
    private void killSimulation() {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to kill execution:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        WorkflowService.Util.getInstance().killWorkflow(simulationID, callback);
        modal.show("Sending killing signal to " + simulationName + "...", true);
    }

    /**
     * Sends a request to clean the simulation.
     */
    private void cleanSimulation() {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to clean execution:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        WorkflowService.Util.getInstance().cleanWorkflow(simulationID, callback);
        modal.show("Cleaning execution " + simulationName + "...", true);
    }

    /**
     * Sends a request to purge the simulation.
     */
    private void purgeSimulation() {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to purge execution:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        WorkflowService.Util.getInstance().purgeWorkflow(simulationID, callback);
        modal.show("Purging execution " + simulationName + "...", true);
    }

    private void markCompleted() {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to mark execution completed:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        WorkflowService.Util.getInstance().markWorkflowCompleted(simulationID, callback);
        modal.show("Marking execution " + simulationName + " cmopleted...", true);
    }

    /**
     * Relaunches a simulation.
     */
    private void relaunchSimulation() {

        AsyncCallback<Map<String, String>> callback = new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to relauch execution:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(final Map<String, String> result) {
                modal.hide();
                String tabId = ApplicationConstants.getLaunchTabID(applicationName);
                Layout.getInstance().removeTab(tabId);
                RelaunchService.getInstance().relaunch(
                        applicationName, applicationVersion, applicationClass, simulationName, result, tabId);
            }
        };
        WorkflowService.Util.getInstance().relaunchSimulation(simulationID, callback);
        modal.show("Relaunching execution " + simulationName + "...", true);
    }

    private void changeUser() {
        new ChangeSimulationUserLayout(modal, simulationID, simulationName,
                simulationUser).show();
    }

    private SimulationsTab getSimulationsTab() {
        return (SimulationsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_MONITOR);
    }
}
