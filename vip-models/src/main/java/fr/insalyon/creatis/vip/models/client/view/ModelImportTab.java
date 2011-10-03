/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
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
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import fr.insalyon.creatis.vip.models.client.ModelConstants;

import java.util.List;

/**
 *
 * @author Tristan Glatard
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
        this.setID(ModelConstants.TAB_MODEL_IMPORT);
        this.setCanClose(true);
        initComplete(this);

        vl = new VLayout();
        files = new VLayout();
        modal = new ModalWindow(vl);
        label = new Label();

        Button upload = new Button("Upload model");
        upload.setIcon(DataManagerConstants.ICON_UPLOAD);
        upload.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (zipFile == null) {
                    SC.warn("No zip file");
                    return;
                }

                if (rdfFile == null) {
                    SC.warn("No annotation file found in zip file");
                    return;
                }

                modal.show("Uploading " + zipFile, true);
                final String lfn = uploadModel(zipFile, "uri");
                modal.show("Committing annotations to the Triple Store", true);
                //commit rdf annotations
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
                                        ModelListTab modelsTab = (ModelListTab) Layout.getInstance().getTab("model-browse-tab");
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
//        modal.hide();
//        OperationLayout.getInstance().loadData();
        setZipFile(fileName);
    }

    private native void initComplete(ModelImportTab upload) /*-{
    $wnd.uploadComplete = function (fileName) {
    upload.@fr.insalyon.creatis.vip.models.client.view.ModelImportTab::uploadComplete(Ljava/lang/String;)(fileName);
    };
    }-*/;

    private void setZipFile(final String zipName) {
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
            String name = result.get(i).substring(result.get(i).lastIndexOf('/') + 1);
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
        label.setContents("(Annotations: " + rdfFile.substring(rdfFile.lastIndexOf('/') + 1) + ")");
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

    private String uploadModel(String file, String URI) {

        //uploading zip file
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot upload zip file");
            }

            public void onSuccess(Void result) {
                SC.say("Added zip file to the upload pool");
                ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
                BrowserLayout.getInstance().loadData(ModelConstants.MODEL_HOME, true);
            }
        };
        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        String lfn = ModelConstants.MODEL_HOME;
        //TODO: check if this exists
        String name = URI + "-" + file;
        service.uploadFile(name, lfn, callback);

        return lfn + "/" + name;
    }
}
