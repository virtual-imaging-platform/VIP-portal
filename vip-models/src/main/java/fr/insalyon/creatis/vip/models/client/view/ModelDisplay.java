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
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.DateChooser;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelUtil;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.Timepoint;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import fr.insalyon.creatis.vip.models.client.ModelConstants;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class ModelDisplay extends VLayout {

    protected ModalWindow modal = new ModalWindow(this);
    protected SimulationObjectModel model = null;
    protected ToolStrip toolStrip;
    private ToolStripButton download, upload;
    private ModelTreeGrid modelTreeGrid;
    private ModelServiceAsync ms = null;
    private String zipFile;
    private int selectTP = -1;
    private boolean bmodif = false;
    private ArrayList<String> addFiles = new ArrayList<String>();

    ModelDisplay(String uri) {
        super();
        buildModel(uri);
    }

    ModelDisplay(SimulationObjectModel result, boolean bfull) {
        super();
        this.model = result;
        ms = ModelService.Util.getInstance();
        init(bfull);
    }

    public ModelServiceAsync getService() {
        return ms;
    }

    public void setZipFile(String z) {
        zipFile = z;
    }

    private void init(boolean bfull) {

        toolStrip = new ToolStrip();
        toolStrip.setWidth100();



        addMember(toolStrip);
        if (bfull) {
            updateTreeGrid();
        } else {
            createTreeGrid();
        }

        if (model != null) {
            enableDownload();
            checkModel(model);
        }
        toolStrip.addSeparator();
        ToolStripButton addTimepoint = new ToolStripButton("Add timepoint");
        addTimepoint.setIcon(ModelConstants.APP_IMG_TIMEPOINT);
        addTimepoint.setTooltip("It adds a TimePoint to current model");


        addTimepoint.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addTimePoint(new Date(System.currentTimeMillis()));

            }
        });

        toolStrip.addButton(addTimepoint);


        ToolStripButton addInstant = new ToolStripButton("Add instant");
        addInstant.setIcon(ModelConstants.APP_IMG_INSTANT);
        addInstant.setTooltip("It adds a Instant to current model");
        addInstant.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addInstant();
            }
        });
        toolStrip.addButton(addInstant);
        addMember(toolStrip);

    }

    private void addTimePoint(Date d) {
           modelTreeGrid.addTimePoint(new Date(System.currentTimeMillis()), 0);
    }

    private void addInstant() {
        modelTreeGrid.addInstant();
       bmodif = true;
    }
    // still needed????

    public void addObject(String ontoName, List<String> ontoObj, int type) {
//         ModelServiceAsync ms = ModelService.Util.getInstance();
//          final AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {
//
//            public void onFailure(Throwable caught) {
//                modal.hide();
//                SC.warn("problem to add file");
//            }
//
//            public void onSuccess(SimulationObjectModel result) {
//             
//                updateTreeGrid();
//                 modelTreeGrid.refreshFields();
//                 modelTreeGrid.refreshModel(model);
//                  bmodif = true;
//            }
//        };
// 
//        ms.addObject(model, ontoName, ontoObj, modelTreeGrid.getTimePoint(),
//                modelTreeGrid.getInstant(), type, selectTP, callback);
    }

    public void setModif(boolean value) {
        bmodif = value;
    }

    private void updateTreeModel() {
        this.removeMember(modelTreeGrid);
        modelTreeGrid = new ModelTreeGrid(model, true);
        addMember(modelTreeGrid);
        modelTreeGrid.refreshFields();
    }

    public ModelTreeGrid getModelTree() {
        return modelTreeGrid;
    }

    public void disableCommit() {
        if (upload != null) {
            upload.hide();
        }
    }

    private void updateTreeGrid() {
//        if(model != null && this.contains(modelTreeGrid).booleanValue())
//        {   modal.hide();
//            SC.warn("nothing to do here");
//            this.removeMember(modelTreeGrid);
//        }

        modelTreeGrid = new ModelTreeGrid(model, true);
        addMember(modelTreeGrid);
        modelTreeGrid.setParentElement(this);

    }

    public void createTreeGrid() {
        modelTreeGrid = new ModelTreeGrid(model, false);
            addMember(modelTreeGrid);
            modelTreeGrid.setParentElement(this);
        addTimePoint(new Date(System.currentTimeMillis()));
        addInstant();
        modelTreeGrid.refreshFields();
        modelTreeGrid.refreshModel(model);
        bmodif = true;

    }

    public void enableCommit() {
        upload = new ToolStripButton("Commit model");
        upload.setIcon(DataManagerConstants.ICON_UPLOAD);
        upload.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (zipFile == null) {
                    SC.warn("No zip file");//TODO: build it
                    return;
                }

                if (model == null) {
                    SC.warn("No simulation object model to commit.");
                    return;
                }
                ////TODO put annotations in ZIP file in case they were modifited
                if (bmodif) {
                    addDatatoZip();
                }
                modal.show("Uploading " + zipFile, true);
                final String lfn = uploadModel(zipFile);

                modal.show("Committing annotations to the Triple Store", true);
                //commit rdf annotations

                ModelServiceAsync ssu = ModelService.Util.getInstance();
                AsyncCallback<SimulationObjectModel> cbssu = new AsyncCallback<SimulationObjectModel>() {

                    public void onFailure(Throwable caught) {
                        SC.warn("Cannot set the model storage URL");
                    }

                    public void onSuccess(SimulationObjectModel result) {
                        final String uri = result.getURI();
                        ModelServiceAsync mms = ModelService.Util.getInstance();
                        AsyncCallback<Void> callback1 = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                modal.hide();
                                SC.warn("Cannot commit model to the Triple Store");
                            }

                            @Override
                            public void onSuccess(Void result) {
                                modal.hide();
                                SC.say("Model successfully comitted to the Triple Store (" + uri + ")");
                                ModelListTab modelsTab = (ModelListTab) Layout.getInstance().getTab("model-browse-tab");
                                if (modelsTab != null) {
                                    modelsTab.loadModels();
                                }
                            }
                        };
                        mms.completeModel(result, callback1);
                    }
                };
                ssu.setStorageUrl(model, lfn, cbssu);
            }
        });
        upload.setTooltip("Commit model to the repository");
        toolStrip.addButton(upload);

    }

    private void addDatatoZip() {
        //uploading zip file
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot added files to  zip");
            }

            public void onSuccess(Void result) {
            }
        };
        ms.recordAddeddFiles(zipFile, addFiles, model, callback);
    }

    private void enableDownload() {
        download = new ToolStripButton("Download");
        download.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        download.setTooltip("Download model files");
        toolStrip.addButton(download);
        download.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String lfn = model.getStorageURL();
                downloadModel(lfn);
            }
        });
    }

    private String uploadModel(String file) {

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
        String remoteDir = ModelConstants.MODEL_HOME;
        //TODO: check if this exists
        String remoteName = getTimeStampMilli() + "-" + file;
        String localName = file;
        service.uploadFile(localName, remoteName, remoteDir, callback);
        upload.disable();
        return remoteDir + "/" + remoteName;
    }

    private long getTimeStampMilli() {
        Date date = new Date();
        return date.getTime();
    }

    private void disableDownload(String lfn, boolean hide) {
        if (hide) {
            download.hide();
        }
        download.setIcon(ModelConstants.APP_IMG_KO);
        download.setTooltip("Cannot find model files (" + lfn + ")");
    }

    public void checkModel(final SimulationObjectModel model) {
        final String lfn = model.getStorageURL();
        DataManagerServiceAsync service = DataManagerService.Util.getInstance();

        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                disableDownload(lfn, false);
            }

            public void onSuccess(Boolean result) {
                modal.hide();
            }
        };
        if (model.getStorageURL() != null) {

            modal.show("Checking if model files exist", true);
            service.exists(lfn, callback);
        } else {
            disableDownload("No storage URL", true);
        }

        if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.IRM)) {
            toolStrip.addMember(modalityButton("MRI", true));
        } else {
            toolStrip.addMember(modalityButton("MRI", false));
        }
        if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.CT)) {
            toolStrip.addMember(modalityButton("CT", true));
        } else {
            toolStrip.addMember(modalityButton("CT", false));
        }
        if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.PET)) {
            toolStrip.addMember(modalityButton("PET", true));
        } else {
            toolStrip.addMember(modalityButton("PET", false));
        }
        if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.US)) {
            toolStrip.addMember(modalityButton("Ultrasound", true));
        } else {
            toolStrip.addMember(modalityButton("Ultrasound", false));
        }
    }

    private void downloadModel(final String lfnModel) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot download model file.");
            }

            public void onSuccess(String result) {
                SC.say("Model file download is in progress.");
                ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
            }
        };
        service.downloadFile(lfnModel, callback);
    }

    private ToolStripButton modalityButton(String caption, boolean test) {
        ToolStripButton ok = new ToolStripButton(caption);
        if (test) {
            ok.setIcon(ModelConstants.APP_IMG_OK);
            ok.setTooltip("This model can be used in a simulation of this modality.");
        } else {
            ok.setTooltip("This model lacks physical parameters to be used in a simulation of this modality.");
            ok.setIcon(ModelConstants.APP_IMG_KO);
        }
        return ok;
    }

    private void buildModel(final String uri) {
        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                init(false);
                SC.say("Cannot load model (" + uri + ")");
            }

            public void onSuccess(SimulationObjectModel result) {
                modal.hide();
                if (result != null) {
                    model = result;
                    //SC.say("Model loaded.");
                    init(true);
                } else {
                    SC.say("Cannot load model");
                }
            }
        };
        modal.show("Loading model", true);
        ms.rebuildObjectModelFromTripleStore(uri, callback);
    }
}
