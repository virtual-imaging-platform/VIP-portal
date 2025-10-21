package fr.insalyon.creatis.vip.datamanager.client.view.system.operation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageOperationsTab extends AbstractManageTab {

    private ListGrid grid;
    private ModalWindow modal;

    public ManageOperationsTab() {

        super(DataManagerConstants.ICON_OPERATION, DataManagerConstants.APP_OPERATIONS,
                DataManagerConstants.TAB_MANAGE_OPERATIONS);

        configureGrid();
        modal = new ModalWindow(grid);
        toolStrip = new ManageOperationsToolStrip(modal);

        vLayout.addMember(toolStrip);
        vLayout.addMember(grid);

        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

        ListGridField iconField = FieldUtil.getIconGridField("typeIcon");
        iconField.setShowHover(true);
        iconField.setHoverCustomizer(new HoverCustomizer() {
            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                OperationRecord operationRecord = (OperationRecord) record;
                return operationRecord.getType();
            }
        });
        ListGridField statusField = FieldUtil.getIconGridField("statusIcon");
        statusField.setShowHover(true);
        statusField.setHoverCustomizer(new HoverCustomizer() {
            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                OperationRecord operationRecord = (OperationRecord) record;
                return operationRecord.getStatus();
            }
        });
        ListGridField sourceField = new ListGridField("name", "Source");
        ListGridField destField = new ListGridField("destination", "Destination");
        ListGridField userField = new ListGridField("owner", "User", 150);
        ListGridField dateField = FieldUtil.getDateField();

        grid.setFields(iconField, statusField, sourceField, destField, userField, dateField);

        grid.addCellContextClickHandler(new CellContextClickHandler() {
            @Override
            public void onCellContextClick(CellContextClickEvent event) {
                event.cancel();
                if (event.getColNum() != 0) {
                    ListGridRecord record = event.getRecord();
                    new ManageOperationsContextMenu(modal, (OperationRecord) record).showContextMenu();
                }
            }
        });
    }

    public void loadData() {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<List<PoolOperation>> callback = new AsyncCallback<List<PoolOperation>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of operations:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<PoolOperation> result) {

                List<OperationRecord> dataList = new ArrayList<OperationRecord>();
                for (PoolOperation o : result) {
                    dataList.add(new OperationRecord(o.getId(), o.getType().name(),
                            o.getStatus().name(), o.getSource(), o.getDest(),
                            o.getRegistration(), o.getUser()));
                }
                grid.setData(dataList.toArray(new OperationRecord[]{}));
                modal.hide();
            }
        };
        modal.show("Loading operations...", true);
        service.getPoolOperations(callback);
    }

    public ListGridRecord[] getGridSelection() {
        return grid.getSelectedRecords();
    }
}
