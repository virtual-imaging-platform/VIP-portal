/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.AxisOptions;
import ca.nanometrics.gflot.client.options.BarSeriesOptions;
import ca.nanometrics.gflot.client.options.GlobalSeriesOptions;
import ca.nanometrics.gflot.client.options.LegendOptions;
import ca.nanometrics.gflot.client.options.PlotOptions;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobsLayout extends VLayout {

    private String simulationID;
    private Label loadingLabel;
    private VLayout chartLayout;
    private VLayout commandsLayout;
    private Map<String, CommandLayout> commandsMap;
    private int counter;
    private SimplePlot plot;
    private SeriesHandler queuedSeries;
    private SeriesHandler runningSeries;
    private SeriesHandler completedSeries;
    private SeriesHandler failedSeries;
    private int queued;
    private int running;
    private int completed;
    private int failed;
    private boolean updating;

    public JobsLayout(String simulationID) {

        this.simulationID = simulationID;
        this.setMembersMargin(10);

        this.commandsMap = new HashMap<String, CommandLayout>();
        this.counter = 0;
        this.updating = false;

        chartLayout = new VLayout();
        buildPlot();
        chartLayout.addMember(plot);
        this.addMember(chartLayout);

        commandsLayout = new VLayout(5);
        commandsLayout.setWidth100();
        commandsLayout.setHeight100();

        loadingLabel = WidgetUtil.getLabel("Loading jobs...", CoreConstants.ICON_LOADING, 16);
        commandsLayout.addMember(loadingLabel);
        this.addMember(commandsLayout);        
    }

    private void buildPlot() {

        PlotModel model = new PlotModel();
        PlotOptions plotOptions = new PlotOptions();

        plotOptions.setGlobalSeriesOptions(new GlobalSeriesOptions()
                .setBarsSeriesOptions(new BarSeriesOptions()
                .setShow(true).setLineWidth(1).setBarWidth(1).setAlignment(BarSeriesOptions.BarAlignment.CENTER)).setMultipleBars(true));
        plotOptions.setLegendOptions(new LegendOptions().setShow(true));
        plotOptions.setMultipleBars(true);
        plotOptions.addXAxisOptions(new AxisOptions().setShow(false));
        plotOptions.addYAxisOptions(new AxisOptions().setLabel("Jobs"));

        queuedSeries = model.addSeries("<font size=\"1\">" + JobStatus.Queued.name() + "</font>", JobStatus.Queued.getColor());
        runningSeries = model.addSeries("<font size=\"1\">" + JobStatus.Running.name() + "</font>", JobStatus.Running.getColor());
        completedSeries = model.addSeries("<font size=\"1\">" + JobStatus.Completed.name() + "</font>", JobStatus.Completed.getColor());
        failedSeries = model.addSeries("<font size=\"1\">" + JobStatus.Failed.name() + "</font>", JobStatus.Failed.getColor());

        plot = new SimplePlot(model, plotOptions);
        plot.setWidth(400);
        plot.setHeight(200);

//        if (!completed) {
//            model = new PlotModel(PlotModelStrategy.slidingWindowStrategy(20));
//            plotOptions.setGlobalSeriesOptions(new GlobalSeriesOptions()
//                    .setLineSeriesOptions(new LineSeriesOptions().setLineWidth(1).setShow(true))
//                    .setPointsOptions(new PointsSeriesOptions().setRadius(2).setShow(true))
//                    .setShadowSize(0d));
//            plotOptions.addXAxisOptions(new TimeSeriesAxisOptions());
//        
//        } else {
//            plotOptions.setGlobalSeriesOptions(new GlobalSeriesOptions()
//                    .setPieSeriesOptions(new PieSeriesOptions()
//                    .setShow(true)
//                    .setRadius(1)
//                    .setInnerRadius(0.2)
//                    .setLabel(
//                    new ca.nanometrics.gflot.client.options.PieSeriesOptions.Label()
//                    .setShow(true).setRadius(3d / 4d)
//                    .setBackground(new Background().setOpacity(0.8))
//                    .setThreshold(0.05).setFormatter(new Formatter() {
//                @Override
//                public String format(String label, Series series) {
//                    return "<div style=\"font-size:8pt;text-align:center;padding:2px;color:white;\">" + label
//                            + "<br/>" + series.getData().getY(0) + " / " + series.getPercent() + "%</div>";
//                }
//            }))));
//            plotOptions.setLegendOptions(new LegendOptions().setShow(false));
//            plotOptions.setGridOptions(new GridOptions().setHoverable(true));
//        }
    }

    private void loadData() {

        AsyncCallback<List<Job>> callback = new AsyncCallback<List<Job>>() {
            @Override
            public void onFailure(Throwable caught) {

                commandsLayout.removeMember(loadingLabel);
                Layout.getInstance().setWarningMessage("Unable to load jobs:<br />" + caught.getMessage());
                updating = false;
            }

            @Override
            public void onSuccess(final List<Job> result) {

                queued = 0;
                running = 0;
                completed = 0;
                failed = 0;

                commandsLayout.removeMember(loadingLabel);
                new Timer() {
                    @Override
                    public void run() {
                        if (counter == result.size()) {
                            counter = 0;
                            cancel();
                            updatePlot();
                        } else {
                            Job job = result.get(counter++);
                            if (commandsMap.containsKey(job.getCommand())) {
                                commandsMap.get(job.getCommand()).addJob(simulationID, job);

                            } else {
                                CommandLayout cLayout = new CommandLayout(job.getCommand());
                                cLayout.addJob(simulationID, job);
                                commandsMap.put(job.getCommand(), cLayout);
                                commandsLayout.addMember(cLayout);
                            }
                            switch (job.getStatus()) {
                                case Queued:
                                    queued++;
                                    break;
                                case Running:
                                    running++;
                                    break;
                                case Completed:
                                    completed++;
                                    break;
                                default:
                                    failed++;
                                    break;
                            }
                        }
                    }
                }.scheduleRepeating(5);
            }
        };
        JobService.Util.getInstance().getList(simulationID, callback);
        updating = true;
    }

    private void updatePlot() {

        chartLayout.removeMembers(chartLayout.getMembers());
        buildPlot();
//        Date currentDate = new Date();
//        queuedSeries.add(new DataPoint(currentDate.getTime(), queued));
//        runningSeries.add(new DataPoint(currentDate.getTime(), running));
//        completedSeries.add(new DataPoint(currentDate.getTime(), completed));
//        failedSeries.add(new DataPoint(currentDate.getTime(), failed));
        queuedSeries.add(new DataPoint(1, queued));
        runningSeries.add(new DataPoint(1, running));
        completedSeries.add(new DataPoint(1, completed));
        failedSeries.add(new DataPoint(1, failed));
        chartLayout.addMember(plot);

        updating = false;
    }

    public void update() {

        if (!updating) {
            loadData();
        }
    }
}
