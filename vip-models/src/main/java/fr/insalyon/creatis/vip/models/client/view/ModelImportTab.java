/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.NamedFrame;
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
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;

import java.util.List;

/**
 *
 * @author glatard
 */
class ModelImportTab extends Tab {
    //private final ToolStrip toolStrip;

    private static ModelImportTab instance = null;

    protected ModalWindow modal;    
    private ModelDisplay modelDisplay;
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
        initComplete(this);
        fileVLayout = new VLayout();
        modal = new ModalWindow(fileVLayout);
       
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
                modal.hide();
                SC.warn("Cannot initialize the model");
            }
            public void onSuccess(SimulationObjectModel result) {
               //TODO display model
                modal.hide();
                SC.say("Model initialized");
            }
        };
        modal.show("Initializing the model",true);
        ms.createModel("New model", callback);

        HStack hs = new HStack(10);
        hs.addMember(fileVLayout);
        hs.addMember(modelDisplay);

        NamedFrame frame = new NamedFrame("uploadTarget");
        frame.setVisible(false);
        frame.setHeight("1px");
        frame.setWidth("1px");
     
        
        VLayout vl = new VLayout();
        vl.addMember(frame);
        vl.addMember(addButton);
        vl.addMember(hs);

        this.setPane(vl);
    }

     public void uploadComplete(String fileName) {
        //modal.hide();
//        OperationLayout.getInstance().loadData();
        addFile(fileName);
    }

    private native void initComplete(ModelImportTab upload) /*-{
    $wnd.uploadComplete = function (fileName) {
    upload.@fr.insalyon.creatis.vip.models.client.view.ModelImportTab::uploadComplete(Ljava/lang/String;)(fileName);
    };
    }-*/;
    
    void addFile(final String zipName) {
        //should be put in uploadComplete
        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error processing file");
            }

            public void onSuccess(List<String> result) {
                String message = "OK: ";
                for (String s : result) {
                    message += " - " + s;
                }
                modal.hide();
                SC.say("Retrieved zip file content");
                addFileBox(zipName, result);

            }
        };
        modal.show("Processing zip file",true);
        ms.getFiles(zipName, callback);
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

    public class FileTreeNode extends TreeNode {

        public FileTreeNode(String zipFileName, String fileName) {
            setAttribute("ArchiveName", zipFileName);
            setAttribute("FileName", fileName);
            setAttribute("isOpen", true);

            setAttribute("description", getFileDescription(zipFileName));

            //extract file
        }
    }

    public String getFileDescription(String fileName) {
        String description = "unrecognized file";
        String ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length() - 1);
        if (ext.equals("vtu")) {
            description = "US scatterers";
        }
        return description;

    }

   public void resetTab() {

        fileVLayout.clear();
    }
      
   
}


