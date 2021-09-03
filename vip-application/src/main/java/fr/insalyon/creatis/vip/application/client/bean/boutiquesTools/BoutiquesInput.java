package fr.insalyon.creatis.vip.application.client.bean.boutiquesTools;

import java.util.List;
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
    private final InputType type;
    protected final boolean isOptional;
    protected final List<String> disablesInputsId;
    protected final List<String> requiresInputsId;
    protected final List<String> possibleValues;
    private final Map<String, List<String>> valueDisablesInputsId;
    private final Map<String, List<String>> valueRequiresInputsId;

    public enum InputType {
        STRING, FILE, NUMBER, FLAG
    }

    /**
     * @param id                    String
     * @param name                  String
     * @param description           String
     * @param type                  InputType
     * @param isOptional            boolean
     * @param disablesInputsId      List of String IDs of inputs disabled when this is non-empty
     * @param requiresInputsId      List of String IDs of inputs requiring this to be non-empty
     * @param possibleValues        List of String values this input can take
     * @param valueDisablesInputsId Map from String values to Lists of String IDs of inputs disabled by corresponding
     *                              value
     * @param valueRequiresInputsId Map from String values to Lists of String IDs of inputs requiring corresponding
     *                              value
     * @throws RuntimeException if descriptor is invalid
     */
    public BoutiquesInput(String id, String name, String description, InputType type, boolean isOptional,
                                 List<String> disablesInputsId, List<String> requiresInputsId,
                                 List<String> possibleValues, Map<String, List<String>> valueDisablesInputsId,
                                 Map<String, List<String>> valueRequiresInputsId) throws RuntimeException{
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.isOptional = isOptional;
        this.disablesInputsId = disablesInputsId;
        this.requiresInputsId = requiresInputsId;
        this.possibleValues = possibleValues;
        this.valueDisablesInputsId = valueDisablesInputsId;
        this.valueRequiresInputsId = valueRequiresInputsId;
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
    public List<String> getDisablesInputsId() {
        return this.disablesInputsId;
    }

    /**
     * @return array of Strings representing IDs of inputs required by this, or null if there is not any
     */
    public List<String> getRequiresInputsId() {
        return this.requiresInputsId;
    }

    /**
     * @return InputType representing the type of this
     */
    public InputType getType(){
        return this.type;
    }

    /**
     * @return Map of String values of this to array of String IDs of inputs disabled by corresponding values.
     *         Return value can be null if this has no 'value-disables' dependency
     */
    public Map<String, List<String>> getValueDisablesInputsId(){
        return this.valueDisablesInputsId;
    }

    /**
     * @return Map of String values of this to array of String IDs of inputs required by corresponding values
     *         Return value can be null if this has no 'value-requires' dependency
     */
    public Map<String, List<String>> getValueRequiresInputsId(){
        return this.valueRequiresInputsId;
    }

    /**
     * @return Array of Strings representing possible value choices for this input, or null if any value can be entered
     */
    public List<String> getPossibleValues(){
        return possibleValues;
    }

    /**
     * @return Object representing this input's default value, or null if there is not any
     */
    public abstract Object getDefaultValue();
}