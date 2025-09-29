package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.JobRecord;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SummaryToolStrip extends ToolStrip {

    private ModalWindow modal;
    private ListGrid grid;
    private String simulationID;

    public SummaryToolStrip(ModalWindow modal, ListGrid grid, final String simulationID) {

        this.modal = modal;
        this.grid = grid;
        this.simulationID = simulationID;
        this.setWidth100();

        this.addButton(getToolStripButton("Replicate",
                ApplicationConstants.ICON_TASK_REPLICATE, TaskStatus.REPLICATE));
        this.addButton(getToolStripButton("Reschedule",
                ApplicationConstants.ICON_TASK_RESCHEDULE, TaskStatus.RESCHEDULE));
        this.addButton(getToolStripButton("Kill",
                ApplicationConstants.ICON_TASK_KILL, TaskStatus.KILL));
    }

    private ToolStripButton getToolStripButton(final String title, String icon,
            final TaskStatus status) {

        ToolStripButton button = new ToolStripButton(title, icon);
        button.setPrompt(title + " all selected not completed tasks.");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to " + title.toLowerCase()
                        + " all selected not completed tasks?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            sendSignal(getSelectedActiveJobs(), status);
                        }
                    }
                });
            }
        });
        return button;
    }

    private List<String> getSelectedActiveJobs() {

        List<String> selected = new ArrayList<String>();

        for (ListGridRecord record : grid.getSelectedRecords()) {
            JobRecord jobRecord = (JobRecord) record;
            TaskStatus status = TaskStatus.valueOf(jobRecord.getStatus());

            if (status == TaskStatus.QUEUED || status == TaskStatus.RUNNING
                    || status == TaskStatus.SUCCESSFULLY_SUBMITTED) {

                selected.add(jobRecord.getID());
            }
        }

        return selected;
    }

    private void sendSignal(final List<String> jobIDs, final TaskStatus status) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to send signal:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage(status.name() + " signal successfully sent to "
                        + jobIDs.size() + " jobs.");
            }
        };
        modal.show("Sending signal to selected jobs...", true);
        service.sendSignal(simulationID, jobIDs, status.name(), callback);
    }
}
