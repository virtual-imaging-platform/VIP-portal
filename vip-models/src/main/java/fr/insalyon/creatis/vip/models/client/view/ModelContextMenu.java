/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;

import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIConstants;
import fr.insalyon.creatis.vip.simulationgui.client.view.SimulationGUITab;
/**
 *
 * @author glatard
 */
class ModelContextMenu extends Menu {

    private ModalWindow modal;
    private String modelURI;
    private String modelName;

    public ModelContextMenu(ModalWindow modal, String uri,  String title, boolean bdelete, final boolean test) {
        this.modal = modal;
        this.modelURI = uri;
        this.modelName = title;
    
        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem deleteItem = new MenuItem("Delete model");
        deleteItem.setIcon(ApplicationConstants.ICON_KILL);
        deleteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to delete model "
                        + modelName+"?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            deleteModel(test);
                        }
                    }
                });
            }
        });
        
        
        MenuItem viewItem = new MenuItem("View model annotations");
        viewItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        viewItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelDisplayTab(modelURI,modelName,test));
            }
        });
        
        MenuItem modifyItem = new MenuItem("Modify model");
        modifyItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        modifyItem.setEnabled(true);
        modifyItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                     Layout.getInstance().addTab(new ModelImportTab(false, modelName, modelURI,test));
            }
        });
        
        MenuItem SimulationItem = new MenuItem("Launch simulation GUI");
        SimulationItem.setIcon(SimulationGUIConstants.APP_IMG_EDITOR);
        SimulationItem.setEnabled(true);
        SimulationItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                      Layout.getInstance().addTab(new SimulationGUITab(modelURI.toString(), null,test));
            }
        });      
        
        MenuItem downloadItem = new MenuItem("Download model");
        downloadItem.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadItem.setEnabled(true);
        downloadItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
               downloadModel(modelURI,test);
            }
        });
        
        if(bdelete)
            this.setItems(viewItem, deleteItem, SimulationItem,downloadItem);
        else
            this.setItems(viewItem, SimulationItem, downloadItem);
    }
  
    private void downloadModel(String modelURI, boolean test){
   
        final ModelServiceAsync ms = ModelService.Util.getInstance();
        AsyncCallback<String> cb1 = new AsyncCallback<String>(){

            public void onFailure(Throwable caught) {
            Layout.getInstance().setWarningMessage("Cannot get model storage URL");
            }

            public void onSuccess(String modelURL) {
                  Layout.getInstance().setNoticeMessage("Added model to the transfer pool");
                    
                    ModelDisplay.downloadModel(modelURL);
        };
        };
        ms.getStorageURL(modelURI, test, cb1);  
        
       
    
    }
    
    private void deleteModel(final boolean test) {
        final ModelServiceAsync ms = ModelService.Util.getInstance();
        AsyncCallback<String> cb1 = new AsyncCallback<String>(){

            public void onFailure(Throwable caught) {
            Layout.getInstance().setWarningMessage("Cannot get model storage URL");
            }

            public void onSuccess(String modelURL) {
                DataManagerServiceAsync dm = DataManagerService.Util.getInstance();
                AsyncCallback<Void> cb = new AsyncCallback<Void>() {

                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Cannot delete model files.");
                    }

                    public void onSuccess(Void result) {
                       Layout.getInstance().setNoticeMessage("Deleted model files.");
                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                Layout.getInstance().setWarningMessage("Cannot delete model annotations (" + caught.getMessage() + ")");
                            }

                            public void onSuccess(Void result) {
                                Layout.getInstance().setNoticeMessage("Deleted model annotations.");
                                ModelListTab modelsTab = (ModelListTab) Layout.getInstance().getTab("model-browse-tab");
                                if (modelsTab != null) {
                                    modelsTab.loadModels();
                                }
                            }
                        };

                        ms.deleteModel(modelURI, test, callback);
                    }
                };
                dm.delete(modelURL, cb);
            }
        };
        ms.getStorageURL(modelURI, test, cb1);  
        
       
    }
}
