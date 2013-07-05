/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.warehouse.client.view;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author cervenansky
 */
public class MedImgWarehouseTreeNode extends TreeNode {
    
    public MedImgWarehouseTreeNode(String id, String name, String description, MedImgWarehouseTreeNode... children )
    {
        setAttribute("id", id);
        setName( name);
        //setAttribute("description", description);
        //this.setChildren(children);
    }            
    
     public MedImgWarehouseTreeNode(String id, String name, String description) {
            this(id, name, description,  new MedImgWarehouseTreeNode[]{});
        }
}
