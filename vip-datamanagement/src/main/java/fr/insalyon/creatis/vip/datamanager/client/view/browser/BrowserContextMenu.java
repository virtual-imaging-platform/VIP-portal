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
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerContext;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.ValidatorUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Rafael Silva
 */
public class BrowserContextMenu extends Menu {

    public BrowserContextMenu(final ModalWindow modal, final String baseDir,
            final DataRecord data) {

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem uploadItem = new MenuItem("Upload");
        uploadItem.setIcon(DataManagerConstants.ICON_UPLOAD);
        uploadItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (ValidatorUtil.validateRootPath(baseDir, "upload a file in")
                        && ValidatorUtil.validateUserLevel(baseDir, "upload a file to")) {

                    DataUploadWindow window = new DataUploadWindow(modal, baseDir);
                    BrowserLayout.getInstance().setDataUploadWindow(window);
                    window.show();
                }
            }
        });

        MenuItem downloadItem = new MenuItem("Download");
        downloadItem.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                download(modal, baseDir, data);
            }
        });

        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setIcon(DataManagerConstants.ICON_CUT);
        cutItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (ValidatorUtil.validateRootPath(baseDir, "cut from")
                        && ValidatorUtil.validateUserLevel(baseDir, "cut from")) {

                    DataManagerContext.getInstance().setCutAction(baseDir, data.getName());
                    BrowserLayout.getInstance().getToolStrip().enablePasteButton();
                }
            }
        });

        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setIcon(DataManagerConstants.ICON_PASTE);
        pasteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (ValidatorUtil.validateRootPath(baseDir, "paste in")
                        && ValidatorUtil.validateUserLevel(baseDir, "paste to")) {

                    paste(modal, baseDir);
                }
            }
        });

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setIcon(CoreConstants.ICON_EDIT);
        renameItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (ValidatorUtil.validateRootPath(baseDir, "rename from")
                        && ValidatorUtil.validateUserLevel(baseDir, "rename from")) {

                    new RenameWindow(modal, baseDir, data.getName()).show();
                }
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setIcon(CoreConstants.ICON_DELETE);
        deleteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (ValidatorUtil.validateRootPath(baseDir, "delete from")
                        && ValidatorUtil.validateUserLevel(baseDir, "delete from")) {
                    
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

    /**
     *
     * @param modal
     * @param baseDir
     * @param name
     */
    private void delete(final ModalWindow modal, final String baseDir, final String name) {

        final DataManagerServiceAsync service = DataManagerService.Util.getInstance();

        if (baseDir.startsWith(DataManagerConstants.ROOT + "/" + DataManagerConstants.TRASH_HOME)) {
            SC.confirm("Do you really want to permanently delete \"" + name + "\"?", new BooleanCallback() {

                public void execute(Boolean value) {
                    if (value != null && value) {
                        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                modal.hide();
                                SC.warn("Unable to delete file/folder:<br />" + caught.getMessage());
                            }

                            public void onSuccess(Void result) {
                                modal.hide();
                                SC.say("The file/folder was successfully scheduled to be permanentely deleted.");
                                BrowserLayout.getInstance().loadData(baseDir, true);
                            }
                        };
                        modal.show("Deleting " + name + "...", true);
                        service.delete(baseDir + "/" + name, callback);
                    }
                }
            });

        } else {
            SC.confirm("Do you really want to delete \"" + name + "\"?", new BooleanCallback() {

                public void execute(Boolean value) {
                    if (value != null && value) {
                        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                modal.hide();
                                SC.warn("Unable to delete file/folder:<br />" + caught.getMessage());
                            }

                            public void onSuccess(Void result) {
                                modal.hide();
                                BrowserLayout.getInstance().loadData(baseDir, true);
                            }
                        };
                        modal.show("Moving " + name + " to Trash...", true);
                        service.rename(baseDir + "/" + name,
                                DataManagerConstants.ROOT + "/"
                                + DataManagerConstants.TRASH_HOME + "/" + name,
                                true, callback);
                    }
                }
            });
        }
    }

    /**
     *
     * @param modal
     * @param baseDir
     * @param data
     */
    private void download(final ModalWindow modal, final String baseDir,
            final DataRecord data) {

        if (data.getType().contains("file")) {
            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    if (caught.getMessage().contains("No such file or directory")
                            || caught.getMessage().contains("Error while performing:LINKSTAT")) {
                        SC.warn("The file " + baseDir + "/" + data.getName() + " is unavailable.");
                        BrowserLayout.getInstance().loadData(baseDir, true);
                    } else {
                        SC.warn("Unable to download file:<br />" + caught.getMessage());
                    }
                }

                public void onSuccess(Void result) {
                    modal.hide();
                    OperationLayout.getInstance().loadData();
                }
            };
            modal.show("Adding file to transfer queue...", true);
            service.downloadFile(baseDir + "/" + data.getName(), callback);

        } else {
            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    if (caught.getMessage().contains("No such file or directory")
                            || caught.getMessage().contains("Error while performing:LINKSTAT")) {
                        SC.warn("The folder " + baseDir + "/" + data.getName() + " is unavailable.");
                        BrowserLayout.getInstance().loadData(baseDir, true);
                    } else {
                        SC.warn("Unable to download folder:<br />" + caught.getMessage());
                    }
                }

                public void onSuccess(Void result) {
                    modal.hide();
                    OperationLayout.getInstance().loadData();
                }
            };
            modal.show("Adding folder to transfer queue...", true);
            service.downloadFolder(baseDir + "/" + data.getName(), callback);
        }
    }

    /**
     *
     * @param modal
     * @param baseDir
     */
    private void paste(final ModalWindow modal, final String baseDir) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to paste file/folder:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                DataManagerContext.getInstance().resetCutAction();
                BrowserLayout.getInstance().getToolStrip().resetPasteButton();
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
}
