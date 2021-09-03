package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import java.util.List;
import java.util.Map;

/**
 * Representation of a Number input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesInputNumber extends BoutiquesInput{
    private final Double defaultValue;
    private final boolean isInteger;
    private final boolean isExclusiveMaximum;
    private final boolean isExclusiveMinimum;
    private final Double maximum;
    private final Double minimum;

    /**
     * @param id                    String
     * @param name                  String
     * @param description           String
     * @param isOptional            boolean
     * @param disablesInputsId      List of String IDs of inputs disabled when this is non-empty
     * @param requiresInputsId      List of String IDs of inputs requiring this to be non-empty
     * @param possibleValues        List of String values this input can take
     * @param valueDisablesInputsId Map from String values to Lists of String IDs of inputs disabled by corresponding
     *                              value
     * @param valueRequiresInputsId Map from String values to Lists of String IDs of inputs requiring corresponding
     *                              value
     * @param defaultValue          Double
     * @param isInteger             boolean: true if this input accepts only integer values
     * @param isExclusiveMaximum    boolean: true if maximum is exclusive
     * @param isExclusiveMinimum    boolean: true if maximum is exclusive
     * @param maximum               Double: maximum allowed value
     * @param minimum               Double: minimum allowed value
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInputNumber(String id, String name, String description, boolean isOptional,
                                List<String> disablesInputsId, List<String> requiresInputsId,
                                List<String> possibleValues, Map<String, List<String>> valueDisablesInputsId,
                                Map<String, List<String>> valueRequiresInputsId, Double defaultValue,
                                boolean isInteger, boolean isExclusiveMaximum, boolean isExclusiveMinimum,
                                Double maximum, Double minimum) throws RuntimeException{
        super(id, name, description, InputType.NUMBER, isOptional, disablesInputsId, requiresInputsId, possibleValues,
                valueDisablesInputsId, valueRequiresInputsId);
        this.defaultValue = defaultValue;
        this.isInteger = isInteger;
        this.isExclusiveMaximum = isExclusiveMaximum;
        this.isExclusiveMinimum = isExclusiveMinimum;
        this.maximum = maximum;
        this.minimum = minimum;
    }

    /**
     * @return Double representing this input's default value, or null if there is not any
     */
    public Double getDefaultValue() {
        return this.defaultValue;
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
