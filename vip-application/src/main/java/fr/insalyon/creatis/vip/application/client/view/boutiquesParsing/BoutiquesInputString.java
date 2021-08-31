package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import java.util.List;

/**
 * Representation of a String or File input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesInputString extends BoutiquesInputNonFlag{
    private final InputType type;
    private final String defaultValue;

    /**
     * Initializes input information from its JSON description
     *
     * @param descriptor JSONObject describing this input
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInputString(JSONObject descriptor, InputType type) throws RuntimeException{
        super(descriptor);
        this.defaultValue = BoutiquesUtil.getStringValue(descriptor, "default-value", true);
        this.type = type;
    }

    /**
     * @param value JSONValue to convert
     * @return      String representation of JSONValue, or null if JSONValue is not a valid representation of a String
     */
    @Override
    public String JSONValueToString(JSONValue value) {
        JSONString stringValue = value.isString();
        if(stringValue != null){
            return stringValue.stringValue();
        }
        return null;
    }

    /**
     * @return String representing this input's default value, or null if there is not any
     */
    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @return Input type as String
     */
    public InputType getType(){
        return this.type;
    }
}