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
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;

import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

import com.smartgwt.client.widgets.tree.events.NodeClickHandler;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author glatard
 */
class FileTree extends TreeGrid {

    private String rdfFile = null;
    private int index = -1;
    private Logger logger = null;
    private ModelDisplay modelDisplay;

    public FileTree(String zipFile, List<String> result) {
        super();

        logger = Logger.getLogger("Models");
        final Tree fileTree = new Tree();
        fileTree.setModelType(TreeModelType.PARENT);
        fileTree.setRootValue(1);
        fileTree.setNameProperty("FileName");
        fileTree.setIdField("FileName");
        fileTree.setParentIdField("ArchiveName");
        fileTree.setOpenProperty("isOpen");
        setCanDragRecordsOut(true);
        int length = result.size();
        for(String res : result)
        {
            if (res.endsWith(".rdf"))
            {
                length = result.size() - 1;
                break;
            }
            else
            {
             //
            }
        }

        TreeNode[] fileData = new TreeNode[length];
        int i = 0;
        for(String res : result)
        {
            String name = res.substring(res.lastIndexOf('/') + 1);
            if (name.endsWith(".rdf")) {
                rdfFile = res;
            }
            else
            {
                fileData[i] = new FileTreeNode(zipFile, name);
                i++;
            }
            
        }
        if(fileData == null)
        {
            fileData[0] = new FileTreeNode(zipFile, ""); 
        }
        fileTree.setData(fileData);

        setWidth(500);
        setHeight(900);
        setNodeIcon("icon-file.png");
        setFolderIcon("icon-folder.png");
        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(true);
        setShowOpenIcons(false);
        setDropIconSuffix("into");
        setClosedIconSuffix("");
        setDragDataAction(DragDataAction.COPY);
        
        setData(fileTree);
   
      }

    public String getRdfFile() {
        return rdfFile;
    }

    public void setModelDisplay(ModelDisplay modDis) {
        modelDisplay = modDis;
    }

    public int typeDropped(String drop) {
        if (drop.contains(".mhd") || drop.contains(".zraw") || drop.contains(".raw")) {
            return 1; //voxels
        } else if (drop.contains("vtk") || drop.contains("vtp")) {
            return 0; //meshes
        } else if (drop.contains(".txt")) {
            return 2;
        } else {
            return -1;
        }


    }

    public FileTreeNode createNode(String archive, String fileName) {
        return new FileTreeNode(archive, fileName);
    }

    class FileTreeNode extends TreeNode {

        public FileTreeNode(String zipFileName, String fileName) {
            setAttribute("ArchiveName", zipFileName);
            setAttribute("FileName", fileName);
            setAttribute("isOpen", true);

        }
    }
}
