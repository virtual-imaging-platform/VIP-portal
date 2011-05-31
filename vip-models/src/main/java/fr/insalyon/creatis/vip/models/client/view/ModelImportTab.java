/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
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
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.SimulationObjectModelFactory;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import fr.insalyon.creatis.vip.models.client.ModelConstants;

import java.util.List;

/**
 *
 * @author glatard
 */
class ModelImportTab extends Tab {
    //private final ToolStrip toolStrip; 

    protected ModalWindow modal;
    private VLayout vl;
    private VLayout files;
    private String zipFile;
    private Label label;
    private String rdfFile;

    public ModelImportTab() {

        this.setTitle("Import model");
        this.setID("model-import-tab");
        this.setCanClose(true);
        initComplete(this);

        vl = new VLayout();
        files = new VLayout();
        modal = new ModalWindow(vl);
        label = new Label();

        Button upload = new Button("Upload model");
        upload.setIcon("icon-upload.png");
        upload.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {


                if (zipFile == null) {
                    SC.warn("No zip file");
                } else {
                    if (rdfFile == null) {
                        SC.warn("No annotation file found in zip file");
                    } else {
                        modal.show("Uploading " + zipFile, true);
                        final String lfn = uploadModel(zipFile);
                        modal.show("Committing annotations to the Triple Store", true);
//                        //commit rdf annotations
                        ModelServiceAsync ms = ModelService.Util.getInstance();
                        AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

                            public void onFailure(Throwable caught) {
                                modal.hide();
                                SC.warn("Cannot build simulation object model from annotations");
                            }

                            public void onSuccess(SimulationObjectModel result) {
                                ModelServiceAsync ssu = ModelService.Util.getInstance();
                                AsyncCallback<SimulationObjectModel> cbssu = new AsyncCallback<SimulationObjectModel>() {
                                    public void onFailure(Throwable caught) {
                                        SC.warn("Cannot set the model storage URL");
                                    }
                                    public void onSuccess(SimulationObjectModel result) {
                                        ModelServiceAsync mms = ModelService.Util.getInstance();
                                        AsyncCallback<Void> callback1 = new AsyncCallback<Void>() {

                                            public void onFailure(Throwable caught) {
                                                modal.hide();
                                                SC.warn("Cannot commit model to the Triple Store");
                                            }

                                            public void onSuccess(Void result) {
                                                modal.hide();
                                                SC.say("Model successfully comitted to the Triple Store");
                                                ModelBrowseTab modelsTab = (ModelBrowseTab) Layout.getInstance().getTab("model-browse-tab");
                                                if (modelsTab != null) {
                                                    modelsTab.loadModels();
                                                }
                                                OperationLayout.getInstance().loadData();
                                                OperationLayout.getInstance().activateAutoRefresh();
                                            }
                                        };
                                        mms.completeModel(result, callback1);
                                    }
                                };
                                ssu.setStorageUrl(result, lfn, cbssu);
                            }
                        };
                        ms.rebuildObjectModelFromAnnotationFile(rdfFile, callback);
                    }
                }
            }
        });

        NamedFrame frame = new NamedFrame("uploadTarget");
        frame.setVisible(false);
        frame.setHeight("1px");
        frame.setWidth("1px");


        vl.addMember(frame);

        label.setAlign(Alignment.LEFT);
        label.setValign(VerticalAlignment.CENTER);
        label.setWrap(false);
        label.setShowEdges(false);
        label.setContents("");
        vl.addMember(files);
        
        vl.addMember(upload);
        vl.addMember(label);

        this.setPane(vl);

        new FileUploadWindow("local").show();
        
    }

    public void uploadComplete(String fileName) {
        //modal.hide();
//        OperationLayout.getInstance().loadData();
        setZipFile(fileName);
    }

    private native void initComplete(ModelImportTab upload) /*-{
    $wnd.uploadComplete = function (fileName) {
    upload.@fr.insalyon.creatis.vip.models.client.view.ModelImportTab::uploadComplete(Ljava/lang/String;)(fileName);
    };
    }-*/;

    void setZipFile(final String zipName) {
        zipFile = zipName;

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
                displayFiles(zipName, result);

            }
        };
        modal.show("Processing zip file", true);
        ms.getFiles(zipName, callback);
    }

    public void displayFiles(String zipName, List<String> result) {

//        Window window = new Window();
//        window.setTitle(zipName);
//        window.setWidth(260);
//        window.setHeight(300);
//        window.setCanDragReposition(true);
//        window.setCanDragResize(true);

        Tree fileTree = new Tree();
        fileTree.setModelType(TreeModelType.PARENT);
        fileTree.setRootValue(1);
        fileTree.setNameProperty("FileName");
        fileTree.setIdField("FileName");
        fileTree.setParentIdField("ArchiveName");
        fileTree.setOpenProperty("isOpen");
        TreeNode[] fileData = new TreeNode[result.size()];
        for (int i = 0; i < result.size(); i++) {
            String name = result.get(i).substring(result.get(i).lastIndexOf('/')+1);
            if (name.endsWith(".rdf")) {
                rdfFile = result.get(i);
                updateLabel();
            }
            fileData[i] = new FileTreeNode(zipName, name);
        }
        fileTree.setData(fileData);

        TreeGrid fileTreeGrid = new TreeGrid();
        fileTreeGrid = new TreeGrid();
        fileTreeGrid.setWidth(500);
        fileTreeGrid.setHeight(900);
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

        fileTreeGrid.addLeafContextClickHandler(new LeafContextClickHandler() {

            public void onLeafContextClick(LeafContextClickEvent event) {
                SC.say("context click");
            }
        });

        // window.addItem(fileTreeGrid);
        files.addMember(fileTreeGrid);
    }

    private void updateLabel() {
        label.setContents("(Annotations: " + rdfFile.substring(rdfFile.lastIndexOf('/')+1)+")");
    }

    public class FileTreeNode extends TreeNode {

        public FileTreeNode(String zipFileName, String fileName) {
            setAttribute("ArchiveName", zipFileName);
            setAttribute("FileName", fileName);
            setAttribute("isOpen", true);

            //    setAttribute("description", getFileDescription(zipFileName));

            //extract file
        }
    }

    private String uploadModel(String file) {
        //uploading zip file
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot upload zip file");
            }

            public void onSuccess(Void result) {
                SC.say("Added zip file to the upload pool");
                DataManagerSection.getInstance().setExpanded(true);
                BrowserLayout.getInstance().loadData(ModelConstants.MODEL_HOME, true);
            }
        };
        TransferPoolServiceAsync tps = new TransferPoolService.Util().getInstance();
        String lfn = ModelConstants.MODEL_HOME;
        //TODO: check if this exists
        tps.uploadFile(Context.getInstance().getUser(), lfn, file, Context.getInstance().getUserDN(), Context.getInstance().getProxyFileName(), callback);

        return lfn + "/" + file;

    }
}
