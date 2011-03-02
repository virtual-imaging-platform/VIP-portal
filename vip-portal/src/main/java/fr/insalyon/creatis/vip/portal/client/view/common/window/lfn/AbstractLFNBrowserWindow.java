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
package fr.insalyon.creatis.vip.portal.client.view.common.window.lfn;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.portal.client.rpc.LFNService;
import fr.insalyon.creatis.vip.portal.client.rpc.LFNServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.portal.client.view.common.RowNumberingColumnConfigAdapter;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractLFNBrowserWindow extends Window {

    protected String HOST = "lfc-biomed.in2p3.fr";
    protected Node parent;
    protected Store store;
    protected TreeNode root;
    protected ToolbarTextItem pathItem;
    protected GridPanel grid;
    protected Toolbar topToolbar;
    private List<String> loadQueue;

    /**
     * Creates a new instance of a LFN browser window with the root
     * path configured to "/".
     *
     * @param title Title of the window
     */
    public AbstractLFNBrowserWindow(String title) {
        root = new TreeNode(HOST);
        root.setId("/");
        configure(title);
    }

    /**
     * Creates a new instance of a LFN browser window with the root
     * path configured according to path specified in the constructor.
     *
     * @param title Title of the window
     * @param rootPath Root path
     */
    public AbstractLFNBrowserWindow(String title, String rootPath) {
        root = new TreeNode(rootPath);
        root.setId(rootPath);
        configure(title);
    }

    /**
     * Displays the window and load the data according to the specified
     * root path.
     *
     * @param rootPath Root path
     */
    protected void display() {
        this.show();
        loadData();
    }

    /**
     * Configures the window appearance.
     *
     * @param title Title of the window
     */
    private void configure(String title) {
        this.setTitle(title);
        this.setWidth(600);
        this.setHeight(400);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setLayout(new FitLayout());

        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());

        BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
        westData.setSplit(true);
        westData.setMinSize(200);
        westData.setMaxSize(400);
        westData.setMargins(0, 0, 0, 0);

        panel.add(getLeftPanel(), westData);
        panel.add(getCenterPanel(), new BorderLayoutData(RegionPosition.CENTER));

        topToolbar = new Toolbar();
        this.setTopToolbar(topToolbar);

        this.add(panel);
    }

    private void loadData() {

        loadQueue = new LinkedList<String>();

        String lastPath = Context.getInstance().getLastGridFolderBrowsed();
        String rootPath = root.getId();
        root.expand();

        if (lastPath != null && !lastPath.equals(rootPath) && lastPath.contains(rootPath)) {
            String tmpPath = rootPath;

            for (String s : lastPath.split("/")) {
                if (!rootPath.contains(s)) {
                    tmpPath += "/" + s;
                    loadQueue.add(tmpPath);
                }
            }
            loadFiles(tmpPath);

        } else {
            loadFiles(rootPath);
        }
    }

    private Panel getCenterPanel() {
        Panel centerPanel = new Panel();
        centerPanel.setLayout(new FitLayout());
        centerPanel.setMargins(0, 0, 0, 0);
        centerPanel.setId("lfn-browser-center");
        centerPanel.setBorder(false);

        // Grid Panel
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("file-name"),
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
            new ColumnConfig("File Name", "file-name", 310, true),
            dirColumn
        };

        ColumnModel columnModel = new ColumnModel(columns);

        grid = new GridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setSelectionModel(cbSelectionModel);
        grid.setFrame(true);
        grid.setStripeRows(true);
        grid.setMargins(0, 0, 0, 0);

        store.load();
        centerPanel.add(grid);

        Toolbar centerTopToolbar = new Toolbar();
        pathItem = new ToolbarTextItem("/");
        centerTopToolbar.addItem(pathItem);

        centerPanel.setTopToolbar(centerTopToolbar);

        return centerPanel;
    }

    private Panel getLeftPanel() {
        Panel leftPanel = new Panel();

        leftPanel.setWidth(200);
        leftPanel.setMargins(0, 0, 0, 0);
        leftPanel.setCollapsible(true);
        leftPanel.setLayout(new FitLayout());
        leftPanel.setTitle("Grid Tree");
        leftPanel.setId("lfn-browser-left");

        // Tree Panel
        TreePanel logsTreePanel = new TreePanel();
        logsTreePanel.setBorder(false);
        logsTreePanel.setAnimate(true);
        logsTreePanel.setAutoScroll(true);
        logsTreePanel.setContainerScroll(true);
        logsTreePanel.setRootVisible(true);
        logsTreePanel.setWidth(180);
        logsTreePanel.setMargins(0, 0, 0, 0);

        root.setExpandable(true);
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
                loadFiles(node.getId());
            }
        });
        logsTreePanel.setRootNode(root);

        leftPanel.add(logsTreePanel);
        return leftPanel;
    }

    private void loadDir(String baseDir, final Node parent) {

        Ext.get("lfn-browser-left").mask("Loading...");

        this.parent = parent;
        LFNServiceAsync service = LFNService.Util.getInstance();
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get LFC dir: " + caught.getMessage());
            }

            public void onSuccess(List<String> result) {


                if (result != null) {
                    appendNodes(result);
                    if (!loadQueue.isEmpty()) {
                        String path = loadQueue.get(0);
                        loadQueue.remove(0);
                        for (Node node : parent.getChildNodes()) {
                            TreeNode treeNode = (TreeNode) node;
                            if (treeNode.getId().equals(path)) {
                                treeNode.expand();
                            }
                        }
                    }
                } else {
                    MessageBox.alert("Error", "Unable to get list of directories.");
                }
                Ext.get("lfn-browser-left").unmask();
            }
        };
        Context context = Context.getInstance();
        context.setLastGridFolderBrowsed(baseDir);
        Authentication auth = context.getAuthentication();
        service.listDir(auth.getProxyFileName(), baseDir, callback);
    }

    private void loadFiles(final String baseDir) {

        Ext.get("lfn-browser-center").mask("Loading...");
        LFNServiceAsync service = LFNService.Util.getInstance();
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get LFN files: " + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                if (result != null) {
                    Object[][] data = new Object[result.size()][2];
                    for (int i = 0; i < result.size(); i++) {
                        String[] r = result.get(i).split("##");
                        data[i][0] = r[0];
                        data[i][1] = baseDir;
                    }
                    PagingMemoryProxy proxy = new PagingMemoryProxy(data);
                    store.setDataProxy(proxy);
                    store.load();
                    store.commitChanges();
                } else {
                    MessageBox.alert("Error", "Unable to get list of files.");
                }
                Ext.get("lfn-browser-center").unmask();
            }
        };
        pathItem.setText(baseDir);
        Context context = Context.getInstance();
        context.setLastGridFolderBrowsed(baseDir);
        Authentication auth = context.getAuthentication();
        service.listFiles(auth.getProxyFileName(), baseDir, callback);
    }

    private void appendNodes(List<String> result) {

        ((TreeNode) parent).setExpanded(true);
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
                    loadFiles(node.getId());
                }
            });
            parent.appendChild(child);
        }
    }
}
