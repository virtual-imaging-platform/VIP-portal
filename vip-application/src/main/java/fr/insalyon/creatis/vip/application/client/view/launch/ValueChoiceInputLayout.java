package fr.insalyon.creatis.vip.application.client.view.launch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

import fr.insalyon.creatis.vip.application.models.boutiquesTools.BoutiquesInput;

/**
 * Representation of a multiple choice input (can be Number, String or File)
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class ValueChoiceInputLayout extends InputLayout{
    protected final Map<InputLayout, Set<String>> valueDisables = new HashMap<>();
    protected final Map<InputLayout, Set<String>> valueRequires = new HashMap<>();
    protected final Map<String, String> labels;

    /**
     * Initialises graphical labels and input field
     *
     * @param input        BoutiquesInput to be represented
     * @param parentLayout LaunchFormLayout containing this
     */
    public ValueChoiceInputLayout(BoutiquesInput input, LaunchFormLayout parentLayout, Map<String, String> labels) {
        // must not create the master form in the parent constructor to create and analyse labels first
        super(input, parentLayout, false);
        if (labels != null) {
            LaunchFormLayout.assertCondition(input.getPossibleValues().equals(labels.keySet()),
                    "The labels for the {" + input.getId() + "} input do not have the good values");
        }
        this.labels = labels;
        createMasterForm();
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
        if (labels != null) {
            inputField.setValueMap(labels);
        } else {
            inputField.setValueMap(this.input.getPossibleValues().toArray(new String[]{}));
        }
        if (this.getDefaultValue() != null) {
            inputField.setValue(this.getDefaultValue());
        } else if ( ! this.input.getPossibleValues().isEmpty()){
            inputField.setValue(this.input.getPossibleValues().iterator().next());
        }
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
     * Make specified values invalid if specified InputLayouts are empty. A validation error will
     * appear to inform user they selected an invalid value
     *
     * @param valueRequiresInputs Map from input values as Strings to Set of InputLayouts required by corresponding
     *                            value.
     */
    public void addValueRequires(Map<String, Set<InputLayout>> valueRequiresInputs){
        valueRequiresInputs.forEach((value, requiredInputSet) -> {
            requiredInputSet.forEach(requiredInput -> {
                addToSetInMap(this.valueRequires, requiredInput, value);
                requiredInput.addRequiredByValue(this, value);
            });
        });
        // Update main value field validator to reflect the dependency
        FormItem inputField = this.masterForm.getField(MAIN_FIELD_NAME);
        final ValueChoiceInputLayout layoutInstance = this;
        inputField.setValidators(new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                // Dependencies are ignored when input has multiple values, thus any value is considered valid
                // Empty value cannot require another input, thus it is always considered valid
                if(layoutInstance.hasUniqueValue() && (value != null)) {
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