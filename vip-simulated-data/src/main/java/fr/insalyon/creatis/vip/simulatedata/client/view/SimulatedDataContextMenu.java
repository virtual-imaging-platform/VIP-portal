/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerModule;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataConstants;
import fr.insalyon.creatis.vip.models.client.view.ModelDisplayTab;

/**
 *
 * @author glatard
 */
public class SimulatedDataContextMenu extends Menu{
    private String model;
    private String simulation;

    public SimulatedDataContextMenu(final String modelURI, final String modelName, final String simulation) {
       
        this.model = modelURI;
        this.simulation = simulation;
        
         this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);
        
        MenuItem simuItem = new MenuItem("View simulation");
        simuItem.setIcon(SimulatedDataConstants.ICON_SIMU);
        simuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                 Layout.getInstance().addTab(new SimulationTab(simulation,
                            "Simulation "+simulation, SimulationStatus.Completed));
            }});
       
        
        MenuItem modelItem1 = new MenuItem("View model");
        modelItem1.setIcon(SimulatedDataConstants.ICON_MODEL); 
        modelItem1.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new ModelDisplayTab(model, modelName, true, false));
            }
        });
        
        MenuItemSeparator separator = new MenuItemSeparator();

        if(this.model == null )
            this.setItems(simuItem);
        else
            this.setItems(simuItem,modelItem1);
       // this.setItems(simuItem, separator, fileItem,paramItem,modelItem);
    }
    
    private void downloadFile(String path) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
             //   modal.hide();
                Layout.getInstance().setWarningMessage("Unable to download file:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
               // modal.hide();
                OperationLayout.getInstance().addOperation(result);
                DataManagerModule.dataManagerSection.expand();
            }
        };
      //  modal.show("Adding file to transfer queue...", true);
        service.downloadFile(path, callback);
    }
}
