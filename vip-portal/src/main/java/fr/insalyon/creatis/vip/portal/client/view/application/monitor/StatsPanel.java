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

import com.gwtext.client.core.Ext;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.Text;
import com.rednels.ofcgwt.client.model.ToolTip;
import com.rednels.ofcgwt.client.model.ToolTip.MouseStyle;
import com.rednels.ofcgwt.client.model.axis.Label;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.portal.client.bean.Workflow;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sorina Camarasu
 */
public class StatsPanel extends Panel {

    List<Workflow> workflowIdList;
    private Panel statsPanel;
    private ComboBox cb;
    private String id_s;


    public StatsPanel() {
        //this.workflowID = workflowID;
        //this.workflowIdList=workflowIdList;

        Date date = new Date();

        id_s = date.toString();
        this.setId("Stats");
        //this.setId(id_s);
        this.setTitle("Detailed Stats");
        this.setLayout(new VerticalLayout(15));
        this.setAutoScroll(true);
        this.setMargins(0, 0, 0, 0);
        this.setPaddings(5, 5, 5, 5);

        FieldSet fieldSet = new FieldSet("Detailed Stats");

        Store store = new SimpleStore(new String[]{"jobs-chart-id", "jobs-chart-name"}, getCharts());
        store.load();

        cb = new ComboBox();
        cb.setFieldLabel("Chart");
        cb.setEmptyText("Select a chart...");
        cb.setStore(store);
        cb.setDisplayField("jobs-chart-name");
        cb.setValueField("jobs-chart-id");
        cb.setTypeAhead(true);
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setWidth(250);
        cb.setReadOnly(true);
        cb.addListener(new ComboBoxListenerAdapter() {
        });
        fieldSet.add(cb);


        Button generate = new Button("Generate Chart", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                System.out.println("cliked generateChart !\n");
                generateChart();
            }
        });
        fieldSet.addButton(generate);

        this.add(fieldSet);

        statsPanel = new Panel();
        statsPanel.setBorder(false);
        this.add(statsPanel);
    }

    public void reloadStats(List<Workflow> workflowIdList) {
        //this.workflowIdList.clear();
        this.workflowIdList = workflowIdList;
    }

    private Object[][] getCharts() {
        return new Object[][]{
                    //{new Integer(1), "Job flow"},
                    {new Integer(1), "Detailed Time Analysis for Completed Jobs"},
                    {new Integer(2), "Job Statuses"}, // {new Integer(3), "Histogram of Upload Times"}
                };
    }

    private void generateChart() {
        int value = new Integer(cb.getValue());
        int binSize = 1000000;
        System.out.println("generateChart\n");

        switch (value) {
            //case 1:
            //    plotJobsPerTime();
            //    break;
            case 1:
                //(workflowIdList, binSize);
                plotOverallStats(workflowIdList);
                System.out.println("generateChart: case1\n");
                break;
            case 2:
                plotJobsSummary(workflowIdList);
                //plotDownloadPerNumberOfJobs(workflowIdList, binSize);
                break;
            /*
            case 3:
            plotUploadPerNumberOfJobs(workflowIdList, binSize);
            break;
             *
             */
        }
    }

    private void plotOverallStats(List<Workflow> workflowIdList) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();

        System.out.println("plotExecutionPerNumberOfJobs\n");
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                //buildBarChartAndGrid(result, "Execution Time (sec)", "#00aa00", binSize);
                //Ext.get("Stats").unmask();
                buildPieChart(result, 1);
            }
        };

        //Ext.get("Stats").mask("Computing stasts...");
        service.getStats(workflowIdList, 1, 1000000, callback);
    }

    private void plotJobsSummary(List<Workflow> workflowIdList) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();

        System.out.println("plotExecutionPerNumberOfJobs\n");
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                //buildBarChartAndGrid(result, "Execution Time (sec)", "#00aa00", binSize);
                //Ext.get("Stats").unmask();
                buildPieChart(result, 2);
            }
        };

        //Ext.get("Stats").mask("Computing stasts...");
        service.getStats(workflowIdList, 2, 1000000, callback);
    }

    /**
 

    /**
     *
     * @param result
     * @param xAxis
     * @param color
     */
    private void buildBarChartAndGrid(List<String> result, String xAxis, String color, int binSize) {

        List<Integer> rangesList = new ArrayList<Integer>();
        List<Integer> numJobsList = new ArrayList<Integer>();
        int maxRange = 0;
        int maxNumJobs = 0;
        int min = Integer.MAX_VALUE;
        int max = 0;
        int sum = 0;
        int count = 0;

        for (String s : result) {
            String[] res = s.split("##");
            int range = new Integer(res[0]);
            rangesList.add(range);
            if (range > maxRange) {
                maxRange = range;
            }
            int numJobs = new Integer(res[1]);
            numJobsList.add(numJobs);
            if (numJobs > maxNumJobs) {
                maxNumJobs = numJobs;
            }
            if (new Integer(res[2]) < min) {
                min = new Integer(res[2]);
            }
            if (new Integer(res[3]) > max) {
                max = new Integer(res[3]);
            }
            sum += new Integer(res[4]);
            count += new Integer(res[1]);
        }

        ChartData chartData = new ChartData();
        chartData.setBackgroundColour("#ffffff");

        // XAxis
        XAxis xa = new XAxis();
        int rangeLabel = 0;
        int index = 0;
        List<Integer> indexesList = new ArrayList<Integer>();
        for (Integer i : rangesList) {
            while (rangeLabel < i) {
                xa.addLabels(new Label(rangeLabel + "", 45));
                indexesList.add(index++);
                rangeLabel += binSize;
            }
            if (rangeLabel == i) {
                xa.addLabels(new Label(i + "", 45));
                index++;
                rangeLabel += binSize;
            }
        }
        chartData.setXAxis(xa);
        chartData.setXLegend(new Text(xAxis, "{font-size: 10px; color: #000000}"));

        // YAxis
        YAxis ya = new YAxis();
        ya.setSteps(maxNumJobs / 10);
        ya.setMax(maxNumJobs);
        chartData.setYAxis(ya);
        chartData.setYLegend(new Text("Number of Jobs", "{font-size: 10px; color: #000000}"));

        BarChart bchart = new BarChart(BarStyle.GLASS);
        bchart.setBarwidth(.5);
        bchart.setColour(color);
        bchart.setTooltip("#val# jobs");
        int j = 0;
        for (int i = 0; i < index; i++) {
            if (indexesList.contains(i)) {
                bchart.addValues(0);
            } else {
                bchart.addValues(numJobsList.get(j++));
            }
        }
        chartData.addElements(bchart);
        chartData.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));

        ChartWidget chart = new ChartWidget();
        chart.setSize("550", "370");
        chart.setChartData(chartData);

        statsPanel.setWidth(550);
        statsPanel.removeAll();
        statsPanel.add(chart);

        Object[][] data;
        if (result.size() > 0) {
            data = new Object[][]{
                        {"Min (s)", min},
                        {"Max (s)", max},
                        {"Cumulated (s)", sum},
                        {"Average (s)", sum / (float) count}
                    };
        } else {
            data = new Object[][]{
                        {"Min (s)", 0},
                        {"Max (s)", 0},
                        {"Cumulated (s)", 0},
                        {"Average (s)", 0}
                    };
        }

        MemoryProxy proxy = new MemoryProxy(data);
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("charts-prop"),
                    new FloatFieldDef("charts-value")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        Store store = new Store(proxy, reader);
        store.load();

        ColumnConfig propsConfig = new ColumnConfig("Properties", "charts-prop", 148, true);
        ColumnConfig valueConfig = new ColumnConfig("Value", "charts-value", 148, true);

        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
                    propsConfig,
                    valueConfig
                });

        EditorGridPanel grid = new EditorGridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setWidth(300);
        grid.setMargins(15, 0, 0, 0);

        statsPanel.add(grid);
        statsPanel.doLayout();
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
        pie.setColours("#00aa00", "#CC9966","#6699CC");

        pie.addSlices(new PieChart.Slice((Integer) data[1][1], (String) data[1][0]));
        pie.addSlices(new PieChart.Slice((Integer) data[2][1], (String) data[2][0]));
        pie.addSlices(new PieChart.Slice((Integer) data[3][1], (String) data[3][0]));

        cd.addElements(pie);
        pie.setAnimateOnShow(false);
        return cd;
    }

    private Object[][] prepareData(List<String> result, int type) {
        Object[][] data;
        List<Integer> rangesList = new ArrayList<Integer>();
        List<Integer> numJobsList = new ArrayList<Integer>();
        int numJobs = 0;

        int first = 0;
        int second = 0;
        int third = 0;

        for (String s : result) {
            String[] res = s.split("##");
            numJobs = new Integer(res[0]);

            first = new Integer(res[1]);
            second = new Integer(res[2]);
            third = new Integer(res[3]);
        }
        if (type == 1) {
            data = new Object[][]{
                        {"NumberOfCompletedJobs", numJobs},
                        {"ExecutionTime (s)", first},
                        {"UploadTime (s)", second},
                        {"DownloadTime (s)", third}
                    };
        } else {
            if (type == 2) {
                data = new Object[][]{
                            {"TotalNumberOfJobs", numJobs},
                            {"Completed", first},
                            {"Failed", third},
                            {"Cancelled", second}
                        };
            } else {
                data = new Object[][]{
                            {"0", 0},
                            {"0", 0},
                            {"0", 0},
                            {"0", 0}
                        };
            }
        }

        return data;

    }

    private void buildPieChart(List<String> result, int type) {

        Object[][] data;
        data = prepareData(result, type);

        ChartWidget chart = new ChartWidget();
        chart.setSize("550", "370");
        chart.setChartData(getPieChartData(data));

        statsPanel.setWidth(550);
        statsPanel.removeAll();
        statsPanel.add(chart);

        MemoryProxy proxy = new MemoryProxy(data);
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("charts-prop"),
                    new FloatFieldDef("charts-value")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        Store store = new Store(proxy, reader);
        store.load();

        ColumnConfig propsConfig = new ColumnConfig("Properties", "charts-prop", 148, true);
        ColumnConfig valueConfig = new ColumnConfig("Value", "charts-value", 148, true);

        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
                    propsConfig,
                    valueConfig
                });

        EditorGridPanel grid = new EditorGridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setWidth(300);
        grid.setMargins(15, 0, 0, 0);

        statsPanel.add(grid);
        statsPanel.doLayout();
    }
}
