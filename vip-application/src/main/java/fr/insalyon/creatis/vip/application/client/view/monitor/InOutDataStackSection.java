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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.menu.InOutContextMenu;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class InOutDataStackSection extends SectionStackSection {

    private String simulationID;
    private String title;
    private ModalWindow modal;
    private TreeGrid treeGrid;
    private Tree tree;
    private InOutTreeNode inputs;
    private InOutTreeNode outputs;

    public InOutDataStackSection(String simulationID, String title) {

        this.simulationID = simulationID;
        this.title = title;
        this.setTitle("In/Output Data");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureTree();
        modal = new ModalWindow(treeGrid);

        loadData();
    }

    private void configureTree() {

        tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setNameProperty("name");

        InOutTreeNode root = new InOutTreeNode("Root", "String", InOutTreeNode.Icon.Simulation);
        tree.setRoot(root);

        InOutTreeNode node = new InOutTreeNode("Simulation: " + title, "Simulation", InOutTreeNode.Icon.Simulation);
        tree.add(node, root);

        inputs = new InOutTreeNode("Inputs", "Simulation", InOutTreeNode.Icon.Input);
        tree.add(inputs, node);
        outputs = new InOutTreeNode("Outputs", "Simulation", InOutTreeNode.Icon.Output);
        tree.add(outputs, node);
        tree.openFolder(node);

        treeGrid = new TreeGrid();
        treeGrid.setWidth100();
        treeGrid.setHeight100();
        treeGrid.setShowOpenIcons(false);
        treeGrid.setShowDropIcons(false);
        treeGrid.setShowHeader(false);
        treeGrid.setFolderIcon("icon-tree-service.png");
        treeGrid.setClosedIconSuffix("");
        treeGrid.setLoadDataOnDemand(true);
        treeGrid.setFields(new TreeGridField("name", "Name"));
        treeGrid.addNodeContextClickHandler(new NodeContextClickHandler() {

            public void onNodeContextClick(NodeContextClickEvent event) {
                event.cancel();
                InOutTreeNode node = (InOutTreeNode) event.getNode();
                new InOutContextMenu(simulationID, tree, node, modal).showContextMenu();
            }
        });
        treeGrid.setData(tree);

        this.addItem(treeGrid);
    }

    public void loadData() {
        loadData(inputs, InOutTreeNode.Icon.Input);
        loadData(outputs, InOutTreeNode.Icon.Output);
    }

    private void loadData(final InOutTreeNode parent, final InOutTreeNode.Icon icon) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<InOutData>> callback = new AsyncCallback<List<InOutData>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Unable to load data: " + caught.getMessage());
            }

            public void onSuccess(List<InOutData> result) {

                for (InOutData data : result) {

                    InOutTreeNode processor = null;
                    InOutTreeNode output = null;

                    for (TreeNode n : tree.getChildren(parent)) {
                        InOutTreeNode node = (InOutTreeNode) n;
                        if (node.getName().equals(data.getProcessor())) {
                            processor = node;
                            break;
                        }
                    }
                    if (processor == null) {
                        processor = addNode(data.getProcessor(), "String", parent, icon);
                        addNode(data.getPath(), data.getType(), processor, icon);
                        continue;
                    }

                    for (TreeNode n : tree.getChildren(processor)) {
                        InOutTreeNode node = (InOutTreeNode) n;
                        if (node.getName().equals(data.getPath())) {
                            output = node;
                            break;
                        }
                    }
                    if (output == null) {
                        addNode(data.getPath(), data.getType(), processor, icon);
                    }
                }
            }
        };
        if (icon == InOutTreeNode.Icon.Input) {
            service.getInputData(simulationID, callback);
        } else {
            service.getOutputData(simulationID, callback);
        }
    }

    private InOutTreeNode addNode(String name, String type,
            InOutTreeNode parent, InOutTreeNode.Icon icon) {

        InOutTreeNode node = new InOutTreeNode(name, type, icon);
        tree.add(node, parent);
        return node;
    }
}
