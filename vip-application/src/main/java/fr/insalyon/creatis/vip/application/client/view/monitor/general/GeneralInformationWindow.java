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
package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;

/**
 *
 * @author Rafael Silva
 */
public class GeneralInformationWindow extends Window {

    private String simulationID;
    private ListGrid grid;
    private ModalWindow modal;

    public GeneralInformationWindow(String simulationID) {

        this.simulationID = simulationID;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_GENERAL) + " General Information");
        this.setWidth100();
        this.setHeight(190);
        this.setShowCloseButton(false);

        configureGrid();
        modal = new ModalWindow(grid);
        
        this.addItem(grid);
    }

    private void configureGrid() {

        grid = new ListGrid() {

            @Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {

                if (getFieldName(colNum).equals("value")) {
                    PropertyRecord propertyRecord = (PropertyRecord) record;
                    SimulationStatus status = SimulationStatus.valueOf(propertyRecord.getValue());

                    if (status == SimulationStatus.Running) {
                        return "font-weight:bold; color:#009900;";

                    } else if (status == SimulationStatus.Completed) {
                        return "font-weight:bold; color:#287fd6;";

                    } else if (status == SimulationStatus.Killed) {
                        return "font-weight:bold; color:#d64949;";
                    }
                }
                return super.getCellCSSText(record, rowNum, colNum);
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");

        grid.setFields(propertyField, valueField);
    }

    public void loadData() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Simulation> callback = new AsyncCallback<Simulation>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to load general information:<br />" + caught.getMessage());
            }

            public void onSuccess(Simulation result) {
                modal.hide();
                grid.setData(new PropertyRecord[]{
                            new PropertyRecord("Simulation Name", result.getSimulationName()),
                            new PropertyRecord("Simulation Identifier", result.getID()),
                            new PropertyRecord("Submission Time", result.getDate().toString()),
                            new PropertyRecord("Owner", result.getUserName()),
                            new PropertyRecord("Application", result.getApplication()),
                            new PropertyRecord("Status", result.getMajorStatus())
                        });
            }
        };
        modal.show("Loading data...", true);
        service.getSimulation(simulationID, callback);
    }
}
