/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.models.client.ModelConstants;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;

/**
 *
 * @author glatard
 */
class ModelTab extends Tab {

    protected ModalWindow modal;
    protected VLayout layout;
    protected SimulationObjectModel model = null;

    public ModelTab(final String uri) {

        this.setTitle("Model");
        this.setID(uri);
        this.setCanClose(true);

        layout = new VLayout();
        modal = new ModalWindow(layout);



        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.say("Cannot load model ("+uri+")");
            }

            public void onSuccess(SimulationObjectModel result) {
                modal.hide();
                if (result != null) {   
                    layout.addMember(new ModelDisplay(result));
                    model = result;
                  
                    Button download = new Button("Download");
                    download.setIcon("icon-download.png");
                    download.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            String lfn = model.getStorageURL();
                            downloadModel(lfn);
                        }
                    });
                    if (model.getStorageURL() != null) {
                        layout.addMember(download);
                    } else {
                        Label label = new Label();
                        label.setAlign(Alignment.LEFT);
                        label.setValign(VerticalAlignment.CENTER);
                        label.setWrap(false);
                        label.setShowEdges(false);
                        label.setContents("No file is available for this model (it may be a fake model).");
                        layout.addMember(label);
                    }
                } else {
                    SC.say("Cannot load model");
                }
            }
        };
        modal.show("Loading model", true);
        ms.rebuildObjectModelFromTripleStore(uri, callback);
     
       
            
        
        this.setPane(layout);

    }
    
     private void downloadModel(String lfnModel) {
        TransferPoolServiceAsync tps = new TransferPoolService.Util().getInstance();
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Cannot download model file.");
            }

            public void onSuccess(Void result) {
                SC.say("Model file download is in progress.");
                DataManagerSection.getInstance().setExpanded(true);

            }
        };
        tps.downloadFile(Context.getInstance().getUser(), lfnModel, Context.getInstance().getUserDN(), Context.getInstance().getProxyFileName(), callback);

    }
}
