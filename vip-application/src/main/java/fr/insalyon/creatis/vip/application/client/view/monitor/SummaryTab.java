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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;
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
import fr.insalyon.creatis.vip.application.client.view.monitor.menu.JobsContextMenu;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.JobRecord;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.SummaryRecord;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SummaryTab extends AbstractCornerTab {

    private ModalWindow summaryModal;
    private ModalWindow detailModal;
    private String simulationID;
    private boolean completed;
    private String[] states = {"Error", "Completed", "Running", "Queued",
        "Successfully_Submitted", "Cancelled", "Stalled"};
    private VLayout chartLayout;
    private VLayout innerChartLayout;
    private ListGrid summaryGrid;
    private ListGrid detailGrid;

    public SummaryTab(String simulationID, boolean completed) {

        this.simulationID = simulationID;
        this.completed = completed;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_SUMMARY));
        this.setPrompt("Tasks Summary");

        configureChart();
        configureSummaryGrid();
        configureDetailGrid();

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();

        HLayout summaryLayout = new HLayout(10);
        summaryLayout.setPadding(10);
        summaryLayout.setHeight(330);
        summaryLayout.setWidth100();
        summaryLayout.setOverflow(Overflow.AUTO);
        summaryLayout.addMember(chartLayout);
        summaryLayout.addMember(summaryGrid);

        summaryModal = new ModalWindow(summaryLayout);
        vLayout.addMember(summaryLayout);

        detailModal = new ModalWindow(detailGrid);

        vLayout.addMember(new SummaryToolStrip(detailModal, detailGrid, simulationID));
        vLayout.addMember(detailGrid);

        this.setPane(vLayout);
        
        loadSummaryData();
        loadDetailData();
    }

    private void configureChart() {

        chartLayout = new VLayout();
        chartLayout.setWidth(600);
        chartLayout.setHeight(300);

        innerChartLayout = new VLayout();
        innerChartLayout.setWidth(600);
        innerChartLayout.setHeight(300);

        chartLayout.addMember(innerChartLayout);
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

    @Override
    public void update() {

        loadSummaryData();
        loadDetailData();
    }

    private void loadSummaryData() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<Map<String, Integer>> callback = new AsyncCallback<Map<String, Integer>>() {
            @Override
            public void onFailure(Throwable caught) {
                summaryModal.hide();
                Layout.getInstance().setWarningMessage("Unable to get summary data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, Integer> result) {
                SummaryRecord[] data = new SummaryRecord[states.length];
                int maxValue = 0;

                int j = 0;
                for (int i = states.length - 1; i >= 0; i--) {
                    int value = 0;
                    if (result.containsKey(states[i].toUpperCase())) {
                        value = result.get(states[i].toUpperCase());
                        if (value > maxValue) {
                            maxValue = value;
                        }
                    }
                    data[j] = new SummaryRecord(states[i], value);
                    j++;
                }
                summaryGrid.setData(data);

                if (completed) {
                    VisualizationUtils.loadVisualizationApi(getPieChartRunnable(data), PieChart.PACKAGE);
                } else {
                    VisualizationUtils.loadVisualizationApi(getBarChartRunnable(data), BarChart.PACKAGE);
                }
                summaryModal.hide();
            }

            private Runnable getPieChartRunnable(final SummaryRecord[] data) {

                return new Runnable() {
                    @Override
                    public void run() {

                        PieOptions options = PieOptions.create();
                        options.setWidth(600);
                        options.setHeight(300);
                        options.set3D(true);
                        options.setColors("#1A767F", "#FEA101", "#47A259", "#B00504");

                        DataTable dataTable = DataTable.create();
                        dataTable.addColumn(ColumnType.STRING, "Status");
                        dataTable.addColumn(ColumnType.NUMBER, "Amount of Jobs");
                        dataTable.addRows(4);

                        dataTable.setValue(0, 0, "Stalled");
                        dataTable.setValue(0, 1, new Integer(data[0].getJobs()));
                        dataTable.setValue(1, 0, "Cancelled");
                        dataTable.setValue(1, 1, new Integer(data[1].getJobs()));
                        dataTable.setValue(2, 0, "Completed");
                        dataTable.setValue(2, 1, new Integer(data[5].getJobs()));
                        dataTable.setValue(3, 0, "Error");
                        dataTable.setValue(3, 1, new Integer(data[6].getJobs()));

                        PieChart pie = new PieChart(dataTable, options);

                        chartLayout.removeMember(innerChartLayout);
                        innerChartLayout = new VLayout();
                        innerChartLayout.setWidth(600);
                        innerChartLayout.setHeight(300);
                        innerChartLayout.addMember(pie);
                        chartLayout.addMember(innerChartLayout);
                    }
                };
            }

            private Runnable getBarChartRunnable(final SummaryRecord[] data) {

                return new Runnable() {
                    @Override
                    public void run() {

                        Options options = Options.create();
                        options.setWidth(600);
                        options.setHeight(300);
                        options.setFontSize(10);
                        options.setColors("#1A767F", "#FF8575", "#cc9933",
                                "#E8DD80", "#64A1E8", "#47A259", "#B00504");

                        AxisOptions hAxisOptions = AxisOptions.create();
                        hAxisOptions.setTitle("Number of Jobs");
                        options.setHAxisOptions(hAxisOptions);

                        DataTable dataTable = DataTable.create();
                        dataTable.addColumn(ColumnType.STRING, "Status");
                        for (int i = 0; i < data.length; i++) {
                            dataTable.addColumn(ColumnType.NUMBER, data[i].getState());
                        }

                        dataTable.addRows(1);
                        dataTable.setValue(0, 0, "");
                        for (int i = 1; i < dataTable.getNumberOfColumns(); i++) {
                            dataTable.setValue(0, i, new Integer(data[i - 1].getJobs()));
                        }

                        BarChart bar = new BarChart(dataTable, options);

                        chartLayout.removeMember(innerChartLayout);
                        innerChartLayout = new VLayout();
                        innerChartLayout.setWidth(600);
                        innerChartLayout.setHeight(300);
                        innerChartLayout.addMember(bar);
                        chartLayout.addMember(innerChartLayout);
                    }
                };
            }
        };
        summaryModal.show("Loading data...", true);
        service.getStatusMap(simulationID, callback);
    }

    private void loadDetailData() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<Task>> callback = new AsyncCallback<List<Task>>() {
            @Override
            public void onFailure(Throwable caught) {
                detailModal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of jobs:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Task> result) {
                List<JobRecord> dataList = new ArrayList<JobRecord>();
                for (Task j : result) {
                    dataList.add(new JobRecord(j.getId(), j.getStatus(),
                            j.getCommand(), j.getFileName(), j.getExitCode(),
                            j.getSiteName(), j.getNodeName(), j.getParameters(),
                            j.getMinorStatus()));
                }
                detailGrid.setData(dataList.toArray(new JobRecord[]{}));
                detailModal.hide();
            }
        };
        detailModal.show("Loading data...", true);
        service.getJobsList(simulationID, callback);
    }
}
