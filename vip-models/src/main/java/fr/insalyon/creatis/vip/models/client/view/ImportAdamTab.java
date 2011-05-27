/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;

/**
 *
 * @author glatard
 */
public class ImportAdamTab extends Tab {
    
    private static ImportAdamTab instance = null;
    
    protected ModalWindow modal;
    protected  VLayout layout;
    protected SimulationObjectModel model = null;
    
    public static ImportAdamTab getInstance(){
        if(instance == null){
            instance = new ImportAdamTab();
        }
        return instance;
    }
   
    private ImportAdamTab(){
        this.setTitle("Adam");
        this.setID("adam-tab");
        this.setCanClose(true);
        
        layout = new VLayout();
        modal = new ModalWindow(layout);
        
        
      
        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {
            
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.say("Cannot load ADAM");
            }
            
            public void onSuccess(SimulationObjectModel result) {
                modal.hide();
                if(result!= null){
                SC.say("ADAM successfully retrieved. It has "+result.getTimepoints().size()+" timepoints");
                layout.addMember(new ModelDisplay(result));
                model = result;
                }
                else
                    SC.say("Cannot load ADAM");
            }
        };
        modal.show("Loading ADAM", true);
        ms.getADAM(callback);
        
        Button importAdam = new Button();
        importAdam.setTitle("Import");
        importAdam.setIcon("icon-adam.png");
        
        importAdam.addClickHandler(new ClickHandler() {
            private AsyncCallback<Void> callback1 = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Cannot import model to repository");
                }

                public void onSuccess(Void result) {
                    modal.hide();
                    SC.say("Model successfully imported.");
                }
            };

            public void onClick(ClickEvent event) {
                ModelServiceAsync ms = ModelService.Util.getInstance();
                modal.show("Importing model",true);
                ms.completeModel(model, callback1);
            }
        });
        layout.addMember(importAdam);
        this.setPane(layout);
    
    }
 
}
