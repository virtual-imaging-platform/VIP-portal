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
package fr.insalyon.creatis.vip.application.client.view.monitor.chart;

import ca.nanometrics.gflot.client.*;
import ca.nanometrics.gflot.client.options.BarSeriesOptions.BarAlignment;
import ca.nanometrics.gflot.client.options.*;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class GeneralBarChart {

    public static void build(List<String> data, VLayout chartLayout, ListGrid grid,
            String caption, String color, int binSize) {

        chartLayout.removeMembers(chartLayout.getMembers());

        LineSeriesOptions lineSeriesOptions = new LineSeriesOptions();
        lineSeriesOptions.setShow(false);
        lineSeriesOptions.setFill(true);

        BarSeriesOptions barSeriesOptions = new BarSeriesOptions();
        barSeriesOptions.setShow(true);
        barSeriesOptions.setBarWidth(0.6);
        barSeriesOptions.setLineWidth(0.5);
        barSeriesOptions.setAlignment(BarAlignment.CENTER);

        GlobalSeriesOptions globalSeriesOptions = new GlobalSeriesOptions();
        globalSeriesOptions.setLineSeriesOptions(lineSeriesOptions);
        globalSeriesOptions.setBarsSeriesOptions(barSeriesOptions);
        globalSeriesOptions.setStack(true);

        AxisOptions xAxisOptions = new AxisOptions();
        xAxisOptions.setLabel(caption);

        PlotOptions plotOptions = new PlotOptions();
        plotOptions.setGlobalSeriesOptions(globalSeriesOptions);
        plotOptions.setLegendOptions(new LegendOptions().setShow(false));
        plotOptions.addXAxisOptions(xAxisOptions);
        plotOptions.addYAxisOptions(new AxisOptions().setLabel("Number of Jobs"));
        plotOptions.setGridOptions(new GridOptions().setBorderWidth(0));

        // create series
        PlotModel model = new PlotModel();
        SeriesHandler series = model.addSeries("value", color);

        int row = 0;
        int min = Integer.MAX_VALUE;
        int max = 0;
        int sum = 0;
        int count = 0;
        final List<Integer> rangesList = new ArrayList<Integer>();

        for (String values : data) {
            String[] res = values.split("##");
            int range = Integer.parseInt(res[0]);

            if (!rangesList.isEmpty()) {
                int estimatedRange = rangesList.get(rangesList.size() - 1) + binSize;
                while (estimatedRange != range) {
                    rangesList.add(estimatedRange);
                    series.add(new DataPoint(row, 0));
                    row++;
                    estimatedRange += binSize;
                }
            }

            rangesList.add(range);
            if (res.length > 1) {
                int numJobs = Integer.parseInt(res[1]);
                series.add(new DataPoint(row, numJobs));
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

        grid.setData(new PropertyRecord[]{
                    new PropertyRecord("Min (s)", min + ""),
                    new PropertyRecord("Max (s)", max + ""),
                    new PropertyRecord("Cumulated (s)", sum + ""),
                    new PropertyRecord("Average (s)", (sum / (float) count) + "")
                });
    }
}
