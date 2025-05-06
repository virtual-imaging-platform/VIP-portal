/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.WorkflowStatsChart;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.JobStatsChart;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class StatsTab extends Tab {

    private List<Simulation> simulationsList;
    private VLayout leftVLayout;
    private VLayout rightVLayout;
    private SelectItem chartsItem;
    private TextItem binItem;
    private ListGrid grid;
    private IButton generateButton;

    public StatsTab() {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_CHART) + " Performance Statistics");
        this.setID(ApplicationConstants.TAB_STATS);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        leftVLayout = new VLayout(15);
        leftVLayout.setWidth(280);
        leftVLayout.setHeight100();
        leftVLayout.setOverflow(Overflow.AUTO);

        rightVLayout = new VLayout(15);
        rightVLayout.setWidth100();
        rightVLayout.setHeight(600);
        rightVLayout.setOverflow(Overflow.AUTO);
        rightVLayout.setPadding(10);

        configureForm();
        configureGrid();

        HLayout hLayout = new HLayout(10);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.addMember(leftVLayout);
        hLayout.addMember(rightVLayout);

        this.setPane(hLayout);
    }

    private void configureForm() {

        LinkedHashMap<String, String> chartsMap = new LinkedHashMap<String, String>();
        chartsMap.put("1", "Job Stats");
        chartsMap.put("2", "Workflows per User");
        chartsMap.put("3", "Workflows per Application");
        chartsItem = new SelectItem();
        chartsItem.setShowTitle(false);
        chartsItem.setWidth(250);
        chartsItem.setValueMap(chartsMap);
        chartsItem.setEmptyDisplayValue("Select a chart...");

        generateButton = WidgetUtil.getIButton("Get Stats", null, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                generateChart();
            }
        });

        VLayout formLayout = WidgetUtil.getVIPLayout(280, 185, false);
        formLayout.addMember(WidgetUtil.getLabel("<b>Performance Statistics</b>", ApplicationConstants.ICON_CHART, 30));
        WidgetUtil.addFieldToVIPLayout(formLayout, "Chart", chartsItem);
        //WidgetUtil.addFieldToVIPLayout(formLayout, "Bin Size", binItem);
        formLayout.addMember(generateButton);

        leftVLayout.addMember(formLayout);
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth(280);
        grid.setHeight(145);
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");
        valueField.setAlign(Alignment.RIGHT);
        valueField.setCellFormatter(new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {

                if (value == null) {
                    return null;
                }
                try {
                    NumberFormat nf = NumberFormat.getDecimalFormat();
                    return nf.format(Double.parseDouble((String) value));
                } catch (Exception e) {
                    return value.toString();
                }
            }
        });

        grid.setFields(propertyField, valueField);
        grid.setCanSelectCells(true);
        grid.setCanEdit(true);
        //grid.setAutoFetchData(true);
        //grid.setCanDragSelect(true);
        grid.setCanSelectText(true);
        grid.setCursor(Cursor.TEXT);

        leftVLayout.addMember(grid);
    }

    private void generateChart() {

        int value = Integer.parseInt(chartsItem.getValueAsString());
        switch (value) {
            case 1:
                plotJobsStats();
                break;
            case 2:
                plotWorkflowsPerUser();
                break;
            case 3:
                plotApplications();
                break;
        }
    }

    private void plotWorkflowsPerUser() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                //modal.hide();
                Layout.getInstance().setWarningMessage("Unable to load chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                if (result != null) {
                    if (result.isEmpty()) {
                        Layout.getInstance().setWarningMessage("Result set is empty!<br />");
                    } else {
                        new WorkflowStatsChart(result, rightVLayout, grid).build();
                        resetGenerateButton();
                        /*
                        PropertyRecord[] p=new PropertyRecord[result.size()];
                        for(int i=0; i< result.size(); i++){
                            if(result.get(i) != null){
                                String[] v = result.get(i).split("##");
                                p[i]=new PropertyRecord(v[0], v[1]);
                            }
                            
                        }
                        grid.setData(p);
                        grid.refreshFields();
                         * 
                         */
//                VisualizationUtils.loadVisualizationApi(getPieChartRunnable(data), PieChart.PACKAGE);
                    }
                }
            }
        };
        //modal.show("Loading Time Analysis for Completed Jobs...", true);

        service.getPerformanceStats(simulationsList, 2, callback);
    }

    private void plotJobsStats() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                //modal.hide();
                Layout.getInstance().setWarningMessage("Unable to load chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {

                if (result != null) {
                    if (result.isEmpty()) {
                        Layout.getInstance().setWarningMessage("Result set is empty!<br />");
                    } else {
                        new JobStatsChart(result, rightVLayout, grid).build();
                        resetGenerateButton();
//                VisualizationUtils.loadVisualizationApi(getPieChartRunnable(data), PieChart.PACKAGE);
                    }
                }
            }
        };
        service.getPerformanceStats(simulationsList, 1, callback);
    }

    private void plotApplications() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                //modal.hide();
                Layout.getInstance().setWarningMessage("Unable to load chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                if (result != null) {
                    if (result.isEmpty()) {
                        Layout.getInstance().setWarningMessage("Result set is empty!<br />");
                    } else {
                        new WorkflowStatsChart(result, rightVLayout, grid).build();
                        resetGenerateButton();
                        /*
                        PropertyRecord[] p=new PropertyRecord[result.size()];
                        for(int i=0; i< result.size(); i++){
                            if(result.get(i) != null){
                                String[] v = result.get(i).split("##");
                                p[i]=new PropertyRecord(v[0], v[1]);
                            }
                            
                        } 
                        grid.setData(p);
                        grid.setCursor(Cursor.TEXT); 
                         * 
                         */
//                VisualizationUtils.loadVisualizationApi(getPieChartRunnable(data), PieChart.PACKAGE);
                    }
                }
            }
        };

        service.getPerformanceStats(simulationsList, 3, callback);
    }

    private Runnable getPieChartRunnable(final Object[][] data) {

        return new Runnable() {

            @Override
            public void run() {
//                PieOptions options = PieOptions.create();
//                options.setWidth(chartWidth);
//                options.setHeight(chartHeight);
//                options.set3D(true);
//                options.setColors("#008000", "#cc0033", "#FEA101", "#669999");
//
//                DataTable dataTable = DataTable.create();
//                dataTable.addColumn(ColumnType.STRING, "Property");
//                dataTable.addColumn(ColumnType.NUMBER, "Value");
//                dataTable.addRows(3);
//
//                dataTable.setValue(0, 0, (String) data[1][0]);
//                dataTable.setValue(0, 1, (Integer) data[1][1]);
//                dataTable.setValue(1, 0, (String) data[2][0]);
//                dataTable.setValue(1, 1, (Integer) data[2][1]);
//                dataTable.setValue(2, 0, (String) data[3][0]);
//                dataTable.setValue(2, 1, (Integer) data[3][1]);
//
//                PieChart pie = new PieChart(dataTable, options);
//
//                chartLayout.removeMember(innerChartLayout);
//                innerChartLayout = new VLayout();
//                innerChartLayout.setWidth100();
//                innerChartLayout.setHeight100();
//                innerChartLayout.addMember(pie);
//                chartLayout.addMember(innerChartLayout);
            }
        };
    }

    public void setSimulationsList(List<Simulation> simulationsList) {
        this.simulationsList = simulationsList;
    }
    
        private void resetGenerateButton() {
        WidgetUtil.resetIButton(generateButton, "Get Stats", null);
    }
}
