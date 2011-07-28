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
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
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
    private ChartWidget chart;
    private ModalWindow modal;
    private List<Simulation> simulationsList;

    public StatsTab() {

        this.setTitle("Detailed Stats");
        this.setID("stats-tab");
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        configureForm();

        vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setMargin(5);
        vLayout.addMember(form);

        modal = new ModalWindow(vLayout);

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
        chart = new ChartWidget();
        chart.setSize("700", "370");
        vLayout.addMember(chart);
        modal = new ModalWindow(vLayout);
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
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                String[] res = result.get(0).split("##");
                Object[][] data = new Object[][]{
                    {"NumberOfCompletedJobs", new Integer(res[0])},
                    {"ExecutionTime (s)", new Integer(res[1])},
                    {"UploadTime (s)", new Integer(res[2])},
                    {"DownloadTime (s)", new Integer(res[3])}
                };
                modal.hide();
                if (chart == null) {
                    configureChart();
                }
                chart.setChartData(getPieChartData(data));
            }
        };
        modal.show("Loading Time Analysis for Completed Jobs...", true);
        service.getStats(simulationsList, 1, 1000000, callback);
    }

    private void plotJobsSummary() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                String[] res = result.get(0).split("##");
                Object[][] data = new Object[][]{
                    {"TotalNumberOfJobs", new Integer(res[0])},
                    {"Completed", new Integer(res[1])},
                    {"Failed", new Integer(res[3])},
                    {"Cancelled", new Integer(res[2])}
                };
                modal.hide();
                if (chart == null) {
                    configureChart();
                }
                chart.setChartData(getPieChartData(data));
            }
        };
        modal.show("Loading Job Statuses...", true);
        service.getStats(simulationsList, 2, 1000000, callback);
    }

    private ChartData getPieChartData(Object[][] data) {
        ChartData chartData = new ChartData();
        chartData.setBackgroundColour("#ffffff");
        chartData.setLegend(new Legend(Position.RIGHT, true));

        PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setRadius(120);
        pie.setNoLabels(true);
        pie.setTooltip("#label# #val#<br>#percent#");
        pie.setGradientFill(true);
        pie.setColours("#008000", "#cc0033", "#FEA101", "#669999");
        pie.addSlices(new PieChart.Slice((Integer) data[1][1], (String) data[1][0]));
        pie.addSlices(new PieChart.Slice((Integer) data[2][1], (String) data[2][0]));
        pie.addSlices(new PieChart.Slice((Integer) data[3][1], (String) data[3][0]));

        chartData.addElements(pie);
        pie.setAnimateOnShow(false);
        return chartData;
    }

    public void setSimulationsList(List<Simulation> simulationsList) {
        this.simulationsList = simulationsList;
    }
}
