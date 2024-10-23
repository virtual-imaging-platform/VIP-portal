
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "type",
    "description",
    "value-key",
    "list",
    "list-separator",
    "optional",
    "command-line-flag",
    "requires-inputs",
    "disables-inputs",
    "command-line-flag-separator",
    "default-value",
    "value-choices",
    "value-requires",
    "value-disables",
    "integer",
    "minimum",
    "maximum",
    "exclusive-minimum",
    "exclusive-maximum",
    "min-list-entries",
    "max-list-entries",
    "uses-absolute-path"
})
@Generated("jsonschema2pojo")
public class Input {

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "data_file".
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: \"data_file\".")
    private String id;
    /**
     * A human-readable input name. Example: 'Data file'.
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("A human-readable input name. Example: 'Data file'.")
    private String name;
    /**
     * Input type.
     * (Required)
     * 
     */
    @JsonProperty("type")
    @JsonPropertyDescription("Input type.")
    private Input.Type type;
    /**
     * Input description.
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Input description.")
    private String description;
    /**
     * A string contained in command-line, substituted by the input value and/or flag at runtime.
     * 
     */
    @JsonProperty("value-key")
    @JsonPropertyDescription("A string contained in command-line, substituted by the input value and/or flag at runtime.")
    private String valueKey;
    /**
     * True if input is a list of value. An input of type "Flag" cannot be a list.
     * 
     */
    @JsonProperty(value = "list")
    @JsonPropertyDescription("True if input is a list of value. An input of type \"Flag\" cannot be a list.")
    private Boolean list;
    /**
     * Separator used between list items. Defaults to a single space.
     * 
     */
    @JsonProperty("list-separator")
    @JsonPropertyDescription("Separator used between list items. Defaults to a single space.")
    private String listSeparator;
    /**
     * True if input is optional.
     * 
     */
    @JsonProperty("optional")
    @JsonPropertyDescription("True if input is optional.")
    private Boolean optional;
    /**
     * Option flag of the input, involved in the value-key substitution. Inputs of type "Flag" have to have a command-line flag. Examples: -v, --force.
     * 
     */
    @JsonProperty("command-line-flag")
    @JsonPropertyDescription("Option flag of the input, involved in the value-key substitution. Inputs of type \"Flag\" have to have a command-line flag. Examples: -v, --force.")
    private String commandLineFlag;
    /**
     * Ids of the inputs or ids of groups whose members must be active for this input to be available.
     * 
     */
    @JsonProperty("requires-inputs")
    @JsonPropertyDescription("Ids of the inputs or ids of groups whose members must be active for this input to be available.")
    private List<String> requiresInputs = new ArrayList<String>();
    /**
     * Ids of the inputs that are disabled when this input is active.
     * 
     */
    @JsonProperty("disables-inputs")
    @JsonPropertyDescription("Ids of the inputs that are disabled when this input is active.")
    private List<String> disablesInputs = new ArrayList<String>();
    /**
     * Separator used between flags and their arguments. Defaults to a single space.
     * 
     */
    @JsonProperty("command-line-flag-separator")
    @JsonPropertyDescription("Separator used between flags and their arguments. Defaults to a single space.")
    private String commandLineFlagSeparator;
    /**
     * Default value of the input. The default value is set when no value is specified, even when the input is optional. If the desired behavior is to omit the input from the command line when no value is specified, then no default value should be used. In this case, the tool might still use a default value internally, but this will remain undocumented in the Boutiques interface.
     * 
     */
    @JsonProperty("default-value")
    @JsonPropertyDescription("Default value of the input. The default value is set when no value is specified, even when the input is optional. If the desired behavior is to omit the input from the command line when no value is specified, then no default value should be used. In this case, the tool might still use a default value internally, but this will remain undocumented in the Boutiques interface.")
    private Object defaultValue;
    /**
     * Permitted choices for input value. May not be used with the Flag type.
     * 
     */
    @JsonProperty("value-choices")
    @JsonPropertyDescription("Permitted choices for input value. May not be used with the Flag type.")
    private List<Object> valueChoices = new ArrayList<Object>();
    /**
     * Ids of the inputs that are required when the corresponding value choice is selected.
     * 
     */
    @JsonProperty("value-requires")
    @JsonPropertyDescription("Ids of the inputs that are required when the corresponding value choice is selected.")
    private ValueRequires valueRequires;
    /**
     * Ids of the inputs that are disabled when the corresponding value choice is selected.
     * 
     */
    @JsonProperty("value-disables")
    @JsonPropertyDescription("Ids of the inputs that are disabled when the corresponding value choice is selected.")
    private ValueDisables valueDisables;
    /**
     * Specify whether the input should be an integer. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("integer")
    @JsonPropertyDescription("Specify whether the input should be an integer. May only be used with Number type inputs.")
    private Boolean integer;
    /**
     * Specify the minimum value of the input (inclusive). May only be used with Number type inputs.
     * 
     */
    @JsonProperty("minimum")
    @JsonPropertyDescription("Specify the minimum value of the input (inclusive). May only be used with Number type inputs.")
    private Double minimum;
    /**
     * Specify the maximum value of the input (inclusive). May only be used with Number type inputs.
     * 
     */
    @JsonProperty("maximum")
    @JsonPropertyDescription("Specify the maximum value of the input (inclusive). May only be used with Number type inputs.")
    private Double maximum;
    /**
     * Specify whether the minimum is exclusive or not. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("exclusive-minimum")
    @JsonPropertyDescription("Specify whether the minimum is exclusive or not. May only be used with Number type inputs.")
    private Boolean exclusiveMinimum;
    /**
     * Specify whether the maximum is exclusive or not. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("exclusive-maximum")
    @JsonPropertyDescription("Specify whether the maximum is exclusive or not. May only be used with Number type inputs.")
    private Boolean exclusiveMaximum;
    /**
     * Specify the minimum number of entries in the list. May only be used with List type inputs.
     * 
     */
    @JsonProperty("min-list-entries")
    @JsonPropertyDescription("Specify the minimum number of entries in the list. May only be used with List type inputs.")
    private Double minListEntries;
    /**
     * Specify the maximum number of entries in the list. May only be used with List type inputs.
     * 
     */
    @JsonProperty("max-list-entries")
    @JsonPropertyDescription("Specify the maximum number of entries in the list. May only be used with List type inputs.")
    private Double maxListEntries;
    /**
     * Specifies that this input must be given as an absolute path. Only specifiable for File type inputs.
     * 
     */
    @JsonProperty("uses-absolute-path")
    @JsonPropertyDescription("Specifies that this input must be given as an absolute path. Only specifiable for File type inputs.")
    private Boolean usesAbsolutePath;

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "data_file".
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "data_file".
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * A human-readable input name. Example: 'Data file'.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * A human-readable input name. Example: 'Data file'.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Input type.
     * (Required)
     * 
     */
    @JsonProperty("type")
    public Input.Type getType() {
        return type;
    }

    /**
     * Input type.
     * (Required)
     * 
     */
    @JsonProperty("type")
    public void setType(Input.Type type) {
        this.type = type;
    }

    /**
     * Input description.
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Input description.
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * A string contained in command-line, substituted by the input value and/or flag at runtime.
     * 
     */
    @JsonProperty("value-key")
    public String getValueKey() {
        return valueKey;
    }

    /**
     * A string contained in command-line, substituted by the input value and/or flag at runtime.
     * 
     */
    @JsonProperty("value-key")
    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    /**
     * True if input is a list of value. An input of type "Flag" cannot be a list.
     * 
     */
    @JsonProperty("list")
    public Boolean getList() {
        return list;
    }

    /**
     * True if input is a list of value. An input of type "Flag" cannot be a list.
     * 
     */
    @JsonProperty("list")
    public void setList(Boolean list) {
        this.list = list;
    }

    /**
     * Separator used between list items. Defaults to a single space.
     * 
     */
    @JsonProperty("list-separator")
    public String getListSeparator() {
        return listSeparator;
    }

    /**
     * Separator used between list items. Defaults to a single space.
     * 
     */
    @JsonProperty("list-separator")
    public void setListSeparator(String listSeparator) {
        this.listSeparator = listSeparator;
    }

    /**
     * True if input is optional.
     * 
     */
    @JsonProperty("optional")
    public Boolean getOptional() {
        return optional;
    }

    /**
     * True if input is optional.
     * 
     */
    @JsonProperty("optional")
    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    /**
     * Option flag of the input, involved in the value-key substitution. Inputs of type "Flag" have to have a command-line flag. Examples: -v, --force.
     * 
     */
    @JsonProperty("command-line-flag")
    public String getCommandLineFlag() {
        return commandLineFlag;
    }

    /**
     * Option flag of the input, involved in the value-key substitution. Inputs of type "Flag" have to have a command-line flag. Examples: -v, --force.
     * 
     */
    @JsonProperty("command-line-flag")
    public void setCommandLineFlag(String commandLineFlag) {
        this.commandLineFlag = commandLineFlag;
    }

    /**
     * Ids of the inputs or ids of groups whose members must be active for this input to be available.
     * 
     */
    @JsonProperty("requires-inputs")
    public List<String> getRequiresInputs() {
        return requiresInputs;
    }

    /**
     * Ids of the inputs or ids of groups whose members must be active for this input to be available.
     * 
     */
    @JsonProperty("requires-inputs")
    public void setRequiresInputs(List<String> requiresInputs) {
        this.requiresInputs = requiresInputs;
    }

    /**
     * Ids of the inputs that are disabled when this input is active.
     * 
     */
    @JsonProperty("disables-inputs")
    public List<String> getDisablesInputs() {
        return disablesInputs;
    }

    /**
     * Ids of the inputs that are disabled when this input is active.
     * 
     */
    @JsonProperty("disables-inputs")
    public void setDisablesInputs(List<String> disablesInputs) {
        this.disablesInputs = disablesInputs;
    }

    /**
     * Separator used between flags and their arguments. Defaults to a single space.
     * 
     */
    @JsonProperty("command-line-flag-separator")
    public String getCommandLineFlagSeparator() {
        return commandLineFlagSeparator;
    }

    /**
     * Separator used between flags and their arguments. Defaults to a single space.
     * 
     */
    @JsonProperty("command-line-flag-separator")
    public void setCommandLineFlagSeparator(String commandLineFlagSeparator) {
        this.commandLineFlagSeparator = commandLineFlagSeparator;
    }

    /**
     * Default value of the input. The default value is set when no value is specified, even when the input is optional. If the desired behavior is to omit the input from the command line when no value is specified, then no default value should be used. In this case, the tool might still use a default value internally, but this will remain undocumented in the Boutiques interface.
     * 
     */
    @JsonProperty("default-value")
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Default value of the input. The default value is set when no value is specified, even when the input is optional. If the desired behavior is to omit the input from the command line when no value is specified, then no default value should be used. In this case, the tool might still use a default value internally, but this will remain undocumented in the Boutiques interface.
     * 
     */
    @JsonProperty("default-value")
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Permitted choices for input value. May not be used with the Flag type.
     * 
     */
    @JsonProperty("value-choices")
    public List<Object> getValueChoices() {
        return valueChoices;
    }

    /**
     * Permitted choices for input value. May not be used with the Flag type.
     * 
     */
    @JsonProperty("value-choices")
    public void setValueChoices(List<Object> valueChoices) {
        this.valueChoices = valueChoices;
    }

    /**
     * Ids of the inputs that are required when the corresponding value choice is selected.
     * 
     */
    @JsonProperty("value-requires")
    public ValueRequires getValueRequires() {
        return valueRequires;
    }

    /**
     * Ids of the inputs that are required when the corresponding value choice is selected.
     * 
     */
    @JsonProperty("value-requires")
    public void setValueRequires(ValueRequires valueRequires) {
        this.valueRequires = valueRequires;
    }

    /**
     * Ids of the inputs that are disabled when the corresponding value choice is selected.
     * 
     */
    @JsonProperty("value-disables")
    public ValueDisables getValueDisables() {
        return valueDisables;
    }

    /**
     * Ids of the inputs that are disabled when the corresponding value choice is selected.
     * 
     */
    @JsonProperty("value-disables")
    public void setValueDisables(ValueDisables valueDisables) {
        this.valueDisables = valueDisables;
    }

    /**
     * Specify whether the input should be an integer. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("integer")
    public Boolean getInteger() {
        return integer;
    }

    /**
     * Specify whether the input should be an integer. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("integer")
    public void setInteger(Boolean integer) {
        this.integer = integer;
    }

    /**
     * Specify the minimum value of the input (inclusive). May only be used with Number type inputs.
     * 
     */
    @JsonProperty("minimum")
    public Double getMinimum() {
        return minimum;
    }

    /**
     * Specify the minimum value of the input (inclusive). May only be used with Number type inputs.
     * 
     */
    @JsonProperty("minimum")
    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    /**
     * Specify the maximum value of the input (inclusive). May only be used with Number type inputs.
     * 
     */
    @JsonProperty("maximum")
    public Double getMaximum() {
        return maximum;
    }

    /**
     * Specify the maximum value of the input (inclusive). May only be used with Number type inputs.
     * 
     */
    @JsonProperty("maximum")
    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    /**
     * Specify whether the minimum is exclusive or not. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("exclusive-minimum")
    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    /**
     * Specify whether the minimum is exclusive or not. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("exclusive-minimum")
    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    /**
     * Specify whether the maximum is exclusive or not. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("exclusive-maximum")
    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    /**
     * Specify whether the maximum is exclusive or not. May only be used with Number type inputs.
     * 
     */
    @JsonProperty("exclusive-maximum")
    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    /**
     * Specify the minimum number of entries in the list. May only be used with List type inputs.
     * 
     */
    @JsonProperty("min-list-entries")
    public Double getMinListEntries() {
        return minListEntries;
    }

    /**
     * Specify the minimum number of entries in the list. May only be used with List type inputs.
     * 
     */
    @JsonProperty("min-list-entries")
    public void setMinListEntries(Double minListEntries) {
        this.minListEntries = minListEntries;
    }

    /**
     * Specify the maximum number of entries in the list. May only be used with List type inputs.
     * 
     */
    @JsonProperty("max-list-entries")
    public Double getMaxListEntries() {
        return maxListEntries;
    }

    /**
     * Specify the maximum number of entries in the list. May only be used with List type inputs.
     * 
     */
    @JsonProperty("max-list-entries")
    public void setMaxListEntries(Double maxListEntries) {
        this.maxListEntries = maxListEntries;
    }

    /**
     * Specifies that this input must be given as an absolute path. Only specifiable for File type inputs.
     * 
     */
    @JsonProperty("uses-absolute-path")
    public Boolean getUsesAbsolutePath() {
        return usesAbsolutePath;
    }

    /**
     * Specifies that this input must be given as an absolute path. Only specifiable for File type inputs.
     * 
     */
    @JsonProperty("uses-absolute-path")
    public void setUsesAbsolutePath(Boolean usesAbsolutePath) {
        this.usesAbsolutePath = usesAbsolutePath;
    }


    /**
     * Input type.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Type {

        STRING("String"),
        FILE("File"),
        FLAG("Flag"),
        NUMBER("Number");
        private final String value;
        private final static Map<String, Input.Type> CONSTANTS = new HashMap<String, Input.Type>();

        static {
            for (Input.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Input.Type fromValue(String value) {
            Input.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
