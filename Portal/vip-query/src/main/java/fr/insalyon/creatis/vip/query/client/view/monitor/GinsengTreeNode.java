/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author Nouha Boujelben
 */
public class GinsengTreeNode extends TreeNode {

    public GinsengTreeNode(String Id, String reportsTo, String name,String propName, String icon, String type, boolean isOpen, String restriction,boolean select,String value,boolean isFolder,boolean open ) {
        setAttribute("Id", Id);
        setAttribute("ReportsTo", reportsTo);
        setAttribute("Name", name);
        setAttribute("PropName", propName);
        setAttribute("icon", icon);
        setAttribute("Type", type);
        setAttribute("isOpen", isOpen);
        setAttribute("Restriction", restriction);
        setAttribute("Select", select);
        setAttribute("Value", value);
        setAttribute("isSelected", select);
        setAttribute("isFolder",isFolder );
         setAttribute("open", open);
       

    }
    
     public GinsengTreeNode(String Id, String reportsTo, String name, String type, boolean isOpen, String restriction,boolean select,String value) {
        setAttribute("Id", Id);
        setAttribute("ReportsTo", reportsTo);
        setAttribute("Name", name);
        setAttribute("Type", type);
        setAttribute("isOpen", isOpen);
        setAttribute("Restriction", restriction);
        setAttribute("Select", select);
        setAttribute("Value", value);
        setAttribute("isSelected", select);

        

    }
}