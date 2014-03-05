/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.view;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;

/**
 *
 * @author Nouha Boujelben
 */
public class N4uImportTab extends Tab {

    private VLayout layout;
    PickerIcon browsePicker;
    PickerIcon morePicker;
    PickerIcon lessPicker;

    public N4uImportTab() {


        this.setTitle(Canvas.imgHTML(N4uConstants.ICON_EXPRESSLANE1) + "ExpressLaneImporter");
        this.setID(N4uConstants.TAB_EXPRESSLANE_2);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);

        configure();
        this.setPane(layout);

    }

    public void configure() {
        layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMargin(6);
        layout.setMembersMargin(5);


        browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                event.getItem().setValue("");
                new PathSelectionWindow((TextItem) event.getItem()).show();
            }
        });
        browsePicker.setPrompt("Browse on the Grid");

        morePicker = new PickerIcon(new PickerIcon.Picker(N4uConstants.ICON_PICKER_MORE), new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
            }
        });

        morePicker.setPrompt("Add");
        
        
        
        lessPicker = new PickerIcon(new PickerIcon.Picker(N4uConstants.ICON_PICKER_LESS), new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                layout.removeMember(event.getForm());
            }
        });
        lessPicker.setPrompt("Remove");









    }

    public void addfielsInputs(String title, String value) {
        Label itemLabel = new Label("<strong>" + title + "</strong>");
        itemLabel.setHeight(20);

        TextItem fieldItem = FieldUtil.getTextItem(400, false, "", "[0-9.,A-Za-z-+/_() ]");
        fieldItem.setValidators(ValidatorUtil.getStringValidator());
        fieldItem.setIcons(browsePicker, morePicker,lessPicker);
        fieldItem.setValue(value);

        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setFields(fieldItem);


        layout.addMember(itemLabel);
        layout.addMember(titleItemForm);




    }
}
