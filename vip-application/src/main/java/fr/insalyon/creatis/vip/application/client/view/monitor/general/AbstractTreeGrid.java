package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.InOutTreeNode.Icon;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class AbstractTreeGrid extends TreeGrid {

    protected String simulationID;
    protected Tree tree;
    protected InOutTreeNode treeNode;

    public AbstractTreeGrid(String simulationID, String title, String nodeTitle, Icon icon) {

        this.simulationID = simulationID;
        this.setWidth100();
        this.setHeight100();
        this.setShowOpenIcons(false);
        this.setShowDropIcons(false);
        this.setShowHeader(true);
        this.setFolderIcon(ApplicationConstants.ICON_TREE_SERVICE);
        this.setCanHover(true);
        this.setClosedIconSuffix("");
        this.setLoadDataOnDemand(true);
        this.setFields(new TreeGridField("name", title));

        tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setNameProperty("name");

        InOutTreeNode root = new InOutTreeNode("Root", "String", InOutTreeNode.Icon.Simulation);
        tree.setRoot(root);

        treeNode = new InOutTreeNode(nodeTitle, "Simulation", icon);
        tree.add(treeNode, root);
        
        this.setData(tree);
    }

    protected void loadData(final InOutTreeNode parent, final Icon icon) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        AsyncCallback<List<InOutData>> callback = new AsyncCallback<List<InOutData>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<InOutData> result) {

                for (InOutData data : result) {

                    InOutTreeNode processor = null;
                    InOutTreeNode baseNode = null;

                    for (TreeNode n : tree.getChildren(parent)) {
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
                    for (TreeNode n : tree.getChildren(processor)) {
                        InOutTreeNode node = (InOutTreeNode) n;
                        if (node.getName().equals(data.getPath())) {
                            baseNode = node;
                            break;
                        }
                    }
                    if (baseNode == null) {
                        addInOutTreeNode(data.getPath(), data.getType(), processor, icon);
                    }
                }
                tree.openAll(parent);
            }
        };
        if (icon == Icon.Input) {
            service.getInputData(simulationID, callback);
        } else {
            service.getOutputData(simulationID, callback);
        }
    }

    private InOutTreeNode addInOutTreeNode(String name, String type,
            InOutTreeNode parent, Icon icon) {

        InOutTreeNode node = new InOutTreeNode(name, type, icon);
        tree.add(node, parent);
        return node;
    }
}
