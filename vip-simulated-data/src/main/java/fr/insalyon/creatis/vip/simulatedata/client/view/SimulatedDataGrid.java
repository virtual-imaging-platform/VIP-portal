/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataRecord;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData.Modality;
import java.util.List;

/**
 *
 * @author glatard
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
        grid = new ListGrid(){   
            @Override
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {

                DetailViewer detailViewer = new DetailViewer();
                detailViewer.setWidth(400);

                DetailViewerField idField = new DetailViewerField("file", "Simulated Data File");
                DetailViewerField statusField = new DetailViewerField("parameters", "Parameters");
                DetailViewerField parametersField = new DetailViewerField("model", "Model");

                detailViewer.setFields(idField, statusField, parametersField);
                detailViewer.setData(new Record[]{record});

                return detailViewer;
            }};
        addMember(lab);
        configureGrid();
        
        grid.addRowMouseDownHandler(new RowMouseDownHandler() {

            @Override
            public void onRowMouseDown(RowMouseDownEvent event) {
                  event.cancel();
                String file = event.getRecord().getAttribute("file");
                String parameters = event.getRecord().getAttribute("parameters");
                String model = event.getRecord().getAttribute("model");
                String simulation = event.getRecord().getAttribute("simulation");
             
                new SimulatedDataContextMenu(file,parameters,model,simulation).showContextMenu();
            }
        });
        
         rowContextClickHandler = grid.addRowContextClickHandler(new RowContextClickHandler() {
            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                String file = event.getRecord().getAttribute("file");
                String parameters = event.getRecord().getAttribute("parameters");
                String model = event.getRecord().getAttribute("model");
                String simulation = event.getRecord().getAttribute("simulation");
             
                new SimulatedDataContextMenu(file,parameters,model,simulation).showContextMenu();
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

        ListGridField icoField = FieldUtil.getIconGridField("icon");
        ListGridField fileField = new ListGridField("short-file", "Simulated Data File");
        ListGridField typeField = new ListGridField("type", "Type");
        ListGridField paramField = new ListGridField("short-param", "Parameters");
        ListGridField modelField = new ListGridField("short-model", "Model");
        ListGridField dateField = new ListGridField("date","Simulation date");
      //  ListGridField simulationField = new ListGridField("simulation", "Produced by simulation");

        grid.setFields(icoField, fileField, typeField, paramField, modelField,dateField);
        grid.setSortField("icon");
        grid.setSortDirection(SortDirection.DESCENDING);

        addMember(grid);

    }

    public void loadData(List<SimulatedData> result) {
        Modality m = SimulatedData.parseModality(name);
        SimulatedDataRecord[] data = new SimulatedDataRecord[result.size()];
        int i = 0;
        for (SimulatedData sd : result) {
            if(sd.getModality() == m)
                data[i++] = new SimulatedDataRecord(sd.getFile().toString(), sd.getType(), sd.getParameters().toString(), sd.getModel().toString(), sd.getSimulation(),sd.getDate());
        }
        grid.setData(data);


    }
}
