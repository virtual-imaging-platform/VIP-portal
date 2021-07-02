package fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import fr.insalyon.creatis.vip.application.client.view.launch.boutiquesParsing.BoutiquesInput;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Representation of a multiple choice input (can be Number, String or File)
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class ValueChoiceInputLayout extends InputLayout{
    protected final Map<InputLayout, Set<String>> valueDisables = new HashMap<>();
    protected final Map<InputLayout, Set<String>> valueRequires = new HashMap<>();

    /**
     * Initialises graphical labels and input field
     *
     * @param input        BoutiquesInput to be represented
     * @param parentLayout LaunchFormLayout containing this
     */
    public ValueChoiceInputLayout(BoutiquesInput input, LaunchFormLayout parentLayout) {
        super(input, parentLayout);
    }

    /**
     * @return FormItem representing an input field for this input
     * @see InputLayout#getFormItem()
     */
    @Override
    protected FormItem getFormItem() {
        SelectItem inputField = new SelectItem();
        inputField.setWidth(400);
        inputField.setShowTitle(false);
        inputField.setValueMap(this.input.getPossibleValues());
        inputField.setValue(this.getDefaultValue());
        return inputField;
    }

    /**
     * Enable, disable and validate this as well as dependant inputs. Meant to be called each time a modification
     * happens on the values tracked by this (value changed by user, addition or removal of an additional form...)
     *
     * @see InputLayout#onValueChanged()
     */
    @Override
    protected void onValueChanged(){
        super.onValueChanged();
        this.valueDisables.keySet().forEach(InputLayout::checkDependencies);
    }

    /**
     * Make all InputLayouts in disabledInputSet disabled when provided value is selected in this
     *
     * @param value             String representing the disabling value
     * @param disabledInputSet  Set of InputLayouts to disable when value is selected
     */
    public void addValueDisables(String value, Set<InputLayout> disabledInputSet){
        disabledInputSet.forEach(disabledInput -> {
            addToSetInMap(this.valueDisables, disabledInput, value);
            disabledInput.addDisabledByValue(this, value);
            disabledInput.checkDependencies();
        });
    }

    /**
     * Make specified value invalid if some InputLayouts of requiredInputSet are empty. A validation error will
     * appear to inform user they selected an invalid value
     *
     * @param value             String representing dependant value
     * @param requiredInputSet  Set of InputLayouts required by value
     */
    public void addValueRequires(String value, Set<InputLayout> requiredInputSet){
        requiredInputSet.forEach(requiredInput -> {
            addToSetInMap(this.valueRequires, requiredInput, value);
            requiredInput.addRequiredByValue(this, value);
        });
        // Update main value field validator to reflect the dependency
        FormItem inputField = this.masterForm.getField(MAIN_FIELD_NAME);
        final ValueChoiceInputLayout layoutInstance = this;
        inputField.setValidators(new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                // Dependencies are ignored when input has multiple values, thus any value is considered valid
                // Empty value cannot require another input, thus it is always considered valid
                if(layoutInstance.hasUniqueValue() & (value != null)) {
                    // Identify all inputs required by current value that are empty, and collect their names
                    Set<String> emptyRequiredInputNames = layoutInstance.valueRequires.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().contains(value.toString()))
                            .filter(entry -> entry.getKey().isUniqueEmpty())
                            .map(entry -> entry.getKey().getInputName())
                            .collect(Collectors.toSet());
                    if (emptyRequiredInputNames.size() > 0) {
                        this.setErrorMessage("This value requires following inputs to be non empty: "
                                + emptyRequiredInputNames);
                        return false;
                    }
                }
                return true;
            }
        });
    }

    /**
     * @return Map of InputLayouts disabled when certain values of this are selected. Keys are dependant InputLayouts
     *         and values are Sets of Strings representing disabling values
     */
   
    public Map<InputLayout, Set<String>> getValueDisables() {
        return this.valueDisables;
    }

    /**
     * @return Map of InputLayouts that need to be non empty for certain values of this to be valid when selected. Keys
     *         are required InputLayouts and values are Sets of Strings representing dependant values
     */
   
    public Map<InputLayout, Set<String>> getValueRequires() {
        return this.valueRequires;
    }
}