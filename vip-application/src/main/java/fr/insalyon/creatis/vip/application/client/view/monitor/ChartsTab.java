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
import com.smartgwt.client.widgets.Canvas;
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
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ChartsTab extends Tab {

    private ModalWindow modal;
    private String simulationID;
    private DynamicForm form;
    private SelectItem chartsItem;
    private TextItem binItem;
    private VLayout vLayout;
    private ChartWidget chart;
    private ListGrid grid;

    public ChartsTab(String simulationID) {

        this.simulationID = simulationID;
        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_CHART));
        this.setPrompt("Performance Statistics");

        configureForm();

        vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setPadding(10);
        vLayout.addMember(form);

        modal = new ModalWindow(vLayout);

        this.setPane(vLayout);
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
        chartsMap.put("5", "Checkpoints per job");
        chartsItem = new SelectItem("charts", "Chart");
        chartsItem.setValueMap(chartsMap);
        chartsItem.setEmptyDisplayValue("Select a chart...");
        chartsItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                int value = new Integer(chartsItem.getValueAsString());
                if (value == 1) {
                    binItem.setDisabled(true);
                } else {
                    binItem.setDisabled(false);
                }
            }
        });

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
            case 5:
                plotCkptsPerJob();
                break;
        }
    }

    private void plotJobsPerTime() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildStackChart(result);
                modal.hide();
            }
        };
        modal.show("Building chart...", true);
        service.getJobsPertTime(simulationID, callback);
    }

    private void plotCkptsPerJob() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                buildCkptChart(result);
                modal.hide();
            }
        };
        modal.show("Building chart...", true);
        service.getCkptsPerJob(simulationID, callback);
    }

    /**
     * 
     * @param binSize Size of the group
     */
    private void plotExecutionPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                modal.hide();
                buildBarChartAndGrid(result, "Execution Time (sec)", "#00aa00", binSize);
            }
        };
        modal.show("Building chart...", true);
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
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                modal.hide();
                buildBarChartAndGrid(result, "Download Time (sec)", "#6699CC", binSize);
            }
        };
        modal.show("Building chart...", true);
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
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                modal.hide();
                buildBarChartAndGrid(result, "Upload Time (sec)", "#CC9966", binSize);
            }
        };
        modal.show("Building chart...", true);
        service.getUploadPerNumberOfJobs(simulationID, binSize, callback);
    }

    private void buildCkptChart(List<String> result) {

        ChartData chartData = new ChartData();
        chartData.setBackgroundColour("#ffffff");

        StackedBarChart stack = new StackedBarChart();
        int max = 0;
        int occ_completed = 0;
        int occ_error = 0;
        int occ_stalled = 0;
        int occ_cancelled = 0;
        int nb_jobs=0;
        int failed_jobs=0;

        for (String values : result) {
            Stack s = new Stack();
            String[] v = values.split("##");

            int nb_occ = new Integer(v[1]);

            if (v[0].equals("COMPLETED")) {
                s.addStackValues(new StackedBarChart.StackValue(nb_occ, "#009966"));
                occ_completed=occ_completed+nb_occ;
                nb_jobs++;
            } else {
                if (v[0].equals("ERROR")) {
                    s.addStackValues(new StackedBarChart.StackValue(nb_occ, "#CC0033"));
                    occ_error=occ_error+nb_occ;
                    failed_jobs++;
                    nb_jobs++;
                } else {
                    if (v[0].equals("STALLED")) {
                        s.addStackValues(new StackedBarChart.StackValue(nb_occ, "#663366"));
                        occ_stalled=occ_stalled+nb_occ;
                        nb_jobs++;
                        failed_jobs++;
                    } else {
                        if (v[0].equals("CANCELLED")) {
                            s.addStackValues(new StackedBarChart.StackValue(nb_occ, "#FF9933"));
                            occ_cancelled=occ_cancelled+nb_occ;
                            nb_jobs++;
                        }
                    }
                }
            }
            stack.addStack(s);

            if (nb_occ > max) {
                max = nb_occ;
            }
        }

        stack.setKeys(
                new Keys("Completed", "#009966", 9),
                new Keys("Error", "#CC0033", 9),
                new Keys("Stalled", "#663366", 9),
                new Keys("Cancelled", "#FF9933", 9));


        chartData.addElements(stack);

        XAxis xa = new XAxis();
        xa.setRange(0, nb_jobs, (nb_jobs / 10));
        chartData.setXAxis(xa);
        chartData.setXLegend(new Text("Jobs", "{font-size: 10px; color: #000000}"));

        YAxis ya = new YAxis();
        ya.setRange(0, max, (max / 10));
        chartData.setYAxis(ya);
        chartData.setYLegend(new Text("Number of checkpoints", "{font-size: 10px; color: #000000}"));

        if (chart == null) {
            configureChartAndGrid();
        }

        PropertyRecord[] data = new PropertyRecord[]{
            new PropertyRecord("Total ckpts for completed jobs", occ_completed + ""),
            new PropertyRecord("Total ckpts for error jobs", occ_error + ""),
            new PropertyRecord("Total ckpts for stalled jobs", occ_stalled + ""),
            new PropertyRecord("Total ckpts for cancelled jobs", occ_cancelled + ""),
            new PropertyRecord("Failure rate", (failed_jobs / (float) nb_jobs) + "")
        };

        grid.setData(data);
        chart.setChartData(chartData);
    }

    private void buildStackChart(List<String> result) {

        ChartData chartData = new ChartData();
        chartData.setBackgroundColour("#ffffff");

        StackedBarChart stack = new StackedBarChart();
        int max = 0;
        int nbJobs = 0;
        long cpuTime = 0;
        long waitingTime = 0;
        long sequentialTime = 0;

        for (String values : result) {
            Stack s = new Stack();
            String[] v = values.split("##");

            int creation = new Integer(v[1]);
            int queued = new Integer(v[2]);
            int input = new Integer(v[3]) >= 0 ? new Integer(v[3]) : 0;
            int execution = new Integer(v[4]) >= 0 ? new Integer(v[4]) : 0;
            int output = new Integer(v[5]) >= 0 ? new Integer(v[5]) : 0;
            int checkpointInit = new Integer(v[6]) >= 0 ? new Integer(v[6]) : 0;
            int checkpointUpload = new Integer(v[7]) >= 0 ? new Integer(v[7]) : 0;

            int count = creation + queued + input + execution + output 
                    + checkpointInit + checkpointUpload;
            cpuTime += execution;
            sequentialTime += input + execution + output;
            nbJobs++;
            waitingTime += queued;

            if (v[0].equals("COMPLETED") || v[0].equals("STALLED") || v[0].equals("CANCELLED")) {
                
                s.addStackValues(new StackedBarChart.StackValue(creation, "#996633"));
                s.addStackValues(new StackedBarChart.StackValue(queued, "#FF9933"));
                s.addStackValues(new StackedBarChart.StackValue(input, "#3366FF"));
                s.addStackValues(new StackedBarChart.StackValue(execution, "#009966"));
                s.addStackValues(new StackedBarChart.StackValue(output, "#663366"));
                s.addStackValues(new StackedBarChart.StackValue(checkpointInit, "#E8830C"));
                s.addStackValues(new StackedBarChart.StackValue(checkpointUpload, "#E82E0C"));

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
                new Keys("Checkpoint Init", "#E8830C", 9),
                new Keys("Checkpoint Upload", "#E82E0C", 9),
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

        PropertyRecord[] data = new PropertyRecord[]{
            new PropertyRecord("Makespan (s)", max + ""),
            new PropertyRecord("Cumulated CPU time (s)", cpuTime + ""),
            new PropertyRecord("Speed-up", (cpuTime / (float) max) + ""),
            new PropertyRecord("Efficiency", (cpuTime / (float) sequentialTime) + ""),
            new PropertyRecord("Mean waiting time", (waitingTime / (float) nbJobs) + "")
        };

        grid.setData(data);
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
