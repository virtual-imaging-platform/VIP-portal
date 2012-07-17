/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;


/**
 *
 * @author cervenansky
 */
public class RenameInstantWindow extends Window {

    private DynamicForm form;
    private TextItem nameItem;
    private ModalWindow modal;
    private ModelTreeGrid modelGrid;
    private String name;
    private int tp;
    private int ins;
    private String nwname ="";

    public RenameInstantWindow(ModelTreeGrid grid, int itp, int iins, String name) {
        
        this.modelGrid = grid;
        this.name = name;
        this.tp = itp;
        this.ins = iins;

        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_EDIT) + " Renaming: " + name);
        this.setWidth(350);
        this.setHeight(110);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        form = new DynamicForm();
        form.setHeight100();
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);
        String oldname = name.replace("Instant (", "").replace(")", "");
        nameItem = FieldUtil.getTextItem(200, true, "Name", "[0-9A-Za-z-_.]");
        nameItem.setValue(oldname);
        nameItem.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    rename();
                }
            }
        });

        ButtonItem renameButton = new ButtonItem("renameButton", "Rename");
        renameButton.setWidth(60);
        renameButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                rename();
            }
        });
        form.setFields(nameItem, renameButton);
        this.addItem(form);
    }

    private void rename() {

         nwname = nameItem.getValueAsString().trim();
        if (form.validate()) {
            if (!name.equals(nwname)) {
                ModelServiceAsync ms = ModelService.Util.getInstance();
                 final AsyncCallback<SimulationObjectModel>  callback = new AsyncCallback<SimulationObjectModel>() {

                    public void onFailure(Throwable caught) {
                      
                        SC.warn("Unable to rename");
                    }

                    public void onSuccess(SimulationObjectModel result) {
                         modelGrid.rename("Instant ("+nwname+")", result);
                       
                    }
                };
                 
                  ms.renameInstant(modelGrid.getModel(), tp, ins,nwname, callback);
                  destroy();

            } else {
                SC.warn("The specified name is the same as the original one.");
            }
        }
    }
}
