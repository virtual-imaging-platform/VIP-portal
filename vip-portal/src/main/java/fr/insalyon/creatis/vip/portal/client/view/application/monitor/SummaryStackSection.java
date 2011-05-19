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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor;

import fr.insalyon.creatis.vip.portal.client.view.application.monitor.record.SummaryRecord;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.ToolTip;
import com.rednels.ofcgwt.client.model.ToolTip.MouseStyle;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.HorizontalBarChart;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import fr.insalyon.creatis.vip.portal.client.rpc.JobService;
import fr.insalyon.creatis.vip.portal.client.rpc.JobServiceAsync;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class SummaryStackSection extends SectionStackSection {

    private String simulationID;
    private boolean completed;
    private String[] states = {"Error", "Completed", "Running", "Queued", 
        "Successfully_Submitted", "Cancelled", "Stalled"};
    private ChartWidget chart;
    private ListGrid grid;

    public SummaryStackSection(String simulationID, boolean completed) {

        this.simulationID = simulationID;
        this.completed = completed;

        this.setTitle("Jobs Summary");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureChart();
        configureGrid();

        HLayout hLayout = new HLayout(10);
        hLayout.setMargin(10);
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.addMember(chart);
        hLayout.addMember(grid);

        this.addItem(hLayout);
        loadData();
    }

    private void configureChart() {
        chart = new ChartWidget();
        chart.setSize("550", "350");
        chart.setChartData(new ChartData());
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth(300);
        grid.setHeight(180);
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField statesField = new ListGridField("states", "State");
        ListGridField jobsField = new ListGridField("jobs", "Number of Jobs");

        grid.setFields(statesField, jobsField);
    }

    public void loadData() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<Map<String, Integer>> callback = new AsyncCallback<Map<String, Integer>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get workflow list\n" + caught.getMessage());
            }

            public void onSuccess(Map<String, Integer> result) {

                if (result != null && result.size() > 0) {
                    SummaryRecord[] data = new SummaryRecord[states.length];
                    int maxValue = 0;

                    int j = 0;
                    for (int i = states.length - 1; i >= 0; i--) {
                        int value = 0;
                        if (result.containsKey(states[i].toUpperCase())) {
                            value = result.get(states[i].toUpperCase());
                            if (value > maxValue) {
                                maxValue = value;
                            }
                        }
                        data[j] = new SummaryRecord(states[i], value);
                        j++;
                    }
                    grid.setData(data);
                    
                    if (completed) {
                        chart.setChartData(getPieChartData(data));
                    } else {
                        chart.setChartData(getBarChartGlassData(data, maxValue));
                    }
                }
            }

            private ChartData getBarChartGlassData(SummaryRecord[] data, int maxValue) {
                ChartData chartData = new ChartData();
                chartData.setBackgroundColour("#ffffff");

                XAxis xa = new XAxis();
                xa.setSteps(maxValue / 10);
                xa.setMax(maxValue);
                xa.setMin(0);
                xa.setOffset(Boolean.FALSE);
                chartData.setXAxis(xa);

                YAxis ya = new YAxis();
                ya.addLabels(Arrays.asList(states));
                ya.setOffset(true);
                chartData.setYAxis(ya);

                HorizontalBarChart hchart = new HorizontalBarChart();
                hchart.setColour("#00aa00");
                hchart.setTooltip("#val# jobs");
                hchart.setBarwidth(.5);

                hchart.addBars(new HorizontalBarChart.Bar(new Integer(data[0].getJobs()), "#669999"));
                hchart.addBars(new HorizontalBarChart.Bar(new Integer(data[1].getJobs()), "#993300"));
                hchart.addBars(new HorizontalBarChart.Bar(new Integer(data[2].getJobs()), "#cc9933"));
                hchart.addBars(new HorizontalBarChart.Bar(new Integer(data[3].getJobs()), "#ffff66"));
                hchart.addBars(new HorizontalBarChart.Bar(new Integer(data[4].getJobs()), "#3399ff"));
                hchart.addBars(new HorizontalBarChart.Bar(new Integer(data[5].getJobs()), "#99ff66"));
                hchart.addBars(new HorizontalBarChart.Bar(new Integer(data[6].getJobs()), "#cc0033"));
                chartData.addElements(hchart);
                chartData.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));

                return chartData;
            }
            
            private ChartData getPieChartData(SummaryRecord[] data) {
                ChartData chartData = new ChartData();
                chartData.setBackgroundColour("#ffffff");
                chartData.setLegend(new Legend(Position.RIGHT, true));

                PieChart pie = new PieChart();
                pie.setAlpha(0.5f);
                pie.setRadius(120);
                pie.setNoLabels(true);
                pie.setTooltip("#label# #val# jobs<br>#percent#");
                pie.setGradientFill(true);
                pie.setColours("#008000", "#cc0033","#FEA101", "#669999");
                pie.addSlices(new PieChart.Slice(new Integer(data[5].getJobs()), "Completed"));
                pie.addSlices(new PieChart.Slice(new Integer(data[6].getJobs()), "Error"));
                pie.addSlices(new PieChart.Slice(new Integer(data[1].getJobs()), "Cancelled"));
                pie.addSlices(new PieChart.Slice(new Integer(data[0].getJobs()), "Stalled"));
                
                chartData.addElements(pie);
                pie.setAnimateOnShow(false);
                return chartData;
            }
        };
        service.getStatusMap(simulationID, callback);
    }
}
