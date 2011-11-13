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

    public ModelContextMenu(ModalWindow modal, String uri, String title) {
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
        ModelServiceAsync ms = ModelService.Util.getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>(){

            public void onFailure(Throwable caught) {
                SC.say("Cannot delete model ("+caught.getMessage()+")");
            }

            public void onSuccess(Void result) {
                SC.say("Model deleted");
            }
        };
        
        ms.deleteModel(modelURI, callback);
        ModelListTab modelsTab = (ModelListTab) Layout.getInstance().getTab("model-browse-tab");
        if (modelsTab != null) {
            modelsTab.loadModels();
        }
    }
}
