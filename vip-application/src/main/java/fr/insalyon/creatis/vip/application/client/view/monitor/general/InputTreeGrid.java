package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;
import fr.insalyon.creatis.vip.application.client.view.monitor.general.InOutTreeNode.Icon;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class InputTreeGrid extends AbstractTreeGrid {

    public InputTreeGrid(final String simulationID) {

        super(simulationID, "Input Data", "Inputs", Icon.Input);

        this.addNodeContextClickHandler(new NodeContextClickHandler() {
            @Override
            public void onNodeContextClick(NodeContextClickEvent event) {
                event.cancel();
                InOutTreeNode node = (InOutTreeNode) event.getNode();
                new InOutContextMenu(simulationID, tree, node).showContextMenu();
            }
        });
        loadData(treeNode, Icon.Input);
    }

    public void update() {
        loadData(treeNode, Icon.Input);
    }
}
