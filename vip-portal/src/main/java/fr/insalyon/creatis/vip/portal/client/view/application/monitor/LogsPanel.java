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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import fr.insalyon.creatis.vip.portal.client.view.common.window.fileviewer.FileViewerWindow;
import fr.insalyon.creatis.vip.portal.client.view.common.window.fileviewer.ImageViewerWindow;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.SyntaxHighlightPanel;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.common.RowNumberingColumnConfigAdapter;
import fr.insalyon.creatis.vip.portal.client.view.common.window.fileviewer.SyntaxHighlightWindow;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class LogsPanel extends Panel {

    private TreePanel logsTreePanel;
    private Store store;
    private Node parent;
    private Menu menu;
    private String clickedFileName;
    private String clickedDir;
    private String workflowID;

    public LogsPanel(String workflowID) {

        this.workflowID = workflowID;
        this.setTitle("Logs");
        this.setLayout(new BorderLayout());
        this.setMargins(0, 0, 0, 0);

        // Left Panel
        BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
        westData.setSplit(false);
        westData.setMinSize(200);
        westData.setMargins(new Margins(0, 0, 0, 0));

        Panel leftPanel = new Panel();
        leftPanel.setWidth(200);
        leftPanel.setMargins(0, 0, 0, 0);
        leftPanel.setCollapsible(true);
        leftPanel.setTitle("Logs");
        leftPanel.setLayout(new FitLayout());

        // Tree Panel
        logsTreePanel = new TreePanel();
        logsTreePanel.setBorder(false);
        logsTreePanel.setAutoScroll(true);
        logsTreePanel.setAnimate(true);
        logsTreePanel.setContainerScroll(true);
        logsTreePanel.setRootVisible(true);
        logsTreePanel.setWidth(195);
        logsTreePanel.setMargins(0, 0, 0, 0);

        TreeNode root = new TreeNode(workflowID);
        root.setExpandable(true);
        root.setId("/" + workflowID);
        root.addListener(new TreeNodeListenerAdapter() {

            @Override
            public boolean doBeforeExpand(Node node, boolean deep, boolean anim) {
                for (Node c : node.getChildNodes()) {
                    node.removeChild(c);
                }
                loadDir(node.getId(), node);
                return super.doBeforeExpand(node, deep, anim);
            }

            @Override
            public void onClick(Node node, EventObject e) {
                loadFiles(node.getId(), parent);
            }
        });
        logsTreePanel.setRootNode(root);
//        loadDir(root.getId(), root);

        leftPanel.add(logsTreePanel);
        this.add(leftPanel, westData);

        // Center Panel
        Panel centerPanel = new Panel();
        centerPanel.setLayout(new FitLayout());
        centerPanel.setMargins(0, 0, 0, 0);

        // Grid Panel
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("file-name"),
                    new StringFieldDef("file-size"),
                    new StringFieldDef("file-date"),
                    new StringFieldDef("file-dir")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        PagingMemoryProxy proxy = new PagingMemoryProxy(new Object[][]{new Object[]{}});

        store = new Store(proxy, reader);
        store.setSortInfo(new SortState("file-name", SortDir.ASC));
        final CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();

        ColumnConfig dirColumn = new ColumnConfig("Directory", "file-dir", 50, true);
        dirColumn.setHidden(true);

        BaseColumnConfig[] columns = new BaseColumnConfig[]{
            new RowNumberingColumnConfigAdapter(30),
            new ColumnConfig("File Name", "file-name", 270, true),
            new ColumnConfig("Size", "file-size", 70, true),
            new ColumnConfig("Last Modified", "file-date", 180, true),
            dirColumn
        };

        ColumnModel columnModel = new ColumnModel(columns);

        GridPanel grid = new GridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setSelectionModel(cbSelectionModel);
        grid.setFrame(true);
        grid.setStripeRows(true);
        grid.setMargins(0, 0, 0, 0);
        grid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                clickedFileName = record.getAsString("file-name");
                clickedDir = record.getAsString("file-dir");
                showWorkflowMenu(e);
            }

            @Override
            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                DOM.eventPreventDefault(e.getBrowserEvent());
                onRowClick(grid, rowIndex, e);
            }
        });

        store.load();

        centerPanel.add(grid);
        this.add(centerPanel, new BorderLayoutData(RegionPosition.CENTER));

        root.expand();
        loadFiles(root.getId(), root);
    }

    private void loadDir(String baseDir, Node parent) {

        this.parent = parent;

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get logs dir: " + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                appendNodes(result);
            }
        };
        service.getLogDir(baseDir, callback);
    }

    private void appendNodes(List<String> result) {

        for (String s : result) {
            TreeNode child = new TreeNode(s);
            child.setExpandable(true);
            child.setId(parent.getId() + "/" + s);
            child.addListener(new TreeNodeListenerAdapter() {

                @Override
                public boolean doBeforeExpand(Node node, boolean deep, boolean anim) {
                    for (Node c : node.getChildNodes()) {
                        node.removeChild(c);
                    }
                    loadDir(node.getId(), node);
                    return super.doBeforeExpand(node, deep, anim);
                }

                @Override
                public void onClick(Node node, EventObject e) {
                    loadFiles(node.getId(), parent);
                }
            });
            parent.appendChild(child);
        }
    }

    private void loadFiles(String baseDir, Node parent) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get logs dir: " + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                Object[][] data = new Object[result.size()][4];
                for (int i = 0; i < result.size(); i++) {
                    String[] r = result.get(i).split("##");
                    data[i][0] = r[0];
                    data[i][1] = r[1];
                    data[i][2] = r[2];
                    data[i][3] = r[3];
                }
                PagingMemoryProxy proxy = new PagingMemoryProxy(data);
                store.setDataProxy(proxy);
                store.load();
                store.commitChanges();
            }
        };
        service.getLogFiles(baseDir, callback);
    }

    private void showWorkflowMenu(EventObject e) {

        if (menu == null) {
            menu = new Menu();
            menu.setId(workflowID + "-logs-menu");

            Item viewFileItem = new Item("View File", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    loadFileWindow();
                }
            });
            viewFileItem.setId(workflowID + "-jobs-app-out");
            menu.addItem(viewFileItem);

            Item downloadFileItem = new Item("Download File", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    downloadFile();
                }
            });
            downloadFileItem.setId(workflowID + "-jobs-app-err");
            menu.addItem(downloadFileItem);
        }
        menu.showAt(e.getXY());
    }

    private void loadFileWindow() {

        if (clickedFileName.toLowerCase().endsWith(".png")
                || clickedFileName.toLowerCase().endsWith(".jpg")
                || clickedFileName.toLowerCase().endsWith(".gif")
                || clickedFileName.toLowerCase().endsWith(".jpeg")) {
            new ImageViewerWindow(clickedDir + "/" + clickedFileName, "jobs-menu");

        } else {
            if (clickedFileName.toLowerCase().endsWith(".xml")) {
                new SyntaxHighlightWindow(clickedDir, clickedFileName, SyntaxHighlightPanel.SYNTAX_XML, "jobs-menu");
            } else {
                new FileViewerWindow(clickedDir, clickedFileName, "jobs-menu");
            }
        }
    }

    private void downloadFile() {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get file: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                Window.open(result, "", null);
            }
        };
        service.getFileURL(clickedDir, clickedFileName, callback);
    }
}
