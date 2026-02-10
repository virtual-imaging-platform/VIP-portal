package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;

/**
 *
 * @author Tristan Glatard
 */
public class LocalTextField extends VLayout {

    private final TextItem fieldItem;

    public LocalTextField(String name, boolean enabled, boolean browseIcon) {

        // Configure label
        Label itemLabel = new Label("<strong>" + name + "</strong>");
        itemLabel.setHeight(20);
        this.addMember(itemLabel);

        // Configure field item
        fieldItem = FieldUtil.getTextItem("*", false, "", "", !enabled, true);
        PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                event.getItem().setValue("");
                new PathSelectionWindow((TextItem) event.getItem()).show();
            }
        });
        browsePicker.setPrompt("Browse");
        if (browseIcon) {
            fieldItem.setIcons(browsePicker);
        }
        DynamicForm titleItemForm = new DynamicForm();
        titleItemForm.setWidth100();
        titleItemForm.setNumCols(1);
        titleItemForm.setFields(fieldItem);

        this.addMember(titleItemForm);
    }

    public LocalTextField(String name, boolean enabled, boolean browseIcon, String value) {
        this(name, enabled, browseIcon);
        setValue(value);
    }

    public final void setValue(String value) {
        fieldItem.setValue(value);
    }
    
    public String getValue(){
        return fieldItem.getValueAsString();
    }
}
