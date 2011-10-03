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
package fr.insalyon.creatis.vip.gatelab.client.view.monitor;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabServiceAsync;
import java.util.Map;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class GateLabGeneralTab extends Tab {

    private GateLabSimulationToolStrip simulationToolStrip;
    private String simulationID;
    private SimulationStatus status;
    private String date;
    private ListGrid grid;
    private ModalWindow modal;

    public GateLabGeneralTab(String simulationID, SimulationStatus status, String date, boolean completed) {

        this.simulationID = simulationID;
        this.status = status;
        this.date = date;
        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_GENERAL));
        this.setPrompt("General Information");

        simulationToolStrip=new GateLabSimulationToolStrip(simulationID, completed);

        VLayout vLayout = new VLayout();
        vLayout.setHeight(100);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.addMember(simulationToolStrip);

        configureGrid();
        vLayout.addMember(grid);
        modal = new ModalWindow(grid);

        this.setPane(vLayout);

        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight(150);
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");

        grid.setFields(propertyField, valueField);


        grid.addCellContextClickHandler(new CellContextClickHandler() {

            public void onCellContextClick(CellContextClickEvent event) {
                event.cancel();
                PropertyRecord prop = (PropertyRecord) event.getRecord();
                new GeneralInformationContextMenu(simulationID, prop, modal).showContextMenu();
            }
        });
/*
        grid.addCellClickHandler(new CellClickHandler() {

            public void onCellClick(CellClickEvent event) {
                if (event.getRowNum() == 6 || event.getRowNum() == 7) {
                    String path = event.getRecord().getAttributeAsString("value");
                    BrowserLayout.getInstance().loadData(path, false);
                }
            }
        });
 * 
 */
    }

    public void loadData() {

        simulationToolStrip.updateDate();

        GateLabServiceAsync gatelabservice = GateLabService.Util.getInstance();
        final AsyncCallback<Map<String, String>> callback = new AsyncCallback<Map<String, String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get simulation data\n" + caught.getMessage());
            }

            public void onSuccess(Map<String, String> result) {

                PropertyRecord[] data = new PropertyRecord[]{
                    //new PropertyRecord("Simulation Name", result.get("application_name")),
                    //new PropertyRecord("Simulation Identifier", simulationID),
                    new PropertyRecord("Submitted Time", date),
                    new PropertyRecord("Status", status.name()),
                    new PropertyRecord("Total Particles", result.get("particles")),
                    new PropertyRecord("Total Particles simulated", result.get("runnedparticles")),
                    new PropertyRecord("Simulation Type", result.get("simulation")),
                    new PropertyRecord("Input", result.get("inputlink")),
                    //new PropertyRecord("Output", result.get("outputlink")),
                    new PropertyRecord("Gate Release", result.get("gate_version"))
                };
                grid.setData(data);
            }
        };
        gatelabservice.getGatelabWorkflowInputs(simulationID, callback);


        WorkflowServiceAsync serviceOut = WorkflowService.Util.getInstance();
        AsyncCallback<List<InOutData>> callbackOut = new AsyncCallback<List<InOutData>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get simulation output\n" + caught.getMessage());
            }

            public void onSuccess(List<InOutData> result) {
                for (InOutData data : result) {
                    if (data.getProcessor().equals("merged_results")) {
                        grid.addData(new PropertyRecord("Output", data.getPath()));
                        break;
                    }

                }
            }
        };
        serviceOut.getOutputData(simulationID, callbackOut);

    }
}
