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
package fr.insalyon.creatis.platform.main.client.view.application.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
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
import fr.insalyon.creatis.platform.main.client.bean.WorkflowInput;
import fr.insalyon.creatis.platform.main.client.rpc.WorkflowService;
import fr.insalyon.creatis.platform.main.client.rpc.WorkflowServiceAsync;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class LoadInputWindow extends Window {

    private String workflowName;
    private LaunchPanel launchPanel;
    private TreeNode root;
    private Store store;
    private TextField selectedInput;
    private Menu menu;
    private String nodeToRemove;

    public LoadInputWindow(String workflowName, LaunchPanel launchPanel) {

        this.workflowName = workflowName;
        this.launchPanel = launchPanel;

        this.setTitle("Input Browser");
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

        this.add(panel);
        this.show();

        loadInputNames();
    }

    private Panel getLeftPanel() {
        Panel leftPanel = new Panel();

        leftPanel.setWidth(200);
        leftPanel.setMargins(0, 0, 0, 0);
        leftPanel.setCollapsible(true);
        leftPanel.setLayout(new FitLayout());
        leftPanel.setTitle("" +
                "" +
                "" +
                "Input");
        leftPanel.setId("simu-input-browser-left");

        // Tree Panel
        TreePanel logsTreePanel = new TreePanel();
        logsTreePanel.setBorder(false);
        logsTreePanel.setAnimate(true);
        logsTreePanel.setAutoScroll(true);
        logsTreePanel.setContainerScroll(true);
        logsTreePanel.setRootVisible(true);
        logsTreePanel.setWidth(180);
        logsTreePanel.setMargins(0, 0, 0, 0);

        root = new TreeNode(workflowName);
        root.setExpandable(true);
        root.setExpanded(true);
        root.setId(workflowName);
        logsTreePanel.setRootNode(root);

        leftPanel.add(logsTreePanel);
        return leftPanel;
    }

    private Panel getCenterPanel() {

        Panel centerPanel = new Panel();
        centerPanel.setLayout(new FitLayout());
        centerPanel.setMargins(0, 0, 0, 0);
        centerPanel.setId("simu-input-browser-center");
        centerPanel.setBorder(false);

        MemoryProxy proxy = new MemoryProxy(new Object[][]{new Object[]{}});
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("workflow-param"),
                    new StringFieldDef("workflow-value")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        store = new Store(proxy, reader);
        store.load();

        ColumnConfig propsConfig = new ColumnConfig("Parameter", "workflow-param", 148, true);
        ColumnConfig valueConfig = new ColumnConfig("Value", "workflow-value", 298, true);

        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
                    propsConfig,
                    valueConfig
                });

        GridPanel grid = new GridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setFrame(true);

        centerPanel.add(grid);

        Toolbar topToolbar = new Toolbar();
        ToolbarTextItem textItem = new ToolbarTextItem("Selected Input: ");
        topToolbar.addItem(textItem);
        selectedInput = new TextField();
        selectedInput.setWidth(200);
        selectedInput.setReadOnly(true);
        topToolbar.addField(selectedInput);

        ToolbarButton loadButton = new ToolbarButton("Load", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {

                if (!selectedInput.getValueAsString().equals("")) {
                    loadInput(selectedInput.getValueAsString());

                } else {
                    MessageBox.alert("Error", "No simulation input selected.");
                }
            }
        });
        topToolbar.addButton(loadButton);
        topToolbar.addSeparator();
        ToolbarButton cancelButton = new ToolbarButton("Cancel", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                close();
            }
        });
        topToolbar.addButton(cancelButton);
        centerPanel.setTopToolbar(topToolbar);

        return centerPanel;
    }

    private void loadInputNames() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<WorkflowInput>> callback = new AsyncCallback<List<WorkflowInput>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get simulations inputs: " + caught.getMessage());
            }

            public void onSuccess(List<WorkflowInput> result) {

                for (WorkflowInput wi : result) {
                    TreeNode child = new TreeNode(wi.getName());
                    child.setId(wi.getName());
                    child.addListener(new TreeNodeListenerAdapter() {

                        @Override
                        public void onClick(Node node, EventObject e) {
                            loadInputInfo(node.getId());
                        }

                        @Override
                        public void onDblClick(Node node, EventObject e) {
                            loadInput(node.getId());
                        }

                        @Override
                        public void onContextMenu(Node node, EventObject e) {
                            nodeToRemove = node.getId();
                            showContextMenu(e);
                        }
                    });
                    root.appendChild(child);
                }
                root.setExpanded(true);
            }
        };
        service.getWorkflowsInputByUserAndAppName(workflowName, callback);
    }

    private void showContextMenu(EventObject e) {

        if (menu == null) {
            menu = new Menu();
            Item removeItem = new Item("Remove", new BaseItemListenerAdapter() {

                @Override
                public void onClick(BaseItem item, EventObject e) {
                    MessageBox.confirm("Confirm", "Do you really want to remove the entry \"" + nodeToRemove + "\"?",
                            new MessageBox.ConfirmCallback() {

                                public void execute(String btnID) {
                                    if (btnID.toLowerCase().equals("yes")) {
                                        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
                                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                            public void onFailure(Throwable caught) {
                                                MessageBox.alert("Error", "Error removing entry: " + caught.getMessage());
                                            }

                                            public void onSuccess(Void v) {
                                                for (Node n : root.getChildNodes()) {
                                                    root.removeChild(n);
                                                }
                                                loadInputNames();
                                                if (selectedInput.getValueAsString().equals(nodeToRemove)) {
                                                    selectedInput.setValue("");
                                                    cleanGrid();
                                                }
                                            }
                                        };
                                        service.removeWorkflowInput(nodeToRemove, callback);
                                    }
                                }
                            });
                }
            });
            menu.addItem(removeItem);
        }
        menu.showAt(e.getXY());
    }

    private void loadInputInfo(String inputName) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<WorkflowInput> callback = new AsyncCallback<WorkflowInput>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get simulation inputs: " + caught.getMessage());
            }

            public void onSuccess(WorkflowInput result) {
                String[] inputs = result.getInputs().split("--");
                Object[][] data = new Object[inputs.length][2];

                int i = 0;
                for (String in : inputs) {
                    String[] pair = in.split("=");
                    data[i][0] = pair[0];
                    if (!in.contains("##")) {
                        if (in.contains("@@")) {
                            String[] v = pair[1].split("@@");
                            data[i][1] = "";
                            for (String s : v) {
                                data[i][1] = data[i][1] + s + "; ";
                            }
                        } else {
                            data[i][1] = pair[1];
                        }
                    } else {
                        String[] v = pair[1].split("##");
                        data[i][1] = "Start: " + v[0] + " - Stop: " + v[1] + " - Step: " + v[2];
                    }
                    i++;
                }
                MemoryProxy proxy = new MemoryProxy(data);
                store.setDataProxy(proxy);
                store.load();
                store.commitChanges();
            }
        };
        service.getWorkflowInputByUserAndName(inputName, callback);
        selectedInput.setValue(inputName);
    }

    private void loadInput(String inputName) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<WorkflowInput> callback = new AsyncCallback<WorkflowInput>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get simulation inputs: " + caught.getMessage());
            }

            public void onSuccess(WorkflowInput result) {
                close();
                launchPanel.loadInput(result.getInputs());
            }
        };
        Ext.get(launchPanel.getId()).mask("Loading Input Values...");
        service.getWorkflowInputByUserAndName(inputName, callback);
    }

    private void cleanGrid() {
        MemoryProxy proxy = new MemoryProxy(new Object[][]{new Object[]{}});
        store.setDataProxy(proxy);
        store.load();
        store.commitChanges();
    }
}
