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
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;

/**
 *
 * @author cervenansky
 */
public class ModelDescriptionWindow extends Window {
    private DynamicForm form;
    private TextItem despItem;
    private ModalWindow modal;
    private ModelTreeGrid modelGrid;
    private String description ="";
    private int tp;
    private int ins;
    private String nwdescription ="";

    public ModelDescriptionWindow(ModelTreeGrid grid, String description) {
        
        this.modelGrid = grid;
      //  this.description = this.modelGrid.getModel().getModelDescription();
      
        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_EDIT) + " Description ");
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
        despItem = FieldUtil.getTextItem(200, true, "Description", "[0-9A-Za-z-_. ]");
        despItem.setValue(description);
        despItem.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    describe();
                }
            }
        });

        ButtonItem okButton = new ButtonItem("ok", "OK");
        okButton.setWidth(60);
        okButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                describe();
            }
        });
        form.setFields(despItem, okButton);
        this.addItem(form);
    }

    private void describe() {

         nwdescription = despItem.getValueAsString().trim();
        if (form.validate()) {
                if (description.equals(nwdescription)) {
                    Layout.getInstance().setWarningMessage("The specified name is the same as the original one.");
                }
                else
                {
                    modelGrid.setModelDescription(nwdescription);
                    destroy();
                }
      } 
        
    }
}