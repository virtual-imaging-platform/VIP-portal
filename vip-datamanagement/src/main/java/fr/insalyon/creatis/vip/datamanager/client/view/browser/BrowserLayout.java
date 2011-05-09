/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class BrowserLayout extends VLayout {

    private static BrowserLayout instance;
    private ToolStrip toolStrip;
    private SelectItem pathItem;
    private ListGrid grid;

    public static BrowserLayout getInstance() {
        if (instance == null) {
            instance = new BrowserLayout();
        }
        return instance;
    }

    private BrowserLayout() {

        initComplete(this);
        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);
        this.setShowResizeBar(true);

        configureToolStrip();
        configureGrid();

        this.addMember(toolStrip);
        this.addMember(grid);

        loadData(DataManagerConstants.ROOT, false);

        NamedFrame frame = new NamedFrame("uploadTarget");
        frame.setVisible(false);
        frame.setHeight("1px");
        frame.setWidth("1px");
        this.addMember(frame);
    }

    private void configureToolStrip() {
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        pathItem = new SelectItem("path");
        pathItem.setShowTitle(false);
        pathItem.setWidth(400);
        pathItem.setValue(DataManagerConstants.ROOT);
        toolStrip.addFormItem(pathItem);

        ToolStripButton folderUpButton = new ToolStripButton();
        folderUpButton.setIcon("icon-folderup.png");
        folderUpButton.setPrompt("Folder up");
        folderUpButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (!pathItem.getValueAsString().equals(DataManagerConstants.ROOT)) {
                    String newPath = pathItem.getValueAsString();
                    loadData(newPath.substring(0, newPath.lastIndexOf("/")), false);
                }
            }
        });
        toolStrip.addButton(folderUpButton);

        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon("icon-refresh.png");
        refreshButton.setPrompt("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                loadData(pathItem.getValueAsString(), true);
            }
        });
        toolStrip.addButton(refreshButton);

        ToolStripButton homeButton = new ToolStripButton();
        homeButton.setIcon("icon-home.png");
        homeButton.setPrompt("Home");
        homeButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                loadData(DataManagerConstants.ROOT, false);
            }
        });
        toolStrip.addButton(homeButton);

        ToolStripButton addFolderButton = new ToolStripButton();
        addFolderButton.setIcon("icon-addfolder.png");
        addFolderButton.setPrompt("Create Folder");
        addFolderButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String path = pathItem.getValueAsString();
                if (path.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot create a folder in the root folder.");
                } else {
                    new AddFolderWindow(path).show();
                }
            }
        });
        toolStrip.addButton(addFolderButton);

        toolStrip.addSeparator();
        ToolStripButton uploadButton = new ToolStripButton();
        uploadButton.setIcon("icon-upload.png");
        uploadButton.setPrompt("Upload File");
        uploadButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String path = pathItem.getValueAsString();
                if (path.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot upload a file in the root folder.");
                } else {
                    new FileUploadWindow(path).show();
                }
            }
        });
        toolStrip.addButton(uploadButton);

        ToolStripButton downloadButton = new ToolStripButton();
        downloadButton.setIcon("icon-download.png");
        downloadButton.setPrompt("Download Selected Files");
        downloadButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                //TODO download files
            }
        });
        toolStrip.addButton(downloadButton);
        
        toolStrip.addSeparator();
        ToolStripButton deleteButton = new ToolStripButton();
        deleteButton.setIcon("icon-delete-files.png");
        deleteButton.setPrompt("Delete Selected Files/Folders");
        deleteButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                //TODO download files
            }
        });
        toolStrip.addButton(deleteButton);
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setCanDragRecordsOut(true);
        grid.setDragDataAction(DragDataAction.COPY);

        ListGridField icoField = new ListGridField("icon", " ", 30);
        icoField.setAlign(Alignment.CENTER);
        icoField.setType(ListGridFieldType.IMAGE);
        icoField.setImageURLSuffix(".png");
        icoField.setImageWidth(12);
        icoField.setImageHeight(12);
        ListGridField nameField = new ListGridField("name", "Name");

        grid.setFields(icoField, nameField);
        grid.setSortField("icon");
        grid.setSortDirection(SortDirection.DESCENDING);

        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                String type = event.getRecord().getAttributeAsString("icon");
                if (type.contains("folder")) {
                    String name = event.getRecord().getAttributeAsString("name");
                    String path = pathItem.getValueAsString() + "/" + name;
                    loadData(path, false);
                }
            }
        });
    }

    public void loadData(final String path, boolean refresh) {

        if (!path.equals(DataManagerConstants.ROOT)) {
            FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
            AsyncCallback<List<Data>> callback = new AsyncCallback<List<Data>>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Error executing get files list: " + caught.getMessage());
                }

                public void onSuccess(List<Data> result) {
                    if (result != null) {
                        List<DataRecord> dataList = new ArrayList<DataRecord>();
                        for (Data d : result) {
                            dataList.add(new DataRecord(
                                    d.getType().toLowerCase(), d.getName()));
                        }
                        pathItem.setValue(path);
                        grid.setData(dataList.toArray(new DataRecord[]{}));

                    } else {
                        SC.warn("Unable to get list of files.");
                    }
                }
            };
            Context context = Context.getInstance();
            service.listDir(context.getUser(), context.getProxyFileName(), path, refresh, callback);

        } else {
            pathItem.setValue(path);
            grid.setData(
                    new DataRecord[]{
                        new DataRecord("folder", DataManagerConstants.USERS_HOME),
                        new DataRecord("folder", DataManagerConstants.PUBLIC_HOME),
                        new DataRecord("folder", DataManagerConstants.GROUPS_HOME),
                        new DataRecord("folder", DataManagerConstants.ACTIVITIES_HOME),
                        new DataRecord("folder", DataManagerConstants.WORKFLOWS_HOME),
                        new DataRecord("folder", DataManagerConstants.CREATIS_HOME)
                    });
        }
    }

    public void uploadComplete(String fileName) {
        SC.say("Upload completed: " + fileName);
    }
    
    private native void initComplete(BrowserLayout upload) /*-{
    $wnd.uploadComplete = function (fileName) {
    upload.@fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout::uploadComplete(Ljava/lang/String;)(fileName);
    };
    }-*/;
}
