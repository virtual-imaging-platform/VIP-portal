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
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.JobStatus;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
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
                ApplicationConstants.ICON_TASK_REPLICATE, JobStatus.REPLICATE));
        this.addButton(getToolStripButton("Reschedule",
                ApplicationConstants.ICON_TASK_RESCHEDULE, JobStatus.RESCHEDULE));
        this.addButton(getToolStripButton("Kill",
                ApplicationConstants.ICON_TASK_KILL, JobStatus.KILL));
    }

    private ToolStripButton getToolStripButton(final String title, String icon,
            final JobStatus status) {

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
            JobStatus status = JobStatus.valueOf(jobRecord.getStatus());

            if (status == JobStatus.QUEUED || status == JobStatus.RUNNING
                    || status == JobStatus.SUCCESSFULLY_SUBMITTED) {

                selected.add(jobRecord.getID());
            }
        }

        return selected;
    }

    private void sendSignal(final List<String> jobIDs, final JobStatus status) {

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
        service.sendSignal(simulationID, jobIDs, status, callback);
    }
}
