package fr.insalyon.creatis.vip.application.models.boutiquesTools;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Map;
import java.util.Set;

public abstract class BoutiquesInput implements IsSerializable {
    protected String id;
    protected String name;
    protected String description;
    private InputType type;
    protected boolean isOptional;
    protected Set<String> disablesInputsId;
    protected Set<String> requiresInputsId;

    protected Set<String> possibleValues;

    private Map<String, Set<String>> valueDisablesInputsId;
    private Map<String, Set<String>> valueRequiresInputsId;
    private String valueKey;

    private boolean list;
    private String commandLineFlag;
    public enum InputType {
        STRING("String"), FILE("File"), NUMBER("Number"), FLAG("Flag");

        private final String camelName;
        InputType(String camelName) {
            this.camelName = camelName;
        }
        public String getCamelName(){
            return this.camelName;
        }
    }

    public BoutiquesInput() {
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
                          Set<String> disablesInputsId, Set<String> requiresInputsId,
                          Set<String> possibleValues, Map<String, Set<String>> valueDisablesInputsId,
                          Map<String, Set<String>> valueRequiresInputsId){
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
    public Set<String> getDisablesInputsId() {
        return this.disablesInputsId;
    }

    /**
     * @return array of Strings representing IDs of inputs required by this, or null if there is not any
     */
    public Set<String> getRequiresInputsId() {
        return this.requiresInputsId;
    }

    /**
     * @return InputType representing the type of this
     */
    public InputType getType(){
        return this.type;
    }

    /**
     * @return String representing the type of this
     */
    public String getTypeString(){
        return this.type.getCamelName();
    }

    /**
     * @return Map of String values of this to array of String IDs of inputs disabled by corresponding values.
     *         Return value can be null if this has no 'value-disables' dependency
     */
    public Map<String, Set<String>> getValueDisablesInputsId(){
        return this.valueDisablesInputsId;
    }

    /**
     * @return Map of String values of this to array of String IDs of inputs required by corresponding values
     *         Return value can be null if this has no 'value-requires' dependency
     */
    public Map<String, Set<String>> getValueRequiresInputsId(){
        return this.valueRequiresInputsId;
    }

    /**
     * @return Array of Strings representing possible value choices for this input, or null if any value can be entered
     */
    public Set<String> getPossibleValues(){
        return possibleValues;
    }

    /**
     * @return Object representing this input's default value, or null if there is not any
     */
    public abstract Object getDefaultValue();

    public String getValueKey() {
        return valueKey;
    }

    public boolean isList() {
        return list;
    }

    public String getCommandLineFlag() {
        return commandLineFlag;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public void setCommandLineFlag(String commandLineFlag) {
        this.commandLineFlag = commandLineFlag;
    }

    public void setPossibleValues(Set<String> possibleValues) {
        this.possibleValues = possibleValues;
    }


}