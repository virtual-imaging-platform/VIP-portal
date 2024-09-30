
package fr.insalyon.creatis.vip.application.server.model.boutiques;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "value",
    "description"
})
@Generated("jsonschema2pojo")
public class EnvironmentVariable {

    /**
     * The environment variable name (identifier) containing only alphanumeric characters and underscores. Example: "PROGRAM_PATH".
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("The environment variable name (identifier) containing only alphanumeric characters and underscores. Example: \"PROGRAM_PATH\".")
    private String name;
    /**
     * The value of the environment variable.
     * (Required)
     * 
     */
    @JsonProperty("value")
    @JsonPropertyDescription("The value of the environment variable.")
    private String value;
    /**
     * Description of the environment variable.
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Description of the environment variable.")
    private String description;

    /**
     * The environment variable name (identifier) containing only alphanumeric characters and underscores. Example: "PROGRAM_PATH".
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * The environment variable name (identifier) containing only alphanumeric characters and underscores. Example: "PROGRAM_PATH".
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The value of the environment variable.
     * (Required)
     * 
     */
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    /**
     * The value of the environment variable.
     * (Required)
     * 
     */
    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Description of the environment variable.
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description of the environment variable.
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

}
