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
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.JobStatus;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.NodeInfoWindow;
import fr.insalyon.creatis.vip.application.client.view.monitor.ViewerWindow;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.JobRecord;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;

/**
 *
 * @author Rafael Silva
 */
public class JobsContextMenu extends Menu {

    private ModalWindow modal;
    private String simulationID;

    public JobsContextMenu(ModalWindow modal, final String simulationID, final JobRecord job) {

        this.modal = modal;
        this.simulationID = simulationID;
        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem appOutputItem = new MenuItem("View Application Output");
        appOutputItem.setIcon(ApplicationConstants.ICON_PREVIEW);
        appOutputItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Viewing Application Output for Job ID " + job.getID(),
                        simulationID, "out", job.getFileName(), ".sh.app.out").show();
            }
        });

        MenuItem appErrorItem = new MenuItem("View Application Error");
        appErrorItem.setIcon(ApplicationConstants.ICON_PREVIEW);
        appErrorItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Viewing Application Error for Job ID " + job.getID(),
                        simulationID, "err", job.getFileName(), ".sh.app.err").show();
            }
        });

        MenuItem outputItem = new MenuItem("View Output File");
        outputItem.setIcon(ApplicationConstants.ICON_PREVIEW);
        outputItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Viewing Output File for Job ID " + job.getID(),
                        simulationID, "out", job.getFileName(), ".sh.out").show();
            }
        });

        MenuItem errorItem = new MenuItem("View Error File");
        errorItem.setIcon(ApplicationConstants.ICON_PREVIEW);
        errorItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Viewing Error File for Job ID " + job.getID(),
                        simulationID, "err", job.getFileName(), ".sh.err").show();
            }
        });

        MenuItem scriptItem = new MenuItem("View Script File");
        scriptItem.setIcon(ApplicationConstants.ICON_PREVIEW);
        scriptItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Viewing Script File for Job ID " + job.getID(),
                        simulationID, "sh", job.getFileName(), ".sh").show();
            }
        });

        MenuItem nodeItem = new MenuItem("Node Information");
        nodeItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new NodeInfoWindow(simulationID, job.getID(),
                        job.getSiteName(), job.getNodeName()).show();
            }
        });

        MenuItem replicateItem = getSignalItem("Replicate", ApplicationConstants.ICON_TASK_REPLICATE,
                job.getID(), JobStatus.REPLICATE);

        MenuItem killItem = getSignalItem("Kill", ApplicationConstants.ICON_TASK_KILL,
                job.getID(), JobStatus.KILL);

        MenuItem rescheduleItem = getSignalItem("Reschedule", ApplicationConstants.ICON_TASK_RESCHEDULE,
                job.getID(), JobStatus.RESCHEDULE);

        MenuItemSeparator separator = new MenuItemSeparator();

        JobStatus status = JobStatus.valueOf(job.getStatus());
        if ((status == JobStatus.ERROR || status == JobStatus.COMPLETED
                || status == JobStatus.CANCELLED || status == JobStatus.STALLED)
                && !job.getMinorStatus().equals("Retrieving Status")) {

            this.setItems(appOutputItem, appErrorItem, separator,
                    outputItem, errorItem, separator, scriptItem,
                    separator, nodeItem);
        } else {
            this.setItems(scriptItem, separator, replicateItem, killItem,
                    rescheduleItem);
        }
    }

    private MenuItem getSignalItem(final String title, String icon,
            final String jobID, final JobStatus status) {

        MenuItem menuItem = new MenuItem("Send " + title + " Signal", icon);
        menuItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.ask("Do you really want to " + title.toLowerCase()
                        + " this task?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value) {
                            sendSignal(jobID, status);
                        }
                    }
                });
            }
        });
        return menuItem;
    }

    private void sendSignal(String jobID, JobStatus status) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing send signal: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SC.say("Signal Successfully sent.");
            }
        };
        modal.show("Sending signal to job...", true);
        service.sendSignal(simulationID, jobID, status, callback);
    }
}
