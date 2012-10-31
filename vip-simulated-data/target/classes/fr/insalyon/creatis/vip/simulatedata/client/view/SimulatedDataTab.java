/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataConstants;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataRecord;
import fr.insalyon.creatis.vip.simulatedata.client.bean.SimulatedData;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataService;
import fr.insalyon.creatis.vip.simulatedata.client.rpc.SimulatedDataServiceAsync;
import java.util.List;

/**
 *
 * @author glatard
 */
public class SimulatedDataTab extends Tab {

    private VLayout vLayout;
    protected ModalWindow modal;
    SimulatedDataGrid pet, us, ct, mri;

    public SimulatedDataTab() {
        
        this.setTitle(Canvas.imgHTML(SimulatedDataConstants.ICON_SD) + " " + SimulatedDataConstants.APP_SD);
        this.setID(SimulatedDataConstants.TAB_SD);
        this.setCanClose(true);

        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);

        this.setPane(vLayout);

        modal = new ModalWindow(vLayout);
        
        pet = new SimulatedDataGrid("PET");
        us = new SimulatedDataGrid("Ultrasound");
        ct = new SimulatedDataGrid("CT");
        mri = new SimulatedDataGrid("MRI");

        vLayout.addMember(pet);
        vLayout.addMember(us);
        vLayout.addMember(ct);
        vLayout.addMember(mri);

        loadData();


    }

    private void loadData() {
        modal.show("Loading data...", true);

        SimulatedDataServiceAsync service = SimulatedDataService.Util.getInstance();
        AsyncCallback<List<SimulatedData>> callback = new AsyncCallback<List<SimulatedData>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get simulated data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<SimulatedData> result) {
                modal.hide();

                pet.loadData(result);
                us.loadData(result);
                ct.loadData(result);
                mri.loadData(result);
            }
        };
        service.getSimulatedData(callback);
    }
}
