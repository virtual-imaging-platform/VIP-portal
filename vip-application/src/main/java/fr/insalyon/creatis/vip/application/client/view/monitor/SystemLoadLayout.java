/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.ApplicationStatus;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Silva
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

        workflowsLabel = new Label("Loading Simulations...");
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
                
                SC.warn("Unable to update system load:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(ApplicationStatus result) {
                
                workflowsLabel.setContents("Running Simulations: " + result.getRunningWorkflows());
                runningTasksLabel.setContents("Running Tasks: " + result.getRunningTasks());
                waitingTasksLabel.setContents("Waiting Tasks: " + result.getWaitingTasks());
            }
        };
        service.getApplicationStatus(callback);
    }
}
