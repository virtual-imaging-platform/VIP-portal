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
package fr.insalyon.creatis.vip.datamanagement.client.view.panel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragSource;
import com.gwtext.client.dd.DropTarget;
import com.gwtext.client.dd.DropTargetConfig;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanagement.client.view.menu.DownloadActionsMenu;
import fr.insalyon.creatis.vip.datamanagement.client.view.menu.DownloadMenu;

/**
 *
 * @author Rafael Silva
 */
public class DownloadPanel extends AbstractOperationPanel {

    private static DownloadPanel instance;

    public static DownloadPanel getInstance() {
        if (instance == null) {
            instance = new DownloadPanel();
        }
        return instance;
    }

    private DownloadPanel() {
        super("dm-download-panel", "Downloads");

        DropTargetConfig cfg = new DropTargetConfig();
        cfg.setdDdGroup("dm-browser-dd");

        DropTarget tg = new DropTarget(grid, cfg) {

            @Override
            public boolean notifyDrop(DragSource source, EventObject e, DragData data) {
                Record[] rows = DataManagerBrowserPanel.getInstance().getSelectionModel().getSelections();

                for (Record r : rows) {
                    if (!r.getAsString("typeico").equals("Folder")) {
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
                                parentDir + "/" + r.getAsString("fileName"),
                                Context.getInstance().getAuthentication().getUserDN(),
                                Context.getInstance().getAuthentication().getProxyFileName(),
                                callback);
                    }
                }
                return super.notifyDrop(source, e, data);
            }

            @Override
            public String notifyOver(DragSource source, EventObject e, DragData data) {
                return "x-dd-drop-ok";
            }
        };
        this.configureToolbar();
    }

    private void configureToolbar() {
        // Actions Menu
        ToolbarButton actionsButton = new ToolbarButton("Actions");
        actionsButton.setMenu(new DownloadActionsMenu());
        topToolbar.addButton(actionsButton);
    }

    @Override
    protected void showMenu(EventObject e) {
        if (menu == null) {
            menu = new DownloadMenu();
        }
        menu.showAt(e.getXY());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public CheckboxSelectionModel getCbSelectionModel() {
        return cbSelectionModel;
    }
}
