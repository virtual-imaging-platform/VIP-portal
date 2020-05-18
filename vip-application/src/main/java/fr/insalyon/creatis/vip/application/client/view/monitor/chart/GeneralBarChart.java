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

import com.googlecode.gflot.client.Axis;
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
import com.googlecode.gflot.client.options.TickFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GeneralBarChart extends AbstractChart {

    public GeneralBarChart(List<String> data, VLayout chartLayout, ListGrid grid) {
        super(data, chartLayout, grid);
    }

    public void build(String caption, String color, int binSize) {

        chartLayout.removeMembers(chartLayout.getMembers());

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

        AxisOptions xAxisOptions = AxisOptions.create();
        xAxisOptions.setLabel(caption);

        PlotOptions plotOptions = PlotOptions.create();
        plotOptions.setGlobalSeriesOptions(globalSeriesOptions);
        plotOptions.setLegendOptions(LegendOptions.create().setShow(false));
        plotOptions.addXAxisOptions(xAxisOptions);
        plotOptions.addYAxisOptions(AxisOptions.create().setLabel("Number of Jobs"));
        plotOptions.setGridOptions(GridOptions.create().setBorderWidth(0));

        // create series
        PlotModel model = new PlotModel();
        SeriesHandler series = model.addSeries(Series.of("value", color));

        int row = 0;
        int min = Integer.MAX_VALUE;
        int max = 0;
        int sum = 0;
        int count = 0;
        final List<Integer> rangesList = new ArrayList<Integer>();

        for (String values : data) {

            addRowData(values);
            String[] res = values.split("##");
            int range = Integer.parseInt(res[0]);

            if (!rangesList.isEmpty()) {
                int estimatedRange = rangesList.get(rangesList.size() - 1) + binSize;
                while (estimatedRange != range) {
                    rangesList.add(estimatedRange);
                    series.add(DataPoint.of(row, 0));
                    row++;
                    estimatedRange += binSize;
                }
            }

            rangesList.add(range);
            if (res.length > 1) {
                int numJobs = Integer.parseInt(res[1]);
                series.add(DataPoint.of(row, numJobs));
                count += numJobs;

                if (res.length > 2) {
                    int localMin = Integer.parseInt(res[2]);
                    if (localMin < min) {
                        min = localMin;
                    }
                    if (res.length > 3) {
                        int localMax = Integer.parseInt(res[3]);
                        if (localMax > max) {
                            max = localMax;
                        }
                        if (res.length > 4) {
                            sum += new Integer(res[4]);
                        }
                    }
                }
            }
            row++;
        }

        xAxisOptions.setTicks(rangesList.size());
        xAxisOptions.setTickFormatter(new TickFormatter() {
            @Override
            public String formatTickValue(double tickValue, Axis axis) {
                if (tickValue >= 0 && tickValue < rangesList.size()) {
                    return rangesList.get((int) tickValue) + "";
                }
                return "";
            }
        });

        // create the plot
        SimplePlot plot = new SimplePlot(model, plotOptions);
        plot.setWidth(700);
        plot.setHeight(400);

        chartLayout.addMember(plot);
        chartLayout.addMember(getRowDataImg());

        grid.setData(new PropertyRecord[]{
            new PropertyRecord("Min (s)", min + ""),
            new PropertyRecord("Max (s)", max + ""),
            new PropertyRecord("Cumulated (s)", sum + ""),
            new PropertyRecord("Average (s)", (sum / (float) count) + "")
        });
    }
}
