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
import ca.nanometrics.gflot.client.options.LegendOptions.LabelFormatter;
import ca.nanometrics.gflot.client.options.LegendOptions.LegendPosition;
import ca.nanometrics.gflot.client.options.*;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.JobStatus;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class JobFlowChart extends AbstractChart {

    public static void build(List<String> data, VLayout chartLayout, ListGrid grid) {

        chartLayout.removeMembers(chartLayout.getMembers());

        PlotModel model = new PlotModel();

        LineSeriesOptions lineSeriesOptions = new LineSeriesOptions();
        lineSeriesOptions.setShow(false);
        lineSeriesOptions.setFill(true);

        BarSeriesOptions barSeriesOptions = new BarSeriesOptions();
        barSeriesOptions.setShow(true);
        barSeriesOptions.setBarWidth(0.6);
        barSeriesOptions.setLineWidth(0.5);

        GlobalSeriesOptions globalSeriesOptions = new GlobalSeriesOptions();
        globalSeriesOptions.setLineSeriesOptions(lineSeriesOptions);
        globalSeriesOptions.setBarsSeriesOptions(barSeriesOptions);
        globalSeriesOptions.setStack(true);

        LegendOptions legendOptions = new LegendOptions();
        legendOptions.setPosition(LegendPosition.NORTH_WEST);
        legendOptions.setBackgroundOpacity(0.75);
        legendOptions.setLabelFormatter(new LabelFormatter() {

            @Override
            public String formatLabel(String label, Series series) {
                return "<div style=\"font-size:8pt\">" + label + "</div>";
            }
        });

        PlotOptions plotOptions = new PlotOptions();
        plotOptions.setGlobalSeriesOptions(globalSeriesOptions);
        plotOptions.setLegendOptions(legendOptions);
        plotOptions.addXAxisOptions(new AxisOptions().setLabel("Jobs"));
        plotOptions.addYAxisOptions(new AxisOptions().setLabel("Time (s)"));
        plotOptions.setGridOptions(new GridOptions().setBorderWidth(0));

        // create series
        SeriesHandler series1 = model.addSeries("Submitted", "#8C8063");
        SeriesHandler series2 = model.addSeries("Queued", "#FFC682");
        SeriesHandler series3 = model.addSeries("Input", "#2388E8");
        SeriesHandler series4 = model.addSeries("Execution", "#33AA82");
        SeriesHandler series5 = model.addSeries("Output", "#7F667F");
        SeriesHandler series6 = model.addSeries("Checkpoint Init", "#3E6864");
        SeriesHandler series7 = model.addSeries("Checkpoint Upload", "#1F3533");
        SeriesHandler series8 = model.addSeries("Error", "#7F263D");

        // add data
        int row = 0;
        int max = 0;
        long cpuTime = 0;
        long waitingTime = 0;
        long sequentialTime = 0;

        for (String values : data) {

            String[] v = values.split("##");
            JobStatus status = JobStatus.valueOf(v[0]);

            int creation = Integer.parseInt(v[1]);
            int queued = Integer.parseInt(v[2]);
            int input = Integer.parseInt(v[3]);
            int execution = Integer.parseInt(v[4]);
            int output = Integer.parseInt(v[5]);
            int checkpointInit = Integer.parseInt(v[6]);
            int checkpointUpload = Integer.parseInt(v[7]);
            int failedTime = Integer.parseInt(v[8]);

            if (status == JobStatus.ERROR) {
                failedTime = input + execution + output + checkpointInit + checkpointUpload;
                input = 0;
                execution = 0;
                output = 0;
                checkpointInit = 0;
                checkpointUpload = 0;
            }

            series1.add(new DataPoint(row, creation));
            series2.add(new DataPoint(row, queued));
            series3.add(new DataPoint(row, input));
            series4.add(new DataPoint(row, execution));
            series5.add(new DataPoint(row, output));
            series6.add(new DataPoint(row, checkpointInit));
            series7.add(new DataPoint(row, checkpointUpload));
            series8.add(new DataPoint(row, failedTime));

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

        grid.setData(new PropertyRecord[]{
                    new PropertyRecord("Makespan (s)", max + ""),
                    new PropertyRecord("Cumulated CPU time (s)", cpuTime + ""),
                    new PropertyRecord("Speed-up", (cpuTime / (float) max) + ""),
                    new PropertyRecord("Efficiency", (cpuTime / (float) sequentialTime) + ""),
                    new PropertyRecord("Mean Waiting Time (s)", (waitingTime / (float) data.size()) + "")
                });
    }
}
