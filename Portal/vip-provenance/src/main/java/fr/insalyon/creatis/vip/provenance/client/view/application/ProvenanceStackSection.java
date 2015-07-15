/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.provenance.client.view.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;
import fr.insalyon.creatis.vip.provenance.client.bean.ProvenanceData;
import fr.insalyon.creatis.vip.provenance.client.rpc.ProvenanceService;
import fr.insalyon.creatis.vip.provenance.client.rpc.ProvenanceServiceAsync;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ProvenanceStackSection extends SectionStackSection {

    private String simulationID;
    private TreeGrid treeGrid;
    private Tree tree;
    private DataTreeNode node;

    public ProvenanceStackSection(String simulationID, boolean completed) {

        this.simulationID = simulationID;
        this.setTitle("In/Output Data");
        this.setCanCollapse(true);
        this.setExpanded(false);
        this.setResizeable(true);

        configureTree();
        loadSimulationInputAndOutputs();
    }

    private void configureTree() {

        tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setNameProperty("name");

        DataTreeNode root = new DataTreeNode("Root", "1", "Simulation");
        node = new DataTreeNode(simulationID, "2", "Simulation");
        tree.setRoot(root);
        tree.add(node, root);

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
        treeGrid.addNodeClickHandler(new NodeClickHandler() {

            public void onNodeClick(NodeClickEvent event) {
                DataTreeNode selectedNode = (DataTreeNode) event.getNode();

                if (!tree.hasChildren(selectedNode)) {
                    if (selectedNode.getType().equals("Input")) {
                        loadDerivedData(selectedNode);

                    } else if (selectedNode.getType().equals("Output")) {
                        loadDataOrigins(selectedNode);
                    }
                }
            }
        });
        treeGrid.setData(tree);

        this.addItem(treeGrid);
    }

    private void loadSimulationInputAndOutputs() {
        ProvenanceServiceAsync service = ProvenanceService.Util.getInstance();
        AsyncCallback<List<ProvenanceData>> callback = new AsyncCallback<List<ProvenanceData>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Unable to load inputs and outputs.");
            }

            public void onSuccess(List<ProvenanceData> result) {
                if (result != null) {
                    for (ProvenanceData data : result) {
                        tree.add(new DataTreeNode(data.getName(), data.getUri(),
                                data.getType().name()), node);
                    }
                }
            }
        };
        service.getSimulationInputAndOutputs(simulationID, callback);
    }

    private void loadDerivedData(final DataTreeNode selectedNode) {
        ProvenanceServiceAsync service = ProvenanceService.Util.getInstance();
        AsyncCallback<List<ProvenanceData>> callback = new AsyncCallback<List<ProvenanceData>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Unable to load inputs and outputs.");
            }

            public void onSuccess(List<ProvenanceData> result) {
                if (result != null) {
                    for (ProvenanceData data : result) {
                        tree.add(new DataTreeNode(data.getName(), data.getUri(),
                                data.getType().name()), selectedNode);
                    }
                    tree.openFolder(selectedNode);
                }
            }
        };
        service.getDerivedData(selectedNode.getUri(), callback);
    }

    private void loadDataOrigins(final DataTreeNode selectedNode) {
        ProvenanceServiceAsync service = ProvenanceService.Util.getInstance();
        AsyncCallback<List<ProvenanceData>> callback = new AsyncCallback<List<ProvenanceData>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Unable to load inputs and outputs.");
            }

            public void onSuccess(List<ProvenanceData> result) {
                if (result != null) {
                    for (ProvenanceData data : result) {
                        tree.add(new DataTreeNode(data.getName(), data.getUri(),
                                data.getType().name()), selectedNode);
                    }
                    tree.openFolder(selectedNode);
                }
            }
        };
        service.getDataOrigins(selectedNode.getUri(), callback);
    }
}
