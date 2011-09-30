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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.menu.InOutContextMenu;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class GeneralTab extends Tab {

    private String simulationID;
    private String simulationName;
    private ModalWindow generalModal;
    private ListGrid generalGrid;
    private ModalWindow inOutTreeModal;
    private TreeGrid inOutTreeGrid;
    private Tree inOutTree;
    private InOutTreeNode inputs;
    private InOutTreeNode outputs;

    public GeneralTab(String simulationID, String simulationName) {

        this.simulationID = simulationID;
        this.simulationName = simulationName;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_GENERAL));
        this.setPrompt("General Information");

        HLayout hLayout = new HLayout(15);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.setPadding(10);

        // Left column
        VLayout leftLayout = new VLayout(15);
        leftLayout.setWidth("55%");
        leftLayout.setHeight100();
        leftLayout.setOverflow(Overflow.AUTO);

        configureGeneralGrid();
        generalModal = new ModalWindow(generalGrid);
        leftLayout.addMember(generalGrid);
        
        leftLayout.addMember(new LogsWindow(simulationID));

        // Right column
        VLayout rightLayout = new VLayout(15);
        rightLayout.setWidth("45%");
        rightLayout.setHeight100();
        rightLayout.setOverflow(Overflow.AUTO);

        configureTreeGrid();
        inOutTreeModal = new ModalWindow(inOutTreeGrid);
        rightLayout.addMember(inOutTreeGrid);

        hLayout.addMember(leftLayout);
        hLayout.addMember(rightLayout);

        this.setPane(hLayout);
        loadData();
    }

    public void loadData() {

        loadSimulationInfo();
        loadTreeData(inputs, InOutTreeNode.Icon.Input);
        loadTreeData(outputs, InOutTreeNode.Icon.Output);
    }

    private void configureGeneralGrid() {

        generalGrid = new ListGrid() {

            @Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {

                if (getFieldName(colNum).equals("value")) {
                    PropertyRecord propertyRecord = (PropertyRecord) record;
                    SimulationStatus status = SimulationStatus.valueOf(propertyRecord.getValue());
                    
                    if (status == SimulationStatus.Running) {
                        return "font-weight:bold; color:#009900;";

                    } else if (status == SimulationStatus.Completed) {
                        return "font-weight:bold; color:#287fd6;";

                    } else if (status == SimulationStatus.Killed) {
                        return "font-weight:bold; color:#d64949;";
                    }
                }
                return super.getCellCSSText(record, rowNum, colNum);
            }
        };
        generalGrid.setWidth100();
        generalGrid.setHeight(160);
        generalGrid.setShowAllRecords(true);
        generalGrid.setShowEmptyMessage(true);
        generalGrid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");

        generalGrid.setFields(propertyField, valueField);
    }

    private void loadSimulationInfo() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Simulation> callback = new AsyncCallback<Simulation>() {

            public void onFailure(Throwable caught) {
                generalModal.hide();
                SC.warn("Unable to load general information:<br />" + caught.getMessage());
            }

            public void onSuccess(Simulation result) {
                generalModal.hide();
                generalGrid.setData(new PropertyRecord[]{
                            new PropertyRecord("Simulation Name", result.getSimulationName()),
                            new PropertyRecord("Simulation Identifier", result.getID()),
                            new PropertyRecord("Submitted Time", result.getDate().toString()),
                            new PropertyRecord("Owner", result.getUserName()),
                            new PropertyRecord("Application", result.getApplication()),
                            new PropertyRecord("Status", result.getMajorStatus())
                        });
            }
        };
        generalModal.show("Loading data...", true);
        service.getSimulation(simulationID, callback);
    }

    private void configureTreeGrid() {

        inOutTree = new Tree();
        inOutTree.setModelType(TreeModelType.CHILDREN);
        inOutTree.setNameProperty("name");

        InOutTreeNode root = new InOutTreeNode("Root", "String", InOutTreeNode.Icon.Simulation);
        inOutTree.setRoot(root);

        InOutTreeNode node = new InOutTreeNode("Simulation: " + simulationName, "Simulation", InOutTreeNode.Icon.Simulation);
        inOutTree.add(node, root);

        inputs = new InOutTreeNode("Inputs", "Simulation", InOutTreeNode.Icon.Input);
        inOutTree.add(inputs, node);
        outputs = new InOutTreeNode("Outputs", "Simulation", InOutTreeNode.Icon.Output);
        inOutTree.add(outputs, node);
        inOutTree.openFolder(node);

        inOutTreeGrid = new TreeGrid();
        inOutTreeGrid.setWidth100();
        inOutTreeGrid.setHeight100();
        inOutTreeGrid.setShowOpenIcons(false);
        inOutTreeGrid.setShowDropIcons(false);
        inOutTreeGrid.setShowHeader(true);
        inOutTreeGrid.setFolderIcon(ApplicationConstants.ICON_TREE_SERVICE);
        inOutTreeGrid.setClosedIconSuffix("");
        inOutTreeGrid.setLoadDataOnDemand(true);
        inOutTreeGrid.setFields(new TreeGridField("name", "In/Output Data"));
        inOutTreeGrid.addNodeContextClickHandler(new NodeContextClickHandler() {

            public void onNodeContextClick(NodeContextClickEvent event) {
                event.cancel();
                InOutTreeNode node = (InOutTreeNode) event.getNode();
                new InOutContextMenu(simulationID, inOutTree, node, inOutTreeModal).showContextMenu();
            }
        });
        inOutTreeGrid.setData(inOutTree);
    }

    private void loadTreeData(final InOutTreeNode parent, final InOutTreeNode.Icon icon) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<InOutData>> callback = new AsyncCallback<List<InOutData>>() {

            public void onFailure(Throwable caught) {
                inOutTreeModal.hide();
                SC.warn("Unable to load data:<br />" + caught.getMessage());
            }

            public void onSuccess(List<InOutData> result) {

                for (InOutData data : result) {

                    InOutTreeNode processor = null;
                    InOutTreeNode output = null;

                    for (TreeNode n : inOutTree.getChildren(parent)) {
                        InOutTreeNode node = (InOutTreeNode) n;
                        if (node.getName().equals(data.getProcessor())) {
                            processor = node;
                            break;
                        }
                    }
                    if (processor == null) {
                        processor = addInOutTreeNode(data.getProcessor(), "String", parent, icon);
                        addInOutTreeNode(data.getPath(), data.getType(), processor, icon);
                        continue;
                    }

                    for (TreeNode n : inOutTree.getChildren(processor)) {
                        InOutTreeNode node = (InOutTreeNode) n;
                        if (node.getName().equals(data.getPath())) {
                            output = node;
                            break;
                        }
                    }
                    if (output == null) {
                        addInOutTreeNode(data.getPath(), data.getType(), processor, icon);
                    }
                }
                inOutTreeModal.hide();
            }
        };
        inOutTreeModal.show("Loading data...", true);
        if (icon == InOutTreeNode.Icon.Input) {
            service.getInputData(simulationID, callback);
        } else {
            service.getOutputData(simulationID, callback);
        }
    }

    private InOutTreeNode addInOutTreeNode(String name, String type,
            InOutTreeNode parent, InOutTreeNode.Icon icon) {

        InOutTreeNode node = new InOutTreeNode(name, type, icon);
        inOutTree.add(node, parent);
        return node;
    }
}
