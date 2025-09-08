package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class OutputTreeGrid extends AbstractTreeGrid {

    public OutputTreeGrid(final String simulationID) {

        super(simulationID, "Output Data", "Outputs", InOutTreeNode.Icon.Output);

        this.addNodeContextClickHandler(new NodeContextClickHandler() {
            @Override
            public void onNodeContextClick(NodeContextClickEvent event) {
                event.cancel();
                InOutTreeNode node = (InOutTreeNode) event.getNode();
                new InOutContextMenu(simulationID, tree, node).showContextMenu();
            }
        });
        loadData(treeNode, InOutTreeNode.Icon.Output);
    }

    public void update() {
        loadData(treeNode, InOutTreeNode.Icon.Output);
    }
}
