package fr.insalyon.creatis.vip.application.client.view.launch;

import com.smartgwt.client.widgets.form.DynamicForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Helper class to handle a range of values entered by user for one input
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class ValueRange extends ValueSet {
    protected final List<Float> rangeLimits; // start, step, end

    /**
     * @param value String to convert to float
     * @return      Float representing value, or null
     */
    public static Float floatValue(String value) throws NumberFormatException{
        return (value == null) ? null : Float.parseFloat(value);
    }

    /**
     * Populate represented list of values from the range represented by masterForm
     *
     * @param masterForm        DynamicForm containing range values (Start, Step and End values)
     * @throws RuntimeException if masterForm does not have three non-empty fields with names
     *                          NumberInputLayout.RangeItem.names
     */
    public ValueRange(DynamicForm masterForm) throws RuntimeException{
        this.rangeLimits = NumberInputLayout.RangeItem.names.stream()
                .map(masterForm::getField)
                .filter(Objects::nonNull)
                .map(item -> NumberInputLayout.valueAsFloat(item.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(this.rangeLimits.size() < 3){
            throw new RuntimeException("Cannot create value range: please ensure provided form has fields with names "
                                       + NumberInputLayout.RangeItem.names + " and numeric values.");
        }
        initializeValues();
    }

    /**
     * Initialized this from start, stop and step values represented as Strings
     *
     * @param start String
     * @param stop String
     * @param step String
     * @throws NumberFormatException if at least one of the values is neither a float nor
     */
    public ValueRange(String start, String stop, String step) throws NumberFormatException{
        this.rangeLimits = new ArrayList<>();
        try{
            this.rangeLimits.add(floatValue(start));
            this.rangeLimits.add(floatValue(step));
            this.rangeLimits.add(floatValue(stop));
        } catch (NumberFormatException exception){
            throw new NumberFormatException("At least one of range limits is not a valid representation of a float " +
                    "or of an empty value.</br>" + "Received range limits: " + start + ", " + step + " and " + stop);
        }
        initializeValues();
    }

    /**
     * Initialize all values from range limits
     */
    private void initializeValues() {
        float previousValue = this.rangeLimits.get(0);
        while(previousValue <= this.rangeLimits.get(2)){
            this.values.add(previousValue);
            this.valuesAsStrings.add(String.valueOf(previousValue));
            previousValue += this.rangeLimits.get(1);
        }
        boolean correctSize = this.values.size() == ((int) ((this.rangeLimits.get(2) - this.rangeLimits.get(0)) / this.rangeLimits.get(1)) + 1);
        LaunchFormLayout.assertCondition(correctSize,
                "Populating range failed: incorrect number of computed values");
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
                List<Float> comparedRange = ((ValueRange) comparedValueSet).getRangeLimits();
                // If start and step are the same, end can differ as long as there is the same number of values.
                return (comparedRange.get(0).equals(this.rangeLimits.get(0))) && (comparedRange.get(1).equals(this.rangeLimits.get(1)));
            }
        }
        return false;
    }

    /**
     * @return Float array containing Start, Step and End value of the range represented by this
     */
    public List<Float> getRangeLimits() {
        return this.rangeLimits;
    }
}