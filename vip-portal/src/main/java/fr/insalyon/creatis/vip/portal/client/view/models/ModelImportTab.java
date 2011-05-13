/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.portal.client.view.models;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ClickEvent;
import fr.insalyon.creatis.vip.portal.client.rpc.ModelService;
import fr.insalyon.creatis.vip.portal.client.rpc.ModelServiceAsync;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author glatard
 */
class ModelImportTab extends Tab {
    //private final ToolStrip toolStrip;

    private static ModelImportTab instance = null;
   public static ModelImportTab getInstance(){
       if(instance == null){
        instance = new ModelImportTab();
       }
       return instance;

   }
    private VLayout vLayout;
    public ModelImportTab() {
        this.setTitle("Import model");
        this.setID("model-import-tab");
        this.setCanClose(true);
        vLayout = new VLayout();

//        //toolStrip
//        toolStrip = new ToolStrip();
//        toolStrip.setWidth(400);
    
        

        Button addButton = new Button("Upload zip file");
        addButton.setIcon("icon-add.png");
        addButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                new FileUploadWindow("local").show();
            }
        });
       

        final TabSet tabSet = new TabSet();
        tabSet.setTabBarPosition(Side.TOP);
        tabSet.setWidth(250);
        tabSet.setHeight(200);

        Tab tTab1 = new Tab("T1", "pieces/16/pawn_blue.png");
        Img tImg1 = new Img("pieces/48/pawn_blue.png", 48, 48);
        tTab1.setPane(tImg1);

        Tab tTab2 = new Tab("T2", "pieces/16/pawn_green.png");
        Img tImg2 = new Img("pieces/48/pawn_green.png", 48, 48);
        tTab2.setPane(tImg2);

        tabSet.addTab(tTab1);
        tabSet.addTab(tTab2);

        tabSet.addDropHandler(new DropHandler(){

            public void onDrop(DropEvent event) {
                
                SC.say("Droped: "+EventHandler.getDragTarget().toString());
            }
        });


        HStack hs = new HStack(10);
        hs.addMember(vLayout);
        hs.addMember(tabSet);

        VLayout vl = new VLayout();
        vl.addMember(addButton);
        vl.addMember(hs);
        
        this.setPane(vl);
    }

    void addFile(final String zipName) {
        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error processing file\n");
            }

            public void onSuccess(List<String> result) {
                String message = "OK: ";
                for (String s : result) {
                    message += " - " + s;
                }
                SC.say(message);
                addFileBox(zipName, result);

            }
        };
        ms.getFiles(zipName, callback);
    }

     public class FileTreeNode extends TreeNode {
        public FileTreeNode(String zipFileName, String fileName) {
            setAttribute("ArchiveName", zipFileName);
            setAttribute("FileName", fileName);
            setAttribute("isOpen", true);

            setAttribute("description",getDescription(zipFileName));

            //extract file
        }
    }

     public String getDescription(String fileName){
        String description = "unrecognized file";
        String ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length()-1);
        if(ext.equals("vtu"))
            description = "US scatterers";
        return description;

     }
     public void addFileBox(String zipName, List<String> result){
         Tree fileTree = new Tree();
         fileTree.setModelType(TreeModelType.PARENT);
         fileTree.setRootValue(1);
         fileTree.setNameProperty("FileName");
         fileTree.setIdField("FileName");
         fileTree.setParentIdField("ArchiveName");
         fileTree.setOpenProperty("isOpen");
         TreeNode[] fileData = new TreeNode[result.size()];
         for (int i = 0; i < result.size(); i++) {
             fileData[i] = new FileTreeNode(zipName, result.get(i));
         }
         fileTree.setData(fileData);

         TreeGrid fileTreeGrid = new TreeGrid();
         fileTreeGrid = new TreeGrid();
         fileTreeGrid.setWidth(250);
         fileTreeGrid.setHeight(200);
         fileTreeGrid.setNodeIcon("icon-file.png");
         fileTreeGrid.setFolderIcon("icon-folder.png");
         fileTreeGrid.setCanReorderRecords(true);
         fileTreeGrid.setCanAcceptDroppedRecords(true);
         fileTreeGrid.setShowOpenIcons(false);
         fileTreeGrid.setDropIconSuffix("into");
         fileTreeGrid.setClosedIconSuffix("");
         fileTreeGrid.setDragDataAction(DragDataAction.MOVE);
         fileTreeGrid.setData(fileTree);

         vLayout.addMember(fileTreeGrid);
     }

     public void resetTab(){

        vLayout.clear();
     }
}
