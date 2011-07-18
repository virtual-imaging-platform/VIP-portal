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
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerContext;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

/**
 *
 * @author Rafael Silva
 */
public class BrowserContextMenu extends Menu {

    public BrowserContextMenu(final ModalWindow modal, final String baseDir, final DataRecord data) {

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem uploadItem = new MenuItem("Upload");
        uploadItem.setIcon("icon-upload.png");
        uploadItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot upload a file in the root folder.");
                } else {
                    DataUploadWindow window = new DataUploadWindow(modal, baseDir);
                    BrowserLayout.getInstance().setDataUploadWindow(window);
                    window.show();
                }
            }
        });

        MenuItem downloadItem = new MenuItem("Download");
        downloadItem.setIcon("icon-download.png");
        downloadItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                download(modal, baseDir, data);
            }
        });

        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setIcon("icon-cut.png");
        cutItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot cut a folder from the root folder.");
                } else {
                    DataManagerContext.getInstance().setCutAction(baseDir, data.getName());
                }
            }
        });

        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setIcon("icon-paste.png");
        pasteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot paste in the root folder.");
                } else {
                    paste(modal, baseDir);
                }
            }
        });

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setIcon("icon-edit.png");
        renameItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot rename a folder from the root folder.");
                } else {
                    new RenameWindow(modal, baseDir, data.getName()).show();
                }
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setIcon("icon-delete.png");
        deleteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot delete a folder from the root folder.");
                } else {
                    delete(modal, baseDir, data.getName());
                }
            }
        });

        MenuItem propertiesItem = new MenuItem("Properties");
        propertiesItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("There are no properties for root folders.");
                } else {
                    new DataPropertiesWindow(baseDir, data).show();
                }
            }
        });

        MenuItemSeparator separator = new MenuItemSeparator();

        if (DataManagerContext.getInstance().hasCutAction()) {
            this.setItems(uploadItem, downloadItem, separator, cutItem, pasteItem,
                    separator, renameItem, deleteItem, separator, propertiesItem);
        } else {
            this.setItems(uploadItem, downloadItem, separator, cutItem, separator,
                    renameItem, deleteItem, separator, propertiesItem);
        }
    }

    private void delete(final ModalWindow modal, final String baseDir, final String name) {

        if (baseDir.equals(DataManagerConstants.ROOT + "/" + DataManagerConstants.TRASH_HOME)) {
            SC.confirm("Do you really want to permanently delete \"" + name + "\"?", new BooleanCallback() {

                public void execute(Boolean value) {
                    if (value != null && value) {
                        FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                modal.hide();
                                SC.warn("Error executing delete files/folders: " + caught.getMessage());
                            }

                            public void onSuccess(Void result) {
                                modal.hide();
                                BrowserLayout.getInstance().loadData(baseDir, true);
                            }
                        };
                        modal.show("Deleting " + name + "...", true);
                        Context context = Context.getInstance();
                        service.delete(context.getUser(), context.getProxyFileName(),
                                baseDir + "/" + name, callback);
                    }
                }
            });

        } else {
            SC.confirm("Do you really want to delete \"" + name + "\"?", new BooleanCallback() {

                public void execute(Boolean value) {
                    if (value != null && value) {
                        FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                modal.hide();
                                SC.warn("Error executing delete files/folders: " + caught.getMessage());
                            }

                            public void onSuccess(Void result) {
                                modal.hide();
                                BrowserLayout.getInstance().loadData(baseDir, true);
                            }
                        };
                        modal.show("Deleting " + name + "...", true);
                        Context context = Context.getInstance();
                        String oldPath = baseDir + "/" + name;
                        String newPath = DataManagerConstants.ROOT + "/"
                                + DataManagerConstants.TRASH_HOME + "/" + name;
                        service.rename(context.getUser(), context.getProxyFileName(),
                                oldPath, newPath, callback);
                    }
                }
            });
        }
    }

    private void download(final ModalWindow modal, final String baseDir, DataRecord data) {
        if (data.getType().contains("file")) {
            TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to download file: " + caught.getMessage());
                }

                public void onSuccess(Void result) {
                    modal.hide();
                    OperationLayout.getInstance().loadData();
                    OperationLayout.getInstance().activateAutoRefresh();
                }
            };
            modal.show("Adding file to transfer queue...", true);
            Context context = Context.getInstance();
            service.downloadFile(
                    context.getUser(),
                    baseDir + "/" + data.getName(),
                    context.getUserDN(), context.getProxyFileName(),
                    callback);

        } else {
            TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
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
            Context context = Context.getInstance();
            service.downloadFolder(
                    context.getUser(),
                    baseDir + "/" + data.getName(),
                    context.getUserDN(), context.getProxyFileName(),
                    callback);
        }
    }

    private void paste(final ModalWindow modal, final String baseDir) {
        FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing paste command: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                DataManagerContext.getInstance().resetCutAction();
                modal.hide();
                BrowserLayout.getInstance().loadData(baseDir, true);
            }
        };

        if (!baseDir.equals(DataManagerContext.getInstance().getCutFolder())) {

            String oldPath = DataManagerContext.getInstance().getCutFolder() + "/"
                    + DataManagerContext.getInstance().getCutName();
            String newPath = baseDir + "/" + DataManagerContext.getInstance().getCutName();

            modal.show("Moving " + oldPath + " to " + newPath + "...", true);
            Context context = Context.getInstance();
            service.rename(context.getUser(), context.getProxyFileName(),
                    oldPath, newPath, callback);
        } else {
            SC.warn("Unable to move data into the same folder.");
        }
    }
}
