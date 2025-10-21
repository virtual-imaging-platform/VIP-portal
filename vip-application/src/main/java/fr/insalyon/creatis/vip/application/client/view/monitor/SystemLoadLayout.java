package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.models.ApplicationStatus;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SystemLoadLayout extends VLayout {

    private Timer timer;
    private Label workflowsLabel;
    private Label runningTasksLabel;
    private Label waitingTasksLabel;

    public SystemLoadLayout(int x, int y) {

        this.setWidth(300);
        this.setHeight(120);
        this.setMembersMargin(5);
        this.setPadding(5);
        this.setBackgroundColor("#FFFFFF");
        this.setBorder("1px solid #CCCCCC");
        this.setOpacity(85);
        this.moveTo(x - getWidth(), y + 20);

        Label titleLabel = new Label("<b>System Load</b>");
        titleLabel.setIcon(ApplicationConstants.ICON_STATUS);
        titleLabel.setHeight(30);

        this.addMember(titleLabel);

        workflowsLabel = new Label("Loading Executions...");
        workflowsLabel.setHeight(18);
        this.addMember(workflowsLabel);

        runningTasksLabel = new Label("Loading Running Tasks...");
        runningTasksLabel.setHeight(18);
        this.addMember(runningTasksLabel);

        waitingTasksLabel = new Label("Loading Waiting Tasks...");
        waitingTasksLabel.setHeight(18);
        this.addMember(waitingTasksLabel);

        Label closeLabel = new Label("Close");
        closeLabel.setIcon(DataManagerConstants.OP_ICON_CLEAR);
        closeLabel.setHeight(25);
        closeLabel.setWidth100();
        closeLabel.setAlign(Alignment.RIGHT);
        closeLabel.setCursor(Cursor.HAND);
        closeLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                timer.cancel();
                destroy();
            }
        });

        this.addMember(closeLabel);

        timer = new Timer() {
            @Override
            public void run() {
                loadData();
            }
        };
        timer.scheduleRepeating(30000);
    }

    private void loadData() {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<ApplicationStatus> callback = new AsyncCallback<ApplicationStatus>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to update system load:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(ApplicationStatus result) {

                workflowsLabel.setContents("Running Executions: " + result.getRunningWorkflows());
                runningTasksLabel.setContents("Running Tasks: " + result.getRunningTasks());
                waitingTasksLabel.setContents("Waiting Tasks: " + result.getWaitingTasks());
            }
        };
        service.getApplicationStatus(callback);
    }
}
