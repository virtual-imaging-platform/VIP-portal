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
                BrowserLayout.getInstance().loadData(folder, true);
                DataManagerModule.dataManagerSection.expand();
            }
        });

        if (node.getName().startsWith(DataManagerConstants.ROOT + "/")) {
            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
            BrowserContextMenu.addVizualisers(menuItems, node.getName());
            menuItems.add(downloadFileItem);
            menuItems.add(jumpToItem);
            this.setItems(menuItems.toArray(new MenuItem[menuItems.size()]));
        } else if (!node.getType().equals("Simulation")) {
            this.setItems(downloadFilesItem);
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
