package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.types.PickerIconName;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Representation of an input in application launch form: name, description, value field or fields
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public abstract class InputLayout extends VLayout {
    public static String MAIN_FIELD_NAME = "value";
    public static String UNMODIFIABLE_ATTR = "unmodifiable";
    protected final LaunchFormLayout parentLayout;
    protected final BoutiquesInput input;
    // Input forms
    protected DynamicForm masterForm;
    protected final ArrayList<DynamicForm> additionalForms = new ArrayList<>();
    protected final PickerIcon addValueIcon = this.configureAddValueIcon();
    protected final PickerIcon removeValueIcon = this.configureRemoveValueIcon();
    // Dependencies with other inputs or groups
    protected final Set<InputLayout> disables = new HashSet<>();
    protected final Set<InputLayout> disabledBy = new HashSet<>();
    protected final Map<ValueChoiceInputLayout, Set<String>> disabledByValue = new HashMap<>();
    protected final Set<InputLayout> requires = new HashSet<>();
    protected final Set<InputLayout> requiredBy = new HashSet<>();
    protected final Map<ValueChoiceInputLayout, Set<String>> requiredByValue = new HashMap<>();
    protected boolean unmodifiable = false;
    protected boolean canAddValue = true;
    protected Set<String> unmodifiableValues = new HashSet<>();

    /**
     * In a Map with Sets as values, add newValue to the Set corresponding to given key. If this Set does not exist yet,
     * create it containing only newValue
     *
     * @param map       Map of Objects to Sets of Objects
     * @param key       Object
     * @param newValue  Object
     */
    public static <K, V> void addToSetInMap(Map<K, Set<V>> map, K key,
                                         V newValue){
        if(map.containsKey(key)){
            map.get(key).add(newValue);
        } else {
            map.put(key, new HashSet<>(Collections.singleton(newValue)));
        }
    }

    /**
     * Initialises graphical labels and input field
     *
     * @param input         BoutiquesInput to be represented
     * @param parentLayout  LaunchFormLayout containing this
     */
    public InputLayout(BoutiquesInput input, LaunchFormLayout parentLayout){
        this(input, parentLayout, true);
    }

    /**
     * Initialize graphical labels and input field in option. If createMasterForm is false, then the child
     * constructor must call createMasterForm itself or create the masterForm in a different way
     *
     * @param input             BoutiquesInput to be represented
     * @param parentLayout      LaunchFormLayout containing this
     * @param createMasterForm  true to create the masterForm
     */
    protected InputLayout(BoutiquesInput input, LaunchFormLayout parentLayout, boolean createMasterForm) {
        super(5);
        this.setLayoutLeftMargin(25);
        this.setWidth(300);
        this.parentLayout = parentLayout;
        this.input = input;
        // Name
        String nameText =  "<b>" + input.getName();
        if (!input.isOptional()){
            nameText += "<font color=\"red\">*</font>";
        }
        nameText += "</b>";
        Label nameLabel = WidgetUtil.getLabel(nameText, 15);
        this.addMember(nameLabel);
        // Description
        String description = input.getDescription();
        if (description != null && !description.isEmpty()){
            Label descriptionLabel = WidgetUtil.getLabel(description,15);
            //descriptionLabel.setWidth(400);
            this.addMember(descriptionLabel);
        }
        if (createMasterForm) {
            // Input field
            this.createMasterForm();
        }
    }

    /**
     * Configure the "add value" button on input main field
     *
     * @return PickerIcon representing the "add value" button
     */
    private PickerIcon configureAddValueIcon(){
        PickerIcon icon = new PickerIcon(PickerIconName.SEARCH, event -> this.createAdditionalForm());
        icon.setSrc(ApplicationConstants.ICON_PICKER_MORE);
        icon.setHeight(20);
        icon.setWidth(20);
        icon.setPrompt("Add another value");
        return icon;
    }

    /**
     * Configure the "remove value" button on input additional fields
     *
     * @return PickerIcon representing the "remove value" button
     */
    private PickerIcon configureRemoveValueIcon(){
        PickerIcon icon = new PickerIcon(PickerIconName.SEARCH, event -> this.removeAddedForm(event.getForm()));
        icon.setSrc(ApplicationConstants.ICON_PICKER_LESS);
        icon.setHeight(20);
        icon.setWidth(20);
        icon.setPrompt("Remove value");
        return icon;
    }

    /**
     * Create and return a new input field and add it to this layout
     *
     * @param iconToAdd PickerIcon attached to created input field
     * @return          DynamicForm representing created input field
     */
    protected DynamicForm newForm(PickerIcon iconToAdd){
        final DynamicForm inputForm = new DynamicForm();
        final FormItem inputField = this.getFormItem();
        inputField.setName(MAIN_FIELD_NAME);
        this.addValueChangeHandler(inputField);
        inputForm.setFields(inputField);
        this.addMember(inputForm);
        // Add/remove value button
        inputField.addIcon(iconToAdd);
        return inputForm;
    }

    /**
     * Create master input field for current layout
     * @see #newForm(PickerIcon)
     */
    protected void createMasterForm(){
        this.masterForm = this.newForm(this.addValueIcon);
    }

    /**
     * Create an additional input field for current layout
     * @see #newForm(PickerIcon)
     */
    protected void createAdditionalForm(){
        DynamicForm additionalForm = this.newForm(this.removeValueIcon);
        this.additionalForms.add(additionalForm);
        if (this.unmodifiable) {
            this.setFormUnmodifiable(additionalForm, true);
        }
        this.onValueChanged();
    }

    /**
     * Remove an additional input field from current layout
     *
     * @param formToRemove  DynamicForm to remove
     * @throws IllegalArgumentException if formToRemove is not in this.additionalForms
     * @see #createAdditionalForm()
     */
    private void removeAddedForm(DynamicForm formToRemove) throws IllegalArgumentException{
        if(!this.additionalForms.remove(formToRemove)){
            throw new IllegalArgumentException("Provided form is not contained in this InputLayout's additional forms.");
        }
        LaunchFormLayout.assertCondition(this.hasMember(formToRemove),
                "Illegal state: provided additional form was found but is not a member of this InputLayout.");
        this.removeMember(formToRemove);
        this.onValueChanged();
    }

    /**
     * Add a changed handler to given FormItem, to make it call this.onValueChanged (dependencies check) on change
     *
     * @param mainField FormItem
     */
    protected void addValueChangeHandler(FormItem mainField){
        mainField.addChangedHandler(changedEvent -> this.onValueChanged(mainField));
    }

    /**
     * Make target InputLayout disabled when this has non empty value
     *
     * @param disabledInput Target InputLayout
     */
    public void addDisables(InputLayout disabledInput){
        this.disables.add(disabledInput);
        disabledInput.addDisabledBy(this);
        disabledInput.checkDependencies();
    }

    /**
     * Make this disabled when target InputLayout has non empty value
     *
     * @param disablingInput Target InputLayout
     */
    protected  void addDisabledBy(InputLayout disablingInput){
        this.disabledBy.add(disablingInput);
    }

    /**
     * Make this disabled when target ValueChoiceInputLayout has specified value
     *
     * @param disablingInput Target InputLayout
     * @param disablingValue String representing specified value
     */
    protected void addDisabledByValue(ValueChoiceInputLayout disablingInput, String disablingValue){
        addToSetInMap(this.disabledByValue, disablingInput, disablingValue);
    }

    /**
     * Make this disabled when target InputLayout has empty value
     *
     * @param requiredInput Target InputLayout
     */
    public void addRequires(InputLayout requiredInput){
        this.requires.add(requiredInput);
        requiredInput.addRequiredBy(this);
        this.checkDependencies();
    }

    /**
     * Make target InputLayout disabled when this has empty value
     *
     * @param requiringInput Target InputLayout
     */
    protected  void addRequiredBy(InputLayout requiringInput){
        this.requiredBy.add(requiringInput);
    }

    /**
     * Make specified value from target ValueChoiceInputLayout invalid when this has empty value
     *
     * @param requiringInput Target InputLayout
     * @param requiringValue String representing specified value
     */
    protected void addRequiredByValue(ValueChoiceInputLayout requiringInput, String requiringValue) {
        addToSetInMap(this.requiredByValue, requiringInput, requiringValue);
    }

    public void addUnmodifiableValues(Set<String> unmodifiableValues) {
        this.unmodifiableValues = unmodifiableValues;
    }

    /**
     * Check dependencies with other inputs to enable or disable this as appropriate
     */
    protected void checkDependencies(){
        if(!this.hasUniqueValue()) {
            return;
        }
        boolean wasEmpty = this.isMasterEmpty();
        // Find all inputs that can cause this to be disabled
        Set<String> disablingInputNames = this.filledDisablingInputs();
        Set<String> requiredInputNames = this.emptyRequiredInputs();
        Map<String, String> disablingValues = this.disablingInputValues();
        if((disablingInputNames.size() + requiredInputNames.size() + disablingValues.size()) == 0){
            this.enableInput();
        } else {
            // Disable input, with a help hover message explaining why
            StringBuilder disabledHoverMessage = new StringBuilder("This value will be ignored because :<ul>");
            if(disablingInputNames.size() > 0){
                disabledHoverMessage.append("<li>following input(s) are non-empty: ")
                        .append(disablingInputNames)
                        .append(".</li>");
            }
            if(requiredInputNames.size() > 0){
                disabledHoverMessage.append("<li>following input(s) are empty: ")
                        .append(requiredInputNames)
                        .append(".</li>");
            }
            if(disablingValues.size() > 0) {
                disablingValues.forEach(
                        (inputName, value) -> disabledHoverMessage.append("<li>input [")
                                .append(inputName)
                                .append("] has value ")
                                .append(value)
                                .append(".</li>"));
            }
            for (FormItem field : this.masterForm.getFields()) {
                field.disable();
                field.setPrompt(disabledHoverMessage.toString());
            }
        }
        if (this.isMasterEmpty() != wasEmpty){
            // Status changed, this may enable/disable dependent inputs
            this.onValueChanged();
        }
    }

    /**
     * @return Set of Strings representing names of inputs that are non empty and disable this.
     */
    protected Set<String> filledDisablingInputs(){
        return this.disabledBy.stream()
                .filter(InputLayout::isUniqueFilled)
                .map(InputLayout::getInputName)
                .collect(Collectors.toSet());
    }

    /**
     * @return Set of Strings representing names of inputs that are empty and required by this (thus disabling this).
     */
    protected Set<String> emptyRequiredInputs(){
        return this.requires.stream()
                .filter(InputLayout::isUniqueEmpty)
                .map(InputLayout::getInputName)
                .collect(Collectors.toSet());
    }

    /**
     * @return Map of Strings representing inputs for which current value disables this. Keys are input names and values
     *         are corresponding current values
     */
    protected Map<String, String> disablingInputValues(){
        Map<String, String> disablingValues = new TreeMap<>();
        this.disabledByValue.forEach((disablingInput, disablingValueSet) -> {
            if(disablingInput.isUniqueFilled()){
                Object masterValue = ValueList.formValue(disablingInput.masterForm);
                LaunchFormLayout.assertCondition(masterValue != null,
                        "Illegal state: master input field is not empty but has null value.");
                String currentValue = masterValue.toString();
                if(disablingValueSet.contains(currentValue)){
                    disablingValues.put(disablingInput.getInputName(), currentValue);
                }
            }
        });
        return disablingValues;
    }

    /**
     * Enable, disable and validate this as well as dependant inputs. Meant to be called each time a modification
     * happens on the values tracked by this (value changed by user, addition or removal of an additional form...)
     *
     * @param focusedItem   FormItem currently focused, in order to give it focus back after validation of inputs
     * @see #onValueChanged()
     */
    protected void onValueChanged(FormItem focusedItem){
        this.onValueChanged();
        focusedItem.focusInItem();
    }

    /**
     * Enable, disable and validate this as well as dependant inputs. Meant to be called each time a modification
     * happens on the values tracked by this (value changed by user, addition or removal of an additional form...)
     */
    protected void onValueChanged(){
        if(this.hasUniqueValue()){
            this.parentLayout.removeUnsupportedDependencies(this.getInputId());

            this.checkDependencies();
        } else {
            this.parentLayout.addUnsupportedDependencies(this);
            this.enableInput();
        }

        this.disables.forEach(InputLayout::checkDependencies);
        this.requiredBy.forEach(InputLayout::checkDependencies);
        // Value-requires does not enable/disable target inputs, but only produces validation errors, thus we call
        // validateInput, not checkDependencies
        this.requiredByValue.keySet().forEach(ValueChoiceInputLayout::validateInput);
        this.validateInput();
        this.checkIfModifiable();
        this.parentLayout.validateGroups();
    }

    /**
     * Enable main field (additional fields cannot be disabled
     */
    protected void enableInput(){
        for(FormItem field : this.masterForm.getFields()){
            field.enable();
        }
    }

    private void checkIfModifiable() {
        if (this.unmodifiable) {
            return;
        }
        Object value = ValueList.formValue(this.masterForm);
        setMasterUnmodifiable(value != null && unmodifiableValues.contains(value.toString()));
    }

    public void setUnmodifiablePermanently() {
        if ( ! unmodifiable) {
            this.unmodifiable = true;
            setMasterUnmodifiable(true);
        }
    }

    private void setMasterUnmodifiable(boolean unmodifiable) {
        this.setFormUnmodifiable(masterForm, unmodifiable);
    }

    private void setFormUnmodifiable(DynamicForm form, boolean unmodifiable) {
        if ( unmodifiable) {
            for(FormItem field : form.getFields()){
                field.disable();
                field.setPrompt("This input cannot be changed");
                field.setAttribute(UNMODIFIABLE_ATTR, true);
            }
        } else {
            for (FormItem field : form.getFields()) {
                field.enable();
                field.setPrompt(null);
                field.setAttribute(UNMODIFIABLE_ATTR, (Object) null);
            }
        }
    }

    public void disableAddingValue() {
        this.canAddValue = false;
        this.masterForm.getField(MAIN_FIELD_NAME).setIcons();
    }

    /**
     * Validate this, and update parentLayout invalid inputs error message as appropriate
     */
    protected void validateInput() {
        if(this.masterForm.validate(false) && validateAdditionalForms()) {
            this.parentLayout.removeValidationErrorOnInput(this);
        } else {
            this.parentLayout.addValidationErrorOnInput(this);
        }
    }

    /**
     * @return boolean: true if all additional fields in this have valid values, else otherwise
     */
    protected boolean validateAdditionalForms() {
        long nValidForms = this.additionalForms.stream()
                .filter(DynamicForm::validate)
                .count();
        return this.additionalForms.size() == nValidForms;
    }

    /**
     * Overwrite current values with given valueSet
     *
     * @param valueList ValueSet containing values to write
     */
    public void overwriteValues(ValueSet valueList) {
        int nValues = valueList.getNValues();
        this.setValue(this.masterForm, valueList.getValueNo(0));
        // Add or remove additional fields as needed
        while (this.additionalForms.size() < (nValues - 1)) {
            this.createAdditionalForm();
        }
        while (this.additionalForms.size() > (nValues - 1)) {
            this.removeAddedForm(this.additionalForms.get(0));
        }
        LaunchFormLayout.assertCondition(this.additionalForms.size() == (nValues - 1),
                "Illegal state: form number still doesn't match the number of provided values.");
        // Overwrite additional form values
        for (int valueNo = 1; valueNo < nValues; valueNo++) {
            this.setValue(this.additionalForms.get(valueNo - 1), valueList.getValueNo(valueNo));
        }
        // Check dependencies
        this.onValueChanged();
    }

    /**
     * Overwrite given input field with provided value
     *
     * @param overWrittenForm   DynamicForm representing the input field to overwrite
     * @param value             Object representing the value to write
     */
    protected void setValue(DynamicForm overWrittenForm, Object value) {
        overWrittenForm.getField(MAIN_FIELD_NAME).setValue(value);
    }

    /**
     * @return boolean: true if this input currently displays only one input field (unique value).
     */
    public boolean hasUniqueValue(){
        return (this.additionalForms.size() <= 0);
    }

    /**
     * @return boolean: true if main input field is empty
     */
    public boolean isMasterEmpty(){
        return (ValueList.formValue(this.masterForm) == null);
    }

    /**
     * @return boolean: true if main input field is empty AND this has no additional input field
     * @see #hasUniqueValue()
     * @see #isMasterEmpty()
     */
    public boolean isUniqueEmpty(){
        return this.hasUniqueValue() && this.isMasterEmpty();
    }

    /**
     * @return boolean: true if main input field is non empty AND this has no additional input field
     * @see #hasUniqueValue()
     * @see #isMasterEmpty()
     */
    public boolean isUniqueFilled(){
        return this.hasUniqueValue() && !this.isMasterEmpty();
    }

    /**
     * @return ValueSet representing current values of this input
     */
    public ValueSet getValueList(){
        return new ValueList(this.masterForm, this.additionalForms);
    }

    /**
     * @return Human-readable input name as String
     */
    public String getInputName(){
        return this.input.getName();
    }

    /**
     * @return Unique input ID as String
     */
    public String getInputId(){
        return this.input.getId();
    }

    /**
     * @return boolean: true if this is an optional input
     */
    public boolean isOptional(){
        return this.input.isOptional();
    }

    /**
     * @return Object representing this input's default value
     */
    public Object getDefaultValue(){
        return this.input.getDefaultValue();
    }

    /**
     * @return Set of InputLayouts that need to be non empty for this to be active
     */
    public Set<InputLayout> getRequires() {
        return this.requires;
    }

    /**
     * @return Set of InputLayouts that are disabled when this is empty
     */
    public Set<InputLayout> getRequiredBy() {
        return this.requiredBy;
    }

    /**
     * @return Set of InputLayouts that are disabled when this is non empty
     */
    public Set<InputLayout> getDisables() {
        return this.disables;
    }

    /**
     * @return Set of InputLayouts that need to be empty for this to be active
     */
    public Set<InputLayout> getDisabledBy() {
        return this.disabledBy;
    }

    /**
     * @return Map of InputLayouts that need this to be non empty to allow selection of certain values. Keys are
     * dependant InputLayouts, and values are Sets of Strings representing their dependant values
     */
    public Map<InputLayout, Set<String>> getRequiredByValue() {
        return new HashMap<>(this.requiredByValue);
    }

    /**
     * @return Map of InputLayouts for which certain values disable this. Keys are these InputLayouts, and values are
     * Sets of Strings representing their values disabling this
     */
    public Map<InputLayout, Set<String>> getDisabledByValue() {
        return new HashMap<>(this.disabledByValue);
    }

    /**
     * @return FormItem representing an input field for this input
     */
    protected abstract FormItem getFormItem();
}
