package fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.launch.boutiquesParsing.BoutiquesInputString;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;


/**
 * Representation of a text input (String or File)
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class StringInputLayout extends InputLayout{
    /**
     * Initialise graphical labels and main input field
     *
     * @param input             BoutiquesInputString to be represented
     * @param parentLayout      LaunchFormLayout containing this
     * @param hasAddValueButton boolean: true if input should have a "add value" button, false otherwise
     */
    public StringInputLayout(BoutiquesInputString input, LaunchFormLayout parentLayout, boolean hasAddValueButton) {
        super(input, parentLayout);
        // Remove add value button if needed
        if (!hasAddValueButton){
            this.masterForm.getField(MAIN_FIELD_NAME).setIcons();
        }
    }

    /**
     * @return FormItem representing an input field for this input
     * @see InputLayout#getFormItem()
     */
    @Override
    protected FormItem getFormItem() {
        final TextItem inputField = FieldUtil.getTextItem(400,
                "[" + ApplicationConstants.INPUT_VALID_CHARS + "]");
        inputField.setValidators(new RegExpValidator(
                "^([" + ApplicationConstants.INPUT_VALID_CHARS + "])+$"));
        inputField.setValue(this.getDefaultValue());
        inputField.setRequired(!this.isOptional());
        assert this.input instanceof BoutiquesInputString;
        if(((BoutiquesInputString) this.input).getType().equals("File")){
            // Add browse icon
            PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH,
                    event -> new PathSelectionWindow(inputField).show());
            browsePicker.setPrompt("Browse on the Grid");
            inputField.setIcons(browsePicker);
        }
        return inputField;
    }
}