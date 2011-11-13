/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;
import com.smartgwt.client.widgets.tree.TreeGrid;
import java.util.List;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;

import com.smartgwt.client.widgets.tree.TreeNode;


/**
 *
 * @author glatard
 */
class FileTree extends TreeGrid{

    private String rdfFile = null;
            
    
    
    public FileTree(String zipFile, List<String> result)  {
        super();
        Tree fileTree = new Tree();
        fileTree.setModelType(TreeModelType.PARENT);
        fileTree.setRootValue(1);
        fileTree.setNameProperty("FileName");
        fileTree.setIdField("FileName");
        fileTree.setParentIdField("ArchiveName");
        fileTree.setOpenProperty("isOpen");
        TreeNode[] fileData = new TreeNode[result.size()];
        for (int i = 0; i < result.size(); i++) {
            String name = result.get(i).substring(result.get(i).lastIndexOf('/') + 1);
            if (name.endsWith(".rdf")) {
                rdfFile = result.get(i);
            }
            fileData[i] = new FileTreeNode(zipFile, name);
        }
        fileTree.setData(fileData);

        
        setWidth(500);
        setHeight(900);
        setNodeIcon("icon-file.png");
        setFolderIcon("icon-folder.png");
        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setShowOpenIcons(false);
        setDropIconSuffix("into");
        setClosedIconSuffix("");
        setDragDataAction(DragDataAction.MOVE);
        setData(fileTree);

        addLeafClickHandler(new LeafClickHandler() {

            public void onLeafClick(LeafClickEvent event) {
                SC.say("click");
            }
        });

        addLeafContextClickHandler(new LeafContextClickHandler() {

            public void onLeafContextClick(LeafContextClickEvent event) {
                SC.say("context click");
            }
        });
    }

    public String getRdfFile() {
        return rdfFile;
    }
    
     class FileTreeNode extends TreeNode {

        public FileTreeNode(String zipFileName, String fileName) {
            setAttribute("ArchiveName", zipFileName);
            setAttribute("FileName", fileName);
            setAttribute("isOpen", true);

            //    setAttribute("description", getFileDescription(zipFileName));

            //extract file
        }
    }
    
}
