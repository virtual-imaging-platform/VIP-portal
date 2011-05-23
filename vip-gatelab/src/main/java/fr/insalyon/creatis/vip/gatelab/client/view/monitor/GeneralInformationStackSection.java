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
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import fr.insalyon.creatis.vip.common.client.view.property.PropertyRecord;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabServiceAsync;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class GeneralInformationStackSection extends SectionStackSection {

    private String simulationID;
    private String status;
    private String date;
    private ListGrid grid;

    public GeneralInformationStackSection(String simulationID, String status, String date) {

        this.simulationID = simulationID;
        this.status = status;
        this.date = date;
        this.setTitle("General Information");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureGrid();
        this.addItem(grid);

        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");

        grid.setFields(propertyField, valueField);
        
        grid.addCellClickHandler(new CellClickHandler() {

            public void onCellClick(CellClickEvent event) {
                if (event.getRowNum() == 6 || event.getRowNum() == 7) {
                    String path = event.getRecord().getAttribute("value");
                    BrowserLayout.getInstance().loadData(path, false);
                }
            }
        });
    }

    public void loadData() {
        GateLabServiceAsync gatelabservice = GateLabService.Util.getInstance();
        final AsyncCallback<Map<String, String>> callback = new AsyncCallback<Map<String, String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get simulation data\n" + caught.getMessage());
            }

            public void onSuccess(Map<String, String> result) {
                
                PropertyRecord[] data = new PropertyRecord[]{
                    new PropertyRecord("Simulation Name", result.get("application_name")),
                    new PropertyRecord("Simulation Identifier", simulationID),
                    new PropertyRecord("Submitted Time", date),
                    new PropertyRecord("Status", status),
                    new PropertyRecord("Total Particles", result.get("particles")),
                    new PropertyRecord("Total Particles simulated", result.get("runnedparticles")),
                    new PropertyRecord("Input Folder", result.get("inputlink")),
                    new PropertyRecord("Output Folder", result.get("outputlink")),
                    new PropertyRecord("Simulation Type", result.get("simulation")),
                    new PropertyRecord("Gate Version", result.get("gate_version"))                    
                };
                grid.setData(data);
            }
        };
        gatelabservice.getGatelabWorkflowInputs(simulationID, callback);
    }
}
