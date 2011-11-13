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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelUtil;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.models.client.ModelConstants;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;

/**
 *
 * @author Tristan Glatard
 */
class ModelDisplayTab extends Tab {

    protected ModalWindow modal;
    protected VLayout layout;
    protected SimulationObjectModel model = null;
    protected ToolStrip toolStrip;
    private ToolStripButton download;

    public ModelDisplayTab(final String uri, String title) {

        this.setTitle(title);
        this.setID(uri);
        this.setCanClose(true);

        layout = new VLayout();
        modal = new ModalWindow(layout);

        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        layout.addMember(toolStrip);

        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.say("Cannot load model (" + uri + ")");
            }

            public void onSuccess(SimulationObjectModel result) {
                modal.hide();
                if (result != null) {
                    layout.addMember(new ModelTreeGrid(result));
                    model = result;
                    download = new ToolStripButton("Download");
                    download.setIcon(DataManagerConstants.ICON_DOWNLOAD);
                    download.setTooltip("Download model files");
                    toolStrip.addButton(download);
                    checkModel(model);
                } else {
                    SC.say("Cannot load model");
                }
            }
        };
        modal.show("Loading model", true);
        ms.rebuildObjectModelFromTripleStore(uri, callback);

        this.setPane(layout);
    }

    private void checkModel(final SimulationObjectModel model) {
        final String lfn = model.getStorageURL();
        DataManagerServiceAsync service = DataManagerService.Util.getInstance();

        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                disableDownload(lfn);
            }

            public void onSuccess(Boolean result) {
                modal.hide();
                download.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                        String lfn = model.getStorageURL();

                        downloadModel(lfn);
                    }
                });
                if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.IRM)) {
                    toolStrip.addMember(testModality("MRI", true));
                } else {
                    toolStrip.addMember(testModality("MRI", false));
                }
                if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.CT)) {
                    toolStrip.addMember(testModality("CT", true));
                } else {
                    toolStrip.addMember(testModality("CT", false));
                }
                if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.PET)) {
                    toolStrip.addMember(testModality("PET", true));
                } else {
                    toolStrip.addMember(testModality("PET", false));
                }
                if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.US)) {
                    toolStrip.addMember(testModality("Ultrasound", true));
                } else {
                    toolStrip.addMember(testModality("Ultrasound", false));
                }
            }
        };
        if (model.getStorageURL() != null) {
            modal.show("Checking if model files exist", true);
            service.exists(lfn, callback);
        } else {
            disableDownload("No storage URL");
        }
    }

    private void disableDownload(String lfn) {
        download.setIcon(ModelConstants.APP_IMG_KO);
        download.setTooltip("Cannot find model files (" + lfn + ")");
    }

    private void downloadModel(final String lfnModel) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot download model file.");
            }

            public void onSuccess(Void result) {
                SC.say("Model file download is in progress.");
                ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
            }
        };
        service.downloadFile(lfnModel, callback);
    }

    private ToolStripButton testModality(String caption, boolean test) {
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
}
