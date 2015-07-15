/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.system.cache;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
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
import fr.insalyon.creatis.vip.datamanager.client.bean.DMCachedFile;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageCachedFilesTab extends AbstractManageTab {

    private ListGrid grid;
    private ModalWindow modal;

    public ManageCachedFilesTab() {

        super(DataManagerConstants.ICON_CACHE, DataManagerConstants.APP_CACHED_FILES,
                DataManagerConstants.TAB_MANAGE_CACHED_FILES);

        configureGrid();
        modal = new ModalWindow(grid);
        toolStrip = new ManageCachedFilesToolStrip(modal);

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

        ListGridField pathField = new ListGridField("path", "Grid File Name");
        ListGridField sizeField = new ListGridField("size", "Size", 120);
        ListGridField frequencyField = new ListGridField("frequency", "Frequency", 120);
        frequencyField.setType(ListGridFieldType.INTEGER);
        ListGridField lastUsageField = FieldUtil.getDateField();

        grid.setFields(pathField, sizeField, frequencyField, lastUsageField);

        grid.addCellContextClickHandler(new CellContextClickHandler() {

            @Override
            public void onCellContextClick(CellContextClickEvent event) {
                event.cancel();
                if (event.getColNum() != 0) {
                    ListGridRecord record = event.getRecord();
                    new ManageCachedFilesContextMenu(modal,
                            (CachedFileRecord) record).showContextMenu();
                }
            }
        });
    }

    public void loadData() {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<List<DMCachedFile>> callback = new AsyncCallback<List<DMCachedFile>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get operations list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<DMCachedFile> result) {
                List<CachedFileRecord> dataList = new ArrayList<CachedFileRecord>();
                for (DMCachedFile cf : result) {
                    dataList.add(new CachedFileRecord(cf.getPath(),
                            cf.getName(), cf.getSize(), cf.getFrequency(),
                            cf.getLastUsage()));
                }
                grid.setData(dataList.toArray(new CachedFileRecord[]{}));
                modal.hide();
            }
        };
        modal.show("Loading cached files...", true);
        service.getCachedFiles(callback);
    }

    public ListGridRecord[] getGridSelection() {
        return grid.getSelectedRecords();
    }
}
