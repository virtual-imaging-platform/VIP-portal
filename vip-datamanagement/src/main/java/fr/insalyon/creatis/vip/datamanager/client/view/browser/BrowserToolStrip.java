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
package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerContext;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.common.BasicBrowserToolStrip;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class BrowserToolStrip extends BasicBrowserToolStrip {

    private ToolStripButton pasteButton;

    public BrowserToolStrip(final ModalWindow modal, final ListGrid grid) {

        super(modal, grid);

        this.addSeparator();
        ToolStripButton cutButton = new ToolStripButton();
        cutButton.setIcon(DataManagerConstants.ICON_CUT);
        cutButton.setPrompt("Cut");
        cutButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String path = pathItem.getValueAsString();
                if (path.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot cut a folder from the root folder.");
                } else {
                    cut();
                }
            }
        });
        this.addButton(cutButton);

        pasteButton = new ToolStripButton();
        pasteButton.setIcon(DataManagerConstants.ICON_PASTE);
        pasteButton.setPrompt("Paste");
        pasteButton.setDisabled(true);
        pasteButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String path = pathItem.getValueAsString();
                if (path.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot paste in the root folder.");
                } else {
                    paste(modal, path);
                }
            }
        });
        this.addButton(pasteButton);

        this.addSeparator();
        ToolStripButton uploadButton = new ToolStripButton();
        uploadButton.setIcon(DataManagerConstants.ICON_UPLOAD);
        uploadButton.setPrompt("Upload a File");
        uploadButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String path = pathItem.getValueAsString();
                if (path.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot upload a file in the root folder.");

                } else {
                    new FileUploadWindow(modal, path, "dataManagerUploadComplete").show();
                }
            }
        });
        this.addButton(uploadButton);
        
        ToolStripButton uploadMultipleButton = new ToolStripButton();
        uploadMultipleButton.setIcon(DataManagerConstants.ICON_UPLOAD_MULTIPLE);
        uploadMultipleButton.setPrompt("Upload Multiple Data");
        uploadMultipleButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String path = pathItem.getValueAsString();
                if (path.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot upload data in the root folder.");

                } else {
                    DataUploadWindow window = new DataUploadWindow(modal, path);
                    BrowserLayout.getInstance().setDataUploadWindow(window);
                    window.show();
                }
            }
        });
        this.addButton(uploadMultipleButton);

        ToolStripButton downloadButton = new ToolStripButton();
        downloadButton.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadButton.setPrompt("Download Selected Data");
        downloadButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                download();
            }
        });
        this.addButton(downloadButton);

        ToolStripButton deleteButton = new ToolStripButton();
        deleteButton.setIcon(CoreConstants.ICON_DELETE);
        deleteButton.setPrompt("Delete Selected Files/Folders");
        deleteButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String path = pathItem.getValueAsString();
                if (path.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot delete a root folder.");
                } else {
                    delete();
                }
            }
        });
        this.addButton(deleteButton);

        this.addSeparator();
        ToolStripButton trashButton = new ToolStripButton();
        trashButton.setIcon(DataManagerConstants.ICON_TRASH);
        trashButton.setPrompt("Trash");
        trashButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                BrowserLayout.getInstance().loadData(DataManagerConstants.ROOT
                        + "/" + DataManagerConstants.TRASH_HOME, false);
            }
        });
        this.addButton(trashButton);

        ToolStripButton emptyTrashButton = new ToolStripButton();
        emptyTrashButton.setIcon(DataManagerConstants.ICON_EMPTY_TRASH);
        emptyTrashButton.setPrompt("Empty Trash");
        emptyTrashButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                emptyTrash();
            }
        });
        this.addButton(emptyTrashButton);
    }

    private void download() {
        ListGridRecord[] records = BrowserLayout.getInstance().getGridSelection();

        for (ListGridRecord record : records) {
            DataRecord data = (DataRecord) record;

            if (data.getType().contains("file")) {
                DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                    public void onFailure(Throwable caught) {
                        modal.hide();
                        SC.warn("Unable to download file:<br />" + caught.getMessage());
                    }

                    public void onSuccess(Void result) {
                        modal.hide();
                        OperationLayout.getInstance().loadData();
                        OperationLayout.getInstance().activateAutoRefresh();
                    }
                };
                modal.show("Adding file to transfer queue...", true);
                service.downloadFile(
                        pathItem.getValueAsString() + "/" + data.getName(),
                        callback);

            } else {
                DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                    public void onFailure(Throwable caught) {
                        modal.hide();
                        SC.warn("Unable to download folder: " + caught.getMessage());
                    }

                    public void onSuccess(Void result) {
                        modal.hide();
                        OperationLayout.getInstance().loadData();
                        OperationLayout.getInstance().activateAutoRefresh();
                    }
                };
                modal.show("Adding folder to transfer queue...", true);
                service.downloadFolder(
                        pathItem.getValueAsString() + "/" + data.getName(),
                        callback);
            }
        }
    }

    private void cut() {

        ListGridRecord[] records = BrowserLayout.getInstance().getGridSelection();
        if (records.length > 0) {
            final List<String> paths = new ArrayList<String>();

            for (ListGridRecord record : records) {
                paths.add(((DataRecord) record).getName());
            }

            DataManagerContext.getInstance().setCutAction(pathItem.getValueAsString(),
                    paths.toArray(new String[]{}));

            pasteButton.setDisabled(false);

        } else {
            SC.warn("There are no data selected to cut.");
        }
    }

    private void paste(final ModalWindow modal, final String baseDir) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to paste file/folder:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                pasteButton.setDisabled(true);
                DataManagerContext.getInstance().resetCutAction();
                BrowserLayout.getInstance().loadData(baseDir, true);
            }
        };

        if (!baseDir.equals(DataManagerContext.getInstance().getCutFolder())) {

            modal.show("Moving data...", true);
            service.rename(DataManagerContext.getInstance().getCutFolder(),
                    new ArrayList(Arrays.asList(DataManagerContext.getInstance().getCutName())),
                    baseDir, false, callback);

        } else {
            SC.warn("Unable to move data into the same folder.");
        }
    }

    private void delete() {

        ListGridRecord[] records = BrowserLayout.getInstance().getGridSelection();
        final List<String> paths = new ArrayList<String>();

        for (ListGridRecord record : records) {
            DataRecord data = (DataRecord) record;
            paths.add(data.getName());
        }
        SC.confirm("Do you really want to delete the selected files/folders?", new BooleanCallback() {

            public void execute(Boolean value) {
                if (value != null && value) {
                    DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                    AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to delete files/folders:<br />" + caught.getMessage());
                        }

                        public void onSuccess(Void result) {
                            modal.hide();
                            BrowserLayout.getInstance().loadData(pathItem.getValueAsString(), true);
                        }
                    };
                    modal.show("Deleting files/folders...", true);
                    service.rename(pathItem.getValueAsString(), paths,
                            DataManagerConstants.ROOT + "/" 
                            + DataManagerConstants.TRASH_HOME, true, callback);
                }
            }
        });
    }

    private void emptyTrash() {
        SC.confirm("Do you really want to remove the items in the trash permanently?", new BooleanCallback() {

            public void execute(Boolean value) {
                if (value != null && value) {

                    final DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                    AsyncCallback<List<Data>> callback = new AsyncCallback<List<Data>>() {

                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to delete files/folders:<br />" + caught.getMessage());
                        }

                        public void onSuccess(List<Data> result) {

                            List<String> paths = new ArrayList<String>();
                            for (Data data : result) {
                                paths.add(DataManagerConstants.ROOT + "/"
                                        + DataManagerConstants.TRASH_HOME
                                        + "/" + data.getName());
                            }

                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                public void onFailure(Throwable caught) {
                                    modal.hide();
                                    SC.warn("Error executing empty trash: " + caught.getMessage());
                                }

                                public void onSuccess(Void result) {
                                    modal.hide();
                                    BrowserLayout.getInstance().loadData(
                                            DataManagerConstants.ROOT + "/"
                                            + DataManagerConstants.TRASH_HOME, true);
                                }
                            };
                            service.delete(paths, callback);
                        }
                    };
                    modal.show("Emptying Trash...", true);
                    service.listDir(DataManagerConstants.ROOT + "/"
                            + DataManagerConstants.TRASH_HOME, true, callback);
                }
            }
        });
    }

    public void resetPasteButton() {
        pasteButton.setDisabled(true);
    }

    public void enablePasteButton() {
        pasteButton.setDisabled(false);
    }
}
