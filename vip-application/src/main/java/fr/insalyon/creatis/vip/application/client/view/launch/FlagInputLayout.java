package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesFlagInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representation of a checkbox input (Flag input type)
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class FlagInputLayout extends InputLayout{
    private String nameText;
    /**
     * Initialises graphical labels and checkbox
     *
     * @param input        BoutiquesInputFlag to be represented
     * @param parentLayout LaunchFormLayout containing this
     */
    public FlagInputLayout(BoutiquesFlagInput input, LaunchFormLayout parentLayout) {
        super(input, parentLayout);
    }

    @Override
    protected void configureLayout(Label nameLabel, Label descriptionLabel, boolean createMasterForm) {
        this.canAddValue = false;
        this.nameText = nameLabel.getContents();
        this.createMasterForm();
        // Description
        if (descriptionLabel != null){
            VLayout descriptionLayout = new VLayout();
            descriptionLayout.setLayoutLeftMargin(20);
            descriptionLayout.addMember(descriptionLabel);
            this.addMember(descriptionLayout);
        }
        this.addMember(new LayoutSpacer(10,5));
    }

    /**
     * @return FormItem representing a checkbox for this input
     * @see InputLayout#getFormItem()
     */
    @Override
    protected FormItem getFormItem() {
        CheckboxItem inputItem = new CheckboxItem();
        inputItem.setWidth(400);
        inputItem.setShowTitle(true);
        inputItem.setTitle(this.nameText);
        inputItem.setValue(this.getDefaultValue());
        return inputItem;
    }

    /**
     * @return boolean: true if main input box is unchecked
     * @see InputLayout#isMasterEmpty()
     */
    @Override
    public boolean isMasterEmpty(){
        Object masterValue = ValueList.formValue(this.masterForm);
        if(masterValue instanceof String){
            masterValue = Boolean.parseBoolean((String) masterValue);
        }
        LaunchFormLayout.assertCondition(masterValue instanceof Boolean,
                "Invalid state: flag input value should be a boolean.");
        return  !((boolean) masterValue);
    }
}