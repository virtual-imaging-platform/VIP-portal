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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class SimulationActionsUtil {

    /**
     * Sends a request to kill the simulation
     *
     * @param modal Modal window object
     * @param simulationId Simulation identification
     */
    public static void killSimulation(final ModalWindow modal, String simulationId) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing kill simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab("simulations-tab");
                simulationsTab.loadData();
            }
        };
        service.killWorkflow(simulationId, callback);
        modal.show("Sending killing signal to " + simulationId + "...", true);
    }

    /**
     * Sends a request to clean the simulation
     * 
     * @param modal Modal window object
     * @param simulationId Simulation identification
     */
    public static void cleanSimulation(final ModalWindow modal, String simulationId) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing clean simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab("simulations-tab");
                simulationsTab.loadData();
            }
        };
        Context context = Context.getInstance();
        service.cleanWorkflow(simulationId, context.getUserDN(), context.getProxyFileName(), callback);
        modal.show("Cleaning simulation " + simulationId + "...", true);
    }

    /**
     * Sends a request to purge the simulation
     *
     * @param modal Modal window object
     * @param simulationId Simulation identification
     */
    public static void purgeSimulation(final ModalWindow modal, String simulationId) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing purge simulation\n" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab("simulations-tab");
                simulationsTab.loadData();
            }
        };
        service.purgeWorkflow(simulationId, callback);
        modal.show("Purging simulation " + simulationId + "...", true);
    }
}
