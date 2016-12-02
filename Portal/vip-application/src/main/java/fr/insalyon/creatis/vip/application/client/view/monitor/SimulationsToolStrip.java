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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.SimulationRecord;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.view.message.MessageComposerWindow;
import fr.insalyon.creatis.vip.social.client.view.message.MessageComposerWindowToReportIssue;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SimulationsToolStrip extends ToolStrip {

    private ModalWindow modal;

    public SimulationsToolStrip(ModalWindow modal) {

        this.modal = modal;
        this.setWidth100();

        // Refresh Button
        this.addButton(WidgetUtil.getToolStripButton("Refresh",
                CoreConstants.ICON_REFRESH, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getSimulationsTab().loadData();
            }
        }));

        // Search Button
        this.addButton(WidgetUtil.getToolStripButton("Search",
                ApplicationConstants.ICON_SEARCH, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getSimulationsTab().expandSearchSection();
            }
        }));

        //Kill Executions Button
        this.addButton(WidgetUtil.getToolStripButton("Kill Executions",
                ApplicationConstants.ICON_KILL, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to kill the selected running executions?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            killSimulations();
                        }
                    }
                });
            }
        }));

        // Clean Executions Button
        this.addButton(WidgetUtil.getToolStripButton("Clean Executions",
                ApplicationConstants.ICON_CLEAN, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.confirm("Do you really want to clean the selected completed/killed executions?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            cleanSimulations();
                        }
                    }
                });
            }
        }));
        //Report issue Button
        this.addButton(WidgetUtil.getToolStripButton("Report Issue About This Execution",
                ApplicationConstants.ICON_REPORT_ISSUE, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] records = getSimulationsTab().getGridSelection();
                List<String> simulationIDs = new ArrayList<String>();
                List<String> simulationNames = new ArrayList<String>();
                for (ListGridRecord record : records) {
                    SimulationRecord data = (SimulationRecord) record;
                    SimulationStatus status = SimulationStatus.valueOf(data.getStatus());
                    simulationIDs.add(data.getSimulationId());
                    simulationNames.add(data.getSimulationName());
                }
                sendMailToVIP(simulationIDs, simulationNames);
            }
        }));


        if (CoreModule.user.isSystemAdministrator()) {
            this.addSeparator();
            // Purge Executions
            this.addButton(WidgetUtil.getToolStripButton("Purge Executionss",
                    CoreConstants.ICON_CLEAR, null, new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    SC.ask("Do you really want to purge the selected cleaned executions?", new BooleanCallback() {
                        @Override
                        public void execute(Boolean value) {
                            if (value) {
                                purgeSimulations();
                            }
                        }
                    });
                }
            }));

            // kill Simulation With Reason
            this.addButton(WidgetUtil.getToolStripButton("Kill Execution With Reason",
                    ApplicationConstants.ICON_KILLWR, null, new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    SC.ask("Do you really want to kill the selected running executions?", new BooleanCallback() {
                        @Override
                        public void execute(Boolean value) {
                            if (value) {
                                killSimulationWithReason();
                            }
                        }
                    });
                }
            }));

            // Mark Simulation as Completed
            this.addButton(WidgetUtil.getToolStripButton("Mark Executions Completed",
                    ApplicationConstants.ICON_MARK_COMPLETED, null, new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    SC.ask("Do you really want to mark as completed the selected killed executions?", new BooleanCallback() {
                        @Override
                        public void execute(Boolean value) {
                            if (value) {
                                markSimulationsCompleted();
                            }
                        }
                    });
                }
            }));


            // Stats Button
            this.addSeparator();
            this.addButton(WidgetUtil.getToolStripButton("Performance Statistics",
                    ApplicationConstants.ICON_CHART, null, new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Layout.getInstance().addTab(new StatsTab());
                    StatsTab statsTab = (StatsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_STATS);
                    statsTab.setSimulationsList(getSimulationsTab().getSimulationsList());
                }
            }));
        }

        // Status Button
        this.addFill();
        this.addButton(WidgetUtil.getToolStripButton("System Load",
                ApplicationConstants.ICON_STATUS, "Current status of the system.", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new SystemLoadLayout(event.getX(), event.getY()).show();
            }
        }));
    }

    /**
     * Sends a request to kill the selected running simulations
     *
     */
    private void killSimulations() {

        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Running) {
                simulationIDs.add(data.getSimulationId());
            }
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to kill executions:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        service.killSimulations(simulationIDs, callback);
        modal.show("Sending killing signal to selected executions...", true);
    }

    private void killSimulationWithReason() {
        final String applicationName;
        final String user;
        final String date;
        final String simulationName;

        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();
        if (records.length > 1) {
            SC.say("there is more than one selected execution");
        } else if (records.length == 1) {
            applicationName = records[0].getAttribute("application");
            user = records[0].getAttribute("user");
            date = records[0].getAttribute("date");
            simulationName = records[0].getAttribute("simulationName");
            SimulationRecord data = (SimulationRecord) records[0];
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Running) {
                simulationIDs.add(data.getSimulationId());
            }

            WorkflowServiceAsync service = WorkflowService.Util.getInstance();
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    Layout.getInstance().setWarningMessage("Unable to kill executions:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    modal.hide();
                    getSimulationsTab().loadData();
                    openWindowToSendMail("Your " + "\""+applicationName+"\"" + " execution",
                            "Dear " + user + ",<br /><br />"
                                    + " I had to kill your " + "\""+ applicationName+ "\"" + " execution "
                                    + "\""+ simulationName+ "\""+ " submitted on " + date
                                    + " because all the jobs were failing with the following error:"
                                    + "<br /><br /><br /><br />",user);



                }
            };
            service.killSimulations(simulationIDs, callback);
            modal.show("Sending killing signal to selected executions...", true);
        }
    }

    /**
     * Sends a request to clean the selected completed/killed simulations
     *
     */
    private void cleanSimulations() {

        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Completed
                    || status == SimulationStatus.Killed) {

                simulationIDs.add(data.getSimulationId());
            }
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to clean executions:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        service.cleanSimulations(simulationIDs, callback);
        modal.show("Cleaning selected executions...", true);
    }

    /**
     * Sends a request to purge the selected cleaned simulations
     *
     */
    private void purgeSimulations() {

        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Cleaned) {
                simulationIDs.add(data.getSimulationId());
            }
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to purge executions:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        service.purgeSimulations(simulationIDs, callback);
        modal.show("Purging selected executions...", true);
    }

    private void markSimulationsCompleted() {
        ListGridRecord[] records = getSimulationsTab().getGridSelection();
        List<String> simulationIDs = new ArrayList<String>();

        for (ListGridRecord record : records) {
            SimulationRecord data = (SimulationRecord) record;
            SimulationStatus status = SimulationStatus.valueOf(data.getStatus());

            if (status == SimulationStatus.Killed) {
                simulationIDs.add(data.getSimulationId());
            }
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to mark executions completed:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                getSimulationsTab().loadData();
            }
        };
        service.markSimulationsCompleted(simulationIDs, callback);
        modal.show("Marking selected executions completed...", true);
    }

    private SimulationsTab getSimulationsTab() {
        return (SimulationsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_MONITOR);
    }

    private void openWindowToSendMail(String subjectValue, String message,String userFullName) {
        MessageComposerWindow messageWindow = new MessageComposerWindow();
        messageWindow.show();
        messageWindow.setSubjectValue(subjectValue);
        messageWindow.setUsersPickerListValue(userFullName);
        messageWindow.setTextMessage(message);
        messageWindow.setSendCopyToSupport(true);

    }

    private void sendMailToVIP(List<String> workflowID, List<String> simulationNames) {


        MessageComposerWindowToReportIssue messageWindow = new MessageComposerWindowToReportIssue(workflowID, simulationNames);
        messageWindow.show();
    }
}
