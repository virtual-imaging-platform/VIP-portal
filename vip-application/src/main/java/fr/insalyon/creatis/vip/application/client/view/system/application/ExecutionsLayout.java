package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.rpc.ExecutionsService;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

public class ExecutionsLayout extends VLayout {
    private ModalWindow modal;
    private ListGrid grid;
    protected HandlerRegistration rowContextClickHandler;

    public ExecutionsLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureGrid();
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
                new ListGridField("name", "Execution Name"),
                new ListGridField("version", "Version"),
                new ListGridField("status", "Status"),
                new ListGridField("author", "Author"),
                new ListGridField("comments", "Comments"));
        grid.addRowContextClickHandler(new RowContextClickHandler() {
            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                new ExecutionsContextMenu(modal).showContextMenu();
            }
        });

        this.addMember(grid);
    }

    public void loadData() {
        final AsyncCallback<List<Execution>> callback = new AsyncCallback<List<Execution>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of executions:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Execution> result) {
                modal.hide();
                List<ExecutionsRecord> dataList = new ArrayList<ExecutionsRecord>();

                for (Execution exe : result) {
                    dataList.add(new ExecutionsRecord(exe.getName(), exe.getVersion(), exe.getStatus(), exe.getAuthor(), exe.getComments()));
                }
                grid.setData(dataList.toArray(new ExecutionsRecord[]{}));
            }
        };
        modal.show("Loading executions...", true);
        ExecutionsService.Util.getInstance().getExecutions(callback);
    }
}
