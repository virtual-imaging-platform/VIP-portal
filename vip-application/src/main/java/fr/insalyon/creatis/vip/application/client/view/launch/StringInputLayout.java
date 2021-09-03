package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesStringInput;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;


/**
 * Representation of a text input (String or File)
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class StringInputLayout extends InputLayout{
    private final String allowedCharacters;
    /**
     * Initialise graphical labels and main input field
     *
     * @param input             BoutiquesInputString to be represented
     * @param parentLayout      LaunchFormLayout containing this
     * @param hasAddValueButton boolean: true if input should have a "add value" button, false otherwise
     * @param allowedChar       String representing a regexp of allowed characters (ex: "[a-zA-z0-9]" for alphanumeric).
     */
    public StringInputLayout(BoutiquesStringInput input, LaunchFormLayout parentLayout, boolean hasAddValueButton,
                             String allowedChar) {
        super(input, parentLayout);
        this.allowedCharacters = allowedChar;
        // Remove add value button if needed
        TextItem mainField = (TextItem) this.masterForm.getField(MAIN_FIELD_NAME);
        if (!hasAddValueButton){
            mainField.setIcons();
        }
        this.allowCharacters(mainField);
    }

    /**
     * @return FormItem representing an input field for this input
     * @see InputLayout#getFormItem()
     */
    @Override
    protected FormItem getFormItem() {
        final TextItem inputField = FieldUtil.getTextItem(400, ".");
        inputField.setValue(this.getDefaultValue());
        assert this.input instanceof BoutiquesStringInput;
        if(this.isFile()){
            // Add browse icon
            PickerIcon browsePicker = new PickerIcon(PickerIcon.SEARCH,
                    event -> new PathSelectionWindow(inputField).show());
            browsePicker.setPrompt("Browse on the Grid");
            inputField.setIcons(browsePicker);
            if(this.allowedCharacters != null) {
                this.allowCharacters(inputField);
            }
        }

        return inputField;
    }

    /**
     * @return boolean: true if this input represents a file input, false if it represent an arbitrary string input.
     */
    public boolean isFile(){
        return this.input.getType() == BoutiquesInput.InputType.FILE;
    }

    /**
     * Ensure provided input field accepts only allowed characters
     *
     * @param inputField TextItem
     */
    private void allowCharacters(TextItem inputField){
            inputField.setKeyPressFilter(this.allowedCharacters);
            inputField.setValidators(
                    ValidatorUtil.getStringValidator("^" + this.allowedCharacters + "+$"),
                    new RequiredIfValidator((formItem, value) -> !this.isOptional()));
    }
}