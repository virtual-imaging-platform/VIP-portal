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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
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
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanagement.client.view.panel.DownloadPanel;
import fr.insalyon.creatis.vip.datamanagement.client.view.panel.EastPanel;
import fr.insalyon.creatis.vip.datamanagement.client.view.window.OperationDetailWindow;

/**
 *
 * @author Rafael Silva
 */
public class DownloadMenu extends Menu {

    public DownloadMenu() {

        this.setId("dm-download-menu");

        Item downloadItem = new Item("Download", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (DownloadPanel.getInstance().getStatus().equals("Done")) {
                    Window.open(
                            GWT.getModuleBaseURL()
                            + "/filedownloadservice?operationid="
                            + DownloadPanel.getInstance().getOperationId()
                            + "&proxy="
                            + Context.getInstance().getAuthentication().getProxyFileName(),
                            "", "");
                } else {
                    MessageBox.alert("Unable to download", "ERROR: Current status is "
                            + DownloadPanel.getInstance().getStatus());
                }
            }
        });
        this.addItem(downloadItem);
        this.addSeparator();

        Item detailsItem = new Item("View Details", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                OperationDetailWindow detailWindow = OperationDetailWindow.getInstance();
                detailWindow.display(DownloadPanel.getInstance().getOperationId());
            }
        });
        detailsItem.setId("dm-details-download-menu");
        this.addItem(detailsItem);

        Item cleanItem = new Item("Clean/Cancel Operation", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                if (!DownloadPanel.getInstance().getStatus().equals("Running")) {
                MessageBox.confirm("Confirm", "Do you really want to clean/cancel this operation \"" + DownloadPanel.getInstance().getOperationId() + "\"?",
                        new MessageBox.ConfirmCallback() {

                            public void execute(String btnID) {
                                if (btnID.toLowerCase().equals("yes")) {
                                    TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
                                    AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                        public void onFailure(Throwable caught) {
                                            MessageBox.alert("Error", "Error executing clean operation: " + caught.getMessage());
                                            Ext.get("dm-download-panel").unmask();
                                        }

                                        public void onSuccess(Void result) {
                                            Ext.get("dm-download-panel").unmask();
                                            EastPanel.getInstance().loadData();
                                        }
                                    };
                                    Authentication auth = Context.getInstance().getAuthentication();
                                    service.removeOperationById(DownloadPanel.getInstance().getOperationId(),
                                            auth.getProxyFileName(), callback);
                                    Ext.get("dm-download-panel").mask("Cleaning Operation...");
                                }
                            }
                        });
                } else {
                    MessageBox.alert("Unable to clean/cancel", "ERROR: Current status is "
                            + DownloadPanel.getInstance().getStatus());
                }
            }
        });
        cleanItem.setId("dm-clean-download-menu");
        this.addItem(cleanItem);
    }
}
