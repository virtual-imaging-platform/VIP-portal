/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
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
