/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobsLayout extends VLayout {

    private String simulationID;
    private HLayout mainLayout;
    private VLayout chartLayout;
    private JobInfoLayout infoLayout;
    private HLayout commandsLayout;
    private VLayout displayLayout;
    private SimplePlot plot;
    private SeriesHandler queuedSeries;
    private SeriesHandler runningSeries;
    private SeriesHandler completedSeries;
    private SeriesHandler failedSeries;
    private Map<String, Command> commandsMap;

    public JobsLayout(String simulationID) {

        this.simulationID = simulationID;
        this.commandsMap = new HashMap<String, Command>();
        this.setOverflow(Overflow.AUTO);
        this.setMembersMargin(5);

        mainLayout = new HLayout(10);
        mainLayout.setWidth100();
        mainLayout.setHeight(220);
        this.addMember(mainLayout);

        chartLayout = new VLayout();
        chartLayout.setWidth(420);
        chartLayout.setHeight(220);
        buildPlot();
        chartLayout.addMember(plot);
        mainLayout.addMember(chartLayout);

        infoLayout = new JobInfoLayout();
        mainLayout.addMember(infoLayout);

        commandsLayout = new HLayout(10);
        commandsLayout.setWidth100();
        commandsLayout.setHeight(35);
        commandsLayout.setBorder("1px solid #CCCCCC");
        commandsLayout.setBackgroundColor("#F7F7F7");
        commandsLayout.setPadding(3);
        commandsLayout.setVisible(false);
        this.addMember(commandsLayout);

        displayLayout = new VLayout(5);
        displayLayout.setWidth100();
        displayLayout.setHeight100();
        displayLayout.setVisible(false);
        this.addMember(displayLayout);
        
        infoLayout.getInfoLayout().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (commandsLayout.isVisible()) {
                    commandsLayout.setVisible(false);
                    displayLayout.setVisible(false);
                } else {
                    commandsLayout.setVisible(true);
                    displayLayout.setVisible(true);
                }
            }
        });

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
    }

    private void loadData() {

        AsyncCallback<List<Job>> callback = new AsyncCallback<List<Job>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load jobs:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(final List<Job> result) {

                int queued = 0;
                int queuedWE = 0;
                int running = 0;
                int runningWE = 0;
                int completed = 0;
                int failed = 0;

                for (Job job : result) {

                    Command cl;
                    if (commandsMap.containsKey(job.getCommand())) {
                        cl = commandsMap.get(job.getCommand());
                    } else {
                        cl = new Command(simulationID, job.getCommand(),
                                commandsMap, displayLayout);
                        commandsMap.put(job.getCommand(), cl);
                        commandsLayout.addMember(cl);
                    }
                    cl.addJob(job);

                    switch (job.getStatus()) {
                        case Queued:
                            queued++;
                            break;
                        case Queued_with_errors:
                            queuedWE++;
                            break;
                        case Running:
                            running++;
                            break;
                        case Running_with_erros:
                            runningWE++;
                            break;
                        case Completed:
                            completed++;
                            break;
                        case Failed:
                            failed++;
                            break;
                    }
                }

                chartLayout.removeMembers(chartLayout.getMembers());
                buildPlot();
                queuedSeries.add(new DataPoint(1, queued + queuedWE));
                runningSeries.add(new DataPoint(1, running + runningWE));
                completedSeries.add(new DataPoint(1, completed));
                failedSeries.add(new DataPoint(1, failed));
                chartLayout.addMember(plot);

                if (failed > 0) {
                    infoLayout.setStatus(2);
                } else if (queuedWE > 0 || runningWE > 0) {
                    infoLayout.setStatus(1);
                } else {
                    infoLayout.setStatus(0);
                }
            }
        };
        JobService.Util.getInstance().getList(simulationID, callback);
    }

    public void update() {
        loadData();
    }
}
