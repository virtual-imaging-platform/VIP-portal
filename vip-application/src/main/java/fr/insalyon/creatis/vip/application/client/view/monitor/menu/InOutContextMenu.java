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
package fr.insalyon.creatis.vip.application.client.view.monitor.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.application.client.view.monitor.InOutTreeNode;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerModule;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class InOutContextMenu extends Menu {

    private String simulationID;
    private Tree tree;
    private InOutTreeNode node;
    private ModalWindow modal;

    public InOutContextMenu(String simulationID, Tree tree, final InOutTreeNode node, ModalWindow modal) {

        this.simulationID = simulationID;
        this.tree = tree;
        this.node = node;
        this.modal = modal;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem downloadFilesItem = new MenuItem("Download Files");
        downloadFilesItem.setIcon("icon-download.png");
        downloadFilesItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                download();
            }
        });
        
        MenuItem downloadFileItem = new MenuItem("Download File");
        downloadFileItem.setIcon("icon-download.png");
        downloadFileItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                downloadFile(node.getName());
            }
        });

        MenuItem jumpToItem = new MenuItem("Go to Folder");
        jumpToItem.setIcon("icon-jumpto.png");
        jumpToItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                String folder = node.getName().substring(0, node.getName().lastIndexOf("/"));
                BrowserLayout.getInstance().loadData(folder, false);
                DataManagerModule.dataManagerSection.expand();
            }
        });

        if (!node.getType().equals("Simulation")) {
            if (node.getType().equals("URI")) {
                this.setItems(downloadFileItem, jumpToItem);
            } else {
                this.setItems(downloadFilesItem);
            }
        }
    }

    private void downloadFile(String path) {
        
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
                DataManagerModule.dataManagerSection.expand();
            }
        };
        modal.show("Adding file to transfer queue...", true);
//        Context context = Context.getInstance();
//        service.downloadFile(
//                context.getUser(), path, 
//                context.getUserDN(), context.getProxyFileName(),
//                callback);
    }
    
    private void download() {

        List<String> paths = new ArrayList<String>();
        for (TreeNode n : tree.getChildren(node)) {
            InOutTreeNode output = (InOutTreeNode) n;
            if (output.getType().equals("URI")) {
                paths.add(output.getName());
            }
        }

        if (paths.isEmpty()) {
            SC.say("There are no data stored on the grid.");
        } else {
            downloadFiles(paths, simulationID + "-" + node.getName());
        }
    }

    private void downloadFiles(List<String> paths, String packName) {

        TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to download files: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                OperationLayout.getInstance().loadData();
                OperationLayout.getInstance().activateAutoRefresh();
                DataManagerModule.dataManagerSection.expand();
            }
        };
        modal.show("Adding files to transfer queue...", true);
//        Context context = Context.getInstance();
//        service.downloadFiles(
//                context.getUser(),
//                paths, packName,
//                context.getUserDN(), context.getProxyFileName(),
//                callback);
    }
}
