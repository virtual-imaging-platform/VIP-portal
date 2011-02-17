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

import com.gwtext.client.widgets.Panel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.rednels.ofcgwt.client.ChartWidget;
import com.gwtext.client.data.Store;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import fr.insalyon.creatis.platform.main.client.rpc.JobService;
import fr.insalyon.creatis.platform.main.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.platform.main.client.view.common.toolbar.LogsToolbarButton;
import java.util.Map;

/**
 *
 * @author Ibrahim Kallel, Rafael Silva
 */
public class CompletedJobsSummaryPanel extends Panel {

    private String workflowID;
    private Store store;
    private ChartWidget chart;
    private String[] states = new String[]{"Error","Cancelled","Completed"};

    public CompletedJobsSummaryPanel(final String workflowID) {

        this.setId(workflowID + "-comp-job-sum");
        this.workflowID = workflowID;
        this.setTitle("Jobs Summary");
        this.setLayout(new VerticalLayout(15));
        this.setMargins(6, 6, 6, 6);
        this.setAutoScroll(true);

        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("state"),
                    new IntegerFieldDef("jobs")
                });
        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy proxy = new MemoryProxy(new Object[][]{
                    {"Cancelled", 0},
                    {"Completed", 0},
                    {"Error", 0}
                    
                });

        loadJobsData();

        store = new Store(proxy, reader);
        store.load();

        chart = new ChartWidget();
        chart.setSize("550", "350");
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
        
        this.setTopToolbar(topToolbar);
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

                    MemoryProxy proxy = new MemoryProxy(data);
                    store.setDataProxy(proxy);
                    store.load();
                    store.commitChanges();

                    chart.setChartData(getPieChartData(data));
                }
            }

            private ChartData getPieChartData(Object[][] data) {
                ChartData cd = new ChartData("Jobs Summary", "font-size: 14px; font-family: Verdana; text-align: center;");
                cd.setBackgroundColour("#ffffff");
                cd.setLegend(new Legend(Position.RIGHT, true));

                PieChart pie = new PieChart();
                pie.setAlpha(0.5f);
                pie.setRadius(120);
                pie.setNoLabels(true);
                pie.setTooltip("#label# #val# jobs<br>#percent#");
                pie.setAnimateOnShow(true);
                pie.setGradientFill(true);
                pie.setColours("#008000", "#cc0033","#FEA101");
                pie.addSlices(new PieChart.Slice((Integer) data[0][1], "Completed"));
                pie.addSlices(new PieChart.Slice((Integer) data[2][1], "Error"));
                pie.addSlices(new PieChart.Slice((Integer) data[1][1], "Cancelled"));
                
                
                cd.addElements(pie);
                pie.setAnimateOnShow(false);
                return cd;
            }
        };
        service.getStatusMap(workflowID, callback);
    }
}
