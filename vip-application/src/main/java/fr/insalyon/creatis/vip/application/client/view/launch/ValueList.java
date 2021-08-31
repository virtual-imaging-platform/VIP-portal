package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;

import java.util.ArrayList;

/**
 * Helper class to handle a list of values entered by user for one input
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class ValueList extends ValueSet{
    /**
     * Return the value of form's field named InputLayout.MAIN_FIELD_NAME
     *
     * @param form DynamicForm of interest
     * @return     Object value of form's value field
     * @throws IllegalArgumentException if form has no field named InputLayout.MAIN_FIELD_NAME
     * @see InputLayout#MAIN_FIELD_NAME
     */
    public static Object formValue(DynamicForm form) throws IllegalArgumentException{
        FormItem valueField = form.getField(InputLayout.MAIN_FIELD_NAME);
        if(valueField == null){
            throw new IllegalArgumentException("Provided form has no field with name " + InputLayout.MAIN_FIELD_NAME);
        }
        if(valueField.isDisabled()){
            // Disable field is considered empty
            if(valueField instanceof CheckboxItem){
                return false;
            }
            return null;
        }
        return valueField.getValue();
    }

    /**
     * Populate represented list of values with values from masterFrom and additionalForms
     *
     * @param masterForm        DynamicForm containing main input value
     * @param additionalForms   ArrayList of DynamicForms containing additional input value
     * @throws IllegalArgumentException if at least one form has no field named InputLayout.MAIN_FIELD_NAME
     * @see #formValue(DynamicForm)
     * @see InputLayout#MAIN_FIELD_NAME
     */
    public ValueList(DynamicForm masterForm, ArrayList<DynamicForm> additionalForms)
            throws IllegalArgumentException{
        Object masterValue = formValue(masterForm);
        this.values.add(masterValue);
        this.valuesAsStrings.add(valueAsString(masterValue));
        for(DynamicForm iForm : additionalForms){
            Object iValue = formValue(iForm);
            this.values.add(iValue);
            this.valuesAsStrings.add(valueAsString(iValue));
        }
    }

    /**
     * Initializes this from a String with values separated with given separator
     *
     * @param stringValues String to initialize this from
     * @param separator    String
     */
    public ValueList(String stringValues, String separator){
        if(stringValues == null){
            this.values.add(null);
            this.valuesAsStrings.add(valueAsString(null));
            return;
        }
        for(String value : stringValues.split(separator)){
            this.values.add(value.equals(valueAsString(null)) ? null : value.trim());
            this.valuesAsStrings.add(value.trim());
        }
    }

    /**
     * @param comparedValueSet ValueSet to compare this to
     * @return                 boolean: true if comparedValueSet represents the same input values as this. Comparison
     *                         is based on String representation of values. Return value is always false if
     *                         comparedValueSet is not a non-null ValueList
     * @see #valuesAsStrings
     */
    @Override
    public boolean isEqualTo(ValueSet comparedValueSet) {
        if(comparedValueSet instanceof ValueList) {
            int nValues = this.getNValues();
            if(comparedValueSet.getNValues() == nValues){
                for(int valueNo = 0; valueNo < nValues; valueNo ++){
                    if(!comparedValueSet.getStringValueNo(valueNo).equals(this.getStringValueNo(valueNo))){
                        return false;
                    }
                }
                // Same number of values and all values are equal
                return true;
            }
        }
        // Not a non-null ValueList of same size
        return false;
    }
}