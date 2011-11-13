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
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;

/**
 *
 * @author glatard
 */
class ModelContextMenu extends Menu {

    private ModalWindow modal;
    private String modelURI;
    private String modelName;

    public ModelContextMenu(ModalWindow modal, String uri,  String title) {
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
                SC.confirm("Do you really want to delete this model ("
                        + modelName +","+modelURI+ ")?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            deleteModel();
                        }
                    }
                });
            }
        });
        
        
        MenuItem viewItem = new MenuItem("View model");
        viewItem.setIcon(ApplicationConstants.ICON_SIMULATION_VIEW);
        viewItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelDisplayTab(modelURI,modelName));
            }
        });
        this.setItems(viewItem, deleteItem);
    }

    private void deleteModel() {
        final ModelServiceAsync ms = ModelService.Util.getInstance();
        AsyncCallback<String> cb1 = new AsyncCallback<String>(){

            public void onFailure(Throwable caught) {
            SC.warn("Cannot get model storage URL");
            }

            public void onSuccess(String modelURL) {
                DataManagerServiceAsync dm = DataManagerService.Util.getInstance();
                AsyncCallback<Void> cb = new AsyncCallback<Void>() {

                    public void onFailure(Throwable caught) {
                        SC.warn("Cannot delete model files.");
                    }

                    public void onSuccess(Void result) {
                        SC.say("Deleted model files.");
                        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                SC.warn("Cannot delete model annotations (" + caught.getMessage() + ")");
                            }

                            public void onSuccess(Void result) {
                                SC.say("Deleted model annotations.");
                                ModelListTab modelsTab = (ModelListTab) Layout.getInstance().getTab("model-browse-tab");
                                if (modelsTab != null) {
                                    modelsTab.loadModels();
                                }
                            }
                        };

                        ms.deleteModel(modelURI, callback);
                    }
                };
                dm.delete(modelURL, cb);
            }
        };
        ms.getStorageURL(modelURI, cb1);  
        
       
    }
}
