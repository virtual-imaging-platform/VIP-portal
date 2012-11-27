


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view.dialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import fr.insalyon.creatis.vip.models.client.view.ModelTreeGrid;

/**
 *
 * @author cervenansky
 */
public class RenameTimepointWindow extends Window{

    private DynamicForm form;
    private DateTimeItem dateItem = null;
    private ModalWindow modal;
    private ModelTreeGrid modelGrid;
    private String name;
    private int tp;
    private String nwname ="";

    public RenameTimepointWindow(ModelTreeGrid grid, int itp,  String name) {
        
        this.modelGrid = grid;
        this.name = name;
        this.tp = itp;


        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_EDIT) + "Renaming: " + name);
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
        String oldname = name.replace("Timepoint (", "").replace(")", "");
        
        dateItem = new DateTimeItem("dateTimeItem", "Date Time");  
        dateItem.setUseTextField(true);  
        dateItem.setUseMask(true);  
        
      //  nameItem = FieldUtil.getTextItem(200, true, "Name", "[0-9A-Za-z-_.]");

        dateItem.addKeyPressHandler(new KeyPressHandler() {

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
        form.setFields(dateItem, renameButton);
        this.addItem(form);
    }

    private void rename() {
        nwname = dateItem.getValueAsDate().toString();
        if (form.validate()) {
            if (!name.equals(nwname)) {
                ModelServiceAsync ms = ModelService.Util.getInstance();
                 final AsyncCallback<SimulationObjectModel>  callback = new AsyncCallback<SimulationObjectModel>() {

                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Unable to rename");
                    }

                    public void onSuccess(SimulationObjectModel result) {
                          Layout.getInstance().setNoticeMessage("rename done");
                          modelGrid.rename("Timepoint ("+nwname+")", result);
                    }
                };
                ms.renameTimepoint(modelGrid.getModel(), tp, dateItem.getValueAsDate(), callback);
                hide();

            } else {
                Layout.getInstance().setWarningMessage("The specified name is the same as the original one.");
            }
        }
    }
}
