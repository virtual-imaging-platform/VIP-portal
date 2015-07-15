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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.menu.LogsContextMenu;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.FileOrFolderRecord;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class LogsTab extends Tab {

    protected ModalWindow modal;
    private String simulationID;
    private ListGrid grid;
    private ToolStrip toolStrip;
    private SelectItem pathItem;

    public LogsTab(String simulationID) {

        this.simulationID = simulationID;
        
        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_LOG));
        this.setPrompt("Logs");

        configureToolStrip();
        configureGrid();
        modal = new ModalWindow(grid);

        VLayout vLayout = new VLayout();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.addMember(toolStrip);
        vLayout.addMember(grid);

        this.setPane(vLayout);

        loadData("/" + simulationID);
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField icoField = FieldUtil.getIconGridField("icon");
        ListGridField nameField = new ListGridField("name", "File Name");
        ListGridField sizeField = new ListGridField("size", "Size");
        ListGridField lastModifiedField = new ListGridField("lastModified", "Last Modification Date");
        ListGridField baseDirField = new ListGridField("baseDir", "Base Path");
        baseDirField.setHidden(true);

        grid.setFields(icoField, nameField, sizeField, lastModifiedField, baseDirField);
        grid.setSortField("icon");
        grid.setSortDirection(SortDirection.DESCENDING);

        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                String type = event.getRecord().getAttributeAsString("icon");
                if (type.contains("folder")) {
                    String baseDir = event.getRecord().getAttributeAsString("baseDir");
                    String name = event.getRecord().getAttributeAsString("name");
                    loadData(baseDir + "/" + name);
                }
            }
        });
        grid.addRowContextClickHandler(new RowContextClickHandler() {

            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                showContextMenu(event.getRecord());
            }
        });
    }

    private void configureToolStrip() {
        
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        pathItem = new SelectItem("path");
        pathItem.setShowTitle(false);
        pathItem.setWidth(400);
        pathItem.setValue("/" + simulationID);
        toolStrip.addFormItem(pathItem);

        ToolStripButton folderUpButton = new ToolStripButton();
        folderUpButton.setIcon(DataManagerConstants.ICON_FOLDER_UP);
        folderUpButton.setTooltip("Folder Up");
        folderUpButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (!pathItem.getValueAsString().equals("/" + simulationID)) {
                    String newPath = pathItem.getValueAsString();
                    loadData(newPath.substring(0, newPath.lastIndexOf("/")));
                }
            }
        });
        toolStrip.addButton(folderUpButton);

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.setTooltip("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                loadData(pathItem.getValueAsString());
            }
        });
        toolStrip.addButton(refreshButton);
        
        ToolStripButton homeButton = new ToolStripButton();
        homeButton.setIcon(CoreConstants.ICON_HOME);
        homeButton.setTooltip("Home");
        homeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                loadData("/" + simulationID);
            }
        });
        toolStrip.addButton(homeButton);
    }

    public void loadData(final String baseDir) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get logs:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                FileOrFolderRecord[] data = new FileOrFolderRecord[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    String[] dataType = result.get(i).split("-#-");
                    if (dataType[1].equals("Folder")) {
                        data[i] = new FileOrFolderRecord(dataType[0], "folder", baseDir);
                    } else {
                        String[] info = dataType[0].split("##");
                        data[i] = new FileOrFolderRecord(info[0], "file", baseDir, info[1], info[2]);
                    }
                }
                pathItem.setValue(baseDir);
                grid.setData(data);
                modal.hide();
            }
        };
        modal.show("Loading Logs Data...", true);
        service.getLogs(baseDir, callback);
    }

    private void showContextMenu(ListGridRecord record) {

        String type = record.getAttributeAsString("icon");
        String dataName = record.getAttributeAsString("name");
        String folder = record.getAttributeAsString("baseDir");

        if (folder.equals("/" + simulationID)) {
            folder = "./";
        } else {
            folder = folder.replace("/" + simulationID + "", "");
        }
        new LogsContextMenu(this, simulationID, dataName,
                folder, type.contains("file")).showContextMenu();
    }

    public ModalWindow getModal() {
        return modal;
    }
}
