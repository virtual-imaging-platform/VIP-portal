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
package fr.insalyon.creatis.vip.datamanager.client.view.common;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.DataRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
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
     * @param path  Grid path
     * @param refresh Not to use cached data
     */
    public static void loadData(final ModalWindow modal, final ListGrid grid,
            final BasicBrowserToolStrip toolStrip, final String path, boolean refresh) {

        if (!path.equals(DataManagerConstants.ROOT)) {
            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
            AsyncCallback<List<Data>> callback = new AsyncCallback<List<Data>>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Error executing get files list: " + caught.getMessage());
                }

                public void onSuccess(List<Data> result) {
                    if (result != null) {
                        List<DataRecord> dataList = new ArrayList<DataRecord>();
                        for (Data d : result) {
                            String replicas = "";
                            for (String replica : d.getReplicas()) {
                                if (!replicas.isEmpty()) {
                                    replicas += ", ";
                                }
                                replicas += replica;
                            }
                            dataList.add(new DataRecord(
                                    d.getType().toLowerCase(), d.getName(),
                                    (int) d.getLength(), d.getModificationDate(),
                                    replicas, d.getPermissions()));
                        }
                        toolStrip.setPath(path);
                        grid.setData(dataList.toArray(new DataRecord[]{}));
                        modal.hide();

                    } else {
                        modal.hide();
                        SC.warn("Unable to get list of files.");
                    }
                }
            };
            modal.show("Loading folder " + path + "...", true);
            Context context = Context.getInstance();
            service.listDir(context.getUser(), context.getProxyFileName(), path, refresh, callback);

        } else {

            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
            AsyncCallback<User> callback = new AsyncCallback<User>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Error executing get files list: " + caught.getMessage());
                }

                public void onSuccess(User result) {
                    toolStrip.setPath(path);

                    List<DataRecord> records = new ArrayList<DataRecord>();
                    records.add(new DataRecord("folder", DataManagerConstants.USERS_HOME));

                    for (String groupName : result.getGroups().keySet()) {
                        if (!groupName.equals("Administrator")) {
                            records.add(new DataRecord("folder", groupName 
                                    + DataManagerConstants.GROUP_APPEND));
                        }
                    }

                    records.add(new DataRecord("folder", DataManagerConstants.TRASH_HOME));

                    if (Context.getInstance().isSystemAdmin()) {
                        records.add(new DataRecord("folder", DataManagerConstants.BIOMED_HOME));
                    }

                    grid.setData(records.toArray(new DataRecord[]{}));
                    modal.hide();
                }
            };
            modal.show("Loading " + DataManagerConstants.ROOT + "...", true);
            service.getUser(Context.getInstance().getUserDN(), callback);
        }
    }
}
