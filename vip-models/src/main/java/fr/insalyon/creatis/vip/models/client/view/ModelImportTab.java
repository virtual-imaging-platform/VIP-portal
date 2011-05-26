/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;

import java.util.List;

/**
 *
 * @author glatard
 */
class ModelImportTab extends Tab {
    //private final ToolStrip toolStrip;

    private static ModelImportTab instance = null;

    private SimulationObjectModel model = null;
    private TreeGrid modelTreeGrid;
    private VLayout fileVLayout;

    public static ModelImportTab getInstance() {
        if (instance == null) {
            instance = new ModelImportTab();
        }
        return instance;

    }
   


    public ModelImportTab() {

        this.setTitle("Import model");
        this.setID("model-import-tab");
        this.setCanClose(true);

        modelTreeGrid = new TreeGrid();
        fileVLayout = new VLayout();

//        modelTreeGrid.setCanAcceptDrop(true);
//        modelTreeGrid.setCanAcceptDroppedRecords(true);
//        modelTreeGrid.addFolderDropHandler(new FolderDropHandler(){
//
//            public void onFolderDrop(FolderDropEvent event) {
//                SC.say("drop!");
//            }
//        });
       
        Button addButton = new Button("Upload zip file");
        addButton.setIcon("icon-add.png");
        addButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                new FileUploadWindow("local").show();
            }
        });


//create a new model that will be iteratively populated
        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {
            public void onFailure(Throwable caught) {
                SC.warn("Cannot initialize the model");
            }
            public void onSuccess(SimulationObjectModel result) {
                model = result;
                display(model);
                ModelImportTab.getInstance().setTitle("New model");
                SC.say("Model initialized");
            }
        };
        ms.createModel("New model", callback);

        HStack hs = new HStack(10);
        hs.addMember(fileVLayout);
        hs.addMember(modelTreeGrid);

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

            setAttribute("description", getDescription(zipFileName));

            //extract file
        }
    }

    public String getDescription(String fileName) {
        String description = "unrecognized file";
        String ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length() - 1);
        if (ext.equals("vtu")) {
            description = "US scatterers";
        }
        return description;

    }

    public void addFileBox(String zipName, List<String> result) {

        Window window = new Window();
        window.setTitle(zipName+" - drag and drop files to the model window");
        window.setWidth(260);
        window.setHeight(230);
        window.setCanDragReposition(true);
        window.setCanDragResize(true);

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

        fileTreeGrid.addLeafClickHandler(new LeafClickHandler() {

            public void onLeafClick(LeafClickEvent event) {
                SC.say("click");
            }
            
            
        });
        
        fileTreeGrid.addLeafContextClickHandler(new LeafContextClickHandler(){

            public void onLeafContextClick(LeafContextClickEvent event) {
                SC.say("context click");
            }
        });
        
        window.addItem(fileTreeGrid);
        fileVLayout.addMember(window);
    }

    public void resetTab() {

        fileVLayout.clear();
    }

    private void display(SimulationObjectModel model) {
        TreeGrid tg = new TreeGrid();
        modelTreeGrid = tg;

    }

    public class ModelTreeNode extends TreeNode {

        public ModelTreeNode(String zipFileName, String fileName) {
            setAttribute("ArchiveName", zipFileName);
            setAttribute("FileName", fileName);
            setAttribute("isOpen", true);

            setAttribute("description", getDescription(zipFileName));

            //extract file
        }
    }
    
  
}
// final TabSet tabSet = new TabSet();
//        tabSet.setTabBarPosition(Side.TOP);
//        tabSet.setWidth(250);
//        tabSet.setHeight(200);
//
//        Tab tTab1 = new Tab("T1", "icon_calendar.png");
//        Img tImg1 = new Img("icon_calendar.png", 48, 48);
//        tTab1.setPane(tImg1);
//
//        Tab tTab2 = new Tab("T2", "icon_calendar.png");
//        Img tImg2 = new Img("icon_calendar.png", 48, 48);
//        tTab2.setPane(tImg2);
//
//        tabSet.addTab(tTab1);
//        tabSet.addTab(tTab2);
//
//        tabSet.addDropHandler(new DropHandler() {
//
//            public void onDrop(DropEvent event) {
//
//                SC.say("Droped: " + EventHandler.getDragTarget().toString());
//            }
//        });
//    }

