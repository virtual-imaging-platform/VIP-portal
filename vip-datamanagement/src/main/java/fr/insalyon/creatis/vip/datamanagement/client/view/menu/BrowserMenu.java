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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanagement.client.view.panel.DataManagerBrowserPanel;
import fr.insalyon.creatis.vip.datamanagement.client.view.panel.EastPanel;
//import fr.insalyon.creatis.vip.datamanagement.client.view.window.CreateFolderWindow;
import fr.insalyon.creatis.vip.datamanagement.client.view.window.FileUploadWindow;

/**
 *
 * @author Rafael Silva
 */
public class BrowserMenu extends Menu {

    public BrowserMenu() {
        this.setId("dm-browser-menu");

        Item uploadItem = new Item("Upload File", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DataManagerBrowserPanel.getInstance().getPathCBValue().equals(DataManagerConstants.ROOT)) {
                    new FileUploadWindow(DataManagerBrowserPanel.getInstance().getPathCBValue());
                } else {
                    MessageBox.alert("You can not upload a file to the root folder.");
                }
            }
        });
        uploadItem.setId("dm-upload-browser-menu");
        this.addItem(uploadItem);

        Item downloadItem = new Item("Download File", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DataManagerBrowserPanel.getInstance().getType().equals("Folder")) {
                    TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
                    final String parentDir = DataManagerBrowserPanel.getInstance().getPathCBValue();
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
                            parentDir + "/" + DataManagerBrowserPanel.getInstance().getName(),
                            Context.getInstance().getAuthentication().getUserDN(),
                            Context.getInstance().getAuthentication().getProxyFileName(),
                            callback);
                }
            }
        });
        downloadItem.setId("dm-download-browser-menu");
        this.addItem(downloadItem);
        this.addSeparator();

        Item deleteItem = new Item("Delete File/Folder", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DataManagerBrowserPanel.getInstance().getPathCBValue().equals(DataManagerConstants.ROOT)) {
                    MessageBox.confirm("Confirm", "Do you really want to delete the file/folder \"" + DataManagerBrowserPanel.getInstance().getName() + "\"?",
                            new MessageBox.ConfirmCallback() {

                                public void execute(String btnID) {
                                    if (btnID.toLowerCase().equals("yes")) {
                                        final String parentDir = DataManagerBrowserPanel.getInstance().getPathCBValue();
                                        FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
                                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                            public void onFailure(Throwable caught) {
                                                MessageBox.alert("Error", "Error executing delete file/folder: " + caught.getMessage());
                                                Ext.get("dm-browser-panel").unmask();
                                            }

                                            public void onSuccess(Void result) {
                                                Ext.get("dm-browser-panel").unmask();
                                                DataManagerBrowserPanel.getInstance().loadData(parentDir, false, true);
                                            }
                                        };
                                        Authentication auth = Context.getInstance().getAuthentication();
                                        service.delete(auth.getUser(), auth.getProxyFileName(), parentDir + "/" + DataManagerBrowserPanel.getInstance().getName(), callback);
                                        Ext.get("dm-browser-panel").mask("Deleting File/Folder...");
                                    }
                                }
                            });
                } else {
                    MessageBox.alert("You can not delete a root folder.");
                }
            }
        });
        deleteItem.setId("dm-delete-browser-menu");
        this.addItem(deleteItem);

        Item createFolderItem = new Item("Create Folder", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DataManagerBrowserPanel.getInstance().getPathCBValue().equals(DataManagerConstants.ROOT)) {
//                    new CreateFolderWindow(DataManagerBrowserPanel.getInstance().getPathCBValue());
                } else {
                    MessageBox.alert("You can not create a folder in the root folder.");
                }
            }
        });
        createFolderItem.setId("dm-createfolder-browser-menu");
        this.addItem(createFolderItem);
    }
}
