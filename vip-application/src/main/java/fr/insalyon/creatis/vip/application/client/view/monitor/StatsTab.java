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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class StatsTab extends Tab {

    private DynamicForm form;
    private VLayout vLayout;
    private SelectItem chartsItem;
    private ModalWindow modal;
    private List<Simulation> simulationsList;
    private VLayout chartLayout;
    private VLayout innerChartLayout;
    private int chartWidth = 780;
    private int chartHeight = 500;

    public StatsTab() {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_CHART) + " Performance Statistics");
        this.setID(ApplicationConstants.TAB_STATS);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        vLayout = new VLayout(15);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setPadding(5);
        
        configureForm();
        configureChart();

        vLayout.addMember(form);
        vLayout.addMember(chartLayout);

        modal = new ModalWindow(chartLayout);

        this.setPane(vLayout);
    }

    private void configureForm() {

        form = new DynamicForm();
        form.setWidth(500);
        form.setNumCols(5);

        LinkedHashMap<String, String> chartsMap = new LinkedHashMap<String, String>();
        chartsMap.put("1", "Detailed Time Analysis for Completed Jobs");
        chartsMap.put("2", "Job Statuses");
        chartsItem = new SelectItem("charts", "Chart");
        chartsItem.setValueMap(chartsMap);
        chartsItem.setEmptyDisplayValue("Select a chart...");
        chartsItem.setWidth(300);

        ButtonItem generateButtonItem = new ButtonItem("generateChart", " Generate Chart ");
        generateButtonItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                generateChart();
            }
        });
        generateButtonItem.setStartRow(false);

        form.setItems(chartsItem, generateButtonItem);
    }

    private void configureChart() {

        chartLayout = new VLayout();
        chartLayout.setWidth(chartWidth);
        chartLayout.setHeight(chartHeight);

        innerChartLayout = new VLayout();
        innerChartLayout.setWidth100();
        innerChartLayout.setHeight100();

        chartLayout.addMember(innerChartLayout);
    }

    private void generateChart() {

        int value = new Integer(chartsItem.getValueAsString());
        switch (value) {
            case 1:
                plotOverallStats();
                break;
            case 2:
                plotJobsSummary();
                break;
        }
    }

    private void plotOverallStats() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to load chart data:<br />" + caught.getMessage());
            }

            public void onSuccess(String result) {

                String[] res = result.split("##");
                Object[][] data = new Object[][]{
                    {"Number Of Completed Jobs", new Integer(res[0])},
                    {"Execution Time (s)", new Integer(res[1])},
                    {"Upload Time (s)", new Integer(res[2])},
                    {"Download Time (s)", new Integer(res[3])}
                };
                modal.hide();
                VisualizationUtils.loadVisualizationApi(getPieChartRunnable(data), PieChart.PACKAGE);
            }
        };
        modal.show("Loading Time Analysis for Completed Jobs...", true);
        service.getPerformanceStats(simulationsList, 1, callback);
    }

    private void plotJobsSummary() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to load chart data:<br />" + caught.getMessage());
            }

            public void onSuccess(String result) {

                String[] res = result.split("##");
                Object[][] data = new Object[][]{
                    {"TotalNumberOfJobs", new Integer(res[0])},
                    {"Completed", new Integer(res[1])},
                    {"Failed", new Integer(res[3])},
                    {"Cancelled", new Integer(res[2])}
                };
                modal.hide();
                VisualizationUtils.loadVisualizationApi(getPieChartRunnable(data), PieChart.PACKAGE);
            }
        };
        modal.show("Loading Job Statuses...", true);
        service.getPerformanceStats(simulationsList, 2, callback);
    }

    private Runnable getPieChartRunnable(final Object[][] data) {

        return new Runnable() {

            public void run() {

                PieOptions options = PieOptions.create();
                options.setWidth(chartWidth);
                options.setHeight(chartHeight);
                options.set3D(true);
                options.setColors("#008000", "#cc0033", "#FEA101", "#669999");

                DataTable dataTable = DataTable.create();
                dataTable.addColumn(ColumnType.STRING, "Property");
                dataTable.addColumn(ColumnType.NUMBER, "Value");
                dataTable.addRows(3);

                dataTable.setValue(0, 0, (String) data[1][0]);
                dataTable.setValue(0, 1, (Integer) data[1][1]);
                dataTable.setValue(1, 0, (String) data[2][0]);
                dataTable.setValue(1, 1, (Integer) data[2][1]);
                dataTable.setValue(2, 0, (String) data[3][0]);
                dataTable.setValue(2, 1, (Integer) data[3][1]);

                PieChart pie = new PieChart(dataTable, options);

                chartLayout.removeMember(innerChartLayout);
                innerChartLayout = new VLayout();
                innerChartLayout.setWidth100();
                innerChartLayout.setHeight100();
                innerChartLayout.addMember(pie);
                chartLayout.addMember(innerChartLayout);
            }
        };
    }

    public void setSimulationsList(List<Simulation> simulationsList) {
        this.simulationsList = simulationsList;
    }
}
