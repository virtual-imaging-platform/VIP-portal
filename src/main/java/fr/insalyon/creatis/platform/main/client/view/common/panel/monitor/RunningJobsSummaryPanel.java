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
package fr.insalyon.creatis.platform.main.client.view.common.panel.monitor;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.ToolTip;
import com.rednels.ofcgwt.client.model.ToolTip.MouseStyle;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.HorizontalBarChart;
import fr.insalyon.creatis.platform.main.client.rpc.JobService;
import fr.insalyon.creatis.platform.main.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.platform.main.client.view.common.toolbar.LogsToolbarButton;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class RunningJobsSummaryPanel extends Panel {

    private String workflowID;
    private Store store;
    private ChartWidget chart;
    private String[] states = {"Error", "Completed", "Running", "Queued", "Successfully_Submitted", "Cancelled"};
    private ToolbarTextItem lastUpdatedItem;
    private Timer timer;

    public RunningJobsSummaryPanel(final String workflowID) {

        this.setId(workflowID + "-run-job-sum");
        this.workflowID = workflowID;
        this.setTitle("Jobs Summary");
        this.setLayout(new VerticalLayout(15));
        this.setMargins(5, 5, 5, 5);
        this.setAutoScroll(true);

        loadJobsData();
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("state"),
                    new IntegerFieldDef("jobs")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy proxy = new MemoryProxy(new Object[][]{
                    {"Cancelled", 0},
                    {"Successfully_Submitted", 0},
                    {"Queued", 0},
                    {"Running", 0},
                    {"Completed", 0},
                    {"Error", 0}
                });
        store = new Store(proxy, reader);
        store.load();

        chart = new ChartWidget();
        chart.setSize("500", "300");
        chart.setChartData(new ChartData());
        this.add(chart);

        ColumnConfig stateConfig = new ColumnConfig("State", "state", 248, true);
        ColumnConfig jobsConfig = new ColumnConfig("Jobs", "jobs", 248, true);

        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
                    stateConfig,
                    jobsConfig
                });

        EditorGridPanel grid = new EditorGridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setWidth(500);

        this.add(grid);

        Toolbar topToolbar = new Toolbar();
        LogsToolbarButton logsbutton = new LogsToolbarButton(workflowID);
        topToolbar.addButton(logsbutton);
        topToolbar.addFill();
        lastUpdatedItem = new ToolbarTextItem("Last updated on " + new Date());

        topToolbar.addItem(lastUpdatedItem);
        this.setTopToolbar(topToolbar);

        timer = new Timer() {

            public void run() {
                loadJobsData();
            }
        };
        timer.scheduleRepeating(15000);
    }

    private void loadJobsData() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<Map<String, Integer>> callback = new AsyncCallback<Map<String, Integer>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get workflow list\n" + caught.getMessage());
            }

            public void onSuccess(Map<String, Integer> result) {

                if (result != null && result.size() > 0) {
                    Object[][] data = new Object[states.length][2];
                    int maxValue = 0;

                    int j = 0;
                    for (int i = states.length - 1; i >= 0; i--) {
                        data[j][0] = states[i];
                        if (result.containsKey(states[i].toUpperCase())) {
                            int value = result.get(states[i].toUpperCase());
                            data[j++][1] = value;
                            if (value > maxValue) {
                                maxValue = value;
                            }
                        } else {
                            data[j++][1] = 0;
                        }
                    }
                    lastUpdatedItem.setText("Last updated on " + new Date());

                    MemoryProxy proxy = new MemoryProxy(data);
                    store.setDataProxy(proxy);
                    store.load();
                    store.commitChanges();

                    chart.setChartData(getBarChartGlassData(data, maxValue));
                }
            }

            private ChartData getBarChartGlassData(Object[][] data, int maxValue) {
                ChartData chartData = new ChartData("Jobs Summary", "font-size: 14px; font-family: Verdana; text-align: center;");
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

                hchart.addBars(new HorizontalBarChart.Bar((Integer) data[0][1], "#993300"));
                hchart.addBars(new HorizontalBarChart.Bar((Integer) data[1][1], "#cc9933"));
                hchart.addBars(new HorizontalBarChart.Bar((Integer) data[2][1], "#ffff66"));
                hchart.addBars(new HorizontalBarChart.Bar((Integer) data[3][1], "#3399ff"));
                hchart.addBars(new HorizontalBarChart.Bar((Integer) data[4][1], "#99ff66"));
                hchart.addBars(new HorizontalBarChart.Bar((Integer) data[5][1], "#cc0033"));
                chartData.addElements(hchart);
                chartData.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));

                return chartData;
            }
        };
        service.getStatusMap(workflowID, callback);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
    }
}
