package fr.insalyon.creatis.vip.application.client.view.launch.boutiquesParsing;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout.InputLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout.LaunchFormLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout.StringInputLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.applicationLayout.ValueChoiceInputLayout;

/**
 * Representation of a String or File input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public class BoutiquesInputString extends BoutiquesInputNonFlag{
    private final String type;
    private final String defaultValue;

    /**
     * Initializes input information from its JSON description
     *
     * @param descriptor JSONObject describing this input
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInputString(JSONObject descriptor, String type) throws RuntimeException{
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
     * @return Array of Strings representing possible value choices for this input, or null if any value can be entered
     */
    @Override
    public String[] getPossibleValues() {
        return this.possibleValues;
    }

    /**
     * Create an InputLayout representing this input for user interaction
     *
     * @param layout LaunchFormLayout representing application launch form, used as InputLayout's parentLayout
     * @return       InputLayout representing this
     */
    @Override
    public InputLayout getLayout(LaunchFormLayout layout) {
        return this.getLayout(layout, true);
    }

    /**
     * Create an InputLayout representing this input for user interaction
     *
     * @param layout LaunchFormLayout representing application launch form, used as InputLayout's parentLayout
     * @param hasAddValueButton boolean: true if input should have a "add value" button, false otherwise. This value
     *                          is ignored if this is a value choice input
     * @return       InputLayout representing this
     */
    public InputLayout getLayout(LaunchFormLayout layout, boolean hasAddValueButton) {
        if(this.possibleValues == null){
            return new StringInputLayout(this, layout, hasAddValueButton);
        } else {
            return new ValueChoiceInputLayout(this, layout);
        }
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
    public String getType(){
        return this.type;
    }
}