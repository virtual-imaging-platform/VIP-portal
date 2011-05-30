/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
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

    public ModelTab(String uri) {

        this.setTitle("Model");
        this.setID(uri);
        this.setCanClose(true);

        layout = new VLayout();
        modal = new ModalWindow(layout);



        ModelServiceAsync ms = ModelService.Util.getInstance();
        final AsyncCallback<SimulationObjectModel> callback = new AsyncCallback<SimulationObjectModel>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.say("Cannot load model");
            }

            public void onSuccess(SimulationObjectModel result) {
                modal.hide();
                if (result != null) {
                    SC.say("Model successfully retrieved. It has " + result.getTimepoints().size() + " timepoint(s)");
                    layout.addMember(new ModelDisplay(result));
                    model = result;
                } else {
                    SC.say("Cannot load model");
                }
            }
        };
        modal.show("Loading model", true);
        ms.rebuildObjectModelFromTripleStore(uri, callback);

        this.setPane(layout);

    }
}
