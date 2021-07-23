package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.form.DynamicForm;

import java.util.Arrays;
import java.util.Objects;

/**
 * Helper class to handle a range of values entered by user for one input
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class ValueRange extends ValueSet {
    protected final Float[] rangeLimits; // start, step, end

    /**
     * Populate represented list of values from the range represented by masterForm
     *
     * @param masterForm        DynamicForm containing range values (Start, Step and End values)
     * @throws RuntimeException if masterForm does not have three non-empty fields with names
     *                          NumberInputLayout.RangeItem.names
     * @see NumberInputLayout.RangeItem#names
     */
    public ValueRange(DynamicForm masterForm) throws RuntimeException{
        this.rangeLimits = Arrays.stream(NumberInputLayout.RangeItem.names)
                .map(masterForm::getField)
                .filter(Objects::nonNull)
                .map(item -> NumberInputLayout.valueAsFloat(item.getValue()))
                .filter(Objects::nonNull)
                .toArray(Float[]::new);
        if(this.rangeLimits.length < 3){
            throw new RuntimeException("Cannot create value range: please ensure provided form has fields with names "
                                       + Arrays.toString(NumberInputLayout.RangeItem.names) + " and numeric values.");
        }
        float previousValue = this.rangeLimits[0];
        while(previousValue <= this.rangeLimits[2]){
            this.values.add(previousValue);
            this.valuesAsStrings.add(String.valueOf(previousValue));
            previousValue += this.rangeLimits[1];
        }
        assert this.values.size() == ((int) ((this.rangeLimits[2] - this.rangeLimits[0]) / this.rangeLimits[1]) + 1);
    }

    /**
     * @param comparedValueSet ValueSet to compare this to
     * @return                 boolean: true if comparedValueSet represents the same value range as this. Return value
     *                         is always false if comparedValueSet is not a non-null ValueRange. Return value can be
     *                         true even if comparedValueSet has a slightly different range end value, as long as all
     *                         values inside the range are equal to those in this
     */
    @Override
    public boolean isEqualTo(ValueSet comparedValueSet) {
        if (comparedValueSet instanceof ValueRange) {
            if (comparedValueSet.getNValues() == this.getNValues()) {
                Float[] comparedRange = ((ValueRange) comparedValueSet).getRangeLimits();
                // If start and step are the same, end can differ as long as there is the same number of values.
                return (comparedRange[0].equals(this.rangeLimits[0])) & (comparedRange[1].equals(this.rangeLimits[1]));
            }
        }
        return false;
    }

    /**
     * @return Float array containing Start, Step and End value of the range represented by this
     */
    public Float[] getRangeLimits() {
        return this.rangeLimits;
    }
}