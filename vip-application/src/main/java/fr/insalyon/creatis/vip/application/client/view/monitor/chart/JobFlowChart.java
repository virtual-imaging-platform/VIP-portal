package fr.insalyon.creatis.vip.application.client.view.monitor.chart;

import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.BarSeriesOptions;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.GridOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.LegendOptions.LabelFormatter;
import com.googlecode.gflot.client.options.LegendOptions.LegendPosition;
import com.googlecode.gflot.client.options.LineSeriesOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobFlowChart extends AbstractChart {

    public JobFlowChart(List<String> data, VLayout chartLayout, ListGrid grid) {
        super(data, chartLayout, grid);
    }

    public void build() {

        chartLayout.removeMembers(chartLayout.getMembers());

        PlotModel model = new PlotModel();

        LineSeriesOptions lineSeriesOptions = LineSeriesOptions.create();
        lineSeriesOptions.setShow(false);
        lineSeriesOptions.setFill(true);

        BarSeriesOptions barSeriesOptions = BarSeriesOptions.create();
        barSeriesOptions.setShow(true);
        barSeriesOptions.setBarWidth(0.6);
        barSeriesOptions.setLineWidth(0.5);

        GlobalSeriesOptions globalSeriesOptions = GlobalSeriesOptions.create();
        globalSeriesOptions.setLineSeriesOptions(lineSeriesOptions);
        globalSeriesOptions.setBarsSeriesOptions(barSeriesOptions);
        globalSeriesOptions.setStack(true);

        LegendOptions legendOptions = LegendOptions.create();
        legendOptions.setPosition(LegendPosition.NORTH_WEST);
        legendOptions.setBackgroundOpacity(0.75);
        legendOptions.setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(String label, Series series) {
                return "<div style=\"font-size:8pt\">" + label + "</div>";
            }
        });

        PlotOptions plotOptions = PlotOptions.create();
        plotOptions.setGlobalSeriesOptions(globalSeriesOptions);
        plotOptions.setLegendOptions(legendOptions);
        plotOptions.addXAxisOptions(AxisOptions.create().setLabel("Jobs"));
        plotOptions.addYAxisOptions(AxisOptions.create().setLabel("Time (s)"));
        plotOptions.setGridOptions(GridOptions.create().setBorderWidth(0));

        // create series
        SeriesHandler series1 = model.addSeries(Series.of("Submitted", "#8C8063"));
        SeriesHandler series2 = model.addSeries(Series.of("Queued", "#FFC682"));
        SeriesHandler series3 = model.addSeries(Series.of("Input", "#2388E8"));
        SeriesHandler series4 = model.addSeries(Series.of("Execution", "#33AA82"));
        SeriesHandler series5 = model.addSeries(Series.of("Output", "#7F667F"));
        SeriesHandler series6 = model.addSeries(Series.of("Checkpoint Init", "#3E6864"));
        SeriesHandler series7 = model.addSeries(Series.of("Checkpoint Upload", "#1F3533"));
        SeriesHandler series8 = model.addSeries(Series.of("Error", "#7F263D"));

        // add data
        int row = 0;
        int max = 0;
        long cpuTime = 0;
        long waitingTime = 0;
        long sequentialTime = 0;

        for (String values : data) {

            addRowData(values);
            String[] v = values.split("##");
            TaskStatus status = TaskStatus.valueOf(v[0]);

            int creation = Integer.parseInt(v[1]);
            int queued = Integer.parseInt(v[2]);
            int input = Integer.parseInt(v[3]);
            int execution = Integer.parseInt(v[4]);
            int output = Integer.parseInt(v[5]);
            int checkpointInit = Integer.parseInt(v[6]);
            int checkpointUpload = Integer.parseInt(v[7]);
            int failedTime = Integer.parseInt(v[8]);

            if (status == TaskStatus.ERROR) {
                failedTime = input + execution + output + checkpointInit + checkpointUpload;
                input = 0;
                execution = 0;
                output = 0;
                checkpointInit = 0;
                checkpointUpload = 0;
            }

            series1.add(DataPoint.of(row, creation));
            series2.add(DataPoint.of(row, queued));
            series3.add(DataPoint.of(row, input));
            series4.add(DataPoint.of(row, execution));
            series5.add(DataPoint.of(row, output));
            series6.add(DataPoint.of(row, checkpointInit));
            series7.add(DataPoint.of(row, checkpointUpload));
            series8.add(DataPoint.of(row, failedTime));

            row++;
            cpuTime += execution;
            sequentialTime += input + execution + output;
            waitingTime += queued;

            int count = creation + queued + input + execution + output
                    + checkpointInit + checkpointUpload;
            if (count > max) {
                max = count;
            }
        }

        // create the plot
        SimplePlot plot = new SimplePlot(model, plotOptions);
        plot.setWidth(700);
        plot.setHeight(400);

        chartLayout.addMember(plot);
        chartLayout.addMember(getRowDataImg());

        grid.setData(new PropertyRecord[]{
            new PropertyRecord("Makespan (s)", max + ""),
            new PropertyRecord("Cumulated CPU time (s)", cpuTime + ""),
            new PropertyRecord("Speed-up", (cpuTime / (float) max) + ""),
            new PropertyRecord("Efficiency", (cpuTime / (float) sequentialTime) + ""),
            new PropertyRecord("Mean Waiting Time (s)", (waitingTime / (float) data.size()) + "")
        });
    }
}
