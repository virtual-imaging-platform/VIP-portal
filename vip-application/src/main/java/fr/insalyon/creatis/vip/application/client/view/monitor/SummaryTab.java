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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.options.BarSeriesOptions;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractCornerTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.menu.JobsContextMenu;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.JobRecord;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.SummaryRecord;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SummaryTab extends AbstractCornerTab {

    private ModalWindow detailModal;
    private String simulationID;
    private VLayout chartLayout;
    private SimplePlot plot;
    private SeriesHandler submittedSeries;
    private SeriesHandler queuedSeries;
    private SeriesHandler runningSeries;
    private SeriesHandler completedSeries;
    private SeriesHandler failedSeries;
    private SeriesHandler cancelledSeries;
    private SeriesHandler stalledSeries;
    private ListGrid summaryGrid;
    private ListGrid detailGrid;

    public SummaryTab(String simulationID, boolean completed) {

        this.simulationID = simulationID;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_SUMMARY));
        this.setPrompt("Tasks Summary");

        chartLayout = new VLayout();
        chartLayout.setWidth(550);
        chartLayout.setHeight(250);
        buildPlot();

        configureSummaryGrid();
        configureDetailGrid();

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();

        HLayout summaryLayout = new HLayout(10);
        summaryLayout.setPadding(10);
        summaryLayout.setHeight(270);
        summaryLayout.setWidth100();
        summaryLayout.setOverflow(Overflow.VISIBLE);
        summaryLayout.addMember(chartLayout);
        summaryLayout.addMember(summaryGrid);

        vLayout.addMember(summaryLayout);

        detailModal = new ModalWindow(detailGrid);

        vLayout.addMember(new SummaryToolStrip(detailModal, detailGrid, simulationID));
        vLayout.addMember(detailGrid);

        this.setPane(vLayout);
        
        if (completed) {
            loadData();
        }
    }

    private void buildPlot() {

        PlotOptions plotOptions = PlotOptions.create();
        plotOptions.setGlobalSeriesOptions(GlobalSeriesOptions.create()
                .setBarsSeriesOptions(BarSeriesOptions.create()
                .setShow(true).setLineWidth(1).setBarWidth(1)
                .setAlignment(BarSeriesOptions.BarAlignment.CENTER))
                .setMultipleBars(true));
        plotOptions.setLegendOptions(LegendOptions.create().setShow(true));
        plotOptions.setMultipleBars(true);
        plotOptions.addXAxisOptions(com.googlecode.gflot.client.options.AxisOptions.create().setShow(false));
        plotOptions.addYAxisOptions(com.googlecode.gflot.client.options.AxisOptions.create().setLabel("Jobs"));

        PlotModel model = new PlotModel();
        submittedSeries = model.addSeries(Series.of("<font size=\"1\">" + TaskStatus.SUCCESSFULLY_SUBMITTED.getDescription() + "</font>", TaskStatus.SUCCESSFULLY_SUBMITTED.getColor()));
        queuedSeries = model.addSeries(Series.of("<font size=\"1\">" + TaskStatus.QUEUED.getDescription() + "</font>", TaskStatus.QUEUED.getColor()));
        runningSeries = model.addSeries(Series.of("<font size=\"1\">" + TaskStatus.RUNNING.getDescription() + "</font>", TaskStatus.RUNNING.getColor()));
        completedSeries = model.addSeries(Series.of("<font size=\"1\">" + TaskStatus.COMPLETED.getDescription() + "</font>", TaskStatus.COMPLETED.getColor()));
        failedSeries = model.addSeries(Series.of("<font size=\"1\">" + TaskStatus.ERROR.getDescription() + "</font>", TaskStatus.ERROR.getColor()));
        cancelledSeries = model.addSeries(Series.of("<font size=\"1\">" + TaskStatus.CANCELLED.getDescription() + "</font>", TaskStatus.CANCELLED.getColor()));
        stalledSeries = model.addSeries(Series.of("<font size=\"1\">" + TaskStatus.STALLED.getDescription() + "</font>", TaskStatus.STALLED.getColor()));

        plot = new SimplePlot(model, plotOptions);
        plot.setWidth(550);
        plot.setHeight(250);
        chartLayout.addMember(plot);
    }

    private void configureSummaryGrid() {

        summaryGrid = new ListGrid();
        summaryGrid.setWidth(300);
        summaryGrid.setHeight(180);
        summaryGrid.setShowAllRecords(true);
        summaryGrid.setShowEmptyMessage(true);
        summaryGrid.setEmptyMessage("<br>No data available.");

        ListGridField statesField = new ListGridField("states", "State");
        ListGridField jobsField = new ListGridField("jobs", "Number of Jobs");

        summaryGrid.setFields(statesField, jobsField);
    }

    private void configureDetailGrid() {

        detailGrid = new ListGrid() {
            @Override
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {

                DetailViewer detailViewer = new DetailViewer();
                detailViewer.setWidth(400);

                DetailViewerField idField = new DetailViewerField("jobID", "Job ID");
                DetailViewerField statusField = new DetailViewerField("status", "Status");
                DetailViewerField parametersField = new DetailViewerField("parameters", "Parameters");

                detailViewer.setFields(idField, statusField, parametersField);
                detailViewer.setData(new Record[]{record});

                return detailViewer;
            }
        };
        detailGrid.setWidth100();
        detailGrid.setHeight100();
        detailGrid.setShowAllRecords(false);
        detailGrid.setShowRowNumbers(true);
        detailGrid.setShowEmptyMessage(true);
        detailGrid.setEmptyMessage("<br>No data available.");
        detailGrid.setSelectionType(SelectionStyle.SIMPLE);
        detailGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        detailGrid.setCanHover(true);
        detailGrid.setShowHover(true);
        detailGrid.setShowHoverComponents(true);

        ListGridField jobIDField = new ListGridField("jobID", "Job ID");
        ListGridField statusField = new ListGridField("status", "Status");
        ListGridField minorField = new ListGridField("minorStatus", "Minor Status");
        ListGridField commandField = new ListGridField("command", "Command");
        commandField.setHidden(true);

        detailGrid.setFields(jobIDField, statusField, minorField, commandField);

        detailGrid.setGroupStartOpen(GroupStartOpen.ALL);
        detailGrid.setGroupByField("command");

        detailGrid.addRowContextClickHandler(new RowContextClickHandler() {
            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                JobRecord job = (JobRecord) event.getRecord();
                new JobsContextMenu(detailModal, simulationID, job).showContextMenu();
            }
        });
        detailGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                detailGrid.expandRecord(event.getRecord());
            }
        });
    }

    private void loadData() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<Task>> callback = new AsyncCallback<List<Task>>() {
            @Override
            public void onFailure(Throwable caught) {
                detailModal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of jobs:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Task> result) {

                int submitted = 0;
                int queued = 0;
                int running = 0;
                int completed = 0;
                int failed = 0;
                int cancelled = 0;
                int stalled = 0;

                List<JobRecord> dataList = new ArrayList<JobRecord>();
                for (Task task : result) {
                    dataList.add(new JobRecord(task.getId(), task.getStatus().name(),
                            task.getCommand(), task.getFileName(), task.getExitCode(),
                            task.getSiteName(), task.getNodeName(), task.getParameters(),
                            task.getMinorStatus()));

                    switch (task.getStatus()) {
                        case SUCCESSFULLY_SUBMITTED:
                            submitted++;
                            break;
                        case QUEUED:
                            queued++;
                            break;
                        case RUNNING:
                        case KILL:
                        case REPLICATE:
                        case RESCHEDULE:
                            running++;
                            break;
                        case COMPLETED:
                            completed++;
                            break;
                        case ERROR:
                            failed++;
                            break;
                        case CANCELLED:
                            cancelled++;
                            break;
                        case STALLED:
                            stalled++;
                            break;
                    }
                }
                detailGrid.setData(dataList.toArray(new JobRecord[]{}));
                detailModal.hide();

                summaryGrid.setData(new SummaryRecord[]{
                    new SummaryRecord(TaskStatus.SUCCESSFULLY_SUBMITTED.getDescription(), submitted),
                    new SummaryRecord(TaskStatus.QUEUED.getDescription(), queued),
                    new SummaryRecord(TaskStatus.RUNNING.getDescription(), running),
                    new SummaryRecord(TaskStatus.COMPLETED.getDescription(), completed),
                    new SummaryRecord(TaskStatus.ERROR.getDescription(), failed),
                    new SummaryRecord(TaskStatus.CANCELLED.getDescription(), cancelled),
                    new SummaryRecord(TaskStatus.STALLED.getDescription(), stalled),});

                if (queuedSeries.getData().isEmpty()) {
                    submittedSeries.add(DataPoint.of(1, submitted));
                    queuedSeries.add(DataPoint.of(1, queued));
                    runningSeries.add(DataPoint.of(1, running));
                    completedSeries.add(DataPoint.of(1, completed));
                    failedSeries.add(DataPoint.of(1, failed));
                    cancelledSeries.add(DataPoint.of(1, cancelled));
                    stalledSeries.add(DataPoint.of(1, stalled));
                } else {
                    submittedSeries.getData().get(0).setY(submitted);
                    queuedSeries.getData().get(0).setY(queued);
                    runningSeries.getData().get(0).setY(running);
                    completedSeries.getData().get(0).setY(completed);
                    failedSeries.getData().get(0).setY(failed);
                    cancelledSeries.getData().get(0).setY(cancelled);
                    stalledSeries.getData().get(0).setY(stalled);
                }
                plot.redraw();
            }
        };
        detailModal.show("Loading data...", true);
        service.getJobsList(simulationID, callback);
    }

    @Override
    public void update() {
        loadData();
    }
}
