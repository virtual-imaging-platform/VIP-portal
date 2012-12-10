/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class TaskLayout extends HLayout {

    private String simulationID;
    private String taskID;
    private TaskStatus status;
    private int exitCode;
    private int minorStatus;
    private Label statusLabel;
    private Label minorStatusLabel;
    private HLayout actionsLayout;
    private boolean selected;

    public TaskLayout(final DebugLayout debugLayout, String simulationID, Task task) {

        this.simulationID = simulationID;
        this.taskID = task.getId();
        this.status = TaskStatus.valueOf(task.getStatus());
        this.exitCode = task.getExitCode();
        this.minorStatus = task.getMinorStatus();
        this.selected = false;

        this.setWidth100();
        this.setHeight(16);
        this.setBorder("1px solid #F2F2F2");
        this.setPadding(1);
        this.setMembersMargin(3);

        Label idLabel = WidgetUtil.getLabel(taskID, 16, Cursor.HAND);
        idLabel.setWidth100();
        this.addMember(idLabel);

        statusLabel = WidgetUtil.getLabel(status.name(), 16, Cursor.HAND);
        statusLabel.setWidth(180);
        this.addMember(statusLabel);

        minorStatusLabel = WidgetUtil.getLabel("<font color=\"#666666\">" + parseMinorStatus() + "</font>", 16, Cursor.HAND);
        minorStatusLabel.setWidth(200);
        this.addMember(minorStatusLabel);

        actionsLayout = new HLayout(2);
        actionsLayout.setWidth(70);
        this.addMember(actionsLayout);
        configureActionLabels();

        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                debugLayout.selectTask(taskID, status);
            }
        });
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                setBackgroundColor("#F7F7F7");
            }
        });
        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                if (!selected) {
                    setBackgroundColor("#FFFFFF");
                } else {
                    setBackgroundColor("#F5F5F5");
                }
            }
        });
    }

    public void setSelected(boolean selected) {

        if (selected) {
            this.selected = true;
            this.setBackgroundColor("#F5F5F5");
        } else {
            this.setBackgroundColor("#FFFFFF");
            this.selected = false;
        }
    }

    private String parseMinorStatus() {

        if (status.isCompletedStateWithOutputs()) {
            switch (exitCode) {
                case -1:
                    return "Retrieving Status";
                case 0:
                    return "Execution Completed";
                case 1:
                    return "Inputs Download Failed";
                case 2:
                    return "Outputs Upload Failed";
                case 6:
                    return "Application Execution Failed";
                case 7:
                    return "Directories Creation Failed";
                default:
            }
        } else {
            switch (minorStatus) {
                case 1:
                    return "Job Set Up";
                case 2:
                    return "Downloading Background Script";
                case 3:
                    return "Downloading Inputs";
                case 4:
                    return "Application Execution";
                case 5:
                    return "Uploading Results";
                case 6:
                    return "Finishing";
                default:
            }
        }
        return "";
    }

    private void configureActionLabels() {

        actionsLayout.removeMembers(actionsLayout.getMembers());

        if (status.isRunningState()) {
            Label replicateLabel = WidgetUtil.getLabel(null, ApplicationConstants.ICON_TASK_REPLICATE, 16, Cursor.HAND);
            replicateLabel.setPrompt("Replicate Task");
            replicateLabel.setWidth(16);
            replicateLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    sendSignal(TaskStatus.REPLICATE);
                }
            });
            actionsLayout.addMember(replicateLabel);

            Label rescheduleLabel = WidgetUtil.getLabel(null, ApplicationConstants.ICON_TASK_RESCHEDULE, 16, Cursor.HAND);
            rescheduleLabel.setPrompt("Reschedule Task");
            rescheduleLabel.setWidth(16);
            rescheduleLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    sendSignal(TaskStatus.RESCHEDULE);
                }
            });
            actionsLayout.addMember(rescheduleLabel);

            Label killLabel = WidgetUtil.getLabel(null, ApplicationConstants.ICON_TASK_KILL, 16, Cursor.HAND);
            killLabel.setPrompt("Kill Task");
            killLabel.setWidth(16);
            killLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    sendSignal(TaskStatus.KILL);
                }
            });
            actionsLayout.addMember(killLabel);
        }
    }

    private void sendSignal(final TaskStatus taskStatus) {

        SC.ask("Do you really want to " + taskStatus.name().toLowerCase()
                + " this task?", new BooleanCallback() {
            @Override
            public void execute(Boolean value) {
                if (value) {
                    AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            statusLabel.setContents(status.name());
                            minorStatusLabel.setContents("");
                            minorStatusLabel.setIcon(null);
                            Layout.getInstance().setWarningMessage("Unable to send signal:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            minorStatusLabel.setContents("");
                            minorStatusLabel.setIcon(null);
                            Layout.getInstance().setNoticeMessage("Signal Successfully sent.");
                        }
                    };
                    statusLabel.setContents(taskStatus.name());
                    minorStatusLabel.setContents("<font color=\"#666666\">Sending signal...</font>");
                    minorStatusLabel.setIcon(CoreConstants.ICON_LOADING);
                    JobService.Util.getInstance().sendTaskSignal(simulationID, taskID, taskStatus, callback);
                }
            }
        });
    }

    public String getTaskID() {
        return taskID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status, int exitCode, int minorStatus) {

        this.status = status;
        this.exitCode = exitCode;
        this.minorStatus = minorStatus;

        this.statusLabel.setContents(status.name());
        this.minorStatusLabel.setContents("<font color=\"#666666\">" + parseMinorStatus() + "</font>");
        configureActionLabels();
    }
}
