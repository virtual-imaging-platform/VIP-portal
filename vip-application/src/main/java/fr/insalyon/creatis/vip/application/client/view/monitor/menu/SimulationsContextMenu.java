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
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.MakeExecutionPublicTab;

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

    public SimulationsContextMenu(ModalWindow modal, final String simulationID,
                                  final String title, final SimulationStatus status, String applicationName,
                                  String applicationVersion, String applicationClass, String simulationUser) {

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

        MenuItem makePublicExecutionItem = new MenuItem("Make this execution public");
        makePublicExecutionItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        makePublicExecutionItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.ask("Do you really want to make this execution public:  ("
                        + title + ")?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            Execution execution = new Execution(simulationName, applicationVersion, "status", simulationUser, "comments");
                            Layout.getInstance().addTab(CoreConstants.TAB_MAKE_EXECUTION_PUBLIC, () -> new MakeExecutionPublicTab(execution));
                        }
                    }
                });
            }
        });

        MenuItemSeparator separator = new MenuItemSeparator();

        switch (status) {
            case Running:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, killItem, separator, relauchItem, separator, changeUserItem, separator, makePublicExecutionItem);
                } else {
                    this.setItems(viewItem, killItem, separator, relauchItem, separator, makePublicExecutionItem);
                }
                break;

            case Completed:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, cleanItem, separator, relauchItem, separator, changeUserItem, separator, makePublicExecutionItem);
                } else {
                    this.setItems(viewItem, cleanItem, separator, relauchItem, separator, makePublicExecutionItem);
                }
                break;

            case Cleaned:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, purgeItem, separator, changeUserItem, separator, makePublicExecutionItem);
                } else {
                    this.setItems(viewItem);
                }
                break;

            case Failed:
            case Killed:
                if (CoreModule.user.isSystemAdministrator()) {
                    this.setItems(viewItem, markCompletedItem, cleanItem, separator, relauchItem, separator, changeUserItem, separator, makePublicExecutionItem);
                } else {
                    this.setItems(viewItem, cleanItem, separator, relauchItem, separator, makePublicExecutionItem);
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
                String tabId =
                        ApplicationConstants.getLaunchTabID(applicationName);
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
