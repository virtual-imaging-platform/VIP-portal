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
package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabServiceAsync;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class GateLabSimulationToolStrip extends ToolStrip {

    private String simulationID;
    private ToolStripButton stopButton;
    private Label lastUpdated;

    public GateLabSimulationToolStrip(String simulationID, boolean completed) {

        this.setWidth100();
        this.setPadding(2);

        if (CoreModule.user.isSystemAdministrator()
                || CoreModule.user.isGroupAdmin()) {
            this.addMenuButton(new LogsMenuButton(simulationID));
        }
        configure();

        this.simulationID = simulationID;
        if (completed) {
            stopButton.setDisabled(true);
        }
    }

    protected void configure() {

        stopButton = new ToolStripButton();
        stopButton.setTitle("Stop and Merge");
        stopButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to Stop and merge the simulation \""
                        + simulationID + "\"?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value) {
                            stopSimulation();
                            stopButton.setDisabled(true);
                        }
                    }
                });
            }
        });
        this.addButton(stopButton);

        this.addFill();
        lastUpdated = new Label();
        lastUpdated.setWidth(300);
        lastUpdated.setAlign(Alignment.RIGHT);
        this.updateDate();
        this.addMember(lastUpdated);
    }

    public void updateDate() {

        this.lastUpdated.setContents("Last updated on " + new Date());

    }

    private void stopSimulation() {

        GateLabServiceAsync service = GateLabService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing stop and merge:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                SC.warn("The simulation was successfully Stopped... Starting Merge in few minutes!");
            }
        };
        service.StopWorkflowSimulation(simulationID, callback);
    }
}
