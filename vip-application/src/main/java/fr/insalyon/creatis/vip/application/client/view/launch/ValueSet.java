package fr.insalyon.creatis.vip.application.client.view.launch;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to handle sets of values entered by user for one input
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public abstract class ValueSet {
    protected final List<Object> values = new ArrayList<>();
    protected final List<String> valuesAsStrings = new ArrayList<>();

    /**
     * 
     * @param value Object value to convert to String
     * @return      String representation of value.
     */
    protected String valueAsString(Object value) {
        return (value == null) ? null : value.toString();
    }

    /**
     * @return String representation of this
     */
    @Override
    public String toString() {
        return "ValueSet{" + this.valuesAsStrings + '}';
    }

    /**
     * @param index int
     * @return      Object representing value at provided index
     * @throws IndexOutOfBoundsException if index is not strictly positive or not less than the number of values in this
     * @see #getNValues()
     */
    public Object getValueNo(int index) throws IndexOutOfBoundsException{
        return this.values.get(index);
    }

    /**
     * @param index int
     * @return      String representing value at provided index
     * @throws IndexOutOfBoundsException if index is not strictly positive or not less than the number of values in this
     * @see #getValueNo(int) 
     * @see #valueAsString(Object) 
     */
    public String getStringValueNo(int index) throws IndexOutOfBoundsException{
        return this.valuesAsStrings.get(index);
    }

    /**
     * @return int number of values in this
     * @see #getValueNo(int) 
     */
    public int getNValues(){
        return this.values.size();
    }

    /**
     * @return List of String representation of values contained in this.
     */
    public List<String> getValuesAsStrings() {
        return this.valuesAsStrings;
    }

    /**
     * @param comparedValueSet ValueSet to compare this to
     * @return                 boolean: true if comparedValueSet represents the same input values as this. Comparison
     *                         is based on String representation of values. Return value is always false if
     *                         comparedValueSet is null
     * @see #valuesAsStrings
     */
    public abstract boolean isEqualTo(ValueSet comparedValueSet);

    /**
     * @param stringValues String to convert to a ValueSet
     * @return             ValueSet representing the same values
     */
    public static ValueSet valueSetFactory(String stringValues){
        if ((stringValues != null) && stringValues.contains("Start: ")) { // Range
            String[] v = stringValues.split("(: | - )");
            return new ValueRange(v[1].trim(), v[3].trim(), v[5].trim());
        } else { // List
            return new ValueList(stringValues, "; ");
        }
    }
}