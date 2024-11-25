package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.application.client.view.system.applications.ExecutionsRecord;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

import java.util.ArrayList;
import java.util.List;

public class PublicExecutionsLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;

    public PublicExecutionsLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureGrid();
        this.addMember(new PublicExecutionsToolStrip());
        this.addMember(grid);

        modal = new ModalWindow(grid);

        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setFields(
                new ListGridField("id", "Execution ID"),
                new ListGridField("simulation_name", "Execution Simulation Name"),
                new ListGridField("application_name", "Execution Application Name"),
                new ListGridField("version", "Version"),
                new ListGridField("status", "Status"),
                new ListGridField("author", "Author"),
                new ListGridField("comments", "Comments"));

        grid.addRowContextClickHandler(new RowContextClickHandler() {
            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                ListGridRecord selectedRecord = grid.getSelectedRecord();
                String executionId = selectedRecord.getAttribute("id");
                PublicExecution.PublicExecutionStatus status =
                        PublicExecution.PublicExecutionStatus.valueOf(selectedRecord.getAttribute("status"));

                new PublicExecutionsContextMenu(modal, executionId, status).showContextMenu();
            }
        });

    }

    public void loadData() {
        final AsyncCallback<List<PublicExecution>> callback = new AsyncCallback<List<PublicExecution>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of executions:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<PublicExecution> result) {
                modal.hide();
                List<ExecutionsRecord> dataList = new ArrayList<ExecutionsRecord>();

                for (PublicExecution exe : result) {
                    dataList.add(new ExecutionsRecord(exe.getId(), exe.getSimulationName(), exe.getApplicationName(), exe.getApplicationVersion(), exe.getStatus(), exe.getAuthor(), exe.getComments()));
                }
                grid.setData(dataList.toArray(new ExecutionsRecord[]{}));
            }
        };
        modal.show("Loading executions...", true);
        ReproVipService.Util.getInstance().getPublicExecutions(callback);
    }
}
