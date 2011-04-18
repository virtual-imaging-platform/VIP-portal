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
package fr.insalyon.creatis.vip.datamanagement.client.view.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanagement.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.FileCatalogServiceAsync;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanagement.client.view.panel.DataManagerBrowserPanel;
import fr.insalyon.creatis.vip.datamanagement.client.view.panel.EastPanel;
import fr.insalyon.creatis.vip.datamanagement.client.view.window.CreateFolderWindow;
import fr.insalyon.creatis.vip.datamanagement.client.view.window.FileUploadWindow;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class BrowserActionsMenu extends Menu {

    public BrowserActionsMenu() {
        this.setId("dm-browser-actions-menu");

        // Upload a File
        Item uploadItem = new Item("Upload a File", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DataManagerBrowserPanel.getInstance().getPathCBValue().equals(DataManagerConstants.ROOT)) {
                    new FileUploadWindow(DataManagerBrowserPanel.getInstance().getPathCBValue());
                } else {
                    MessageBox.alert("You can not upload a file to the root folder.");
                }
            }
        });

        // Download Selected Files
        Item downloadSelectedItem = new Item("Download Selected Files", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                Record[] records = DataManagerBrowserPanel.getInstance().getCbSelectionModel().getSelections();
                final String parentDir = DataManagerBrowserPanel.getInstance().getPathCBValue();
                for (Record r : records) {
                    if (!r.getAsString("typeico").equals("Folder")) {
                        TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                MessageBox.alert("Error", "Unable to download file: " + caught.getMessage());
                            }

                            public void onSuccess(Void result) {
                                EastPanel.getInstance().loadData();
                                EastPanel.getInstance().displayDownloadPanel();
                            }
                        };
                        Authentication auth = Context.getInstance().getAuthentication();
                        service.downloadFile(auth.getUser(),
                                parentDir + "/" + r.getAsString("fileName"),
                                Context.getInstance().getAuthentication().getUserDN(),
                                Context.getInstance().getAuthentication().getProxyFileName(),
                                callback);
                    }
                }

            }
        });

        // Delete Selected Files/Folders
        Item deleteSelectedItem = new Item("Delete Selected Files/Folders", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DataManagerBrowserPanel.getInstance().getPathCBValue().equals(DataManagerConstants.ROOT)) {
                    Record[] records = DataManagerBrowserPanel.getInstance().getCbSelectionModel().getSelections();
                    final String parentDir = DataManagerBrowserPanel.getInstance().getPathCBValue();
                    final List<String> paths = new ArrayList<String>();
                    for (Record r : records) {
                        paths.add(parentDir + "/" + r.getAsString("fileName"));
                    }

                    MessageBox.confirm("Confirm", "Do you really want to delete the files/folders \"" + paths + "\"?",
                            new MessageBox.ConfirmCallback() {

                                public void execute(String btnID) {
                                    if (btnID.toLowerCase().equals("yes")) {
                                        FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
                                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                            public void onFailure(Throwable caught) {
                                                MessageBox.alert("Error", "Error executing delete files/folders: " + caught.getMessage());
                                                Ext.get("dm-browser-panel").unmask();
                                            }

                                            public void onSuccess(Void result) {
                                                Ext.get("dm-browser-panel").unmask();
                                                DataManagerBrowserPanel.getInstance().loadData(parentDir, false, true);
                                            }
                                        };
                                        Context context = Context.getInstance();
//                                    context.setLastGridFolderBrowsed(baseDir);
                                        Authentication auth = context.getAuthentication();
                                        service.deleteFiles(auth.getUser(), auth.getProxyFileName(), paths, callback);
                                        Ext.get("dm-browser-panel").mask("Deleting Files/Folders...");
                                    }
                                }
                            });
                } else {
                    MessageBox.alert("You can not delete a root folder.");
                }
            }
        });

        // Create Folder
        Item createItem = new Item("Create Folder", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DataManagerBrowserPanel.getInstance().getPathCBValue().equals(DataManagerConstants.ROOT)) {
                    new CreateFolderWindow(DataManagerBrowserPanel.getInstance().getPathCBValue());
                } else {
                    MessageBox.alert("You can not create a folder in the root folder.");
                }
            }
        });

        this.addItem(uploadItem);
        this.addSeparator();
        this.addItem(downloadSelectedItem);
        this.addItem(deleteSelectedItem);
        this.addSeparator();
        this.addItem(createItem);
    }
}
