package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;

/**
 *
 * @author Rafael Silva
 */
public class InOutTreeNode extends TreeNode {
    
    public static enum Icon { Input, Output, Simulation, Other }
    
    public InOutTreeNode(String name, String type, Icon icon) {
        
        setAttribute("name", name);
        setAttribute("type", type);
        setIcon(icon);
    }
    
    private void setIcon(Icon icon) {
        
        if (icon == Icon.Input) {
            setIcon(ApplicationConstants.ICON_TREE_INPUT);
        } else if (icon == Icon.Output) {
            setIcon(ApplicationConstants.ICON_TREE_OUTPUT);
        } else if (icon == Icon.Simulation) {
            setIcon(ApplicationConstants.ICON_TREE_SIMULATION);
        } else if (icon == Icon.Other) {
            setIcon(ApplicationConstants.ICON_TREE_SERVICE);
        }
    }
    
    public String getType() {
        return getAttributeAsString("type");
    }
}
