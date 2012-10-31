/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;


import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

/**
 *
 * @author cervenansky
 */
public class ModelNameWindow extends Window {
    private DynamicForm form;
    private TextItem despItem;
    private ModelTreeGrid modelGrid;
    private String nwname ="";

    public ModelNameWindow(ModelTreeGrid grid, String name) {
        
        this.modelGrid = grid;
      //  this.description = this.modelGrid.getModel().getModelDescription();
      
        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_EDIT) + " Rename ");
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
        despItem = FieldUtil.getTextItem(200, true, "Rename", "[0-9A-Za-z-_. ]");
        despItem.setValue(name);
        despItem.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    rename();
                }
            }
        });

        ButtonItem okButton = new ButtonItem("ok", "OK");
        okButton.setWidth(60);
        okButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                rename();
            }
        });
        form.setFields(despItem, okButton);
        this.addItem(form);
    }

    private void rename() {

         nwname = despItem.getValueAsString().trim();
        if (form.validate()) {
            if (modelGrid.getModelName().equals(nwname)) {
                Layout.getInstance().setWarningMessage("The specified name is the same as the original one.");
            }
            else
            {
                modelGrid.setModelName(nwname);
                destroy();
            }

            } 
        
    }
}