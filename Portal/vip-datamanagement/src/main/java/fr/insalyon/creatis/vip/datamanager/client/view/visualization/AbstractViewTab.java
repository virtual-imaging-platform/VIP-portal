/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.visualization;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.bean.VisualizationItem;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;

/**
 *
 * @author Tristan Glatard
 */
public abstract class AbstractViewTab extends Tab {

    protected final ModalWindow modal;
    private final String lfn;
    
    public AbstractViewTab(String lfn) {
        String fileName = lfn.substring(lfn.lastIndexOf('/') + 1);
        this.setTitle(fileName);
        this.setCanClose(true);
        this.setPane(new Canvas());
        modal = new ModalWindow(this.getPane());
        this.lfn = lfn;
    }

    public void load(){
        loadLFN(lfn);
    }
    
    private void loadLFN(String lfn) {
        DataManagerServiceAsync dmsa = DataManagerService.Util.getInstance();
        modal.show("Loading data file...", true);
        dmsa.getVisualizationItemFromLFN(lfn, new AsyncCallback<VisualizationItem>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Cannot load file: " + caught.getMessage());
            }

            @Override
            public void onSuccess(VisualizationItem result) {
                modal.hide();
                displayFile(result);
            }
        });
    }

    public abstract void displayFile(VisualizationItem url);

    public abstract boolean isFileSupported(String fileName);

    public abstract String fileTypeName();
}
