package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

import com.google.gwt.json.client.JSONObject;
import fr.insalyon.creatis.vip.application.client.view.launch.InputLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchFormLayout;

import java.util.Map;

/**
 * Representation of an input in an application Boutiques descriptor
 *
 * @author Guillaume Vanel
 * @version %I%, %G%
 */
public abstract class BoutiquesInput {
    protected final String id;
    protected final String name;
    protected final String description;
    protected final boolean isOptional;
    protected final String[] disablesInputsId;
    protected final String[] requiresInputsId;

    /**
     * Initializes input information from its JSON description
     *
     * @param descriptor JSONObject describing this input
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInput(JSONObject descriptor) throws RuntimeException{
        this.id = BoutiquesUtil.getStringValue(descriptor, "id");
        this.name = BoutiquesUtil.getStringValue(descriptor, "name");
        this.description = BoutiquesUtil.getStringValue(descriptor, "description", true);
        this.isOptional = BoutiquesUtil.getBooleanValue(descriptor, "optional", true);
        this.disablesInputsId = BoutiquesUtil.getArrayValueAsStrings(descriptor, "disables-inputs", true);
        this.requiresInputsId = BoutiquesUtil.getArrayValueAsStrings(descriptor, "requires-inputs", true);
    }

    /**
     * @return Input name as String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Input ID as String
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return Input description as String, or null if there is no description
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * @return boolean: true id this is an optional input, else otherwise
     */
    public boolean isOptional() {
        return this.isOptional;
    }

    /**
     * @return array of Strings representing IDs of inputs disabled by this, or null if there is not any
     */
    public String[] getDisablesInputsId() {
        return this.disablesInputsId;
    }

    /**
     * @return array of Strings representing IDs of inputs required by this, or null if there is not any
     */
    public String[] getRequiresInputsId() {
        return this.requiresInputsId;
    }

    /**
     * Create an InputLayout representing this input for user interaction
     *
     * @param layout LaunchFormLayout representing application launch form, used as InputLayout's parentLayout
     * @return       InputLayout representing this
     */
    public abstract InputLayout getLayout(LaunchFormLayout layout);

    /**
     * @return Map of String values of this to array of String IDs of inputs disabled by corresponding values.
     *         Return value can be null if this has no 'value-disables' dependency
     */
    public abstract Map<String, String[]> getValueDisablesInputsId();

    /**
     * @return Map of String values of this to array of String IDs of inputs required by corresponding values
     *         Return value can be null if this has no 'value-requires' dependency
     */
    public abstract Map<String, String[]> getValueRequiresInputsId();

    /**
     * @return Array of Strings representing possible value choices for this input, or null if any value can be entered
     */
    public abstract String[] getPossibleValues();

    /**
     * @return Object representing this input's default value, or null if there is not any
     */
    public abstract Object getDefaultValue();
}