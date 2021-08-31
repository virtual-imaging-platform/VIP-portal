package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import fr.insalyon.creatis.vip.application.client.view.launch.InputLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchFormLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.NumberInputLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.ValueChoiceInputLayout;

import java.util.List;

/**
 * Representation of a Number input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesInputNumber extends BoutiquesInputNonFlag{
    private final Double defaultValue;
    private final boolean isInteger;
    private final boolean isExclusiveMaximum;
    private final boolean isExclusiveMinimum;
    private final Double maximum;
    private final Double minimum;

    /**
     * Initializes input information from its JSON description
     *
     * @param descriptor JSONObject describing this input
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInputNumber(JSONObject descriptor) throws RuntimeException{
        super(descriptor);
        this.defaultValue = BoutiquesUtil.getDoubleValue(descriptor, "default-value", true);
        this.isInteger = BoutiquesUtil.getBooleanValue(descriptor, "integer", true);
        this.maximum = BoutiquesUtil.getDoubleValue(descriptor, "maximum", true);
        this.minimum = BoutiquesUtil.getDoubleValue(descriptor, "minimum", true);
        this.isExclusiveMaximum = BoutiquesUtil.getBooleanValue(descriptor, "exclusive-maximum", true);
        this.isExclusiveMinimum = BoutiquesUtil.getBooleanValue(descriptor, "exclusive-minimum", true);
    }

    /**
     * @param value JSONValue to convert
     * @return      String representation of JSONValue, or null if JSONValue is not a valid representation of a number
     */
    @Override
    public String JSONValueToString(JSONValue value) {
        Double doubleValue = BoutiquesUtil.JSONToDouble(value);
        if(doubleValue == null){
            return null;
        }
        return doubleValue.toString();
    }

    /**
     * Create an InputLayout representing this input for user interaction
     *
     * @param layout LaunchFormLayout representing application launch form, used as InputLayout's parentLayout
     * @return       InputLayout representing this
     */
    @Override
    public InputLayout getLayout(LaunchFormLayout layout) {
        if(this.possibleValues == null){
            return new NumberInputLayout(this, layout);
        } else {
            return new ValueChoiceInputLayout(this, layout);
        }
    }

    /**
     * @return Double representing this input's default value, or null if there is not any
     */
    public Double getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @return Array of Strings representing possible value choices for this input, or null if any number can be entered
     */
    public List<String> getPossibleValues() {
        return this.possibleValues;
    }

    /**
     * @return Double representing this input's maximum allowed value, or null if there is not any
     */
    public Double getMaximum() {
        return this.maximum;
    }

    /**
     * @return Double representing this input's minimum allowed value, or null if there is not any
     */
    public Double getMinimum() {
        return this.minimum;
    }

    /**
     * @return boolean: true if maximum allowed value for this is exclusive, false otherwise
     */
    public boolean isExclusiveMaximum() {
        return this.isExclusiveMaximum;
    }

    /**
     * @return boolean: true if minimum allowed value for this is exclusive, false otherwise
     */
    public boolean isExclusiveMinimum() {
        return this.isExclusiveMinimum;
    }

    /**
     * @return boolean: true if this accepts only integer values, false otherwise
     */
    public boolean isInteger() {
        return this.isInteger;
    }
}
