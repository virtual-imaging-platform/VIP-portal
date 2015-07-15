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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIConstants;
import fr.insalyon.creatis.vip.simulationgui.client.view.SimulationGUITab;

/**
 *
 * @author Tristan Glatard
 */
class ModelContextMenu extends Menu {

    private String modelURI;
    private String modelName;
    private String modelSURL;

    public ModelContextMenu(ModalWindow modal, String uri, String title, String surl, boolean bdelete, final boolean test) {

        this.modelURI = uri;
        this.modelName = title;
        this.modelSURL = surl;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem deleteItem = new MenuItem("Delete model");
        deleteItem.setIcon(ApplicationConstants.ICON_KILL);
        deleteItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.ask("Do you really want to delete model " + modelName + "?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            deleteModel(test);
                        }
                    }
                });
            }
        });

        MenuItem viewItem = new MenuItem("View model annotations");
        viewItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        viewItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelDisplayTab(modelURI, modelName, test, false));
            }
        });

        MenuItem modifyItem = new MenuItem("Modify model");
        modifyItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        modifyItem.setEnabled(true);
        modifyItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelImportTab(false, modelName, modelURI, test));
            }
        });

        MenuItem SimulationItem = new MenuItem("Launch simulation from model");
        SimulationItem.setIcon(SimulationGUIConstants.APP_IMG_EDITOR);
        SimulationItem.setEnabled(true);
        SimulationItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new SimulationGUITab(modelURI.toString(), modelName, modelSURL, null, test));
            }
        });

        MenuItem downloadItem = new MenuItem("Download model");
        downloadItem.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadItem.setEnabled(true);
        downloadItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                downloadModel(modelURI, test);
            }
        });

        if (bdelete) {
            this.setItems(viewItem, modifyItem, deleteItem, SimulationItem, downloadItem);
        } else {
            this.setItems(viewItem, SimulationItem, downloadItem);
        }
    }

    private void downloadModel(String modelURI, boolean test) {

        final ModelServiceAsync ms = ModelService.Util.getInstance();
        AsyncCallback<String> cb1 = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot get model storage URL");
            }

            @Override
            public void onSuccess(String modelURL) {
                Layout.getInstance().setNoticeMessage("Added model to the transfer pool");

                ModelDisplay.downloadModel(modelURL);
            };
        };
        ms.getStorageURL(modelURI, test, cb1);
    }

    private void deleteModel(final boolean test) {

        AsyncCallback<String> cb1 = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot get model storage URL");
            }

            @Override
            public void onSuccess(String modelURL) {

                AsyncCallback<Void> cb = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Cannot delete model files.");
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Layout.getInstance().setNoticeMessage("Deleted model files.");
                    }
                };
                DataManagerService.Util.getInstance().delete(modelURL, cb);
            }
        };
        ModelService.Util.getInstance().getStorageURL(modelURI, test, cb1);

        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Cannot delete model annotations (" + caught.getMessage() + ")");
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("Deleted model annotations.");
                ModelListTab modelsTab = (ModelListTab) Layout.getInstance().getTab("model-browse-tab");
                if (modelsTab != null) {
                    modelsTab.loadModels();
                }
            }
        };
        ModelService.Util.getInstance().deleteModel(modelURI, test, callback);
    }
}
