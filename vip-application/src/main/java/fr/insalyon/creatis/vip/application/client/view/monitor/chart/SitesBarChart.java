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

import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.BarSeriesOptions.BarAlignment;
import ca.nanometrics.gflot.client.options.*;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class SitesBarChart extends AbstractChart {

    public SitesBarChart(List<String> data, VLayout chartLayout, ListGrid grid) {
        super(data, chartLayout, grid);
    }

    public void build() {

        chartLayout.removeMembers(chartLayout.getMembers());

        PlotModel model = new PlotModel();

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

        PlotOptions plotOptions = new PlotOptions();
        plotOptions.setGlobalSeriesOptions(globalSeriesOptions);
        plotOptions.setLegendOptions(new LegendOptions().setShow(false));
        plotOptions.addXAxisOptions(new AxisOptions().setLabel("Sites"));
        plotOptions.addYAxisOptions(new AxisOptions().setLabel("Number of Jobs"));
        plotOptions.setGridOptions(new GridOptions().setBorderWidth(0));

        // create series
        SeriesHandler series = model.addSeries("value", "#FF0000");

        int row = 0;

        for (String values : data) {
            addRowData(values);
            String[] res = values.split("##");
            series.add(new DataPoint(row, Integer.parseInt(res[1])));
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
