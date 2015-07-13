/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.client.view;

import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.warehouse.client.WarehouseConstants;

/**
 *
 * @author cervenansky
 */
public class WareTreeNode extends TreeNode {
    
    public WareTreeNode(String id, String name, String description, String type, WareTreeNode... children )
    {
        setAttribute("id", id);
        setAttribute("name",name);
        setAttribute("description", description);
        setAttribute("type", type);
        if(type.equals("folder"))
            this.setIcon(WarehouseConstants.ICON_FOLDER);
        else
            this.setIcon(WarehouseConstants.ICON_FILE);
        this.setChildren(children);
    }            
    
     public WareTreeNode(String id, String name, String description, String type) {
            this(id, name, description, type, new WareTreeNode[]{});
        }
}
