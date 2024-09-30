
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


/**
 * Tool
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "tool-version",
    "description",
    "deprecated-by-doi",
    "author",
    "url",
    "descriptor-url",
    "doi",
    "shell",
    "tool-doi",
    "command-line",
    "container-image",
    "schema-version",
    "environment-variables",
    "groups",
    "inputs",
    "tests",
    "online-platform-urls",
    "output-files",
    "invocation-schema",
    "suggested-resources",
    "tags",
    "error-codes",
    "custom"
})
@Generated("jsonschema2pojo")
public class BoutiquesDescriptor {

    /**
     * Tool name.
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Tool name.")
    private String name;
    /**
     * Tool version.
     * (Required)
     * 
     */
    @JsonProperty("tool-version")
    @JsonPropertyDescription("Tool version.")
    private String toolVersion;
    /**
     * Tool description.
     * (Required)
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Tool description.")
    private String description;
    /**
     * doi of the tool that deprecates the current one. May be set to 'true' if the current tool is deprecated but no specific tool deprecates it.
     * 
     */
    @JsonProperty("deprecated-by-doi")
    @JsonPropertyDescription("doi of the tool that deprecates the current one. May be set to 'true' if the current tool is deprecated but no specific tool deprecates it.")
    private String deprecatedByDoi;
    /**
     * Tool author name(s).
     * 
     */
    @JsonProperty("author")
    @JsonPropertyDescription("Tool author name(s).")
    private String author;
    /**
     * Tool URL.
     * 
     */
    @JsonProperty("url")
    @JsonPropertyDescription("Tool URL.")
    private String url;
    /**
     * Link to the descriptor itself (e.g. the GitHub repo where it is hosted).
     * 
     */
    @JsonProperty("descriptor-url")
    @JsonPropertyDescription("Link to the descriptor itself (e.g. the GitHub repo where it is hosted).")
    private String descriptorUrl;
    /**
     * DOI of the descriptor (not of the tool itself).
     * 
     */
    @JsonProperty("doi")
    @JsonPropertyDescription("DOI of the descriptor (not of the tool itself).")
    private String doi;
    /**
     * Absolute path of the shell interpreter to use in the container (defaults to /bin/sh).
     * 
     */
    @JsonProperty("shell")
    @JsonPropertyDescription("Absolute path of the shell interpreter to use in the container (defaults to /bin/sh).")
    private String shell;
    /**
     * DOI of the tool (not of the descriptor).
     * 
     */
    @JsonProperty("tool-doi")
    @JsonPropertyDescription("DOI of the tool (not of the descriptor).")
    private String toolDoi;
    /**
     * A string that describes the tool command line, where input and output values are identified by "keys". At runtime, command-line keys are substituted with flags and values.
     * (Required)
     * 
     */
    @JsonProperty("command-line")
    @JsonPropertyDescription("A string that describes the tool command line, where input and output values are identified by \"keys\". At runtime, command-line keys are substituted with flags and values.")
    private String commandLine;
    @JsonProperty("container-image")
    private ContainerImage containerImage;
    /**
     * Version of the schema used.
     * (Required)
     * 
     */
    @JsonProperty("schema-version")
    @JsonPropertyDescription("Version of the schema used.")
    private BoutiquesDescriptor.SchemaVersion schemaVersion;
    /**
     * An array of key-value pairs specifying environment variable names and their values to be used in the execution environment.
     * 
     */
    @JsonProperty("environment-variables")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("An array of key-value pairs specifying environment variable names and their values to be used in the execution environment.")
    private Set<EnvironmentVariable> environmentVariables = new LinkedHashSet<EnvironmentVariable>();
    /**
     * Sets of identifiers of inputs, each specifying an input group.
     * 
     */
    @JsonProperty("groups")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("Sets of identifiers of inputs, each specifying an input group.")
    private Set<Group> groups = new LinkedHashSet<Group>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputs")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Input> inputs = new LinkedHashSet<Input>();
    @JsonProperty("tests")
    private List<Test> tests = new ArrayList<Test>();
    /**
     * Online platform URLs from which the tool can be executed.
     * 
     */
    @JsonProperty("online-platform-urls")
    @JsonPropertyDescription("Online platform URLs from which the tool can be executed.")
    private List<String> onlinePlatformUrls = new ArrayList<String>();
    @JsonProperty("output-files")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<OutputFile> outputFiles = new LinkedHashSet<OutputFile>();
    @JsonProperty("invocation-schema")
    private InvocationSchema invocationSchema;
    @JsonProperty("suggested-resources")
    private SuggestedResources suggestedResources;
    /**
     * A set of key-value pairs specifying tags describing the pipeline. The tag names are open, they might be more constrained in the future.
     * 
     */
    @JsonProperty("tags")
    @JsonPropertyDescription("A set of key-value pairs specifying tags describing the pipeline. The tag names are open, they might be more constrained in the future.")
    private Tags tags;
    /**
     * An array of key-value pairs specifying exit codes and their description. Can be used for tools to specify the meaning of particular exit codes. Exit code 0 is assumed to indicate a successful execution.
     * 
     */
    @JsonProperty("error-codes")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("An array of key-value pairs specifying exit codes and their description. Can be used for tools to specify the meaning of particular exit codes. Exit code 0 is assumed to indicate a successful execution.")
    private Set<ErrorCode> errorCodes = new LinkedHashSet<ErrorCode>();
    @JsonProperty("custom")
    private Custom custom;

    /**
     * Tool name.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Tool name.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Tool version.
     * (Required)
     * 
     */
    @JsonProperty("tool-version")
    public String getToolVersion() {
        return toolVersion;
    }

    /**
     * Tool version.
     * (Required)
     * 
     */
    @JsonProperty("tool-version")
    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    /**
     * Tool description.
     * (Required)
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Tool description.
     * (Required)
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * doi of the tool that deprecates the current one. May be set to 'true' if the current tool is deprecated but no specific tool deprecates it.
     * 
     */
    @JsonProperty("deprecated-by-doi")
    public String getDeprecatedByDoi() {
        return deprecatedByDoi;
    }

    /**
     * doi of the tool that deprecates the current one. May be set to 'true' if the current tool is deprecated but no specific tool deprecates it.
     * 
     */
    @JsonProperty("deprecated-by-doi")
    public void setDeprecatedByDoi(String deprecatedByDoi) {
        this.deprecatedByDoi = deprecatedByDoi;
    }

    /**
     * Tool author name(s).
     * 
     */
    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    /**
     * Tool author name(s).
     * 
     */
    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Tool URL.
     * 
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * Tool URL.
     * 
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Link to the descriptor itself (e.g. the GitHub repo where it is hosted).
     * 
     */
    @JsonProperty("descriptor-url")
    public String getDescriptorUrl() {
        return descriptorUrl;
    }

    /**
     * Link to the descriptor itself (e.g. the GitHub repo where it is hosted).
     * 
     */
    @JsonProperty("descriptor-url")
    public void setDescriptorUrl(String descriptorUrl) {
        this.descriptorUrl = descriptorUrl;
    }

    /**
     * DOI of the descriptor (not of the tool itself).
     * 
     */
    @JsonProperty("doi")
    public String getDoi() {
        return doi;
    }

    /**
     * DOI of the descriptor (not of the tool itself).
     * 
     */
    @JsonProperty("doi")
    public void setDoi(String doi) {
        this.doi = doi;
    }

    /**
     * Absolute path of the shell interpreter to use in the container (defaults to /bin/sh).
     * 
     */
    @JsonProperty("shell")
    public String getShell() {
        return shell;
    }

    /**
     * Absolute path of the shell interpreter to use in the container (defaults to /bin/sh).
     * 
     */
    @JsonProperty("shell")
    public void setShell(String shell) {
        this.shell = shell;
    }

    /**
     * DOI of the tool (not of the descriptor).
     * 
     */
    @JsonProperty("tool-doi")
    public String getToolDoi() {
        return toolDoi;
    }

    /**
     * DOI of the tool (not of the descriptor).
     * 
     */
    @JsonProperty("tool-doi")
    public void setToolDoi(String toolDoi) {
        this.toolDoi = toolDoi;
    }

    /**
     * A string that describes the tool command line, where input and output values are identified by "keys". At runtime, command-line keys are substituted with flags and values.
     * (Required)
     * 
     */
    @JsonProperty("command-line")
    public String getCommandLine() {
        return commandLine;
    }

    /**
     * A string that describes the tool command line, where input and output values are identified by "keys". At runtime, command-line keys are substituted with flags and values.
     * (Required)
     * 
     */
    @JsonProperty("command-line")
    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    @JsonProperty("container-image")
    public ContainerImage getContainerImage() {
        return containerImage;
    }

    @JsonProperty("container-image")
    public void setContainerImage(ContainerImage containerImage) {
        this.containerImage = containerImage;
    }

    /**
     * Version of the schema used.
     * (Required)
     * 
     */
    @JsonProperty("schema-version")
    public BoutiquesDescriptor.SchemaVersion getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Version of the schema used.
     * (Required)
     * 
     */
    @JsonProperty("schema-version")
    public void setSchemaVersion(BoutiquesDescriptor.SchemaVersion schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * An array of key-value pairs specifying environment variable names and their values to be used in the execution environment.
     * 
     */
    @JsonProperty("environment-variables")
    public Set<EnvironmentVariable> getEnvironmentVariables() {
        return environmentVariables;
    }

    /**
     * An array of key-value pairs specifying environment variable names and their values to be used in the execution environment.
     * 
     */
    @JsonProperty("environment-variables")
    public void setEnvironmentVariables(Set<EnvironmentVariable> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    /**
     * Sets of identifiers of inputs, each specifying an input group.
     * 
     */
    @JsonProperty("groups")
    public Set<Group> getGroups() {
        return groups;
    }

    /**
     * Sets of identifiers of inputs, each specifying an input group.
     * 
     */
    @JsonProperty("groups")
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputs")
    public Set<Input> getInputs() {
        return inputs;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("inputs")
    public void setInputs(Set<Input> inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("tests")
    public List<Test> getTests() {
        return tests;
    }

    @JsonProperty("tests")
    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    /**
     * Online platform URLs from which the tool can be executed.
     * 
     */
    @JsonProperty("online-platform-urls")
    public List<String> getOnlinePlatformUrls() {
        return onlinePlatformUrls;
    }

    /**
     * Online platform URLs from which the tool can be executed.
     * 
     */
    @JsonProperty("online-platform-urls")
    public void setOnlinePlatformUrls(List<String> onlinePlatformUrls) {
        this.onlinePlatformUrls = onlinePlatformUrls;
    }

    @JsonProperty("output-files")
    public Set<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    @JsonProperty("output-files")
    public void setOutputFiles(Set<OutputFile> outputFiles) {
        this.outputFiles = outputFiles;
    }

    @JsonProperty("invocation-schema")
    public InvocationSchema getInvocationSchema() {
        return invocationSchema;
    }

    @JsonProperty("invocation-schema")
    public void setInvocationSchema(InvocationSchema invocationSchema) {
        this.invocationSchema = invocationSchema;
    }

    @JsonProperty("suggested-resources")
    public SuggestedResources getSuggestedResources() {
        return suggestedResources;
    }

    @JsonProperty("suggested-resources")
    public void setSuggestedResources(SuggestedResources suggestedResources) {
        this.suggestedResources = suggestedResources;
    }

    /**
     * A set of key-value pairs specifying tags describing the pipeline. The tag names are open, they might be more constrained in the future.
     * 
     */
    @JsonProperty("tags")
    public Tags getTags() {
        return tags;
    }

    /**
     * A set of key-value pairs specifying tags describing the pipeline. The tag names are open, they might be more constrained in the future.
     * 
     */
    @JsonProperty("tags")
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    /**
     * An array of key-value pairs specifying exit codes and their description. Can be used for tools to specify the meaning of particular exit codes. Exit code 0 is assumed to indicate a successful execution.
     * 
     */
    @JsonProperty("error-codes")
    public Set<ErrorCode> getErrorCodes() {
        return errorCodes;
    }

    /**
     * An array of key-value pairs specifying exit codes and their description. Can be used for tools to specify the meaning of particular exit codes. Exit code 0 is assumed to indicate a successful execution.
     * 
     */
    @JsonProperty("error-codes")
    public void setErrorCodes(Set<ErrorCode> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @JsonProperty("custom")
    public Custom getCustom() {
        return custom;
    }

    @JsonProperty("custom")
    public void setCustom(Custom custom) {
        this.custom = custom;
    }


    /**
     * Version of the schema used.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum SchemaVersion {

        _0_5("0.5");
        private final String value;
        private final static Map<String, BoutiquesDescriptor.SchemaVersion> CONSTANTS = new HashMap<String, BoutiquesDescriptor.SchemaVersion>();

        static {
            for (BoutiquesDescriptor.SchemaVersion c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SchemaVersion(String value) {
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
        public static BoutiquesDescriptor.SchemaVersion fromValue(String value) {
            BoutiquesDescriptor.SchemaVersion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
