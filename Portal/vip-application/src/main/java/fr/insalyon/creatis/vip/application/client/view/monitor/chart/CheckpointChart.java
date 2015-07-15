/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
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
public class CheckpointChart extends AbstractChart {

    public CheckpointChart(List<String> data, VLayout chartLayout, ListGrid grid) {
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
        plotOptions.addYAxisOptions(AxisOptions.create().setLabel("Number of Checkpoints"));
        plotOptions.setGridOptions(GridOptions.create().setBorderWidth(0));

        // create series
        SeriesHandler series1 = model.addSeries(Series.of("Completed", "#009966"));
        SeriesHandler series2 = model.addSeries(Series.of("Error", "#CC0033"));
        SeriesHandler series3 = model.addSeries(Series.of("Stalled", "#663366"));
        SeriesHandler series4 = model.addSeries(Series.of("Cancelled", "#FF9933"));

        // add data
        int row = 0;
        int occCompleted = 0;
        int occError = 0;
        int occStalled = 0;
        int occCancelled = 0;
        int failedJobs = 0;

        for (String values : data) {

            addRowData(values);
            String[] v = values.split("##");
            TaskStatus status = TaskStatus.valueOf(v[0]);
            int nb_occ = Integer.parseInt(v[1]);

            int completed = 0;
            int error = 0;
            int stalled = 0;
            int cancelled = 0;

            switch (status) {
                case COMPLETED:
                    completed = nb_occ;
                    occCompleted += nb_occ;
                    break;
                case ERROR:
                    error = nb_occ;
                    occError += nb_occ;
                    failedJobs++;
                    break;
                case STALLED:
                    stalled = nb_occ;
                    occStalled += nb_occ;
                    failedJobs++;
                    break;
                case CANCELLED:
                    cancelled = nb_occ;
                    occCancelled += nb_occ;
            }

            series1.add(DataPoint.of(row, completed));
            series2.add(DataPoint.of(row, error));
            series3.add(DataPoint.of(row, stalled));
            series4.add(DataPoint.of(row, cancelled));
            row++;
        }

        // create the plot
        SimplePlot plot = new SimplePlot(model, plotOptions);
        plot.setWidth(700);
        plot.setHeight(400);

        chartLayout.addMember(plot);
        chartLayout.addMember(getRowDataImg());

        grid.setData(new PropertyRecord[]{
            new PropertyRecord("Ckpts for Completed Jobs", occCompleted + ""),
            new PropertyRecord("Ckpts for Error Jobs", occError + ""),
            new PropertyRecord("Ckpts for Stalled Jobs", occStalled + ""),
            new PropertyRecord("Ckpts for Cancelled Jobs", occCancelled + ""),
            new PropertyRecord("Failure rate", (failedJobs / (float) row) + "")
        });
    }
}
