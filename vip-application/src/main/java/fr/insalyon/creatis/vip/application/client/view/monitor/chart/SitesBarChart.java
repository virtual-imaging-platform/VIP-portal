package fr.insalyon.creatis.vip.application.client.view.monitor.chart;

import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.BarSeriesOptions;
import com.googlecode.gflot.client.options.BarSeriesOptions.BarAlignment;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.GridOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.LineSeriesOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SitesBarChart extends AbstractChart {

    public SitesBarChart(List<String> data, VLayout chartLayout, ListGrid grid) {
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
        barSeriesOptions.setAlignment(BarAlignment.CENTER);

        GlobalSeriesOptions globalSeriesOptions = GlobalSeriesOptions.create();
        globalSeriesOptions.setLineSeriesOptions(lineSeriesOptions);
        globalSeriesOptions.setBarsSeriesOptions(barSeriesOptions);
        globalSeriesOptions.setStack(true);

        PlotOptions plotOptions = PlotOptions.create();
        plotOptions.setGlobalSeriesOptions(globalSeriesOptions);
        plotOptions.setLegendOptions(LegendOptions.create().setShow(false));
        plotOptions.addXAxisOptions(AxisOptions.create().setLabel("Sites"));
        plotOptions.addYAxisOptions(AxisOptions.create().setLabel("Number of Jobs"));
        plotOptions.setGridOptions(GridOptions.create().setBorderWidth(0));

        // create series
        SeriesHandler series = model.addSeries(Series.of("value", "#FF0000"));

        int row = 0;

        for (String values : data) {
            addRowData(values);
            String[] res = values.split("##");
            series.add(DataPoint.of(row, Integer.parseInt(res[1])));
            row++;
        }

        // create the plot
        SimplePlot plot = new SimplePlot(model, plotOptions);
        plot.setWidth(700);
        plot.setHeight(400);

        chartLayout.addMember(plot);
        chartLayout.addMember(getRowDataImg());

        grid.setData(new PropertyRecord[]{});
    }
}
