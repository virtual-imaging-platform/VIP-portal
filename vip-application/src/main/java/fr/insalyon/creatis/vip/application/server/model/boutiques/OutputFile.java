
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "description",
    "value-key",
    "path-template",
    "conditional-path-template",
    "path-template-stripped-extensions",
    "list",
    "optional",
    "command-line-flag",
    "command-line-flag-separator",
    "uses-absolute-path",
    "file-template"
})
@Generated("jsonschema2pojo")
public class OutputFile {

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "data_file"
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: \"data_file\"")
    private String id;
    /**
     * A human-readable output name. Example: 'Data file'
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("A human-readable output name. Example: 'Data file'")
    private String name;
    /**
     * Output description.
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Output description.")
    private String description;
    /**
     * A string contained in command-line, substituted by the output value and/or flag at runtime.
     * 
     */
    @JsonProperty("value-key")
    @JsonPropertyDescription("A string contained in command-line, substituted by the output value and/or flag at runtime.")
    private String valueKey;
    /**
     * Describes the output file path relatively to the execution directory. May contain input value keys and wildcards. Example: "results/[INPUT1]_brain*.mnc".
     * 
     */
    @JsonProperty("path-template")
    @JsonPropertyDescription("Describes the output file path relatively to the execution directory. May contain input value keys and wildcards. Example: \"results/[INPUT1]_brain*.mnc\".")
    private String pathTemplate;
    /**
     * List of objects containing boolean statement (Limited python syntax: ==, !=, <, >, <=, >=, and, or) and output file paths relative to the execution directory, assign path of first true boolean statement. May contain input value keys, "default" object required if "optional" set to True . Example list: "[{"[PARAM1] > 8": "outputs/[INPUT1].txt"}, {"default": "outputs/default.txt"}]".
     * 
     */
    @JsonProperty("conditional-path-template")
    @JsonPropertyDescription("List of objects containing boolean statement (Limited python syntax: ==, !=, <, >, <=, >=, and, or) and output file paths relative to the execution directory, assign path of first true boolean statement. May contain input value keys, \"default\" object required if \"optional\" set to True . Example list: \"[{\"[PARAM1] > 8\": \"outputs/[INPUT1].txt\"}, {\"default\": \"outputs/default.txt\"}]\".")
    private List<ConditionalPathTemplate> conditionalPathTemplate = new ArrayList<ConditionalPathTemplate>();
    /**
     * List of file extensions that will be stripped from the input values before being substituted in the path template. Example: [".nii",".nii.gz"].
     * 
     */
    @JsonProperty("path-template-stripped-extensions")
    @JsonPropertyDescription("List of file extensions that will be stripped from the input values before being substituted in the path template. Example: [\".nii\",\".nii.gz\"].")
    private List<String> pathTemplateStrippedExtensions = new ArrayList<String>();
    /**
     * True if output is a list of value.
     * 
     */
    @JsonProperty("list")
    @JsonPropertyDescription("True if output is a list of value.")
    private Boolean list;
    /**
     * True if output may not be produced by the tool.
     * 
     */
    @JsonProperty("optional")
    @JsonPropertyDescription("True if output may not be produced by the tool.")
    private Boolean optional;
    /**
     * Option flag of the output, involved in the value-key substitution. Examples: -o, --output
     * 
     */
    @JsonProperty("command-line-flag")
    @JsonPropertyDescription("Option flag of the output, involved in the value-key substitution. Examples: -o, --output")
    private String commandLineFlag;
    /**
     * Separator used between flags and their arguments. Defaults to a single space.
     * 
     */
    @JsonProperty("command-line-flag-separator")
    @JsonPropertyDescription("Separator used between flags and their arguments. Defaults to a single space.")
    private String commandLineFlagSeparator;
    /**
     * Specifies that this output filepath will be given as an absolute path.
     * 
     */
    @JsonProperty("uses-absolute-path")
    @JsonPropertyDescription("Specifies that this output filepath will be given as an absolute path.")
    private Boolean usesAbsolutePath;
    /**
     * An array of strings that may contain value keys. Each item will be a line in the configuration file.
     * 
     */
    @JsonProperty("file-template")
    @JsonPropertyDescription("An array of strings that may contain value keys. Each item will be a line in the configuration file.")
    private List<String> fileTemplate = new ArrayList<String>();

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "data_file"
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * A short, unique, informative identifier containing only alphanumeric characters and underscores. Typically used to generate variable names. Example: "data_file"
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * A human-readable output name. Example: 'Data file'
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * A human-readable output name. Example: 'Data file'
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Output description.
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Output description.
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * A string contained in command-line, substituted by the output value and/or flag at runtime.
     * 
     */
    @JsonProperty("value-key")
    public String getValueKey() {
        return valueKey;
    }

    /**
     * A string contained in command-line, substituted by the output value and/or flag at runtime.
     * 
     */
    @JsonProperty("value-key")
    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    /**
     * Describes the output file path relatively to the execution directory. May contain input value keys and wildcards. Example: "results/[INPUT1]_brain*.mnc".
     * 
     */
    @JsonProperty("path-template")
    public String getPathTemplate() {
        return pathTemplate;
    }

    /**
     * Describes the output file path relatively to the execution directory. May contain input value keys and wildcards. Example: "results/[INPUT1]_brain*.mnc".
     * 
     */
    @JsonProperty("path-template")
    public void setPathTemplate(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    /**
     * List of objects containing boolean statement (Limited python syntax: ==, !=, <, >, <=, >=, and, or) and output file paths relative to the execution directory, assign path of first true boolean statement. May contain input value keys, "default" object required if "optional" set to True . Example list: "[{"[PARAM1] > 8": "outputs/[INPUT1].txt"}, {"default": "outputs/default.txt"}]".
     * 
     */
    @JsonProperty("conditional-path-template")
    public List<ConditionalPathTemplate> getConditionalPathTemplate() {
        return conditionalPathTemplate;
    }

    /**
     * List of objects containing boolean statement (Limited python syntax: ==, !=, <, >, <=, >=, and, or) and output file paths relative to the execution directory, assign path of first true boolean statement. May contain input value keys, "default" object required if "optional" set to True . Example list: "[{"[PARAM1] > 8": "outputs/[INPUT1].txt"}, {"default": "outputs/default.txt"}]".
     * 
     */
    @JsonProperty("conditional-path-template")
    public void setConditionalPathTemplate(List<ConditionalPathTemplate> conditionalPathTemplate) {
        this.conditionalPathTemplate = conditionalPathTemplate;
    }

    /**
     * List of file extensions that will be stripped from the input values before being substituted in the path template. Example: [".nii",".nii.gz"].
     * 
     */
    @JsonProperty("path-template-stripped-extensions")
    public List<String> getPathTemplateStrippedExtensions() {
        return pathTemplateStrippedExtensions;
    }

    /**
     * List of file extensions that will be stripped from the input values before being substituted in the path template. Example: [".nii",".nii.gz"].
     * 
     */
    @JsonProperty("path-template-stripped-extensions")
    public void setPathTemplateStrippedExtensions(List<String> pathTemplateStrippedExtensions) {
        this.pathTemplateStrippedExtensions = pathTemplateStrippedExtensions;
    }

    /**
     * True if output is a list of value.
     * 
     */
    @JsonProperty("list")
    public Boolean getList() {
        return list;
    }

    /**
     * True if output is a list of value.
     * 
     */
    @JsonProperty("list")
    public void setList(Boolean list) {
        this.list = list;
    }

    /**
     * True if output may not be produced by the tool.
     * 
     */
    @JsonProperty("optional")
    public Boolean getOptional() {
        return optional;
    }

    /**
     * True if output may not be produced by the tool.
     * 
     */
    @JsonProperty("optional")
    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    /**
     * Option flag of the output, involved in the value-key substitution. Examples: -o, --output
     * 
     */
    @JsonProperty("command-line-flag")
    public String getCommandLineFlag() {
        return commandLineFlag;
    }

    /**
     * Option flag of the output, involved in the value-key substitution. Examples: -o, --output
     * 
     */
    @JsonProperty("command-line-flag")
    public void setCommandLineFlag(String commandLineFlag) {
        this.commandLineFlag = commandLineFlag;
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
     * Specifies that this output filepath will be given as an absolute path.
     * 
     */
    @JsonProperty("uses-absolute-path")
    public Boolean getUsesAbsolutePath() {
        return usesAbsolutePath;
    }

    /**
     * Specifies that this output filepath will be given as an absolute path.
     * 
     */
    @JsonProperty("uses-absolute-path")
    public void setUsesAbsolutePath(Boolean usesAbsolutePath) {
        this.usesAbsolutePath = usesAbsolutePath;
    }

    /**
     * An array of strings that may contain value keys. Each item will be a line in the configuration file.
     * 
     */
    @JsonProperty("file-template")
    public List<String> getFileTemplate() {
        return fileTemplate;
    }

    /**
     * An array of strings that may contain value keys. Each item will be a line in the configuration file.
     * 
     */
    @JsonProperty("file-template")
    public void setFileTemplate(List<String> fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

}
