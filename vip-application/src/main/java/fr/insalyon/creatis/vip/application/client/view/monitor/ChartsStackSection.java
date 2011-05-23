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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.property.PropertyRecord;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ChartsStackSection extends SectionStackSection {

    private String simulationID;
    private DynamicForm form;
    private SelectItem chartsItem;
    private TextItem binItem;
    private VLayout vLayout;
    private ChartWidget chart;
    private ListGrid grid;

    public ChartsStackSection(String simulationID) {
        this.simulationID = simulationID;
        this.setTitle("Detailed Stats");
        this.setCanCollapse(true);
        this.setExpanded(false);
        this.setResizeable(true);

        configureForm();

        vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setMargin(5);
        vLayout.addMember(form);

        this.addItem(vLayout);
    }

    private void configureForm() {
        form = new DynamicForm();
        form.setWidth(500);
        form.setNumCols(5);

        LinkedHashMap<String, String> chartsMap = new LinkedHashMap<String, String>();
        chartsMap.put("1", "Job Flow");
        chartsMap.put("2", "Histogram of Execution Times");
        chartsMap.put("3", "Histogram of Download Times");
        chartsMap.put("4", "Histogram of Upload Times");
        chartsItem = new SelectItem("charts", "Chart");
        chartsItem.setValueMap(chartsMap);
        chartsItem.setEmptyDisplayValue("Select a chart...");

        binItem = new TextItem("bin", "Bin Size");
        binItem.setWidth(50);
        binItem.setValue("100");
        binItem.setKeyPressFilter("[0-9.]");

        ButtonItem generateButtonItem = new ButtonItem("generateChart", " Generate Chart ");
        generateButtonItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                generateChart();
            }
        });
        generateButtonItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                int value = new Integer(chartsItem.getValueAsString());
                if (value == 1) {
                    binItem.setDisabled(true);
                } else {
                    binItem.setDisabled(false);
                }
            }
        });
        generateButtonItem.setStartRow(false);

        form.setItems(chartsItem, binItem, generateButtonItem);
    }

    private void generateChart() {
        int value = new Integer(chartsItem.getValueAsString());
        int binSize = 100;
        if (binItem.getValueAsString() != null && !binItem.getValueAsString().equals("")) {
            binSize = new Integer(binItem.getValueAsString());
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

    private void plotJobsPerTime() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildStackChart(result);
            }
        };
        service.getJobsPertTime(simulationID, callback);
    }

    /**
     * 
     * @param binSize Size of the group
     */
    private void plotExecutionPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildBarChartAndGrid(result, "Execution Time (sec)", "#00aa00", binSize);
            }
        };
        service.getExecutionPerNumberOfJobs(simulationID, binSize, callback);
    }

    /**
     * 
     * @param binSize
     */
    private void plotDownloadPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildBarChartAndGrid(result, "Download Time (sec)", "#6699CC", binSize);
            }
        };
        service.getDownloadPerNumberOfJobs(simulationID, binSize, callback);
    }

    /**
     * 
     * @param binSize
     */
    private void plotUploadPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get chart data\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildBarChartAndGrid(result, "Upload Time (sec)", "#CC9966", binSize);
            }
        };
        service.getUploadPerNumberOfJobs(simulationID, binSize, callback);
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

        if (chart == null) {
            configureChartAndGrid();
        }
        grid.setData(new PropertyRecord[]{});
        chart.setChartData(chartData);
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

        PropertyRecord[] data;
        if (result.size() > 0) {
            data = new PropertyRecord[]{
                new PropertyRecord("Min (s)", min + ""),
                new PropertyRecord("Max (s)", max + ""),
                new PropertyRecord("Cumulated (s)", sum + ""),
                new PropertyRecord("Average (s)", (sum / (float) count) + "")
            };
        } else {
            data = new PropertyRecord[]{
                new PropertyRecord("Min (s)", "0"),
                new PropertyRecord("Max (s)", "0"),
                new PropertyRecord("Cumulated (s)", "0"),
                new PropertyRecord("Average (s)", "0")
            };
        }

        if (chart == null) {
            configureChartAndGrid();
        }
        grid.setData(data);
        chart.setChartData(chartData);
    }

    private void configureChartAndGrid() {
        chart = new ChartWidget();
        chart.setSize("700", "370");

        grid = new ListGrid();
        grid.setWidth(300);
        grid.setHeight(130);
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");

        grid.setFields(propertyField, valueField);

        vLayout.addMember(chart);
        vLayout.addMember(grid);
    }
}
