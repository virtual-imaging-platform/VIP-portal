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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
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
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Text;
import com.rednels.ofcgwt.client.model.ToolTip;
import com.rednels.ofcgwt.client.model.ToolTip.MouseStyle;
import com.rednels.ofcgwt.client.model.axis.Keys;
import com.rednels.ofcgwt.client.model.axis.Label;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;
import com.rednels.ofcgwt.client.model.elements.StackedBarChart;
import com.rednels.ofcgwt.client.model.elements.StackedBarChart.Stack;
import fr.insalyon.creatis.vip.portal.client.rpc.JobService;
import fr.insalyon.creatis.vip.portal.client.rpc.JobServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ChartsPanel extends Panel {

    private String workflowID;
    private Panel chartPanel;
    private ComboBox cb;
    private NumberField binField;

    public ChartsPanel(String workflowID) {

        this.workflowID = workflowID;
        this.setId("Charts"+workflowID);
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

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                switch (new Integer(comboBox.getValue())) {
                    case 1:
                        binField.setVisible(false);
                        break;
                    default:
                        binField.setVisible(true);
                        break;
                }
            }
        });
        fieldSet.add(cb);

        binField = new NumberField();
        binField.setValue("100");
        binField.setWidth(50);
        binField.setFieldLabel("Bin Size");
        binField.setAllowDecimals(false);
        binField.setAllowNegative(false);
        fieldSet.add(binField);

        Button generate = new Button("Generate Chart", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                generateChart();
            }
        });
        fieldSet.addButton(generate);

        this.add(fieldSet);

        chartPanel = new Panel();
        chartPanel.setBorder(false);
        this.add(chartPanel);
    }

    private Object[][] getCharts() {
        return new Object[][]{
                    {new Integer(1), "Job flow"},
                    {new Integer(2), "Histogram of Execution Times"},
                    {new Integer(3), "Histogram of Download Times"},
                    {new Integer(4), "Histogram of Upload Times"}
                };
    }

    private void generateChart() {
        int value = new Integer(cb.getValue());
        int binSize = 100;
        if (binField.getText() != null && !binField.getText().equals("")) {
            binSize = new Integer(binField.getText());
        }

        switch (value) {
            case 1:
                plotJobsPerTime();
                break;
            case 2:
                plotExecutionPerNumberOfJobs(binSize);
                break;
            case 3:
                plotDownloadPerNumberOfJobs(binSize);
                break;
            case 4:
                plotUploadPerNumberOfJobs(binSize);
                break;
        }
    }

    /**
     * 
     * @param binSize Size of the group
     */
    private void plotExecutionPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildBarChartAndGrid(result, "Execution Time (sec)", "#00aa00", binSize);
            }
        };
        service.getExecutionPerNumberOfJobs(workflowID, binSize, callback);
    }

    /**
     * 
     * @param binSize
     */
    private void plotDownloadPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildBarChartAndGrid(result, "Download Time (sec)", "#6699CC", binSize);
            }
        };
        service.getDownloadPerNumberOfJobs(workflowID, binSize, callback);
    }

    /**
     * 
     * @param binSize
     */
    private void plotUploadPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildBarChartAndGrid(result, "Upload Time (sec)", "#CC9966", binSize);
            }
        };
        service.getUploadPerNumberOfJobs(workflowID, binSize, callback);
    }

    private void plotJobsPerTime() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildStackChart(result);
            }
        };
        service.getJobsPertTime(workflowID, callback);
    }

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

        chartPanel.setWidth(550);
        chartPanel.removeAll();
        chartPanel.add(chart);

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

        chartPanel.add(grid);
        chartPanel.doLayout();
    }

    private void buildStackChart(List<String> result) {

        ChartData chartData = new ChartData();
        chartData.setBackgroundColour("#ffffff");

        StackedBarChart stack = new StackedBarChart();
        int max = 0;

        for (String values : result) {
            Stack s = new Stack();
            String[] v = values.split("##");

            int creation = new Integer(v[1]);
            int queued = new Integer(v[2]);
            int input = new Integer(v[3]) >= 0 ? new Integer(v[3]) : 0;
            int execution = new Integer(v[4]) >= 0 ? new Integer(v[4]) : 0;
            int output = new Integer(v[5]) >= 0 ? new Integer(v[5]) : 0;

            int count = creation + queued + input + execution + output;
            
            if (v[0].equals("COMPLETED")) {
                s.addStackValues(new StackedBarChart.StackValue(creation, "#996633"));
                s.addStackValues(new StackedBarChart.StackValue(queued, "#FF9933"));
                s.addStackValues(new StackedBarChart.StackValue(input, "#3366FF"));
                s.addStackValues(new StackedBarChart.StackValue(execution, "#009966"));
                s.addStackValues(new StackedBarChart.StackValue(output, "#663366"));
                
            } else {
                s.addStackValues(new StackedBarChart.StackValue(count, "#CC0033"));
            }
            stack.addStack(s);

            if (count > max) {
                max = count;
            }
        }

        stack.setKeys(
                new Keys("Submitted", "#996633", 9),
                new Keys("Queued", "#FF9933", 9),
                new Keys("Input", "#3366FF", 9),
                new Keys("Execution", "#009966", 9),
                new Keys("Output", "#663366", 9),
                new Keys("Error", "#CC0033", 9));

        chartData.addElements(stack);

        XAxis xa = new XAxis();
        xa.setLabels("");
        chartData.setXAxis(xa);
        chartData.setXLegend(new Text("Jobs", "{font-size: 10px; color: #000000}"));

        YAxis ya = new YAxis();
        ya.setRange(0, max, (max / 10));
        chartData.setYAxis(ya);
        chartData.setYLegend(new Text("Time (sec)", "{font-size: 10px; color: #000000}"));

        ChartWidget chart = new ChartWidget();
        chart.setSize("550", "370");
        chart.setChartData(chartData);

        chartPanel.setWidth(550);
        chartPanel.removeAll();
        chartPanel.add(chart);
        chartPanel.doLayout();
    }
}
