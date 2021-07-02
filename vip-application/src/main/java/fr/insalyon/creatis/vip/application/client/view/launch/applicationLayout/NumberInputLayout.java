package fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.FloatRangeValidator;
import fr.insalyon.creatis.vip.application.client.view.launch.boutiquesParsing.BoutiquesInputNumber;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Representation of a number input. Notably allows validation of constraints on the number (integer, minimum, maximum),
 * as well as providing a range instead of a list of values
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class NumberInputLayout extends InputLayout {
    private final SelectItem typeSelector = this.configureLayoutSelector(); // Range / List layout selection
    // Validators for client-side validation logic
    private final CustomValidator numberValidator; // For checking we have a valid number
    private final CustomValidator rangeValidator; // For checking this number is between allowed minimum and maximum
    public enum FormLayout {
        LIST, RANGE;
        public static String[] names = camelNames(values());
    }
    public enum RangeItem {
        START, STEP, STOP;
        public static String[] names = camelNames(values());
    }

    /**
     * Return given enum value's name with only first letter in uppercase (used for displaying values)
     *
     * @param enumValue Enum value
     * @return          Formatted name as String
     */
    public static String camelName(Enum<?> enumValue) {
        String enumName = enumValue.name();
        return enumName.charAt(0) + enumName.substring(1).toLowerCase();
    }

    /**
     * Take an array of enum values and return an array of their names with only first letter in uppercase
     *
     * @param enumValues    Array of Enum values
     * @return              Array of Strings representing enumValues formatted names
     */
    public static String[] camelNames(Enum<?>[] enumValues) {
        return Arrays.stream(enumValues).map(NumberInputLayout::camelName).toArray(String[]::new);
    }

    /**
     * Initialise graphical labels and main input form, which contains one value field and a SelectItem to display
     * instead a range layout (three value fields: start, step and end)
     *
     * @param parsedInput   BoutiquesInputNumber to be represented
     * @param parentLayout  LaunchFormLayout containing this
     */
    public NumberInputLayout(final BoutiquesInputNumber parsedInput, LaunchFormLayout parentLayout) {
        super(parsedInput, parentLayout);
        // Configure validators for client-side validation logic
        // Check that input is filled if non optional and is a valid number / integer.
        this.numberValidator = new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                if (value == null) {
                    this.setErrorMessage("Field is required");
                    return parsedInput.isOptional();
                }
                try {
                    double doubleValue = Double.parseDouble(String.valueOf(value));
                    if (parsedInput.isInteger()) {
                        this.setErrorMessage("Must be an integer.");
                        return ((doubleValue % 1) == 0);
                    }
                    return true;
                } catch (NumberFormatException e) {
                    this.setErrorMessage("Must be a number.");
                    return false;
                }
            }
        };
        // Check that provided value is between input minimum and maximum.
        this.rangeValidator = new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                try {
                    double doubleValue = Double.parseDouble(String.valueOf(value));
                    Double maximum = parsedInput.getMaximum();
                    if (maximum != null) {
                        if (parsedInput.isExclusiveMaximum() & (doubleValue >= maximum)) {
                            this.setErrorMessage("Must be strictly less than " + maximum);
                            return false;
                        } else {
                            if (doubleValue > maximum) {
                                this.setErrorMessage("Must be at most " + maximum);
                                return false;
                            }
                        }
                    }
                    Double minimum = parsedInput.getMinimum();
                    if (minimum != null) {
                        if (parsedInput.isExclusiveMinimum() & (doubleValue <= minimum)) {
                            this.setErrorMessage("Must be strictly greater than " + minimum);
                            return false;
                        } else {
                            if (doubleValue < minimum) {
                                this.setErrorMessage("Must be at least " + minimum);
                                return false;
                            }
                        }
                    }
                } catch (NumberFormatException ignored) {
                }
                return true;
            }
        };
        // Overwrite main input field created by super(). Number main input field is indeed more complicated, notably
        // allowing range input instead of single value. Done here instead of overriding createMasterForm() because we
        // need validators above to have been configured first
        this.overwriteMasterForm();
    }

    /**
     * @return FormItem representing an input field for this input
     * @see InputLayout#getFormItem()
     */
    @Override
    protected FormItem getFormItem() {
        TextItem inputField = new TextItem();
        inputField.setWidth(330);
        inputField.setShowTitle(false);
        inputField.setKeyPressFilter("[0-9.\\-]");
        inputField.setValue(this.getDefaultValue());
        return inputField;
    }

    /**
     * Create an additional input field for current layout
     * @see InputLayout#createAdditionalForm()
     */
    @Override
    protected void createAdditionalForm() {
        final DynamicForm inputForm = this.addForm(this.removeValueIcon);
        inputForm.getField(MAIN_FIELD_NAME).setValidators(this.numberValidator, this.rangeValidator);
        // Size adjustment to align with main field, which contains an additional SelectItem
        inputForm.setWidth(400);
        inputForm.setNumCols(1);
        inputForm.getField(MAIN_FIELD_NAME).setAlign(Alignment.RIGHT);
        this.additionalForms.add(inputForm);
        this.onValueChanged();
    }

    /**
     * @return boolean: true if all additional fields in this have valid values, else otherwise. When range layout
     *         is selected, additional fields are hidden and ignored, thus considered valid (true is returned)
     * @see InputLayout#validateAdditionalForms()
     */
    @Override
    protected boolean validateAdditionalForms() {
        if(this.isRange()){
            return true;
        }
        return super.validateAdditionalForms();
    }

    /**
     * Overwrite current values with given valueSet
     *
     * @param valueList ValueSet containing values to write
     * @see InputLayout#overwriteValues(ValueSet)
     */
    @Override
    public void overwriteValues(ValueSet valueList) {
        if (valueList instanceof ValueRange) {
            this.setLayout(FormLayout.RANGE);
            assert !this.masterForm.getField(MAIN_FIELD_NAME).isVisible();
            Float[] rangeLimits = ((ValueRange) valueList).getRangeLimits();
            for (int k = 0; k < 3; k++) {
                this.masterForm.getField(RangeItem.names[k]).setValue(rangeLimits[k]);
            }
            this.onValueChanged();
        } else {
            this.setLayout(FormLayout.LIST);
            assert this.masterForm.getField(MAIN_FIELD_NAME).isVisible();
            super.overwriteValues(valueList);
        }
    }

    /**
     * @return ValueSet representing current values of this input
     * @see InputLayout#getValueList()
     */
    @Override
    public ValueSet getValueList() {
        if (this.isRange()) {
            return new ValueRange(this.masterForm);
        }
        return new ValueList(this.masterForm, this.additionalForms);
    }

    /**
     * @return boolean: true if this input currently displays only one input field (unique value).
     * @see InputLayout#hasUniqueValue()
     */
    @Override
    public boolean hasUniqueValue() {
        GWT.log(String.valueOf(this.typeSelector));
        return super.hasUniqueValue() & !this.isRange();
    }

    /**
     * @return SelectItem allowing user to chose between entering a list of values or a range
     */
    private SelectItem configureLayoutSelector() {
        SelectItem selector = new SelectItem();
        selector.setWidth(70);
        selector.setShowTitle(false);
        selector.setValueMap(FormLayout.names);
        selector.setValue(camelName(FormLayout.LIST));
        selector.addChangedHandler(changedEvent -> this.setLayout(this.getSelectedFormLayout()));
        return selector;
    }

    /**
     * Overwrite main input field created by super. This master form contains a SelectItem to select List or Range
     * layout, as well as fields for both layouts. Should be called only once by constructor
     */
    protected void overwriteMasterForm() {
        // Fields for master form
        final FormItem[] inputFields = new FormItem[5];
        // List/Range selection
        inputFields[0] = this.typeSelector;
        // Value
        final FormItem valueItem = this.getFormItem();
        valueItem.setName(MAIN_FIELD_NAME);
        this.addValueChangeHandler(valueItem);
        valueItem.setValidators(this.numberValidator, this.rangeValidator);
        inputFields[1] = valueItem;
        // Range
        final FormItem[] rangeItems = new FormItem[3]; // Start, Step, End
        for (int k = 0; k < 3; k++) {
            rangeItems[k] = this.getFormItem();
            rangeItems[k].setWidth(80);
            rangeItems[k].setTitle(RangeItem.names[k]);
            rangeItems[k].setShowTitle(true);
            rangeItems[k].setName(RangeItem.names[k]);
            rangeItems[k].setRequired(true);
            rangeItems[k].hide(); // Default to List layout
            inputFields[k + 2] = rangeItems[k];
        }
        // Range validation logic
        // Start
        CustomValidator rangeStartValidator = new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                // Check that start is less than end
                Float maxValue = (Float) rangeItems[2].getValue();
                if (maxValue != null) {
                    try {
                        float floatValue = Float.parseFloat(String.valueOf(value));
                        return (floatValue < maxValue);
                    } catch (NumberFormatException ignored) { }
                }
                return true;
            }
        };
        rangeStartValidator.setErrorMessage("Must be less than range end");
        rangeItems[0].setValidators(this.numberValidator, this.rangeValidator, rangeStartValidator);
        // End
        CustomValidator rangeEndValidator = new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                // Check that end is greater than start
                Float minValue = (Float) rangeItems[0].getValue();
                if (minValue != null) {
                    try {
                        float floatValue = Float.parseFloat(String.valueOf(value));
                        return (floatValue > minValue);
                    } catch (NumberFormatException ignored) {
                    }
                }
                return true;
            }
        };
        rangeEndValidator.setErrorMessage("Must be more than range start");
        rangeItems[2].setValidators(this.numberValidator, this.rangeValidator, rangeEndValidator);
        // Step
        CustomValidator rangeStepValidator = new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                // Check that step is smaller than the difference between end and start
                Float minValue = (Float) rangeItems[0].getValue();
                Float maxValue = (Float) rangeItems[2].getValue();
                if ((minValue != null) & (maxValue != null)) {
                    try {
                        float floatValue = Float.parseFloat(String.valueOf(value));
                        return (floatValue < (maxValue - minValue));
                    } catch (NumberFormatException ignored) {
                    }
                }
                return true;
            }
        };
        rangeStepValidator.setErrorMessage("Must be less than range size");
        // Check that step is positive
        FloatRangeValidator positiveValidator = new FloatRangeValidator();
        positiveValidator.setMin(0);
        //positiveValidator.setExclusive(true);
        positiveValidator.setErrorMessage("Must be strictly positive.");
        rangeItems[1].setValidators(rangeStepValidator, positiveValidator, this.numberValidator);
        // Overwrite master form
        this.masterForm.setFields(inputFields);
        this.masterForm.addItemChangedHandler(itemChangedEvent -> this.onValueChanged(itemChangedEvent.getItem()));
        valueItem.addIcon(this.addValueIcon); // If placed before call to setFields, the icon don't show
    }

    /**
     * Set input layout to List (one or more value fields) or Range (three fields: Start, Step, End)
     *
     * @param layoutType FormLayout indicating the layout to set
     */
    private void setLayout(FormLayout layoutType) {
        this.typeSelector.setValue(camelName(layoutType)); // Redundant only when called from typeSelector changedEvent
        FormItem valueField = this.masterForm.getField(MAIN_FIELD_NAME);
        switch (layoutType) {
            case LIST:
                // Show value fields, hide range fields
                this.rangeItemAction(FormItem::hide);
                this.additionalForms.forEach(DynamicForm::show);
                valueField.show();
                this.masterForm.setColWidths(0, 0);
                this.masterForm.setNumCols(2);
                break;
            case RANGE:
                // Hide value fields, show range fields
                this.rangeItemAction(FormItem::show);
                this.additionalForms.forEach(DynamicForm::hide);
                valueField.hide();
                this.masterForm.setNumCols(7);
                this.masterForm.setColWidths(0, 32, 0, 30, 0, 28, 0);
                this.masterForm.setFixedColWidths(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + layoutType);
        }
        this.onValueChanged();
    }

    /**
     * Perform given action on each Range field (Start value, Step value and End value)
     *
     * @param action Consumer<FormItem> to apply to Range fields
     */
    private void rangeItemAction(Consumer<FormItem> action){
        Arrays.stream(RangeItem.names)
                .map(this.masterForm::getField)
                .forEach(action);
    }

    /**
     * @return FormLayout representing currently selected
     * @throws IllegalStateException if selected value does not correspond to an existing FormLayout
     */
    public FormLayout getSelectedFormLayout() throws IllegalStateException{
        String selectedValue = ((String) this.typeSelector.getValue());
        if(Arrays.asList(FormLayout.names).contains(selectedValue)){
            return FormLayout.valueOf(selectedValue.toUpperCase());
        } else {
            throw new IllegalStateException("Unknown selected layout type. Only allowed values are "
                    + Arrays.toString(FormLayout.names));
        }
    }

    /**
     * @return boolean: true if Range layout is selected, false otherwise
     */
    public boolean isRange() {
        return this.getSelectedFormLayout().equals(FormLayout.RANGE);
    }
}