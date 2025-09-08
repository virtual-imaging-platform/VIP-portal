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
        this.status = task.getStatus();
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

        statusLabel = WidgetUtil.getLabel("<font color=\"" + status.getColor() + "\">" + status.getDescription() + "</font>", 16, Cursor.HAND);
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

        } else if (status == TaskStatus.ERROR_HELD || status == TaskStatus.STALLED_HELD) {
            Label releaseLabel = WidgetUtil.getLabel(null, ApplicationConstants.ICON_TASK_UNHOLD, 16, Cursor.HAND);
            releaseLabel.setPrompt("Release Task");
            releaseLabel.setWidth(16);
            releaseLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (status == TaskStatus.ERROR_HELD) {
                        sendSignal(TaskStatus.UNHOLD_ERROR);
                    } else if (status == TaskStatus.STALLED_HELD) {
                        sendSignal(TaskStatus.UNHOLD_STALLED);
                    }
                }
            });
            actionsLayout.addMember(releaseLabel);
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
                            statusLabel.setContents("<font color=\"" + status.getColor() + "\">" + status.getDescription() + "</font>");
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
                    statusLabel.setContents("<font color=\"" + taskStatus.getColor() + "\">" + taskStatus.getDescription() + "</font>");
                    minorStatusLabel.setContents("<font color=\"#666666\">Sending signal...</font>");
                    minorStatusLabel.setIcon(CoreConstants.ICON_LOADING);
                    JobService.Util.getInstance().sendTaskSignal(simulationID, taskID, taskStatus.name(), callback);
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

        this.statusLabel.setContents("<font color=\"" + status.getColor() + "\">" + status.getDescription() + "</font>");
        this.minorStatusLabel.setContents("<font color=\"#666666\">" + parseMinorStatus() + "</font>");
        configureActionLabels();
    }
}
