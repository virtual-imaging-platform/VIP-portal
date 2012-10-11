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
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;

import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
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
import fr.insalyon.creatis.vip.models.client.view.FileTree.FileTreeNode;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

//import com.allen_sauer.gwt.dnd.client.*;
/**
 *
 * @author Tristan Glatard
 */
class ModelImportTab extends Tab {

    //private final ToolStrip toolStrip; 
    protected ModalWindow modal;
    private VLayout vl;
    private VLayout files;
    private HLayout hl;
    private String zipFile;
    private Label label;
    private SimulationObjectModel simulationObjectModel = null;
    private ModelDisplay modelDisplay = null;
    private FileTree fileTree = null;
    //private PickupDragController dragController;
    private HStack grids = null;
    private String dwnmodel;
    private String zipFullPath = "";
    private boolean mbUpload = true;

    public ModelImportTab(boolean bTS, String nameTab, String modelURI) {


        this.setTitle(nameTab);
        //this.setID();
        this.setCanClose(true);
        initComplete(this);

        hl = new HLayout();

        vl = new VLayout();
        files = new VLayout();
        modal = new ModalWindow(hl);
        label = new Label();

        ToolStrip ts = new ToolStrip();
        ts.setWidth100();


        // ToolStripButton addButton = new ToolStripButton("Add file");
        addFileButton addButton = new addFileButton("Add file");
        addButton.setIcon(CoreConstants.ICON_ADD);
        addButton.setTooltip("It adds a File to complete the current model");
        addButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                new FileUploadWindow("local", "addFileComplete").show();

            }
        });

        ts.addButton(addButton);
        vl.addMember(ts);

        VLayout ml = new VLayout();
        ml.setAlign(Alignment.CENTER);

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
        vl.addMember(label);

        VStack moveControls = new VStack(10);
        moveControls.setWidth(32);
        moveControls.setHeight(74);
        moveControls.setLayoutAlign(Alignment.CENTER);

        TransferImgButton rightArrow = new TransferImgButton(TransferImgButton.RIGHT, new ClickHandler() {

            public void onClick(ClickEvent event) {

                FolderDropEvent event3 = new FolderDropEvent(fileTree.getSelectedRecord().getJsObj());
                SC.say(event3.toString());
                modelDisplay.getModelTree().fireEvent(event3);
            }
        });

        rightArrow.setTooltip("drop file to IAMF model");

        moveControls.addMember(rightArrow);

        hl.addMember(vl);
        ml.addMember(moveControls);
        hl.addMember(ml);


        this.setPane(hl);

        if (bTS) {
            if(!nameTab.isEmpty() || !modelURI.isEmpty())
            {
                new FileUploadWindow("local", "uploadComplete").show();
            }
            else
            {
                ArrayList<String> empty = new ArrayList<String>();
                displayFiles(null, empty );
            }
                
        } else {
            ModelServiceAsync msa = ModelService.Util.getInstance();
            AsyncCallback<String> mscallback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Cannot identify model URL");
                }

                public void onSuccess(String result) {
                    zipFullPath = result;
                    mbUpload = false;
                    dwnmodel = result.substring(result.lastIndexOf("/") + 1, result.length());
                    DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                    AsyncCallback<String> callback = new AsyncCallback<String>() {

                        public void onFailure(Throwable caught) {
                            SC.warn("Cannot modify model file.");
                        }

                        public void onSuccess(String result2) {
                            SC.warn(dwnmodel);
                            setZipFile(dwnmodel, zipFullPath, false);
                        }
                    };
                    service.downloadFile(result, callback);
                }
            };
            msa.getURLFromURI(modelURI, mscallback);
        }


    }

    public void uploadComplete(String fileName) {
        setZipFile(fileName,"", true);
    
    }

    private native void initComplete(ModelImportTab upload) /*-{
    
    $wnd.uploadComplete = function (fileName) {
    upload.@fr.insalyon.creatis.vip.models.client.view.ModelImportTab::uploadComplete(Ljava/lang/String;)(fileName);
    };
    $wnd.addFileComplete = function (fileName) {
    upload.@fr.insalyon.creatis.vip.models.client.view.ModelImportTab::addFileComplete(Ljava/lang/String;)(fileName);
     };
    }-*/;


    public void addFileComplete(String filename) {
        modelDisplay.addFile(filename);
        fileTree.addData(fileTree.createNode("none", filename));
    }

    private void setZipFile(final String zipName, String modelFullName, boolean bUpload) {
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
        ms.getFiles(zipName,modelFullName, bUpload, callback);
    }

    public void displayFiles(String zipName, List<String> result) {

        // window.addItem(fileTreeGrid);
        fileTree = new FileTree(zipFile, result);



        fileTree.addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
                FileTreeNode node = (FileTreeNode) fileTree.getRecord(fileTree.getEventRow());
                modelDisplay.getModelTree().setObjName(node.getAttribute("filename"));
            }
        });

       files.addMember(fileTree);
        final String rdf = fileTree.getRdfFile();
        if (rdf != null) {
            label.setContents("(Initialized annotations from file: " + rdf.substring(rdf.lastIndexOf('/') + 1) + ")");

            //building object model
            modal.show("Building simulation object model.", true);
            ModelServiceAsync ms = ModelService.Util.getInstance();
            AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Couldn't build a simulation object model from file " + rdf.substring(rdf.lastIndexOf('/') + 1) + ". You will have to annotate the model yourself.");
                    // Create an empty model
                    setEmptyModel();
                }

                public void onSuccess(SimulationObjectModel result) {
                    setSimulationObjectModel(result);
                    fileTree.setModelDisplay(modelDisplay);
                }
            };
            ms.rebuildObjectModelFromAnnotationFile(rdf, callback);

        } else {
            SC.say("Couldn't find any annotation file in this archive. You will have to annotate the model yourself.");
            setEmptyModel();
             
        }

    }

    public void setSimulationObjectModel(SimulationObjectModel result) {

        modelDisplay = new ModelDisplay(result, true);
        modelDisplay.setZipFile(zipFile, zipFullPath, mbUpload);
        modelDisplay.enableCommit();
        hl.addMember(modelDisplay);

    }

    public void setEmptyModel() {

        ModelServiceAsync ms = ModelService.Util.getInstance();
        AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Cant create an empty model. Please contact administrator");
            }

            public void onSuccess(SimulationObjectModel result) {
                setCreateObjectModel(result);
            }
        };
        ms.createModel("EmptyModel"+ String.valueOf((new Date()).getTime()),"cervenansky",callback);
    }

    public void setCreateObjectModel(SimulationObjectModel result) {
        modelDisplay = new ModelDisplay(result, true);
         modelDisplay.setZipFile(zipFile, zipFullPath, mbUpload);
        hl.addMember(modelDisplay);
        modelDisplay.enableCommit();
    }

    public class addFileButton extends ToolStripButton {

        private ModelImportTab tab = null;

        public addFileButton(String name) {
            this.setTitle(name);
            this.setIcon(CoreConstants.ICON_ADD);
            this.setTooltip("It adds a file to complete the current model");
            this.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
         
                }
            });
        }

        public void setTab(ModelImportTab modeltab) {
            tab = modeltab;
        }
    }
}
