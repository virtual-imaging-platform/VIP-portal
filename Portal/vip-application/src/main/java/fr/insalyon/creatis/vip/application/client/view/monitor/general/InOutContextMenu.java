/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerModule;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserContextMenu;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.visualization.BrainBrowserViewTab;
import fr.insalyon.creatis.vip.datamanager.client.view.visualization.ImageViewTab;
import fr.insalyon.creatis.vip.datamanager.client.view.visualization.DicomViewTab;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class InOutContextMenu extends Menu {

    private String simulationID;
    private Tree tree;
    private InOutTreeNode node;

    public InOutContextMenu(String simulationID, Tree tree, final InOutTreeNode node) {

        this.simulationID = simulationID;
        this.tree = tree;
        this.node = node;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem downloadFilesItem = new MenuItem("Download Files");
        downloadFilesItem.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadFilesItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                download();
            }
        });

        MenuItem viewImageItem = new MenuItem("View Image");
        viewImageItem.setIcon(DataManagerConstants.ICON_VIEW);
        viewImageItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ImageViewTab(node.getName()));
            }
        });

        MenuItem viewSurfaceItem = new MenuItem("View Surface");
        viewSurfaceItem.setIcon(DataManagerConstants.ICON_VIEW);
        viewSurfaceItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new BrainBrowserViewTab(node.getName()));
            }
        });

        MenuItem viewDicomItem = new MenuItem("View DICOM");
        viewDicomItem.setIcon(DataManagerConstants.ICON_VIEW);
        viewDicomItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new DicomViewTab(node.getName()));
            }
        });
        
        MenuItem downloadFileItem = new MenuItem("Download");
        downloadFileItem.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadFileItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                downloadFile(node.getName());
            }
        });

        MenuItem jumpToItem = new MenuItem("Go to Folder");
        jumpToItem.setIcon(DataManagerConstants.ICON_JUMPTO);
        jumpToItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                String folder = node.getName().substring(0, node.getName().lastIndexOf("/"));
                BrowserLayout.getInstance().loadData(folder, false);
                DataManagerModule.dataManagerSection.expand();
            }
        });

        if (!node.getType().equals("Simulation")) {
            if (node.getName().startsWith(DataManagerConstants.ROOT + "/")) {
                 ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
                 
                 BrowserContextMenu.addVizualisers(menuItems, node.getName());
                 menuItems.add(downloadFileItem);
                 menuItems.add(jumpToItem);
            } else {
                this.setItems(downloadFilesItem);
            }
        }
    }

    private void downloadFile(String path) {

        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to download file:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                OperationLayout.getInstance().addOperation(result);
                DataManagerModule.dataManagerSection.expand();
            }
        };
        DataManagerService.Util.getInstance().downloadFile(path, callback);
    }

    private void download() {

        List<String> paths = new ArrayList<String>();
        for (TreeNode n : tree.getChildren(node)) {
            InOutTreeNode output = (InOutTreeNode) n;
            if (output.getName().startsWith(DataManagerConstants.ROOT + "/")) {
                paths.add(output.getName());
            }
        }

        if (paths.isEmpty()) {
            Layout.getInstance().setWarningMessage("There are no data stored on the grid.");
        } else {
            downloadFiles(paths, simulationID + "-" + node.getName());
        }
    }

    private void downloadFiles(List<String> paths, String packName) {

        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to download files:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                OperationLayout.getInstance().addOperation(result);
                DataManagerModule.dataManagerSection.expand();
            }
        };
        DataManagerService.Util.getInstance().downloadFiles(paths, packName, callback);
    }
}
