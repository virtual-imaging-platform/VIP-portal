/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author nouha
 */
public class GinsengTreeNode extends TreeNode {

    public GinsengTreeNode(String Id, String reportsTo, String name, String type, boolean isOpen, String restriction,boolean select) {
        setAttribute("Id", Id);
        setAttribute("ReportsTo", reportsTo);
        setAttribute("Name", name);
        setAttribute("Type", type);
        setAttribute("isOpen", isOpen);
        setAttribute("Restriction", restriction);
        setAttribute("Select", select);

    }
}