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
package fr.insalyon.creatis.vip.simulatedata.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataRecord;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData.Modality;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class SimulatedDataGrid extends VLayout {

    private ListGrid grid;
    protected HandlerRegistration rowContextClickHandler;
    private String name;

    public SimulatedDataGrid(String name) {
        
        this.name = name;
        Label lab = new Label("<b> " + name + " </b>");
        lab.setAlign(Alignment.LEFT);
        lab.setWidth100();
        lab.setHeight(20);
        lab.setBackgroundColor("#F2F2F2");
        grid = new ListGrid() {
            @Override
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {

                DetailViewer detailViewer = new DetailViewer();
                detailViewer.setWidth(400);

                DetailViewerField idField = new DetailViewerField("files", "Simulated Data File");
                DetailViewerField statusField = new DetailViewerField("parameters", "Parameters");
                DetailViewerField parametersField = new DetailViewerField("model", "Model");
                //      DetailViewerField uriField = new DetailViewerField("model-uri", "Model URI");

                detailViewer.setFields(idField, statusField, parametersField);
                detailViewer.setData(new Record[]{record});

                return detailViewer;
            }
        };
        addMember(lab);
        configureGrid();

        grid.addRowMouseDownHandler(new RowMouseDownHandler() {
            @Override
            public void onRowMouseDown(RowMouseDownEvent event) {
                event.cancel();
                String model = event.getRecord().getAttribute("model-uri");
                String simulation = event.getRecord().getAttribute("simulation");
                String name = event.getRecord().getAttribute("short-model");
                String simuName = event.getRecord().getAttribute("simulation-name");
                if(!simuName.equals("unknown"))
                    new SimulatedDataContextMenu(model, name, simulation).showContextMenu();
                else
                     Layout.getInstance().setWarningMessage("Cannot find the simulation related to this data");

            }
        });

        rowContextClickHandler = grid.addRowContextClickHandler(new RowContextClickHandler() {
            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String model = event.getRecord().getAttribute("model-uri");
                String simulation = event.getRecord().getAttribute("simulation");
                String name = event.getRecord().getAttribute("short-model");
                String simuName = event.getRecord().getAttribute("simulation-name");
                if(!simuName.equals("unknown"))
                    new SimulatedDataContextMenu(model, name, simulation).showContextMenu();
                else
                     Layout.getInstance().setWarningMessage("Cannot find the simulation related to this data");
            }
        });
        grid.setCanHover(true);
        grid.setShowHover(true);
        grid.setShowHoverComponents(true);
    }

    private void configureGrid() {

        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setFields(FieldUtil.getIconGridField("icon"), 
                new ListGridField("short-files", "Simulated Data File"), 
                new ListGridField("type", "Type"), 
                new ListGridField("short-param", "Simulation Parameters"), 
                new ListGridField("short-model", "Model"), 
                new ListGridField("simulation-name", "Simulation Name"), 
                new ListGridField("date", "Simulation Date"));
        grid.setSortField("date");
        grid.setSortDirection(SortDirection.DESCENDING);

        addMember(grid);
    }

    public void loadData(List<SimulatedData> result) {
        
        Modality m = SimulatedData.parseModality(name);
        List<SimulatedDataRecord> dataList = new ArrayList<SimulatedDataRecord>();
        
        for (SimulatedData sd : result) {
            if (sd.getModality() == m) {
                dataList.add(new SimulatedDataRecord(sd.getFiles(), sd.getParameters(), 
                        sd.getModels(), sd.getSimulation(), sd.getDate(), sd.getName()));
            }
        }
        grid.setData(dataList.toArray(new SimulatedDataRecord[]{}));
    }
}
