package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representation of a non-Flag input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public abstract class BoutiquesInputNonFlag extends BoutiquesInput{
    protected final List<String> possibleValues;
    private final Map<String, List<String>> valueDisablesInputsId;
    private final Map<String, List<String>> valueRequiresInputsId;

    /**
     * Initializes input information from its JSON description
     *
     * @param descriptor JSONObject describing this input
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInputNonFlag(JSONObject descriptor) throws RuntimeException{
        super(descriptor);
        JSONArray possibleValuesArray = BoutiquesUtil.getArrayValue(descriptor, "value-choices", true);
        if(possibleValuesArray == null) {
            this.possibleValues = null;
            this.valueDisablesInputsId = null;
            this.valueRequiresInputsId = null;
        } else {
            this.possibleValues = new ArrayList<>();
            // If input is optional, we add possibility of selecting empty value
            if(this.isOptional){
                this.possibleValues.add(null);
            }
            for (int valueNo = 0; valueNo < possibleValuesArray.size(); valueNo++) {
                String iValue = JSONValueToString(possibleValuesArray.get(valueNo));
                if (iValue == null) {
                    throw new RuntimeException("Invalid Boutiques descriptor: input '" + this.id
                            + "' has invalid value-choices.");
                }
                this.possibleValues.add(iValue);
            }
            this.valueDisablesInputsId = BoutiquesUtil.getStringMapValue(descriptor, "value-disables",
                    true);
            this.valueRequiresInputsId = BoutiquesUtil.getStringMapValue(descriptor, "value-requires",
                    true);
        }
    }

    /**
     * @param value JSONValue to convert
     * @return      String representation of JSONValue, or null if JSONValue is not a valid value for this input
     */
    public abstract String JSONValueToString(JSONValue value);

    /**
     * @return Map of String values of this to array of String IDs of inputs disabled by corresponding values.
     *         Return value can be null if this has no 'value-disables' dependency
     */
    @Override
    public Map<String, List<String>> getValueDisablesInputsId(){
        return this.valueDisablesInputsId;
    }

    /**
     * @return Map of String values of this to array of String IDs of inputs required by corresponding values
     *         Return value can be null if this has no 'value-requires' dependency
     */
    @Override
    public Map<String, List<String>> getValueRequiresInputsId(){
        return this.valueRequiresInputsId;
    }

    /**
     * @return Array of Strings representing possible value choices for this input, or null if any value can be entered
     */
    @Override
    public List<String> getPossibleValues() {
        return this.possibleValues;
    }
}

