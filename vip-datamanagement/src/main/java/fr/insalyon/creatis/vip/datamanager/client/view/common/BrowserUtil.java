package fr.insalyon.creatis.vip.datamanager.client.view.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.DataRecord;
import fr.insalyon.creatis.vip.datamanager.models.Data;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class BrowserUtil {

    /**
     * Creates an elementary browser list grid.
     *
     * @return Elementary browser list grid
     */
    public static ListGrid getListGrid() {

        ListGrid grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField icoField = FieldUtil.getIconGridField("icon");
        ListGridField nameField = new ListGridField("name", "Name");
        ListGridField sizeField = new ListGridField("length", "Size", 100);
        sizeField.setType(ListGridFieldType.INTEGER);
        sizeField.setCellFormatter(FieldUtil.getSizeCellFormatter());
        ListGridField dateField = new ListGridField("modificationDate", "Modification Date", 160);

        grid.setFields(icoField, nameField, sizeField, dateField);
        grid.setSortField("icon");
        grid.setSortDirection(SortDirection.DESCENDING);

        return grid;
    }

    /**
     * Loads the data from a path to the grid and updates the tool strip.
     *
     * @param modal Modal window object
     * @param grid List grid
     * @param toolStrip Browser tool strip
     * @param path Grid path
     * @param refresh Not to use cached data
     */
    public static void loadData(final ModalWindow modal, final ListGrid grid,
            final BasicBrowserToolStrip toolStrip, final String path, boolean refresh) {

        if (!path.equals(DataManagerConstants.ROOT)) {

            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
            AsyncCallback<List<Data>> callback = new AsyncCallback<List<Data>>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    Layout.getInstance().setWarningMessage("Unable to list folder:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(List<Data> result) {
                    List<DataRecord> dataList = new ArrayList<DataRecord>();
                    for (Data data : result) {
                        String replicas = "";
                        for (String replica : data.getReplicas()) {
                            if (!replicas.isEmpty()) {
                                replicas += ", ";
                            }
                            replicas += replica;
                        }
                        dataList.add(new DataRecord(
                                data.getType(),
                                data.getName(),
                                (long) data.getLength(),
                                data.getModificationDate(),
                                replicas,
                                data.getPermissions()));
                    }
                    toolStrip.setPath(path);
                    grid.setData(dataList.toArray(new DataRecord[]{}));
                    modal.hide();
                }
            };
            modal.show("Loading folder " + path + "...", true);
            service.listDir(path, refresh, callback);

        } else {

            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
            AsyncCallback<List<Group>> callback = new AsyncCallback<>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    Layout.getInstance().setWarningMessage("Unable to list folder:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(List<Group> result) {
                    toolStrip.setPath(path);

                    List<DataRecord> records = new ArrayList<DataRecord>();
                    records.add(new DataRecord(Data.Type.folder, DataManagerConstants.USERS_HOME));
                    records.add(new DataRecord(Data.Type.folder, DataManagerConstants.TRASH_HOME));

                    for (Group group : result) {
                        records.add(new DataRecord(Data.Type.folder, group.getName()
                                + DataManagerConstants.GROUP_APPEND));
                    }

                    if (CoreModule.user.isSystemAdministrator()) {
                        records.add(new DataRecord(Data.Type.folder, DataManagerConstants.USERS_FOLDER));
                        records.add(new DataRecord(Data.Type.folder, DataManagerConstants.VO_ROOT_FOLDER));
                    }

                    grid.setData(records.toArray(new DataRecord[]{}));
                    modal.hide();
                }
            };
            modal.show("Loading " + DataManagerConstants.ROOT + "...", true);
            service.getUserGroups(callback);
        }
    }
}
