/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.SimulationTab;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.SimulationsTab;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class SimulationsContextMenu extends Menu {

    public SimulationsContextMenu(final String simulationID, final String status) {

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem viewItem = new MenuItem("View Simulation");
        viewItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addCenterTab(new SimulationTab(simulationID, status));
            }
        });

        MenuItem killItem = new MenuItem("Kill Simulation");
        killItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to kill this simulation ("
                        + simulationID + ")?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            killSimulation(simulationID);
                        }
                    }
                });
            }
        });

        MenuItem cleanItem = new MenuItem("Clean Simulation");
        cleanItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to clean this simulation ("
                        + simulationID + ")?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            cleanSimulation(simulationID);
                        }
                    }
                });

            }
        });

        MenuItem purgeItem = new MenuItem("Purge Simulation");
        purgeItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to purge this simulation ("
                        + simulationID + ")?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            purgeSimulation(simulationID);
                        }
                    }
                });

            }
        });

        if (status.equals("Running")) {
            this.setItems(viewItem, killItem);
        } else if (status.equals("Completed") || status.equals("Killed")) {
            this.setItems(viewItem, cleanItem);
        } else if (status.equals("Cleaned")) {
            this.setItems(viewItem, purgeItem);
        }
    }

    /**
     * Sends a request to kill the workflow
     *
     * @param simulationId Workflow identification
     */
    private void killSimulation(String simulationId) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing kill simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab("simulations-tab");
                simulationsTab.loadData();
            }
        };
        service.killWorkflow(simulationId, callback);
//        Ext.get(appendID + "-workflows-grid").mask("Sending killing signal to " + workflowID + "...");
    }

    /**
     * Sends a request to clean the workflow
     * 
     * @param simulationId Worflow identification
     */
    private void cleanSimulation(String simulationId) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing clean simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab("simulations-tab");
                simulationsTab.loadData();
            }
        };
//        Authentication auth = Context.getInstance().getAuthentication();
//        service.cleanWorkflow(workflowID, auth.getUserDN(), auth.getProxyFileName(), callback);
//        Ext.get(appendID + "-workflows-grid").mask("Cleaning simulation " + workflowID + "...");
    }

    /**
     * Sends a request to purge the workflow
     *
     * @param workflowID Workflow identification
     */
    private void purgeSimulation(String workflowID) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing purge simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab("simulations-tab");
                simulationsTab.loadData();
            }
        };
        service.purgeWorkflow(workflowID, callback);
//        Ext.get(appendID + "-workflows-grid").mask("Purging simulation " + workflowID + "...");
    }
}
